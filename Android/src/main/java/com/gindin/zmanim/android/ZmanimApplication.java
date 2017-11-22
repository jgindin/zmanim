package com.gindin.zmanim.android;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 *
 */
@ReportsCrashes(
  formUri = "https://crashes.zmanim.today:6095/acra-zmanim/_design/acra-storage/_update/report",
  reportType = org.acra.sender.HttpSender.Type.JSON,
  httpMethod = org.acra.sender.HttpSender.Method.PUT,

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

      try {
        ACRAConfiguration config = new ConfigurationBuilder( this )
          .setFormUriBasicAuthLogin( BuildConfig.ACRA_USER )
          .setFormUriBasicAuthPassword( BuildConfig.ACRA_KEY )
          .build();
        ACRA.init( this, config );
      }
      catch ( ACRAConfigurationException e ) {
        Log.e( "init", "Unable to init ACRA", e );
      }

    }
    else {
      // Just so we *KNOW* we're dealing with a dev version...
      Toast toast = Toast.makeText( base, "This is dev...", Toast.LENGTH_LONG );
      toast.show();
    }
  }


} // End of ZmanimApplication class
