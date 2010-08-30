/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.util;

import com.gindin.zmanim.android.location.ZmanimLocationManager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Wraps our usage of the Flurry analytics agent.
 */
public class LogUtils {


	/** Remember where we are. */
	private static ZmanimLocationManager                      locationManager;


	private LogUtils() {
	}


	public static void startSession(
		Context context
	) {

		if ( !Version.isDevelopment() ) {
			FlurryAgent.onStartSession( context, "FLURRY_KEY_GOES_HERE" );
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



	public static void setLocationHandler(
		ZmanimLocationManager locationManager
	) {

		LogUtils.locationManager = locationManager;
	}


	public static void logError(
		String errorID,
	  String message
	) {

		if ( !Version.isDevelopment() ) {
			FlurryAgent.onError( errorID, message, locationManager.getLastKnownLocation().toString() );
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


	public static void logEvent(
		String      eventName,
	  String ...  messages
	) {

		if ( !Version.isDevelopment() ) {

			Map<String,String> messageMap = new HashMap<String, String>();
			if ( null != messages ) {
				for ( int index = 0; index < messages.length; index++ ) {
					messageMap.put( "Msg" + index, messages[ index ] );
				}
			}

			FlurryAgent.onEvent( eventName, messageMap );
		}

		
		StringBuffer message = new StringBuffer();
		if ( null != messages ) {
			for ( String msg : messages ) {
				message.append( msg ).append( "; " );
			}
		}

		Log.i( eventName, message.toString() );
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


}  // End of LogUtils class
