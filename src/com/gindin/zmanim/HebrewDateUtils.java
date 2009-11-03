package com.gindin.zmanim;

import il.ac.technion.math.Taarich;
import net.sourceforge.zmanim.ZmanimCalendar;

import java.util.Calendar;

/**
 * Given the hebrew date, figure out an appropriate messsage.
 * <p/>
 * See http://hebrewcalendar.tripod.com/ for tons more info...may have to write my own HY <--> GY converter.
 * <p/>
 * From http://www.jewishvirtuallibrary.org/jsource/Judaism/calendar.html: The Jewish calendar has the following months:
 * Month     Length          Gregorian Equivalent
 * =======   =============   ====================
 * Nissan 	  30 days         March-April
 * Iyar 	    29 days 	      April-May
 * Sivan 	    30 days 	      May-June
 * Tammuz 	  29 days 	      June-July
 * Av         30 days 	      July-August
 * Elul 	    29 days 	      August-September
 * Tishri 	  30 days         September-October
 * Heshvan    29 or 30 days 	October-November
 * Kislev 	  30 or 29 days 	November-December
 * Tevet 	    29 days         December-January
 * Shevat 	  30 days 	      January-February
 * Adar 	    29 or 30 days 	February-March
 * Adar II    29 days         March-April
 * <p/>
 * In leap years, Adar has 30 days. In non-leap years, Adar has 29 days.
 */
@SuppressWarnings( { "MagicNumber" } )
public class HebrewDateUtils {

	private static final int EREV_ROSH_CHODESH = 29;

	private static final int ROSH_CHODESH = 1;

	private static final int ROSH_CHODESH_1 = 30;


	/** Dates in Nisan */
	private static final int EREV_PESACH = 14;

	private static final int PESACH_1 = 15;

	private static final int PESACH_2 = 16;

	private static final int PESACH_3 = 17;

	private static final int PESACH_4 = 18;

	private static final int PESACH_5 = 19;

	private static final int PESACH_6 = 20;

	private static final int PESACH_7 = 21;

	private static final int PESACH_8 = 22;

	private static final int PESACH_SHENI = 14;

	/** Dates in Iyyar */
	private static final int LAG_BA_OMER = 18;

	/** Sivan */
	private static final int EREV_SHAVUOT = 5;

	private static final int SHAVUOT_1 = 6;

	private static final int SHAVUOT_2 = 7;

	/** Tammuz */
	private static final int FAST_OF_TAMUZ = 17;

	/** Av */
	private static final int TISHA_B_AV = 9;

	private static final int TU_B_AV = 15;

	/** Elul */
	private static final int EREV_ROSH_HASHANA = 29;

	/** Tishri */
	private static final int ROSH_HASHANA_1 = 1;

	private static final int ROSH_HASHANA_2 = 2;

	private static final int FAST_OF_GEDALIA = 3;

	private static final int EREV_YOM_KIPPUR = 9;

	private static final int YOM_KIPPUR = 10;

	private static final int EREV_SUKKOT = 14;

	private static final int SUKKOT_1 = 15;

	private static final int SUKKOT_2 = 16;

	private static final int SUKKOT_3 = 17;

	private static final int SUKKOT_4 = 18;

	private static final int SUKKOT_5 = 19;

	private static final int SUKKOT_6 = 20;

	private static final int SUKKOT_7 = 21;

	private static final int SHEMINI_ATZERET = 22;

	private static final int SIMCHAT_TORAH = 23;

	/** Kislev */
	private static final int EREV_CHANUKAH = 24;

	private static final int CHANUKAH_1 = 25;

	private static final int CHANUKAH_2 = 26;

	private static final int CHANUKAH_3 = 27;

	private static final int CHANUKAH_4 = 28;

	private static final int CHANUKAH_5 = 29;

	private static final int CHANUKAH_6_FULL_KISLEV = 30;

	/** Tevet */
	private static final int CHANUKAH_7_FULL_KISLEV = 1;

	private static final int CHANUKAH_8_FULL_KISLEV = 2;

	private static final int FAST_OF_TEVET = 10;

	/** Shevat */
	private static final int TU_B_SHVAT = 15;

	/** Adar */
	private static final int TAANIT_ESTHER_NORMAL = 13;

	private static final int PURIM_NORMAL = 14;

	private static final int SHUSHAN_PURIM = 15;


	private final ZmanimCalendar zmanimCalendar;

	private final Taarich taarich;


	public HebrewDateUtils(
		ZmanimCalendar zmanimCalendar,
		Taarich taarich
	) {
		this.zmanimCalendar = zmanimCalendar;
		this.taarich = taarich;
	}


