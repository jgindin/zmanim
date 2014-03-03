/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.util;

/**
 * Allows us to access programmatically some info about the current version.
 */
public class Version {

	// DO NOT MUCK WITH THE FORMATTING!!!
	public static final int androidCodeVersion = 225;

	// DO NOT MUCK WITH THE FORMATTING!!!
	public static final String androidVersionName = "3.5.225.bcb4a1ef9aca";


	public static boolean isDevelopment() {
		return androidVersionName.endsWith( "-DEV" );
	}


	/**
	 * Ensure this cannot be instantiated.
	 */
	private Version() {
	}

}  // End of Version class
