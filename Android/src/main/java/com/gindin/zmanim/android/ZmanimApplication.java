/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

// TODO: Turn Acra back on after we get the new and updated backend setup. See
//  https://github.com/ACRA/acra, and https://github.com/F43nd1r/Acrarium
//
// TODO: Re-enable ACRA usage in LogUtils
//@AcraCore(
//  buildConfigClass = BuildConfig.class,
//  reportFormat = StringFormat.JSON,
//
//  // Log the preferences. See ZmanimPrefs; but note we can't use the enums .name(), because
//  //  the values here have to be constants.
//  additionalSharedPreferences = { "ZMANIM_SELECTION", "LOCATION_MANAGEMENT", "PERSONAL" },
//  alsoReportToAndroidFramework = true
//)
//@AcraHttpSender(
//  httpMethod = HttpSender.Method.PUT,
    // TODO: We MUST use an https connection...
//  uri = "http://crashes.zmanim.today:6095/acra-zmanim/_design/acra-storage/_update/report",
//  basicAuthLogin = BuildConfig.ACRA_USER,
//  basicAuthPassword = BuildConfig.ACRA_KEY
//)
//@AcraToast( resText = R.string.crash_toast_text )
//@AcraScheduler( requiresNetworkType = JobInfo.NETWORK_TYPE_UNMETERED, requiresBatteryNotLow = true )
public class ZmanimApplication
  extends Application {


  @Override
  protected void attachBaseContext( Context base ) {
    super.attachBaseContext( base );

    if ( !BuildConfig.DEBUG ) {

//      ACRA.init( this );
    }
    else {
      // Just so we *KNOW* we're dealing with a dev version...
      Toast toast = Toast.makeText( base, "This is dev...", Toast.LENGTH_LONG );
      toast.show();
    }
  }


} // End of ZmanimApplication class
