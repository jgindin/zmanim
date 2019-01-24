/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.Pair;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.json.JSONObject;

import android.content.Context;
import android.location.Address;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * Uses the Nominatim service to resolve the location details.
 *<p/>
 *
 * The syntax for querying the Nominatim web service is described here: http://developer.mapquest.com/web/products/open/nominatim
 *  Basically, there are two ways of querying:
 *  <ul>
 *    <li>search: format=json&q=[query goes here]
 *    <li>reverse: format=json&lat=[latitude]&lon=[longitude]
 *  </ul>
 */
class NominatimResolver
  extends JsonQueryingResolver {

  /** URL to make the request. */
  private static final String                               NOMINATIM_URL = "http://open.mapquestapi.com/nominatim/v1/";

  /** The various operations which can be sent. */
  private static final String                               SEARCH_OPERATION = "search.php";
  private static final String                               REVERSE_LOOKUP_OPERATION = "reverse.php";

  private static final String                               LATITUDE_PARAM = "lat";
  private static final String                               LONGITUDE_PARAM = "lon";

  // Other fields (not currently used) that could be returned.
/*
  private static final String                               HOUSE_NUMBER = "house_number";
  private static final String                               ROAD = "road";
*/



  NominatimResolver(
    Context context
  ) {
    super( context );
  }


  @Override
  public void onDestroy() {

  }


  @Override
  protected void fillInLatitudeAndLongitude(
    String                      latitude,
    String                      longitude,
    List<Pair<String, String>>  params
    ) {

    params.add( new Pair<>( LATITUDE_PARAM, latitude ) );
    params.add( new Pair<>( LONGITUDE_PARAM, longitude ) );
  }


  @Override
  protected void fillInSearch(
    ZmanimLocation              location,
    List<Pair<String, String>>  params
  ) {

    params.add( new Pair<>( "q", URLEncoder.encode( location.getLocationName() ) ) );
  }


  @Override
  protected void addAdditionalParams(
    Operation                   operation,
    ZmanimLocation              unresolvedLocation,
    List<Pair<String, String>>  params
  ) {

    params.add( new Pair<>( "key", BuildConfig.MAPQUEST_KEY ) );

    // We want all the details.
    params.add( new Pair<>( "addressdetails", "1" ) );

    params.add( new Pair<>( "limit", "1" ) );

    // We know we always want JSON returned
    params.add( new Pair<>( "format", "json" ) );
    params.add( new Pair<>( "email", "zmanim.apps@gmail.com" ) );
  }


  @Override
  protected String getRequestUrl( Operation operation ) {

    if ( operation == Operation.ReverseGeocode ) {
      return NOMINATIM_URL + REVERSE_LOOKUP_OPERATION;
    }
    else {
      return NOMINATIM_URL + SEARCH_OPERATION;
    }
  }


  @Override
  protected Address addressFromJSON( JSONObject jsonRoot ) {

    try {

      JSONObject jsonAddress = jsonRoot.getJSONObject( "address" );

      Address address = new Address( Locale.getDefault() );
      address.setCountryCode( jsonAddress.getString( "country_code" ) );
      address.setCountryName( jsonAddress.getString( "country" ) );
      address.setLatitude( jsonRoot.getDouble( LATITUDE_PARAM ) );
      address.setLongitude( jsonRoot.getDouble( LONGITUDE_PARAM ) );

      String postcode = jsonAddress.optString( "postcode", null );
      String city = jsonAddress.optString( "city", null );
      String suburb = jsonAddress.optString( "suburb", null );
      if ( ( city == null ) || ( city.equals( postcode ) ) ) {
        city = suburb;
      }
      else {
        address.setSubLocality( suburb );
      }

      address.setLocality( city );
      address.setAdminArea( jsonAddress.getString( "state" ) );
      address.setPostalCode( postcode );

      return address;
    }
    catch ( Exception e ) {
      // Don't log an error...it can fail if we don't get anything back.
      //LogUtils.logError( "NominatimResolver::addressFromJSON", e.getMessage(), e );
      return null;
    }
  }


} // End of NominatimResolver class
