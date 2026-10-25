/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar

import org.intellij.lang.annotations.MagicConstant

/**
 * Enumeration of the Hebrew months, starting with Nissan.
 *
 * TODO: Make this an enum.
 */
public interface HebrewMonth {

  @MagicConstant(
    intValues =
      [
        HebrewMonth.NISSAN.toLong(),
        HebrewMonth.IYAR.toLong(),
        HebrewMonth.SIVAN.toLong(),
        HebrewMonth.TAMMUZ.toLong(),
        HebrewMonth.AV.toLong(),
        HebrewMonth.ELUL.toLong(),
        HebrewMonth.TISHREI.toLong(),
        HebrewMonth.CHESHVAN.toLong(),
        HebrewMonth.KISLEV.toLong(),
        HebrewMonth.TEVET.toLong(),
        HebrewMonth.SHEVAT.toLong(),
        HebrewMonth.ADAR.toLong(),
        HebrewMonth.ADAR_II.toLong(),
      ]
  )
  @Retention(AnnotationRetention.BINARY)
  public annotation class Constant

  public companion object {

    /**
     * Value of the month field indicating Nissan, the first numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 7th (or 8th in a
     * [leap year][HebrewDate.isHebrewLeapYear]) month of the year.
     */
    public const val NISSAN: Int = 1

    /**
     * Value of the month field indicating Iyar, the second numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 8th (or 9th in a
     * [leap year][HebrewDate.isHebrewLeapYear]) month of the year.
     */
    public const val IYAR: Int = 2

    /**
     * Value of the month field indicating Sivan, the third numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 9th (or 10th in a
     * [leap year][HebrewDate.isHebrewLeapYear]) month of the year.
     */
    public const val SIVAN: Int = 3

    /**
     * Value of the month field indicating Tammuz, the fourth numeric month of the year in the
     * Jewish calendar. With the year starting at [TISHREI], it would actually be the 10th (or 11th
     * in a [leap year][HebrewDate.isHebrewLeapYear]) month of the year.
     */
    public const val TAMMUZ: Int = 4

    /**
     * Value of the month field indicating Av, the fifth numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 11th (or 12th in a
     * [leap year][HebrewDate.isHebrewLeapYear]) month of the year.
     */
    public const val AV: Int = 5

    /**
     * Value of the month field indicating Elul, the sixth numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 12th (or 13th in a
     * [leap year][HebrewDate.isHebrewLeapYear]) month of the year.
     */
    public const val ELUL: Int = 6

    /**
     * Value of the month field indicating Tishrei, the seventh numeric month of the year in the
     * Jewish calendar. With the year starting at this month, it would actually be the 1st month of
     * the year.
     */
    public const val TISHREI: Int = 7

    /**
     * Value of the month field indicating Cheshvan/Marcheshvan, the eighth numeric month of the
     * year in the Jewish calendar. With the year starting at [TISHREI], it would actually be the
     * 2nd month of the year.
     */
    public const val CHESHVAN: Int = 8

    /**
     * Value of the month field indicating Kislev, the ninth numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 3rd month of the
     * year.
     */
    public const val KISLEV: Int = 9

    /**
     * Value of the month field indicating Teves, the tenth numeric month of the year in the Jewish
     * calendar. With the year starting at [TISHREI], it would actually be the 4th month of the
     * year.
     */
    public const val TEVET: Int = 10

    /**
     * Value of the month field indicating Shevat, the eleventh numeric month of the year in the
     * Jewish calendar. With the year starting at [TISHREI], it would actually be the 5th month of
     * the year.
     */
    public const val SHEVAT: Int = 11

    /**
     * Value of the month field indicating Adar (or Adar I in a
     * [leap year][HebrewDate.isHebrewLeapYear]), the twelfth numeric month of the year in the
     * Jewish calendar. With the year starting at [TISHREI], it would actually be the 6th month of
     * the year.
     */
    public const val ADAR: Int = 12

    /**
     * Value of the month field indicating Adar II, the leap (intercalary or embolismic) thirteenth
     * (Undecimber) numeric month of the year added in Jewish
     * [leap year][HebrewDate.isHebrewLeapYear]). The leap years are years 3, 6, 8, 11, 14, 17 and
     * 19 of a 19 year cycle. With the year starting at [TISHREI], it would actually be the 7th
     * month of the year.
     */
    public const val ADAR_II: Int = 13

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Month lengths

    public const val SHORT_MONTH_LENGTH: Int = 29
    public const val LONG_MONTH_LENGTH: Int = 30
  }
} // End of HebrewMonth interface
