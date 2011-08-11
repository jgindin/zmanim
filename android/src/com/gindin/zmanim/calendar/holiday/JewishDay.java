/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;


import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Represents a day in the Jewish calendar.
 *<p>
 * This class IS NOT THREAD SAFE!!!
 *<p>
 * For testing purposes, see http://en.wikipedia.org/wiki/Jewish_holidays_2000-2050.
 */
@SuppressWarnings( { "MagicNumber", "ClassReferencesSubclass" } )
public abstract class JewishDay {


	/**
	 * Have one instance we use for checking.
	 *<p>
	 * NOTE: This limits the usages of this class (and subclasses) such that it is NOT thread-safe!!!
	 */
	protected static final HebrewCalendar                     calendar = new HebrewCalendar();


	static {
		// Set it so that the hours, minutes, and seconds won't matter.
		calendar.set( 2009, 10, 21, 0, 0, 0 );
	}

	
	private final String                                      name;
	
	protected final int                                       month;
	protected final int                                       date;

	/** What date was this holiday specifically constructed for? Maybe null to indicate a generic instance. */
	protected final HebrewCalendar                            forDate;


	protected JewishDay(
		int     month,
		int     date,
		String  name
	) {
		this( month, date, name, null );
	}

	protected JewishDay(
		int             month,
		int             date,
		String          name,
		HebrewCalendar  forDate
	) {
		this.month = month;
		this.date = date;
		this.name = name;
		this.forDate = forDate;
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
	public JewishDay isOn(
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
	 * Is today erev something?
	 *
	 * @return The erev if it is, null otherwise.
	 */
	public Erev isErev() {
		return null;
	}


	/**
	 * Is today part of a multi-day observance?
	 *
	 * @return the multi-day observance if today is; null otherwise.
	 */
	public MultiDayHoliday isMultiDay() {
		return null;
	}


	/**
	 * Is today a Rosh Chodesh?
	 *
	 * @return null if today is not a rosh chodesh.
	 */
	public RoshChodesh isRoshChodesh() {
		return null;
	}


	public boolean isTaanit() {
		return false;
	}


	/**
	 * Does this holiday start tonight?
	 *
	 * @param dateToTest what date are we being asked about?
	 * @return A JewishDay to represent the Erev of the upcoming holiday, or null if not an erev anything.
	 */
	public JewishDay isErev(
		HebrewCalendar dateToTest
	) {

		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
		calendar.add( HebrewCalendar.DATE, -1 );

		if ( isSameDate( dateToTest, calendar ) ) {
			return createErevOf( dateToTest );
		}

		return null;
	}


	/**
	 * Does this holiday require candles on the specified date?
	 *
	 * @return true if candles are lit for this holiday; false otherwise
	 */
	public boolean showCandlelighting() {
		return isShabbat();
	}


	/**
	 * Does the end of this holiday require havdalah?
	 *
	 * @return true if havdalah is observed; false otherwise
	 */
	public boolean showHavdalah() {
		return isMotzaeShabbat();
	}


	/**
	 * @param forDate The date we want a 'version' of this holiday for.
	 *
	 * @return a version of this holiday, specific for the specified date.
	 *  NOTE: Many implementations will just return themselves, as there is nothing special about them that changes
	 *    based on the date.
	 */
	protected abstract JewishDay createForDate( HebrewCalendar forDate );


	/**
	 * @param forDate The date we want a 'version' of this erev holiday for.
	 * @return a "holiday" to represent the day before this holiday.
	 */
	protected abstract JewishDay createErevOf( HebrewCalendar forDate );


	protected final boolean isShabbat() {
		//noinspection SimplifiableIfStatement
		if ( null == forDate ) {
			return false;
		}

		return ( HebrewCalendar.FRIDAY == forDate.get( HebrewCalendar.DAY_OF_WEEK ) );
	}


	protected final boolean isMotzaeShabbat() {
		//noinspection SimplifiableIfStatement
		if ( null == forDate ) {
			return false;
		}

		return ( HebrewCalendar.SATURDAY == forDate.get( HebrewCalendar.DAY_OF_WEEK ) );
	}


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

}  // End of JewishDay class
