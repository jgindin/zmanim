/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.zman.Zmanim
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.HashSet

private const val CHOL_HA_MOED = "Chol HaMoed "

/** Contains details about a given Observance. */
public abstract class Details(private val date: HebrewDate) {

  /**
   * @return A list of the types of zmanim this day has, ordered by the sequence in which they will
   *   occur.
   */
  public open fun getZmanim(): List<Zmanim.Type> {

    val regularCandles: Boolean
    val lateCandles: Boolean
    if (lightCandles()) {

      lateCandles = (isShabbat() || (isErevYomTovSheni() && !isErevShabbat()))
      regularCandles = !lateCandles
    } else {
      regularCandles = false
      lateCandles = false
    }

    val zmanim = ArrayList<Zmanim.Type>()
    zmanim.add(Zmanim.Type.Alot)
    zmanim.add(Zmanim.Type.Talit)
    zmanim.add(Zmanim.Type.Hanetz)
    zmanim.add(Zmanim.Type.SofZmanShema)
    zmanim.add(Zmanim.Type.SofZmanTefila)
    zmanim.add(Zmanim.Type.Chatzot)
    zmanim.add(Zmanim.Type.MinchaGedola)
    zmanim.add(Zmanim.Type.MinchaKetana)
    zmanim.add(Zmanim.Type.PlagHaMincha)

    if (regularCandles) {
      zmanim.add(Zmanim.Type.CandleLighting)
    }

    zmanim.add(Zmanim.Type.Shkia)
    zmanim.add(Zmanim.Type.LatestMincha)
    zmanim.add(Zmanim.Type.BeinHashmashot)
    zmanim.add(Zmanim.Type.TzaitHacochavim)

    if (sayHavdalah()) {
      zmanim.add(Zmanim.Type.ShabbatEnd)
    }

    if (lateCandles) {
      zmanim.add(Zmanim.Type.YomTovSheniCandleLighting)
    }

    zmanim.add(Zmanim.Type.NightChatzot)

    return Collections.unmodifiableList(zmanim)
  }

  public open fun isErevYomTovSheni(): Boolean {

    return (isPesach() || isShavuot() || isRoshHashana() || isSukkot() || isShminiAtzeret()) &&
      lightCandles() &&
      !isErev() &&
      !isCholHaMoed()
  }

  /** Is this actually erev of the observance? If so, today is NOT actually the observance. */
  public abstract fun isErev(): Boolean

  /**
   * Does one light candles tonight for this observance? This could be the first night of the
   * observance, or in some cases the second or subsequent evenings too.
   */
  public abstract fun lightCandles(): Boolean

  /**
   * Does one say havdalah tonight at the end of this observance? Not all observances end with
   * havdalah.
   */
  public abstract fun sayHavdalah(): Boolean

  public open fun isRoshChodesh(): Boolean {
    return false
  }

  public open fun isTaanit(): Boolean {
    return false
  }

  public open fun isYomTov(): Boolean {
    return false
  }

  public open fun isCholHaMoed(): Boolean {
    return false
  }

  public open fun isPesach(): Boolean {
    return false
  }

  public open fun isSukkot(): Boolean {
    return false
  }

  public open fun isShminiAtzeret(): Boolean {
    return false
  }

  public open fun isSimchatTorah(): Boolean {
    return false
  }

  public open fun isShavuot(): Boolean {
    return false
  }

  public open fun isPurim(): Boolean {
    return false
  }

  public open fun isChanukah(): Boolean {
    return false
  }

  public open fun isRoshHashana(): Boolean {
    return false
  }

  public open fun isYomKippur(): Boolean {
    return false
  }

