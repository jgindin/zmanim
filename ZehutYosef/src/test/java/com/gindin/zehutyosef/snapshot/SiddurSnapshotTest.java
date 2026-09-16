/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef.snapshot;

import com.github.takahirom.roborazzi.RoborazziContextKt;
import com.github.takahirom.roborazzi.RoborazziKt;
import com.github.takahirom.roborazzi.RoborazziOptions;

import com.gindin.zehutyosef.BuildConfig;
import com.gindin.zehutyosef.ZehutYosefActivity;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;

import android.os.Looper;
import android.view.View;

import androidx.test.core.app.ActivityScenario;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Roborazzi baseline snapshots of {@link ZehutYosefActivity}, which is shared, unmodified, by
 *  both {@code ZehutYosef} product flavors ({@code barMitzvah} and {@code sidur}). The two
 *  flavors differ only in resources -- most visibly the theme: {@code barMitzvah}'s manifest
 *  (src/barMitzvah/AndroidManifest.xml) applies {@code @style/EliPurple}, while {@code sidur}'s
 *  manifest applies {@code @style/MaterialDrawerTheme.TranslucentStatus}. There is no runtime
 *  switch for this; it is baked in at resource-merge time per flavor.
 *<p/>
 *
 * Because this file lives under the shared {@code src/test/java} source set, Gradle compiles and
 *  runs it once per flavor variant (e.g. {@code testBarMitzvahDebugUnitTest} and
 *  {@code testSidurDebugUnitTest} -- and correspondingly
 *  {@code recordRoborazziBarMitzvahDebug}/{@code recordRoborazziSidurDebug} for Roborazzi, NOT a
 *  flavor-less {@code recordRoborazziDebug} the way the single-flavor {@code :Android} module
 *  has). Both methods below therefore exist in both variant runs; each uses
 *  {@link Assume#assumeTrue} to only actually capture under the flavor it names, and cleanly
 *  skips (not fails) under the other -- otherwise "captureBarMitzvahReader" run under the
 *  {@code sidur} variant would silently capture the wrong theme under the right-sounding name.
 *<p/>
 *
 * {@code ZehutYosefActivity}'s {@code zehut_yosef} options menu declares a single
 *  {@code showAsAction="ifRoom"} item (see {@code res/menu/zehut_yosef.xml}) whose action view is
 *  a plain {@code TextView} showing the current Hebrew date -- AppCompat inflates and populates
 *  "ifRoom" action items eagerly (they must be visible in the toolbar immediately, not only once
 *  the overflow menu is opened), so that badge is already on screen by the time
 *  {@code ActivityScenario.launch()} returns control here, and it lands in the captured PNG. The
 *  activity computes that date from real wall-clock time ({@code hebrewDate} is initialized in its
 *  constructor from {@link HebrewCalendar#today()}), so left alone, these baselines would only
 *  ever match on the exact calendar day they were recorded -- exactly the live-clock problem
 *  {@link com.gindin.zmanim.android.snapshot.ZmanimSnapshotTest} avoids for {@code ZmanimActivity}
 *  by firing a fixed {@code HebrewDateChangedEvent} on that activity's event bus.
 *<p/>
 *
 * {@code ZehutYosefActivity} has no equivalent event bus (or any other production seam) for
 *  pushing in a date -- its only date mutation path is the private {@code hebrewDate} field plus
 *  the private {@code updateHebrewDate()} method, both driven internally by the date-picker
 *  dialog. Rather than add a test-only seam to production code for this, {@link #fixHebrewDate}
 *  reaches into that existing private field/method pair via reflection and drives it exactly the
 *  way {@code onDateSet()} already does in production: set {@code hebrewDate}, then call
 *  {@code updateHebrewDate()} to refresh the toolbar {@code TextView}. Same fixed date as
 *  {@code ZmanimSnapshotTest} (September 7, 2026) for consistency across the two test suites.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class SiddurSnapshotTest {

  /** Same fixed date {@code ZmanimSnapshotTest} uses, so both suites' baselines agree on "today". */
  private static final int     FIXED_YEAR = 2026;
  private static final int     FIXED_MONTH = Calendar.SEPTEMBER;
  private static final int     FIXED_DAY_OF_MONTH = 7;


  @Test
  public void captureBarMitzvahReader() {

    Assume.assumeTrue( "Only meaningful when built as the barMitzvah flavor", isFlavor( "barMitzvah" ) );

    captureReaderScreen( "screenshots/barmitzvah.png" );
  }


  @Test
  public void captureSiddurReader() {

    Assume.assumeTrue( "Only meaningful when built as the sidur flavor", isFlavor( "sidur" ) );

    // No reference photo exists for this one in docs/modernization/phase1/screenshots -- that's
    //  expected per the Task 3 brief. Eyeballed against the app's current sidur-flavor UI instead.
    captureReaderScreen( "screenshots/sidur.png" );
  }


  private void captureReaderScreen(
    String  filePath
  ) {

    try ( ActivityScenario<ZehutYosefActivity> scenario = ActivityScenario.launch( ZehutYosefActivity.class ) ) {

      scenario.onActivity( activity -> {

        // onCreate() kicks off a real AsyncTask (readPrefsTask) to load font prefs before it
        //  finishes wiring up the drawer selection/typeface/size; wait for it the same bounded
        //  way we do in ZmanimSnapshotTest, rather than assuming a single idle() is enough.
        settle();

        // Overwrite the live "today" the activity computed at construction time with a fixed
        //  date, and refresh the toolbar's date badge to match -- see the class Javadoc for why
        //  this goes through reflection rather than a production-code seam.
        fixHebrewDate( activity );

        // The tefillah text overflows the ScrollView (R.id.scrollView in
        //  activity_zehut_yosef.xml), so Android "awakens" its scrollbar with a one-shot fade
        //  on first layout. That fade's alpha at any instant depends on real elapsed wall-clock
        //  time, which is not something this test controls -- recording then immediately
        //  verifying produced a few-pixel diff in exactly the scrollbar thumb's rectangle.
        //  Disabling the scrollbar removes that non-determinism; it doesn't hide anything the
        //  brief asked this snapshot to capture.
        View scrollView = activity.findViewById( com.gindin.zehutyosef.R.id.scrollView );
        if ( scrollView != null ) {
          scrollView.setVerticalScrollBarEnabled( false );
        }

        View root = activity.findViewById( android.R.id.content );
        capture( root, filePath );
      } );
    }
  }


  private boolean isFlavor(
    String  flavorName
  ) {
    return flavorName.equals( BuildConfig.FLAVOR );
  }


  /**
   * {@code ZehutYosefActivity.onCreate()}'s {@code readPrefsTask} does trivial work (a
   *  SharedPreferences read and a font lookup), but it still runs on a real background thread
   *  under Robolectric, with its {@code onPostExecute} posted back to the paused main looper.
   *  There's no externally observable "done" signal on this activity to poll for (unlike
   *  ZmanimSnapshotTest's zman row text), so this settles for a short, fixed bound of
   *  idle+sleep cycles -- generous relative to how fast that background work actually completes.
   */
  private void settle() {

    for ( int attempt = 0; attempt < 25; attempt++ ) {

      Shadows.shadowOf( Looper.getMainLooper() ).idle();

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
   * Drives {@code ZehutYosefActivity}'s private {@code hebrewDate} field and
   *  {@code updateHebrewDate()} method via reflection, exactly the way its own
   *  {@code onDateSet()} callback already does in production, so both the toolbar date badge and
   *  (should any future capture exercise it) any tefillah content keyed off {@code hebrewDate}
   *  are pinned to a fixed date instead of real wall-clock "today".
   */
  private void fixHebrewDate(
    ZehutYosefActivity  activity
  ) {

    HebrewDate fixedDate = HebrewCalendar.getForDate( FIXED_YEAR, FIXED_MONTH, FIXED_DAY_OF_MONTH );

    try {
      Field hebrewDateField = ZehutYosefActivity.class.getDeclaredField( "hebrewDate" );
      hebrewDateField.setAccessible( true );
      hebrewDateField.set( activity, fixedDate );

      Method updateHebrewDateMethod = ZehutYosefActivity.class.getDeclaredMethod( "updateHebrewDate" );
      updateHebrewDateMethod.setAccessible( true );
      updateHebrewDateMethod.invoke( activity );
    }
    catch ( ReflectiveOperationException reflectionFailure ) {
      throw new RuntimeException( "Unable to pin ZehutYosefActivity's Hebrew date for a deterministic snapshot", reflectionFailure );
    }
  }


  private void capture(
    View    view,
    String  filePath
  ) {

    RoborazziOptions options = RoborazziContextKt.provideRoborazziContext().getOptions();
    RoborazziKt.captureRoboImage( view, filePath, options );
  }

} // End of SiddurSnapshotTest class
