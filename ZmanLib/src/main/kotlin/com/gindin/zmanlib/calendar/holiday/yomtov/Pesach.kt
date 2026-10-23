/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance
import com.gindin.zmanlib.zman.Zmanim
import java.util.ArrayList

private const val PESACH = "Pesach"

/**  */
@Suppress("MagicNumber")
public class Pesach : Observance() {

  override fun getName(): String {
    return PESACH
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth

    if (HebrewMonth.NISSAN != hebrewMonth) {
      return null
    }

    val hebrewDayOfMonth = date.hebrewDayOfMonth
    return when (hebrewDayOfMonth) {
      14 -> ErevPesachDetails(date)
      15 -> PesachYomTovDetails(date, true)
      16 -> PesachYomTovDetails(date, false)
      17,
      18,
      19,
      20 -> PesachCholHaMoedDetails(date, hebrewDayOfMonth - 14)
      21 -> PesachYomTovDetails(date, true)
      22 -> PesachYomTovDetails(date, false)
      else -> null
    }
  }

  private open class ErevPesachDetails internal constructor(date: HebrewDate) : Details.ErevYomTov(date, PESACH) {

    override fun isPesach(): Boolean {
      return true
    }

    override fun getZmanim(): List<Zmanim.Type> {

      val zmanim = ArrayList<Zmanim.Type>()
      zmanim.add(Zmanim.Type.Alot)
      zmanim.add(Zmanim.Type.Talit)
      zmanim.add(Zmanim.Type.Hanetz)
      zmanim.add(Zmanim.Type.SofZmanShema)
      zmanim.add(Zmanim.Type.SofZmanTefila)
      zmanim.add(Zmanim.Type.SofZmanAchilatChametz)

      // NOTE: Even when erev Pesach falls out on Shabbat, there is Biur. True, it's not the same
      //  action (just the declaration), but still...
      zmanim.add(Zmanim.Type.SofZmanBiurChametz)

      zmanim.add(Zmanim.Type.Chatzot)
      zmanim.add(Zmanim.Type.MinchaGedola)
      zmanim.add(Zmanim.Type.MinchaKetana)
      zmanim.add(Zmanim.Type.PlagHaMincha)

      if (lightCandles()) {
        zmanim.add(Zmanim.Type.CandleLighting)
      }

      zmanim.add(Zmanim.Type.Shkia)
      zmanim.add(Zmanim.Type.LatestMincha)
      zmanim.add(Zmanim.Type.BeinHashmashot)
      zmanim.add(Zmanim.Type.FastEnd)
      zmanim.add(Zmanim.Type.TzaitHacochavim)
      zmanim.add(Zmanim.Type.NightChatzot)

      return zmanim
    }
  }

  private open class PesachYomTovDetails
  internal constructor(
    date: HebrewDate,
    lightCandles: Boolean,
  ) : Details.YomTovDetails(date, PESACH, lightCandles) {

    override fun isPesach(): Boolean {
      return true
    }
  }

  private open class PesachCholHaMoedDetails
  internal constructor(
    date: HebrewDate,
    dayOfChag: Int,
  ) : Details.CholHaMoedDetails(date, dayOfChag, PESACH, 6 == dayOfChag) {

    override fun isPesach(): Boolean {
      return true
    }
  }
} // End of Pesach class
