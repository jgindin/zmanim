/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Extension to the GeoLocation used by the zmanim calculations that adds some additional info used by the
 *  zmanim app.
 */
public class ZmanimLocation
	extends GeoLocation {


	/** Who provided this location to us? E.g., 'network', 'gps'. */
	public final String                                       providerName;

	/** When was this location determined? */
	public final long                                         locationTime;

	/** How accurate do we believe this location to be? */
	public final float                                        accuracy;


	/**
	 * Default ctor for when we are unable to obtain any real location information.
	 */
	@SuppressWarnings( "MagicNumber" )
	public ZmanimLocation() {

		providerName = "Zmanim";
		locationTime = System.currentTimeMillis();
		accuracy = 0;
		setLocationName( "Unknown" );
		setLatitude( 47, 37, 0, "N" );
		setLongitude( 122, 20, 0, "W" );
		setElevation( 0.0 );
		setTimeZone( TimeZone.getDefault() );
	}


	public ZmanimLocation(
		Location  newLocation,
		String    locationName,
		TimeZone  timeZone
	) {

		providerName = newLocation.getProvider();
		locationTime = newLocation.getTime();

		if ( newLocation.hasAccuracy() ) {
			accuracy = newLocation.getAccuracy();
		}
		else {
			accuracy = -1;
		}

		setLocationName( locationName );
		setLatitude( newLocation.getLatitude() );
		setLongitude( newLocation.getLongitude() );

		double altitude = newLocation.getAltitude();
		if ( !newLocation.hasAltitude() || ( altitude < 0 ) ) {
			altitude = 0.0;
		}

		setElevation( altitude );

		// If we don't set a time zone, then it defaults to GMT...
		if ( null == timeZone ) {
			setTimeZone( TimeZone.getDefault() );
		}
		else {
			setTimeZone( timeZone );
		}

	}


	public ZmanimLocation(
		ZmanimLocation source
	) {

		providerName = source.providerName;
		locationTime = source.locationTime;
		accuracy = source.accuracy;
		setLocationName( source.getLocationName() );
		setLatitude( source.getLatitude() );
		setLongitude( source.getLongitude() );
		setElevation( source.getElevation() );
		setTimeZone( source.getTimeZone() );
	}

	
	public String getLatitudeString() {
		//noinspection AutoBoxing
		return String.format( "%.1f", getLatitude() );
	}
	
	
	public String getLongitudeString() {
		//noinspection AutoBoxing
		return String.format( "%.1f", getLongitude() );
	}


	@Override
	public String toString() {

		@SuppressWarnings( { "MagicNumber" } )
		int MILLIS_IN_HOUR = 60 * 60 * 1000;

		StringBuffer sb = new StringBuffer();
		sb.append( "Location Name:\t" ).append( getLocationName() );
		sb.append( "\nLatitude:\t" ).append( getLatitudeString() ).append( " deg" );
		sb.append( "\nLongitude:\t" ).append( getLongitudeString() ).append( " deg" );
		sb.append( "\nElevation:\t" ).append( getElevation() ).append( " Meters" );
		sb.append( "\nTZ Name:\t" ).append( getTimeZone().getDisplayName( false, TimeZone.SHORT, Locale.getDefault() ) );
		sb.append( "\nTZ GMT Offset:\t" ).append( getTimeZone().getRawOffset() / MILLIS_IN_HOUR );
		sb.append( "\nTZ DST Offset:\t" ).append( getTimeZone().getDSTSavings() / MILLIS_IN_HOUR );
		sb.append( "\nProvider:\t" ).append( providerName );
		sb.append( "\nAccuracy:\t" ).append( accuracy );

		DateFormat sdf = SimpleDateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault() );
		sb.append( "\nFix:\t" ).append( sdf.format( new Date( locationTime ) ) );

		return sb.toString();
	}
	
	public String toShortString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append( "Where: " ).append( getLocationName());
		sb.append( ";La: " ).append( getLatitudeString() ).append( " d" );
		sb.append( ";Lo: " ).append( getLongitudeString() ).append( " d" );
		sb.append( ";TZ: " ).append( getTimeZone().getDisplayName( false, TimeZone.SHORT, Locale.getDefault() ) );

		DateFormat sdf = SimpleDateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault() );
		sb.append( ";Fix: " ).append( sdf.format( new Date( locationTime ) ) );

		return sb.toString();
	}	
	
}  // End of ZmanimLocation class