	public boolean showCandlelighting() {

		Calendar secularCalendar = zmanimCalendar.getCalendar();
		if ( Calendar.FRIDAY == secularCalendar.get( Calendar.DAY_OF_MONTH ) ) {
			return true;
		}

		final int dayOfMonth = taarich.getHebrewDayOfMonth();

		switch ( taarich.getHebrewMonth() ) {
			case NISAN:
				return ( EREV_PESACH == dayOfMonth ) || ( PESACH_1 == dayOfMonth ) || ( PESACH_6 == dayOfMonth ) || ( PESACH_7 == dayOfMonth );

			case SIVAN:
				return ( EREV_SHAVUOT == dayOfMonth ) || ( SHAVUOT_1 == dayOfMonth );

			case ELUL:
				return ( EREV_ROSH_HASHANA == dayOfMonth );

			case TISHRI:
				return ( ROSH_HASHANA_1 == dayOfMonth ) || ( EREV_YOM_KIPPUR == dayOfMonth ) ||
					( EREV_SUKKOT == dayOfMonth ) || ( SUKKOT_1 == dayOfMonth ) || ( SUKKOT_7 == dayOfMonth ) || ( SHEMINI_ATZERET == dayOfMonth );
		}

		return false;
	}


	public boolean showHavdalah() {

		Calendar secularCalendar = zmanimCalendar.getCalendar();
		if ( Calendar.SATURDAY == secularCalendar.get( Calendar.DAY_OF_MONTH ) ) {
			return true;
		}

		final int dayOfMonth = taarich.getHebrewDayOfMonth();

		switch ( taarich.getHebrewMonth() ) {
			case NISAN:
				return ( PESACH_2 == dayOfMonth ) || ( PESACH_8 == dayOfMonth );

			case SIVAN:
				return ( SHAVUOT_2 == dayOfMonth );

			case TISHRI:
				return ( ROSH_HASHANA_2 == dayOfMonth ) || ( YOM_KIPPUR == dayOfMonth ) ||
					( SUKKOT_2 == dayOfMonth ) || ( SIMCHAT_TORAH == dayOfMonth );
		}

		return false;
	}


	public String getTodaysHoliday(
	) {

		switch ( taarich.getHebrewMonth() ) {
			case NISAN:
				return todayInNisan();

			case IYYAR:
				return todayInIyyar();

			case SIVAN:
				return todayInSivan();

			case TAMMUZ:
				return todayInTammuz();

			case AV:
				return todayInAv();

			case ELUL:
				return todayInElul();

			case TISHRI:
				return todayInTishri();

			case HESHVAN:
				return todayInHeshvan();

			case KISLEV:
				return todayInKislev();

			case TEVET:
				return todayInTevet();

			case SHEVAT:
				return todayInShevat();

			case ADAR:
				return todayInAdar();

			case ADAR_I:
				return todayInAdarAlef();

			case ADAR_II:
				return todayInAdarBet();
		}

		return "";
	}


	private String todayInNisan(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case EREV_PESACH:
				return "Taanich Bechorot";

			case PESACH_1:
				return "Pesach 1";

			case PESACH_2:
				return "Pesach 2";

			case PESACH_3:
				return "Pesach 3";

			case PESACH_4:
				return "Pesach 4";

			case PESACH_5:
				return "Pesach 5";

			case PESACH_6:
				return "Pesach 6";

			case PESACH_7:
				return "Pesach 7";

			case PESACH_8:
				return "Isru Chag";

			case ROSH_CHODESH_1:
				return "Rosh Chodesh";
		}

