/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/**
 * Used to parse a string into a latitude or a longitude.
 *
 * <p>
 *
 * <p>
 * Supported formats are:
 * <ul>
 * <li> DDD MM['] [SS["]] -- Assumed North
 * <li> [N|S] DDD MM['] [SS["]]
 * <li> DDD.MM[N|S]
 * </ul>
 */
public open class LatitudeLongitudeParser
protected constructor(
  direction: Direction,
  degrees: Int,
  minutes: Int,
  seconds: Double,
  maxDegrees: Int,
) {

  public enum class Direction(internal val abbreviation: String) {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W"),
  }

  @JvmField public val direction: Direction

  @JvmField public val minutes: Int

  @JvmField public val degrees: Int

  @JvmField public val seconds: Double

  init {

    var adjustedDirection = direction
    var adjustedDegrees = degrees

    if (adjustedDegrees < 0) {
      if (Direction.NORTH == adjustedDirection) {
        adjustedDirection = Direction.SOUTH
      } else if (Direction.EAST == adjustedDirection) {
        adjustedDirection = Direction.WEST
      }

      adjustedDegrees = adjustedDegrees * -1
    }

    this.direction = adjustedDirection
    this.minutes = minutes
    this.seconds = seconds

    // If the value is still too large, then just choose zero.
    if (adjustedDegrees > maxDegrees) {
      this.degrees = 0
    } else {
      this.degrees = adjustedDegrees
    }
  }

  public fun toFullString(withMarkers: Boolean): String {
    return direction.abbreviation + " " + formattedDMS(withMarkers)
  }

  public fun toSimpleString(withMarkers: Boolean): String {
    val str: String
    if ((Direction.SOUTH == direction) || (Direction.WEST == direction)) {
      str = direction.abbreviation + " "
    } else {
      str = ""
    }

    return str + formattedDMS(withMarkers)
  }

  private fun formattedDMS(withMarkers: Boolean): String {

    val str = StringBuilder().append(degrees)
    if ((0 != minutes) || (0.0 != seconds)) {
      str.append(" ").append(minutes)
      if (withMarkers) {
        str.append("'")
      }
    }

    if (0.0 != seconds) {
      str.append(" ").append(seconds)
      if (withMarkers) {
        str.append("\"")
      }
    }

    return str.toString()
  }

  public companion object {

    private const val MAX_LATITUDE = 90

    private const val MAX_LONGITUDE = 180

    private const val MINUTES_PER_DEGREE = 60
    private const val SECONDS_PER_MINUTE = 60

    @JvmStatic
    public fun latitude(
      direction: Direction,
      degrees: Int,
      minutes: Int,
      seconds: Int,
    ): LatitudeLongitudeParser {
      return LatitudeLongitudeParser(direction, degrees, minutes, seconds.toDouble(), MAX_LATITUDE)
    }

    @JvmStatic
    public fun latitude(
      direction: Direction,
      degreesText: String,
      minutesText: String?,
      secondsText: String,
    ): LatitudeLongitudeParser {
      return parse(direction, degreesText, minutesText, secondsText, MAX_LATITUDE)
    }

    @JvmStatic
    public fun latitude(value: String): LatitudeLongitudeParser {
      return parse(value, Direction.NORTH, MAX_LATITUDE)
    }

    @JvmStatic
    public fun latitudeValue(value: String): Double {

      var latitude = parseAsDouble(value, Direction.NORTH, MAX_LATITUDE)

      // Latitude must be -90 <= lat <= 90
      if ((latitude < -MAX_LATITUDE) || (latitude > MAX_LATITUDE)) {
        latitude = 0.0
      }

      return latitude
    }

    @JvmStatic
    public fun longitude(
      direction: Direction,
      degrees: Int,
      minutes: Int,
      seconds: Int,
    ): LatitudeLongitudeParser {
      return LatitudeLongitudeParser(direction, degrees, minutes, seconds.toDouble(), MAX_LONGITUDE)
    }

    @JvmStatic
    public fun longitude(
      direction: Direction,
      degreesText: String,
      minutesText: String?,
      secondsText: String,
    ): LatitudeLongitudeParser {
      return parse(direction, degreesText, minutesText, secondsText, MAX_LONGITUDE)
    }

    @JvmStatic
    public fun longitude(value: String): LatitudeLongitudeParser {
      return parse(value, Direction.EAST, MAX_LONGITUDE)
    }

    @JvmStatic
    public fun longitudeValue(value: String): Double {

      var longitude = parseAsDouble(value, Direction.EAST, MAX_LONGITUDE)

      // Longitude must be -180 <= long <= 180
      if ((longitude < -MAX_LONGITUDE) || (longitude > MAX_LONGITUDE)) {
        longitude = MAX_LONGITUDE.toDouble()
      }

      return longitude
    }

    private fun parse(
      direction: Direction,
      degreesText: String,
      minutesText: String?,
      secondsText: String,
      maxDegrees: Int,
    ): LatitudeLongitudeParser {

      var parseMinutes = true

      var degrees: Int
      var minutes = 0
      var seconds = 0.0

      try {
        degrees = degreesText.toInt()
      } catch (e: NumberFormatException) {
        // Maybe the user doesn't have separate minutes & degrees, so they just entered something
        // like "47.5461"?
        try {
          val degreesDouble = degreesText.toDouble()

          // Casting the double to an int will give us the degrees, by just lopping off the part
          // after the decimal.
          degrees = degreesDouble.toInt()

          // Now, convert the decimal portion to the minutes.
          // http://geography.about.com/library/howto/htdegrees.htm
          val doubleMinutes = Math.abs(((degreesDouble - degrees) * MINUTES_PER_DEGREE))
          minutes = doubleMinutes.toInt()

          seconds = ((doubleMinutes - minutes) * SECONDS_PER_MINUTE)

          parseMinutes = false
        } catch (e1: NumberFormatException) {
          // Nope... just give up.
          degrees = 0
          minutes = 0
          seconds = 0.0
        }
      }

      if (parseMinutes) {

        try {
          minutes = minutesText?.toInt() ?: 0
        } catch (e: NumberFormatException) {
          minutes = 0
        }

        try {
          seconds = secondsText.toDouble()
        } catch (e: NumberFormatException) {
          seconds = 0.0
        }
      }

      return LatitudeLongitudeParser(direction, degrees, minutes, seconds, maxDegrees)
    }

    /**
     * Attempts to parse the specified string representing a latitude or longitude into a double
     * containing the decimal representation.
     */
    private fun parseAsDouble(
      value: String,
      defaultDirection: Direction,
      maxDegress: Int,
    ): Double {

      val parsed = parse(value, defaultDirection, maxDegress)

      // See
      // http://stackoverflow.com/questions/6945008/converting-value-longitude-valuesdmscompass-direction-format-to-correspondi
      var convertedDegrees =
        parsed.degrees +
          (((parsed.seconds / SECONDS_PER_MINUTE)) + parsed.minutes) / MINUTES_PER_DEGREE.toDouble()

      if ((Direction.SOUTH == parsed.direction) || (Direction.WEST == parsed.direction)) {
        convertedDegrees = -1 * convertedDegrees
      }

      return convertedDegrees
    }

    private fun parse(
      value: String,
      defaultDirection: Direction,
      maxDegrees: Int,
    ): LatitudeLongitudeParser {

      // Make sure there's no extra whitespace...
      var remaining = value.javaTrim()

      val extracted = extractDirection(remaining, defaultDirection)
      remaining = extracted.second

      // Clean up the input....
      remaining = remaining.replace('.', ' ')
      remaining = remaining.replace('\'', ' ')
      remaining = remaining.replace('"', ' ')
      remaining = remaining.javaTrim()

      val latitudeComponents = remaining.split(" ")
      if (latitudeComponents.isEmpty()) {
        throw IllegalArgumentException("Can't parse value: $remaining")
      }

      val degrees = latitudeComponents[0].javaTrim().toInt()

      val minutes: Int
      if (latitudeComponents.size > 1) {
        minutes = latitudeComponents[1].javaTrim().toInt()
      } else {
        minutes = 0
      }

      val seconds: Int
      if (latitudeComponents.size > 2) {
        seconds = latitudeComponents[2].javaTrim().toInt()
      } else {
        seconds = 0
      }

      return LatitudeLongitudeParser(
        extracted.first,
        degrees,
        minutes,
        seconds.toDouble(),
        maxDegrees,
      )
    }

    private fun extractDirection(
      value: String,
      defaultDirection: Direction,
    ): Pair<Direction, String> {

      val leading = toDirection(value[0])
      val trailing = toDirection(value[value.length - 1])

      return when {
        leading != null -> Pair(leading, value.substring(1))
        trailing != null -> Pair(trailing, value.substring(0, value.length - 1))
        else -> Pair(defaultDirection, value)
      }
    }

    private fun toDirection(letter: Char): Direction? {
      return when (letter) {
        'S',
        's' -> Direction.SOUTH
        'W',
        'w' -> Direction.WEST
        'N',
        'n' -> Direction.NORTH
        'E',
        'e' -> Direction.EAST
        else -> null
      }
    }
  }
}

/** Trims the way java.lang.String.trim() does (every character up to and including the space). */
private fun String.javaTrim(): String = trim { it <= ' ' }
