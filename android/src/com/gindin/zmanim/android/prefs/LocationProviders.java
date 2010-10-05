/*
 * Copyright (c) 2010. Jay R. Gindin
 */
package com.gindin.zmanim.android.prefs;

/**
 * Lists the possible types of location providers.
 */
public enum LocationProviders {

	/** Indicates the user wants us to find their location every time. */
	DYNAMIC_POSITION,

	/** The user has given us an address (or at least enough data from one) to figure out where they are. */
	STATIC_POSITION,

	/** The user has pinpointed their location. */
	LATITUDE_LONGITUDE_POSITION;



	/** Options specific to the static position provider. */
	public static enum StaticPositionOptions {

		LATITUDE,
		LONGITUDE
	}

} // End of LocationProviders enum
