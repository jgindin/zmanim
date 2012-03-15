/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;


import org.intellij.lang.annotations.MagicConstant;

/**
 * Enumeration of the Hebrew months, starting with Nissan.
 * 
 * TODO: Make this an enum.
 */
public interface HebrewMonth {

	@MagicConstant( intValues = { HebrewMonth.NISSAN, HebrewMonth.IYAR, HebrewMonth.SIVAN, HebrewMonth.TAMMUZ, HebrewMonth.AV, HebrewMonth.ELUL, HebrewMonth.TISHREI, HebrewMonth.CHESHVAN, HebrewMonth.KISLEV, HebrewMonth.TEVET, HebrewMonth.SHEVAT, HebrewMonth.ADAR, HebrewMonth.ADAR_II })
	@interface Constant {
	}

	/**
	 * Value of the month field indicating Nissan, the first numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 7th (or 8th in a {@link HebrewDate#isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int NISSAN = 1;

	/**
	 * Value of the month field indicating Iyar, the second numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 8th (or 9th in a {@link HebrewDate#isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int IYAR = 2;

	/**
	 * Value of the month field indicating Sivan, the third numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 9th (or 10th in a {@link HebrewDate#isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int SIVAN = 3;

	/**
	 * Value of the month field indicating Tammuz, the fourth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 10th (or 11th in a {@link HebrewDate#isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int TAMMUZ = 4;

	/**
	 * Value of the month field indicating Av, the fifth numeric month of the year in the Jewish calendar. With the year
	 * starting at {@link #TISHREI}, it would actually be the 11th (or 12th in a {@link HebrewDate#isJewishLeapYear() leap year})
	 * month of the year.
	 */
	public static final int AV = 5;

	/**
	 * Value of the month field indicating Elul, the sixth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 12th (or 13th in a {@link HebrewDate#isJewishLeapYear() leap
	 * year}) month of the year.
	 */
	public static final int ELUL = 6;

	/**
	 * Value of the month field indicating Tishrei, the seventh numeric month of the year in the Jewish calendar. With
	 * the year starting at this month, it would actually be the 1st month of the year.
	 */
	public static final int TISHREI = 7;

	/**
	 * Value of the month field indicating Cheshvan/Marcheshvan, the eighth numeric month of the year in the Jewish
	 * calendar. With the year starting at {@link #TISHREI}, it would actually be the 2nd month of the year.
	 */
	public static final int CHESHVAN = 8;

	/**
	 * Value of the month field indicating Kislev, the ninth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 3rd month of the year.
	 */
	public static final int KISLEV = 9;

	/**
	 * Value of the month field indicating Teves, the tenth numeric month of the year in the Jewish calendar. With the
	 * year starting at {@link #TISHREI}, it would actually be the 4th month of the year.
	 */
	public static final int TEVET = 10;

	/**
	 * Value of the month field indicating Shevat, the eleventh numeric month of the year in the Jewish calendar. With
	 * the year starting at {@link #TISHREI}, it would actually be the 5th month of the year.
	 */
	public static final int SHEVAT = 11;

	/**
	 * Value of the month field indicating Adar (or Adar I in a {@link HebrewDate#isJewishLeapYear() leap year}), the twelfth
	 * numeric month of the year in the Jewish calendar. With the year starting at {@link #TISHREI}, it would actually
	 * be the 6th month of the year.
	 */
	public static final int ADAR = 12;

	/**
	 * Value of the month field indicating Adar II, the leap (intercalary or embolismic) thirteenth (Undecimber) numeric
	 * month of the year added in Jewish {@link HebrewDate#isJewishLeapYear() leap year}). The leap years are years 3, 6, 8, 11,
	 * 14, 17 and 19 of a 19 year cycle. With the year starting at {@link #TISHREI}, it would actually be the 7th month
	 * of the year.
	 */
	public static final int ADAR_II = 13;

	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Month lengths 
	
	public static final int SHORT_MONTH_LENGTH = 29;
	public static final int LONG_MONTH_LENGTH = 30;

}  // End of HebrewMonth class
