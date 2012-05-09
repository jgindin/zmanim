/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

/**
 * In the Hebrew calendar, the six single year lengths give rise to the terms haser, qesidrah, and shalem.
 *
 * Haser is the term applied to Hebrew years that are is either 353 or 383 days long. Such year
 *  lengths are accommodated by removing the 30th day of the month of Kislev. In English, the
 *  term haser is very often translated either as deficient or imperfect. The Hebrew letter het
 *  is used to denote either one of these year lengths.
 *
 * Qesidrah is the term applied to Hebrew years that are is either 354 or 384 days long. Such
 *  year lengths are accommodated by keeping all of their month's lengths intact. In English,
 *  the term qesidrah is very often translated either as regular or intermediate. The Hebrew
 *  letter chof is used to denote either one of these year lengths.
 *
 * Shalem is the term applied to Hebrew years that are is either 355 or 385 days long. Such year
 *  lengths are accommodated by adding a 30th day to the month of Heshvan. In English, the term
 *  shalem is very often translated either as abundant or perfect. The Hebrew letter shiyyen is
 *  used to denote either one of these year lengths.
 *<p>
 * (Taken from http://www.geocities.com/Athens/1584/, which is unfortunately no longer available
 *  except via the Way Back Machine.)
 *<p>
 * Putting this together, we get the following table:
 *    Month       Gregorian Equivalent  Haser Length    Qesidrah Length   Shalem Length
 *    =======     ====================  =============   ================  =============
 *    1.  Nissan 	  March-April           30 days         30 days           30 days
 *    2.  Iyar 	    April-May             29 days 	      29 days 	        29 days
 *    3.  Sivan 	  May-June              30 days 	      30 days 	        30 days
 *    4.  Tammuz 	  June-July             29 days 	      29 days 	        29 days
 *    5.  Av        July-August           30 days 	      30 days 	        30 days
 *    6.  Elul 	    August-September      29 days 	      29 days 	        29 days
 *    7.  Tishri 	  September-October     30 days         30 days           30 days
 *    8.  Heshvan   October-November      29 days 	      29 days 	        30 days
 *    9.  Kislev 	  November-December     29 days 	      30 days 	        30 days
 *    10. Tevet 	  December-January      29 days         29 days           29 days
 *    11. Shevat 	  January-February      30 days 	      30 days 	        30 days
 *    12. Adar      February-March        29 days 	      29 days 	        29 days
 *    12. Adar I    February-March        30 days 	      30 days 	        30 days
 *    13. Adar II   March-April           29 days         29 days           29 days
 *                                    ----------      ----------        ---------
 *                                      353 / 383         354/384           355/385
 *<p>
 * (Partially taken from http://www.jewishvirtuallibrary.org/jsource/Judaism/calendar.html.)
 */

public enum HebrewYearType {

	/**
	 * A short year where both {@link HebrewMonth#CHESHVAN} and {@link HebrewMonth#KISLEV} are 29 days.
	 */
	HASER,

	/**
	 * An ordered year where {@link HebrewMonth#CHESHVAN} is 29 days and {@link HebrewMonth#KISLEV} is 30 days.
	 */
	QESIDRAH,

	/**
	 * A long year where both {@link HebrewMonth#CHESHVAN} and {@link HebrewMonth#KISLEV} are 30 days.
	 */
	SHALEM

}
