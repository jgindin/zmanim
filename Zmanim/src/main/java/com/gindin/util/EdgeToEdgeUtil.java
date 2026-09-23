/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;


public class EdgeToEdgeUtil {


  /**
   * Pads {@code view}'s top so it clears the classic system-provided ActionBar (as used by
   *  {@code Theme.AppCompat} without an embedded {@code Toolbar}).
   *
   * The status bar itself does NOT need handling here: on a real device, AppCompat's own
   *  {@code ContentFrameLayout}/{@code ActionBarOverlayLayout} already positions
   *  {@code android.R.id.content} correctly below the status bar without any app code --
   *  confirmed by inspecting the live view hierarchy on a real device with no insets handling
   *  present at all. The ActionBar itself, though, renders as an overlay on top of content rather
   *  than reserving its own space, so without this, the location/date header and the top of the
   *  zmanim list render underneath it.
   *
   * This measures the ActionBar's actual on-screen position rather than assuming a fixed value
   *  like {@code ?attr/actionBarSize}: on a real device, the ActionBar's rendered top sits lower
   *  than that attribute alone predicts (confirmed empirically -- there's a real gap between
   *  where the status bar ends and where the ActionBar actually starts, on top of the ActionBar's
   *  own height, that {@code actionBarSize} doesn't account for). Measuring the ActionBar's real
   *  bottom edge and matching it exactly is correct regardless of what causes that gap.
   *
   * Call once, right after the activity's content view is set (or, for an activity that uses
   *  {@code android.R.id.content} directly as a fragment container, right after
   *  {@code super.onCreate()}).
   *
   * Verified end-to-end against a real device (Roborazzi/Robolectric can't verify this one):
   *  {@code action_bar_container} never reports a nonzero height under Robolectric, so this
   *  method's guard against measuring too early never clears and the padding silently never
   *  applies there -- harmless (no crash, no hang, just a no-op), and consistent with a real gap
   *  already noted elsewhere in this codebase between what Robolectric simulates and real device
   *  window-manager/compositor behavior (see the JDK/CLDR AM-PM note in
   *  zmanim.android-application-conventions.gradle.kts for another example of the same class of
   *  gap). The recorded Roborazzi goldens reflect this: they still show content starting flush at
   *  the top, same as before this fix, since that's genuinely what Robolectric renders.
   */
  public static void padForActionBarOverlay(
    Activity activity,
    View     view
  ) {

    View decorView = activity.getWindow().getDecorView();

    decorView.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {

      @Override
      public void onGlobalLayout() {

        View actionBarContainer = decorView.findViewById( androidx.appcompat.R.id.action_bar_container );
        if ( ( actionBarContainer == null ) || ( actionBarContainer.getHeight() == 0 ) ) {
          // Not laid out yet (or this theme has no ActionBar at all) -- wait for a later pass.
          return;
        }

        int[] actionBarLocation = new int[2];
        actionBarContainer.getLocationOnScreen( actionBarLocation );
        int actionBarBottom = actionBarLocation[1] + actionBarContainer.getHeight();

        int[] viewLocation = new int[2];
        view.getLocationOnScreen( viewLocation );
        int viewTopWithoutPadding = viewLocation[1] - view.getPaddingTop();

        int neededTopPadding = actionBarBottom - viewTopWithoutPadding;

        if ( neededTopPadding > view.getPaddingTop() ) {
          view.setPadding(
            view.getPaddingLeft(), neededTopPadding, view.getPaddingRight(), view.getPaddingBottom() );
        }

        decorView.getViewTreeObserver().removeOnGlobalLayoutListener( this );
      }
    } );
  }


} // End of EdgeToEdgeUtil class