  /**
   * Zehut Yosef has the following regarding when tachanun are not said:
   *
   * Every day, Tahanunim are recited during Shaharit and Minha, with the following exceptions:
   * <ol>
   * <li>Shabbat
   * <li>Rosh Hodesh
   * <li>Moed (all the days of Pesah, Shavuot and Sukkot)
   * <li>The whole month of Nisan
   * <li>Yom Aatsmaut (5 Iyar)
   * <li>Pesah Sheni (14 Iyar)
   * <li>Lag LaOmer (18 Iyar)
   * <li>Yom Yerushalayim (28 Iyar)
   * <li>From 1-12 of Sivan
   * <li>9 Av
   * <li>15 Av
   * <li>Erev Rosh Ashanah
   * <li>From Erev Kippur until the end of Tishri
   * <li>Hanukkah
   * <li>15 Shevat
   * <li>14 and 15 of Adar Rishon
   * <li>Purim and the day after (Shushan Purim).
   * </ol>
   *
   * On all the days shown above, Tahanunim are not recited on Minha of the prior day, with the
   * exception of Erev Rosh Ashanah and Erev Yom Kippur.
   *
   * <p/>
   *
   * Also, Tahanunim are not recited (during Shaharit only) when there is a Hatan (bridegroom) in
   * the synagogue or when there is a Berit Milah in the synagogue and the Avi Aben (father of the
   * baby), or the Mo’el, or the Sandak (godfather) are praying with the minyan; or on the actual
   * day of a Bar Mitsvah.
   *
   * <p/>
   *
   * Tahanunim are not recited in a House of Mourning.
   */
  public open fun sayTachanun(): Boolean {
    return !isShabbat()
  }

  /**
   * A complete description of today's observance. Could be something simple like, "Yom Kippur" or
   * something more complex like "Pesach 4 Chol HaMoed / 3rd Day of the Omer"
   */
  public abstract fun getDescription(): String

  /** A non-null Details may still not be anything interesting. */
  public open fun isAnything(): Boolean {
    // If someone wants to create a 'nothing' details, they'll have to override this.
    return true
  }

  public open fun composeWith(otherDetails: Details): Details {
    return CompositeDetails(date, this, otherDetails)
  }

  protected fun isErevShabbat(): Boolean {

    val shabbatDetails = Holidays.SHABBAT.getDetailsFor(date)
    return null != shabbatDetails && shabbatDetails.isErev()
  }

  public open fun isShabbat(): Boolean {

    val shabbatDetails = Holidays.SHABBAT.getDetailsFor(date)
    return null != shabbatDetails && !shabbatDetails.isErev()
  }

  /** Is this a day of the omer? */
  protected open fun isOmer(): Boolean {
    return false
  }

  protected fun getTodaysDate(): HebrewDate {
    return date
  }

  public companion object {

    protected const val EREV: String = "Erev "
  }

  public open class Erev
  @JvmOverloads
  constructor(
    date: HebrewDate,
    private val name: String,
    private val candles: Boolean = true,
  ) : Details(date) {

    override fun isErev(): Boolean {
      return true
    }

    override fun lightCandles(): Boolean {
      // No candle lighting if we're coming straight out of Shabbat...
      return (candles && !isShabbat()) || isErevShabbat()
    }

    override fun sayHavdalah(): Boolean {

      // Could be that Shabbat is erev something...
      return isShabbat()
    }

    override fun getDescription(): String {
      return EREV + name
    }
  }

  /** Intended for simple details about a day. */
  public open class Yom(
    date: HebrewDate,
    @JvmField protected val name: String,
  ) : Details(date) {

    override fun isErev(): Boolean {
      return false
    }

    override fun lightCandles(): Boolean {
      return isErevShabbat()
    }

    override fun sayHavdalah(): Boolean {
      return isShabbat()
    }

    override fun getDescription(): String {
      return name
    }
  }

  public open class ErevYomTov
  @JvmOverloads
  constructor(
    date: HebrewDate,
    name: String,
    candles: Boolean = true,
  ) : Erev(date, name, candles) {

    override fun sayHavdalah(): Boolean {
      // Erev of a holiday doesn't get havdalah, even when the erev is motsa'e Shabbat.
      return false
    }
  }

