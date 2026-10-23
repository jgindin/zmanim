/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "GR'A"

internal class MinchaGedola(zmanimCalculator: ZmanimCalculator) : Zmanim(Zmanim.Type.MinchaGedola, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.",
          Zmanim.Type.MinchaGedola,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedola())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 16.1 deg",
          "Magen Avraham, based on the day starting and ending 16.1 degrees below the horizon.",
          Zmanim.Type.MinchaGedola,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedola16Point1Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "30 Minutes",
          "Calculated as 30 minutes after chatzot (and not 1/2 of a shaah zmanit after chatzot).",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedola30Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Lechumra 30 Minutes",
          "Calculated as 30 minutes after chatzot (and not 1/2 of a shaah zmanit after chatzot), except in the winter when 1/2 shaah zmanit is less than 30 minutes.",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedolaGreaterThan30())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 72 Minutes",
          "Magen Avraham, based onthe day starting 72 minutes before sunrise and ending 72 minutes after sunset.",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedola72Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Chacham Yosef Harari-Raful",
          "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedolaAteretTorah())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya",
          "Based on Baal HaTanya's sunrise and sunset calculations.",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedolaBaalHatanya())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya Lechumra 30 Minutes",
          "Based on Baal HaTanya's sunrise and sunset calculations, except in the winter when 1/2 shaah zmanit is less than 30 minutes.",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedolaBaalHatanyaGreaterThan30())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Rabbi Yaakov Moshe Hillel",
          "Calculated as half a shaah zmanit after chatzot with shaot zmaniyot calculated based on a day starting 72 minutes befoe sunrise" +
            "and ending 13.5 minutes after sunset",
          Zmanim.Type.MinchaGedola,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaGedolaAhavatShalom())
        }
      }
    )

    return zmanim
  }
}
