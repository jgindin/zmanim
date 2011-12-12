/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Calendar;
import java.util.Date;

/**
 * Adds support for earliest time for Mincha.
 */
public class ZmanimCalendar
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
	 * In Seattle, the Sephardic community which descends from the Island of Rhodes considers Shabbat to be
	 *  out 45 minutes after sunset.
	 */
	private static final int                                  MINUTES_AFTER_SUNSET_FOR_EB_TZAIT = 45;

	/**
	 * In Seattle, the Sephardic community which descends from the Turkey considers Shabbat to be
	 *  out 42 minutes after sunset.
	 */
	private static final int                                  MINUTES_AFTER_SUNSET_FOR_SBH_TZAIT = 42;


	/**
	 * How many milliseconds in a minute?
	 *<p>
	 * Have to copy this since it is package-local in the base class.
	 */
	private static final long                                 MINUTE_MILLIS = 60000L;



	public ZmanimCalendar( GeoLocation location ) {
		super( location );
	}



	/**
	 * Method to return the latest time for mincha based on Daily Halacha's representation of the Mishna Berura's ruling,
	 *  that mincha must be FINISHED by sunset.
	 *
	 * @return the <code>Date</code> representing the time.
	 */
	public Date getLatestMinchaMishnaBerura() {
		return getSunset();
	}

	/**
	 * Method to return the latest time for mincha based on Daily Halacha's representation of Chacham Ovadia Yosef's
	 *  ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5 minutes after sunset.
	 *
	 * @return the <code>Date</code> representing the time.
	 */
	public Date getLatestMinchaOvadiaYosef() {
		return getTimeOffset( getSunset(),
			( MINUTES_AFTER_SUNSET_FOR_MINCHA_OVADIA_YOSEF * MINUTE_MILLIS ) + ( MINUTE_MILLIS / 2 ) );
	}


	/**
	 * Method to return the latest time for mincha based on Daily Halacha's representation of Chacham Ben Sion Abba
	 *  Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26 minutes after sunset. 
	 *
	 * @return the <code>Date</code> representing the time.
	 */
	public Date getLatestMinchaBenSion() {
		return getTimeOffset( getSunset(), MINUTES_AFTER_SUNSET_FOR_MINCHA_BEN_SION_ABBA_SHAUL * MINUTE_MILLIS);
	}


	public Date getTzaitEzraBessaroth() {
		return getTimeOffset( getSeaLevelSunset(), MINUTES_AFTER_SUNSET_FOR_EB_TZAIT * MINUTE_MILLIS );
	}


	public Date getTzaitSephardicBikurHolim() {
		return getTimeOffset( getSeaLevelSunset(), MINUTES_AFTER_SUNSET_FOR_SBH_TZAIT * MINUTE_MILLIS );
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
		return getTimeOffset(getSunset(), - minutesBeforeShkia * MINUTE_MILLIS);
	}

	/**
	 * Method to calculate the middle of the night as defined by the midpoint between sunset and sunrise.
	 *
	 * @return the <code>Date</code> representing the middle of the night. Will return null if either the
	 *  sunset or sunrise cannot be calculated.
	 */
	public Date getNightChatzos() {

		ZmanimCalendar clonedCal = (ZmanimCalendar)clone();
		clonedCal.getCalendar().add( Calendar.DAY_OF_MONTH, 1);
		Date sunset = getSunset();
		Date sunrise = clonedCal.getSunrise();
		if ( ( null == sunset ) || ( null == sunrise ) ) {
			return null;
		}

		return getTimeOffset( sunset, ( sunrise.getTime() - sunset.getTime() ) / 2 );
	}

}  // End of ZmanimCalendar class
