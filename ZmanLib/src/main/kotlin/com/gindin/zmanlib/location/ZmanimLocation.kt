/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import com.kosherjava.zmanim.util.GeoLocation
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Extension to the GeoLocation used by the zmanim calculations that adds some additional info used by the
 *  zmanim app.
 */
public class ZmanimLocation : GeoLocation {

  public class Builder {

    /** Name of the location provider. */
    internal var providerName: String? = ZMANIM_PROVIDER

    /** What time is it in this location? */
    internal var locationTime: Long = System.currentTimeMillis()

    /** How accurate is the location? -1 to indicate this isn't applicable. */
    internal var accuracy: Float = -1f

    /** What's the name of this location? */
    internal var locationName: String = UNKNOWN_LOCATION

    /** Time zone for this location. */
    internal var timeZone: TimeZone = TimeZone.getDefault()

    /** What's the latitude of this location? */
    internal var latitude: Double = 0.0

    /** What's the longitude of this location? */
    internal var longitude: Double = 0.0

    /** What's the altitude of this location? <= 0 to indicate unknown. */
    internal var elevation: Double = 0.0

    /** Only used when the user gives us a name and we have to go find out where it is. */
    internal var userName: String? = null


    public fun fromProvider(providerName: String?): Builder {
      this.providerName = providerName
      return this
    }

    public fun locationName(locationName: String?): Builder {
      if (locationName == null) {
        this.locationName = UNKNOWN_LOCATION
      }
      else {
        this.locationName = locationName
      }
      return this
    }

    public fun userName(userName: String?): Builder {
      this.userName = userName
      return this
    }

    public fun at(locationTime: Long): Builder {
      this.locationTime = locationTime
      return this
    }


    /**
     * @param timeZone if null, the default time zone will be used.
     */
    public fun inTimeZone(timeZone: TimeZone?): Builder {
      if (timeZone != null) {
        this.timeZone = timeZone
      }
      else {
        this.timeZone = TimeZone.getDefault()
      }
      return this
    }

    public fun atLatitude(latitude: Double): Builder {
      this.latitude = latitude
      return this
    }

    public fun atLongitude(longitude: Double): Builder {
      this.longitude = longitude
      return this
    }

    public fun atElevation(elevation: Double): Builder {
      if ((elevation < 0) || elevation.isInfinite() || elevation.isNaN()) {
        this.elevation = 0.0
      }
      else {
        this.elevation = elevation
      }

      return this
    }

    public fun withAccuracy(accuracy: Float): Builder {
      if (accuracy >= 0) {
        this.accuracy = accuracy
      }
      else {
        this.accuracy = -1f
      }

      return this
    }

    public fun from(source: ZmanimLocation): Builder {
      return this
        .fromProvider(source.providerName)
        .locationName(source.getLocationName())
        .userName(source.originalUserName)
        .at(source.locationTime)
        .inTimeZone(source.timeZone)
        .atLatitude(source.latitude)
        .atLongitude(source.longitude)
        .atElevation(source.elevation)
        .withAccuracy(source.accuracy)
    }


    public fun invalidLocation(): ZmanimLocation {
      return INVALID_LOCATION
    }

    public fun build(): ZmanimLocation {
      return ZmanimLocation(this)
    }
  }


  /** Who provided this location to us? E.g., 'network', 'gps'. */
  @JvmField
  public val providerName: String?

  /** When was this location determined? */
  @JvmField
  public val locationTime: Long

  /** How accurate do we believe this location to be? */
  @JvmField
  public val accuracy: Float

  /**
   * True indicates this is a known invalid location; false simply means we believe it to be valid, but cannot be
   *  completely sure.
   */
  @JvmField
  public val isInvalid: Boolean

  /**
   * When the user gives us just a name of a location, and we have to go track it down, holding on to that name
   *  allows us to recognize when we're seeing the same location again.
   */
  @JvmField
  public val originalUserName: String?


  private constructor(
    builder: Builder
  ) : super() {

    this.isInvalid = false

    this.providerName = builder.providerName
    this.locationTime = builder.locationTime
    this.originalUserName = builder.userName
    this.accuracy = builder.accuracy
    setLocationName(builder.locationName)
    setLatitude(builder.latitude)
    setLongitude(builder.longitude)
    setElevation(builder.elevation)
    setTimeZone(builder.timeZone)
  }


  /**
   * Only used when we need to build an invalid location.
   */
  private constructor() : super() {

    this.isInvalid = true

    this.providerName = UNKNOWN_LOCATION
    this.locationTime = 0
    this.originalUserName = ""
    this.accuracy = -1f
  }


