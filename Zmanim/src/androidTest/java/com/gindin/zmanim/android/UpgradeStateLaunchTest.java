/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Starts the current build on top of the saved state of the released version 409 app (every preference file and
 *  every data file it wrote), as it is for a user who has just updated.
 *<p/>
 *
 * {@link AppState#installUpgradeState()} writes the state before the activity exists, and the preferences go through
 *  the same cached {@code SharedPreferences} objects the activity then reads, so the saved values are in force from
 *  the very first read regardless of what earlier tests in this process loaded.
 */
@RunWith( AndroidJUnit4.class )
public class UpgradeStateLaunchTest {

  /** The versionCode of the released app whose saved state is installed. */
  private static final int                                  SAVED_BY_VERSION = 409;

  @Rule
  public final GrantPermissionRule                          permissions = AppState.grantRuntimePermissions();


  @Before
  public void installReleasedState() throws Exception {
    AppState.installUpgradeState();
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  @Test
  public void launchesOnTheSavedStateAndKeepsIt() throws Exception {

    assertTrue( "The build under test must be newer than the saved state", BuildConfig.VERSION_CODE > SAVED_BY_VERSION );

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
      ZmanimActivity activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "a header location", () ->
        ( (TextView)activity.findViewById( R.id.location ) ).getText().length() > 0 );
      ViewWaits.until( "a non-empty zmanim list", () -> {
        ListView list = activity.findViewById( android.R.id.list );
        return ( list.getAdapter() != null ) && ( list.getAdapter().getCount() > 0 );
      } );

      assertEquals( "The upgrade must record the new version", BuildConfig.VERSION_CODE,
        preferences( ZmanimPrefs.VERSION ).getInt( "lastKnownCodeVersion", -1 ) );
      assertEquals( "Saved zman choice must survive the upgrade", "Seattle Sephardic",
        preferences( ZmanimPrefs.ZMANIM_SELECTION ).getString( "Fast Ends", null ) );
      assertEquals( "Saved notification lead time must survive the upgrade", 90,
        preferences( ZmanimPrefs.PERSONAL ).getInt( "Shkia", -1 ) );
      assertTrue( "Saved lat/long choice must survive the upgrade",
        preferences( ZmanimPrefs.LOCATION_MANAGEMENT ).getBoolean( "LATITUDE_LONGITUDE_POSITION", false ) );
    }
  }


  /**
   * The saved location cache holds an entry the user typed in as {@code Saratoga Springs, NY}. Asking for that place
   *  again must be answered from the cache, which gives the place its full name, rather than from a geocoding service
   *  (which would need the network, and which the released app's cache exists to avoid).
   */
  @Test
  public void typedPlaceIsAnsweredFromTheSavedLocationCache() throws Exception {

    AppState.useTypedPlace( "Saratoga Springs, NY" );

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      ZmanimActivity activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "the cached location name in the header", () ->
        "Saratoga Springs, New York".contentEquals( ( (TextView)activity.findViewById( R.id.location ) ).getText() ) );
      ViewWaits.until( "a non-empty zmanim list", () -> {
        ListView list = activity.findViewById( android.R.id.list );
        return ( list.getAdapter() != null ) && ( list.getAdapter().getCount() > 0 );
      } );
    }
  }


  @Test
  public void personalPreferencesOpenOnTheSavedState() throws Exception {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
      PersonalPreferences activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "the preference rows", () -> {
        RecyclerView rows = ViewWaits.firstRecyclerView( activity.findViewById( android.R.id.content ) );
        return ( rows != null ) && ( rows.getChildCount() > 0 );
      } );
    }
  }


  private static SharedPreferences preferences( ZmanimPrefs prefs ) {
    return InstrumentationRegistry.getInstrumentation().getTargetContext()
      .getSharedPreferences( prefs.name(), Context.MODE_PRIVATE );
  }

}
