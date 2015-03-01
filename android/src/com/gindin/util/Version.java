/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util;

/**
 * Allows us to access programmatically some info about the current version.
 */
public class Version {

	// DO NOT MUCK WITH THE FORMATTING!!!
	public static final int androidCodeVersion = 246;

	// DO NOT MUCK WITH THE FORMATTING!!!
	public static final String androidVersionName = "4.0.246.c8125610320c";


	public static boolean isDevelopment() {
		return androidVersionName.endsWith( "-DEV" );
	}


	/**
	 * Ensure this cannot be instantiated.
	 */
	private Version() {
	}

}  // End of Version class
