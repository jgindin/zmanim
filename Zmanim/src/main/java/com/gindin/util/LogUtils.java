/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.util;

import com.gindin.zmanim.android.BuildConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import java.util.List;


public class LogUtils {


  private LogUtils() {
  }


  public static void logError(
    String              where,
    String              message,
    @Nullable Throwable error
  ) {
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
    @NonNull String feedback
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

      if ( moreInfo != null ) {
        StringBuilder extra = new StringBuilder( "\n" );
        for ( Pair<String, String> info : moreInfo ) {
          extra.append( info.first ).append( ": " ).append( info.second ).append( "\n" );
        }

        message += extra.toString();
      }

      Log.e( where, "Msg: " + message, error );

    }
  }


  private static class UserFeedbackException
    extends Exception {

  }

}  // End of LogUtils class
