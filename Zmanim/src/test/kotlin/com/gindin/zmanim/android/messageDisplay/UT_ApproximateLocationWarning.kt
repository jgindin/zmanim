/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay

import android.content.Context
import android.os.Looper
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gindin.zmanim.android.R
import com.gindin.zmanim.android.location.acquirers.LocationGrant
import com.gindin.zmanim.android.prefs.ZmanimPrefs
import com.google.android.material.snackbar.Snackbar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UT_ApproximateLocationWarning {

  private val activity =
    Robolectric.buildActivity(AppCompatActivity::class.java).create().get().apply {
      setTheme(R.style.AppTheme)
      setContentView(FrameLayout(this))
    }

  private val prefs = activity.getSharedPreferences(ZmanimPrefs.PERSONAL.name, Context.MODE_PRIVATE)

  private val warning = ApproximateLocationWarning.forContext(activity)

  private var usePreciseTaps = 0

  @Before
  fun clearPrefs() {
    prefs.edit().clear().commit()
  }

  private fun show(): Snackbar =
    warning.show(activity.findViewById(android.R.id.content)) { usePreciseTaps++ }.also { idle() }

  private fun idle() = shadowOf(Looper.getMainLooper()).idle()

  private fun Snackbar.button(id: Int) = view.findViewById<Button>(id)

  @Test
  fun itIsShownForAnApproximateOnlyGrantOnly() {
    assertTrue(warning.shouldShow(LocationGrant.APPROXIMATE))
    assertFalse(warning.shouldShow(LocationGrant.PRECISE))
    assertFalse(warning.shouldShow(LocationGrant.NONE))
  }

  @Test
  fun itSaysWhatApproximateLocationCostsAndOffersOkAndUsePrecise() {
    val snackbar = show()

    assertTrue(snackbar.isShown)
    val text = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    assertEquals(activity.getString(R.string.approximate_location_warning), text.text.toString())
    // The message is longer than the two lines a Snackbar allows.
    assertEquals(5, text.maxLines)
    assertEquals("OK", snackbar.button(R.id.approximate_location_ok).text.toString())
    assertEquals(
      "Use precise",
      snackbar.button(com.google.android.material.R.id.snackbar_action).text.toString(),
    )
  }

  @Test
  fun okHidesItForThisGrant() {
    val snackbar = show()

    snackbar.button(R.id.approximate_location_ok).performClick()
    idle()

    assertFalse(snackbar.isShownOrQueued)
    assertFalse(warning.shouldShow(LocationGrant.APPROXIMATE))
    assertEquals(0, usePreciseTaps)
  }

  @Test
  fun usePreciseAsksForPreciseLocationAndHidesItForThisGrant() {
    val snackbar = show()

    snackbar.button(com.google.android.material.R.id.snackbar_action).performClick()
    idle()

    assertEquals(1, usePreciseTaps)
    assertFalse(snackbar.isShownOrQueued)
    assertFalse(warning.shouldShow(LocationGrant.APPROXIMATE))
  }

  @Test
  fun aLaterApproximateOnlyGrantShowsItAgain() {
    show().button(R.id.approximate_location_ok).performClick()
    assertFalse(warning.shouldShow(LocationGrant.APPROXIMATE))

    // The user granted precise location, then went back to approximate.
    warning.shouldShow(LocationGrant.PRECISE)
    assertTrue(warning.shouldShow(LocationGrant.APPROXIMATE))

    show().button(R.id.approximate_location_ok).performClick()
    // The user took location away, then granted approximate again.
    warning.shouldShow(LocationGrant.NONE)
    assertTrue(warning.shouldShow(LocationGrant.APPROXIMATE))
  }

  @Test
  fun theDismissalIsKeptInThePersonalPreferences() {
    show().button(R.id.approximate_location_ok).performClick()

    assertTrue(prefs.getBoolean(ApproximateLocationWarning.DISMISSED_KEY, false))
    assertTrue(
      ApproximateLocationWarning.forContext(activity).shouldShow(LocationGrant.APPROXIMATE).not()
    )
  }
}
