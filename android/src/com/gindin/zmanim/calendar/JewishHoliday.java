/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;



/**
 * Represents a Jewish holiday. Not using the ICU4J HebrewHoliday class because I just couldn't figure out how
 *  to properly use the rules (especially the RangeDateRule) which would be necessary, given that when certain
 *  holidays fall out depends on what year.
 */
@SuppressWarnings( { "MagicNumber" } )
public class JewishHoliday {

	private static final JewishHoliday ROSH_HASHANAH    = new RoshHashana();
	private static final JewishHoliday YOM_KIPPUR       = new JewishHoliday( HebrewCalendar.TISHRI, 10,      "Yom Kippur" );
	private static final JewishHoliday SUKKOT           = new JewishHoliday( HebrewCalendar.TISHRI, 15,  6,  "Sukkot" );
	private static final JewishHoliday HOSHANAH_RABBAH  = new JewishHoliday( HebrewCalendar.TISHRI, 21,      "Hoshanah Rabbah" );
	private static final JewishHoliday SHEMINI_ATZERET  = new JewishHoliday( HebrewCalendar.TISHRI, 22,      "Shemini Atzeret" );
	private static final JewishHoliday SIMCHAT_TORAH    = new JewishHoliday( HebrewCalendar.TISHRI, 23,      "Simchat Torah" );
	private static final JewishHoliday HANUKKAH         = new Hanukkah();
	private static final JewishHoliday TU_BSHEVAT       = new Observance(    HebrewCalendar.SHEVAT, 15,      "Tu B'Shevat" );
	private static final JewishHoliday PURIM_KATAN      = new PurimKatan();
	private static final JewishHoliday PURIM            = new Observance( HebrewCalendar.ADAR,      14,      "Purim" );
	private static final JewishHoliday SHUSHAN_PURIM    = new Observance( HebrewCalendar.ADAR,      15,      "Shushan Purim" );
	private static final JewishHoliday PASSOVER         = new JewishHoliday( HebrewCalendar.NISAN,  15,  8,  "Pesach" );
	private static final JewishHoliday YOM_HASHOAH      = new Observance( HebrewCalendar.NISAN,     27,      "Yom Hashoah" );
	private static final JewishHoliday YOM_HAZIKARON    = new Observance( HebrewCalendar.IYAR,       4,      "Yom Hazikaron" );
	private static final JewishHoliday YOM_HAATZMAUT    = new Observance( HebrewCalendar.IYAR,       5,      "Yom Ha'Atzmaut" );
	private static final JewishHoliday PESACH_SHEINI    = new Observance( HebrewCalendar.IYAR,      14,      "Pesach Sheini" );
	private static final JewishHoliday LAG_BOMER        = new Observance( HebrewCalendar.IYAR,      18,      "Lag B'Omer" );
	private static final JewishHoliday YOM_YERUSHALAYIM = new Observance( HebrewCalendar.IYAR,      28,      "Yom Yerushalayim" );
	private static final JewishHoliday SHAVUOT          = new JewishHoliday( HebrewCalendar.SIVAN,   6,  2,  "Shavuot" );
	private static final JewishHoliday TISHA_BAV        = new DayTimeTaanit( HebrewCalendar.AV,      9,      "Tisha B'Av" );
	private static final JewishHoliday TU_BAV           = new Observance( HebrewCalendar.AV,        15,      "Tu B'Av" );

	/**
	 * Set of the holidays. We're not using the com.ibm.icu.util.JewishHoliday.getHolidays() because the
	 *  list declared in com.ibm.icu.impl.data.HolidayBundle_iw_IL is sadly lacking.
	 */
	public static final JewishHoliday[] JEWISH_HOLIDAYS = {
		JewishHoliday.ROSH_HASHANAH,      JewishHoliday.YOM_KIPPUR,         JewishHoliday.SUKKOT,
		JewishHoliday.HOSHANAH_RABBAH,    JewishHoliday.SHEMINI_ATZERET,    JewishHoliday.SIMCHAT_TORAH,
		JewishHoliday.HANUKKAH,           JewishHoliday.TU_BSHEVAT,         JewishHoliday.PURIM_KATAN,
		JewishHoliday.PURIM,              JewishHoliday.SHUSHAN_PURIM,      JewishHoliday.PASSOVER,
		JewishHoliday.YOM_HASHOAH,        JewishHoliday.YOM_HAZIKARON,      JewishHoliday.YOM_HAATZMAUT,
		JewishHoliday.PESACH_SHEINI,      JewishHoliday.LAG_BOMER,          JewishHoliday.YOM_YERUSHALAYIM,
		JewishHoliday.SHAVUOT,            JewishHoliday.TISHA_BAV,          JewishHoliday.TU_BAV
	};