		return "";
	}


	private String todayInIyyar(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case PESACH_SHENI:
				return "Pesach Sheni";

			case LAG_BA_OMER:
				return "Lag Ba'Omer";
		}

		return "";
	}


	private String todayInSivan(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case EREV_SHAVUOT:
				return "Erev Shavuot";

			case SHAVUOT_1:
				return "Shavuot";

			case SHAVUOT_2:
				return "Isru Chag";

			case EREV_ROSH_CHODESH:
				return "Rosh Chodesh";

			case ROSH_CHODESH_1:
				return "Rosh Chodesh Tammuz";
		}

		return "";
	}


	private String todayInTammuz(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh Tammuz";

			case FAST_OF_TAMUZ:
				return "Fast of Tammuz";
		}

		return "";
	}


	private String todayInAv(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case TISHA_B_AV:
				return "Tisha B'Av";

			case TU_B_AV:
				return "Tu B'Av";

			case EREV_ROSH_CHODESH:
				return "Rosh Chodesh";

			case ROSH_CHODESH_1:
				return "Rosh Chodesh Elul";
		}

		return "";
	}


	private String todayInElul(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case EREV_ROSH_HASHANA:
				return "Erev Rosh Hashana";
		}

		return "";
	}


	private String todayInTishri(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_HASHANA_1:
			case ROSH_HASHANA_2:
				return "Rosh Hashana";

			case FAST_OF_GEDALIA:
				return "Fast of Gedalia";

			case EREV_YOM_KIPPUR:
				return "Erev Yom Kippur";

			case YOM_KIPPUR:
				return "Yom Kippur";

			case EREV_SUKKOT:
				return "Erev Sukkot";

			case SUKKOT_1:
				return "Sukkot 1";

			case SUKKOT_2:
				return "Sukkot 2";

			case SUKKOT_3:
				return "Sukkot 3";

			case SUKKOT_4:
				return "Sukkot 4";

			case SUKKOT_5:
				return "Sukkot 5";

			case SUKKOT_6:
				return "Sukkot 6";

			case SUKKOT_7:
				return "Sukkot 7 / Hoshana Rabba";

			case SHEMINI_ATZERET:
				return "Shemini Atzeret";

			case SIMCHAT_TORAH:
				return "Simchat Torah";

			case EREV_ROSH_CHODESH:
				return "Rosh Chodesh";

			case ROSH_CHODESH_1:
				return "Rosh Chodesh Heshvan";
		}

		return "";
	}


	private String todayInHeshvan(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case EREV_ROSH_CHODESH:
				return "Rosh Chodesh";

			// TODO: Heshvan can be 29 OR 30 days, depending on the year... If it's 29, then this won't happen.
			case ROSH_CHODESH_1:
				return "Rosh Chodesh Kislev";
		}

		return "";
	}


	private String todayInKislev(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case EREV_CHANUKAH:
				return "Erev Chanukah";

			case CHANUKAH_1:
				return "Chanukah 1";

			case CHANUKAH_2:
				return "Chanukah 2";

			case CHANUKAH_3:
				return "Chanukah 3";

			case CHANUKAH_4:
				return "Chanukah 4";

			case CHANUKAH_5:
				return "Chanukah 5 (Erev Rosh Chodesh)";

			// TODO: Kislev can be 29 or 30 days :-( 
			case CHANUKAH_6_FULL_KISLEV:
				return "Chanukah 6";
		}

		return "";
	}


	private String todayInTevet(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {

			// TODO: Kislev can be 29 or 30 days. If it was 30, then this is correct.
			case CHANUKAH_7_FULL_KISLEV:
				return "Chanukah 7 (Rosh Chodesh)";

			case CHANUKAH_8_FULL_KISLEV:
				return "Chanukah 8";

			case FAST_OF_TEVET:
				return "Fast of Tevet";

			case EREV_ROSH_CHODESH:
				return "Erev Rosh Chodesh";
		}

		return "";
	}


	private String todayInShevat(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case TU_B_SHVAT:
				return "Tu B'Shvat";

			case EREV_ROSH_CHODESH:
				return "Erev Rosh Chodesh";
		}

		return "";
	}


	private String todayInAdar(
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case TAANIT_ESTHER_NORMAL:
				// TODO: This is NOT correct if it falls out on a Friday or Shabbat...
				return "Taanit Esther";

			case PURIM_NORMAL:
				// TODO: This is NOT correct if it falls out on a Shabbat...
				return "Purim";

			case SHUSHAN_PURIM:
				return "Shushan Purim";

			case EREV_ROSH_CHODESH:
				return "Erev Rosh Chodesh";

			// If Adar has only 29 days, then we won't get here...
			case ROSH_CHODESH_1:
				return "Rosh Chodesh";
		}

		return "";
	}


	private String todayInAdarAlef(
	) {
		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case EREV_ROSH_CHODESH:
				return "Erev Rosh Chodesh";
		}

		return "";
	}


	private String todayInAdarBet(
	) {
		switch ( taarich.getHebrewDayOfMonth() ) {
			case ROSH_CHODESH:
				return "Rosh Chodesh";

			case TAANIT_ESTHER_NORMAL:
				// TODO: This is NOT correct if it falls out on a Friday or Shabbat...
				return "Taanit Esther";

			case PURIM_NORMAL:
				// TODO: This is NOT correct if it falls out on a Shabbat...
				return "Purim";

			case SHUSHAN_PURIM:
				return "Shushan Purim";

			case EREV_ROSH_CHODESH:
				return "Erev Rosh Chodesh";
		}

		return "";
	}


}  // End of HebrewDateUtils class
