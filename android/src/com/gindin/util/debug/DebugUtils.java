/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.util.debug;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.gindin.zmanim.android.AndroidGeoLocation;
import com.gindin.zmanim.android.Version;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Wraps our usage of the Flurry analytics agent.
 */
public class DebugUtils {


	/** Remember where we are. */
	private static AndroidGeoLocation                         location;


	private DebugUtils() {
	}


	public static void startSession(
		Context context
	) {

		if ( !Version.isDevelopment() ) {
			FlurryAgent.onStartSession( context, "HDK5S96L5E7C638N91S1" );
		}
		else {
			// Just so we *KNOW* we're dealing with a dev version...
			Toast toast = Toast.makeText( context, "No flurry...this is dev...", Toast.LENGTH_LONG );
			toast.show();
		}


		Thread.setDefaultUncaughtExceptionHandler(
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(
					Thread    t,
					Throwable e
				) {

					String where = ( null == t ? "null thread" : t.getName() );
					logError( where, "", e );
				}
			}
		);

	}


	public static void endSession(
		Context context
	) {

		if ( !Version.isDevelopment() ) {
			FlurryAgent.onEndSession( context );
		}
	}



	public static void setLocation(
		AndroidGeoLocation location
	) {

		DebugUtils.location = location;
	}


	public static void logError(
		String errorID,
	  String message
	) {

		if ( !Version.isDevelopment() ) {
			FlurryAgent.onError( errorID, message, location.toLocationString() );
		}
		else {
			Log.e( errorID, message );
		}
	}


	public static void logError(
		String    where,
		String    message,
		Throwable error
	) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter( sw );
		error.printStackTrace( pw );

		String errorMessage = cleanMessage( sw.toString() );

		logError( where, message + "; " + errorMessage );
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


}  // End of DebugUtils class