  override fun getLocationName(): String {
    var name: String? = super.getLocationName()
    if ((name == null) || name == UNKNOWN_LOCATION || name.trim { it <= ' ' }.isEmpty()) {
      name = toLatLongString(latitude, longitude)
    }

    return name
  }


  public fun isSignificantChangeFrom(previousLocation: ZmanimLocation?): Boolean {

    if (previousLocation == null) {
      return true
    }

    // Less than 1km? Don't bother...
    // I suppose you could run into an issue if that 1km puts you in a different time zone
    //  or something like that...
    if (getGeodesicDistance(previousLocation) >= 1000) {
      return true
    }

    // So, we're within 1km, but Has the elevation changed?
    if (Math.abs(elevation - previousLocation.elevation) > 5) {
      return true
    }

    // It's also possible the user has switched to (or from) a custom time zone...
    if (timeZone != previousLocation.timeZone) {
      return true
    }

    return false
  }


  override fun toString(): String {

    val millisInHour = 60 * 60 * 1000

    val sb = StringBuilder()
    sb.append("Location Name:\t").append(getLocationName())
    sb.append("\nLatitude:\t").append(getLatitudeString(latitude, 5)).append(" deg")
    sb.append("\nLongitude:\t").append(getLongitudeString(longitude, 5)).append(" deg")
    sb.append("\nElevation:\t").append(elevation).append(" Meters")
    sb.append("\nTZ Name:\t").append(timeZone.getDisplayName(false, TimeZone.SHORT, Locale.getDefault()))
    sb.append("\nTZ GMT Offset:\t").append(timeZone.rawOffset / millisInHour)
    sb.append("\nTZ DST Offset:\t").append(timeZone.dstSavings / millisInHour)
    sb.append("\nProvider:\t").append(providerName)
    sb.append("\nAccuracy:\t").append(accuracy)

    val sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault())
    sb.append("\nFix:\t").append(sdf.format(Date(locationTime)))

    return sb.toString()
  }


  public fun hasName(): Boolean {

    // Make sure to NOT compare against our overridden getLocationName!
    return UNKNOWN_LOCATION != super.getLocationName()
  }


  public fun hasCoordinates(): Boolean {
    return (latitude != 0.0) && (longitude != 0.0)
  }


  /**
   * Check if this is a resolved location, meaning that we know both the coordinates AND the name.
   */
  public fun isResolved(): Boolean {
    return hasCoordinates() && hasName()
  }


  public fun toShortString(): String {

    val sb = StringBuilder()
    sb.append("Where: ").append(getLocationName())
    sb.append(";L: ").append(toLatLongString(latitude, longitude))
    sb.append(";TZ: ").append(timeZone.getDisplayName(false, TimeZone.SHORT, Locale.getDefault()))

    val sdf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault())
    sb.append(";Fix: ").append(sdf.format(Date(locationTime)))

    return sb.toString()
  }


  /**
   * Writes this zmanim location to the specified output. Does NOT close the stream or perform any other operation to the stream.
   *
   * @throws IOException if there is any error writing to the stream.
   */
  @Throws(IOException::class)
  public fun save(output: OutputStream) {

    val dataOutputStream = DataOutputStream(output)

    val json = ZmanLocationJSON.toJSON(this)
    dataOutputStream.writeUTF(json)
  }


  public companion object {

    /**
     * Whenever we need an invalid location, this one instance will do.
     */
    private val INVALID_LOCATION: ZmanimLocation = ZmanimLocation()


    /** When the location name is unknown, use this, and an appropriate substitution will be made. */
    public const val UNKNOWN_LOCATION: String = " _unk_ "

    private const val ZMANIM_PROVIDER = "Zmanim"


    /**
     * Loads a zmanim location from the specified input, assuming the stream is correctly positioned. Does NOT close the
     *  stream or perform any other operations to the stream.
     *
     * @throws IOException If there is any error interacting with the stream.
     * @throws org.json.JSONException If there is any error reading the location from the stream.
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun load(input: InputStream): ZmanimLocation {

      val inputStream = DataInputStream(input)
      val json = inputStream.readUTF()

      return ZmanLocationJSON.fromJSON(json)
    }


    private fun getLatitudeString(latitude: Double, precision: Int): String {
      return String.format("%." + precision + "f", latitude)
    }


    private fun getLongitudeString(longitude: Double, precision: Int): String {
      return String.format("%." + precision + "f", longitude)
    }


    @JvmStatic
    public fun toLatLongString(latitude: Double, longitude: Double): String {
      return getLatitudeString(latitude, 1) + " lat; " + getLongitudeString(longitude, 1) + " long"
    }
  }

}  // End of ZmanimLocation class
