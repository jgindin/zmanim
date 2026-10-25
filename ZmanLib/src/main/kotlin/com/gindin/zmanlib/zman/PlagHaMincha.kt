/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "GR'A"

internal class PlagHaMincha(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.PlagHaMincha, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  // A flat table of independent zman definitions, one per opinion, with no control flow: splitting
  // it would only
  //  scatter the table, so the length rule does not apply.
  @Suppress("LongMethod")
  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.",
          Zmanim.Type.PlagHaMincha,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 120 Minutes",
          "MG'A where the day runs from dawn to dusk. Dawn for this calculation is 120 minutes before sunrise and dusk is 120 minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha120Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 120 Minutes Zmaniyot",
          "MG'A where the day runs from dawn to dusk. Dawn for this calculation is 120 minutes zemaniyot before sunrise and dusk is 120 minutes zemaniyot after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha120MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 16.1 deg",
          "Based on the day starting at alot 16.1 degrees and ending at tzait 16.1 degrees. This is calculated as 10.75 hours zmaniyot after dawn",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha16Point1Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 18 deg",
          "Based on the day starting at alot 18 degrees and ending at tzait 18 degrees.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha18Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 19.8 deg",
          "Based on the the day starting at alot 19.8 degrees and ending at tzait 19.8 degrees.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha19Point8Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 26 deg",
          "Based on the day starting at alot 26 degrees and ending at tzait 26 degrees.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha26Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 60 Minutes",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 60 minutes before sunrise and dusk is 60 minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha60Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 72 Minutes",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 72 minutes before sunrise and dusk is 72 minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha72Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 72 Minutes Zmaniyot",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 72 minutes zemaniyot before sunrise and dusk is 72 zemaniyot minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha72MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 90 Minutes",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 90 minutes before sunrise and dusk is 90 minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha90Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 90 Minutes Zmaniyot",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 90 minutes zemaniyot before sunrise and dusk is 90 zemaniyot minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha90MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 96 Minutes",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 96 minutes before sunrise and dusk is 96 minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha96Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 96 Minutes Zmaniyot",
          "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 96 minutes zemaniyot before sunrise and dusk is 96 zemaniyot minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHamincha96MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Chacham Yosef Harari-Raful",
          "Based on Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.",
          Zmanim.Type.PlagHaMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHaminchaAteretTorah())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "R. Moshe Feinstein's Rabbenu Tam",
          "Calculated based on the day running from alot 16.1 degrees to tzait 8.5 degrees.",
          Zmanim.Type.PlagHaMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagAlot16Point1ToTzait8Point5())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "16.1 deg - 7.083 deg ",
          "Based on the day starting at alot 16.1 degrees and ending at tzait 7.083 degrees.",
          Zmanim.Type.PlagHaMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getPlagAlos16Point1ToTzaisGeonim7Point083Degrees(),
          )
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "16.1 deg - Sunset",
          "Based on the day starting at alot 16.1 degrees and ending at sunset.",
          Zmanim.Type.PlagHaMincha,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagAlosToSunset())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya",
          "Based on Baal HaTanya's sunrise and sunset calculations.",
          Zmanim.Type.PlagHaMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagHaminchaBaalHatanya())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Rabbi Yaakov Moshe Hillel",
          "Calculated as 1.25 shaot zmaniyot before " +
            "tzait 3.8 deg with shaot zmaniyot calculated based on a day starting at alos 16.1 deg and " +
            "ending at tzait 3.8 deg.",
          Zmanim.Type.PlagHaMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getPlagAhavatShalom())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - GR'A half day",
          "calculated according to the GR'A that the day ends at sunset and is 4.75 shaot zmaniyot (solar hours) after fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getPlagHaminchaGRAFixedLocalChatzosToSunset(),
          )
        }
      }
    )

    return zmanim
  }
}
