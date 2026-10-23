/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import com.kosherjava.zmanim.AstronomicalCalendar
import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "8.5 deg"
private const val TZAIT_3_POINT_5_DEGREES = 3.5
private const val TZAIT_6_POINT_5_DEGREES = 6.5

internal open class TzaitHacochavim(
  type: Zmanim.Type,
  zmanimCalculator: ZmanimCalculator,
) : Zmanim(type, zmanimCalculator) {

  constructor(zmanimCalculator: ZmanimCalculator) : this(Zmanim.Type.TzaitHacochavim, zmanimCalculator)

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    return mergeZmanimLists(
      buildStandardTzaitZmanim(Zmanim.Type.TzaitHacochavim, zmanimCalculator, true),
      buildExtraTzaitZmanim(Zmanim.Type.TzaitHacochavim, zmanimCalculator),
    )
  }

  protected companion object {

    @JvmStatic
    protected fun mergeZmanimLists(
      firstList: MutableList<Zman>,
      secondList: List<Zman>?,
    ): MutableList<Zman> {

      if (null != secondList) {
        firstList.addAll(secondList)
      }

      return firstList
    }

    // A flat table of independent zman definitions, one per opinion, with no control flow: splitting it would only
    //  scatter the table, so the length rule does not apply.
    @Suppress("LongMethod")
    @JvmStatic
    protected fun buildStandardTzaitZmanim(
      zmanType: Zmanim.Type,
      zmanimCalculator: ZmanimCalculator,
      includeBaalHaTanya: Boolean,
    ): MutableList<Zman> {

      val zmanim = ArrayList<Zman>()

      zmanim.add(
        object :
          Zman(
            "MG'A 120 Minutes",
            "Based on the time to walk a Mil in 24 minutes, for a total of 120 minutes based on the opinion of Ula who calculated tzait as 5 Mil after sea level shkiah.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais120())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "GR'A 120 Minutes Zmaniyot",
            "120 minutes zmaniyot after sea level sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais120Zmanis())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "3.5 deg",
            "When the sun is 3.5 degrees below the western geometric horizon after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(
              zmanimCalculator,
              zmanimCalculator.getSunsetOffsetByDegrees(
                AstronomicalCalendar.GEOMETRIC_ZENITH + TZAIT_3_POINT_5_DEGREES
              ),
            )
          }
        }
      )

      if (includeBaalHaTanya) {
        zmanim.add(
          object :
            Zman(
              "Baal HaTanya",
              "Based on the sun's position at 6 degrees below the western horizon.",
              zmanType,
            ) {
            override fun calculate(): Calendar? {
              return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisBaalHatanya())
            }
          }
        )
      }

      zmanim.add(
        object :
          Zman(
            "6.5 deg",
            "When the sun is 6.5 degrees below the western geometric horizon after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(
              zmanimCalculator,
              zmanimCalculator.getSunsetOffsetByDegrees(
                AstronomicalCalendar.GEOMETRIC_ZENITH + TZAIT_6_POINT_5_DEGREES
              ),
            )
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            DEFAULT_ZMAN_NAME,
            "When the sun is 8.5 degrees below the western geometric horizon after sunset.",
            zmanType,
            false,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "16.1 deg",
            "When the sun is 16.1 degrees below the western geometric horizon after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais16Point1Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "18 deg",
            "When the sun is 18 degrees below the western geometric horizon after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais18Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "19.8 deg",
            "When the sun is 19.8 degrees below the western geometric horizon after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais19Point8Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "26 deg",
            "When the sun is 26 degrees below the western geometric horizon after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais26Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Chavas Yair and Divray Malkiel",
            "Based on the time to walk 4 Mil at 15 minutes/Mil.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais60())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Rambam / Rabainu Tam",
            "Based on the time to walk 4 Mil at 18 minutes/Mil.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais72())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "72 Minutes Zmaniyot",
            "Calculated as 72 minutes zmaniyot, or 1/10th of the day after sea level sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais72Zmanis())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "MG'A 90 Minutes",
            "Based on the time to walk 5 Mil at 18 minutes/Mil.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais90())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "GR'A 90 Minutes Zmaniyot",
            "90 minutes zmaniyot after sea level sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais90Zmanis())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "96 Minutes",
            "96 minutes after sea level sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais96())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "GR'A 96 Minutes Zmaniyot",
            "96 minutes zmaniyot after sea level sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzais96Zmanis())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Chacham Yosef Harari-Raful",
            "Usually calculated as 40 minutes after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisAteretTorah())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 3.65 deg",
            "Based on the sun's position at 3.65 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim3Point65Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 4.37 deg",
            "Based on the sun's position at 4.37 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim4Point37Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 4.61 deg",
            "Based on the sun's position at 4.61 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim4Point61Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 4.8 deg",
            "Based on the sun's position at 4.8 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim4Point8Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 5.88 deg",
            "Based on the sun's position at 5.88 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim5Point88Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 5.95 deg",
            "Based on the sun's position at 5.95 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim5Point95Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 7.083 deg",
            "Based on the sun's position at 7.083 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim7Point083Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Geonim 8.5 deg",
            "Based on the sun's position at 8.5 degrees below the western horizon.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaisGeonim8Point5Degrees())
          }
        }
      )

      zmanim.add(
        object :
          Zman(
            "Rabbi Moshe Feinstein",
            "For use in the New York area: Fixed 50 minutes after sunset.",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaitRavMoshe())
          }
        }
      )

      return zmanim
    }

    @JvmStatic
    protected fun buildExtraTzaitZmanim(
      zmanType: Zmanim.Type,
      zmanimCalculator: ZmanimCalculator,
    ): List<Zman> {

      val zmanim = ArrayList<Zman>()

      zmanim.add(
        object :
          Zman(
            "Seattle Sephardic",
            "Based on the Seattle Sephardic community's tradition (45 minutes).",
            zmanType,
          ) {
          override fun calculate(): Calendar? {
            return toCalendar(zmanimCalculator, zmanimCalculator.getTzaitSeattleSephardic())
          }
        }
      )

      return zmanim
    }
  }
}
