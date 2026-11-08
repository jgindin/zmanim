/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.feedback;

import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.location.ZmanimLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * The email the user's own email app is asked to compose for feedback. The app never sends anything itself: the user
 *  sees the whole message, and can change it, before sending it. The details after the feedback are in English, and
 *  not translated, because they are read by the developers.
 */
final class FeedbackEmail {

  static final String ADDRESS = "zmanim.apps@gmail.com";


  private FeedbackEmail() {
  }


  /** Whether the device has an app that can compose the email. */
  static boolean canBeSent(
    @NonNull Context context
  ) {
    return emailApp().resolveActivity( context.getPackageManager() ) != null;
  }


  /**
   * An ACTION_SENDTO intent, which only email apps handle, addressed to the developers.
   *
   * @param location a valid location, or null when there is none.
   */
  @NonNull
  static Intent intent(
    @NonNull  Context         context,
    @NonNull  String          feedback,
    @Nullable ZmanimLocation  location
  ) {

    String appName = context.getString( R.string.app_name );

    return emailApp()
      .putExtra( Intent.EXTRA_EMAIL, new String[] { ADDRESS } )
      .putExtra( Intent.EXTRA_SUBJECT, appName + " feedback (" + BuildConfig.VERSION_NAME + ")" )
      .putExtra( Intent.EXTRA_TEXT, body( appName, feedback, location ) );
  }


  /**
   * The intent that only email apps answer. Seeing which apps answer it needs the matching queries element in the
   *  manifest.
   */
  @NonNull
  private static Intent emailApp() {
    return new Intent( Intent.ACTION_SENDTO, Uri.parse( "mailto:" ) );
  }


  /** @param location a valid location, or null when there is none. */
  @NonNull
  static String body(
    @NonNull  String          appName,
    @NonNull  String          feedback,
    @Nullable ZmanimLocation  location
  ) {

    StringBuilder body = new StringBuilder( feedback )
      .append( "\n\n--\n" )
      .append( "App: " ).append( appName ).append( ' ' )
      .append( BuildConfig.VERSION_NAME ).append( " (" ).append( BuildConfig.VERSION_CODE ).append( ")\n" );

    if ( location == null ) {
      return body.append( "Location: not available\n" ).toString();
    }

    body
      .append( "Location name: " ).append( location.hasName() ? location.getLocationName() : "none" ).append( '\n' )
      .append( "Latitude: " ).append( location.getLatitude() ).append( '\n' )
      .append( "Longitude: " ).append( location.getLongitude() ).append( '\n' )
      .append( "Elevation: " ).append( location.getElevation() ).append( " m\n" )
      .append( "Time zone: " ).append( location.getTimeZone().getID() ).append( '\n' )
      .append( "Provider: " ).append( ( location.providerName != null ) ? location.providerName : "unknown" ).append( '\n' );

    // ZmanimLocation uses a negative accuracy for one that is not known.
    if ( location.accuracy >= 0 ) {
      body.append( "Accuracy: " ).append( location.accuracy ).append( " m\n" );
    }

    String obtained = Instant.ofEpochMilli( location.locationTime )
      .atZone( location.getTimeZone().toZoneId() )
      .format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
    body.append( "Obtained: " ).append( obtained ).append( '\n' );

    return body.toString();
  }

}  // End of FeedbackEmail class
