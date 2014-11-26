/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.location.Address;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Uses the Nominatim service to resolve the location details.
 *<p/>
 *
 * NOTE: The terms of use (http://wiki.openstreetmap.org/wiki/Nominatim_usage_policy) for this service are fairly limiting:
 * <ul>
 *   <li>No heavy uses (an absolute maximum of 1 request/s).
 *   <li>Provide a valid HTTP Referer or User-Agent identifying the application (stock User-Agents as set by http libraries will not do).
 * </ul>
 *
 * The syntax for querying the Nominatim web service is described here: http://wiki.openstreetmap.org/wiki/Nominatim.
 *  Basically, there are two ways of querying:
 *  <ul>
 *    <li>search: format=json&q=[query goes here]
 *    <li>reverse: format=json&lat=[latitude]&lon=[longitude]
 *  </ul>
 */
class NominatimResolver
	extends LocationResolver {

	/** URL to make the request. */
	private static final String                               NOMINATIM_URL = "http://nominatim.openstreetmap.org/";

	/** The various operations which can be sent. */
	private static final String                               SEARCH_OPERATION = "search";
	private static final String                               REVERSE_LOOKUP_OPERATION = "reverse";

	/** Parameters to send when making the request. */
	private static final String                               ADDRESS_DETAILS_PARAM = "addressdetails";
	private static final String                               EMAIL_PARAM = "email";
	private static final String                               FORMAT_PARAM = "format";
	private static final String                               LATITUDE_PARAM = "lat";
	private static final String                               LONGITUDE_PARAM = "lon";
	private static final String                               QUERY_PARAM = "q";

	/** Fields which come back in the JSON. */
	private static final String                               ADDRESS = "address";
	private static final String                               CITY = "city";
	private static final String                               COUNTRY = "country";
	private static final String                               COUNTRY_CODE = "country_code";
	private static final String                               POSTAL_CODE = "postcode";
	private static final String                               SUBURB = "suburb";
	private static final String                               STATE = "state";

	// Other fields (not currently used) that could be returned.
/*
	private static final String                               HOUSE_NUMBER = "house_number";
	private static final String                               ROAD = "road";
*/


	static {

		// Work around pre-Froyo bugs in HTTP connection reuse. See
		//  http://developer.android.com/reference/java/net/HttpURLConnection.html
		// If this is null, then we're running some unit tests...
		if ( ( Build.VERSION.SDK != null ) && Integer.parseInt( Build.VERSION.SDK ) < Build.VERSION_CODES.FROYO ) {
			System.setProperty( "http.keepAlive", "false" );
		}

	}



	NominatimResolver(
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {
		super( eventBus, personalPrefs );
	}


	@Override
	public void onDestroy() {

	}


	@Nullable
	@Override
	protected ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation ) {

		ZmanimLocation resolvedLocation;

		try {
			List<Pair<String,String>> params = new ArrayList<Pair<String, String>>();
			params.add( new Pair<String, String>( LATITUDE_PARAM, Double.toString( zmanimLocation.getLatitude() ) ) );
			params.add( new Pair<String, String>( LONGITUDE_PARAM, Double.toString( zmanimLocation.getLongitude() ) ) );

			JSONObject json = makeRequest( REVERSE_LOOKUP_OPERATION, params );
			resolvedLocation = buildLocation( zmanimLocation, json );
		}
		catch ( Exception e ) {
			LogUtils.logError( "Nomatim::fromCoords", e.getMessage(), e );
			return null;
		}

		return resolvedLocation;
	}


	@Nullable
	@Override
	protected ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation ) {

		ZmanimLocation resolvedLocation;

		try {
			List<Pair<String,String>> params = new ArrayList<Pair<String, String>>();
			params.add( new Pair<String, String>( QUERY_PARAM, zmanimLocation.getLocationName() ) );

			JSONObject json = makeRequest( SEARCH_OPERATION, params );
			resolvedLocation = buildLocation( zmanimLocation, json );
		}
		catch ( Exception e ) {
			LogUtils.logError( "Nomatim::fromName", e.getMessage(), e );
			return null;
		}

		return resolvedLocation;
	}


	private JSONObject makeRequest( String operation, List<Pair<String,String>> params ) throws Exception {

		// We know we always want JSON returned and we need all the gory details..
		params.add( new Pair<String, String>( FORMAT_PARAM, "json" ) );
		params.add( new Pair<String, String>( ADDRESS_DETAILS_PARAM, "1" ) );
		params.add( new Pair<String, String>( EMAIL_PARAM, "zmanim.apps@gmail.com" ) );

		StringBuilder queryParams = new StringBuilder();
		for ( int index = 0; index < params.size(); index++ ) {

			queryParams.append( ( index == 0 ) ? "?" : "&");

			Pair<String,String> param = params.get( index );
			queryParams.append( URLEncoder.encode( param.first ) ).append( "=" ).append( URLEncoder.encode( param.second ) );
		}

		URL url = new URL( NOMINATIM_URL + operation + queryParams.toString() );
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput( true );

			InputStream input = new BufferedInputStream( connection.getInputStream() );
			if ( !url.getHost().equals( connection.getURL().getHost() ) ) {
				throw new IOException( "Unexpected redirection." );
			}

			StringBuilder response = new StringBuilder();
			//noinspection MagicNumber
			byte[] buf = new byte[ 1024 ];
			while ( input.read( buf ) != -1 ) {
				response.append( new String( buf ) );
			}

			Object jsonRoot = new JSONTokener( response.toString() ).nextValue();
			if ( jsonRoot instanceof JSONArray ) {
				jsonRoot = ((JSONArray)jsonRoot).getJSONObject( 0 );
			}

			if ( jsonRoot instanceof JSONObject ) {
				return (JSONObject)jsonRoot;
			}

			return null;
		}
		finally {
			if ( connection != null ) {
				connection.disconnect();
			}
		}
	}


	@SuppressWarnings( "FeatureEnvy" )
	private ZmanimLocation buildLocation(
		ZmanimLocation  zmanimLocation,
		JSONObject      jsonRoot
	) {

		try {

			JSONObject jsonAddress = jsonRoot.getJSONObject( ADDRESS );

			Address address = new Address( Locale.getDefault() );
			address.setCountryCode( jsonAddress.getString( COUNTRY_CODE ) );
			address.setCountryName( jsonAddress.getString( COUNTRY ) );
			address.setLatitude( jsonRoot.getDouble( LATITUDE_PARAM ) );
			address.setLongitude( jsonRoot.getDouble( LONGITUDE_PARAM ) );

			String locality = jsonAddress.optString( CITY, null );
			if ( locality == null ) {
				locality = jsonAddress.optString( SUBURB, null );
			}
			else {
				address.setSubLocality( jsonAddress.optString( SUBURB, null ) );
			}

			address.setLocality( locality );
			address.setAdminArea( jsonAddress.getString( STATE ) );
			address.setPostalCode( jsonAddress.optString( POSTAL_CODE, null ) );

			return new ZmanimLocation.Builder()
				.from( zmanimLocation )
				.resolvedTo( address )
				.build();
		}
		catch ( Exception e ) {
			return null;
		}
	}


} // End of NominatimResolver class
