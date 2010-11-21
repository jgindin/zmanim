/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

/**
 * The activities used by the Zmanim app. Actually used so that when an activity is launched, the primary
 *  activity knows whom to notify upon completion.
 */
public enum Activities {

	MAIN,

	/**
	 * When the user wants to change their location preferences
	 */
	EDIT_LOCATION_PREFERENCES,

	/**
	 * In case there are no location sources enabled, we'll make a request to the intent that will allow the
	 *  user to turn something on.
	 */
	ENABLE_LOCATION_SERVICES

}  // End of Activities class
