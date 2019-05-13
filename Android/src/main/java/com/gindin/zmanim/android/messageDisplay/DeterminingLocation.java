package com.gindin.zmanim.android.messageDisplay;

import com.gindin.zmanim.android.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * When we need to display to the user that we're trying to find their location, if it can be determined in a
 *  "short enough time" then having the dialog flash up and then go away is just annoying. This class ensures
 *  that it won't be displayed in such a case.
 */
public class DeterminingLocation
  extends ProgressDialog {

  /** How long to delay before showing the activity? Measured in milliseconds. */
  private static final int                                  DELAY = 250;

  private final Activity                                    activity;

  @Nullable
  private Timer                                             delayTimer;


  public DeterminingLocation( Activity activity ) {
    super( activity );
    this.activity = activity;

    setIndeterminate( true );
    setMessage( activity.getString( R.string.determining_location ) );
  }


  @Override
  public void show() {

    if ( isShowing() ) {
      return;
    }

    if ( delayTimer != null ) {
      delayTimer.cancel();
    }

    delayTimer = new Timer();

    delayTimer.schedule( new TimerTask() {
        @Override public void run() {

          // If the timer is null then we got dismissed.
          if ( delayTimer != null ) {
            delayTimer.cancel();
            activity.runOnUiThread( DeterminingLocation.super::show );
          }
        }
      },

      DELAY
    );
  }


  @Override
  public void dismiss() {

    if ( delayTimer != null ) {
      delayTimer.cancel();
    }

    super.dismiss();

    delayTimer = null;
  }
}
