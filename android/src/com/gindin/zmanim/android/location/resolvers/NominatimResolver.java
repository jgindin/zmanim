/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import android.location.Address;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.location.ZmanimLocation;
import org.json.JSONObject;

import java.net.URLEncoder;
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
	extends JsonQueryingResolver {

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



	NominatimResolver(
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {
		super( eventBus, personalPrefs );
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

		params.add( new Pair<String, String>( LATITUDE_PARAM, latitude ) );
		params.add( new Pair<String, String>( LONGITUDE_PARAM, longitude ) );
	}


	@Override
	protected void fillInSearch(
		ZmanimLocation              location,
		List<Pair<String, String>>  params
	) {

		params.add( new Pair<String, String>( QUERY_PARAM, URLEncoder.encode( location.getLocationName() ) ) );
	}


	@Override
	protected void addAdditionalParams(
		Operation                   operation,
		ZmanimLocation              unresolvedLocation,
		List<Pair<String, String>>  params
	) {

		if ( operation == Operation.ReverseGeocode ) {
			// We want all the details.
			params.add( new Pair<String, String>( ADDRESS_DETAILS_PARAM, "1" ) );
		}
		else {
			// Nothing else here.
		}

		// We know we always want JSON returned
		params.add( new Pair<String, String>( FORMAT_PARAM, "json" ) );
		params.add( new Pair<String, String>( EMAIL_PARAM, "zmanim.apps@gmail.com" ) );
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

			return address;
		}
		catch ( Exception e ) {
			LogUtils.logError( "NominatimResolver::addressFromJSON", e.getMessage(), e );
			return null;
		}
	}


} // End of NominatimResolver class
