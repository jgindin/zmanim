/*
 * Copyright (c) 2011. Jay R. Gindin
 */
package net.sourceforge.zmanim.hebrewcalendar;

import java.util.*;

/**
 * The HebrewDate class allows one to maintain an instance of a Gregorian date
 * along with the corresponding Hebrew date. This class can use the standard
 * Java Date and Calendar classes for setting it, but does not subclass these
 * classes or use them internally to any extensive use. This class also does not
 * have a concept of a time (which the Date class does). If you are looking for
 * a class that implements a Hebrew calendar version of the Calendar class, one
 * is available from <a
 * href="http://oss.software.ibm.com/developerworks/opensource/icu4j/"
 * >developerWorks</a> by IBM.
 * 
 * This open source Java code was written by <a
 * href="http://www.facebook.com/avromf">Avrom Finkelstien</a> from his C++
 * code. it was adapted to the KosherJava Zmanim API with simplification of the
 * non-core code. The original algorithms were untouched.
 * 
 * Some of Avrom's original C++ code was translated or taken from other C/C++
 * code in <a href="http://www.calendarists.com">Calendrical Calculations</a> by
 * Nachum Dershowitz and Edward M. Reingold, Software-- Practice & Experience,
 * vol. 20, no. 9 (September, 1990), pp. 899- 928. Any method with the mark
 * "ND+ER" indicates that the method was taken from this source with minor
 * modifications.
 * 
 * Available at <a href="http://www.calendarists.com">www.calendarists.com</a>
 * (<a href="http://emr.cs.uiuc.edu/~reingold/calendar.C">Original C++
 * source</a>).
 * 
 * The methods used to obtain the parsha were derived from the source code of <a
 * href="http://www.sadinoff.com/hebcal/">HebCal</a> by Danny Sadinoff and JCal
 * for the Mac by Frank Yellin. Both based their code on routines by Nachum
 * Dershowitz and Edward M. Reingold. The class allows setting whether the
 * parsha and holiday scheme follows the Israel scheme or outside Israel scheme.
 * 
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.1
 */
public class HebrewDate implements Comparable, Cloneable {
	private static final int HEBREW_EPOCH = -1373429;

	private int hebrewMonth;
	private int hebrewDay;
	private int hebrewYear;

	private int gregorianMonth;
	private int gregorianDayOfMonth;
	private int gregorianYear;

	private int dayOfWeek;

	private int gregorianAbsDate;

	/**
	 * Use the formatting class for formatting Ashkenazi VS Sephardi English
	 * transliteration.
	 */
	private static final String[] parshios = { "Bereshis", "Noach",
			"Lech Lecha", "Vayera", "Chayei Sara", "Toldos", "Vayetzei",
			"Vayishlach", "Vayeshev", "Miketz", "Vayigash", "Vayechi",
			"Shemos", "Vaera", "Bo", "Beshalach", "Yisro", "Mishpatim",
			"Terumah", "Tetzaveh", "Ki Sisa", "Vayakhel", "Pekudei", "Vayikra",
			"Tzav", "Shmini", "Tazria", "Metzora", "Achrei Mos", "Kedoshim",
			"Emor", "Behar", "Bechukosai", "Bamidbar", "Nasso", "Beha'aloscha",
			"Sh'lach", "Korach", "Chukas", "Balak", "Pinchas", "Matos",
			"Masei", "Devarim", "Vaeschanan", "Eikev", "Re'eh", "Shoftim",
			"Ki Seitzei", "Ki Savo", "Nitzavim", "Vayeilech", "Ha'Azinu",
			"Vayakhel Pekudei", "Tazria Metzora", "Achrei Mos Kedoshim",
			"Behar Bechukosai", "Chukas Balak", "Matos Masei",
			"Nitzavim Vayeilech" };
	private boolean inIsrael = false;

	/**
	 * Returns the number of days in a given month.
	 * ND+ER
	 */
	public int getLastDayOfGregorianMonth(int month) {
		switch (month) {
		case 2:
			if ((((gregorianYear % 4) == 0) && ((gregorianYear % 100) != 0))
					|| ((gregorianYear % 400) == 0)) {
				return 29;
			} else {
				return 28;
			}
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		default:
			return 31;
		}
	}

