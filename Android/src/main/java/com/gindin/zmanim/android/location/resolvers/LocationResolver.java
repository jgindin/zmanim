/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

/**
 * Uses the information in either a RawLocationAcquiredEvent or PlaceAcquiredEvent to fill out the full set of
 *  data about a given location.
 *<p/>
 *
 * After resolving the location, a FullLocationAcquiredEvent will be fired.
 */
abstract class LocationResolver {


  protected final EventBus                                  eventBus;

  private final SharedPreferences                           personalPrefs;


  protected static String addressToLocationName( Address address ) {

    String locationString = null;

    if ( address == null ) {
      return ZmanimLocation.UNKNOWN_LOCATION;
    }

    final String adminArea = address.getAdminArea();
    final String locality = address.getLocality();
    final String postalCode = address.getPostalCode();
    final int maxAddressLineIndex = address.getMaxAddressLineIndex();
    final String country = address.getCountryName();

    // There are cases where locality is null. No kiddin'...if you use the StaticLocationProvider, and choose
    //  "Brooklyn, New York", the locality comes back null. Go figure...
    //  (Well, that is the case, except that when the StaticLocationProvider goes to look up what the location is
    //    it now stores the returned Address as the Extras in the Location. And, for some reason, *that* Address
    //    comes back properly. Oh well...)
    if ( ( null != locality ) && ( null != adminArea ) ) {
      locationString = locality + ", " + adminArea;
    }
    else if ( null == locality ) {

      if ( maxAddressLineIndex > 0 ) {

        // Find the address line that has the location. In the case mentioned above, address line two is: Brooklyn, NY 11226
        for ( int index = 0; index < maxAddressLineIndex; index++ ) {
          String addrLine = address.getAddressLine( index );
          if ( null == addrLine ) continue;

          if ( ( null != postalCode ) && addrLine.contains( postalCode ) ) {
            locationString = addrLine.substring( 0, addrLine.indexOf( postalCode ) );
            break;
          }
        }
      }
      else if ( null != country ) {
        // Yup. I forced a usage of 55.0, -1.6 and the only info we get back is "United Kingdom".
        locationString = country;
      }

    }
    else if ( null != country ) {
      locationString = locality + ", " + country;
    }
    else {
      locationString = locality;
    }

    // Final fallback...if we got nothing then just use the lat & long...
    if ( null == locationString ) {
      locationString = ZmanimLocation.toLatLongString( address.getLatitude(), address.getLongitude() );
    }

    return locationString;
  }


  protected LocationResolver(
    EventBus          eventBus,
    SharedPreferences personalPrefs
  ) {
    this.eventBus = eventBus;
    this.personalPrefs = personalPrefs;
  }


  /**
   * Called when the resolver is being destroyed.
   */
  public abstract void onDestroy();


  public final void resolve(
    @NotNull ZmanimLocation          location,
    @NotNull List<LocationResolver>  nextResolvers
  ) {

    if ( location.hasCoordinates() ) {
      resolveFromCoordinates( location, nextResolvers );
    }
    else {
      resolveFromName( location, nextResolvers );
    }
  }


  /**
   * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
   *  specified location using the latitude and longitude, and return a new location with all possible details filled out.
   *
   * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
   */
  @Nullable
  protected abstract ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation );

  /**
   * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
   *  specified location using the name, and return a new location with all possible details fillled out.
   *
   * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
   */
  @Nullable
  protected abstract ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation );


  /**
   * Given a location which MUST have the latitude and longitude set, use reverse geocoding to find the
   *  name of that location. Upon completion, the location is updated and the FullLocationAcquiredEvent
   *  will be fired.
   */
  private void resolveFromCoordinates(
    @NotNull final ZmanimLocation          location,
    @NotNull final List<LocationResolver>  nextResolvers
  ) {

    AsyncTask<ZmanimLocation, Void, ZmanimLocation> task = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
      @Override
      protected ZmanimLocation doInBackground( final ZmanimLocation... zmanimLocations ) {
        return resolveFromCoordinatesInBackground( zmanimLocations[0] );
      }


      @Override
      protected void onPostExecute( final ZmanimLocation resolvedLocation ) {
        super.onPostExecute( resolvedLocation );
        handlePostExecute( location, resolvedLocation, nextResolvers );
      }
    };

    task.execute( location );
  }


  /**
   * Given a descriptive place name, use geocoding to find the actual location.
   */
  private void resolveFromName(
    @NotNull final ZmanimLocation          location,
    @NotNull final List<LocationResolver>  nextResolvers
  ) {

    if ( location.getLocationName().trim().length() == 0 ) {
      eventBus.fire( new LocationAcquiredEvent( location, true, new LocationError( LocationError.Code.BAD_INPUT, "Empty place!" ) ) );
    }

    AsyncTask<ZmanimLocation,Void,ZmanimLocation> task = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
      @Override
      protected ZmanimLocation doInBackground( final ZmanimLocation... zmanimLocations ) {
        return resolveFromNameInBackground( zmanimLocations[0] );
      }


      @Override
      protected void onPostExecute( final ZmanimLocation resolvedLocation ) {
        super.onPostExecute( resolvedLocation );
        handlePostExecute( location, resolvedLocation, nextResolvers );
      }
    };

    task.execute( location );
  }


  private void handlePostExecute(
    @NotNull ZmanimLocation                originalLocation,
    @Nullable ZmanimLocation               resolvedLocation,
    @NotNull final List<LocationResolver>  nextResolvers
  ) {

    if ( ( resolvedLocation == null ) && !nextResolvers.isEmpty() ) {
      nextResolvers.get( 0 ).resolve( originalLocation, nextResolvers.subList( 1, nextResolvers.size() ) );
    }
    else {

      if ( ( resolvedLocation == null ) || resolvedLocation.isInvalid || !resolvedLocation.isResolved() ) {
        // If we never got this location resolved, don't fire an event.
        LogUtils.debug( "LocationResolver", "No one resolved location!" );
      }
      else {

        boolean useElevation = personalPrefs.getBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false );
        if ( !useElevation ) {
          resolvedLocation.setElevation( 0.0 );
        }

        eventBus.fire( new LocationAcquiredEvent( resolvedLocation, false ) );

        // Let's keep track of how often we use each resolver.
        LogUtils.logEvent( "LocationResolved", Arrays.asList( new Pair<String, String>( "Resolver", getClass().getSimpleName() ) ) );
      }
    }
  }


} // End of LocationResolver class
