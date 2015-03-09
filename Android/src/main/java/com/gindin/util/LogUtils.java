/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;


/**
 * Wraps our usage of the Flurry analytics agent.
 */
public class LogUtils {


	private LogUtils() {
	}


	public static void startSession(
		final Activity context
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
					logError( "UEH", where, e );

/*
					// This could be useful when hitting a problem that only reproduces in production builds..
					final String display =  "Where: " + where + "; e: " + exceptionToString( e );

					context.runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								AlertDialog.Builder builder = new AlertDialog.Builder( context );
								builder.setMessage( display );
								builder.create().show();
							}
						}
					);
*/

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



	public static void logError(
		String    where,
		String    message,
		Throwable error
	) {
		//noinspection NullArgumentToVariableArgMethod
		logError( where, message, error, null );
	}	

	public static void logError(
		String                  where,
		String                  message,
		Throwable               error,
		Pair<String, String>... moreInfo
	) {

		String stackTrace = ( null == error ? null: exceptionToString( error ) );

		if ( Version.isDevelopment() ) {
			Log.e( where, "Msg: " + message + "\nStack: " + stackTrace );
		}
		else {

			List<Pair<String,String>> messages = new ArrayList<Pair<String, String>>();
			messages.add( new Pair<String, String>( "Where", where ) );
			messages.add( new Pair<String, String>( "Message", message ) );
			if ( null != error ) {
				messages.add( new Pair<String, String>( "Stack", stackTrace ) );
			}

			if ( null != moreInfo ) {
				Collections.addAll( messages, moreInfo );
			}

			//noinspection unchecked
			logEvent( "Error", messages );

			FlurryAgent.onError( where, message, "" );
		}

	}


	public static void logEvent(
		String                      eventName,
	  List<Pair<String,String>>   messages
	) {

		if ( Version.isDevelopment() ) {

			StringBuilder msg = new StringBuilder();
			for ( Pair<String, String> pair : messages ) {
				msg.append( pair.first ).append( ": " ).append( pair.second );
			}

			Log.i( eventName, msg.toString() );
		}
		else {

			Map<String,String> messageMap = new HashMap<String, String>();
			if ( null != messages ) {
				for ( Pair<String, String> message : messages ) {
					messageMap.put( message.first, message.second );
				}
			}

			FlurryAgent.onEvent( eventName, messageMap );
		}
	}


	public static void debug(
		String tag,
	  String message
	) {

		if ( Version.isDevelopment() ) {
			Log.i( tag, message );
		}
	}


	/**
	 * The idea here is that flurry messages are short. Really short. So, don't waste space with well-known
	 *  strings that don't offer value. Especially when trying to figure out what an exception was...
	 *
	 * @param message The message that needs to get cleaned up & shortened.
	 *
	 * @return The 'cleaned' version of the input.
	 */
	private static String cleanMessage( String message ) {

		String cleaned;
		cleaned = message.replace( "com.gindin.zmanim.android.zmanList", "c.g.z.a.z" );
		cleaned = cleaned.replace( "com.gindin.zmanim.android.location", "c.g.z.a.l" );
		cleaned = cleaned.replace( "com.gindin.zmanim.android", "c.g.z.a" );
		cleaned = cleaned.replace( "com.gindin.zmanlib.calendar", "c.g.z.c" );
		cleaned = cleaned.replace( "com.gindin.zmanlib.zman", "c.g.z.z" );
		cleaned = cleaned.replace( "com.gindin.zmanim", "c.g.z" );
		cleaned = cleaned.replace( "com.gindin.util", "c.g.u" );
		cleaned = cleaned.replace( "com.gindin", "c.g" );

		cleaned = cleaned.replace( "java.util.concurrent.FutureTask$Sync.innerSetException", "j.u.c.FT$S.iSE" );
		cleaned = cleaned.replace( "java.util.concurrent.FutureTask$Sync.innerRun", "j.u.c.FT$S.iR" );
    cleaned = cleaned.replace( "java.util.concurrent.FutureTask.setException", "j.u.c.FT.sE" );
		cleaned = cleaned.replace( "java.util.concurrent.FutureTask.run", "j.u.c.FT.r" );
		cleaned = cleaned.replace( "java.util.concurrent", "j.u.c" );
		cleaned = cleaned.replace( "java.util", "j.u" );
		cleaned = cleaned.replace( "java.lang.NullPointerException", "NPE" );
		cleaned = cleaned.replace( "java.lang.RuntimeException", "RE" );
		cleaned = cleaned.replace( "java.lang", "j.l" );
		cleaned = cleaned.replace( "java.", "j." );

		cleaned = cleaned.replace( "android.os.AsyncTask", "a.o.AT" );
		cleaned = cleaned.replace( "android.os", "a.o" );

		cleaned = cleaned.replace( "An error occurred while executing", "Error" );

		return cleaned;
	}


	private static String exceptionToString( Throwable error ) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter( sw );

		if ( null == error.getCause() ) {
			error.printStackTrace( pw );
		}
		else {
			error.getCause().printStackTrace( pw );
		}

		return cleanMessage( sw.toString() );
	}

}  // End of LogUtils class
