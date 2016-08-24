/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

import net.sourceforge.zmanim.util.GeoLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

import java.io.*;
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
		private TimeZone                                        timeZone = TimeZone.getDefault();
		private double                                          latitude = 0.0;
		private double                                          longitude = 0.0;
		private double                                          elevation = 0.0;


		public Builder fromProvider( String providerName ) {
			this.providerName = providerName;
			return this;
		}

		public Builder locationName( String locationName ) {
			if ( locationName == null ) {
				this.locationName = UNKNOWN_LOCATION;
			}
			else {
				this.locationName = locationName;
			}
			return this;
		}

		public Builder at( long locationTime ) {
			this.locationTime = locationTime;
			return this;
		}


    /**
     * @param timeZone if null, the default time zone will be used.
     */
		public Builder inTimeZone( @Nullable TimeZone timeZone ) {
			if ( timeZone != null ) {
				this.timeZone = timeZone;
			}
			else {
				this.timeZone = TimeZone.getDefault();
			}
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


		public ZmanimLocation invalidLocation() {
			return INVALID_LOCATION;
		}

		public ZmanimLocation build() {
			return new ZmanimLocation( providerName, locationName, locationTime, timeZone, latitude, longitude, elevation, accuracy );
		}
	}


	/**
	 * Whenever we need an invalid location, this one instance will do.
	 */
	private static final ZmanimLocation                       INVALID_LOCATION = new ZmanimLocation();


	/** When the location name is unknown, use this, and an appropriate substitution will be made. */
	public static final String                                UNKNOWN_LOCATION = " _unk_ ";

	private static final String                               ZMANIM_PROVIDER = "Zmanim";

	/** Who provided this location to us? E.g., 'network', 'gps'. */
	public final String                                       providerName;

	/** When was this location determined? */
	public final long                                         locationTime;

	/** How accurate do we believe this location to be? */
	public final float                                        accuracy;

	/**
	 * True indicates this is a known invalid location; false simply means we believe it to be valid, but cannot be
	 *  completely sure.
	 */
	public final boolean                                      isInvalid;


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

		this.isInvalid = false;

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


	/**
	 * Only used when we need to build an invalid location.
	 */
	private ZmanimLocation() {

		this.isInvalid = true;
		this.accuracy = -1;
		this.providerName = UNKNOWN_LOCATION;
		this.locationTime = 0;

	}


	@Override
	@NotNull
	public String getLocationName() {
		String name = super.getLocationName();
		if ( ( name == null ) || name.equals( UNKNOWN_LOCATION ) || name.trim().isEmpty() ) {
			name = toLatLongString( getLatitude(), getLongitude() );
		}

		return name;
	}



	public final boolean isSignificantChangeFrom( ZmanimLocation previousLocation ) {

		if ( previousLocation == null ) {
			return true;
		}

		// Less than 1km? Don't bother...
		// I suppose you could run into an issue if that 1km puts you in a different time zone
		//  or something like that...
		return getGeodesicDistance( previousLocation ) >= 1000;
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


	public boolean hasName() {

		// Make sure to NOT compare against our overridden getLocationName!
		return !UNKNOWN_LOCATION.equals( super.getLocationName() );
	}


	public boolean hasCoordinates() {
		return ( getLatitude() != 0 ) && ( getLongitude() != 0 );
	}


	/**
	 * Check if this is a resolved location, meaning that we know both the coordinates AND the name.
	 */
	public boolean isResolved() {
		return hasCoordinates() && hasName();
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


	/**
	 * Writes this zmanim location to the specified output. Does NOT close the stream or perform any other operation to the stream.
	 *
	 * @throws IOException if there is any error writing to the stream.
	 */
	public void save( OutputStream output ) throws IOException {

		DataOutputStream dataOutputStream = new DataOutputStream( output );

		String json = ZmanLocationJSON.toJSON( this );
		dataOutputStream.writeUTF( json );
	}


	/**
	 * Loads a zmanim location from the specified input, assuming the stream is correctly positioned. Does NOT close the
	 *  stream or perform any other operations to the stream.
	 *
	 * @throws IOException If there is any error interacting with the stream.
	 * @throws JSONException If there is any error reading the location from the stream.
	 */
	public static ZmanimLocation load( InputStream input ) throws IOException, JSONException {

		DataInputStream inputStream = new DataInputStream( input );
		String json = inputStream.readUTF();

		return ZmanLocationJSON.fromJSON( json );
	}


	static String getLatitudeString( double latitude ) {
		//noinspection AutoBoxing
		return String.format( "%.1f", latitude );
	}


	static String getLongitudeString( double longitude ) {
		//noinspection AutoBoxing
		return String.format( "%.1f", longitude );
	}


	public static String toLatLongString( double latitude, double longitude ) {
		return getLatitudeString( latitude ) + " lat; " + getLongitudeString( longitude ) + " long";
	}


}  // End of ZmanimLocation class