	private static final JewishHoliday GEDALIAH        = new DayTimeTaanit( HebrewCalendar.TISHRI,  3,      "Gedaliah" );
	private static final JewishHoliday TEVET_10        = new DayTimeTaanit( HebrewCalendar.TEVET,  10,      "Tevet 10" );
	private static final JewishHoliday ESTHER          = new TaanitEsther();
	private static final JewishHoliday TAMMUZ_17       = new DayTimeTaanit( HebrewCalendar.TAMUZ,  17,      "Tammuz 17" );

	public static final JewishHoliday[] ARBA_TAANIOT = {
		JewishHoliday.GEDALIAH,           JewishHoliday.TEVET_10,           JewishHoliday.ESTHER,
		JewishHoliday.TAMMUZ_17
	};


	/**
	 * The omer count shows up as a "holiday".
	 */
	private static final JewishHoliday NISAN_OMER = new NisanOmer();
	private static final JewishHoliday IYAR_OMER = new IyarOmer();
	private static final JewishHoliday SIVAN_OMER = new SivanOmer();

	public static  final JewishHoliday[] OMER = {
		NISAN_OMER, IYAR_OMER, SIVAN_OMER
	};
	
	private static final JewishHoliday ROSH_CHODESH_NISAN   = new RoshChodesh( HebrewCalendar.NISAN,   1, 1, "Nisan" );
	private static final JewishHoliday ROSH_CHODESH_IYAR    = new RoshChodesh( HebrewCalendar.NISAN,  30, 2, "Iyar" );
	private static final JewishHoliday ROSH_CHODESH_SIVAN   = new RoshChodesh( HebrewCalendar.SIVAN,   1, 1, "Sivan" );
	private static final JewishHoliday ROSH_CHODESH_TAMMUZ  = new RoshChodesh( HebrewCalendar.SIVAN,  30, 2, "Tammuz" );
	private static final JewishHoliday ROSH_CHODESH_AV      = new RoshChodesh( HebrewCalendar.AV,      1, 1, "Av" );
	private static final JewishHoliday ROSH_CHODESH_ELUL    = new RoshChodesh( HebrewCalendar.AV,     30, 2, "Elul" );
	// Don't bother...this is Rosh Hashana...
	//private static final JewishHoliday ROSH_CHODESH_TISHRI  = new RoshChodesh( HebrewCalendar.ELUL,   30, 2, "Tishri" );
	private static final JewishHoliday ROSH_CHODESH_HESHVAN = new RoshChodesh( HebrewCalendar.HESHVAN, 1, 1, "Heshvan" );
	private static final JewishHoliday ROSH_CHODESH_KISLEV  = new RoshChodesh( HebrewCalendar.KISLEV,  1, 1, "Kislev" );
	private static final JewishHoliday ROSH_CHODESH_TEVET   = new RoshChodesh( HebrewCalendar.TEVET,   1, 1, "Tevet" );
	private static final JewishHoliday ROSH_CHODESH_SHEVAT  = new RoshChodesh( HebrewCalendar.SHEVAT,  1, 1, "Shevat" );
	private static final JewishHoliday ROSH_CHODESH_ADAR    = new RoshChodesh( HebrewCalendar.SHEVAT, 30, 2, "Adar" );
	private static final JewishHoliday ROSH_CHODESH_ADAR_I  = new RoshChodesh( HebrewCalendar.SHEVAT, 30, 2, "Adar I" );
	private static final JewishHoliday ROSH_CHODESH_ADAR_II = new RoshChodesh( HebrewCalendar.ADAR_1, 30, 2, "Adar II" );

