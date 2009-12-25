package com.gindin.zmanim;



/**
 * Represents a Jewish holiday. Not using the ICU4J HebrewHoliday class because I just couldn't figure out how
 *  to properly use the rules (especially the RangeDateRule) which would be necessary, given that when certain
 *  holidays fall out depends on what year.
 */
@SuppressWarnings( { "MagicNumber" } )
public class JewishHoliday {

	private static JewishHoliday ROSH_HASHANAH   = new JewishHoliday( HebrewCalendar.TISHRI,  1,  2,  "Rosh Hashanah" );
	private static JewishHoliday GEDALIAH        = new JewishHoliday( HebrewCalendar.TISHRI,  3,      "Fast of Gedaliah" );
	private static JewishHoliday YOM_KIPPUR      = new JewishHoliday( HebrewCalendar.TISHRI, 10,      "Yom Kippur" );
	private static JewishHoliday SUKKOT          = new JewishHoliday( HebrewCalendar.TISHRI, 15,  6,  "Sukkot" );
	private static JewishHoliday HOSHANAH_RABBAH = new JewishHoliday( HebrewCalendar.TISHRI, 21,      "Hoshanah Rabbah" );
	private static JewishHoliday SHEMINI_ATZERET = new JewishHoliday( HebrewCalendar.TISHRI, 22,      "Shemini Atzeret" );
	private static JewishHoliday SIMCHAT_TORAH   = new JewishHoliday( HebrewCalendar.TISHRI, 23,      "Simchat Torah" );
	private static JewishHoliday HANUKKAH        = new JewishHoliday( HebrewCalendar.KISLEV, 25, 8,   "Hanukkah" );
	private static JewishHoliday TEVET_10        = new JewishHoliday( HebrewCalendar.TEVET,  10,      "Fast of Tevet 10" );
	private static JewishHoliday TU_BSHEVAT      = new JewishHoliday( HebrewCalendar.SHEVAT, 15,      "Tu B'Shevat" );
	private static JewishHoliday ESTHER          = new TaanitEsther();
	private static JewishHoliday PURIM_KATAN     = new PurimKatan();
	private static JewishHoliday PURIM           = new JewishHoliday( HebrewCalendar.ADAR,   14,      "Purim" );
	private static JewishHoliday SHUSHAN_PURIM   = new JewishHoliday( HebrewCalendar.ADAR,   15,      "Shushan Purim" );
	private static JewishHoliday PASSOVER        = new JewishHoliday( HebrewCalendar.NISAN,  15,  8,  "Pesach" );
	private static JewishHoliday YOM_HASHOAH     = new JewishHoliday( HebrewCalendar.NISAN,  27,      "Yom Hashoah" );
	private static JewishHoliday YOM_HAZIKARON   = new JewishHoliday( HebrewCalendar.IYAR,    4,      "Yom Hazikaron" );
	private static JewishHoliday YOM_HAATZMAUT   = new JewishHoliday( HebrewCalendar.IYAR,    5,      "Yom Ha'Atzmaut" );
	private static JewishHoliday PESACH_SHEINI   = new JewishHoliday( HebrewCalendar.IYAR,   14,      "Pesach Sheini" );
	private static JewishHoliday LAG_BOMER       = new JewishHoliday( HebrewCalendar.IYAR,   18,      "Lab B'Omer" );
	private static JewishHoliday YOM_YERUSHALAYIM = new JewishHoliday( HebrewCalendar.IYAR,  28,      "Yom Yerushalayim" );
	private static JewishHoliday SHAVUOT         = new JewishHoliday( HebrewCalendar.SIVAN,   6,  2,  "Shavuot" );
	private static JewishHoliday TAMMUZ_17       = new JewishHoliday( HebrewCalendar.TAMUZ,  17,      "Fast of Tammuz 17" );
	private static JewishHoliday TISHA_BAV       = new JewishHoliday( HebrewCalendar.AV,      9,      "Fast of Tisha B'Av" );
	                                    
