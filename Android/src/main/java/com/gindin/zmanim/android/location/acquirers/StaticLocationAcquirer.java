/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Location provider which uses a place name like "5217 South Brandon St., Seattle, WA" to determine the Zmanim.
 */
public class StaticLocationAcquirer
  extends LocationAcquirer {

  public StaticLocationAcquirer(
    SharedPreferences locationManagementPrefs,
    Context           context
  ) {

    super( locationManagementPrefs, context );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Override
  public void acquire( final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage ) {

    String place = locationManagementPrefs.getString( LocationProviders.StaticProviderOptions.PLACE.name(), "" );
    ZmanimLocation newLocation = new ZmanimLocation.Builder()
      .locationName( place )
      .userName( place )
      .inTimeZone( getTimeZoneFromPrefs() )
      .build();

    firePlaceAcquired( newLocation );
  }

}  // End of StaticLocationProvider class
