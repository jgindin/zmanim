/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import com.gindin.zmanlib.calendar.HebrewDate
import com.kosherjava.zmanim.ComprehensiveZmanimCalendar
import com.kosherjava.zmanim.util.GeoLocation
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

/**
 * How many minutes after sunset can you still start mincha?
 *
 * <p/>
 * Daily Halacha (http://www.dailyhalacha.com/displayRead.asp?readID=609&txtSearch=mincha):
 *
 * "At first glance, it would appear that the final time for reciting Minha is “Sheki’a,” or
 * sundown, and this is indeed the view of the Mishna Berura...
 *
 * <p/>
 * "Others, however, disagree, and claim that one may, if necessary, pray Minha even after sunset,
 * during the period of Ben Ha’shemashot (“twilight”). This was the position of Hacham Ben Sion Abba
 * Shaul (Israel, 1923-1998), and he calculated Ben Ha’shemashot as extending for 26 minutes after
 * sundown. Hacham Ovadia Yosef similarly allows reciting Minha during the period of Ben
 * Ha’shemashot, but he does not accept Hacham Ben Sion’s calculation. In his view, Ben Ha’shemashot
 * extends for only 13.5 minutes after sundown...
 *
 * <p/>
 * "May a person recite Minha if he knows that he will be unable to complete the Amida prayer before
 * the final time? For example, if a person realizes at exactly 13 minutes after sunset that he has
 * not yet prayed Minha, may be begin praying at that point, realizing that most of his Amida prayer
 * will be recited after the final time for Minha, which occurs just 30 seconds later?
 *
 * <p/>
 * "The Ben Ish Hai (Rav Yosef Haim of Baghdad, 1833-1909), in one place in his writings, rules that
 * one should not begin the Amida if he will be unable to complete the prayer before the final time.
 * Elsewhere, he writes that the majority of the Amida must be recited before the final time, and
 * therefore one may begin praying the Amida if he will be able to recite most of the prayer before
 * 13.5 minutes after sunset. Hacham Ovadia Yosef, however, rules that it suffices to begin the
 * Amida prayer before the final time. As long as 13.5 minutes have yet to pass since sunset, one
 * may begin the Amida, even if the majority of the Amida will be recited after this point."
 *
 * <p/>
 */
private const val MINUTES_AFTER_SUNSET_FOR_MINCHA_OVADIA_YOSEF = 13

private const val MINUTES_AFTER_SUNSET_FOR_MINCHA_BEN_SION_ABBA_SHAUL = 26

/** In Seattle, the Sephardic community considers Shabbat to be out 45 minutes after sunset. */
private const val MINUTES_AFTER_SUNSET_FOR_SEATTLE_SEPHARDIC_TZAIT = 45

private const val MINUTES_AFTER_SUNSET_RAV_MOSHE = 50

/** How far below the horizon the sun is at alot, for the opinion of 16.01 degrees. */
private const val ALOS_DEGREES_BELOW_HORIZON = 16.01

/** How far below the horizon the sun is at tzais, for the Geonim's opinion of 3.8 degrees. */
private const val TZAIS_GEONIM_DEGREES_BELOW_HORIZON = 3.8

/** How far below the horizon the sun is at tzais, for the Geonim's opinion of 4.37 degrees. */
private const val TZAIS_GEONIM_4_POINT_37_DEGREES = 4.37

/** How far below the horizon the sun is at tzais, for the Geonim's opinion of 4.61 degrees. */
private const val TZAIS_GEONIM_4_POINT_61_DEGREES = 4.61

/** Minutes in an hour. */
private const val MINUTES_PER_HOUR = 60

/** Hours from the fixed local chatzot of the day to the middle of the night. */
private const val HOURS_FROM_CHATZOT_TO_NIGHT_CHATZOT = 12L

