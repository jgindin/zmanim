/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.location.Location;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Date;
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


	public ZmanimLocation() {

		providerName = null;
		locationTime = 0;
		accuracy = -1;

		setTimeZone();
	}


	public ZmanimLocation(
		Location  newLocation,
		String    locationName
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
		setTimeZone();
	}


	@Override
	public String toString() {

		@SuppressWarnings( { "MagicNumber" } )
		int MILLIS_IN_HOUR = 60 * 60 * 1000;

		StringBuffer sb = new StringBuffer();
		sb.append( "Location Name:\t" ).append(getLocationName());
		sb.append( "\nLatitude:\t" ).append(getLatitude()).append( " deg" );
		sb.append( "\nLongitude:\t" ).append(getLongitude()).append( " deg" );
		sb.append( "\nElevation:\t" ).append(getElevation()).append( " Meters" );
		sb.append( "\nTZ Name:\t" ).append(getTimeZone().getID());
		sb.append( "\nTZ GMT Offset:\t" ).append( getTimeZone().getRawOffset() / MILLIS_IN_HOUR );
		sb.append( "\nTZ DST Offset:\t" ).append( getTimeZone().getDSTSavings() / MILLIS_IN_HOUR );
		sb.append( "\nProvider:\t" ).append( providerName );
		sb.append( "\nTime:\t" ).append( new Date( locationTime ) );
		sb.append( "\nAccuacy:\t" ).append( accuracy );

		return sb.toString();
	}



	private void setTimeZone() {

		// If we don't set a time zone, then it defaults to GMT...
		setTimeZone( TimeZone.getDefault() );
	}

}  // End of ZmanimLocation class