  public open class YomTovDetails(
    date: HebrewDate,
    @JvmField protected val name: String,
    private val lightCandles: Boolean,
  ) : Details(date) {

    override fun isErev(): Boolean {
      return false
    }

    override fun isYomTov(): Boolean {
      return true
    }

    override fun lightCandles(): Boolean {

      // If we don't want to light candles for this yom tov, but it's erev Shabbat, then we DO light
      // candles.
      return (lightCandles || isErevShabbat())
    }

    override fun sayHavdalah(): Boolean {

      // If we want to say havdalah for this yom tov, but it's erev Shabbat, then we don't say
      // havdalah.
      return (!lightCandles && !isErevShabbat())
    }

    override fun sayTachanun(): Boolean {
      return false
    }

    override fun getDescription(): String {
      return name
    }
  }

  public open class CholHaMoedDetails(
    date: HebrewDate,
    /** This is the day of the chag, NOT the day of chol hamoed. */
    private val dayOfChag: Int,
    private val name: String,
    private val lightCandles: Boolean,
  ) : Details(date) {

    override fun isErev(): Boolean {
      return false
    }

    override fun lightCandles(): Boolean {
      return lightCandles || isErevShabbat()
    }

    override fun sayHavdalah(): Boolean {
      return isShabbat()
    }

    override fun getDescription(): String {
      return CHOL_HA_MOED + name + " " + dayOfChag
    }

    override fun isCholHaMoed(): Boolean {
      return true
    }

    override fun sayTachanun(): Boolean {
      return false
    }
  }

  private class CompositeDetails(
    date: HebrewDate,
    details: Details,
    otherDetails: Details,
  ) : Details(date) {

    /** The details of the dates that are composed into this. */
    private val composedDates: MutableList<Details> = ArrayList()

    /**
     * There are cases where the only way to determine something about today is by looking at
     * tomorrow. E.g., should we say havdalah? Well, if today is Shabbat, then yes. Unless it's the
     * first day of Pesach or Sukkot, that is... This field will be filledin lazily if it's needed.
     */
    private var tomorrowsDetails: Details? = null

    init {
      composeWith(details)
      composeWith(otherDetails)
    }

    override fun isErev(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isErev()) {
          return true
        }
      }

