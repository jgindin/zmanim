/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef.snapshot;

import com.github.takahirom.roborazzi.RoborazziContextKt;
import com.github.takahirom.roborazzi.RoborazziKt;
import com.github.takahirom.roborazzi.RoborazziOptions;

import com.gindin.zehutyosef.BuildConfig;
import com.gindin.zehutyosef.Section;
import com.gindin.zehutyosef.ZehutYosefActivity;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;

import android.os.Looper;
import android.view.View;
import android.widget.TextView;

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
import java.util.List;

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
 *  flavor-less {@code recordRoborazziDebug} the way the single-flavor {@code :Zmanim} module
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
 *<p/>
 *
 * The toolbar badge is not the only thing keyed off {@code hebrewDate}: {@code onCreate()}'s
 *  {@code readPrefsTask.onPostExecute()} also drives the drawer's default section selection
 *  ({@code drawer.setSelection(sections.get(0).asDrawerItem())}) into {@code handleItem()}, which
 *  calls {@code section.load(hebrewDate, getAssets(), textContent)} -- and this fires during
 *  {@link #settle()}, i.e. <em>before</em> {@link #fixHebrewDate} ever runs, using the real
 *  {@code HebrewCalendar.today()} value set in the activity's constructor. That load's text
 *  content (via {@code Tefilla.getTextInfoForDate()} / {@code DoWeSay}) genuinely varies by real
 *  calendar day -- Friday's erev-Shabbat insertions, tachanun being skipped on Shabbat/holidays,
 *  the aseret-yemei-teshuva insertions -- so leaving that first, live-dated load as the captured
 *  body text would just relocate the live-clock dependency from the toolbar into the tefillah
 *  text itself. {@link #reloadSectionText} re-issues that same section load, this time with the
 *  now-fixed date, immediately after {@link #fixHebrewDate}.
 *<p/>
 *
 * That reissue only works if the <em>first</em>, live-dated load has actually finished before the
 *  second one starts -- {@code Section.load()} kicks off a real {@code LoadSectionTextTask}
 *  ({@code AsyncTask}) whose {@code doInBackground()} does an initial 300ms sleep plus a 25ms
 *  sleep per text chunk (Mincha's amidah alone is 25+ chunks depending on which day-dependent
 *  insertions apply), so it can easily still be running after {@link #settle()}'s fixed,
 *  short iteration budget -- confirmed empirically: {@code Section}'s private
 *  {@code loadingSectionTask} field was still non-null after a single {@link #settle()} call.
 *  Calling {@code load()} again while that first task is still posting {@code onProgressUpdate}
 *  callbacks to the (paused, then pumped) main looper races the two tasks against each other on
 *  the same {@code TextView}, corrupting the captured text with an interleaved mix of both dates'
 *  content instead of cleanly replacing it (confirmed by temporarily instrumenting {@code
 *  textContent}: length nearly doubled and the live date's aseret-yemei-teshuva marker text was
 *  still present after the "reissued" load, because the first task's callbacks kept
 *  overwriting/appending after the second task's ran). {@link #waitForSectionLoad} polls
 *  {@code loadingSectionTask} (pumping the main looper each iteration so its callbacks actually
 *  run) until it goes {@code null}, with a bound generous enough for the worst-case chunk count;
 *  it is called once after {@link #settle()} (to let the first, live-dated load fully finish
 *  before touching anything) and again after {@link #reloadSectionText} (to let the second,
 *  fixed-dated load fully finish before capture). With that wait in place, the same
 *  instrumentation confirmed the fixed date genuinely reaches {@code Mincha.getTefillaText()}:
 *  after the first (live-dated) load, the loaded text contained the aseret-yemei-teshuva marker
 *  (the real date, September 16 2026 / 5 Tishrei 5787, falls in that window); after the second
 *  (fixed-dated) load, it did not (September 7 2026 / 25 Elul 5786 does not).
 *<p/>
 *
 * One more subtlety worth recording: re-recording the goldens after this fix produced
 *  byte-identical PNGs to the unfixed version. That is not a sign the fix had no effect --
 *  {@code Section.getTextInfoForDate()}'s day-dependent insertions (aseret yemei teshuva,
 *  tachanun, erev Shabbat) all occur well after {@code mincha_intro.txt} and
 *  {@code pitum_haketoret.txt} (9,199 characters combined, neither date-dependent), which alone
 *  fill the entire visible, non-scrolled capture area at this screen size -- confirmed by
 *  inspecting the recorded PNG directly. So for this specific fixed date, the underlying
 *  live-clock dependency this fix closes happens not to be visually distinguishable in the
 *  captured pixels; it was confirmed by inspecting the loaded text content directly (see above),
 *  not by an image diff.
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

        Section defaultSection = getDefaultSection( activity );

        // readPrefsTask's onPostExecute() (pumped by settle() above) already triggered the
        //  drawer's default-selection load with the real, live hebrewDate -- let that fully
        //  finish before touching anything else, so the reissued load below (with the fixed
        //  date) doesn't race against it. See class Javadoc for why this matters.
        waitForSectionLoad( defaultSection );

        // Overwrite the live "today" the activity computed at construction time with a fixed
        //  date, and refresh the toolbar's date badge to match -- see the class Javadoc for why
        //  this goes through reflection rather than a production-code seam.
        HebrewDate fixedDate = fixHebrewDate( activity );

        // The live-dated load above already finished, so it's now safe to reissue that same
        //  section load with the corrected date -- see class Javadoc for why day-dependent text
        //  (tachanun, erev Shabbat, aseret yemei teshuva, ...) would otherwise still be keyed off
        //  the real calendar day despite the toolbar badge being fixed. Wait for this second load
        //  to finish too before capturing.
        reloadSectionText( activity, defaultSection, fixedDate );
        waitForSectionLoad( defaultSection );

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
   *  {@code onDateSet()} callback already does in production, so the toolbar date badge is pinned
   *  to a fixed date instead of real wall-clock "today". Returns the fixed date so the caller can
   *  also drive {@link #reloadSectionText} with the same value -- this method only updates the
   *  toolbar; it does not touch the already-loaded tefillah body text (see class Javadoc).
   */
  private HebrewDate fixHebrewDate(
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

    return fixedDate;
  }


  /**
   * {@code ZehutYosefActivity}'s {@code sections} field is private; fetches it via reflection and
   *  returns element 0 -- the same section {@code onCreate()}'s drawer default-selection wiring
   *  (and consequently {@link #reloadSectionText}/{@link #waitForSectionLoad}) always operate on.
   */
  private Section getDefaultSection(
    ZehutYosefActivity  activity
  ) {

    try {
      Field sectionsField = ZehutYosefActivity.class.getDeclaredField( "sections" );
      sectionsField.setAccessible( true );

      @SuppressWarnings( "unchecked" )
      List<Section> sections = (List<Section>)sectionsField.get( activity );

      return sections.get( 0 );
    }
    catch ( ReflectiveOperationException reflectionFailure ) {
      throw new RuntimeException( "Unable to fetch ZehutYosefActivity's default section for a deterministic snapshot", reflectionFailure );
    }
  }


  /**
   * Re-issues the default section's tefillah text load with the now-fixed date, so the captured
   *  body content stops depending on whatever real calendar day the test happens to run on (see
   *  class Javadoc for the full explanation of why this is necessary). {@link Section} and its
   *  {@code load(HebrewDate, AssetManager, TextView)} method are both public, so no reflection is
   *  needed to drive the reload itself -- only {@code R.id.text_content} needs to be looked up the
   *  same way the activity itself does in {@code onCreate()}, since {@code textContent} is a
   *  private field. Caller must have already awaited the section's prior load (see class Javadoc
   *  on why racing two concurrent loads corrupts the captured text).
   */
  private void reloadSectionText(
    ZehutYosefActivity  activity,
    Section             defaultSection,
    HebrewDate          fixedDate
  ) {

    TextView textContent = (TextView)activity.findViewById( com.gindin.zehutyosef.R.id.text_content );
    defaultSection.load( fixedDate, activity.getAssets(), textContent );
  }


  /**
   * Polls {@link Section}'s private {@code loadingSectionTask} field (set back to {@code null} in
   *  {@code Section.load()}'s {@code onPostExecute} override) until the section's in-flight
   *  {@code LoadSectionTextTask} finishes, pumping the main looper each iteration so that task's
   *  {@code onProgressUpdate}/{@code onPostExecute} callbacks -- posted from a real background
   *  thread -- actually get to run. See class Javadoc for why a fixed, short {@link #settle()}
   *  budget is not reliable enough for this specific wait: the load's own background work
   *  (an initial 300ms sleep plus 25ms per text chunk, and Mincha alone has 25+ chunks depending
   *  on which day-dependent insertions apply) can outlast it. The iteration bound here (100
   *  attempts x up to ~20ms of idle+sleep each) is generous relative to that worst case.
   */
  private void waitForSectionLoad(
    Section  section
  ) {

    try {
      Field loadingSectionTaskField = Section.class.getDeclaredField( "loadingSectionTask" );
      loadingSectionTaskField.setAccessible( true );

      for ( int attempt = 0; attempt < 100; attempt++ ) {

        Shadows.shadowOf( Looper.getMainLooper() ).idle();

        if ( loadingSectionTaskField.get( section ) == null ) {
          return;
        }

        Thread.sleep( 20 );
      }

      throw new IllegalStateException(
        "Section text load did not finish within the bounded wait -- capturing now would have " +
        "recorded a partially-loaded, non-deterministic body" );
    }
    catch ( ReflectiveOperationException reflectionFailure ) {
      throw new RuntimeException( "Unable to inspect Section's loadingSectionTask for a deterministic snapshot", reflectionFailure );
    }
    catch ( InterruptedException interrupted ) {
      Thread.currentThread().interrupt();
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
