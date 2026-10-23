/*
 * Copyright (c) 2025. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "MG'A 16.1 deg"

internal class SofZmanShema(zmanimCalculator: ZmanimCalculator) : Zmanim(Zmanim.Type.SofZmanShema, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  // A flat table of independent zman definitions, one per opinion, with no control flow: splitting it would only
  //  scatter the table, so the length rule does not apply.
  @Suppress("LongMethod")
  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "3 Hours before Chatzot",
          "Shach in the Nekudas Hakesef (Yora Deah 184), Shevus Yaakov, Chasan Sofer and others.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShma3HoursBeforeChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "16.1 deg to Sunset",
          "Based on the day starting at at alot 16.1° and ending at sunset.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaAlos16Point1ToSunset())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "16.1 deg to tzait 7.083 deg",
          "Based on the day starting at at alot 16.1° and ending at tzait 7.083°.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees(),
          )
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Chacham Yosef Harari-Raful",
          "Based on the day starting 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaAteretTorah())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "GR'A",
          "Based on the day as calculated from sunrise to sunset.",
          Zmanim.Type.SofZmanShema,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaGRA())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A",
          "Based on alot being 72 minutes before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 120 Minutes",
          "Based on alot being 120 minutes or 1/6th of the day before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA120Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "Based on alot being 16.1° before sunrise.",
          Zmanim.Type.SofZmanShema,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA16Point1Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 19.8 deg",
          "Based on alot being 19.8° before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA19Point8Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 72 Minutes Zmaniyot",
          "Based on alot being 72 minutes zmaniyot, or 1/10th of the day before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA72MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 90 Minutes",
          "Based on alot being 90 minutes before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA90Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 90 Minutes Zmaniyot",
          "Based on alot being 90 minutes zmaniyot before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA90MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "96 Minutes",
          "Based on alot being 96 minutes before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA96Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "96 Minutes Zmaniyot",
          "Based on alot being 96 minutes zmaniyot before sunrise.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA96MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "R. Yaakov Moshe Hillel of Bet Horaah",
          "Based on alot being 72 minutes (16.01°) before sunrise, and tzait being when the sun's position at 3.8° below the western horizon.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaAlos16Point01ToTzaisGeonim3Point8Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya",
          "Based on the Baal HaTanya's sunrise and sunset calculations.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaBaalHatanya())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - MG'A 18° half day",
          "Calculated from dawn to nightfall, but calculated " +
            "using the first half of the day only, which starts at alot defined as 18° and ends at fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA18DegreesToFixedLocalChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - MG'A 16.1° half day",
          "Calculated from dawn to nightfall, but calculated " +
            "using the first half of the day only, which starts at alot defined as 16.1° and ends at fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - MG'A 90 minutes half day",
          "Calculated from dawn to nightfall, but calculated " +
            "using the first half of the day only, which starts at alot defined as 90 minutes before sunrise and ends at fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA90MinutesToFixedLocalChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - MG'A 72 minutes half day",
          "Calculated from dawn to nightfall, but calculated " +
            "using the first half of the day only, which starts at alot defined as 72 minutes before sunrise and ends at fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaMGA72MinutesToFixedLocalChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - GR'A half day",
          "Calculated from dawn to nightfall, but calculated " +
            "using the first half of the day only, which starts at sunrise and ends at fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanShmaGRASunriseToFixedLocalChatzos())
        }
      }
    )

    return zmanim
  }
}