/** Used to calculate all of our zmanim. */
public class ZmanimCalculator(
  location: GeoLocation,
  date: HebrewDate,
) : ComprehensiveZmanimCalendar(location) {

  init {
    // java.util.Calendar months start at 0, LocalDate's at 1.
    setLocalDate(
      LocalDate.of(date.gregorianYear, date.gregorianMonth + 1, date.gregorianDayOfMonth)
    )
  }

  /**
   * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning)
   * based on the opinion that the day starts at <em>alos 16.01&deg;</em> and ends at <em>tzait
   * 3.8&deg;</em> after sunset. 3 shaos zmaniyos are calculated based on this day and added to alos
   * to reach this time. <b>Note: </b> Based on this calculation <em>chatzos</em> will not be at
   * midday.
   *
   * @return the time of the latest <em>zman krias shema</em> based on this day. If the calculation
   *   can't be computed such as northern and southern locations even south of the Arctic Circle and
   *   north of the Antarctic Circle where the sun may not reach low enough below the horizon for
   *   this calculation, a null will be returned. See detailed explanation on top of the
   *   [com.kosherjava.zmanim.AstronomicalCalendar] documentation.
   * @see getAlos16Point1Degrees
   */
  public fun getSofZmanShmaAlos16Point01ToTzaisGeonim3Point8Degrees(): Instant? {
    return getSofZmanShma(
      getSunriseOffsetByDegrees(GEOMETRIC_ZENITH + ALOS_DEGREES_BELOW_HORIZON),
      getSunsetOffsetByDegrees(GEOMETRIC_ZENITH + TZAIS_GEONIM_DEGREES_BELOW_HORIZON),
    )
  }

  /**
   * Method to return the latest time for mincha based on Daily Halacha's representation of the
   * Mishna Berura's ruling, that mincha must be FINISHED by sunset.
   *
   * @return the time representing the time.
   */
  public fun getLatestMinchaMishnaBerura(): Instant? {
    return getSeaLevelSunset()
  }

  /**
   * Method to return the latest time for mincha based on Daily Halacha's representation of Chacham
   * Ovadia Yosef's ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5
   * shaot zmaniot minutes after sunset.
   *
   * @return the time representing the time.
   */
  public fun getLatestMinchaOvadiaYosef(): Instant? {
    // First, we have to figure out how long a shaot zmaniot minute is. The returned temporal hour
    //  is represented by the number of milliseconds in that "hour".
    val shaotZmaniotHour: Long = getTemporalHour()?.toMillis() ?: return null
    val shaotZmaniotMinute: Long = shaotZmaniotHour / MINUTES_PER_HOUR

    return getTimeOffset(
      getSeaLevelSunset(),
      Duration.ofMillis(
        (MINUTES_AFTER_SUNSET_FOR_MINCHA_OVADIA_YOSEF * shaotZmaniotMinute) +
          (shaotZmaniotMinute / 2)
      ),
    )
  }

  /**
   * Method to return the latest time for mincha based on Daily Halacha's representation of Chacham
   * Ben Sion Abba Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26
   * minutes after sunset.
   *
   * @return the time representing the time.
   */
  public fun getLatestMinchaBenSion(): Instant? {
    return getTimeOffset(
      getSeaLevelSunset(),
      Duration.ofMinutes(MINUTES_AFTER_SUNSET_FOR_MINCHA_BEN_SION_ABBA_SHAUL.toLong()),
    )
  }

  /**
   * KosherJava 3 replaced this zman with 4.42 degrees, so it is calculated here to keep its time.
   *
   * @return the time, or null where the sun does not reach that far below the horizon.
   */
  public fun getTzaitGeonim4Point37Degrees(): Instant? {
    return getSunsetOffsetByDegrees(GEOMETRIC_ZENITH + TZAIS_GEONIM_4_POINT_37_DEGREES)
  }

  /**
   * KosherJava 3 replaced this zman with 4.66 degrees, so it is calculated here to keep its time.
   *
   * @return the time, or null where the sun does not reach that far below the horizon.
   */
  public fun getTzaitGeonim4Point61Degrees(): Instant? {
    return getSunsetOffsetByDegrees(GEOMETRIC_ZENITH + TZAIS_GEONIM_4_POINT_61_DEGREES)
  }

  public fun getTzaitSeattleSephardic(): Instant? {
    return getTimeOffset(
      getSeaLevelSunset(),
      Duration.ofMinutes(MINUTES_AFTER_SUNSET_FOR_SEATTLE_SEPHARDIC_TZAIT.toLong()),
    )
  }

  /**
   * Based on Rav Moshe's fixed 50 minutes after sunset. This time is ONLY intended to be used in
   * New York.
   *
   * TODO: We should simply allow users to create their own times rather than coding all of these...
   */
  public fun getTzaitRavMoshe(): Instant? {
    return getTimeOffset(
      getSeaLevelSunset(),
      Duration.ofMinutes(MINUTES_AFTER_SUNSET_RAV_MOSHE.toLong()),
    )
  }

  /**
   * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts
   * at <em>[alos 16.1&deg;][getAlos16Point1Degrees]</em> and ends at <em>[tzais
   * 8.5&deg;][getTzaisGeonim8Point5Degrees]</em>.
   *
   * @return the time of the time of <em>plag hamincha</em>. If the calculation can't be computed
   *   such as northern and southern locations even south of the Arctic Circle and north of the
   *   Antarctic Circle where the sun may not reach low enough below the horizon for this
   *   calculation, a null will be returned. See detailed explanation on top of the
   *   [com.kosherjava.zmanim.AstronomicalCalendar] documentation.
   */
  public fun getPlagAlot16Point1ToTzait8Point5(): Instant? {
    return getPlagHamincha(getAlos16Point1Degrees(), getTzaisGeonim8Point5Degrees())
  }

  public fun getEndOfFast(minutesAfterShkia: Int): Instant? {
    return getTimeOffset(getSeaLevelSunset(), Duration.ofMinutes(minutesAfterShkia.toLong()))
  }

  /**
   * Calculates candle lighting times. Used rather than setting the offset and asking for the candle
   * lighting in the base class because that won't be thread safe.
   *
   * @param minutesBeforeShkia How many minutes prior to Shkia should candle lighting be calculated?
   * @return the time representing candle lighting.
   */
  public fun getCandleLighting(minutesBeforeShkia: Int): Instant? {
    return getTimeOffset(getSeaLevelSunset(), Duration.ofMinutes(-minutesBeforeShkia.toLong()))
  }

  /**
   * Calculates the middle of the night as a 12 hours after the fixed local chatzot.
   *
   * @return the time representing the local middle of the night.
   * @see getFixedLocalChatzosHayom
   */
  public fun getFixedLocalNightChatzos(): Instant? {
    return getFixedLocalChatzosHayom()?.plus(Duration.ofHours(HOURS_FROM_CHATZOT_TO_NIGHT_CHATZOT))
  }

  // Part of the library's public API, which is why this placeholder stays even though it cannot be
  // implemented yet.
  @Suppress("FunctionOnlyReturningConstant")
  public fun getNightChatzos6HoursAfterTzait(): Instant? {
    // todo: it seems the only way to calculate this is to know how the user has defined Tzeit!
    return null
  }
}
