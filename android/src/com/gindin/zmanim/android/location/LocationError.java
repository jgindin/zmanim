/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

/**
 * Describes an error while trying to determine the user's location.
 */
public class LocationError {

	/**
	 * Enumerates the possible errors when trying to determine the location.
	 */
	public static enum Code {
		CANNOT_DETERMINE_LOCATION,
		BAD_INPUT,
		NO_LOCATION_MANAGER_SERVICE,
		NO_PROVIDERS
	}


	public final Code                                         errorCode;

	public final String                                       message;



	public LocationError(
		Code    errorCode
	) {
		this( errorCode, null );
	}

	
	public LocationError(
		Code    errorCode,
	  String  message
	) {

		this.errorCode = errorCode;
		this.message = message;
	}

}
