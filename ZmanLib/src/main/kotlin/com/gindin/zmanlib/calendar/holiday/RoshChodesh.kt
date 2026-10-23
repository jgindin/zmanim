/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.HebrewYearType

private const val ROSH_CHODESH = "Rosh Chodesh"

/**  */
public class RoshChodesh : Observance() {

  override fun getName(): String {
    return ROSH_CHODESH
  }

  @Suppress("MagicNumber", "FeatureEnvy")
  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth
    val hebrewDayOfMonth = date.hebrewDayOfMonth

    when (hebrewMonth) {
      HebrewMonth.NISSAN -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Nissan")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Iyar")
        }
        if (30 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Iyar (1)")
        }
      }

      HebrewMonth.IYAR -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Iyar (2)")
        } else if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Sivan")
        }
      }

      HebrewMonth.SIVAN -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Sivan")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Tammuz")
        }
        if (30 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Tammuz (1)")
        }
      }

      HebrewMonth.TAMMUZ -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Tammuz (2)")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Av")
        }
      }

      HebrewMonth.AV -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Av")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Elul")
        }
        if (30 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Elul (1)")
        }
      }

      HebrewMonth.ELUL -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Elul (2)")
        }
      }

      HebrewMonth.TISHREI -> {
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Heshvan")
        }
        if (30 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Heshvan (1)")
        }
      }

      HebrewMonth.CHESHVAN -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Heshvan (2)")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Kislev")
        }
        if (30 == hebrewDayOfMonth) {
          // Will only happen in a Shalem year
          assert(date.isCheshvanLong())
          assert(date.getYearType() == HebrewYearType.SHALEM)
          return RoshChodeshDetails(date, "Kislev (1)")
        }
      }

      HebrewMonth.KISLEV -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(
            date,
            "Kislev" + (if (date.getYearType() == HebrewYearType.SHALEM) " (2)" else ""),
          )
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Tevet")
        }
        if (30 == hebrewDayOfMonth) {
          assert(!date.isKislevShort())
          val yearType = date.getYearType()
          assert(HebrewYearType.SHALEM == yearType || HebrewYearType.QESIDRAH == yearType)
          return RoshChodeshDetails(date, "Tevet (1)")
        }
      }

      HebrewMonth.TEVET -> {
        if (1 == hebrewDayOfMonth) {
          val yearType = date.getYearType()
          return RoshChodeshDetails(
            date,
            "Tevet" + (if (HebrewYearType.SHALEM == yearType || HebrewYearType.QESIDRAH == yearType) " (2)" else ""),
          )
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Shevat")
        }
      }

      HebrewMonth.SHEVAT -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Shevat")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar I" else "Adar")
        }
        if (30 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar I (1)" else "Adar")
        }
      }

      HebrewMonth.ADAR -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar I (2)" else "Adar")
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, if (date.isHebrewLeapYear()) "Adar II" else "Nissan")
        }
        if (30 == hebrewDayOfMonth) {
          // Can only happen if this is a leap year
          assert(date.isHebrewLeapYear())
          return RoshChodeshDetails(date, "Adar II (1)")
        }
      }

      HebrewMonth.ADAR_II -> {
        if (1 == hebrewDayOfMonth) {
          return RoshChodeshDetails(date, "Adar II" + (if (date.isHebrewLeapYear()) " (2)" else ""))
        }
        if (29 == hebrewDayOfMonth) {
          return ErevRoshChodeshDetails(date, "Nissan")
        }
      }
    }

    return null
  }

  private class ErevRoshChodeshDetails(date: HebrewDate, month: String) :
    Details.Erev(date, "$ROSH_CHODESH $month", false)

  private class RoshChodeshDetails(date: HebrewDate, month: String) : Details.Yom(date, "$ROSH_CHODESH $month") {

    override fun isRoshChodesh(): Boolean {
      return true
    }

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of RoshChodesh class
