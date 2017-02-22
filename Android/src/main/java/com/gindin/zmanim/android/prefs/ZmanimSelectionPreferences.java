/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity;

/**
 * Exists because the ZmanimSelectionPrefsManager needs us to exist. At this time, however, this is not
 *  an activity which is "registered" and so won't / can't be invoked.
 */
@SuppressLint( "Registered" )
public class ZmanimSelectionPreferences
  extends PreferenceActivity {

}  // End of ZmanimSelectionPreferences class
