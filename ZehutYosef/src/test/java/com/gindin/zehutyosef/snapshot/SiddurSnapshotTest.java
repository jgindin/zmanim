/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef.snapshot;

import com.github.takahirom.roborazzi.RoborazziContextKt;
import com.github.takahirom.roborazzi.RoborazziKt;
import com.github.takahirom.roborazzi.RoborazziOptions;

import com.gindin.zehutyosef.BuildConfig;
import com.gindin.zehutyosef.ZehutYosefActivity;

import android.os.Looper;
import android.view.View;

import androidx.test.core.app.ActivityScenario;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

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
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class SiddurSnapshotTest {

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


  private void capture(
    View    view,
    String  filePath
  ) {

    RoborazziOptions options = RoborazziContextKt.provideRoborazziContext().getOptions();
    RoborazziKt.captureRoboImage( view, filePath, options );
  }

} // End of SiddurSnapshotTest class
