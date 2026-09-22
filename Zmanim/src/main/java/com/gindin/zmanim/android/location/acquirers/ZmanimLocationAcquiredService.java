package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import com.google.android.gms.location.LocationResult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;

import java.util.List;
import java.util.TimeZone;

public class ZmanimLocationAcquiredService
  extends BroadcastReceiver {

  /**
   * The intent action this receiver listens for.
   */
  private static final String                               ACTION = "com.gindin.zmanim.android.location.acquirers.ZmanimLocationAcquiredService.LOCATION_RECEIVED";



  public static Intent createIntent(
    Context   context
  ) {

    Intent intent = new Intent( context, ZmanimLocationAcquiredService.class );
    intent.setAction( ACTION );

    return intent;
  }


  @Override
  public void onReceive(
    Context context,
    Intent  intent
  ) {

    if ( intent == null ) {
      return;
    }

    // Sanity check.
    if ( !ACTION.equals( intent.getAction() ) ) {
      return;
    }

    // The last location in the result is the newest...
    LocationResult result;
    try {
      result = LocationResult.extractResult( intent );
    }
    catch ( Exception e ) {
      // Some errors are coming in where calling extractResult causes a RuntimeException:
      //  java.lang.RuntimeException: Parcel android.os.Parcel@4e7729e8: Unmarshalling unknown type code 7274595 at offset 308
      ZmanimService.enqueueWork( context, new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION ), null );
      result = null;
    }

    if ( result == null ) {
      // OK... we seem to get LocationAvailability sent too. Ignore.
      return;
    }

    final List<Location> locations = result.getLocations();
    Location newLocation = locations.get( locations.size() - 1 );

    final SharedPreferences prefs = context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    TimeZone timeZone = LocationAcquirer.getTimeZoneFromPrefs( prefs );

    ZmanimLocation location = ZmanLocationUtil.fromLocation( newLocation, timeZone );

    // See the save() in DynamicLocationProvider when we are just using the location provider's last known position...
    ZmanLocationUtil.save( context, location );

    ZmanimService.enqueueWork( context, location );
  }
}
