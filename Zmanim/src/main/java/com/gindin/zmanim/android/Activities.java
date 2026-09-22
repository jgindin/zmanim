/*
 * Copyright (c) 2014. Jay R. Gindin
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
   * When the user wants to edit their personal preferences
   */
  PERSONAL_PREFERENCS,

  /**
   * When Google Play services are not available and we've set them off to do something about it.
   */
  PLAY_SERVICES_NOT_AVAILABLE,

  /**
   *
   */
  REQUEST_CHECK_SETTINGS,

}  // End of Activities class
