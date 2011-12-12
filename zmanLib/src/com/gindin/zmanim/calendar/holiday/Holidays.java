/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Serves as the central location for declaring all the Jewish Holidays.
 */
public class Holidays {

	// Nissan
	private static final JewishDay PESACH           = new Pesach();
	private static final JewishDay YOM_HASHOAH      = new FixedDateObservance( HebrewCalendar.NISAN, 27, "Yom Hashoah" );

	// Iyar
	private static final JewishDay YOM_HAZIKARON    = new YomHaZikaron();
	private static final JewishDay YOM_HAATZMAUT    = new YomHaAtzmaut();
	private static final JewishDay PESACH_SHEINI    = new FixedDateObservance( HebrewCalendar.IYAR, 14, "Pesach Sheini" );
	private static final JewishDay LAG_BOMER        = new FixedDateObservance( HebrewCalendar.IYAR, 18, "Lag B'Omer" );
	private static final JewishDay YOM_YERUSHALAYIM = new FixedDateObservance( HebrewCalendar.IYAR, 28, "Yom Yerushalayim" );

	// Sivan
	private static final JewishDay SHAVUOT          = new Shavuot();

	// Tamuz

	// Av
	private static final JewishDay TU_BAV           = new FixedDateObservance( HebrewCalendar.AV, 15, "Tu B'Av" );

	// Elul

	// Tishri
	private static final JewishDay ROSH_HASHANAH    = new RoshHashana();
	private static final JewishDay YOM_KIPPUR       = new YomKippur();
	private static final JewishDay SUKKOT           = new Sukkot();
	private static final JewishDay HOSHANAH_RABBAH  = new HoshanahRabbah();
	private static final JewishDay SHEMINI_ATZERET  = new SheminiAtzeret();
	private static final JewishDay SIMCHAT_TORAH    = new SimchatTorah();

	// Heshvan

	// Kislev
	private static final JewishDay HANUKKAH         = new Hanukkah();

	// Tevet

	// Shevat
	private static final JewishDay TU_BSHEVAT       = new FixedDateObservance( HebrewCalendar.SHEVAT, 15, "Tu B'Shevat" );

	// Adar
	private static final JewishDay PURIM_KATAN      = new PurimKatan();
	private static final JewishDay PURIM            = new FixedDateObservance( HebrewCalendar.ADAR, 14, "Purim" );
	private static final JewishDay SHUSHAN_PURIM    = new FixedDateObservance( HebrewCalendar.ADAR, 15, "Shushan Purim" );


	/**
	 * Set of the holidays.
	 */
	public static final JewishDay[] JEWISH_HOLIDAYS = {
		ROSH_HASHANAH,          YOM_KIPPUR,       SUKKOT,
		HOSHANAH_RABBAH,        SHEMINI_ATZERET,  SIMCHAT_TORAH,
		HANUKKAH,               TU_BSHEVAT,       PURIM_KATAN,
		PURIM,                  SHUSHAN_PURIM,    PESACH,
		YOM_HASHOAH,            YOM_HAZIKARON,    YOM_HAATZMAUT,
		PESACH_SHEINI,          LAG_BOMER,        YOM_YERUSHALAYIM,
		SHAVUOT,                TU_BAV
	};


	/** Not instantiatable. */
	private Holidays() {
	}


}  // End of Holidays class
