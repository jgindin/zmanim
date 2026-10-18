/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Polling helpers for conditions that become true some time after an activity launches: the location arrives from a
 *  background job, the list fills, the window lays out.
 */
final class ViewWaits {

  private static final long                                 TIMEOUT_MILLIS = 30_000;

  private static final long                                 POLL_MILLIS = 100;

  /** How many consecutive polls a value must hold still before the layout counts as settled. */
  private static final int                                  STABLE_POLLS = 8;


  private ViewWaits() {
  }


  /** Evaluates {@code condition} on the main thread until it returns true, failing with {@code what} on timeout. */
  static void until(
    String            what,
    Callable<Boolean> condition
  ) throws Exception {

    long deadline = System.currentTimeMillis() + TIMEOUT_MILLIS;
    while ( !onMainThread( condition ) ) {
      if ( System.currentTimeMillis() > deadline ) {
        throw new AssertionError( "Timed out waiting for " + what );
      }
      Thread.sleep( POLL_MILLIS );
    }
  }


  /**
   * Waits until the vertical position of {@code view} on screen has stopped changing. Window insets and the padding
   *  that reacts to them arrive over several layout passes, so a single early reading is not trustworthy.
   */
  static void untilSettled( View view ) throws Exception {

    int lastTop = Integer.MIN_VALUE;
    int stableFor = 0;

    long deadline = System.currentTimeMillis() + TIMEOUT_MILLIS;
    while ( stableFor < STABLE_POLLS ) {
      if ( System.currentTimeMillis() > deadline ) {
        throw new AssertionError( "Layout never settled" );
      }

      InstrumentationRegistry.getInstrumentation().waitForIdleSync();
      int top = onMainThread( () -> topOnScreen( view ) );
      stableFor = ( top == lastTop ) ? stableFor + 1 : 0;
      lastTop = top;

      Thread.sleep( POLL_MILLIS );
    }
  }


  /** Runs {@code callable} on the main thread, since views may only be inspected there. */
  static <T> T onMainThread( Callable<T> callable ) throws Exception {

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
      // Keep assertion failures as they are so the test report names the real assertion.
      throw (Error)thrown;
    }
    if ( thrown != null ) {
      throw new Exception( thrown );
    }
    return result.get();
  }


  static int topOnScreen( View view ) {
    int[] location = new int[ 2 ];
    view.getLocationOnScreen( location );
    return location[ 1 ];
  }


  static int bottomOnScreen( View view ) {
    return topOnScreen( view ) + view.getHeight();
  }


  /** The launched activity, for use from conditions that already run on the main thread. */
  static <A extends Activity> A activityOf( ActivityScenario<A> scenario ) {

    AtomicReference<A> holder = new AtomicReference<>();
    scenario.onActivity( holder::set );
    return holder.get();
  }


  /** The first {@link RecyclerView} below {@code root} in depth-first order, or null if there is none. */
  static RecyclerView firstRecyclerView( View root ) {

    if ( root instanceof RecyclerView ) {
      return (RecyclerView)root;
    }

    if ( root instanceof ViewGroup ) {
      ViewGroup group = (ViewGroup)root;
      for ( int i = 0; i < group.getChildCount(); i++ ) {
        RecyclerView found = firstRecyclerView( group.getChildAt( i ) );
        if ( found != null ) {
          return found;
        }
      }
    }

    return null;
  }

}
