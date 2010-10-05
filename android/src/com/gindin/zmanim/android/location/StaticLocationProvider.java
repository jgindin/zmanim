/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.location.Location;

/**
 *
 */
class StaticLocationProvider
	extends LocationProvider {


	StaticLocationProvider( Activity activity ) {
		super( activity );
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
	protected Location getLastKnownLocation() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
	}

}  // End of StaticLocationProvider class
