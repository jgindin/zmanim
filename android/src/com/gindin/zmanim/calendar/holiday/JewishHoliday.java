/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;


import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Represents a Jewish holiday. By 'holiday', I don't mean just Yom Tov...rather it's any and
 *  all Jewish observances.
 *<p>
 * For testing purposes, see http://en.wikipedia.org/wiki/Jewish_holidays_2000-2050.
 */
@SuppressWarnings( { "MagicNumber" } )
public abstract class JewishHoliday {


	/** Have one instance we use for checking. */
	protected static final HebrewCalendar                     calendar = new HebrewCalendar();


	static {
		// Set it so that the hours, minutes, and seconds won't matter.
		calendar.set( 2009, 10, 21, 0, 0, 0 );
	}

	
	private final String                                      name;
	
	protected final int                                       month;
	protected final int                                       date;


	public JewishHoliday(
		int     month,
		int     date,
	  String  name
	) {
		this.month = month;
		this.date = date;
		this.name = name;
	}


	/**
	 * @return the name of this holiday.
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return a short description of the holiday; usually just the name, but could be different (e.g., "Hanukkah 3").
	 */
	public String getDescription() {
		return name;
	}


	/**
	 * Is this holiday on the specified date?
	 *
	 * @param dateToTest what date are we being asked about?
	 * @return true if this holiday falls on the specified date; false otherwise.
	 */
	public JewishHoliday isOn(
		HebrewCalendar dateToTest
	) {

		if ( null == dateToTest ) {
			return null;
		}

		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
		if ( isSameDate( dateToTest, calendar ) ) {
			return createForDate( dateToTest );
		}

		return null;
	}


	/**
	 * Does this holiday start tonight?
	 *
	 * @param dateToTest what date are we being asked about?
	 * @return true if this holiday starts on the evening of the specified date; false otherwise.
	 */
	public JewishHoliday isErev(
		HebrewCalendar dateToTest
	) {

		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
		calendar.add( HebrewCalendar.DATE, -1 );

		if ( isSameDate( dateToTest, calendar ) ) {
			return createErevOf();
		}

		return null;
	}


	/**
	 * Does this holiday require candles on the specified date?
	 *
	 * @return true if candles are lit for this holiday; false otherwise
	 */
	public abstract boolean showCandlelighting();


	/**
	 * Does the end of this holiday require havdalah?
	 *
	 * @return true if havdalah is observed; false otherwise
	 */
	public abstract boolean showHavdalah();


	/**
	 * @param hebrewCalendar The date we want a 'version' of this holiday for.
	 *
	 * @return a version of this holiday, specific for the specified date.
	 *  NOTE: Many implementations will just return themselves, as there is nothing special about them that changes
	 *    based on the date.
	 */
	protected JewishHoliday createForDate(
		HebrewCalendar hebrewCalendar
	) {

		// Nothing about this is dependent upon the date...
		return this;
	}


	/**
	 * @return a "holiday" to represent the day before this holiday.
	 */
	protected abstract JewishHoliday createErevOf();


	@SuppressWarnings( { "FeatureEnvy", "RedundantIfStatement" } )
	protected static boolean isSameDate(
		HebrewCalendar cal1,
	  HebrewCalendar cal2
	) {

		if ( cal1.get( HebrewCalendar.YEAR ) != cal2.get( HebrewCalendar.YEAR ) ) {
			return false;
		}

		if ( cal1.get( HebrewCalendar.MONTH ) != cal2.get( HebrewCalendar.MONTH ) ) {
			return false;
		}

		if ( cal1.get( HebrewCalendar.DAY_OF_MONTH ) != cal2.get( HebrewCalendar.DAY_OF_MONTH ) ) {
			return false;
		}

		return true;
	}


}  // End of JewishHoliday class
