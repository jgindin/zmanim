/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ZmanimApplication
  extends Application {


  @Override
  protected void attachBaseContext( Context base ) {
    super.attachBaseContext( base );

    if ( !BuildConfig.DEBUG ) {
    }
    else {
      // Just so we *KNOW* we're dealing with a dev version...
      Toast toast = Toast.makeText( base, "This is dev...", Toast.LENGTH_LONG );
      toast.show();
    }
  }


} // End of ZmanimApplication class
