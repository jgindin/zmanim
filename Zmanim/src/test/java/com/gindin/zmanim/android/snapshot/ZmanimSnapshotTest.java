/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.snapshot;

import com.github.takahirom.roborazzi.RoborazziContextKt;
import com.github.takahirom.roborazzi.RoborazziKt;
import com.github.takahirom.roborazzi.RoborazziOptions;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Roborazzi baseline snapshots of two {@code :Zmanim} screens: the main daily zmanim list and the
 *  personal preferences screen.
 *<p/>
 *
 * {@link ZmanimActivity} determines "today" via {@link HebrewCalendar#today()} (real wall-clock
 *  time) and acquires its location asynchronously through a chain of {@code SafeJobIntentService}
 *  work (see {@code ZmanimService}) that talks to Google Play Services location APIs -- none of
 *  which is available, or safely triggerable, inside a Robolectric JVM test.
 *<p/>
 *
 * Rather than fighting that pipeline, this test uses the same seam the production code itself
 *  uses to communicate a location or date change to the UI: {@code ZmanimService.ZMANIM_EVENT_BUS}.
 *  {@link ZmanimActivity}'s own {@code refresh()} call in {@code onResume()} still fires (it
 *  enqueues work against {@code ZmanimService}), but under Robolectric that enqueue is a
 *  observable no-op (no {@code JobScheduler}/service actually runs), so it never races with, or
 *  overwrites, the fixed values we push moments later. We then fire a fixed
 *  {@link HebrewDateChangedEvent} (September 7, 2026) and a fixed {@link LocationAcquiredEvent}
 *  (Yonkers, NY) directly on that bus -- exactly the events {@code ZmanimActivity},
 *  {@code DatePresenterImpl}, and {@code ZmanimPresenterImpl} already listen for -- so the
 *  rendered screen depends only on values this test controls, never on the machine's clock or
 *  network/location state.
 *<p/>
 *
 * {@code qualifiers = "w411dp-h869dp"} sizes the simulated device to a modern phone (Robolectric's
 *  un-configured default is a much smaller/shorter screen); without it, {@code capturePreferencesScreen()}
 *  would cut off before showing any of the per-zman notification sliders this test captures.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33, qualifiers = "w411dp-h869dp" )
public class ZmanimSnapshotTest {

  /** Yonkers, NY -- an arbitrary, fixed test location, matching the reference screenshot. */
  private static final double  YONKERS_LATITUDE = 40.9312;
  private static final double  YONKERS_LONGITUDE = -73.8988;

  /** Fixed so the astronomical zmanim calculated for "the day" never depend on when this test runs. */
  private static final int     FIXED_YEAR = 2026;
  private static final int     FIXED_MONTH = Calendar.SEPTEMBER;
  private static final int     FIXED_DAY_OF_MONTH = 7;


  /**
   * Launching the main activity over a fresh install runs the app's upgrade steps, which leave this process-wide flag
   *  set. Clear it so that tests running later in the same JVM that start the converter see a loaded cache.
   */
  @After
  public void clearProcessWideUpgradeFlag() {
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  @Test
  public void captureMainScreen() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> fireFixedDateAndLocation() );

      scenario.onActivity( activity -> {

        ListView zmanimList = activity.findViewById( android.R.id.list );
        waitForZmanimToRender( zmanimList );

        View root = activity.findViewById( android.R.id.content );
        capture( root, "screenshots/zmanim.png" );
      } );
    }
  }


  @Test
  public void capturePreferencesScreen() {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        // The preference screen is added as a Fragment transaction, which is asynchronous;
        //  force it to run before we capture, so the sliders/categories are actually attached.
        activity.getSupportFragmentManager().executePendingTransactions();
        Shadows.shadowOf( Looper.getMainLooper() ).idle();

        View root = activity.findViewById( android.R.id.content );
        capture( root, "screenshots/preferences.png" );
      } );
    }
  }


  /**
   * Fires the fixed date first (so it is in place before the location triggers the actual
   *  render), then the fixed location, directly on the app's own event bus -- the same bus
   *  {@code ZmanimActivity} and its presenters are already listening to by the time
   *  {@code ActivityScenario.launch()} returns.
   *
   * NOTE: this fixes the *calendar* date/location used for zman calculation, but
   *  {@code HebrewDate.isToday()}'s holiday-description branch still compares against the real
   *  system clock rather than this fixed date. That's a narrow, self-healing edge case (it can
   *  only mis-render on the one calendar day where the two disagree) and isn't itself fixed here.
   */
  private void fireFixedDateAndLocation() {

    HebrewDate fixedDate = HebrewCalendar.getForDate( FIXED_YEAR, FIXED_MONTH, FIXED_DAY_OF_MONTH );

    ZmanimLocation fixedLocation = new ZmanimLocation.Builder()
      .locationName( "Yonkers, NY" )
      .atLatitude( YONKERS_LATITUDE )
      .atLongitude( YONKERS_LONGITUDE )
      .atElevation( 0 )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .at( 0L ) // Fixed & unused by anything this test renders -- just avoiding System.currentTimeMillis().
      .build();

    ZmanimService.ZMANIM_EVENT_BUS.fire( new HebrewDateChangedEvent( fixedDate ) );
    ZmanimService.ZMANIM_EVENT_BUS.fire( new LocationAcquiredEvent( fixedLocation ) );
  }


  /**
   * {@code ZmanimPresenterImpl} posts the list of zmanim to the UI thread immediately (that part
   *  is synchronous under Robolectric since we're already running on the "UI" thread), but the
   *  formatted time for each zman is computed by a real {@code AsyncTask}, whose background work
   *  runs on an actual JVM thread and whose result is posted back to the paused main looper. So
   *  we poll: idle the main looper, and check whether the first row actually has a formatted
   *  time yet, with a generous bound (this is pure string formatting -- it never takes anywhere
   *  close to this long in practice).
   */
  private void waitForZmanimToRender(
    ListView listView
  ) {

    for ( int attempt = 0; attempt < 200; attempt++ ) {

      Shadows.shadowOf( Looper.getMainLooper() ).idle();

      if ( ( listView.getChildCount() > 0 ) && allVisibleRowsHaveTimes( listView ) ) {
        return;
      }

      try {
        Thread.sleep( 20 );
      }
      catch ( InterruptedException interrupted ) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }


  /**
   * Checks every currently-visible row, not just the first -- the background AsyncTask formats
   *  each zman's time individually, and while in practice they all land in the same batch of
   *  {@code onProgressUpdate} calls, only checking row 0 wouldn't actually prove the rest (e.g.
   *  the last, off-screen-until-scrolled entries currently on screen) are done too.
   */
  private boolean allVisibleRowsHaveTimes(
    ListView listView
  ) {

    for ( int i = 0, count = listView.getChildCount(); i < count; i++ ) {

      TextView timeField = listView.getChildAt( i ).findViewById( R.id.zman_time );
      if ( ( timeField == null ) || ( timeField.getText().length() == 0 ) ) {
        return false;
      }
    }

    return true;
  }


  private void capture(
    View    view,
    String  filePath
  ) {

    RoborazziOptions options = RoborazziContextKt.provideRoborazziContext().getOptions();
    RoborazziKt.captureRoboImage( view, filePath, options );
  }

} // End of ZmanimSnapshotTest class
