/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.holiday.minor.*;
import com.gindin.zmanim.calendar.holiday.modern.*;
import com.gindin.zmanim.calendar.holiday.rabbanan.*;
import com.gindin.zmanim.calendar.holiday.taaniyot.*;
import com.gindin.zmanim.calendar.holiday.yomtov.*;

/**
 * Serves as the central location for declaring all the Jewish Holidays.
 */
public class Holidays {

	// Yom Tov
	private static final Pesach                               PESACH = new Pesach();
	private static final RoshHashana                          ROSH_HASHANA = new RoshHashana();
	private static final Shavuot                              SHAVUOT = new Shavuot();
	private static final Succot                               SUCCOT = new Succot();
	private static final YomKippur                            YOM_KIPPUR = new YomKippur();

	// Rabanic Holidays
	private static final Chanukah                             CHANUKAH = new Chanukah();
	private static final Purim                                PURIM = new Purim();
	private static final ShushanPurim                         SHUSHAN_PURIM = new ShushanPurim();

	// Minor
	private static final LagBOmer                             LAG_B_OMER = new LagBOmer();
	private static final Omer                                 OMER = new Omer();
	private static final PesachSheni                          PESACH_SHENI = new PesachSheni();
	private static final TuBAv                                TU_B_AV = new TuBAv();
	private static final TuBShvat                             TU_B_SHVAT = new TuBShvat();

	// Fast Days
	private static final AsaraBTevet                          ASARA_B_TEVET = new AsaraBTevet();
	private static final Taanit17Tamuz                        TAANIT_17_TAMUZ = new Taanit17Tamuz();
	private static final TaanitBechorot                       TAANIT_BECHOROT = new TaanitBechorot();
	private static final TaanitEsther                         TAANIT_ESTHER = new TaanitEsther();
	private static final TishaBAv                             TISHA_B_AV = new TishaBAv();
	private static final TzomGedaliah                         TZOM_GEDALIAH = new TzomGedaliah();

	// Modern holidays
	private static final YomHaAtzmaut                         YOM_HA_ATZMAUT = new YomHaAtzmaut();
	private static final YomHaShoah                           YOM_HA_SHOAH = new YomHaShoah();
	private static final YomHaZikaron                         YOM_HA_ZIKARON = new YomHaZikaron();
	private static final YomYerushalayim                      YOM_YERUSHALAYIM = new YomYerushalayim();

	// Handles all of the Rosh Chodeshim
	private static final RoshChodesh                          ROSH_CHODESH = new RoshChodesh();

	/**
	 * Sets of the holidays.
	 */
	public static final Observance[]                          ALL = {
		PESACH,         ROSH_HASHANA,       SHAVUOT,          SUCCOT,           YOM_KIPPUR,
		CHANUKAH,       PURIM,              SHUSHAN_PURIM,
		LAG_B_OMER,     OMER,               PESACH_SHENI,     TU_B_AV,          TU_B_SHVAT,
		ASARA_B_TEVET,  TAANIT_17_TAMUZ,    TAANIT_BECHOROT,  TAANIT_ESTHER,    TISHA_B_AV,   TZOM_GEDALIAH,
		YOM_HA_ATZMAUT, YOM_HA_SHOAH,       YOM_HA_ZIKARON,   YOM_YERUSHALAYIM,
		ROSH_CHODESH
	};




	/** Not instantiatable. */
	private Holidays() {
	}
}  // End of Holidays class