	/**
	 * Computes the Gregorian date from the absolute date.
	 * ND+ER
	 */
	private void absDateToDate() {
		// Search forward year by year from approximate year//
		gregorianYear = gregorianAbsDate / 366;
		while (gregorianAbsDate >= dateToAbsDate(1, 1, gregorianYear + 1)) {
			gregorianYear++;
		}
		// Search forward month by month from January//
		gregorianMonth = 1;
		while (gregorianAbsDate > dateToAbsDate(gregorianMonth,
				getLastDayOfGregorianMonth(gregorianMonth), gregorianYear)) {
			gregorianMonth++;
		}
		gregorianDayOfMonth = gregorianAbsDate
				- dateToAbsDate(gregorianMonth, 1, gregorianYear) + 1;
	}

	/**
	 * Returns the absolute date (days on the gregorian since Januray 1, 1)
	 */
	public int getAbsDate() {
		return gregorianAbsDate;
	}

	/**
	 * Computes the absolute date from the Gregorian date. Default is
	 * current date.
	 * ND+ER
	 */
	private int dateToAbsDate(int month, int date, int year) {

		for (int m = month - 1; m > 0; m--) {
			// days in prior months this year
			date = date + getLastDayOfGregorianMonth(m);
		}
		return (date // days this year //
				+ 365 * (year - 1) // days in previous years ignoring leap days
				+ (year - 1) / 4 // Julian leap days before this year...
				- (year - 1) / 100 // ...minus prior century years...
		+ (year - 1) / 400); // ...plus prior years divisible by 400
	}

