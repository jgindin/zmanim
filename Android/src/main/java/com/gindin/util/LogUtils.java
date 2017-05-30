/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util;

import com.gindin.zmanim.android.BuildConfig;

import com.flurry.android.FlurryAgent;
import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.jetbrains.annotations.Nullable;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Wraps our usage of the Flurry analytics agent.
 */
public class LogUtils {


  private LogUtils() {
  }


  public static void startSession(
    final Context context
  ) {

    if ( !BuildConfig.DEBUG ) {
      FlurryAgent.onStartSession( context );
    }
  }


  public static void endSession(
    Context context
  ) {

    if ( !BuildConfig.DEBUG ) {
      FlurryAgent.onEndSession( context );
    }
  }


  public static void logError(
    String    where,
    String    message,
    Throwable error
  ) {
    //noinspection NullArgumentToVariableArgMethod
    logError( where, message, error, (Pair<String, String>[])null );
  }


  public static void logError(
    String                  where,
    String                  message,
    Throwable               error,
    @Nullable Pair<String, String>... moreInfo
  ) {


    if ( BuildConfig.DEBUG ) {
      Log.e( where, "Msg: " + message, error );
    }
    else {

      final ErrorReporter errorReporter = ACRA.getErrorReporter();
      errorReporter.putCustomData( "Where", where );
      errorReporter.putCustomData( "Message", message );
      errorReporter.putCustomData( "LogTime", new Date( System.currentTimeMillis() ).toString() );

      if ( null != moreInfo ) {
        for ( Pair<String, String> info : moreInfo ) {
          errorReporter.putCustomData( info.first, info.second );
        }
      }

      // We want this to be silent...it's something we're interested in logging, but the user
      //  shouldn't know/care...
      if ( error == null ) {
        error = new Exception( message );
      }

      errorReporter.handleSilentException( error );

      // Remove the extra data that was passed in here. But if someone had added additional info
      //  besides this, then presumably it should remain...
      errorReporter.removeCustomData( "Where" );
      errorReporter.removeCustomData( "Message" );
      errorReporter.removeCustomData( "LogTime" );
      if ( moreInfo != null ) {
        for ( Pair<String, String> info : moreInfo ) {
          errorReporter.removeCustomData( info.first );
        }
      }
    }
  }


  public static void logEvent(
    String                      eventName,
    List<Pair<String,String>>   messages
  ) {

    if ( BuildConfig.DEBUG ) {

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

      FlurryAgent.logEvent( eventName, messageMap );
    }
  }


  public static void debug(
    String tag,
    String message
  ) {

    if ( BuildConfig.DEBUG ) {
      Log.i( tag, message );
    }
  }


  public static void setContext(
    String key,
    String data
  ) {

    if ( BuildConfig.DEBUG ) {
      if ( data != null ) {
        Log.e( key, data );
      }
    }
    else {
      if ( data == null ) {
        ACRA.getErrorReporter().removeCustomData( key );
      }
      else {
        ACRA.getErrorReporter().putCustomData( key, data );
      }
    }
  }

}  // End of LogUtils class
