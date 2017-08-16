package com.gindin.zmanim.android.location;

import com.gindin.zmanlib.location.ZmanimLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.location.Location;

import java.util.TimeZone;

/**
 * Since the ZmanLocation class is in our ZmanLib module, which knows nothing about Android, the methods in this
 *  class are intended to serve as a bit of a bridge.
 */
public class ZmanLocationUtil {


  @NotNull
  public static ZmanimLocation fromLocation(
    @Nullable Location location
  ) {
    return fromLocation( location, null );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @NotNull
  public static ZmanimLocation fromLocation(
    @Nullable Location  location,
    @Nullable TimeZone  timeZone
  ) {

    if ( location == null ) {
      return new ZmanimLocation.Builder().invalidLocation();
    }

    ZmanimLocation.Builder builder = new ZmanimLocation.Builder()
      .fromProvider( location.getProvider() )
      .at( location.getTime() )
      .atLatitude( location.getLatitude() )
      .atLongitude( location.getLongitude() );

    if ( location.hasAccuracy() ) { builder.withAccuracy( location.getAccuracy() ); }
    if ( location.hasAltitude() ) { builder.atElevation( location.getAltitude() ); }
    if ( timeZone != null ) { builder.inTimeZone( timeZone ); }

    return builder.build();
  }

} // End of ZmanLocationUtil class