	/**
	 * returns true if the year is an Hebrew leap year.
	 */
	public static boolean isHebrewLeapYear(int year) {
		if ((((7 * year) + 1) % 19) < 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * returns true if the current Hebrew year is a leap year.
	 */
	public boolean isHebrewLeapYear2() {
		return isHebrewLeapYear(hebrewYear);
	}

	/**
	 * returns the last month of a given Hebrew year. This would be 12 on a non
	 * leap-year or 13 on a leap-year.
	 */
	public static int getLastMonthOfHebrewYear(int year) {
		if (isHebrewLeapYear(year)) {
			return 13;
		} else {
			return 12;
		}
	}

	/**
	 * Number of days elapsed from the Sunday prior to the start of the
	 * Hebrew calendar to the mean conjunction of Tishri of Hebrew year.
	 * ND+ER
	 */
	private static int getHebrewCalendarElapsedDays(int year) {

		int monthsElapsed = (235 * ((year - 1) / 19)) // Months in complete
														// cycles so far
				+ (12 * ((year - 1) % 19)) // Regular months in this cycle
				+ (7 * ((year - 1) % 19) + 1) / 19; // Leap months this cycle
		int partsElapsed = 204 + 793 * (monthsElapsed % 1080);
		int hoursElapsed = 5 + 12 * monthsElapsed + 793
				* (monthsElapsed / 1080) + partsElapsed / 1080;
		int conjunctionDay = 1 + 29 * monthsElapsed + hoursElapsed / 24;
		int conjunctionParts = 1080 * (hoursElapsed % 24) + partsElapsed % 1080;
		int alternativeDay;
		if ((conjunctionParts >= 19440) // If new moon is at or after midday,
				|| (((conjunctionDay % 7) == 2) // ...or is on a Tuesday...
						&& (conjunctionParts >= 9924) // at 9 hours, 204 parts
														// or later...
				&& !isHebrewLeapYear(year)) // ...of a common year,
				|| (((conjunctionDay % 7) == 1) // ...or is on a Monday at...
						&& (conjunctionParts >= 16789) // 15 hours, 589 parts or later...
				&& (isHebrewLeapYear(year - 1)))) { // at the end of a leap year
			// Then postpone Rosh HaShanah one day//
			alternativeDay = conjunctionDay + 1;
		} else {
			alternativeDay = conjunctionDay;
		}
		if (((alternativeDay % 7) == 0)// If Rosh HaShanah would occur on Sunday,
				|| ((alternativeDay % 7) == 3) // or Wednesday,
				|| ((alternativeDay % 7) == 5)) { // or Friday
			// Then postpone it one (more) day
			return (1 + alternativeDay);
		} else {
			return alternativeDay;
		}
	}

	/**
	 * Returns the number of days in a Hebrew year.
	 * ND+ER
	 */
	private static int getDaysInHebrewYear(int year) {
		return ((getHebrewCalendarElapsedDays(year + 1)) - (getHebrewCalendarElapsedDays(year)));
	}

	/**
	 * Returns true if Cheshvan is long in Hebrew year.
	 * ND+ER
	 */
	public static boolean isCheshvanLong(int year) {
		if ((getDaysInHebrewYear(year) % 10) == 5) {
			return true;
		} else {
			return false;
		}
	}
//	public boolean isCheshvanLong() {
//		return isCheshvanLong(hebrewYear);
//	}


	/**
	 * Returns true if Kislev is short in Hebrew year.
	 * ND+ER
	 */
	public static boolean isKislevShort(int year) {
		if ((getDaysInHebrewYear(year) % 10) == 3) {
			return true;
		} else {
			return false;
		}
	}
//	public boolean isKislevShort2() {
//		return isKislevShort(hebrewYear);
//	}

	/**
	 * Returns last day of a Hebrew month for a given month and year.
	 */
	public static int getLastDayOfHebrewMonth(int month, int year) {
		if ((month == 2) || (month == 4) || (month == 6)
				|| ((month == 8) && !(isCheshvanLong(year)))
				|| ((month == 9) && isKislevShort(year)) || (month == 10)
				|| ((month == 12) && !(isHebrewLeapYear(year))) || (month == 13)) {
			return 29;
		} else {
			return 30;
		}
	}

	/**
	 * ND+ER Computes the Hebrew date from the absolute date.
	 */
	private void absDateToHebrewDate() {
		hebrewYear = (gregorianAbsDate + HEBREW_EPOCH) / 366; // Approximation
																// from
		// below.//
		// Search forward for year from the approximation.//
		while (gregorianAbsDate >= hebrewDateToAbsDate(7, 1, hebrewYear + 1)) {
			hebrewYear++;
		}
		// Search forward for month from either Tishri or Nisan.//
		if (gregorianAbsDate < hebrewDateToAbsDate(1, 1, hebrewYear)) {
			hebrewMonth = 7;// Start at Tishri //
		} else {
			hebrewMonth = 1;// Start at Nisan //
		}
		while (gregorianAbsDate > hebrewDateToAbsDate(hebrewMonth,
				getLastDayOfHebrewMonth(hebrewMonth, hebrewYear), hebrewYear)) {
			hebrewMonth++;
		}
		// Calculate the day by subtraction.//
		hebrewDay = gregorianAbsDate
				- hebrewDateToAbsDate(hebrewMonth, 1, hebrewYear) + 1;
	}

	/**
	 * ND+ER Computes the absolute date of Hebrew date. Default is current
	 * Hebrew date.
	 */
	private int hebrewDateToAbsDate(int month, int date, int year) {
		int m;

		// Before Tishri, so add days in prior months//
		if (month < 7) {
			// this year before and after Nisan.//
			for (m = 7; m <= getLastMonthOfHebrewYear(hebrewYear); m++) {
				date = date + getLastDayOfHebrewMonth(m, year);
			}
			for (m = 1; m < month; m++) {
				date = date + getLastDayOfHebrewMonth(m, year);
			}
		} else { // Add days in prior months this year//
			for (m = 7; m < month; m++) {
				date = date + getLastDayOfHebrewMonth(m, year);
			}
		}
		// Days in prior years + Days elapsed before absolute date 1
		return (date + getHebrewCalendarElapsedDays(year) + HEBREW_EPOCH);
	}

	/**
	 * Creates a Hebrew date based on gregorian date (month, day, year)
	 */
	public HebrewDate(int month, int date, int year) {
		setGregorianDate(month, date, year);
	}

	/**
	 * Creates a Hebrew date based on Gregorian date and whether in Israel
	 * (month, day, year, inIsrael)
	 */
	public HebrewDate(int month, int date, int year, boolean inIsrael) {
		this(month, date, year);
		setInIsrael(inIsrael);
	}

	/**
	 * Initializes a date based on the current system date.
	 */
	public HebrewDate() {
		resetDate();
	}

	/**
	 * Initializes a date based on java.util.Date object.
	 */
	public HebrewDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
	}

	/**
	 * Initializes a date based on java.util.Calendar object.
	 */
	public HebrewDate(Calendar cal) {
		setDate(cal);
	}

	/**
	 * Sets the date based on a java.util.Calendar object. Modifies the Hebrew
	 * date as well.
	 */
	public void setDate(Calendar cal) {
		gregorianMonth = cal.get(Calendar.MONTH) + 1;
		gregorianDayOfMonth = cal.get(Calendar.DATE);
		gregorianYear = cal.get(Calendar.YEAR);

		// init the hebrew date //
		gregorianAbsDate = dateToAbsDate(gregorianMonth, gregorianDayOfMonth,
				gregorianYear);
		absDateToHebrewDate();

		// set day of week //
		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;
	}

	/**
	 * Sets the Gregorian Date, and updates the Hebrew date accordingly
	 */
	public void setGregorianDate(int month, int day, int year) {
		// precondition should be 1->12 anyways, but just in case... //
		if (month > 12 || month < 0) {
			throw new IllegalArgumentException(
					"The Gregorian month has to be between 1 - 12. " + month
							+ " is invalid.");
		}
		if (day <= 0) {
			throw new IllegalArgumentException(
					"The day of month can't be less than 1. " + day
							+ " is invalid.");
		}

		// make sure date is a valid date for the given month, if not, set to
		// last day of month //
		if (day > getLastDayOfGregorianMonth(month)) {
			day = getLastDayOfGregorianMonth(month);
		}
		if (year < 0) {
			throw new IllegalArgumentException(
					"Years < 0 can't be claculated. " + year + " is invalid.");
		}
		// init month, date, year
		gregorianMonth = month;
		gregorianDayOfMonth = day;
		gregorianYear = year;

		// init the Hebrew date
		gregorianAbsDate = dateToAbsDate(gregorianMonth, gregorianDayOfMonth,
				gregorianYear);
		absDateToHebrewDate();

		// set day of week
		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;
	}

	/**
	 * Sets the Hebrew Date, and updates the Gregorian date accordingly. If the
	 * day of month is set to 30 for a month that only has 29 days, the day will
	 * be set as 29.
	 */
	public void setHebrewDate(int month, int day, int year) {
		if (month < 1 || month > getLastMonthOfHebrewYear(year)) {
			throw new IllegalArgumentException(
					"The Hebrew month has to be between 1 and 12 (or 13 on a leap year). "
							+ month + " is invalid for the year " + year + ".");
		}
		if (day < 1) {
			throw new IllegalArgumentException(
					"The Hebrew day of month can't be < 1.  " + day
							+ " is invalid.");
		}

		if (day > 30) {
			throw new IllegalArgumentException(
					"The Hebrew day of month can't be > 30.  " + day
							+ " is invalid.");
		}

		// if 30 is passed for a month that only has 29 days (for example by
		// rolling the month from a month that had 30 days to a month that only
		// has 29) set the date to 29th
		if (day > getLastDayOfHebrewMonth(month, year)) {
			day = getLastDayOfHebrewMonth(month, year);
		}

		if (year < 0) {
			throw new IllegalArgumentException(
					"A Hebrew years < 0 can't be set. " + year + " is invalid.");
		}

		hebrewMonth = month;
		hebrewDay = day;
		hebrewYear = year;

		// reset Gregorian date
		gregorianAbsDate = hebrewDateToAbsDate(hebrewMonth, hebrewDay,
				hebrewYear);
		absDateToDate();

		// reset day of week
		dayOfWeek = Math.abs(gregorianAbsDate % 7) + 1;
	}

	/**
	 * Returns this object's date as a java.util.Date object. <B>Note</B>: This
	 * class does not have a concept of time.
	 * 
	 * @return The date as a java.util.Date object.
	 */
	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(gregorianYear, gregorianMonth - 1, gregorianDayOfMonth);
		return cal.getTime();
	}

