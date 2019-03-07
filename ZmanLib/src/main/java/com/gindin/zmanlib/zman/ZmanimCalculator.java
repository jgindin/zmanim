/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import com.gindin.zmanlib.calendar.HebrewDate;

import net.sourceforge.zmanim.AstronomicalCalendar;
import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;
import net.sourceforge.zmanim.util.NOAACalculator;

import java.util.Calendar;
import java.util.Date;

/**
 * Used to calculate all of our zmanim.
 */
public class ZmanimCalculator
  extends ComplexZmanimCalendar {

  /**
   * How many minutes after sunset can you still start mincha?
   * <p/>
   * Daily Halacha (http://www.dailyhalacha.com/displayRead.asp?readID=609&txtSearch=mincha):
   *
   *  "At first glance, it would appear that the final time for reciting Minha is “Sheki’a,” or sundown, and this is
   *    indeed the view of the Mishna Berura...
   * <p/>
   *  "Others, however, disagree, and claim that one may, if necessary, pray Minha even after sunset, during the period
   *    of Ben Ha’shemashot (“twilight”). This was the position of Hacham Ben Sion Abba Shaul (Israel, 1923-1998), and
   *    he calculated Ben Ha’shemashot as extending for 26 minutes after sundown. Hacham Ovadia Yosef similarly allows
   *    reciting Minha during the period of Ben Ha’shemashot, but he does not accept Hacham Ben Sion’s calculation. In
   *    his view, Ben Ha’shemashot extends for only 13.5 minutes after sundown...
   * <p/>
   *  "May a person recite Minha if he knows that he will be unable to complete the Amida prayer before the final time?
   *    For example, if a person realizes at exactly 13 minutes after sunset that he has not yet prayed Minha, may be
   *    begin praying at that point, realizing that most of his Amida prayer will be recited after the final time for
   *    Minha, which occurs just 30 seconds later?
   * <p/>
   * "The Ben Ish Hai (Rav Yosef Haim of Baghdad, 1833-1909), in one place in his writings, rules that one should not
   *    begin the Amida if he will be unable to complete the prayer before the final time. Elsewhere, he writes that
   *    the majority of the Amida must be recited before the final time, and therefore one may begin praying the Amida
   *    if he will be able to recite most of the prayer before 13.5 minutes after sunset. Hacham Ovadia Yosef, however,
   *    rules that it suffices to begin the Amida prayer before the final time. As long as 13.5 minutes have yet to
   *    pass since sunset, one may begin the Amida, even if the majority of the Amida will be recited after this point."
   * <p/>
   */
  private static final int                                  MINUTES_AFTER_SUNSET_FOR_MINCHA_OVADIA_YOSEF = 13;

  private static final int                                  MINUTES_AFTER_SUNSET_FOR_MINCHA_BEN_SION_ABBA_SHAUL = 26;


  /**
   * In Seattle, the Sephardic community considers Shabbat to be out 45 minutes after sunset.
   */
  private static final int                                  MINUTES_AFTER_SUNSET_FOR_SEATTLE_SEPHARDIC_TZAIT = 45;

  private static final int                                  MINUTES_AFTER_SUNSET_RAV_MOSHE = 50;

  /**
   * Minutes in an hour.
   */
  private static final int                                  MINUTES_PER_HOUR = 60;

  /**
   * How many milliseconds in a minute?
   *<p>
   * Have to copy this since it is package-local in the base class.
   */
  private static final long                                 MINUTE_MILLIS = 60000L;



  public ZmanimCalculator(
    GeoLocation location,
    HebrewDate  date
  ) {
    super( location );
    date.syncCalendar( getCalendar() );

    setAstronomicalCalculator( new NOAACalculator() );
  }



  /**
 	 * This method returns the latest <em>zman krias shema</em> (time to recite Shema in the morning) based on the
 	 * opinion that the day starts at <em>alos 16.01&deg;</em> and ends at
   * <em>tzait 3.8&deg;</em> after sunset. 3 shaos zmaniyos are calculated based on this day and added to
 	 * alos to reach this time.
 	 * <b>Note: </b> Based on this calculation <em>chatzos</em> will not be at midday.
 	 *
 	 * @return the <code>Date</code> of the latest <em>zman krias shema</em> based on this day. If the calculation can't
 	 *         be computed such as northern and southern locations even south of the Arctic Circle and north of the
 	 *         Antarctic Circle where the sun may not reach low enough below the horizon for this calculation, a null
 	 *         will be returned. See detailed explanation on top of the {@link AstronomicalCalendar} documentation.
 	 * @see #getAlos16Point1Degrees()
 	 * @see #getTzaisGeonim3Point65Degrees()
 	 */
 	public Date getSofZmanShmaAlos16Point01ToTzaisGeonim3Point8Degrees() {
 		return getSofZmanShma( getSunriseOffsetByDegrees( GEOMETRIC_ZENITH + 16.01 ),
      getSunsetOffsetByDegrees( GEOMETRIC_ZENITH + 3.8 ) );
 	}


  /**
   * Method to return the latest time for mincha based on Daily Halacha's representation of the Mishna Berura's ruling,
   *  that mincha must be FINISHED by sunset.
   *
   * @return the <code>Date</code> representing the time.
   */
  public Date getLatestMinchaMishnaBerura() {
    return getSeaLevelSunset();
  }

  /**
   * Method to return the latest time for mincha based on Daily Halacha's representation of Chacham Ovadia Yosef's
   *  ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5 shaot zmaniot minutes after sunset.
   *
   * @return the <code>Date</code> representing the time.
   */
  public Date getLatestMinchaOvadiaYosef() {
    // First, we have to figure out how long a shaot zmaniot minute is. The returned temporal hour is represented by
    //  the number of milliseconds in that "hour".
    long shaotZmaniotHour = getTemporalHour();
    long shaotZmaniotMinute = shaotZmaniotHour / MINUTES_PER_HOUR;

    return getTimeOffset( getSeaLevelSunset(),
      ( MINUTES_AFTER_SUNSET_FOR_MINCHA_OVADIA_YOSEF * shaotZmaniotMinute ) + ( shaotZmaniotMinute / 2 ) );
  }


  /**
   * Method to return the latest time for mincha based on Daily Halacha's representation of Chacham Ben Sion Abba
   *  Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26 minutes after sunset. 
   *
   * @return the <code>Date</code> representing the time.
   */
  public Date getLatestMinchaBenSion() {
    return getTimeOffset( getSeaLevelSunset(), MINUTES_AFTER_SUNSET_FOR_MINCHA_BEN_SION_ABBA_SHAUL * MINUTE_MILLIS);
  }


  public Date getTzaitSeattleSephardic() {
    return getTimeOffset( getSeaLevelSunset(), MINUTES_AFTER_SUNSET_FOR_SEATTLE_SEPHARDIC_TZAIT * MINUTE_MILLIS );
  }


  /**
   * Based on Rav Moshe's fixed 50 minutes after sunset. This time is ONLY intended to be used in New York.
   *
   * TODO: We should simply allow users to create their own times rather than coding all of these...
   */
  public Date getTzaitRavMoshe() {
    return getTimeOffset( getSeaLevelSunset(), MINUTES_AFTER_SUNSET_RAV_MOSHE * MINUTE_MILLIS );
  }


  /**
   * This method returns the time of <em>plag hamincha</em> based on the opinion that the day starts at
 	 * <em>{@link #getAlos16Point1Degrees() alos 16.1&deg;}</em> and ends at
 	 * <em>{@link #getTzais()}  tzais 8.5&deg;}</em>.
 	 *
 	 * @return the <code>Date</code> of the time of <em>plag hamincha</em>. If the calculation can't be computed such as
 	 *         northern and southern locations even south of the Arctic Circle and north of the Antarctic Circle where
 	 *         the sun may not reach low enough below the horizon for this calculation, a null will be returned. See
 	 *         detailed explanation on top of the {@link AstronomicalCalendar} documentation.
 	 *
   */
  public Date getPlagAlot16Point1ToTzait8Point5() {
    return getPlagHamincha( getAlos16Point1Degrees(), getTzais() );
  }


  public Date getEndOfFast( int minutesAfterShkia ) {
    return getTimeOffset( getSeaLevelSunset(), minutesAfterShkia * MINUTE_MILLIS );
  }


  /**
   * Calculates candle lighting times. Used rather than setting the offset and asking for the candle lighting
   *  in the base class because that won't be thread safe.
   *
   * @param minutesBeforeShkia How many minutes prior to Shkia should candle lighting be calculated?
   * @return the <code>Date</code> representing candle lighting.
   */
  public Date getCandleLighting( int minutesBeforeShkia ) {
    return getTimeOffset(getSeaLevelSunset(), -minutesBeforeShkia * MINUTE_MILLIS);
  }

  /**
   * Method to calculate the middle of the night as defined by the midpoint between sunset and sunrise.
   *
   * @return the <code>Date</code> representing the middle of the night. Will return null if either the
   *  sunset or sunrise cannot be calculated.
   */
  public Date getNightChatzos() {

    ZmanimCalculator clonedCal = (ZmanimCalculator)clone();
    clonedCal.getCalendar().add( Calendar.DAY_OF_MONTH, 1);
    Date sunset = getSeaLevelSunset();
    Date sunrise = clonedCal.getSeaLevelSunrise();
    if ( ( null == sunset ) || ( null == sunrise ) ) {
      return null;
    }

    return getTimeOffset( sunset, ( sunrise.getTime() - sunset.getTime() ) / 2 );
  }


  /**
   * Calculates the middle of the night as a 12 hours after the fixed local chatzot.
   *
   * @return the Date representing the local middle of the night.
   * @see #getFixedLocalChatzos()
   */
  public Date getFixedLocalNightChatzos() {
    Date localFixedChatzot = getFixedLocalChatzos();
    long midnight = localFixedChatzot.getTime() + ( 12 * MINUTES_PER_HOUR * MINUTE_MILLIS );

    return new Date( midnight );
  }


  public Date getNightChatzos6HoursAfterTzait() {
    // todo: it seems the only way to calculate this is to know how the user has defined Tzeit!
    return null;
  }

}  // End of ZmanimCalculator class
