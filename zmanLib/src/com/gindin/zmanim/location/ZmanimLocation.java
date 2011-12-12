/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.location;

import net.sourceforge.zmanim.util.GeoLocation;

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


	/**
	 * 
	 * @param providerName Name of the location provider.
	 * @param locationName What's the name of this location?                  
	 * @param locationTime What time is it in this location?
	 * @param locationTimeZone Time zone for this location                     
	 * @param accuracy How accurate is the location? -1 to indicate this isn't applicable.
	 * @param latitude What's the latitude of this location?
	 * @param longitude What's the longitude of this location?
	 * @param altitude What's the altitude of this location? <= 0 to indicate unknown.
	 */
	public ZmanimLocation(
		String    providerName,
		String    locationName,
	  long      locationTime,
	  TimeZone  locationTimeZone,
	  double    latitude,
	  double    longitude,
	  double    altitude,
	  float     accuracy
	  ) {
		
		this.providerName = providerName;
		this.locationTime = locationTime;
		setLocationName( locationName );
		setLatitude( latitude );
		setLongitude( longitude );

		if ( accuracy >= 0 ) {
			this.accuracy = accuracy;
		}
		else {
			this.accuracy = -1;
		}

		if ( altitude < 0 ) {
			altitude = 0.0;
		}
		setElevation( altitude );
		
		// If we don't set a time zone, then it defaults to GMT...
		if ( null == locationTimeZone ) {
			setTimeZone( TimeZone.getDefault() );
		}
		else {
			setTimeZone( locationTimeZone );
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

		StringBuilder sb = new StringBuilder();
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
		
		StringBuilder sb = new StringBuilder();
		sb.append( "Where: " ).append( getLocationName());
		sb.append( ";La: " ).append( getLatitudeString() ).append( " d" );
		sb.append( ";Lo: " ).append( getLongitudeString() ).append( " d" );
		sb.append( ";TZ: " ).append( getTimeZone().getDisplayName( false, TimeZone.SHORT, Locale.getDefault() ) );

		DateFormat sdf = SimpleDateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault() );
		sb.append( ";Fix: " ).append( sdf.format( new Date( locationTime ) ) );

		return sb.toString();
	}	
	
}  // End of ZmanimLocation class
