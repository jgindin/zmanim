/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;
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

}  // End of ZmanimCalendar class