	/**
	 * Resets this date to the current system date.
	 */
	public void resetDate() {
		Calendar cal = Calendar.getInstance();
		setDate(cal);
	}

	/**
	 * Returns a string containing the Hebrew date in the form,
	 * "day Month, year" e.g. "21 Shevat, 5729"
	 */
	public String toString() {
		return HebrewDateFormatter.getHebrewDateAsString(this);
	}

	/**
	 * Rolls the date forward by 1. It modifies both the Gregorian and Hebrew
	 * dates accordingly.
	 */
	public void forward() {
		// Change Gregorian date //
		if (gregorianDayOfMonth == getLastDayOfGregorianMonth(gregorianMonth)) {
			// if last day of year //
			if (gregorianMonth == 12) {
				gregorianYear++;
				gregorianMonth = 1;
				gregorianDayOfMonth = 1;
			} else {
				gregorianMonth++;
				gregorianDayOfMonth = 1;
			}
		} else { // if not last day of month
			gregorianDayOfMonth++;
		}

		// Change Hebrew Date //
		if (hebrewDay == getLastDayOfHebrewMonth(hebrewMonth, hebrewYear)) {
			// if it last day of elul (i.e. last day of hebrew year)//
			if (hebrewMonth == 6) {
				hebrewYear++;
				hebrewMonth++;
				hebrewDay = 1;
			} else if (hebrewMonth == getLastMonthOfHebrewYear(hebrewYear)) { // if it is
																	// the last
																	// day of
																	// Adar, or
																	// Adar II
																	// as case
																	// may be
				hebrewMonth = 1;
				hebrewDay = 1;
			} else {
				hebrewMonth++;
				hebrewDay = 1;
			}
		} else { // if not last date of month
			hebrewDay++;
		}
		// if last day of week, loop back to Sunday
		if (dayOfWeek == 7) {
			dayOfWeek = 1;
		} else {
			dayOfWeek++;
		}
		// increment the absolute date
		gregorianAbsDate++;
	}

