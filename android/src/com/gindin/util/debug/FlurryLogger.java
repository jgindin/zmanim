/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.util.debug;

import android.content.Context;
import com.flurry.android.FlurryAgent;
import com.gindin.zmanim.android.AndroidGeoLocation;

/**
 * Wraps our usage of the Flurry analytics agent.
 */
public class FlurryLogger {


	/** Remember where we are. */
	private static AndroidGeoLocation location;


	private FlurryLogger() {
	}


	public static void startSession(
		Context context
	) {
		FlurryAgent.onStartSession( context, "HDK5S96L5E7C638N91S1" );
	}


	public static void endSession(
		Context context
	) {
		FlurryAgent.onEndSession( context );
	}



	public static void setLocation(
		AndroidGeoLocation location
	) {

		FlurryLogger.location = location;
	}


	public static void logError(
		String errorID,
	  String message
	) {

		message = cleanMessage( message );

		FlurryAgent.onError( errorID, message, location.toLocationString() );
	}


	private static String cleanMessage( String message ) {

		String cleaned = message.replace( "com.gindin.zmanim.android.zmanList", "c.g.z.a.z" );
		cleaned = cleaned.replace( "com.gindin.zmanim.android", "c.g.z.a" );
		cleaned = cleaned.replace( "com.gindin.zmanim.calendar", "c.g.z.c" );
		cleaned = cleaned.replace( "com.gindin.zmanim.times", "c.g.z.t" );
		cleaned = cleaned.replace( "java.util", "j.u" );
		cleaned = cleaned.replace( "java.lang.NullPointerException", "NPE" );

		return cleaned;
	}


}  // End of FlurryLogger class
