/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar

import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Holidays
import com.gindin.zmanlib.location.ZmanimLocation
import com.gindin.zmanlib.zman.Zmanim
import com.gindin.zmanlib.zman.ZmanimFactory
import com.gindin.zmanlib.zman.ZmanimList
import java.util.Calendar
import java.util.Date

/**
 * A HebrewDate represents a specific date obtained from a HebrewCalendar. Each instance is immutable once created.
 */
@Suppress("UnusedDeclaration", "ClassWithTooManyFields", "ClassWithTooManyMethods")
public class HebrewDate internal constructor(
  /** The Hebrew year (e.g., 5772). */
  public val hebrewYear: Int,

  /**
   * The Hebrew month 1-12 (or 13 years in a leap year). The month count starts with 1 for Nisan and goes to 13
   * for Adar II
   */
  @param:HebrewMonth.Constant @get:HebrewMonth.Constant
  public val hebrewMonth: Int,

  /** The Hebrew day of month. */
  public val hebrewDayOfMonth: Int,

  /** The Gregorian year (e.g., 2012). */
  public val gregorianYear: Int,

  /**
   * The Gregorian month, in a format compatible with java.util.Calendar's definition of month zero being January,
   *  through to month 11 being December. Can be directly compared to the months as defined in java.util.Calendar.
   */
  @param:CalendarMonth @get:CalendarMonth
  public val gregorianMonth: Int,

  /** The day of the Gregorian month */
  public val gregorianDayOfMonth: Int,

  /** The day of the week as a number between 1-7. Strictly compatible with Calendar.SUNDAY, etc... */
  @param:CalendarDay @get:CalendarDay
  public val dayOfWeek: Int,

  /** Internal representation of the date. */
  internal val gregorianAbsDate: Int,

  moladHours: Int,

  /**
   * The molad minutes. Only a HebrewDate created via the getMolad() method will have this field set. Other
   *  HebrewDates will have this field set to 0.
   */
  public val moladMinutes: Int,

  /**
   * The molad chalakim/parts. Only a HebrewDate created via the getMolad() method will have this field set. Other
   *  HebrewDates will have this field set to 0.
   */
  public val moladChalakim: Int
) : Comparable<HebrewDate> {

  /**
   * The molad hours. Only a HebrewDate created via the getMolad() method will have this field set. Other
   *  HeberwDates will have this field set to 0.
   */
  public var moladHours: Int = moladHours
    private set


  /** Cache the details lookup. */
  private var dateDetails: Details? = null


  internal constructor(
    hebrewYear: Int,
    @HebrewMonth.Constant hebrewMonth: Int,
    hebrewDayOfMonth: Int,
    gregorianYear: Int,
    @CalendarMonth gregorianMonth: Int,
    gregorianDayOfMonth: Int,
    @CalendarDay dayOfWeek: Int,
    gregorianAbsDate: Int
  ) : this(
    hebrewYear, hebrewMonth, hebrewDayOfMonth, gregorianYear, gregorianMonth, gregorianDayOfMonth, dayOfWeek,
    gregorianAbsDate, 0, 0, 0
  )


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Find out about this date
  public fun getDescription(): String {
    return getDetails().getDescription()
  }


  public fun getDetails(): Details {

    val cached = dateDetails
    if (null != cached) {
      return cached
    }

    // Create a NullDetails here...it avoids a bunch of null checking...
    var details: Details = NullDetails(this)
    dateDetails = details

    fun compose(additional: Details?) {
      if (null != additional) {
        details = details.composeWith(additional)
        dateDetails = details
      }
    }

    compose(Holidays.SHABBAT.getDetailsFor(this))

    compose(Holidays.ROSH_CHODESH.getDetailsFor(this))

    for (holiday in Holidays.MAJOR_HOLIDAYS) {
      compose(holiday.getDetailsFor(this))
    }

    for (holiday in Holidays.MINOR_HOLIDAYS) {
      compose(holiday.getDetailsFor(this))
    }

    for (fastDay in Holidays.FAST_DAYS) {
      compose(fastDay.getDetailsFor(this))
    }

    for (modernHoliday in Holidays.MODERN_HOLIDAYS) {
      compose(modernHoliday.getDetailsFor(this))
    }

    for (observance in Holidays.OTHER) {
      compose(observance.getDetailsFor(this))
    }


    return details
  }


  public fun getZmanim(
    location: ZmanimLocation
  ): ZmanimList {

    val details = getDetails()
    val zmanimList = ZmanimList()

    for (type in details.getZmanim()) {
      zmanimList.add(ZmanimFactory.get(location, this, type))
    }

    return zmanimList
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Queries about this date.

  public fun isGregorianLeapYear(): Boolean {

    // If the year isn't divisible by 4, it cannot be a leap year.
    // But, if it is divisible by 100, it is NOT a leap year.
    // But....if ti is divisible by 400, then it IS a leap year.

    if (gregorianYear % 4 != 0) {
      return false
    }

    if (gregorianYear % 400 == 0) {
      return true
    }

    if (gregorianYear % 100 == 0) {
      return false
    }

    return true
  }


  /**
   * Returns this object's date as a java.util.Date object.
   *
   * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
   *
   * @return The `Date`
   */
  public fun getTime(): Date {
    val cal = Calendar.getInstance()
    syncCalendar(cal)
    return cal.time
  }


  /**
   * Get a calendar which matches the Gregorian date represented by this Hebrew date.
   *
   * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
   */
  public fun getCalendar(): Calendar {
    val cal = Calendar.getInstance()
    syncCalendar(cal)

    return cal
  }


  public fun isToday(): Boolean {

    val today = HebrewCalendar.today()
    return equals(today)
  }


  /**
   * Check if the specified time, given the specified location, is after the time of the specified zman.
   *
   * @param currentTime The time to check against.
   * @param type The zman we're checking.
   * @param location Where in the world to check. Should not be null, but in case it is, this
   *  method assumes that we are NOT after the zman.
   *
   * @return true if this HebrewDate represents a time after the zman for the current day.
   */
  public fun isAfter(
    currentTime: Calendar,
    type: Zmanim.Type,
    location: ZmanimLocation?
  ): Boolean {

    if (location == null) {
      return false
    }

    val currentDate = HebrewCalendar.getForDate(currentTime)
    if (0 != currentDate.compareTo(this)) {
      return false
    }

    val zmanim = getZmanim(location).findType(type)
    if (zmanim == null) {
      return false
    }

    val zmanTime = zmanim.getDefault()!!.getTime()
    if (zmanTime == null) {
      // If the zman doesn't have a time then the current time cannot be after the time of the zman.
      return false
    }

    return currentTime.time.after(zmanTime.time)
  }


  /**
   * Check if the current SYSTEM time, given the specified location, is after sunset.
   *
   * @param location Where in the world to check. Should not be null, but in case it is, this method assumes that
   *  we are NOT after sunset.
   *
   * @return true if this HebrewDate represents a time after sunset for the current day.
   */
  public fun isAfterSunset(
    location: ZmanimLocation?
  ): Boolean {

    return isAfter(getCalendar(), Zmanim.Type.Shkia, location)
  }


  /**
   * Updates the specified calendar to match the Gregorian date represented by this Hebrew date.
   *
   * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
   */
  public fun syncCalendar(calendar: Calendar) {

    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(gregorianYear, gregorianMonth, gregorianDayOfMonth)
  }


  /**
   * Returns if the year the calendar is set to is a Hebrew leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year
   * cycle are leap years.
   *
   * @return true if it is a leap year
   */
  public fun isHebrewLeapYear(): Boolean {
    return HebrewCalendar.isHebrewLeapYear(hebrewYear)
  }


  /**
   * Returns the number of days for the current year that the calendar is set to.
   *
   * @see isCheshvanLong
   * @see isKislevShort
   * @see isHebrewLeapYear
   */
  public fun getDaysInHebrewYear(): Int {
    return HebrewCalendar.getElapsedDays(hebrewYear + 1) - HebrewCalendar.getElapsedDays(hebrewYear)
  }


  /**
   * @return true if Cheshvan is long (29 or 30 days) for the current year that the calendar is set to
   */
  public fun isCheshvanLong(): Boolean {
    return HebrewCalendar.isCheshvanLong(hebrewYear)
  }


  /**
   * @return true if Kislev is short (29 or 30 days) for the year that this class is set to
   */
  public fun isKislevShort(): Boolean {
    return HebrewCalendar.isKislevShort(hebrewYear)
  }


  /**
   * Returns the Cheshvan and Kislev kviah (whether a Hebrew year is short, regular or long). It will return
   * [HebrewYearType.SHALEM] if both Cheshvan and Kislev are 30 days, [HebrewYearType.QESIDRAH] if Cheshvan is 29
   * days and Kislev is 30 days and [HebrewYearType.HASER] if both are 29 days.
   *
   * @return [HebrewYearType.SHALEM] if both Cheshvan and Kislev are 30 days, [HebrewYearType.QESIDRAH] if
   *         Cheshvan is 29 days and Kislev is 30 days and [HebrewYearType.HASER] if both are 29 days.
   *
   * @see isCheshvanLong
   * @see isKislevShort
   */
  public fun getYearType(): HebrewYearType {
    return HebrewCalendar.getYearType(hebrewYear)
  }


  /**
   * @return the number of days for a given Heberw month that the calendar is currently set to.
   */
  public fun getDaysInHebrewMonth(): Int {
    return HebrewCalendar.getDaysInHebrewMonth(hebrewMonth, hebrewYear)
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Molad info

  /**
   * Returns the molad for the year and month of the specified HebrewDate. Returns a HebrewDate set to the date of
   * the molad with the molad fields properly set.
   *
   * In the current implementation, it sets the molad time based on a midnight date rollover. This means that Rosh
   * Chodesh Adar II, 5771 with a molad of 7 chalakim past midnight on Shabbos 29 Adar I / March 5, 2011 12:00 AM and 7
   * chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
   */
  public fun getMolad(): HebrewDate {

    var moladDate = HebrewCalendar.getForMolad(HebrewCalendar.getChalakimSinceMoladTohu(hebrewYear, hebrewMonth))

    if (moladDate.moladHours >= 6) {
      moladDate = moladDate.tomorrow()
    }

    moladDate.moladHours = (moladDate.moladHours + HebrewCalendar.CHALAKIM_PER_MINUTE) % HebrewCalendar.HOURS_PER_DAY
    return moladDate
  }


  /**
   * Returns the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu to the year and
   * month passed in.
   *
   * @return the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu
   */
  public fun getChalakimSinceMoladTohu(): Long {
    return HebrewCalendar.getChalakimSinceMoladTohu(hebrewYear, hebrewMonth)
  }



  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Date navigation

  public fun tomorrow(): HebrewDate {
    return HebrewCalendar.forward(this)
  }

  public fun forward(numDays: Int): HebrewDate {
    return HebrewCalendar.forward(this, numDays)
  }


  public fun yesterday(): HebrewDate {
    return HebrewCalendar.back(this)
  }

  public fun back(numDays: Int): HebrewDate {
    return HebrewCalendar.back(this, numDays)
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Base class overrides

  override fun compareTo(other: HebrewDate): Int {

    // We use gregorian because with the Hebrew months, because Nisan is the first month, it gets more complex.
    //  E.g., Nisan 1, 5772 is AFTER Adar 29, 5772, but in the HebrewMonth definitions, NISAN < ADAR...

    if (gregorianYear < other.gregorianYear) {
      return -1
    }
    else if (gregorianYear > other.gregorianYear) {
      return 1
    }

    if (gregorianMonth < other.gregorianMonth) {
      return -1
    }
    else if (gregorianMonth > other.gregorianMonth) {
      return 1
    }

    if (gregorianDayOfMonth < other.gregorianDayOfMonth) {
      return -1
    }
    else if (gregorianDayOfMonth > other.gregorianDayOfMonth) {
      return 1
    }

    return 0
  }


  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is HebrewDate) {
      return false
    }

    if (hebrewDayOfMonth != other.hebrewDayOfMonth) {
      return false
    }
    if (hebrewMonth != other.hebrewMonth) {
      return false
    }
    if (hebrewYear != other.hebrewYear) {
      return false
    }

    return true
  }


  override fun hashCode(): Int {
    var result = hebrewMonth
    result = 31 * result + hebrewYear
    result = 31 * result + hebrewDayOfMonth
    return result
  }


  override fun toString(): String {
    return "HebrewDate{" +
      "hebrewYear=" + hebrewYear +
      ", hebrewMonth=" + hebrewMonth +
      ", hebrewDay=" + hebrewDayOfMonth +
      ", gregorianYear=" + gregorianYear +
      ", gregorianMonth=" + gregorianMonth +
      ", gregorianDayOfMonth=" + gregorianDayOfMonth +
      ", dateDetails=" + dateDetails +
      '}'
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //

  private class NullDetails(date: HebrewDate) : Details(date) {

    override fun isErev(): Boolean {
      return false
    }

    override fun lightCandles(): Boolean {
      return false
    }

    override fun sayHavdalah(): Boolean {
      return false
    }

    override fun getDescription(): String {
      return ""
    }


    override fun isAnything(): Boolean {
      return false
    }
  }

}  // End of HebrewDate class
