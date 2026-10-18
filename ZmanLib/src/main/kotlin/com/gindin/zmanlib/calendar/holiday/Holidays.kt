/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday

import com.gindin.zmanlib.calendar.holiday.minor.LagBOmer
import com.gindin.zmanlib.calendar.holiday.minor.PesachSheni
import com.gindin.zmanlib.calendar.holiday.minor.TuBAv
import com.gindin.zmanlib.calendar.holiday.minor.TuBShvat
import com.gindin.zmanlib.calendar.holiday.modern.YomHaAtzmaut
import com.gindin.zmanlib.calendar.holiday.modern.YomHaShoah
import com.gindin.zmanlib.calendar.holiday.modern.YomHaZikaron
import com.gindin.zmanlib.calendar.holiday.modern.YomYerushalayim
import com.gindin.zmanlib.calendar.holiday.other.Nissan
import com.gindin.zmanlib.calendar.holiday.other.Omer
import com.gindin.zmanlib.calendar.holiday.other.Sivan
import com.gindin.zmanlib.calendar.holiday.other.Tishri
import com.gindin.zmanlib.calendar.holiday.rabbanan.Chanukah
import com.gindin.zmanlib.calendar.holiday.rabbanan.Purim
import com.gindin.zmanlib.calendar.holiday.rabbanan.PurimKatan
import com.gindin.zmanlib.calendar.holiday.rabbanan.ShushanPurim
import com.gindin.zmanlib.calendar.holiday.taaniyot.AsaraBTevet
import com.gindin.zmanlib.calendar.holiday.taaniyot.Taanit
import com.gindin.zmanlib.calendar.holiday.taaniyot.Taanit17Tamuz
import com.gindin.zmanlib.calendar.holiday.taaniyot.TaanitBechorot
import com.gindin.zmanlib.calendar.holiday.taaniyot.TaanitEsther
import com.gindin.zmanlib.calendar.holiday.taaniyot.TishaBAv
import com.gindin.zmanlib.calendar.holiday.taaniyot.TzomGedaliah
import com.gindin.zmanlib.calendar.holiday.yomtov.Pesach
import com.gindin.zmanlib.calendar.holiday.yomtov.RoshHashana
import com.gindin.zmanlib.calendar.holiday.yomtov.Shavuot
import com.gindin.zmanlib.calendar.holiday.yomtov.Sukkot
import com.gindin.zmanlib.calendar.holiday.yomtov.YomKippur
import java.util.Arrays

/**
 * Serves as the central location for declaring all the Jewish Holidays.
 */
@Suppress("OverlyCoupledClass")
public object Holidays {

  @JvmField
  public val SHABBAT: Shabbat = Shabbat()

  // Yom Tov
  private val PESACH = Pesach()
  private val ROSH_HASHANA = RoshHashana()
  private val SHAVUOT = Shavuot()
  private val SUKKOT = Sukkot()
  private val YOM_KIPPUR = YomKippur()

  // Rabanic Holidays
  private val CHANUKAH = Chanukah()
  private val PURIM = Purim()
  private val SHUSHAN_PURIM = ShushanPurim()

  // Minor
  private val LAG_B_OMER = LagBOmer()
  private val PESACH_SHENI = PesachSheni()
  private val TU_B_AV = TuBAv()
  private val TU_B_SHVAT = TuBShvat()
  private val PURIM_KATAN = PurimKatan()

  // Fast Days
  private val ASARA_B_TEVET = AsaraBTevet()
  private val TAANIT_17_TAMUZ = Taanit17Tamuz()
  private val TAANIT_BECHOROT = TaanitBechorot()
  private val TAANIT_ESTHER = TaanitEsther()
  private val TISHA_B_AV = TishaBAv()
  private val TZOM_GEDALIAH = TzomGedaliah()

  // Modern holidays
  private val YOM_HA_ATZMAUT = YomHaAtzmaut()
  private val YOM_HA_SHOAH = YomHaShoah()
  private val YOM_HA_ZIKARON = YomHaZikaron()
  private val YOM_YERUSHALAYIM = YomYerushalayim()

  /** Handles all of the Rosh Chodeshim. */
  @JvmField
  public val ROSH_CHODESH: RoshChodesh = RoshChodesh()

  /** Handles all of the Omer. */
  private val OMER = Omer()

  /** Handles that we don't say tachanun the first 12 days of Sivan. */
  private val SIVAN = Sivan()

  /** Handles that we don't say tachanun for all of Nissan. */
  private val NISSAN = Nissan()

  /** Handles that we don't say tachanun from Erev Kippur until the end of the month. */
  private val TISHRI = Tishri()


  /**
   * Sets of the holidays.
   */
  @JvmField
  public val MAJOR_HOLIDAYS: List<Observance> = Arrays.asList(
    ROSH_HASHANA,       YOM_KIPPUR,         SUKKOT,
    CHANUKAH,           PURIM,              PESACH,
    SHAVUOT
  )


  @JvmField
  public val MINOR_HOLIDAYS: List<Observance> = Arrays.asList(
    TU_B_SHVAT,         PURIM_KATAN,        SHUSHAN_PURIM,
    PESACH_SHENI,       LAG_B_OMER,         TU_B_AV
  )


  @JvmField
  public val FAST_DAYS: List<Taanit> = Arrays.asList(
    TZOM_GEDALIAH,      ASARA_B_TEVET,      TAANIT_ESTHER,
    TAANIT_BECHOROT,    TAANIT_17_TAMUZ,    TISHA_B_AV
  )


  @JvmField
  public val MODERN_HOLIDAYS: List<Observance> = Arrays.asList(
    YOM_HA_SHOAH,       YOM_HA_ZIKARON,     YOM_HA_ATZMAUT,
    YOM_YERUSHALAYIM
  )


  @JvmField
  public val OTHER: List<Observance> = Arrays.asList(
    OMER,               SIVAN,              NISSAN,
    TISHRI
  )

}  // End of Holidays class
