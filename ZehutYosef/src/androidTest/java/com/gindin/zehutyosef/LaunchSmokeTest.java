/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Launches {@link ZehutYosefActivity} in a real process. The activity is shared by both product flavors, so this runs
 *  against whichever flavor's variant is being tested.
 */
@RunWith( AndroidJUnit4.class )
public class LaunchSmokeTest {

  private static final long                                 TIMEOUT_MILLIS = 30_000;


  @Test
  public void activityResumesAndShowsTheFirstSection() throws Exception {

    try ( ActivityScenario<ZehutYosefActivity> scenario = ActivityScenario.launch( ZehutYosefActivity.class ) ) {

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );

      AtomicReference<ZehutYosefActivity> holder = new AtomicReference<>();
      scenario.onActivity( holder::set );
      ZehutYosefActivity activity = holder.get();

      // The section text is loaded in the background after the activity starts.
      waitUntil( () -> ( (TextView)activity.findViewById( R.id.text_content ) ).getText().length() > 0 );

      onMainThread( () -> {
        View toolbar = activity.findViewById( R.id.toolbar );
        View content = activity.findViewById( R.id.text_content );
        assertTrue( "Toolbar is not shown", toolbar.getVisibility() == View.VISIBLE && toolbar.getHeight() > 0 );
        assertTrue( "Section text is not shown", content.getVisibility() == View.VISIBLE && content.getHeight() > 0 );
        return null;
      } );

      assertEquals( Lifecycle.State.RESUMED, scenario.getState() );
    }
  }


  private static void waitUntil( Callable<Boolean> condition ) throws Exception {

    long deadline = System.currentTimeMillis() + TIMEOUT_MILLIS;
    while ( !onMainThread( condition ) ) {
      if ( System.currentTimeMillis() > deadline ) {
        throw new AssertionError( "Timed out waiting for the first section's text" );
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
