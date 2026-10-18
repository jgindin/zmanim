/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import kotlin.Pair;
import com.gindin.zmanlib.location.ZmanimLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import kotlinx.serialization.SerializationException;
import kotlinx.serialization.json.JsonObject;

import android.location.Address;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the basics needed for querying a web service to get back a JSON-formatted address resolution.
 */
abstract class JsonQueryingResolver
  extends LocationResolver {


  /**
   * Determine which operation is being invoked.
   */
  protected enum Operation {
    ReverseGeocode,
    Search
  }


  static {

    // Work around pre-Froyo bugs in HTTP connection reuse. See
    //  http://developer.android.com/reference/java/net/HttpURLConnection.html
    // If this is null, then we're running some unit tests...
    if ( ( Build.VERSION.SDK != null ) && Integer.parseInt( Build.VERSION.SDK ) < Build.VERSION_CODES.FROYO ) {
      System.setProperty( "http.keepAlive", "false" );
    }
  }



  @Nullable
  @Override
  protected final ZmanimLocation resolveFromCoordinates( @NonNull ZmanimLocation zmanimLocation ) {

    List<Pair<String,String>> params = new ArrayList<Pair<String, String>>();
    fillInLatitudeAndLongitude( Double.toString( zmanimLocation.getLatitude() ),
      Double.toString( zmanimLocation.getLongitude() ), params );

    return resolveLocation( zmanimLocation, params );
  }


  @Nullable
  @Override
  protected final ZmanimLocation resolveFromName( @NonNull ZmanimLocation zmanimLocation ) {

    List<Pair<String,String>> params = new ArrayList<Pair<String, String>>();
    fillInSearch( zmanimLocation, params );

    return resolveLocation( zmanimLocation, params );
  }


  /**
   * Called to add the latitude and longitude as name/value pairs for when we're doing a reverse geocode.
   */
  protected abstract void fillInLatitudeAndLongitude(
    String                      latitude,
    String                      longitude,
    List<Pair<String, String>>  params
    );


  protected abstract void fillInSearch(
    ZmanimLocation              location,
    List<Pair<String, String>>  params
  );


  /**
   * Called to add parameters specific to the operation as name/value pairs.
   */
  protected abstract void addAdditionalParams(
    Operation                   operation,
    ZmanimLocation              unresolvedLocation,
    List<Pair<String, String>>  params
  );


  /**
   * Called to get the URL to make the request, which does NOT include any query parameters.
   */
  protected abstract String getRequestUrl( Operation operation );


  /**
   * Called to convert the JSON retrieved from the web service into an address object that we can then convert
   *  into a ZmanimLocation.
   */
  @Nullable
  protected abstract Address addressFromJSON( JsonObject json );



  @Nullable
  private ZmanimLocation resolveLocation(
    ZmanimLocation              zmanimLocation,
    List<Pair<String, String>>  params
  ) {

    // In case we get a connection refused, we'll try a couple of times...
    for ( int attempts = 0; attempts < 3; attempts++ ) {

      try {
        return doLocationResolution( zmanimLocation, params );
      }
      catch ( IOException | SerializationException | NumberFormatException ex ) {
        // Try again; don't log anything because it can be overwhelming...
        //  Parse failures (an empty or truncated response, for example) happen pretty often.
      }
      catch ( Exception e ) {
        LogUtils.logError( "resolveFromCoords", e.getMessage(), e );
        return null;
      }

    }

    return null;
  }


  @Nullable
  @SuppressWarnings( "FeatureEnvy" )
  private ZmanimLocation doLocationResolution(
    ZmanimLocation              zmanimLocation,
    List<Pair<String, String>>  params
  )
    throws Exception {

    Operation operation = zmanimLocation.hasCoordinates() ? Operation.ReverseGeocode : Operation.Search;
    addAdditionalParams( operation, zmanimLocation, params );

    JsonObject json = makeRequest( getRequestUrl( operation ), params );
    if ( json == null ) {
      return null;
    }

    Address address = addressFromJSON( json );

    if ( address != null ) {
      return new ZmanimLocation.Builder()
        .from( zmanimLocation )
        .locationName( addressToLocationName( address ) )
        .atLatitude( address.getLatitude() )
        .atLongitude( address.getLongitude() )
        .build();
    }

    return null;
  }


  @Nullable
  private JsonObject makeRequest(
    String                    urlString,
    List<Pair<String,String>> params
  )
    throws Exception {

    StringBuilder queryParams = new StringBuilder();
    for ( int index = 0; index < params.size(); index++ ) {

      queryParams.append( ( index == 0 ) ? "?" : "&");

      Pair<String,String> param = params.get( index );
      queryParams.append( param.getFirst() ).append( "=" ).append( param.getSecond() );
    }

    URL url = new URL( urlString + queryParams.toString() );
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection)url.openConnection();
      connection.setDoInput( true );

      InputStream input = new BufferedInputStream( connection.getInputStream() );
      if ( !url.getHost().equals( connection.getURL().getHost() ) ) {
        throw new IOException( "Unexpected redirection." );
      }

      ByteArrayOutputStream response = new ByteArrayOutputStream();
      //noinspection MagicNumber
      byte[] buf = new byte[ 1024 ];
      int count;
      while ( ( count = input.read( buf ) ) != -1 ) {
        response.write( buf, 0, count );
      }

      return GeocodingResponseParser.parse( new String( response.toByteArray(), StandardCharsets.UTF_8 ) );
    }
    finally {
      if ( connection != null ) {
        connection.disconnect();
      }
    }

  }

}
