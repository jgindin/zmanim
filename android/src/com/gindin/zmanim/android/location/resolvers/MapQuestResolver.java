package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import android.location.Address;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.location.ZmanimLocation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * The API for accessing the Geocoding WebService is described here: http://www.mapquestapi.com/geocoding/.
 *
 * The result of a Geocode operation is described in a 5-character code as described here: http://www.mapquestapi.com/geocoding/geocodequality.html
 */
class MapQuestResolver
	extends JsonQueryingResolver {

	/** Filled in with the real key at build time. */
	private static final String                               MAP_QUEST_KEY = "MAP_QUEST_KEY_GOES_HERE";



	MapQuestResolver(
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
		List<Pair<String, String>>   params
	) {

		params.add( new Pair<String, String>( "lat", latitude ) );
		params.add( new Pair<String, String>( "lng", longitude ) );
	}


	@Override
	protected void fillInSearch(
		ZmanimLocation              location,
		List<Pair<String, String>>  params
	) {

	}


	@Override
	protected void addAdditionalParams(
		Operation                   operation,
		ZmanimLocation              unresolvedLocation,
		List<Pair<String, String>>  params
	) {

		params.add( new Pair<String, String>( "key", MAP_QUEST_KEY ) );
		params.add( new Pair<String, String>( "outFormat", "json" ) );
		params.add( new Pair<String, String>( "thumbMaps", "false" ) );
	}


	@Override
	protected String getRequestUrl( Operation operation ) {
		if ( operation == Operation.ReverseGeocode ) {
			return "http://www.mapquestapi.com/geocoding/v1/reverse";
		}
		else {
			return "http://www.mapquestapi.com/geocoding/v1/address";
		}
	}


	@Override
	protected Address addressFromJSON( JSONObject json ) {

		try {
			JSONArray results = json.getJSONArray( "results" );
			if ( results.length() == 0 ) {
				return null;
			}

			JSONObject result = results.optJSONObject( 0 );
			JSONArray locations = result.getJSONArray( "locations" );
			if ( locations.length() == 0 ) {
				return null;
			}

			JSONObject location = locations.getJSONObject( 0 );

			Address address = new Address( Locale.getDefault() );
			address.setCountryName( location.optString( "adminArea1" ) );   // Country
			address.setAdminArea( location.optString( "adminArea3" ) );     // State
			address.setSubAdminArea( location.optString( "adminArea4" ) );  // County
			address.setLocality( location.optString( "adminArea5" ) );      // City
			address.setSubLocality( location.optString( "adminArea6" ) );   // Neighborhood
			address.setPostalCode( location.optString( "postalCode" ) );

			JSONObject latLong = location.getJSONObject( "latLng" );
			address.setLatitude( latLong.getDouble( "lat" ) );
			address.setLongitude( latLong.getDouble( "lng" ) );

			return address;

		}
		catch ( Exception e ) {
			LogUtils.logError( "MapQuestResolver::addressFromJSON", e.getMessage(), e );
			return null;
		}
	}


}
