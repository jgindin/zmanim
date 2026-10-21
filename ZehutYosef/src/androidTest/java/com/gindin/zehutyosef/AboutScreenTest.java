/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mikepenz.aboutlibraries.ui.LibsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Opens the About screen the way the navigation drawer does. The screen is AboutLibraries' {@link LibsActivity}, which
 *  installs its own Toolbar as the action bar and so throws from onCreate when the theme it is given already supplies
 *  a window action bar. The activity is shared by both product flavors, so this runs against whichever flavor's
 *  variant is being tested.
 */
@RunWith( AndroidJUnit4.class )
public class AboutScreenTest {

  private static final long                                 TIMEOUT_MILLIS = 30_000;


  @Test
  public void aboutScreenResumesAndListsTheLibraries() throws Exception {

    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor( LibsActivity.class.getName(), null, false );

    try ( ActivityScenario<ZehutYosefActivity> scenario = ActivityScenario.launch( ZehutYosefActivity.class ) ) {

      scenario.onActivity( activity -> new About().show( activity ) );

      Activity about = monitor.waitForActivityWithTimeout( TIMEOUT_MILLIS );
      assertNotNull( "The About screen was not started", about );

      try {
        waitUntil( "the About screen to resume", () -> ( (LibsActivity)about ).getLifecycle().getCurrentState() == Lifecycle.State.RESUMED );

        // The entries are bound after the fragment is attached, so wait for the adapter to have them.
        waitUntil( "the library list to be populated", () -> {
          RecyclerView list = findRecyclerView( about.getWindow().getDecorView() );
          return list != null && list.getAdapter() != null && list.getAdapter().getItemCount() > 0;
        } );

        // On a platform that draws apps edge to edge the library's layout would otherwise run under the system bars.
        waitUntil( "the toolbar and list to sit between the system bars", () -> isBetweenSystemBars( about ) );

        assertEquals( Lifecycle.State.RESUMED, ( (LibsActivity)about ).getLifecycle().getCurrentState() );
      }
      finally {
        instrumentation.runOnMainSync( about::finish );
      }
    }
    finally {
      instrumentation.removeMonitor( monitor );
    }
  }


  /**
   * True when everything the toolbar shows is below the status bar, the list is below the toolbar, and the list ends
   *  above the navigation bar.
   */
  private static boolean isBetweenSystemBars( Activity about ) {

    View decor = about.getWindow().getDecorView();
    WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets( decor );
    View toolbar = about.findViewById( R.id.toolbar );
    RecyclerView list = findRecyclerView( decor );
    if ( windowInsets == null || toolbar == null || list == null || toolbar.getHeight() == 0 || list.getHeight() == 0 ) {
      return false;
    }

    Insets bars = windowInsets.getInsets( WindowInsetsCompat.Type.systemBars() );
    int[] location = new int[2];

    ViewGroup toolbarGroup = (ViewGroup)toolbar;
    for ( int i = 0; i < toolbarGroup.getChildCount(); i++ ) {
      View child = toolbarGroup.getChildAt( i );
      if ( child.getVisibility() == View.VISIBLE ) {
        child.getLocationOnScreen( location );
        if ( location[1] < bars.top ) {
          return false;
        }
      }
    }

    toolbar.getLocationOnScreen( location );
    int toolbarBottom = location[1] + toolbar.getHeight();
    list.getLocationOnScreen( location );
    int listTop = location[1];
    int listBottom = location[1] + list.getHeight();

    return listTop >= toolbarBottom && listBottom <= decor.getHeight() - bars.bottom;
  }


  private static RecyclerView findRecyclerView( View view ) {

    if ( view instanceof RecyclerView ) {
      return (RecyclerView)view;
    }
    if ( view instanceof ViewGroup ) {
      ViewGroup group = (ViewGroup)view;
      for ( int i = 0; i < group.getChildCount(); i++ ) {
        RecyclerView found = findRecyclerView( group.getChildAt( i ) );
        if ( found != null ) {
          return found;
        }
      }
    }
    return null;
  }


  private static void waitUntil( String description, Callable<Boolean> condition ) throws Exception {

    long deadline = System.currentTimeMillis() + TIMEOUT_MILLIS;
    while ( !onMainThread( condition ) ) {
      if ( System.currentTimeMillis() > deadline ) {
        throw new AssertionError( "Timed out waiting for " + description );
      }
      Thread.sleep( 100 );
    }
  }


  private static <T> T onMainThread( Callable<T> callable ) throws Exception {

    AtomicReference<T> result = new AtomicReference<>();
    AtomicReference<Throwable> failure = new AtomicReference<>();

    InstrumentationRegistry.getInstrumentation().runOnMainSync( () -> {
      try {
        result.set( callable.call() );
      }
      catch ( Throwable t ) {
        failure.set( t );
      }
    } );

    Throwable thrown = failure.get();
    if ( thrown instanceof Error ) {
      throw (Error)thrown;
    }
    if ( thrown != null ) {
      throw new Exception( thrown );
    }
    return result.get();
  }

}