	/**
	 * Set of the holidays. We're not using the com.ibm.icu.util.JewishHoliday.getHolidays() because the
	 *  list declared in com.ibm.icu.impl.data.HolidayBundle_iw_IL is sadly lacking.
	 */
	public static JewishHoliday[] JEWISH_HOLIDAYS = {
		JewishHoliday.ROSH_HASHANAH,      JewishHoliday.GEDALIAH,           JewishHoliday.YOM_KIPPUR,
		JewishHoliday.SUKKOT,             JewishHoliday.HOSHANAH_RABBAH,    JewishHoliday.SHEMINI_ATZERET,
		JewishHoliday.SIMCHAT_TORAH,      JewishHoliday.HANUKKAH,           JewishHoliday.TEVET_10,
		JewishHoliday.TU_BSHEVAT,         JewishHoliday.PURIM_KATAN,        JewishHoliday.ESTHER,
		JewishHoliday.PURIM,              JewishHoliday.SHUSHAN_PURIM,      JewishHoliday.PASSOVER,
		JewishHoliday.YOM_HASHOAH,        JewishHoliday.YOM_HAZIKARON,      JewishHoliday.YOM_HAATZMAUT,
		JewishHoliday.PESACH_SHEINI,      JewishHoliday.LAG_BOMER,          JewishHoliday.YOM_YERUSHALAYIM,
		JewishHoliday.SHAVUOT,            JewishHoliday.TAMMUZ_17,          JewishHoliday.TISHA_BAV
	};

	
	private static final JewishHoliday ROSH_CHODESH_NISAN = new JewishHoliday( HebrewCalendar.NISAN,     1, 1, "Rosh Chodesh Nisan" );
	private static final JewishHoliday ROSH_CHODESH_IYAR = new JewishHoliday( HebrewCalendar.NISAN,     30, 2, "Rosh Chodesh Iyar" );
	private static final JewishHoliday ROSH_CHODESH_SIVAN = new JewishHoliday( HebrewCalendar.SIVAN,     1, 1, "Rosh Chodesh Sivan" );
	private static final JewishHoliday ROSH_CHODESH_TAMMUZ = new JewishHoliday( HebrewCalendar.SIVAN,   30, 2, "Rosh Chodesh Tammuz" );
	private static final JewishHoliday ROSH_CHODESH_AV = new JewishHoliday( HebrewCalendar.AV,           1, 1, "Rosh Chodesh Av" );
	private static final JewishHoliday ROSH_CHODESH_ELUL = new JewishHoliday( HebrewCalendar.AV,        30, 2, "Rosh Chodesh Elul" );
	private static final JewishHoliday ROSH_CHODESH_TISHRI = new JewishHoliday( HebrewCalendar.ELUL,    30, 2, "Rosh Chodesh Tishri" );
	private static final JewishHoliday ROSH_CHODESH_HESHVAN = new JewishHoliday( HebrewCalendar.HESHVAN, 1, 1, "Rosh Chodesh Heshvan" );
	private static final JewishHoliday ROSH_CHODESH_KISLEV = new JewishHoliday( HebrewCalendar.KISLEV,   1, 1, "Rosh Chodesh Kislev" );
	private static final JewishHoliday ROSH_CHODESH_TEVET = new JewishHoliday( HebrewCalendar.TEVET,     1, 1, "Rosh Chodesh Tevet" );
	private static final JewishHoliday ROSH_CHODESH_SHEVAT = new JewishHoliday( HebrewCalendar.SHEVAT,   1, 1, "Rosh Chodesh Shevat" );
	private static final JewishHoliday ROSH_CHODESH_ADAR = new JewishHoliday( HebrewCalendar.SHEVAT,    30, 2, "Rosh Chodesh Adar" );
	private static final JewishHoliday ROSH_CHODESH_ADAR_I = new JewishHoliday( HebrewCalendar.SHEVAT,  30, 2, "Rosh Chodesh Adar I" );
	private static final JewishHoliday ROSH_CHODESH_ADAR_II = new JewishHoliday( HebrewCalendar.ADAR_1, 30, 2, "Rosh Chodesh Adar II" );

	/**
	 * We treat Rosh Chodesh as a holiday too.
	 */
	public static final JewishHoliday[]                       ROSH_CHODESH_HASER = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_TISHRI,    ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,    ROSH_CHODESH_TEVET,     ROSH_CHODESH_SHEVAT,
	};

	public static final JewishHoliday[]                       ROSH_CHODESH_QESIDRAH = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_TISHRI,    ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,
		new JewishHoliday( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	public static final JewishHoliday[]                       ROSH_CHODESH_SHALEM = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_TISHRI,    ROSH_CHODESH_HESHVAN,
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

	
	public final String                                       name;
	
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


	public static JewishHoliday createErevOf(
		JewishHoliday holiday
	) {

		return new JewishHoliday( holiday.month, holiday.date, 1, "Erev " + holiday.name );
	}


	public boolean isOn(
		HebrewCalendar dateToTest
	) {

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


	@SuppressWarnings( { "FeatureEnvy" } )
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


	private static class TaanitEsther
		extends JewishHoliday {

		TaanitEsther() {
			super( HebrewCalendar.ADAR, 13, "Fast of Esther" );
		}


		@SuppressWarnings( { "FeatureEnvy" } )
		@Override
		public boolean isOn( HebrewCalendar dateToTest ) {

			// Not Adar? Then it can't be ta'anit Esther!
			if ( HebrewCalendar.ADAR != dateToTest.get( HebrewCalendar.MONTH ) ) {
				return false;
			}

			int dateToUse = getDateToUse( dateToTest );

			calendar.set( HebrewCalendar.DATE, dateToUse );
			return isSameDate( dateToTest, calendar );
		}


		@Override
		public boolean isErev( HebrewCalendar dateToTest ) {

			// Not Adar? Then it can't be ta'anit Esther!
			if ( HebrewCalendar.ADAR != dateToTest.get( HebrewCalendar.MONTH ) ) {
				return false;
			}

			int dateToUse = getDateToUse( dateToTest );

			calendar.set( HebrewCalendar.DATE, dateToUse );
			calendar.add( HebrewCalendar.DATE, -1 );
			
			return isSameDate( dateToTest, calendar );
		}


		@SuppressWarnings( { "FeatureEnvy" } )
		private int getDateToUse(
			HebrewCalendar dateToTest
		) {

			calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );

			int dateToUse = date;

			// Now, if the fast is supposed to fall out on a Friday or Shabbat, move it back to Thursday.
			final int dayOfWeek = calendar.get( HebrewCalendar.DAY_OF_WEEK );
			if ( HebrewCalendar.FRIDAY == dayOfWeek ) {
				dateToUse--;
			}
			else if ( HebrewCalendar.SATURDAY == dayOfWeek ) {
				dateToUse -= 2;
			}
			return dateToUse;
		}
	} // End of TaanitEsther class


	private static class PurimKatan
		extends JewishHoliday {

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


}  // End of JewishHoliday class
