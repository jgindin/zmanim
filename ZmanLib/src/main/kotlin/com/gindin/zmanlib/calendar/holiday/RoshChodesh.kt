/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.HebrewYearType

private const val ROSH_CHODESH = "Rosh Chodesh"

/**
 * The 29th is the day before the next month's Rosh Chodesh begins, in a short month and a long one
 * alike.
 */
private const val EREV_DAY = HebrewMonth.SHORT_MONTH_LENGTH

/** The 30th of a long month is already the first day of the next month's Rosh Chodesh. */
private const val LAST_DAY = HebrewMonth.LONG_MONTH_LENGTH

/**  */
public class RoshChodesh : Observance() {

  override fun getName(): String {
    return ROSH_CHODESH
  }

  override fun getDetailsFor(date: HebrewDate): Details? {
    return detailsOfMonth[date.hebrewMonth]?.invoke(date, date.hebrewDayOfMonth)
  }

  // Each month knows which of its days begin or precede the Rosh Chodesh of this or the next month.
  //  A 30-day month's 30th day is the first day of the next month's Rosh Chodesh, and the 1st of
  //  the month is its second day.
  private val detailsOfMonth: Map<Int, (HebrewDate, Int) -> Details?> =
    mapOf(
      HebrewMonth.NISSAN to ::getNissanDetails,
      HebrewMonth.IYAR to ::getIyarDetails,
      HebrewMonth.SIVAN to ::getSivanDetails,
      HebrewMonth.TAMMUZ to ::getTammuzDetails,
      HebrewMonth.AV to ::getAvDetails,
      HebrewMonth.ELUL to ::getElulDetails,
      HebrewMonth.TISHREI to ::getTishreiDetails,
      HebrewMonth.CHESHVAN to ::getCheshvanDetails,
      HebrewMonth.KISLEV to ::getKislevDetails,
      HebrewMonth.TEVET to ::getTevetDetails,
      HebrewMonth.SHEVAT to ::getShevatDetails,
      HebrewMonth.ADAR to ::getAdarDetails,
      HebrewMonth.ADAR_II to ::getAdarIIDetails,
    )

  private fun getNissanDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Nissan")
      EREV_DAY -> ErevRoshChodeshDetails(date, "Iyar")
      LAST_DAY -> RoshChodeshDetails(date, "Iyar (1)")
      else -> null
    }
  }

  private fun getIyarDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Iyar (2)")
      EREV_DAY -> ErevRoshChodeshDetails(date, "Sivan")
      else -> null
    }
  }

  private fun getSivanDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Sivan")
      EREV_DAY -> ErevRoshChodeshDetails(date, "Tammuz")
      LAST_DAY -> RoshChodeshDetails(date, "Tammuz (1)")
      else -> null
    }
  }

  private fun getTammuzDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Tammuz (2)")
      EREV_DAY -> ErevRoshChodeshDetails(date, "Av")
      else -> null
    }
  }

  private fun getAvDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Av")
      EREV_DAY -> ErevRoshChodeshDetails(date, "Elul")
      LAST_DAY -> RoshChodeshDetails(date, "Elul (1)")
      else -> null
    }
  }

  private fun getElulDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Elul (2)")
      else -> null
    }
  }

  private fun getTishreiDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      EREV_DAY -> ErevRoshChodeshDetails(date, "Heshvan")
      LAST_DAY -> RoshChodeshDetails(date, "Heshvan (1)")
      else -> null
    }
  }

  private fun getCheshvanDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Heshvan (2)")
      EREV_DAY -> ErevRoshChodeshDetails(date, "Kislev")
      LAST_DAY -> {
        // Will only happen in a Shalem year
        assert(date.isCheshvanLong())
        assert(date.getYearType() == HebrewYearType.SHALEM)
        RoshChodeshDetails(date, "Kislev (1)")
      }
      else -> null
    }
  }

  private fun getKislevDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 ->
        RoshChodeshDetails(
          date,
          "Kislev" + (if (date.getYearType() == HebrewYearType.SHALEM) " (2)" else ""),
        )
      EREV_DAY -> ErevRoshChodeshDetails(date, "Tevet")
      LAST_DAY -> {
        assert(!date.isKislevShort())
        val yearType = date.getYearType()
        assert(HebrewYearType.SHALEM == yearType || HebrewYearType.QESIDRAH == yearType)
        RoshChodeshDetails(date, "Tevet (1)")
      }
      else -> null
    }
  }

  private fun getTevetDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> {
        val yearType = date.getYearType()
        RoshChodeshDetails(
          date,
          "Tevet" +
            (if (HebrewYearType.SHALEM == yearType || HebrewYearType.QESIDRAH == yearType) " (2)"
            else ""),
        )
      }
      EREV_DAY -> ErevRoshChodeshDetails(date, "Shevat")
      else -> null
    }
  }

  private fun getShevatDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Shevat")
      EREV_DAY -> ErevRoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar I" else "Adar")
      LAST_DAY -> RoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar I (1)" else "Adar")
      else -> null
    }
  }

  private fun getAdarDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar I (2)" else "Adar")
      EREV_DAY -> ErevRoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar II" else "Nissan")
      LAST_DAY -> {
        // Can only happen if this is a leap year
        assert(date.isHebrewLeapYear())
        RoshChodeshDetails(date, "Adar II (1)")
      }
      else -> null
    }
  }

  private fun getAdarIIDetails(date: HebrewDate, dayOfMonth: Int): Details? {
    return when (dayOfMonth) {
      1 -> RoshChodeshDetails(date, "Adar II" + (if (date.isHebrewLeapYear()) " (2)" else ""))
      EREV_DAY -> ErevRoshChodeshDetails(date, "Nissan")
      else -> null
    }
  }

  private class ErevRoshChodeshDetails(date: HebrewDate, month: String) :
    Details.Erev(date, "$ROSH_CHODESH $month", false)

  private class RoshChodeshDetails(date: HebrewDate, month: String) :
    Details.Yom(date, "$ROSH_CHODESH $month") {

    override fun isRoshChodesh(): Boolean {
      return true
    }

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of RoshChodesh class
