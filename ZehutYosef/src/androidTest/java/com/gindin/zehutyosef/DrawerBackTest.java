/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef;

import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Presses the system Back key, through the system rather than by calling the activity, with the section drawer open
 *  and closed. On Android 16 that is the predictive back path, which never calls the activity's onBackPressed().
 */
@RunWith( AndroidJUnit4.class )
public class DrawerBackTest {

  private static final long                                 TIMEOUT_MILLIS = 30_000;


  @Test
  public void backClosesAnOpenDrawerAndLeavesTheReaderOpen() throws Exception {

    try ( ActivityScenario<ZehutYosefActivity> scenario = ActivityScenario.launch( ZehutYosefActivity.class ) ) {

      AtomicReference<ZehutYosefActivity> holder = new AtomicReference<>();
      scenario.onActivity( holder::set );
      DrawerLayout drawerLayout = findDrawerLayout( holder.get() );
      waitForTheFirstSection( holder.get() );

      onMainThread( () -> {
        drawerLayout.openDrawer( GravityCompat.START );
        return null;
      } );
      waitUntil( "the drawer to open", () -> isOpen( drawerLayout ) );

      pressBack();

      waitUntil( "the drawer to close", () -> !isShowing( drawerLayout ) );
      AtomicBoolean finishing = new AtomicBoolean();
      scenario.onActivity( activity -> finishing.set( activity.isFinishing() ) );
      assertFalse( "Back closed the reader instead of the drawer", finishing.get() );
      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
    }
  }


  @Test
  public void backWithTheDrawerClosedLeavesTheReader() throws Exception {

    try ( ActivityScenario<ZehutYosefActivity> scenario = ActivityScenario.launch( ZehutYosefActivity.class ) ) {

      AtomicReference<ZehutYosefActivity> holder = new AtomicReference<>();
      scenario.onActivity( holder::set );
      ZehutYosefActivity activity = holder.get();
      DrawerLayout drawerLayout = findDrawerLayout( activity );
      waitForTheFirstSection( activity );

      onMainThread( () -> {
        drawerLayout.closeDrawer( GravityCompat.START );
        return null;
      } );
      waitUntil( "the drawer to close", () -> !isShowing( drawerLayout ) );
      assertFalse( "Something still takes Back", onMainThread( () -> activity.getOnBackPressedDispatcher().hasEnabledCallbacks() ) );

      pressBack();

      // Back on the first screen of a task finishes it, or, from Android 12, moves the task to the background.
      waitUntil( "the reader to go away", () -> activity.isFinishing() || !activity.hasWindowFocus() );
    }
  }


  /** Loading the first section, in the background at startup, closes the drawer; wait for it before using the drawer. */
  private static void waitForTheFirstSection( final ZehutYosefActivity activity ) throws Exception {

    waitUntil( "the first section's text",
      () -> ( (TextView)activity.findViewById( R.id.text_content ) ).getText().length() > 0 );
  }


  private static DrawerLayout findDrawerLayout( final ZehutYosefActivity activity ) throws Exception {

    DrawerLayout found = onMainThread( () -> find( activity.getWindow().getDecorView() ) );
    if ( found == null ) {
      throw new AssertionError( "The reader has no drawer" );
    }
    return found;
  }


  private static DrawerLayout find( final View view ) {

    if ( view instanceof DrawerLayout ) {
      return (DrawerLayout)view;
    }
    if ( view instanceof ViewGroup ) {
      ViewGroup group = (ViewGroup)view;
      for ( int i = 0; i < group.getChildCount(); i++ ) {
        DrawerLayout found = find( group.getChildAt( i ) );
        if ( found != null ) {
          return found;
        }
      }
    }
    return null;
  }


  private static boolean isOpen( final DrawerLayout drawerLayout ) {

    return drawerLayout.isDrawerOpen( GravityCompat.START );
  }


  /** Whether any of the drawer is on screen, which it is while it slides closed. */
  private static boolean isShowing( final DrawerLayout drawerLayout ) {

    return drawerLayout.isDrawerVisible( GravityCompat.START );
  }


  /** Sends the key through the system's input pipeline, as a user's Back does. */
  private static void pressBack() throws IOException {

    ParcelFileDescriptor output = InstrumentationRegistry.getInstrumentation().getUiAutomation()
      .executeShellCommand( "input keyevent KEYCODE_BACK" );
    try ( FileInputStream in = new ParcelFileDescriptor.AutoCloseInputStream( output ) ) {
      byte[] buffer = new byte[ 256 ];
      //noinspection StatementWithEmptyBody
      while ( in.read( buffer ) != -1 ) {
        // The command is done when its output closes.
      }
    }
  }


  private static void waitUntil( final String what, final Callable<Boolean> condition ) throws Exception {

    long deadline = System.currentTimeMillis() + TIMEOUT_MILLIS;
    while ( !onMainThread( condition ) ) {
      if ( System.currentTimeMillis() > deadline ) {
        throw new AssertionError( "Timed out waiting for " + what );
      }
      Thread.sleep( 100 );
    }
  }


  private static <T> T onMainThread( final Callable<T> callable ) throws Exception {

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
