/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "GR'A"

/** The times for the last time to each chametz are the same as the latest time to say tefila. */
public class SofZmanAchilatChametz internal constructor(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.SofZmanAchilatChametz, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  // A flat table of independent zman definitions, one per opinion, with no control flow: splitting
  //  it would only scatter the table, so the length rule does not apply.
  @Suppress("LongMethod")
  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "2 Hours before Chatzot",
          "2 hours before chatzot.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfila2HoursBeforeChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "Based on the day as calculated from sunrise to sunset.",
          Zmanim.Type.SofZmanAchilatChametz,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanAchilasChametzGRA())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Chacham Yosef Harari-Raful",
          "Based on the day starting at 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilahAteretTorah())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A",
          "Based on alot being 72 minutes before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 120 Minutes",
          "Based on alot being 120 minutes before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA120Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 16.1 deg",
          "Based on alot being 16.1 degrees before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getSofZmanAchilasChametzMGA16Point1Degrees(),
          )
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 19.8 deg",
          "Based on alot being 19.8 degrees before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA19Point8Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 72 Minutes Zmaniyot",
          "Based on alot being 72 minutes zmaniyot before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getSofZmanAchilasChametzMGA72Minutes(),
          )
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 90 Minutes",
          "Based on alot being 90 minutes before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA90Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 90 Minutes Zmaniyot",
          "Based on alot being 90 minutes zmaniyot before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA90MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 96 Minutes",
          "Based on alot being 96 minutes before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA96Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 96 Minutes Zmaniyot",
          "Based on alot being 96 minutes zmaniyot before sunrise.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA96MinutesZmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya",
          "Based on Baal HaTanya's sunrise and sunset calculations.",
          Zmanim.Type.SofZmanAchilatChametz,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getSofZmanAchilasChametzBaalHatanya(),
          )
        }
      }
    )

    return zmanim
  }
}
