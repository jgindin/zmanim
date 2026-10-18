/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.text.DateFormat
import java.util.Calendar

/**
 * Format zmanim.
 */
public class ZmanFormatter(
  private val options: Options
) {

  public fun interface Options {

    public fun showSeconds(): Boolean
  }


  public fun formatZmanTime(
    zman: Zman
  ): String {
    return formatZmanTime(zman.type, zman.getTime())
  }


  public fun formatZmanTime(
    zmanType: Zmanim.Type,
    zmanTime: Calendar?
  ): String {

    val timeFormatter: DateFormat
    val zmanRounder: ZmanRounder?

    if (options.showSeconds()) {
      timeFormatter = DateFormat.getTimeInstance(DateFormat.MEDIUM)
      zmanRounder = null
    }
    else {
      timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)
      zmanRounder = ZmanRounder()
    }


    // This can return null in case the zman just doesn't apply. E.g., there are places where the Sun never
    //  actually gets low enough to be able to calculate Alot @ 26degrees.
    var time: Calendar = zmanTime ?: return "N/A"

    if (null != zmanRounder) {
      // Some of the zman types we need to do some rounding so that we avoid a situation of showing
      //  a time that can mislead someone into missing their mitzvah...
      when (zmanType) {

        Zmanim.Type.Alot,
        Zmanim.Type.Talit,
        Zmanim.Type.Hanetz,
        Zmanim.Type.FastEnd,
        Zmanim.Type.TzaitHacochavim,
        Zmanim.Type.ShabbatEnd -> time = zmanRounder.roundToLatestMinute(time)

        Zmanim.Type.SofZmanShema,
        Zmanim.Type.SofZmanTefila,
        Zmanim.Type.MinchaGedola,
        Zmanim.Type.MinchaKetana,
        Zmanim.Type.PlagHaMincha,
        Zmanim.Type.CandleLighting,
        Zmanim.Type.LatestMincha -> time = zmanRounder.roundToEarliestMinute(time)

        else -> {}
      }
    }

    synchronized(this) {
      timeFormatter.timeZone = time.timeZone
      return timeFormatter.format(time.time)
    }
  }


  /**
   * Helper class that handles rounding of zmanim.
   */
  private class ZmanRounder {

    /**
     * Because the application does not show seconds to the user, we want to ensure that we're not going
     *  to cause an error. Therefore, for some zmanim, we want to ensure that the time which gets displayed
     *  is not rounded up. E.g., If the latest time for saying mincha is actually 4:33.35, we're much better
     *  off if we report that time as 4:33, rather than 4:34.
     *
     * @param zman The zman to round
     *
     * @return the rounded zman.
     */
    fun roundToEarliestMinute(
      zman: Calendar
    ): Calendar {

      // Make sure to work on a copy so we don't muck up the real thing!
      val copy = Calendar.getInstance()
      copy.timeZone = zman.timeZone
      copy.time = zman.time

      copy.set(Calendar.SECOND, 0)
      copy.set(Calendar.MILLISECOND, 0)

      return copy
    }


    /**
     * Because the application does not show seconds to the user, we want to ensure that we're not going
     *  to cause an error. Therefore, for some zmanim, we want to ensure that the time which gets displayed
     *  is rounded up. E.g., If the earliest time for putting on tefiln is actually 6:33.05, we're much better
     *  off if we report that time as 6:34, rather than 6:33.
     *
     * @param zman The zman to round
     *
     * @return the rounded zman.
     */
    fun roundToLatestMinute(
      zman: Calendar
    ): Calendar {

      // Make sure to work on a copy so we don't muck up the real thing!
      val copy = Calendar.getInstance()
      copy.timeZone = zman.timeZone
      copy.time = zman.time

      copy.add(Calendar.MINUTE, 1)
      copy.set(Calendar.SECOND, 0)
      copy.set(Calendar.MILLISECOND, 0)

      return copy
    }

  }

}
