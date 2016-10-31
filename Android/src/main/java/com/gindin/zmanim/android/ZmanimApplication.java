package com.gindin.zmanim.android;

import com.flurry.android.FlurryAgent;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 *
 */
@ReportsCrashes(
  formUri = "https://zmanim.cloudant.com/acra-zmanim/_design/acra-storage/_update/report",
  reportType = org.acra.sender.HttpSender.Type.JSON,
  httpMethod = org.acra.sender.HttpSender.Method.PUT,
  formUriBasicAuthLogin="[reporteruser]",
  formUriBasicAuthPassword="[reporterpassword]",

  // Log the preferences. See ZmanimPrefs; but note we can't use the enums .name(), because
  //  the values here have to be constants.
  additionalSharedPreferences = { "ZMANIM_SELECTION", "LOCATION_MANAGEMENT", "PERSONAL" },

  // Your usual ACRA configuration
  mode = ReportingInteractionMode.TOAST,
  resToastText = R.string.crash_toast_text,
  alsoReportToAndroidFramework = true
)
public class ZmanimApplication
  extends Application {


  @Override
  protected void attachBaseContext( Context base ) {
    super.attachBaseContext( base );

    if ( !BuildConfig.DEBUG ) {
      ACRA.init( this );

      new FlurryAgent.Builder()
        .withLogEnabled( false )
        .withCaptureUncaughtExceptions( false )
        .build( this, "FLURRY_KEY_GOES_HERE" );
    }
    else {
      // Just so we *KNOW* we're dealing with a dev version...
      Toast toast = Toast.makeText( base, "This is dev...", Toast.LENGTH_LONG );
      toast.show();
    }
  }


} // End of ZmanimApplication class
