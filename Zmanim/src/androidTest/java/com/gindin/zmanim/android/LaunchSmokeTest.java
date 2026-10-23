/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;

import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Launches the real activities in a real process with a static location already saved, and checks that each reaches
 *  the resumed state and shows what the user expects to see.
 */
@RunWith( AndroidJUnit4.class )
public class LaunchSmokeTest {

  /** The name the saved location cache gives to the saved coordinates. */
  static final String                                       SAVED_LOCATION_NAME = "Albany, New York";

  @Rule
  public final GrantPermissionRule                          permissions = AppState.grantLocationPermissions();


  @Before
  public void saveStaticLocation() throws Exception {
    AppState.installStaticLocation();
    AppState.markNotificationQuestionAnswered();
    AppState.markExactAlarmQuestionAnswered();
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  @Test
  public void mainScreenResumesAndShowsLocationDateAndZmanim() throws Exception {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
      ZmanimActivity activity = ViewWaits.activityOf( scenario );

      // The cache is only proven to supply this name when the Geocoder cannot answer (an offline emulator); online
      //  the Geocoder may return the same name, so a broken cache path can go unnoticed on CI.
      ViewWaits.until( "the saved location name in the header", () ->
        SAVED_LOCATION_NAME.contentEquals( ( (TextView)activity.findViewById( R.id.location ) ).getText() ) );
      ViewWaits.until( "a non-empty zmanim list", () -> {
        ListView list = activity.findViewById( android.R.id.list );
        return ( list.getAdapter() != null ) && ( list.getAdapter().getCount() > 0 ) && ( list.getChildCount() > 0 );
      } );

      ViewWaits.onMainThread( () -> {
        assertTrue( "Gregorian date is empty", ( (TextView)activity.findViewById( R.id.secular_date ) ).getText().length() > 0 );
        assertTrue( "Hebrew date is empty", ( (TextView)activity.findViewById( R.id.hebrew_date ) ).getText().length() > 0 );

        ListView list = activity.findViewById( android.R.id.list );
        TextView firstZmanName = list.getChildAt( 0 ).findViewById( R.id.zman_name );
        TextView firstZmanTime = list.getChildAt( 0 ).findViewById( R.id.zman_time );
        assertTrue( "First zman has no name", firstZmanName.getText().length() > 0 );
        assertTrue( "First zman has no time", firstZmanTime.getText().length() > 0 );
        return null;
      } );

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
    }
  }


  @Test
  public void personalPreferencesResumeAndListPreferences() throws Exception {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
      PersonalPreferences activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "the preference rows", () -> {
        RecyclerView rows = ViewWaits.firstRecyclerView( activity.findViewById( android.R.id.content ) );
        return ( rows != null ) && ( rows.getChildCount() > 0 );
      } );
    }
  }

}
