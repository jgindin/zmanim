/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.location.PlaceAcquiredEvent;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Location provider which uses a place name like "5217 South Brandon St., Seattle, WA" to determine the Zmanim.
 */
public class StaticLocationAcquirer
  extends LocationAcquirer {


  public StaticLocationAcquirer(
    EventBus          eventBus,
    SharedPreferences locationManagementPrefs,
    SharedPreferences personalPrefs
  ) {

    super( eventBus, locationManagementPrefs, personalPrefs );
  }


  @Override
  public void acquire( final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage ) {

    AsyncTask<Void, Void, ZmanimLocation> task = new AsyncTask<Void, Void, ZmanimLocation>() {
      @Override
      protected ZmanimLocation doInBackground( Void... voids ) {

        String place = locationManagementPrefs.getString( LocationProviders.StaticProviderOptions.PLACE.name(), "" );
        ZmanimLocation newLocation = new ZmanimLocation.Builder()
          .locationName( place )
          .userName( place )
          .inTimeZone( getTimeZoneFromPrefs() )
          .build();

        return newLocation;
      }


      @Override
      protected void onPostExecute( ZmanimLocation zmanimLocation ) {
        super.onPostExecute( zmanimLocation );

        eventBus.fire( new PlaceAcquiredEvent( zmanimLocation ) );
      }
    };

    task.execute( (Void)null );
  }

}  // End of StaticLocationProvider class
