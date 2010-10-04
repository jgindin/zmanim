/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

/**
 * Enumerates the namespaces of the preferences used by the app.
 */
public enum ZmanimPrefs {

	/** Tracks info about the last known version of the app that was run. Might be better to use the database... */ 
	VERSION( "Version" ),

	/** Used to track which zmanim the user has selected to use. */
	ZMANIM_SELECTION( "ZmanimSelection" ),

	/** Used for storing the last known location. Might be better to use the database... */
	LAST_LOCATION( "ZmanimLocation" ),

	LOCATION_MANAGEMENT( "ZmanimLocationManagement" );

	public final String                                       name;


	private ZmanimPrefs(
		String  name
	) {

		this.name = name;
	}

}  // End of ZmanimPrefs class
