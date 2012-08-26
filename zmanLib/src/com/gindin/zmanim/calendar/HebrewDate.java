/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Holidays;
import com.gindin.zmanim.calendar.holiday.Observance;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.Zmanim;
import com.gindin.zmanim.zman.ZmanimFactory;

import java.util.*;

/**
 * A HebrewDate represents a specific date obtained from a HebrewCalendar. Each instance is immutable once created.
 */
@SuppressWarnings( "UnusedDeclaration" )
public class HebrewDate
	implements Comparable<HebrewDate> {

	private int                                               hebrewYear;
	@HebrewMonth.Constant
	private int                                               hebrewMonth;
	private int                                               hebrewDay;

	private int                                               moladHours;
	private int                                               moladMinutes;
	private int                                               moladChalakim;

	/** The Gregorian year */
	private int                                               gregorianYear;

	/**
	 * The month, in a format compatible with java.util.Calendar's definition of month zero being January, through to
	 *  month 11 being December.
	 */
	@Constants.CalendarMonth
	private int                                               gregorianMonth;

	/** The day of the Gregorian month */
	private int                                               gregorianDayOfMonth;

	/** 1 == Sunday, 2 == Monday, etc... */
	@Constants.CalendarDay
	private int                                               dayOfWeek;

	/** Internal representation of the date. */
	private int                                               gregorianAbsDate;

	/** Cache the details lookup. */
	private Details                                           dateDetails;

	/** Cache the zmanim which are applicable to today. */
	private Map<ZmanimLocation, List<Zmanim>>                 locationToZmanim;


	HebrewDate(
		int hebrewYear,
		@HebrewMonth.Constant int hebrewMonth,
		int hebrewDay,
		int gregorianYear,
		@Constants.CalendarMonth int gregorianMonth,
		int gregorianDayOfMonth,
		@Constants.CalendarDay int dayOfWeek,
		int gregorianAbsDate
	) {
		this( hebrewYear, hebrewMonth, hebrewDay, gregorianYear, gregorianMonth, gregorianDayOfMonth, dayOfWeek, gregorianAbsDate, 0, 0, 0 );
	}


	HebrewDate(
		int hebrewYear,
		@HebrewMonth.Constant int hebrewMonth,
		int hebrewDay,
		int gregorianYear,
		@Constants.CalendarMonth int gregorianMonth,
		int gregorianDayOfMonth,
		@Constants.CalendarDay int dayOfWeek,
		int gregorianAbsDate,
		int moladHours,
		int moladMinutes,
		int moladChalakim
	) {

		this.hebrewYear = hebrewYear;
		this.hebrewMonth = hebrewMonth;
		this.hebrewDay = hebrewDay;
		this.gregorianYear = gregorianYear;
		this.gregorianMonth = gregorianMonth;
		this.gregorianDayOfMonth = gregorianDayOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.gregorianAbsDate = gregorianAbsDate;
		this.moladHours = moladHours;
		this.moladMinutes = moladMinutes;
		this.moladChalakim = moladChalakim;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Find out about this date
	@SuppressWarnings( "FeatureEnvy" )
	public Details getDetails() {

		if ( null != dateDetails ) {
			return dateDetails;
		}

		// Create a NullDetails here...it avoids a bunch of null checking...
		dateDetails = new NullDetails( this );

		Details shabbat = Holidays.SHABBAT.getDetailsFor( this );
		if ( null != shabbat ) {
			dateDetails = dateDetails.composeWith( shabbat );
		}

		Details roshChodeshDetails = Holidays.ROSH_CHODESH.getDetailsFor( this );
		if ( null != roshChodeshDetails ) {
			dateDetails = dateDetails.composeWith( roshChodeshDetails );
		}


		Details holidayDetails;
		for ( Observance fastDay : Holidays.FAST_DAYS ) {
			holidayDetails = fastDay.getDetailsFor( this );
			if ( null != holidayDetails ) {
				dateDetails = dateDetails.composeWith( holidayDetails );
			}
		}

		for ( Observance holiday : Holidays.MAJOR_HOLIDAYS ) {
			holidayDetails = holiday.getDetailsFor( this );
			if ( null != holidayDetails ) {
				dateDetails = dateDetails.composeWith( holidayDetails );
			}
		}

		for ( Observance holiday : Holidays.MINOR_HOLIDAYS ) {
			holidayDetails = holiday.getDetailsFor( this );
			if ( null != holidayDetails ) {
				dateDetails = dateDetails.composeWith( holidayDetails );
			}
		}

		for ( Observance modernHoliday : Holidays.MODERN_HOLIDAYS ) {
			holidayDetails = modernHoliday.getDetailsFor( this );
			if ( null != holidayDetails ) {
				dateDetails = dateDetails.composeWith( holidayDetails );
			}
		}

		Details omerDetails = Holidays.OMER.getDetailsFor( this );
		if ( null != omerDetails ) {
			if ( null == dateDetails ) {
				dateDetails = omerDetails;
			}
			else {
				dateDetails = dateDetails.composeWith( omerDetails );
			}
		}


		return dateDetails;
	}


	public Zmanim getZmanim(
		ZmanimLocation  location,
		Zmanim.Type     type
	) {

		return ZmanimFactory.getInstance( location ).getOrCreate( type );
	}


	@SuppressWarnings( "FeatureEnvy" )
	public List<Zmanim> getZmanim(
		ZmanimLocation location
	) {

		if ( null == locationToZmanim ) {
			locationToZmanim = new HashMap<ZmanimLocation, List<Zmanim>>();
		}

		List<Zmanim> zmanimList = locationToZmanim.get( location );
		if ( null != zmanimList ) {
			return zmanimList;
		}

		Details details = getDetails();
		zmanimList = new ArrayList<Zmanim>();

		// Get all the possible zmanim, then weed out the ones that don't apply for today.
		zmanimList.add( getZmanim( location, Zmanim.Type.Alot ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.Talit ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.Hanetz ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.SofZmanShema ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.SofZmanTefila ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.Chatzot ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.MinchaGedola ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.MinchaKetana ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.PlagHaMincha ) );

		if ( details.lightCandles() ) {
			zmanimList.add( getZmanim( location, Zmanim.Type.CandleLighting ) );
		}

		zmanimList.add( getZmanim( location, Zmanim.Type.Shkia ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.LatestMincha ) );
		zmanimList.add( getZmanim( location, Zmanim.Type.BeinHashmashot ) );

		if ( details.isTaanit() ) {
			zmanimList.add( getZmanim( location, Zmanim.Type.FastEnd ) );
		}

		zmanimList.add( getZmanim( location, Zmanim.Type.TzaitHacochavim ) );

		if ( details.sayHavdalah() ) {
			zmanimList.add( getZmanim( location, Zmanim.Type.ShabbatEnd ) );
		}

		zmanimList.add( getZmanim( location, Zmanim.Type.NightChatzot ) );

		return zmanimList;
	}


	public Zman getZman(
		ZmanimLocation  location,
		Zmanim.Type     type
	) {

		Zmanim zmanim = getZmanim( location, type );
		return zmanim.getDefault();
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Queries about this date.

	/**
	 * @return the Hebrew year (e.g., 5772).
	 */
	public int getHebrewYear() {
		return hebrewYear;
	}


	/**
	 * @return  the Hebrew month 1-12 (or 13 years in a leap year). The month count starts with 1 for Nisan and goes to 13
	 * for Adar II
	 */
	@HebrewMonth.Constant
	public int getHebrewMonth() {
		return hebrewMonth;
	}


	/**
	 * @return the Hebrew day of month.
	 */
	public int getHebrewDayOfMonth() {
		return hebrewDay;
	}


	/**
	 * @return the day of the week as a number between 1-7. Strictly compatible with Calendar.SUNDAY, etc...
	 */
	@Constants.CalendarDay
	public int getDayOfWeek() {
		return dayOfWeek;
	}


	/**
	 * @return the gregorian year (e.g., 2012).
	 */
	public int getGregorianYear() {
		return gregorianYear;
	}


	/**
	 * @return A month which can be directly compared to the months as defined in java.util.Calendar.
	 */
	@Constants.CalendarMonth
	public int getGregorianMonth() {
		return gregorianMonth;
	}


	public int getGregorianDayOfMonth() {
		return gregorianDayOfMonth;
	}


	int getGregorianAbsDate() {
		return gregorianAbsDate;
	}


	/**
	 * Returns this object's date as a java.util.Date object.
	 *
	 * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
	 *
	 * @return The <code>Date</code>
	 */
	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( System.currentTimeMillis() );
		cal.set( gregorianYear, gregorianMonth, gregorianDayOfMonth );
		return cal.getTime();
	}


	/**
	 * Returns if the year the calendar is set to is a Hebrew leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year
	 * cycle are leap years.
	 *
	 * @return true if it is a leap year
	 */
	public boolean isHebrewLeapYear() {
		return HebrewCalendar.isHebrewLeapYear( getHebrewYear() );
	}

	
	/**
	 * Returns the number of days for the current year that the calendar is set to.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 * @see #isHebrewLeapYear
	 */
	public int getDaysInHebrewYear() {
		return HebrewCalendar.getElapsedDays( getHebrewYear() + 1 ) - HebrewCalendar.getElapsedDays( getHebrewYear() );
	}


	/**
	 * @return true if Cheshvan is long (29 or 30 days) for the current year that the calendar is set to
	 */
	public boolean isCheshvanLong() {
		return HebrewCalendar.isCheshvanLong( getHebrewYear() );
	}


	/**
	 * @return true if Kislev is short (29 or 30 days) for the year that this class is set to
	 */
	public boolean isKislevShort() {
		return HebrewCalendar.isKislevShort( getHebrewYear() );
	}


	/**
	 * Returns the Cheshvan and Kislev kviah (whether a Hebrew year is short, regular or long). It will return {@link
	 * HebrewYearType#SHALEM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#QESIDRAH} if Cheshvan is 29
	 * days and Kislev is 30 days and {@link HebrewYearType#HASER} if both are 29 days.
	 *
	 * @return {@link HebrewYearType#SHALEM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#QESIDRAH} if
	 *         Cheshvan is 29 days and Kislev is 30 days and {@link HebrewYearType#HASER} if both are 29 days.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 */
	public HebrewYearType getYearType() {
		return HebrewCalendar.getYearType( getHebrewYear() );
	}


	/**
	 * @return the number of days for a given Heberw month that the calendar is currently set to.
	 */
	public int getDaysInHebrewMonth() {
		return HebrewCalendar.getDaysInHebrewMonth( hebrewMonth, hebrewYear );
	}
	

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Molad info

	/**
	 * Returns the molad for the year and month of the specified HebrewDate. Returns a HebrewDate set to the date of
	 * the molad with the molad fields properly set.
	 *
	 * In the current implementation, it sets the molad time based on a midnight date rollover. This means that Rosh
	 * Chodesh Adar II, 5771 with a molad of 7 chalakim past midnight on Shabbos 29 Adar I / March 5, 2011 12:00 AM and 7
	 * chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
	 */
	public HebrewDate getMolad() {

		HebrewDate moladDate = HebrewCalendar.getForMolad( HebrewCalendar.getChalakimSinceMoladTohu( hebrewYear, hebrewMonth ) );

		if ( moladDate.getMoladHours() >= 6 ) {
			moladDate = moladDate.tomorrow();
		}
		
		moladDate.moladHours = ( moladDate.getMoladHours() + HebrewCalendar.CHALAKIM_PER_MINUTE ) % HebrewCalendar.HOURS_PER_DAY;
		return moladDate;
	}

	
	
	/**
	 * Returns the molad hours. Only a HebrewDate created via the getMolad() method will have this field set. Other
	 *  HeberwDates will have this field set to 0.
	 *
	 * @return the molad hours
	 */
	public int getMoladHours() {
		return moladHours;
	}


	/**
	 * Returns the molad minutes. Only a HebrewDate created via the getMolad() method will have this field set. Other
	 *  HebrewDates will have this field set to 0.
	 *
	 * @return the molad minutes
	 */
	public int getMoladMinutes() {
		return moladMinutes;
	}


	/**
	 * Returns the molad chalakim/parts. Only a HebrewDate created via the getMolad() method will have this field set. Other
	 *  HebrewDates will have this field set to 0.
	 *
	 * @return the molad chalakim/parts
	 */
	public int getMoladChalakim() {
		return moladChalakim;
	}

	
	/**
	 * Returns the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu to the year and
	 * month passed in.
	 *
	 * @return the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu
	 */
	public long getChalakimSinceMoladTohu() {
		return HebrewCalendar.getChalakimSinceMoladTohu( hebrewYear, hebrewMonth );
	}


	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Date navigation

	public HebrewDate tomorrow() {
		return HebrewCalendar.forward( this );
	}

	public HebrewDate forward( int numDays ) {
		return HebrewCalendar.forward( this, numDays );
	}
	
	public HebrewDate yesterday() {
		return HebrewCalendar.back( this );
	}

	public HebrewDate back( int numDays ) {
		return HebrewCalendar.back( this, numDays );
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Base class overrides
	
	@Override
	public int compareTo( HebrewDate otherDate ) {

		// We use gregorian because with the Hebrew months, because Nisan is the first month, it gets more complex.
		//  E.g., Nisan 1, 5772 is AFTER Adar 29, 5772, but in the HebrewMonth definitions, NISAN < ADAR...

		if ( gregorianYear < otherDate.gregorianYear ) {
			return -1;
		}
		else if ( gregorianYear > otherDate.gregorianYear ) {
			return 1;
		}
		
		if ( gregorianMonth < otherDate.gregorianMonth ) {
			return -1;
		}
		else if ( gregorianMonth > otherDate.gregorianMonth ) {
			return 1;
		}
		
		if ( gregorianDayOfMonth < otherDate.gregorianDayOfMonth ) {
			return -1;
		}
		else if ( gregorianDayOfMonth > otherDate.gregorianDayOfMonth ) {
			return 1;
		}
		
		return 0;
	}


	@SuppressWarnings( "RedundantIfStatement" )
	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof HebrewDate ) ) {
			return false;
		}

		HebrewDate that = (HebrewDate)o;

		if ( hebrewDay != that.hebrewDay ) {
			return false;
		}
		if ( hebrewMonth != that.hebrewMonth ) {
			return false;
		}
		if ( hebrewYear != that.hebrewYear ) {
			return false;
		}

		return true;
	}


	@Override
	public int hashCode() {
		int result = hebrewMonth;
		result = 31 * result + hebrewYear;
		result = 31 * result + hebrewDay;
		return result;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//

	private static class NullDetails
		extends Details {

		NullDetails( HebrewDate date ) {
			super( date );
		}

		@Override
		public boolean isErev() {
			return false;
		}

		@Override
		public boolean lightCandles() {
			return false;
		}

		@Override
		public boolean sayHavdalah() {
			return false;
		}

		@Override
		public String getDescription() {
			return "";
		}


		@Override
		public boolean isAnything() {
			return false;
		}
	}

}  // End of HebrewDate class
