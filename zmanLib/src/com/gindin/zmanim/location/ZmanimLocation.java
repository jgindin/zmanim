/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.location;

import net.sourceforge.zmanim.util.GeoLocation;

import android.location.Address;
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

	public static final class Builder {

		private String                                          providerName = ZMANIM_PROVIDER;
		private long                                            locationTime = System.currentTimeMillis();
		private float                                           accuracy = -1;
		private String                                          locationName = UNKNOWN_LOCATION;
		private TimeZone                                        timeZone = null;
		private double                                          latitude = 0.0;
		private double                                          longitude = 0.0;
		private double                                          elevation = 0.0;


		public Builder fromProvider( String providerName ) {
			this.providerName = providerName;
			return this;
		}

		public Builder locationName( String locationName ) {
			this.locationName = locationName;
			return this;
		}

		public Builder at( long locationTime ) {
			this.locationTime = locationTime;
			return this;
		}

		public Builder inTimeZone( TimeZone timeZone ) {
			this.timeZone = timeZone;
			return this;
		}

		public Builder atLatitude( double latitude ) {
			this.latitude = latitude;
			return this;
		}

		public Builder atLongitude( double longitude ) {
			this.longitude = longitude;
			return this;
		}

		public Builder atElevation( double elevation ) {
			this.elevation = elevation;
			return this;
		}

		public Builder withAccuracy( float accuracy ) {
			this.accuracy = accuracy;
			return this;
		}

		public Builder from( ZmanimLocation source ) {
			return this
				.fromProvider( source.providerName )
				.locationName( source.getLocationName() )
				.at( source.locationTime )
				.inTimeZone( source.getTimeZone() )
				.atLatitude( source.getLatitude() )
				.atLongitude( source.getLongitude() )
				.atElevation( source.getElevation() )
				.withAccuracy( source.accuracy );
		}

		public Builder resolvedTo( Address address ) {
			if ( address == null ) {
				return this;
			}

			return this
				.locationName( addressToLocationName( address ) )
				.atLatitude( address.getLatitude() )
				.atLongitude( address.getLongitude() );
		}

		public Builder from( Location location ) {
			this
				.fromProvider( location.getProvider() )
				.at( location.getTime() )
				.atLatitude( location.getLatitude() )
				.atLongitude( location.getLongitude() );

			if ( location.hasAccuracy() ) withAccuracy( location.getAccuracy() );
			if ( location.hasAltitude() ) atElevation( location.getAltitude() );

			return this;
		}

		public ZmanimLocation build() {
			return new ZmanimLocation( providerName, locationName, locationTime, timeZone, latitude, longitude, elevation, accuracy );
		}
	}


	/** When the location name is unknown, use this, and an appropriate substitution will be made. */
	private final static String                               UNKNOWN_LOCATION = " _unk_ ";

	private static final String                               ZMANIM_PROVIDER = "Zmanim";

	/** Who provided this location to us? E.g., 'network', 'gps'. */
	public final String                                       providerName;

	/** When was this location determined? */
	public final long                                         locationTime;

	/** How accurate do we believe this location to be? */
	public final float                                        accuracy;


	/**
	 * @param providerName Name of the location provider.
	 * @param locationName What's the name of this location?
	 * @param locationTime What time is it in this location?
	 * @param locationTimeZone Time zone for this location
	 * @param accuracy How accurate is the location? -1 to indicate this isn't applicable.
	 * @param latitude What's the latitude of this location?
	 * @param longitude What's the longitude of this location?
	 * @param altitude What's the altitude of this location? <= 0 to indicate unknown.
	 */
	private ZmanimLocation(
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
			//noinspection AssignmentToMethodParameter
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


	@Override
	public String getLocationName() {
		String name = super.getLocationName();
		if ( ( name == null ) || ( name.equals( UNKNOWN_LOCATION ) ) ) {
			name = toLatLongString( getLatitude(), getLongitude() );
		}

		return name;
	}


	@Override
	public String toString() {

		@SuppressWarnings( { "MagicNumber" } )
		int MILLIS_IN_HOUR = 60 * 60 * 1000;

		StringBuilder sb = new StringBuilder();
		sb.append( "Location Name:\t" ).append( getLocationName() );
		sb.append( "\nLatitude:\t" ).append( getLatitudeString( getLatitude() ) ).append( " deg" );
		sb.append( "\nLongitude:\t" ).append( getLongitudeString( getLongitude() ) ).append( " deg" );
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
		sb.append( ";L: " ).append( toLatLongString( getLatitude(), getLongitude() ) );
		sb.append( ";TZ: " ).append( getTimeZone().getDisplayName( false, TimeZone.SHORT, Locale.getDefault() ) );

		DateFormat sdf = SimpleDateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault() );
		sb.append( ";Fix: " ).append( sdf.format( new Date( locationTime ) ) );

		return sb.toString();
	}


	private static String getLatitudeString( double latitude ) {
		//noinspection AutoBoxing
		return String.format( "%.1f", latitude );
	}


	private static String getLongitudeString( double longitude ) {
		//noinspection AutoBoxing
		return String.format( "%.1f", longitude );
	}


	private static String toLatLongString( double latitude, double longitude ) {
		return getLatitudeString( latitude ) + " lat; " + getLongitudeString( longitude ) + " long";
	}


	private static String addressToLocationName( Address address ) {

		String locationString = null;

		if ( address == null ) {
			return UNKNOWN_LOCATION;
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
			locationString = toLatLongString( address.getLatitude(), address.getLongitude() );
		}

		return locationString;
	}

}  // End of ZmanimLocation class
