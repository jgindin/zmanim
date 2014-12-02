package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
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



	JsonQueryingResolver(
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {
		super( eventBus, personalPrefs );
	}


	@Nullable
	@Override
	protected final ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation ) {

		List<Pair<String,String>> params = new ArrayList<Pair<String, String>>();
		fillInLatitudeAndLongitude( Double.toString( zmanimLocation.getLatitude() ),
			Double.toString( zmanimLocation.getLongitude() ), params );

		return resolveLocation( zmanimLocation, params );
	}


	@Nullable
	@Override
	protected final ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation ) {

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
	protected abstract Address addressFromJSON( JSONObject json );



	private ZmanimLocation resolveLocation(
		ZmanimLocation              zmanimLocation,
		List<Pair<String, String>>  params
	) {

		try {
			Operation operation = zmanimLocation.hasCoordinates() ? Operation.ReverseGeocode : Operation.Search;
			addAdditionalParams( operation, zmanimLocation, params );

			JSONObject json = makeRequest( getRequestUrl( operation ), params );
			if ( json == null ) {
				return null;
			}

			Address address = addressFromJSON( json );

			return new ZmanimLocation.Builder()
				.from( zmanimLocation )
				.resolvedTo( address )
				.build();
		}
		catch ( Exception e ) {
			LogUtils.logError( "resolveFromCoords", e.getMessage(), e );
		}

		return null;
	}



	private JSONObject makeRequest(
		String                    urlString,
		List<Pair<String,String>> params
	)
		throws Exception {

		StringBuilder queryParams = new StringBuilder();
		for ( int index = 0; index < params.size(); index++ ) {

			queryParams.append( ( index == 0 ) ? "?" : "&");

			Pair<String,String> param = params.get( index );
			queryParams.append( param.first ).append( "=" ).append( param.second );
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

			StringBuilder response = new StringBuilder();
			//noinspection MagicNumber
			byte[] buf = new byte[ 1024 ];
			while ( input.read( buf ) != -1 ) {
				response.append( new String( buf ) );
			}

			Object jsonRoot = new JSONTokener( response.toString() ).nextValue();
			if ( jsonRoot instanceof JSONArray ) {
				if ( ((JSONArray)jsonRoot).length() == 0 ) {
					return null;
				}

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

}