      return false
    }

    override fun isYomTov(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isYomTov()) {
          return true
        }
      }

      return false
    }

    override fun isCholHaMoed(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isCholHaMoed()) {
          return true
        }
      }

      return false
    }

    override fun lightCandles(): Boolean {

      var light = false
      for (composedDate in composedDates) {
        if (composedDate.lightCandles()) {
          light = true
          break
        }
      }

      // No candle lighting if we're coming straight out of Shabbat...
      return light && !isShabbat()
    }

    override fun sayHavdalah(): Boolean {

      var havdalah = false
      for (composedDate in composedDates) {
        if (composedDate.sayHavdalah()) {
          havdalah = true
          break
        }
      }

      // If we want to say havdalah, but it's erev Shabbat, then we don't say havdalah. Also, if
      //  tomorrow is something (e.g., 2nd day of Pesach or Sukkot), then we also don't want to say
      //  havdalah.
      var tomorrow = tomorrowsDetails
      if (tomorrow == null) {
        tomorrow = getTodaysDate().tomorrow().getDetails()
        tomorrowsDetails = tomorrow
      }

      return (havdalah && !isErevShabbat() && !tomorrow.isYomTov())
    }

    override fun isRoshChodesh(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isRoshChodesh()) {
          return true
        }
      }

      return false
    }

    override fun isShabbat(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isShabbat()) {
          return true
        }
      }

      return false
    }

    override fun isTaanit(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isTaanit()) {
          return true
        }
      }

      return false
    }

    override fun isPesach(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isPesach()) {
          return true
        }
      }

      return false
    }

    override fun isSukkot(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isSukkot()) {
          return true
        }
      }

      return false
    }

    override fun isShminiAtzeret(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isShminiAtzeret()) {
          return true
        }
      }

      return false
    }

    override fun isSimchatTorah(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isSimchatTorah()) {
          return true
        }
      }

      return false
    }

    override fun isShavuot(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isShavuot()) {
          return true
        }
      }

      return false
    }

    override fun isPurim(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isPurim()) {
          return true
        }
      }

      return false
    }

    override fun isChanukah(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isChanukah()) {
          return true
        }
      }

      return false
    }

    override fun isRoshHashana(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isRoshHashana()) {
          return true
        }
      }

      return false
    }

    override fun isYomKippur(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isYomKippur()) {
          return true
        }
      }

      return false
    }

    override fun sayTachanun(): Boolean {
      for (composedDate in composedDates) {
        if (!composedDate.sayTachanun()) {
          return false
        }
      }

      return true
    }

    override fun getDescription(): String {
      val sb = StringBuilder()

      Collections.sort(composedDates, COMPARATOR)

      for (composedDate in composedDates) {
        val composedDateDescription = composedDate.getDescription()

        if ((sb.length > 0) && (composedDateDescription.length > 0)) {
          sb.append(" / ")
        }
        sb.append(composedDateDescription)
      }

      return sb.toString()
    }

    override fun composeWith(otherDetails: Details): Details {
      composedDates.add(otherDetails)
      return this
    }

    override fun isAnything(): Boolean {
      for (composedDate in composedDates) {
        if (composedDate.isAnything()) {
          return true
        }
      }

      return false
    }

    override fun getZmanim(): List<Zmanim.Type> {

      // This isn't really the prettiest, but...hand-writing something would no doubt be worse...
      val combined = HashSet<Zmanim.Type>()
      for (details in composedDates) {
        combined.addAll(details.getZmanim())
      }

      // NOTE: If one contains Havdalah, but another is Erev Shabbat OR a Yom Tov, then take out the
      // havdalah.
      if (!sayHavdalah()) {
        combined.remove(Zmanim.Type.ShabbatEnd)
      }

      val todaysZmanim = ArrayList<Zmanim.Type>()
      todaysZmanim.addAll(combined)
      Collections.sort(todaysZmanim, Comparator<Zmanim.Type> { t1, t2 -> t1.ordinal - t2.ordinal })

      return todaysZmanim
    }

    private companion object {

      /**
       * This comparator is used to arrange the dates which are composed into this one date. The way
       * CompositeDetails is used, we can assert the following. Every Details represented in this
       * composite:
       * <ul>
       * <li>is for the SAME date
       * <li>represents *something*, whether it be Erev or actual Yom.
       * </ul>
       */
      private val COMPARATOR: Comparator<Details> = Comparator { lhs, rhs ->
        if (!lhs.isAnything() && !rhs.isAnything()) {
          // Both are nothing... This should not happen.
          return@Comparator 0
        }

        if (lhs.isAnything() && !rhs.isAnything()) {
          // The one that has something should come first. This should not happen.
          return@Comparator -1
        }

        if (!lhs.isAnything() && rhs.isAnything()) {
          // The one that has something should come first. This should not happen.
          return@Comparator 1
        }

        // OK, so now we KNOW that both are *something*.
        //  * Omer should always come LAST
        if (lhs.isOmer() && !rhs.isOmer()) {
          return@Comparator 1
        }

        if (rhs.isOmer() && !lhs.isOmer()) {
          return@Comparator -1
        }

        // We always want the erev of something to come AFTER a day which *is* something itself.
        if (lhs.isErev() && rhs.isErev()) {
          return@Comparator 0
        }

        if (lhs.isErev() && !rhs.isErev()) {
          return@Comparator 1
        }

        if (!lhs.isErev() && rhs.isErev()) {
          return@Comparator -1
        }

        0
      }
    }
  } // End of CompositeDetails class
} // End of Details class
