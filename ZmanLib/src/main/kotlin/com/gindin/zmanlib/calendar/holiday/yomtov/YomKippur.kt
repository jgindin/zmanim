/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val NAME = "Yom Kippur"

/** Yom Kippur is on the 10th of Tishrei. */
private const val DAY_OF_MONTH = 10
private const val EREV_DAY_OF_MONTH = DAY_OF_MONTH - 1

/**  */
public class YomKippur : Observance() {

  override fun getName(): String {
    return NAME
  }

  @Suppress("FeatureEnvy")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.TISHREI != date.hebrewMonth) {
      return null
    }

    return when (date.hebrewDayOfMonth) {
      EREV_DAY_OF_MONTH ->
        object : Details.Erev(date, NAME) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      DAY_OF_MONTH -> YomKippurDetails(date)
      else -> null
    }
  }

  private class YomKippurDetails(date: HebrewDate) : Details(date) {

    override fun isErev(): Boolean {
      return false
    }

    override fun isYomTov(): Boolean {
      return true
    }

    override fun isYomKippur(): Boolean {
      return true
    }

    override fun lightCandles(): Boolean {
      return false
    }

    override fun sayHavdalah(): Boolean {
      return true
    }

    override fun sayTachanun(): Boolean {
      return false
    }

    override fun getDescription(): String {
      return NAME
    }
  }
} // End of YomKippur class
