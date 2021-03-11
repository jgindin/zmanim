/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraHttpSender;
import org.acra.annotation.AcraScheduler;
import org.acra.annotation.AcraToast;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

import android.app.Application;
import android.app.job.JobInfo;
import android.content.Context;
import android.widget.Toast;

/**
 *
 */
@AcraCore(
  buildConfigClass = BuildConfig.class,
  reportFormat = StringFormat.JSON,

  // Log the preferences. See ZmanimPrefs; but note we can't use the enums .name(), because
  //  the values here have to be constants.
  additionalSharedPreferences = { "ZMANIM_SELECTION", "LOCATION_MANAGEMENT", "PERSONAL" },
  alsoReportToAndroidFramework = true
)
@AcraHttpSender(
  httpMethod = HttpSender.Method.PUT,
  uri = "http://crashes.zmanim.today:6095/acra-zmanim/_design/acra-storage/_update/report",
  basicAuthLogin = BuildConfig.ACRA_USER,
  basicAuthPassword = BuildConfig.ACRA_KEY
)
@AcraToast( resText = R.string.crash_toast_text )
@AcraScheduler( requiresNetworkType = JobInfo.NETWORK_TYPE_UNMETERED, requiresBatteryNotLow = true )
public class ZmanimApplication
  extends Application {


  @Override
  protected void attachBaseContext( Context base ) {
    super.attachBaseContext( base );

    if ( !BuildConfig.DEBUG ) {

      ACRA.init( this );
    }
    else {
      // Just so we *KNOW* we're dealing with a dev version...
      Toast toast = Toast.makeText( base, "This is dev...", Toast.LENGTH_LONG );
      toast.show();
    }
  }


} // End of ZmanimApplication class
