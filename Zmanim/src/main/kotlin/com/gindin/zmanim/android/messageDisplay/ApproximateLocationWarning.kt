/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay

import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.gindin.zmanim.android.R
import com.gindin.zmanim.android.location.acquirers.LocationGrant
import com.gindin.zmanim.android.prefs.ZmanimPrefs
import com.google.android.material.snackbar.Snackbar

/**
 * Tells the user that, with approximate location only, the times may be off by a few seconds. The
 * app accepts an approximate-only grant; this is the one place it says what that costs.
 *
 * It is shown once per approximate-only grant: either button dismisses it for good, and a grant of
 * precise location, or none, forgets that, so a later approximate-only grant shows it again. It is
 * a Snackbar, so it does not stop the user from reading the times.
 */
class ApproximateLocationWarning(private val prefs: SharedPreferences) {

  /**
   * Whether to show the warning for [grant]. Anything but an approximate-only grant forgets an
   * earlier dismissal.
   */
  fun shouldShow(grant: LocationGrant): Boolean {
    if (grant != LocationGrant.APPROXIMATE) {
      if (prefs.getBoolean(DISMISSED_KEY, false)) prefs.edit().remove(DISMISSED_KEY).apply()
      return false
    }
    return !prefs.getBoolean(DISMISSED_KEY, false)
  }

  /**
   * Shows the warning at the bottom of [anchor]'s window, with an "OK" button and a "Use precise"
   * button that calls [onUsePrecise], which asks for precise location.
   */
  fun show(anchor: View, onUsePrecise: () -> Unit): Snackbar {

    val snackbar =
      Snackbar.make(anchor, R.string.approximate_location_warning, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.approximate_location_use_precise) {
          dismiss()
          onUsePrecise()
        }

    // A Snackbar has one action, so "OK" is a second button made to look the same, and the two sit
    //  together where the action was. The message is longer than the two lines a Snackbar allows.
    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines =
      MESSAGE_MAX_LINES
    val usePrecise =
      snackbar.view.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
    val ok =
      Button(anchor.context, null, androidx.appcompat.R.attr.borderlessButtonStyle).apply {
        id = R.id.approximate_location_ok
        setText(R.string.approximate_location_ok)
        setTextColor(usePrecise.textColors)
        minWidth = usePrecise.minWidth
        setOnClickListener {
          dismiss()
          snackbar.dismiss()
        }
      }
    val row = usePrecise.parent as ViewGroup
    val at = row.indexOfChild(usePrecise)
    val actionSize = usePrecise.layoutParams
    row.removeView(usePrecise)
    val buttons =
      LinearLayout(anchor.context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.END or Gravity.CENTER_VERTICAL
        addView(ok, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
        addView(usePrecise, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
      }
    row.addView(buttons, at, actionSize)

    snackbar.show()
    return snackbar
  }

  private fun dismiss() {
    prefs.edit().putBoolean(DISMISSED_KEY, true).apply()
  }

  companion object {

    /**
     * Key, in the [ZmanimPrefs.PERSONAL] preferences, that is true once the user has dismissed the
     * warning for the current approximate-only grant.
     */
    const val DISMISSED_KEY: String = "APPROXIMATE_LOCATION_WARNING_DISMISSED"

    private const val MESSAGE_MAX_LINES = 5

    @JvmStatic
    fun forContext(context: Context): ApproximateLocationWarning =
      ApproximateLocationWarning(
        context.getSharedPreferences(ZmanimPrefs.PERSONAL.name, Context.MODE_PRIVATE)
      )
  }
}
