package com.gindin.zmanim.android.location;

import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import com.google.android.gms.location.LocationResult;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class ZmanimLocationService
  extends IntentService {

  /**
   * This is the value that the Fused location provider uses to name the LocationResult. We match it so that the code
   *  paths are not duplicated.
   */
  private static final String                               LOCATION = "com.google.android.gms.location.EXTRA_LOCATION_RESULT";
  private static final String                               TIME_ZONE = "time zone";



  public static Intent createIntent(
    Context             context,
    @Nullable TimeZone  timeZone
  ) {

    return createIntent( context, null, timeZone );
  }


  public static Intent createIntent(
    Context             context,
    @Nullable Location  location,
    @Nullable TimeZone  timeZone
  ) {

    Intent intent = new Intent( context, ZmanimLocationService.class );

    if ( location != null ) {
      final LocationResult locationResult = LocationResult.create( Collections.singletonList( location ) );
      intent.putExtra( LOCATION, locationResult );
    }

    if ( timeZone != null ) {
      intent.putExtra( TIME_ZONE, timeZone );
    }

    return intent;
  }


  public ZmanimLocationService() {
    super( "ZmanimLocationService" );
  }


  @Override
  protected void onHandleIntent(
    @Nullable Intent intent
  ) {

    if ( intent == null ) {
      return;
    }

    // The last location in the result is the newest...
    LocationResult result = LocationResult.extractResult( intent );
    if ( result == null ) {
      // OK... we seem to get LocationAvailability sent too. Ignore.
      return;
    }

    final List<Location> locations = result.getLocations();
    Location newLocation = locations.get( locations.size() - 1 );

    TimeZone timeZone = (TimeZone)intent.getSerializableExtra( TIME_ZONE );
    ZmanimLocation location = ZmanLocationUtil.fromLocation( newLocation, timeZone );

    ZmanimService.enqueueWork( this, location );
  }
}
