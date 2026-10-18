/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val SUKKOT = "Sukkot"
private const val HOSHANA_RABBA = "Hoshana Rabba"
private const val SHMINI_ATZERET = "Shmini Atzeret"
private const val SIMCHAT_TORAH = "Simchat Torah"

/**
 *
 */
@Suppress("MagicNumber")
public class Sukkot : Observance() {

  override fun getName(): String {
    return SUKKOT
  }


  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.TISHREI == date.hebrewMonth) {
      val hebrewDayOfMonth = date.hebrewDayOfMonth

      when (hebrewDayOfMonth) {
        14 -> return Details.ErevYomTov(date, SUKKOT)

        15 -> return SukkotYomTovDetails(date, true)

        16 -> return SukkotYomTovDetails(date, false)

        17, 18, 19, 20 -> return SukkotCholHaModeDetails(date, hebrewDayOfMonth - 14)

        21 -> return HoshanaRabbaDetails(date)

        22 -> return ShminiAtzeretDetails(date)

        23 -> return SimchatTorahDetails(date)
      }
    }

    return null
  }


  private class SukkotYomTovDetails(
    date: HebrewDate,
    lightCandles: Boolean
  ) : Details.YomTovDetails(date, SUKKOT, lightCandles) {

    override fun isSukkot(): Boolean {
      return true
    }
  }


  private class SukkotCholHaModeDetails(
    date: HebrewDate,
    dayOfChag: Int
  ) : Details.CholHaMoedDetails(date, dayOfChag, SUKKOT, false) {

    override fun isSukkot(): Boolean {
      return true
    }
  }


  private class HoshanaRabbaDetails(date: HebrewDate) : Details.ErevYomTov(date, HOSHANA_RABBA) {

    override fun isSukkot(): Boolean {
      return true
    }


    override fun getDescription(): String {
      return "$SUKKOT 7 ($HOSHANA_RABBA)"
    }
  }


  private class ShminiAtzeretDetails(date: HebrewDate) : Details.YomTovDetails(date, SHMINI_ATZERET, true) {

    override fun isShminiAtzeret(): Boolean {
      return true
    }
  }


  private class SimchatTorahDetails(date: HebrewDate) : Details.YomTovDetails(date, SIMCHAT_TORAH, false) {

    override fun isSimchatTorah(): Boolean {
      return true
    }
  }

}  // End of Sukkot class
