/*
 * Copyright (c) 2010. Jay R. Gindin
 */
package com.gindin.zmanim.android.prefs;

/**
 * Lists the possible types of location providers.
 */
public enum LocationProviders {

	/** Indicates the user wants us to find their location every time. */
	DYNAMIC_POSITION( "useDynamicPosition" ),

	/** The user has given us an address (or at least enough data from one) to figure out where they are. */
	STATIC_POSITION( "useStaticLocation" ),

	/** The user has pinpointed their location. */
	LATITUDE_LONGITUDE_POSITION( "useLatLong" );



	public static LocationProviders fromProviderType(
		String providerType
	) {

		if ( DYNAMIC_POSITION.providerType.equals( providerType ) ) {
			return DYNAMIC_POSITION;
		}

		if ( STATIC_POSITION.providerType.equals( providerType ) ) {
			return STATIC_POSITION;
		}

		if ( LATITUDE_LONGITUDE_POSITION.providerType.equals( providerType ) ) {
			return LATITUDE_LONGITUDE_POSITION;
		}

		return null;
	}


	public final String                                       providerType;


	private LocationProviders(
		String providerType
	) {

		this.providerType = providerType;
	}

} // End of LocationProviders enum
