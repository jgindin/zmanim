/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;

/**
 *
 */
class StaticLocationProvider
	extends LocationProvider {


	StaticLocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {
		super( activity, locationManagementPrefs );
	}


	@Override
	public void onLocationProvidersChanged() {
		//To change body of implemented methods use File | Settings | File Templates.
	}


	@Override
	public boolean addListener( Listener listener ) {
		return super.addListener( listener );
	}


	@Override
	protected SharedPreferences.OnSharedPreferenceChangeListener createLocationManagementPrefsListener() {
		return null;
	}


	@Override
	protected Location getLastKnownLocation() {
		return null;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}  // End of StaticLocationProvider class