	/**
	 * We treat Rosh Chodesh as a holiday too.
	 */
	public static final JewishHoliday[]                       ROSH_CHODESH_HASER = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,    ROSH_CHODESH_TEVET,     ROSH_CHODESH_SHEVAT,
	};

	public static final JewishHoliday[]                       ROSH_CHODESH_QESIDRAH = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,
		new JewishHoliday( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	public static final JewishHoliday[]                       ROSH_CHODESH_SHALEM = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_HESHVAN,
		new JewishHoliday( HebrewCalendar.HESHVAN, 30, 2, "Rosh Chodesh Kislev" ),  // Add a day compared to Qesidrah
		new JewishHoliday( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	public static final JewishHoliday[]                       ROSH_CHODESH_ADAR_REGULAR = {
		ROSH_CHODESH_ADAR,
	};

	public static final JewishHoliday[]                       ROSH_CHODESH_ADAR_LEAP = {
		ROSH_CHODESH_ADAR_I,    ROSH_CHODESH_ADAR_II
	};
	

	/** Have one instance we use for checking. */
	private static final HebrewCalendar                       calendar = new HebrewCalendar();

	static {
		// Set it so that the hours, minutes, and seconds won't matter.
		calendar.set( 2009, 10, 21, 0, 0, 0 );
	}

	
	private final String                                      name;
	
	protected final int                                       month;
	protected final int                                       date;
	protected final int                                       length;


	public JewishHoliday(
		int     month,
		int     date,
	  String  name
	) {
		this( month, date, 1, name );
	}


	public JewishHoliday(
		int     month,
		int     date,
		int     length,
	  String  name
	) {
		this.month = month;
		this.date = date;
		this.length = length;
		this.name = name;
	}


	/**
	 * @param hebrewCalendar The date we want a 'version' of this holiday for.
	 *
	 * @return a version of this holiday, specific for the specified date.
	 *  NOTE: Many implementations will just return themselves, as there is nothing special about them that changes
	 *    based on the date.
	 */
	public JewishHoliday getForDate(
		HebrewCalendar hebrewCalendar
	) {

		// Nothing about this is dependent upon the date...
		return this;
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


	public static JewishHoliday createErevOf(
		JewishHoliday holiday
	) {

		return new Erev( holiday );
	}


	public boolean isOn(
		HebrewCalendar dateToTest
	) {

		if ( null == dateToTest ) {
			return false;
		}

		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
		if ( isSameDate( dateToTest, calendar ) ) {
			return true;
		}

		for ( int i = 1; i < length; i++ ) {

			calendar.add( HebrewCalendar.DATE, 1 );
			if ( isSameDate( dateToTest, calendar ) ) {
				return true;
			}
		}

		return false;
	}

	public boolean isErev(
		HebrewCalendar dateToTest
	) {

		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
		calendar.add( HebrewCalendar.DATE, -1 );

		return isSameDate( dateToTest, calendar );
	}


	public boolean showCandlelighting() {
		return true;
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


	/**
	 * Used by the isErev() method to create a 'placeholder'.
	 */
	private static class Erev
		extends JewishHoliday {

		private final boolean                                   showCandles;

		private Erev(
			JewishHoliday holiday
		) {

			super( holiday.month, holiday.date, 1, "Erev " + holiday.name );
			showCandles = holiday.showCandlelighting();
		}


		@Override
		public boolean showCandlelighting() {
			return showCandles;
		}
	}


	/**
	 * A non-holiday observance. The real difference is this is not a Yom Tov which requires
	 *  candle lighting.
	 */
	private static class Observance
		extends JewishHoliday {

		private Observance(
			int     month,
			int     date,
			String  name
		) {
			super( month, date, name );
		}


		private Observance(
			int     month,
			int     date,
			int     length,
			String  name
		) {
			super( month, date, length, name );
		}


		@Override
		public boolean showCandlelighting() {
			return false;
		}
	}


	/**
	 * Specialized for Rosh Hashana, because when we try to check for Erev, the years will be different...
	 */
	private static class RoshHashana
		extends JewishHoliday {

		private RoshHashana() {
			super( HebrewCalendar.TISHRI,  1,  2,  "Rosh Hashanah" );
		}


		@Override
		public boolean isErev( HebrewCalendar dateToTest ) {

			// Basically the same as the base class, but when we're actually checking for Erev Rosh Hashana,
			//  the date to test will be, e.g., "29 Elul 5770". When we subtract a day from our day (1 Tishri),
			//  then we wind up going "back" to 5769, so we don't match...
			calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
			calendar.add( HebrewCalendar.DATE, -1 );
			calendar.add( HebrewCalendar.YEAR, 1 );

			return isSameDate( dateToTest, calendar );
		}
	}


	/**
	 * Specialized for Rosh Chodesh
	 */
	private static class RoshChodesh
		extends Observance {

		private RoshChodesh(
			int     month,
			int     date,
			int     length,
			String  name
		) {
			
			super( month, date, length, "Rosh Chodesh " + name );
		}

	} // End of RoshChodesh class


	/**
	 * Specialized because most fasts are just during the day.
	 *<p>
	 * Note: The fact that these fast days push off to Sunday is from http://www.jewishgen.org/infofiles/m_calint.htm
	 */
	private static class DayTimeTaanit
		extends Observance {

		private DayTimeTaanit(
			int     month,
			int     date,
			String  name
		) {
			super( month, date, "Ta'anit " + name );
		}


		@Override
		public boolean isOn( HebrewCalendar dateToTest ) {

			// Wrong month? Then can't be!
			if ( month != dateToTest.get( HebrewCalendar.MONTH ) ) {
				return false;
			}

			int adjustment = getDateToUse( dateToTest );
			calendar.add( HebrewCalendar.DATE, adjustment );

			return isSameDate( dateToTest, calendar );
		}


		@Override
		public boolean isErev( HebrewCalendar dateToTest ) {

			// Wrong month? Then can't be!
			if ( month != dateToTest.get( HebrewCalendar.MONTH ) ) {
				return false;
			}

			int adjustment = getDateToUse( dateToTest );
			calendar.add( HebrewCalendar.DATE, adjustment - 1 );

			return isSameDate( dateToTest, calendar );
		}


		@SuppressWarnings( { "FeatureEnvy" } )
		protected int getDateToUse( HebrewCalendar dateToTest ) {

			// Update the calendar for the test...
			calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );

			// Cannot fall out on Shabbat! If this Shabbat, then move it to Sunday.
			int adjustment = 0;

			final int dayOfWeek = calendar.get( HebrewCalendar.DAY_OF_WEEK );
			if ( HebrewCalendar.SATURDAY == dayOfWeek ) {
				adjustment = 1;
			}

			return adjustment;
		}

	} // End of DayTimeTaanit



	/**
	 * Special class to handle the date changes associated with Ta'anit Esther.
	 */
	private static class TaanitEsther
		extends DayTimeTaanit {

		TaanitEsther() {
			super( HebrewCalendar.ADAR, 13, "Esther" );
		}


		@SuppressWarnings( { "FeatureEnvy" } )
		protected int getDateToUse(
			HebrewCalendar dateToTest
		) {

			// Update the calendar for the test...
			calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );

			int adjustment = 0;

			// Now, if the fast is supposed to fall out on a Friday or Shabbat, move it back to Thursday.
			final int dayOfWeek = calendar.get( HebrewCalendar.DAY_OF_WEEK );
			if ( HebrewCalendar.FRIDAY == dayOfWeek ) {
				adjustment = -1;
			}
			else if ( HebrewCalendar.SATURDAY == dayOfWeek ) {
				adjustment = -2;
			}

			return adjustment;
		}

	} // End of TaanitEsther class


	/**
	 * Special class to ensure that Purim katan only shows up in leap years.
	 */
	private static class PurimKatan
		extends Observance {

		PurimKatan() {
			super( HebrewCalendar.ADAR_1, 14, "Purim Katan" );
		}


		@Override
		public boolean isErev( HebrewCalendar dateToTest ) {

			// Not a leap year? Then this "holiday" doesn't exist...
			return dateToTest.isLeapYear() && super.isErev( dateToTest );
		}


		@Override
		public boolean isOn( HebrewCalendar dateToTest ) {

			// Not a leap year? Then this "holiday" doesn't exist...
			return dateToTest.isLeapYear() && super.isOn( dateToTest );
		}
	}


	/**
	 * For those observances where we want a different description based on which day of the observance it is.
	 *  E.g., "Hanukkah 3", or "Omer 22".
	 */
	private static class DatedObservance
		extends Observance {

		private final HebrewCalendar                            forDate;

		DatedObservance(
			int             month,
			int             date,
			int             length,
			String          name,
			HebrewCalendar  forDate
		) {
			super( month, date, length, name  );
			this.forDate = forDate;
		}


		@Override
		public String getDescription() {
			String description = super.getDescription();

			if ( null == forDate ) {
				return description;
			}

			calendar.set( forDate.get( HebrewCalendar.YEAR ), month, date );
			if ( isSameDate( forDate, calendar ) ) {
				return description + " " + ( 1 + getCountOffset() );
			}

			for ( int i = 1; i < length; i++ ) {

				calendar.add( HebrewCalendar.DATE, 1 );
				if ( isSameDate( forDate, calendar ) ) {
					return description + " " + ( i + 1 + getCountOffset() );
				}
			}

			// Should not have been able to get here...
			return description;
		}


		protected int getCountOffset() {
			return 0;
		}
	}


	/**
	 * Special class for Hanukkah so that we can display which day of the holiday it is.
	 */
	private static class Hanukkah
		extends DatedObservance {


		Hanukkah() {
			this( null );
		}


		Hanukkah(
			HebrewCalendar forDate
		) {

			super( HebrewCalendar.KISLEV, 25, 8, "Hanukkah", forDate );
		}


		@Override
		public JewishHoliday getForDate( HebrewCalendar hebrewCalendar ) {
			return new Hanukkah( hebrewCalendar );
		}
	}


	private static abstract class Omer<T extends Omer>
		extends DatedObservance {


		Omer(
			int             month,
			int             date,
			int             length,
			HebrewCalendar  forDate
		) {
			super( month, date, length, "Omer", forDate );
		}


		@Override
		public JewishHoliday getForDate( HebrewCalendar hebrewCalendar ) {
			return newOmerForDate( hebrewCalendar );
		}


		protected abstract T newOmerForDate( HebrewCalendar hebrewCalendar );
	}


	private static class NisanOmer
		extends Omer<NisanOmer> {

		NisanOmer() {
			this( null );
		}

		NisanOmer(
			HebrewCalendar forDate
		) {
			super( HebrewCalendar.NISAN, 16, 14, forDate );
		}


		@Override
		protected NisanOmer newOmerForDate( HebrewCalendar hebrewCalendar ) {
			return new NisanOmer( hebrewCalendar );
		}
	}


	private static class IyarOmer
		extends Omer<IyarOmer> {

		IyarOmer() {
			this( null );
		}


		IyarOmer(
			HebrewCalendar forDate
		) {
			super( HebrewCalendar.IYAR, 1, 29, forDate );
		}


		@Override
		protected int getCountOffset() {
			// Already counted the omer for the last 15 days of Nisan.
			return 15;
		}


		@Override
		protected IyarOmer newOmerForDate( HebrewCalendar hebrewCalendar ) {
			return new IyarOmer( hebrewCalendar );
		}
	}


	private static class SivanOmer
		extends Omer<SivanOmer> {

		SivanOmer() {
			this( null );
		}


		SivanOmer(
			HebrewCalendar forDate
		) {
			super( HebrewCalendar.SIVAN, 1, 5, forDate );
		}


		@Override
		protected int getCountOffset() {
			// Already counted the omer for the last 15 days of Nisan and all of Iyar.
			return 15 + 29;
		}


		@Override
		protected SivanOmer newOmerForDate( HebrewCalendar hebrewCalendar ) {
			return new SivanOmer( hebrewCalendar );
		}
	}

}  // End of JewishHoliday class
