package com.ibm.icu;

import android.content.res.AssetManager;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * Bridge that will handle things that ICU would normally want to do one way, but must do differently
 *  in order to play nicely with Android.
 */
public class ICUAndroidBridge {

	/**
	 * Only one instance of this guy.
	 */
	private static ICUAndroidBridge                           instance;

	/**
	 * ICU would normally want to use the Class.getResource() and Class.getResourceAsStream() to read things.
	 *  The Android way, however, says that you go through the asset manager.
	 */
	private final AssetManager                                assetManager;


	public static void create(
		AssetManager  assetManager
	) {

		if ( null == instance ) {
			instance = new ICUAndroidBridge( assetManager );
		}
	}


	public static ICUAndroidBridge get() {
		return instance;
	}


	private ICUAndroidBridge( AssetManager assetManager ) {
		this.assetManager = assetManager;
	}


	public boolean resourceExists(
		String resourceName
	) {

		int lastSlash = resourceName.lastIndexOf( "/" );

		String directory;
		String file;
		if ( -1 != lastSlash ) {
			directory = resourceName.substring( 0, lastSlash - 1 );
			file = resourceName.substring( lastSlash );
		}
		else {
			directory = "";
			file = resourceName;
		}

		// Make sure we're not starting with a "/"... that won't work here...
		if ( directory.startsWith( "/" ) ) {
			directory = directory.substring( 1 );
		}


		try {
			String [] files = assetManager.list( directory );
			for ( String f : files ) {
				if ( file.equals( f  ) ) {
					return true;
				}
			}
		}
		catch ( IOException e ) {
			// Ignore.
		}

		return false;
	}


	public ArrayList<String> list(
		String          directory,
		FilenameFilter  filenameFilter
	) {

		// Asset manager doesn't like to do a listing if the the directory ends with a '/'...
		if ( directory.endsWith( "/" ) ) {
			directory = directory.substring( 0, directory.length() - 1 );
		}

		ArrayList<String> listing = new ArrayList<String>();

		try {
			String [] files = assetManager.list( directory );
			for ( String f : files ) {
				if ( filenameFilter.accept( null, f ) ) {
					listing.add( f );
				}
			}
		}
		catch ( IOException e ) {
			return null;
		}

		return listing;
	}


	public InputStream open(
		String  resourceName,
	  boolean failIfNotPresent
	) {

		InputStream i;

		// Make sure we're not starting with a "/"... that won't work here...
		if ( resourceName.startsWith( "/" ) ) {
			resourceName = resourceName.substring( 1 );
		}

		try {
			i = assetManager.open( resourceName );
		}
		catch ( IOException e ) {
			if ( failIfNotPresent ) {
				throw new MissingResourceException("could not locate data " + resourceName, "", resourceName);
			}

			i = null;
		}

		return i;
	}


} // End of ICUAndroidBridge interface
