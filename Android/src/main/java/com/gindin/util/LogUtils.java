/*
 * Copyright (c) 2020. Jay R. Gindin
 */

package com.gindin.util;

import com.gindin.zmanim.android.BuildConfig;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogUtils {


  private LogUtils() {
  }


  public static void logError(
    String              where,
    String              message,
    @Nullable Throwable error
  ) {
    //noinspection NullArgumentToVariableArgMethod
    logError( where, message, error, (Pair<String, String>[])null );
  }


  public static void logError(
    String                            where,
    String                            message,
    @Nullable Throwable               error,
    @Nullable Pair<String, String>... moreInfo
  ) {

    LogErrorTask task = new LogErrorTask( where, message, error, moreInfo );
    task.run();
  }


  public static void logFeedback(
    @NotNull String feedback
  ) {

    LogErrorTask task = new LogErrorTask( "User Feedback", feedback, new UserFeedbackException(),
      (Pair<String, String>[])null );
    task.run();
  }


  public static void logEvent(
    String                      eventName,
    List<Pair<String,String>>   messages
  ) {

    // TODO: Find a way to capture this data again...
//    LogEventTask task = new LogEventTask( eventName, messages );
//    task.run();
  }


  public static void setContext(
    String key,
    String data
  ) {

    if ( !BuildConfig.DEBUG ) {
      if ( data == null ) {
        ACRA.getErrorReporter().removeCustomData( key );
      }
      else {
        ACRA.getErrorReporter().putCustomData( key, data );
      }
    }
  }



  private static class LogEventTask
    implements Runnable {

    private final String                                    eventName;
    private final List<Pair<String,String>>                 messages;


    LogEventTask(
      String eventName,
      List<Pair<String, String>> messages
    ) {
      this.eventName = eventName;
      this.messages = messages;
    }


    @Override
    public void run() {

      if ( BuildConfig.DEBUG ) {

        StringBuilder msg = new StringBuilder();
        for ( Pair<String, String> pair : messages ) {
          msg.append( pair.first ).append( ": " ).append( pair.second );
        }

        Log.i( eventName, msg.toString() );
      }
      else {

        Map<String,String> messageMap = new HashMap<>();
        if ( null != messages ) {
          for ( Pair<String, String> message : messages ) {
            messageMap.put( message.first, message.second );
          }
        }

      }
    }
  }


  private static class LogErrorTask
    implements Runnable {

    private final String                                    where;

    private final String                                    msg;

    @Nullable
    private final Throwable                                 error;

    @Nullable
    private final Pair<String, String>[]                    moreInfo;


    LogErrorTask(
      String                            where,
      String                            message,
      @Nullable Throwable               error,
      @Nullable Pair<String, String>... moreInfo
    ) {
      this.where = where;
      this.msg = message;
      this.moreInfo = moreInfo;

      if ( ( error == null ) && !BuildConfig.DEBUG ) {
        this.error = new Exception( message );
      }
      else {
        this.error = error;
      }
    }


    @Override
    public void run() {

      String message = msg;

      if ( BuildConfig.DEBUG ) {
        if ( moreInfo != null ) {
          StringBuilder extra = new StringBuilder( "\n" );
          for ( Pair<String, String> info : moreInfo ) {
            extra.append( info.first ).append( ": " ).append( info.second ).append( "\n" );
          }

          message += extra.toString();
        }

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
  }


  private static class UserFeedbackException
    extends Exception {

  }

}  // End of LogUtils class
