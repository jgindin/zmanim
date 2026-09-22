/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

/**
 * Enumerates the namespaces of the preferences used by the app.
 */
public enum ZmanimPrefs {

  /** Tracks info about the last known version of the app that was run. Might be better to use the database... */ 
  VERSION,

  /** Used to track which zmanim the user has selected to use. */
  ZMANIM_SELECTION,

  /** Used for storing the last known location. Might be better to use the database... */
  LAST_LOCATION,

  /** Preferences related to which location provider to use, and the choices about those providers made by the user. */
  LOCATION_MANAGEMENT,

  /** General preferences */
  PERSONAL

}  // End of ZmanimPrefs class
