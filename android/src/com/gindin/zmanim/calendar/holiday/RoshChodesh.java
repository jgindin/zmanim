/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Specialized for Rosh Chodesh
 */
public class RoshChodesh
	extends MultiDayObservance {

	private static final RoshChodesh ROSH_CHODESH_NISAN   = new RoshChodesh( HebrewCalendar.NISAN,   1, 1, "Nisan" );
	private static final RoshChodesh ROSH_CHODESH_IYAR    = new RoshChodesh( HebrewCalendar.NISAN,  30, 2, "Iyar" );
	private static final RoshChodesh ROSH_CHODESH_SIVAN   = new RoshChodesh( HebrewCalendar.SIVAN,   1, 1, "Sivan" );
	private static final RoshChodesh ROSH_CHODESH_TAMMUZ  = new RoshChodesh( HebrewCalendar.SIVAN,  30, 2, "Tammuz" );
	private static final RoshChodesh ROSH_CHODESH_AV      = new RoshChodesh( HebrewCalendar.AV,      1, 1, "Av" );
	private static final RoshChodesh ROSH_CHODESH_ELUL    = new RoshChodesh( HebrewCalendar.AV,     30, 2, "Elul" );
	// Don't bother...this is Rosh Hashana...
	//private static final RoshChodesh ROSH_CHODESH_TISHRI  = new RoshChodesh( HebrewCalendar.ELUL,   30, 2, "Tishri" );
	private static final RoshChodesh ROSH_CHODESH_HESHVAN = new RoshChodesh( HebrewCalendar.HESHVAN, 1, 1, "Heshvan" );
	private static final RoshChodesh ROSH_CHODESH_KISLEV  = new RoshChodesh( HebrewCalendar.KISLEV,  1, 1, "Kislev" );
	private static final RoshChodesh ROSH_CHODESH_TEVET   = new RoshChodesh( HebrewCalendar.TEVET,   1, 1, "Tevet" );
	private static final RoshChodesh ROSH_CHODESH_SHEVAT  = new RoshChodesh( HebrewCalendar.SHEVAT,  1, 1, "Shevat" );
	private static final RoshChodesh ROSH_CHODESH_ADAR    = new RoshChodesh( HebrewCalendar.SHEVAT, 30, 2, "Adar" );
	private static final RoshChodesh ROSH_CHODESH_ADAR_I  = new RoshChodesh( HebrewCalendar.SHEVAT, 30, 2, "Adar I" );
	private static final RoshChodesh ROSH_CHODESH_ADAR_II = new RoshChodesh( HebrewCalendar.ADAR_1, 30, 2, "Adar II" );

	/**
	 * We treat Rosh Chodesh as a holiday too.
	 */
	public static final RoshChodesh[]                         ROSH_CHODESH_HASER = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,    ROSH_CHODESH_TEVET,     ROSH_CHODESH_SHEVAT,
	};

	public static final RoshChodesh[]                         ROSH_CHODESH_QESIDRAH = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,
		new RoshChodesh( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	public static final RoshChodesh[]                         ROSH_CHODESH_SHALEM = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_HESHVAN,
		new RoshChodesh( HebrewCalendar.HESHVAN, 30, 2, "Rosh Chodesh Kislev" ),  // Add a day compared to Qesidrah
		new RoshChodesh( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	public static final RoshChodesh[]                         ROSH_CHODESH_ADAR_REGULAR = {
		ROSH_CHODESH_ADAR,
	};

	public static final RoshChodesh[]                         ROSH_CHODESH_ADAR_LEAP = {
		ROSH_CHODESH_ADAR_I,    ROSH_CHODESH_ADAR_II
	};


	RoshChodesh(
		int month,
		int date,
		int length,
		String name
	) {

		super( month, date, length, "Rosh Chodesh " + name, null );
	}


	@Override
	public RoshChodesh isRoshChodesh() {

		// By definition!
		return this;
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar forDate ) {
		// No need for a separate instance
		return this;
	}

} // End of RoshChodesh class
