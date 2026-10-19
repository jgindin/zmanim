/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay;

import android.app.Activity;
import android.os.Looper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

/**
 * The "Determining location" dialog appears only after a short delay, on a timer thread's say-so. The activity it
 *  belongs to can be gone by then, in which case the dialog must stay hidden rather than crash the app.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_DeterminingLocation {

  /** Longer than the dialog's delay, so the timer has certainly fired by the time we have slept this long. */
  private static final long                                 PAST_THE_DELAY_MS = 600;

  /** How long to wait for the dialog to appear before deciding it never will. */
  private static final long                                 APPEAR_TIMEOUT_MS = 5_000;


  @Test
  public void aDialogForAnActivityDestroyedDuringTheDelayIsNeverShown() throws Exception {

    ActivityController<Activity> controller = Robolectric.buildActivity( Activity.class ).setup();
    DeterminingLocation dialog = new DeterminingLocation( controller.get() );

    dialog.show();
    controller.pause().stop().destroy();
    waitOutTheDelay();

    Assert.assertFalse( dialog.isShowing() );
  }


  @Test
  public void aDialogForAnActivityFinishedDuringTheDelayIsNeverShown() throws Exception {

    ActivityController<Activity> controller = Robolectric.buildActivity( Activity.class ).setup();
    DeterminingLocation dialog = new DeterminingLocation( controller.get() );

    dialog.show();
    controller.get().finish();
    waitOutTheDelay();

    Assert.assertFalse( dialog.isShowing() );
  }


  @Test
  public void aDialogForALiveActivityIsShownOnceTheDelayHasPassed() throws Exception {

    ActivityController<Activity> controller = Robolectric.buildActivity( Activity.class ).setup();
    DeterminingLocation dialog = new DeterminingLocation( controller.get() );

    dialog.show();
    Assert.assertFalse( dialog.isShowing() );

    Assert.assertTrue( waitForDialogToAppear( dialog ) );

    dialog.dismiss();
  }


  @Test
  public void aDialogDismissedDuringTheDelayIsNeverShown() throws Exception {

    ActivityController<Activity> controller = Robolectric.buildActivity( Activity.class ).setup();
    DeterminingLocation dialog = new DeterminingLocation( controller.get() );

    dialog.show();
    dialog.dismiss();
    waitOutTheDelay();

    Assert.assertFalse( dialog.isShowing() );
  }


  /** Sleeps past the dialog's timer, then runs whatever the timer posted to the main thread. */
  private static void waitOutTheDelay() throws InterruptedException {

    Thread.sleep( PAST_THE_DELAY_MS );
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  private static boolean waitForDialogToAppear( DeterminingLocation dialog ) throws InterruptedException {

    long giveUpAt = System.currentTimeMillis() + APPEAR_TIMEOUT_MS;

    while ( !dialog.isShowing() && System.currentTimeMillis() < giveUpAt ) {
      Thread.sleep( 25 );
      Shadows.shadowOf( Looper.getMainLooper() ).idle();
    }

    return dialog.isShowing();
  }
}
