package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.Pair;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * The API for accessing the Geocoding WebService is described here: http://www.mapquestapi.com/geocoding/.
 *
 * The result of a Geocode operation is described in a 5-character code as described here: http://www.mapquestapi.com/geocoding/geocodequality.html
 */
class MapQuestResolver
  extends JsonQueryingResolver {



  MapQuestResolver(
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
    List<Pair<String, String>>   params
  ) {

    params.add( new Pair<>( "lat", latitude ) );
    params.add( new Pair<>( "lng", longitude ) );
  }


  @Override
  protected void fillInSearch(
    ZmanimLocation              location,
    List<Pair<String, String>>  params
  ) {

    params.add( new Pair<>( "location", URLEncoder.encode( location.getLocationName() ) ) );
  }


  @Override
  protected void addAdditionalParams(
    Operation                   operation,
    ZmanimLocation              unresolvedLocation,
    List<Pair<String, String>>  params
  ) {

    params.add( new Pair<>( "key", BuildConfig.MAPQUEST_KEY ) );
    params.add( new Pair<>( "outFormat", "json" ) );
    params.add( new Pair<>( "thumbMaps", "false" ) );
    params.add( new Pair<>( "maxResults", "1" ) );
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
      String countryName = location.optString( "adminArea1" );
      // Special case handling for Israel...
      if ( "IL".equals( countryName ) ) {
        address.setCountryName( "Israel" );
      }
      else {
        address.setCountryName( countryName );
        address.setAdminArea( location.optString( "adminArea3" ) );     // State
      }

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
      // Don't log an error...it can fail if we don't get anything back.
      //LogUtils.logError( "MapQuestResolver::addressFromJSON", e.getMessage(), e );
      return null;
    }
  }


}
