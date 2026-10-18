/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance
import com.gindin.zmanlib.zman.Zmanim
import java.util.ArrayList
import java.util.Calendar
import java.util.Collections

/**
 * Fast days:
 *  - Gedaliah
 *  - 10 Tevet
 *  - Esther
 *  - 14 Nissan (Bechorot -- firstborn)
 *  - 17 Tammuz
 *  - Tisha B'Av
 *
 * These fast days may move around on the calendar such that, none of them can fall on Shabbat,
 *  and none, except 10 Tevet, can fall on Friday.
 *<p>
 * Note: The fact that these fast days push off to Sunday is from http://www.jewishgen.org/infofiles/m_calint.htm
 */
public abstract class Taanit protected constructor(
  /** Which month does the fast fall in? */
  @HebrewMonth.Constant
  private val monthOfFast: Int,

  /** Absent any reason to postpone the fast, what day of the month does it fall on? */
  @JvmField
  protected val normalDayOfFast: Int
) : Observance() {

  /** Can the fast take place on a Friday? */
  protected abstract fun canFallOnFriday(date: HebrewDate): Boolean

  protected abstract fun createErevDetails(date: HebrewDate): Details

  protected abstract fun createYomDetails(date: HebrewDate): Details


  /**
   * Can the fast be pushed back to Sunday if it were to fall on Shabbat? Usually yes, but Ta'anit Esther gets
   *  pulled back instead.
   */
  protected open fun canPushToSunday(): Boolean {
    return true
  }

  /**
   * Allows the subclass to change the month of the fast based on the specified date.
   */
  @HebrewMonth.Constant
  protected open fun getAdjustedMonthOfFast(date: HebrewDate): Int {
    return monthOfFast
  }


  @Suppress("FeatureEnvy")
  final override fun getDetailsFor(date: HebrewDate): Details? {

    @HebrewMonth.Constant
    val actualMonthOfFast = getAdjustedMonthOfFast(date)

    if (actualMonthOfFast != date.hebrewMonth) {
      return null
    }

    val dayOfWeek = date.dayOfWeek
    val jewishDayOfMonth = date.hebrewDayOfMonth

    if (Calendar.WEDNESDAY == dayOfWeek) {
      val details = checkWednesday(date)
      if (null != details) {
        return details
      }
    }

    if (Calendar.THURSDAY == dayOfWeek) {
      val details = checkThursday(date)
      if (null != details) {
        return details
      }
    }

    if (Calendar.FRIDAY == dayOfWeek) {

      if (canFallOnFriday(date)) {
        if (normalDayOfFast == jewishDayOfMonth) {
          return createYomDetails(date)
        }
      } else {
        // If we can't fall on Friday, then for sure this cannot be the Yom, but it also cannot be
        //  Erev, because we never have a Ta'anit on Shabbat (well, except Yom Kippur, but that's
        //  a whole separate case)...
        return null
      }
    }

    if (Calendar.SATURDAY == dayOfWeek) {
      if (canPushToSunday()) {
        if (normalDayOfFast == jewishDayOfMonth) {
          return createErevDetails(date)
        }
      } else {
        // If we can't push it, then today is nothing.
        return null
      }
    }

    if (Calendar.SUNDAY == dayOfWeek) {
      if (((normalDayOfFast + 1) == jewishDayOfMonth) && canPushToSunday()) {
        return createYomDetails(date)
      }
    }


    // We've handled all the special cases above...
    if (normalDayOfFast == jewishDayOfMonth) {
      return createYomDetails(date)
    }

    if ((normalDayOfFast - 1) == jewishDayOfMonth) {
      return createErevDetails(date)
    }


    return null
  }


  protected open fun checkWednesday(
    date: HebrewDate
  ): Details? {

    val jewishDayOfMonth = date.hebrewDayOfMonth

    if (canFallOnFriday(date)) {
      if ((normalDayOfFast - 1) == jewishDayOfMonth) {
        return createErevDetails(date)
      }
    } else {
      if ((normalDayOfFast - 2) == jewishDayOfMonth) {
        return createErevDetails(date)
      }
    }

    return null
  }


  protected open fun checkThursday(
    date: HebrewDate
  ): Details? {

    val jewishDayOfMonth = date.hebrewDayOfMonth

    if (canFallOnFriday(date)) {
      if ((normalDayOfFast - 1) == jewishDayOfMonth) {
        return createErevDetails(date)
      }
    } else {
      if ((normalDayOfFast - 1) == jewishDayOfMonth) {
        return createYomDetails(date)
      }
    }

    return null
  }


  public companion object {

    protected const val TAANIT: String = "Ta'anit "
  }


  protected open class ErevTaanitDetails internal constructor(
    date: HebrewDate,
    name: String
  ) : Details.Erev(
    date,
    name,
    // Can never have a fast that falls on Shabbat (except Yom Kippur, but that's handled by a separate
    //  Details class)
    false
  ) {

    override fun sayHavdalah(): Boolean {

      // We can have a fast day that falls on Sunday, in which case we would say Havdalah on erev of the fast...
      return isShabbat()
    }
  }


  protected open class TaanitDetails internal constructor(
    date: HebrewDate,
    name: String
  ) : Details.Yom(date, name) {

    override fun lightCandles(): Boolean {

      // At least one fast can fall on Erev Shabbat...
      return isErevShabbat()
    }


    override fun sayHavdalah(): Boolean {
      // No fast requires havdalah.
      return false
    }


    override fun isTaanit(): Boolean {
      return true
    }


    override fun getZmanim(): List<Zmanim.Type> {

      val zmanim = ArrayList<Zmanim.Type>()
      zmanim.add(Zmanim.Type.Alot)
      zmanim.add(Zmanim.Type.Talit)
      zmanim.add(Zmanim.Type.Hanetz)
      zmanim.add(Zmanim.Type.SofZmanShema)
      zmanim.add(Zmanim.Type.SofZmanTefila)
      zmanim.add(Zmanim.Type.Chatzot)
      zmanim.add(Zmanim.Type.MinchaGedola)
      zmanim.add(Zmanim.Type.MinchaKetana)
      zmanim.add(Zmanim.Type.PlagHaMincha)

      // Asara B'Tevet can fall on a Friday...
      if (lightCandles()) {
        zmanim.add(Zmanim.Type.CandleLighting)
      }

      zmanim.add(Zmanim.Type.Shkia)
      zmanim.add(Zmanim.Type.LatestMincha)
      zmanim.add(Zmanim.Type.BeinHashmashot)
      zmanim.add(Zmanim.Type.FastEnd)
      zmanim.add(Zmanim.Type.TzaitHacochavim)
      zmanim.add(Zmanim.Type.NightChatzot)

      return Collections.unmodifiableList(zmanim)
    }
  }

}  // End of Taanit class
