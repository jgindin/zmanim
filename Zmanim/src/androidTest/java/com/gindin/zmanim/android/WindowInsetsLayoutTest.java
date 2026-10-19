/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Checks, on a real window, that the content the user needs to read is not drawn underneath the system bars.
 *<p/>
 *
 * From API 35 an app targeting API 35 or later is laid out edge to edge, and the classic ActionBar then overlays the
 *  top of the content instead of reserving space for it. Robolectric never reports a height for the ActionBar
 *  container, so only a device can show whether {@code EdgeToEdgeUtil.padForActionBarOverlay} is doing its job. On
 *  older API levels the framework already reserves the space, and these same assertions hold without any padding.
 */
@RunWith( AndroidJUnit4.class )
public class WindowInsetsLayoutTest {

  @Rule
  public final GrantPermissionRule                          permissions = AppState.grantRuntimePermissions();


  @Before
  public void saveStaticLocation() throws Exception {
    AppState.installStaticLocation();
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  @Test
  public void mainScreenHeaderStartsBelowTheActionBar() throws Exception {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      ZmanimActivity activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "the action bar, header and list to be laid out", () ->
        ( actionBar( activity ) != null )
          && ( actionBar( activity ).getHeight() > 0 )
          && ( ( (TextView)activity.findViewById( R.id.location ) ).getText().length() > 0 )
          && ( ( (ListView)activity.findViewById( android.R.id.list ) ).getChildCount() > 0 ) );
      ViewWaits.untilSettled( activity.findViewById( R.id.location ) );

      ViewWaits.onMainThread( () -> {

        View actionBar = actionBar( activity );
        int actionBarBottom = ViewWaits.bottomOnScreen( actionBar );
        int statusBarBottom = statusBarBottom( activity );

        assertTrue( "ActionBar (top " + ViewWaits.topOnScreen( actionBar ) + ") starts under the status bar (bottom "
            + statusBarBottom + ")",
          ViewWaits.topOnScreen( actionBar ) >= statusBarBottom );

        for ( int id : new int[] { R.id.location, R.id.secular_date, R.id.hebrew_date } ) {
          View header = activity.findViewById( id );
          assertTrue( "Header view " + activity.getResources().getResourceEntryName( id ) + " (top "
              + ViewWaits.topOnScreen( header ) + ") starts under the ActionBar (bottom " + actionBarBottom + ")",
            ViewWaits.topOnScreen( header ) >= actionBarBottom );
          assertTrue( "Header view " + activity.getResources().getResourceEntryName( id ) + " starts under the status bar",
            ViewWaits.topOnScreen( header ) >= statusBarBottom );
        }

        View firstRow = ( (ListView)activity.findViewById( android.R.id.list ) ).getChildAt( 0 );
        assertTrue( "First zman row (top " + ViewWaits.topOnScreen( firstRow ) + ") starts under the ActionBar (bottom "
            + actionBarBottom + ")",
          ViewWaits.topOnScreen( firstRow ) >= actionBarBottom );

        return null;
      } );
    }
  }


  @Test
  public void personalPreferencesListStartsBelowTheActionBar() throws Exception {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      PersonalPreferences activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "the action bar and preference rows to be laid out", () -> {
        RecyclerView rows = ViewWaits.firstRecyclerView( activity.findViewById( android.R.id.content ) );
        return ( actionBar( activity ) != null )
          && ( actionBar( activity ).getHeight() > 0 )
          && ( rows != null )
          && ( rows.getChildCount() > 0 );
      } );
      ViewWaits.untilSettled( ViewWaits.onMainThread( () ->
        ViewWaits.firstRecyclerView( activity.findViewById( android.R.id.content ) ).getChildAt( 0 ) ) );

      ViewWaits.onMainThread( () -> {

        View actionBar = actionBar( activity );
        int actionBarBottom = ViewWaits.bottomOnScreen( actionBar );
        int statusBarBottom = statusBarBottom( activity );

        assertTrue( "ActionBar (top " + ViewWaits.topOnScreen( actionBar ) + ") starts under the status bar (bottom "
            + statusBarBottom + ")",
          ViewWaits.topOnScreen( actionBar ) >= statusBarBottom );

        // The list's own top edge is what bounds the visible rows. The first row may start above it: on a small screen
        //  the list opens scrolled so a focused text field stays in view, which clips the row at the list's edge
        //  instead of hiding it under the ActionBar.
        RecyclerView rows = ViewWaits.firstRecyclerView( activity.findViewById( android.R.id.content ) );
        assertTrue( "Preference list (top " + ViewWaits.topOnScreen( rows ) + ") starts under the ActionBar (bottom "
            + actionBarBottom + ")",
          ViewWaits.topOnScreen( rows ) >= actionBarBottom );

        return null;
      } );
    }
  }


  private static View actionBar( Activity activity ) {
    return activity.getWindow().getDecorView().findViewById( androidx.appcompat.R.id.action_bar_container );
  }


  /** The lowest edge of the status bar on screen; content above this line cannot be seen by the user. */
  private static int statusBarBottom( Activity activity ) {

    WindowInsetsCompat rootInsets = ViewCompat.getRootWindowInsets( activity.getWindow().getDecorView() );
    assertNotNull( "The window has no insets yet", rootInsets );

    Insets statusBar = rootInsets.getInsets( WindowInsetsCompat.Type.statusBars() );
    return statusBar.top;
  }

}