	/**
	 * Rolls the date back by 1. It modifies both the Gregorian and Hebrew dates
	 * accordingly
	 */
	public void back() {
		// Change Gregorian date
		// if first day of month
		if (gregorianDayOfMonth == 1) {
			// if first day of year
			if (gregorianMonth == 1) {
				gregorianMonth = 12;
				gregorianYear--;
			} else {
				gregorianMonth--;
			}
			// change to last day of previous month
			gregorianDayOfMonth = getLastDayOfGregorianMonth(gregorianMonth);
		} else {
			gregorianDayOfMonth--;
		}
		// change Hebrew date
		// if first day of Hebrew month
		if (hebrewDay == 1) {
			// if Nissan
			if (hebrewMonth == 1) {
				hebrewMonth = getLastMonthOfHebrewYear(hebrewYear);
			} else if (hebrewMonth == 7) { // if Rosh Hashana
				hebrewYear--;
				hebrewMonth--;
			} else {
				hebrewMonth--;
			}
			hebrewDay = getLastDayOfHebrewMonth(hebrewMonth, hebrewYear);
		} else {
			hebrewDay--;
		}
		// if first day of week, loop back to Saturday
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		} else {
			dayOfWeek--;
		}
		// change the absolute date
		gregorianAbsDate--;
	}

	/**
	 * Compares two dates to see if they are equal
	 */
	public boolean equals(Object object) {
		HebrewDate hebDate = (HebrewDate) object;
		if (gregorianAbsDate != hebDate.getAbsDate()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Compares two dates as per the compareTo() method in the Comparable
	 * interface. Returns a value less than 0 if this date is "less than"
	 * (before) the date, greater than 0 if this date is "greater than" (after)
	 * the date, or 0 if they are equal.
	 */
	public int compareTo(Object o) {
		HebrewDate hebDate = (HebrewDate) o;
		if (gregorianAbsDate < hebDate.getAbsDate()) {
			return -1;
		} else if (gregorianAbsDate > hebDate.getAbsDate()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Returns the Gregorian month (between 1-12).
	 */
	public int getGregorianMonth() {
		return gregorianMonth;
	}

	/**
	 * Returns the Gregorian day of the month.
	 */
	public int getGregorianDayOfMonth() {
		return gregorianDayOfMonth;
	}

	/**
	 * Returns the year.
	 */
	public int getGregorianYear() {
		return gregorianYear;
	}

	/**
	 * Returns the Hebrew month (1-12 or 13).
	 */
	public int getHebrewMonth() {
		return hebrewMonth;
	}

	/**
	 * Returns the Hebrew date of the month.
	 */
	public int getHebrewDayOfMonth() {
		return hebrewDay;
	}

	/**
	 * Returns the Hebrew year.
	 */
	public int getHebrewYear() {
		return hebrewYear;
	}

	/**
	 * Returns the day of the week as a number between 1-7.
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/** sets the month. */
	public void setGregorianMonth(int month) {
		setGregorianDate(month, gregorianDayOfMonth, gregorianYear);
	}

	/** sets the year. */
	public void setGregorianYear(int year) {
		setGregorianDate(gregorianMonth, gregorianDayOfMonth, year);
	}

	/** sets the date of the month. */
	public void setGregorianDayOfMonth(int dayOfMonth) {
		setGregorianDate(gregorianMonth, dayOfMonth, gregorianYear);
	}

	/** sets the Hebrew month. */
	public void setHebrewMonth(int month) {
		setHebrewDate(month, hebrewDay, hebrewYear);
	}

	/** sets the hebrew year. */
	public void setHebrewYear(int year) {
		setGregorianDate(hebrewMonth, hebrewDay, year);
	}

	/** sets the Hebrew date of month. */
	public void setHebrewDayOfMonth(int dayOfMonth) {
		setHebrewDate(hebrewMonth, dayOfMonth, hebrewYear);
	}

	/**
	 * returns the int value of the Omer day or zero if the day is not in the
	 * omer
	 * 
	 * @return The Omer count as an int or {@link Integer#MIN_VALUE} if it is
	 *         not a day of the Omer.
	 */
	public int getOmer() {
		// int omer = 0;
		// better to use MIN_VALUE that is a better equivalent to null
		int omer = Integer.MIN_VALUE;

		// if Nissan and second day of Pesach and on
		if (hebrewMonth == 1 && hebrewDay >= 16) {
			omer = hebrewDay - 15;
			// if Iyar
		} else if (hebrewMonth == 2) {
			omer = hebrewDay + 15;
			// if Sivan and before Shavuos
		} else if (hebrewMonth == 3 && hebrewDay < 6)
			omer = hebrewDay + 44;

		return omer;
	}

	/**
	 * returns a String of the Jewish holiday or fast day for the current day,
	 * or a null if there is no holiday for this day. Has no "modern" holidays.
	 * 
	 * @return A String containing the holiday name or an empty string.
	 */
	public String getHoliday() {
		// check by month (starts from Nissan)
		switch (hebrewMonth) {
		case 1:
			if (hebrewDay == 14) {
				return "Erev Pesach";
			} else if (hebrewDay == 15 || hebrewDay == 21
					|| (!inIsrael && (hebrewDay == 16 || hebrewDay == 22))) {
				return "Pesach";
			} else if (hebrewDay >= 17 && hebrewDay <= 20
					|| (hebrewDay == 16 && inIsrael)) {
				return "Chol Hamoed Pesach";
			}
			break;
		case 2:
			if (hebrewDay == 14) {
				return "Pesach Sheni";
			}
			break;
		case 3:
			if (hebrewDay == 5) {
				return "Erev Shavuos";
				// if (ashkenaz) {
				// return "Erev Shavuos";
				// } else {
				// return "Erev Shavuot";
				// }
			} else if (hebrewDay == 6 || (hebrewDay == 7 && !inIsrael)) {
				return "Shavuos";
				// if (ashkenaz) {
				// return "Shavuos";
				// } else {
				// return "Shavuot";
				// }
			}
			break;
		case 4:
			// push off the fast day if it falls on Shabbos
			if ((hebrewDay == 17 && dayOfWeek != 7)
					|| (hebrewDay == 18 && dayOfWeek == 1)) {
				return "Tzom Tammuz";
			}
			break;
		case 5:
			// if Tisha B'av falls on Shabbos, push off until Sunday
			if ((dayOfWeek == 1 && hebrewDay == 10)
					|| (dayOfWeek != 7 && hebrewDay == 9)) {
				return "Tisha B'av";
			} else if (hebrewDay == 15) {
				return "Tu B'Av";
			}
			break;
		case 6:
			if (hebrewDay == 29) {
				return "Erev Rosh Hashanah";
			}
			break;
		case 7:
			if (hebrewDay == 1 || hebrewDay == 2) {
				return "Rosh Hashanah";
			} else if ((hebrewDay == 3 && dayOfWeek != 7)
					|| (hebrewDay == 4 && dayOfWeek == 1)) { // push off Tzom
																// Gedalia
				// if it falls on
				// Shabbos
				return "Tzom Gedalia";
			} else if (hebrewDay == 9) {
				return "Erev Yom Kippur";
			} else if (hebrewDay == 10) {
				return "Yom Kippur";
			} else if (hebrewDay == 14) {
				return "Erev Sukkos";
				// if (ashkenaz) {
				// return "Erev Sukkos";
				// } else {
				// return "Erev Sukkot";
				// }
			}
			if (hebrewDay == 15 || (hebrewDay == 16 && !inIsrael)) {
				return "Sukkos";
				// if (ashkenaz) {
				// return "Sukkos";
				// } else {
				// return "Sukkot";
				// }
			}
			if (hebrewDay >= 17 && hebrewDay <= 20
					|| (hebrewDay == 16 && inIsrael)) {
				return "Chol Hamoed Sukkos";
				// if (ashkenaz) {
				// return "Chol Hamoed Sukkos";
				// } else {
				// return "Chol Hamoed Sukkot";
				// }
			}
			if (hebrewDay == 21) {
				return "Hoshana Rabah";
			}
			if (hebrewDay == 22) {
				return "Shmini Atzeres";
				// if (ashkenaz) {
				// return "Shmini Atzeres";
				// } else {
				// return "Shmini Atzeret";
				// }
			}
			if (hebrewDay == 23 && !inIsrael) {
				return "Simchas Torah";
				// if (ashkenaz) {
				// return "Simchas Torah";
				// } else {
				// return "Simchat Torah";
				// }
			}
			break;
		case 9:
			if (hebrewDay == 24) {
				return "Erev Chanukah";
			} else if (hebrewDay >= 25) {
				return "Chanukah";
			}
			break;
		case 10:
			if (hebrewDay == 1 || hebrewDay == 2
					|| (hebrewDay == 3 && isKislevShort(hebrewYear))) {
				return "Chanukah";
			} else if (hebrewDay == 10) {
				return "Asarah BeTeves";
				// if (ashkenaz) {
				// return "Tzom Teves";
				// } else {
				// return "Tzom Tevet";
				// }
			}
			break;
		case 11:
			if (hebrewDay == 15) {
				return "Tu B'Shvat";
			}
			break;
		case 12:
			if (!isHebrewLeapYear(hebrewYear)) {
				// if 13th Adar falls on Friday or Shabbos, push back to
				// Thursday
				if (((hebrewDay == 11 || hebrewDay == 12) && dayOfWeek == 5)
						|| (hebrewDay == 13 && !(dayOfWeek == 6 || dayOfWeek == 7))) {
					return "Ta'anis Esther";
					// if (ashkenaz) {
					// return "Ta'anis Esther";
					// } else {
					// return "Ta'anit Esther";
					// }
				}
				if (hebrewDay == 14) {
					return "Purim";
				} else if (hebrewDay == 15) {
					return "Shushan Purim";
				}
			}
			// else if a leap year //
			else {
				if (hebrewDay == 14) {
					return "Purim Katan";
				}
			}
			break;
		case 13:
			// if 13th Adar falls on Friday or Shabbos, push back to Thursday
			if (((hebrewDay == 11 || hebrewDay == 12) && dayOfWeek == 5)
					|| (hebrewDay == 13 && !(dayOfWeek == 6 || dayOfWeek == 7))) {
				return "Ta'anis Esther";
				// if (ashkenaz) {
				// return "Ta'anis Esther";
				// } else {
				// return "Ta'anit Esther";
				// }
			}
			if (hebrewDay == 14) {
				return "Purim";
			} else if (hebrewDay == 15) {
				return "Shushan Purim";
			}
			break;
		}
		// if we get to this stage, then there are no holidays for the given
		// date
		// return "";
		return null;
	}

	/**
	 * Sets whether to use Israel parsha and holiday scheme or not. Default is
	 * "false".
	 */
	public void setInIsrael(boolean inIsrael) {
		this.inIsrael = inIsrael;
	}

	/**
	 * Gets whether Israel parsha and holiday scheme is used or not. Default is
	 * "false".
	 */
	public boolean getInIsrael() {
		return inIsrael;
	}

	// These indices were originally included in the emacs 19 distribution.
	// These arrays determine the correct indices into the parsha names
	// -1 means no parsha that week, values > 52 means it is a double parsha
	private static final int[] Sat_short = { -1, 52, -1, -1, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 53, 23, 24,
			-1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44,
			45, 46, 47, 48, 49, 50 };

	private static final int[] Sat_long = { -1, 52, -1, -1, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 53, 23, 24,
			-1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44,
			45, 46, 47, 48, 49, 59 };

	private static final int[] Mon_short = { 51, 52, -1, 0, 1, 2, 3, 4, 5, 6,
			7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 53, 23, 24,
			-1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44,
			45, 46, 47, 48, 49, 59 };

	private static final int[] Mon_long = // split //
	{ 51, 52, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 53, 23, 24, -1, 25, 54, 55, 30, 56, 33, -1, 34, 35, 36,
			37, 57, 40, 58, 43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Thu_normal = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6,
			7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 53, 23, 24,
			-1, -1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43,
			44, 45, 46, 47, 48, 49, 50 };
	private static final int[] Thu_normal_Israel = { 52, -1, -1, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 53, 23,
			24, -1, 25, 54, 55, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 58,
			43, 44, 45, 46, 47, 48, 49, 50 };

	private static final int[] Thu_long = { 52, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
			-1, 25, 54, 55, 30, 56, 33, 34, 35, 36, 37, 38, 39, 40, 58, 43, 44,
			45, 46, 47, 48, 49, 50 };

	private static final int[] Sat_short_leap = { -1, 52, -1, -1, 0, 1, 2, 3,
			4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 58, 43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Sat_long_leap = { -1, 52, -1, -1, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
			23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, -1, 34, 35, 36, 37,
			57, 40, 58, 43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Mon_short_leap = { 51, 52, -1, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, -1, 34, 35, 36, 37, 57,
			40, 58, 43, 44, 45, 46, 47, 48, 49, 59 };
	private static final int[] Mon_short_leap_Israel = { 51, 52, -1, 0, 1, 2,
			3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36,
			37, 38, 39, 40, 58, 43, 44, 45, 46, 47, 48, 49, 59 };

	private static final int[] Mon_long_leap = { 51, 52, -1, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, -1, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
			39, 40, 58, 43, 44, 45, 46, 47, 48, 49, 50 };
	private static final int[] Mon_long_leap_Israel = { 51, 52, -1, 0, 1, 2, 3,
			4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50 };

	private static final int[] Thu_short_leap = { 52, -1, -1, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28, -1, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
			40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50 };

	private static final int[] Thu_long_leap = { 52, -1, -1, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28, -1, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
			40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 59 };

	/**
	 * returns a string of today's parsha(ios) or an empty string if there are
	 * none. FIXME: consider possibly return the parsha of the week for any day
	 * during the week instead of empty.
	 */
	public String getParsha() {

		// if today is not Shabbos, then there is no normal parsha reading
		// if (getDayOfWeek() != 7)
		// return "";

		// kvia = whether a Hebrew year is short/regular/long (0/1/2)
		// roshHashana = Rosh Hashana of this Hebrew year
		// roshHashanaDay= day of week Rosh Hashana was on this year
		// week= current week in Hebrew calendar from Rosh Hashana
		// array= the correct index array for this Hebrew year
		// index= the index number of the parsha name
		int kvia;
		int roshHashanaDay;
		int week;
		int[] array = null;
		int index;
		// create a clone of this date
		HebrewDate roshHashana = (HebrewDate) this.clone();
		try {
			// set it to Rosh Hashana of this year
			roshHashana.setHebrewDate(7, 1, hebrewYear);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		// get day Rosh Hashana was on
		roshHashanaDay = roshHashana.getDayOfWeek();

		// week is the week since the first Shabbos on or after Rosh Hashana
		week = (((gregorianAbsDate - roshHashana.getAbsDate()) - (7 - roshHashanaDay)) / 7);

		// get kvia
		if (isCheshvanLong(hebrewYear) && !isKislevShort(hebrewYear))
			kvia = 2;
		else if (!isCheshvanLong(hebrewYear) && isKislevShort(hebrewYear))
			kvia = 0;
		else
			kvia = 1;

		// determine appropriate array
		if (!isHebrewLeapYear(hebrewYear)) {
			switch (roshHashanaDay) {
			case 7: // RH was on a Saturday
				if (kvia == 0)
					array = Sat_short;
				else if (kvia == 2)
					array = Sat_long;
				break;
			case 2: // RH was on a Monday
				if (kvia == 0)
					array = Mon_short;
				else if (kvia == 2)
					array = inIsrael ? Mon_short : Mon_long;
				break;
			case 3: // RH was on a Tuesday
				if (kvia == 1)
					array = inIsrael ? Mon_short : Mon_long;
				break;
			case 5: // RH was on a Thursday
				if (kvia == 1)
					array = inIsrael ? Thu_normal_Israel : Thu_normal;
				else if (kvia == 2)
					array = Thu_long;
				break;
			}
		}

		// if leap year //
		else {
			switch (roshHashanaDay) {
			case 7: // RH was on a Sat
				if (kvia == 0)
					array = Sat_short_leap;
				else if (kvia == 2)
					array = inIsrael ? Sat_short_leap : Sat_long_leap;
				break;
			case 2: // RH was on a Mon
				if (kvia == 0)
					array = inIsrael ? Mon_short_leap_Israel : Mon_short_leap;
				else if (kvia == 2)
					array = inIsrael ? Mon_long_leap_Israel : Mon_long_leap;
				break;
			case 3: // RH was on a Tue
				if (kvia == 1)
					array = inIsrael ? Mon_long_leap_Israel : Mon_long_leap;
				break;
			case 5: // RH was on a Thu
				if (kvia == 0)
					array = Thu_short_leap;
				else if (kvia == 2)
					array = Thu_long_leap;
				break;
			}
		}
		// if something goes wrong
		if (array == null)
			throw new RuntimeException(
					"Was not able to set the index array to any of the known types."); // FIXME
																						// -
																						// Clearer
																						// message
																						// required

		// get index from array
		index = array[week];

		// If no Parsha this week
		if (index == -1)
			return "";

		// if parsha this week
		else {
			return parshios[index];
		}
	}

	/** Create a copy of this date. */
	// FIXME - create deep clone
	public Object clone() {
		// try {
		return new HebrewDate(gregorianMonth, gregorianDayOfMonth,
				gregorianYear);
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// return null;
		// }
	}
}