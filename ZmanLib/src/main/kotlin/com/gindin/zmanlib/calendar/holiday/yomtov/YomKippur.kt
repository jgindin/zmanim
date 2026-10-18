/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val NAME = "Yom Kippur"

/**
 *
 */
public class YomKippur : Observance() {

  override fun getName(): String {
    return NAME
  }


  @Suppress("FeatureEnvy")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.TISHREI == date.hebrewMonth) {
      if (9 == date.hebrewDayOfMonth) {
        return object : Details.Erev(date, NAME) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      }

      if (10 == date.hebrewDayOfMonth) {
        return YomKippurDetails(date)
      }
    }

    return null
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

}  // End of YomKippur class
