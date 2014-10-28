/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim;

import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.Zmanim;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Spot-checks some dates to verify the expected zmanim are present and properly calculated.
 */
@SuppressWarnings( { "MagicNumber", "AutoBoxing", "SpellCheckingInspection" } )
public class UT_Zmanim {

	private static final ZmanimLocation                       ezraBessaroth;

	private static final ZmanimLocation                       yeshivaUniviersity;

	private static final ZmanimLocation                       haRambanSynagogue;

	private static final List<Zmanim.Type>                    dailyZmanim = new ArrayList<Zmanim.Type>();
	private static final List<Zmanim.Type>                    erevShabbatZmanim = new ArrayList<Zmanim.Type>();
	private static final List<Zmanim.Type>                    shabbatZmanim = new ArrayList<Zmanim.Type>();
	private static final List<Zmanim.Type>                    fastDayZmanim = new ArrayList<Zmanim.Type>();
	private static final List<Zmanim.Type>                    erevPesachZmanim = new ArrayList<Zmanim.Type>();
	private static final List<Zmanim.Type>                    erevPesachOnShabbatZmanim = new ArrayList<Zmanim.Type>();


	static {

		ezraBessaroth = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		yeshivaUniviersity = new ZmanimLocation.Builder()
			.atLatitude( 40.851546 )
			.atLongitude( -73.928937 )
			.locationName( "Yeshiva University" )
			.inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
			.build();

		haRambanSynagogue = new ZmanimLocation.Builder()
			.atLatitude( 31.7750 )
			.atLongitude( 35.2312 )
			.locationName( "HaRamban Synagogue" )
			.inTimeZone( TimeZone.getTimeZone( "Asia/Jerusalem" ) )
			.build();

		Collections.addAll( dailyZmanim,
			Zmanim.Type.Alot,             Zmanim.Type.Talit,        Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
			Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,      Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
			Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
			Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
		);

		Collections.addAll( erevShabbatZmanim,
			Zmanim.Type.Alot,             Zmanim.Type.Talit,            Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
			Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,          Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
			Zmanim.Type.PlagHaMincha,     Zmanim.Type.CandleLighting,   Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha,
			Zmanim.Type.BeinHashmashot,   Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
		);

		Collections.addAll( shabbatZmanim,
			Zmanim.Type.Alot,             Zmanim.Type.Talit,        Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
			Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,      Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
			Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
			Zmanim.Type.TzaitHacochavim,  Zmanim.Type.ShabbatEnd,   Zmanim.Type.NightChatzot
		);

		Collections.addAll( fastDayZmanim,
			Zmanim.Type.Alot,             Zmanim.Type.Talit,            Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
			Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,          Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
			Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
			Zmanim.Type.FastEnd,          Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
		);

		Collections.addAll( erevPesachZmanim,
			Zmanim.Type.Alot,             Zmanim.Type.Talit,                  Zmanim.Type.Hanetz,             Zmanim.Type.SofZmanShema,
			Zmanim.Type.SofZmanTefila,    Zmanim.Type.SofZmanAchilatChametz,  Zmanim.Type.SofZmanBiurChametz, Zmanim.Type.Chatzot,
			Zmanim.Type.MinchaGedola,     Zmanim.Type.MinchaKetana,           Zmanim.Type.PlagHaMincha,       Zmanim.Type.CandleLighting,
			Zmanim.Type.Shkia,            Zmanim.Type.LatestMincha,           Zmanim.Type.BeinHashmashot,     Zmanim.Type.FastEnd,
			Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
		);

		Collections.addAll( erevPesachOnShabbatZmanim,
			Zmanim.Type.Alot,             Zmanim.Type.Talit,        Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
			Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,      Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
			Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
			Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
		);

	}


	/**
	 * Just a regular day....
	 */
	@Test
	public void october_19_2014() {

		final String ezraBessarothExpected =
			"Ezra Bessaroth\n" +
				"      Alot Hashachar - 120 Minutes                   :  5:33:26\n" +
				"      Alot Hashachar - 120 Minutes Zmaniyot          :  5:46:41\n" +
				"      Alot Hashachar - 16.1 deg                      :  6:02:11\n" +
				"      Alot Hashachar - 18 deg                        :  5:50:54\n" +
				"      Alot Hashachar - 19.8 deg                      :  5:40:10\n" +
				"      Alot Hashachar - 26 deg                        :  5:02:46\n" +
				"      Alot Hashachar - 60 Minutes                    :  6:33:26\n" +
				"      Alot Hashachar - 72 Minutes                    :  6:21:26\n" +
				"      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:29:23\n" +
				"      Alot Hashachar - 90 Minutes                    :  6:03:26\n" +
				"      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:13:22\n" +
				"      Alot Hashachar - 96 Minutes                    :  5:57:26\n" +
				"      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:08:02\n" +
				"       Talit/Tefilin - 10.2 deg                      :  6:37:10\n" +
				"       Talit/Tefilin - 11 deg                        :  6:32:25\n" +
				"       Talit/Tefilin - 11.5 deg                      :  6:29:27\n" +
				"              Hanetz - Sunrise                       :  7:33:26\n" +
				"              Hanetz - Sunrise (with elevation)      :  7:33:26\n" +
				"      Sof Zman Shema - 3 Hours before Chatzot        :  9:53:38\n" +
				"      Sof Zman Shema - 16.1 deg to Sunset            :  9:05:06\n" +
				"      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:14:25\n" +
				"      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:35:30\n" +
				"      Sof Zman Shema - GR'A                          : 10:13:32\n" +
				"      Sof Zman Shema - GR'A Kol Eliyahu              : 10:21:15\n" +
				"      Sof Zman Shema - MG'A                          :  9:37:32\n" +
				"      Sof Zman Shema - MG'A 120 Minutes              :  9:13:32\n" +
				"      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:55\n" +
				"      Sof Zman Shema - MG'A 19.8 deg                 :  9:16:55\n" +
				"      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:41:31\n" +
				"      Sof Zman Shema - MG'A 90 Minutes               :  9:28:32\n" +
				"      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:33:30\n" +
				"      Sof Zman Shema - 96 Minutes                    :  9:25:32\n" +
				"      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:30:50\n" +
				"     Sof Zman Tefila - 2 Hours before Chatzot        : 10:53:38\n" +
				"     Sof Zman Tefila - GR'A                          : 11:06:54\n" +
				"     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:37:32\n" +
				"     Sof Zman Tefila - MG'A                          : 10:42:54\n" +
				"     Sof Zman Tefila - MG'A 120 Minutes              : 10:26:54\n" +
				"     Sof Zman Tefila - MG'A 16.1 deg                 : 10:36:30\n" +
				"     Sof Zman Tefila - MG'A 19.8 deg                 : 10:29:10\n" +
				"     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:45:33\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes               : 10:36:54\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:40:13\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes               : 10:34:54\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:38:26\n" +
				"             Chatzot - Mid-day                       : 12:53:38\n" +
				"       Mincha Gedola - GR'A                          : 13:20:20\n" +
				"       Mincha Gedola - MG'A 16.1 deg                 : 13:27:57\n" +
				"       Mincha Gedola - MG'A 72 Minutes               : 13:26:20\n" +
				"       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:12:38\n" +
				"       Mincha Ketana - GR'A                          : 16:00:26\n" +
				"       Mincha Ketana - MG'A 16.1 deg                 : 16:53:41\n" +
				"       Mincha Ketana - MG'A 72 Minutes               : 16:42:26\n" +
				"       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:18:45\n" +
				"       Plag HaMincha - GR'A                          : 17:07:09\n" +
				"       Plag HaMincha - MG'A 120 Minutes              : 18:42:09\n" +
				"       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:31:39\n" +
				"       Plag HaMincha - MG'A 16.1 deg                 : 18:19:25\n" +
				"       Plag HaMincha - MG'A 18 deg                   : 18:28:21\n" +
				"       Plag HaMincha - MG'A 19.8 deg                 : 18:36:50\n" +
				"       Plag HaMincha - MG'A 26 deg                   : 19:06:25\n" +
				"       Plag HaMincha - MG'A 60 Minutes               : 17:54:39\n" +
				"       Plag HaMincha - MG'A 72 Minutes               : 18:04:09\n" +
				"       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:57:51\n" +
				"       Plag HaMincha - MG'A 90 Minutes               : 18:18:24\n" +
				"       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:10:31\n" +
				"       Plag HaMincha - MG'A 96 Minutes               : 18:23:09\n" +
				"       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:14:45\n" +
				"       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:36:18\n" +
				"               Shkia - Sunset                        : 18:13:51\n" +
				"       Latest Mincha - Mishna Berura                 : 18:13:51\n" +
				"       Latest Mincha - Hacham Ovadia Yosef           : 18:25:52\n" +
				"       Latest Mincha - Hacham Ben Sion               : 18:39:51\n" +
				"     Bein Hashmashot - 13 Degrees                    : 19:28:11\n" +
				"     Bein Hashmashot - 58.5 Minutes                  : 19:12:21\n" +
				"     Bein Hashmashot - 13.5 Minutes                  : 18:37:35\n" +
				"     Bein Hashmashot - 2 Stars                       : 18:45:19\n" +
				"    Tzait Hacochavim - MG'A 120 Minutes              : 20:13:51\n" +
				"    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:00:36\n" +
				"    Tzait Hacochavim - 3.5 deg                       : 18:30:04\n" +
				"    Tzait Hacochavim - 6.5 deg                       : 18:48:06\n" +
				"    Tzait Hacochavim - 8.5 deg                       : 19:00:02\n" +
				"    Tzait Hacochavim - 16.1 deg                      : 19:45:08\n" +
				"    Tzait Hacochavim - 18 deg                        : 19:56:25\n" +
				"    Tzait Hacochavim - 19.8 deg                      : 20:07:09\n" +
				"    Tzait Hacochavim - 26 deg                        : 20:44:31\n" +
				"    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:13:51\n" +
				"    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:25:51\n" +
				"    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:17:54\n" +
				"    Tzait Hacochavim - MG'A 90 Minutes               : 19:43:51\n" +
				"    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:33:55\n" +
				"    Tzait Hacochavim - 96 Minutes                    : 19:49:51\n" +
				"    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:39:15\n" +
				"    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:53:51\n" +
				"    Tzait Hacochavim - Geonim 3.65 deg               : 18:30:58\n" +
				"    Tzait Hacochavim - Geonim 4.37 deg               : 18:35:19\n" +
				"    Tzait Hacochavim - Geonim 4.61 deg               : 18:36:45\n" +
				"    Tzait Hacochavim - Geonim 4.8 deg                : 18:37:54\n" +
				"    Tzait Hacochavim - Geonim 5.88 deg               : 18:44:23\n" +
				"    Tzait Hacochavim - Geonim 5.95 deg               : 18:44:48\n" +
				"    Tzait Hacochavim - Geonim 7.083 deg              : 18:51:05\n" +
				"    Tzait Hacochavim - Geonim 8.5 deg                : 19:00:02\n" +
				"    Tzait Hacochavim - Seattle - SBH                 : 18:55:51\n" +
				"    Tzait Hacochavim - Seattle - EB                  : 18:58:51\n" +
				"            Midnight - Mid-night                     :  0:54:22\n";

		final String yeshivaUniversityExpected =
			"Yeshiva University\n" +
				"      Alot Hashachar - 120 Minutes                   :  2:11:08\n" +
				"      Alot Hashachar - 120 Minutes Zmaniyot          :  2:21:23\n" +
				"      Alot Hashachar - 16.1 deg                      :  2:49:48\n" +
				"      Alot Hashachar - 18 deg                        :  2:39:44\n" +
				"      Alot Hashachar - 19.8 deg                      :  2:30:12\n" +
				"      Alot Hashachar - 26 deg                        :  1:57:11\n" +
				"      Alot Hashachar - 60 Minutes                    :  3:11:08\n" +
				"      Alot Hashachar - 72 Minutes                    :  2:59:08\n" +
				"      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  3:05:17\n" +
				"      Alot Hashachar - 90 Minutes                    :  2:41:08\n" +
				"      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  2:48:49\n" +
				"      Alot Hashachar - 96 Minutes                    :  2:35:08\n" +
				"      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  2:43:20\n" +
				"       Talit/Tefilin - 10.2 deg                      :  3:21:01\n" +
				"       Talit/Tefilin - 11 deg                        :  3:16:47\n" +
				"       Talit/Tefilin - 11.5 deg                      :  3:14:08\n" +
				"              Hanetz - Sunrise                       :  4:11:08\n" +
				"              Hanetz - Sunrise (with elevation)      :  4:11:08\n" +
				"      Sof Zman Shema - 3 Hours before Chatzot        :  6:40:24\n" +
				"      Sof Zman Shema - 16.1 deg to Sunset            :  5:54:46\n" +
				"      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  6:03:03\n" +
				"      Sof Zman Shema - Chacham Yosef Harari-Raful    :  6:16:23\n" +
				"      Sof Zman Shema - GR'A                          :  6:55:46\n" +
				"      Sof Zman Shema - GR'A Kol Eliyahu              :  7:03:25\n" +
				"      Sof Zman Shema - MG'A                          :  6:19:46\n" +
				"      Sof Zman Shema - MG'A 120 Minutes              :  5:55:46\n" +
				"      Sof Zman Shema - MG'A 16.1 deg                 :  6:15:06\n" +
				"      Sof Zman Shema - MG'A 19.8 deg                 :  6:05:19\n" +
				"      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  6:22:50\n" +
				"      Sof Zman Shema - MG'A 90 Minutes               :  6:10:46\n" +
				"      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  6:14:36\n" +
				"      Sof Zman Shema - 96 Minutes                    :  6:07:46\n" +
				"      Sof Zman Shema - 96 Minutes Zmaniyot           :  6:11:52\n" +
				"     Sof Zman Tefila - 2 Hours before Chatzot        :  7:40:24\n" +
				"     Sof Zman Tefila - GR'A                          :  7:50:39\n" +
				"     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  7:20:05\n" +
				"     Sof Zman Tefila - MG'A                          :  7:26:39\n" +
				"     Sof Zman Tefila - MG'A 120 Minutes              :  7:10:39\n" +
				"     Sof Zman Tefila - MG'A 16.1 deg                 :  7:23:33\n" +
				"     Sof Zman Tefila - MG'A 19.8 deg                 :  7:17:01\n" +
				"     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      :  7:28:42\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes               :  7:20:39\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      :  7:23:12\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes               :  7:18:39\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      :  7:21:23\n" +
				"             Chatzot - Mid-day                       :  9:40:24\n" +
				"       Mincha Gedola - GR'A                          : 10:07:50\n" +
				"       Mincha Gedola - MG'A 16.1 deg                 : 10:14:38\n" +
				"       Mincha Gedola - MG'A 72 Minutes               : 10:13:50\n" +
				"       Mincha Gedola - Chacham Yosef Harari-Raful    :  9:59:19\n" +
				"       Mincha Ketana - GR'A                          : 12:52:28\n" +
				"       Mincha Ketana - MG'A 16.1 deg                 : 13:39:57\n" +
				"       Mincha Ketana - MG'A 72 Minutes               : 13:34:28\n" +
				"       Mincha Ketana - Chacham Yosef Harari-Raful    : 13:10:25\n" +
				"       Plag HaMincha - GR'A                          : 14:01:04\n" +
				"       Plag HaMincha - MG'A 120 Minutes              : 15:36:04\n" +
				"       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 15:27:58\n" +
				"       Plag HaMincha - MG'A 16.1 deg                 : 15:05:30\n" +
				"       Plag HaMincha - MG'A 18 deg                   : 15:13:27\n" +
				"       Plag HaMincha - MG'A 19.8 deg                 : 15:21:00\n" +
				"       Plag HaMincha - MG'A 26 deg                   : 15:47:08\n" +
				"       Plag HaMincha - MG'A 60 Minutes               : 14:48:34\n" +
				"       Plag HaMincha - MG'A 72 Minutes               : 14:58:04\n" +
				"       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 14:53:12\n" +
				"       Plag HaMincha - MG'A 90 Minutes               : 15:12:19\n" +
				"       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 15:06:14\n" +
				"       Plag HaMincha - MG'A 96 Minutes               : 15:17:04\n" +
				"       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 15:10:35\n" +
				"       Plag HaMincha - Chacham Yosef Harari-Raful    : 14:30:03\n" +
				"               Shkia - Sunset                        : 15:09:40\n" +
				"       Latest Mincha - Mishna Berura                 : 15:09:40\n" +
				"       Latest Mincha - Hacham Ovadia Yosef           : 15:22:01\n" +
				"       Latest Mincha - Hacham Ben Sion               : 15:35:40\n" +
				"     Bein Hashmashot - 13 Degrees                    : 16:15:55\n" +
				"     Bein Hashmashot - 58.5 Minutes                  : 16:08:10\n" +
				"     Bein Hashmashot - 13.5 Minutes                  : 15:29:18\n" +
				"     Bein Hashmashot - 2 Stars                       : 15:37:42\n" +
				"    Tzait Hacochavim - MG'A 120 Minutes              : 17:09:40\n" +
				"    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 16:59:26\n" +
				"    Tzait Hacochavim - 3.5 deg                       : 15:24:04\n" +
				"    Tzait Hacochavim - 6.5 deg                       : 15:40:08\n" +
				"    Tzait Hacochavim - 8.5 deg                       : 15:50:47\n" +
				"    Tzait Hacochavim - 16.1 deg                      : 16:31:03\n" +
				"    Tzait Hacochavim - 18 deg                        : 16:41:06\n" +
				"    Tzait Hacochavim - 19.8 deg                      : 16:50:38\n" +
				"    Tzait Hacochavim - 26 deg                        : 17:23:38\n" +
				"    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 16:09:40\n" +
				"    Tzait Hacochavim - Rambam / Rabainu Tam          : 16:21:40\n" +
				"    Tzait Hacochavim - 72 Minutes Zmaniyot           : 16:15:31\n" +
				"    Tzait Hacochavim - MG'A 90 Minutes               : 16:39:40\n" +
				"    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 16:31:59\n" +
				"    Tzait Hacochavim - 96 Minutes                    : 16:45:40\n" +
				"    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 16:37:28\n" +
				"    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 15:49:40\n" +
				"    Tzait Hacochavim - Geonim 3.65 deg               : 15:24:52\n" +
				"    Tzait Hacochavim - Geonim 4.37 deg               : 15:28:44\n" +
				"    Tzait Hacochavim - Geonim 4.61 deg               : 15:30:01\n" +
				"    Tzait Hacochavim - Geonim 4.8 deg                : 15:31:02\n" +
				"    Tzait Hacochavim - Geonim 5.88 deg               : 15:36:49\n" +
				"    Tzait Hacochavim - Geonim 5.95 deg               : 15:37:12\n" +
				"    Tzait Hacochavim - Geonim 7.083 deg              : 15:42:48\n" +
				"    Tzait Hacochavim - Geonim 8.5 deg                : 15:50:47\n" +
				"    Tzait Hacochavim - Seattle - SBH                 : 15:51:40\n" +
				"    Tzait Hacochavim - Seattle - EB                  : 15:54:40\n" +
				"            Midnight - Mid-night                     : 21:40:57\n";

		final String haRambanSynagogueExpected =
			"HaRamban Synagogue\n" +
				"      Alot Hashachar - 120 Minutes                   : 18:44:51\n" +
				"      Alot Hashachar - 120 Minutes Zmaniyot          : 18:51:49\n" +
				"      Alot Hashachar - 16.1 deg                      : 19:32:29\n" +
				"      Alot Hashachar - 18 deg                        : 19:23:32\n" +
				"      Alot Hashachar - 19.8 deg                      : 19:15:04\n" +
				"      Alot Hashachar - 26 deg                        : 18:45:52\n" +
				"      Alot Hashachar - 60 Minutes                    : 19:44:51\n" +
				"      Alot Hashachar - 72 Minutes                    : 19:32:51\n" +
				"      Alot Hashachar - GR'A 72 Minutes Zmaniyot      : 19:37:02\n" +
				"      Alot Hashachar - 90 Minutes                    : 19:14:51\n" +
				"      Alot Hashachar - GR'A 90 Minutes Zmaniyot      : 19:20:05\n" +
				"      Alot Hashachar - 96 Minutes                    : 19:08:51\n" +
				"      Alot Hashachar - GR'A 96 Minutes Zmaniyot      : 19:14:26\n" +
				"       Talit/Tefilin - 10.2 deg                      : 20:00:18\n" +
				"       Talit/Tefilin - 11 deg                        : 19:56:31\n" +
				"       Talit/Tefilin - 11.5 deg                      : 19:54:10\n" +
				"              Hanetz - Sunrise                       : 20:44:51\n" +
				"              Hanetz - Sunrise (with elevation)      : 20:44:51\n" +
				"      Sof Zman Shema - 3 Hours before Chatzot        : 23:23:54\n" +
				"      Sof Zman Shema - 16.1 deg to Sunset            : 22:40:06\n" +
				"      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   : 22:47:27\n" +
				"      Sof Zman Shema - Chacham Yosef Harari-Raful    : 22:53:31\n" +
				"      Sof Zman Shema - GR'A                          : 23:34:23\n" +
				"      Sof Zman Shema - GR'A Kol Eliyahu              : 23:41:57\n" +
				"      Sof Zman Shema - MG'A                          : 22:58:23\n" +
				"      Sof Zman Shema - MG'A 120 Minutes              : 22:34:23\n" +
				"      Sof Zman Shema - MG'A 16.1 deg                 : 22:58:12\n" +
				"      Sof Zman Shema - MG'A 19.8 deg                 : 22:49:30\n" +
				"      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      : 23:00:28\n" +
				"      Sof Zman Shema - MG'A 90 Minutes               : 22:49:23\n" +
				"      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      : 22:52:00\n" +
				"      Sof Zman Shema - 96 Minutes                    : 22:46:23\n" +
				"      Sof Zman Shema - 96 Minutes Zmaniyot           : 22:49:10\n" +
				"     Sof Zman Tefila - 2 Hours before Chatzot        :  0:23:54\n" +
				"     Sof Zman Tefila - GR'A                          :  0:30:53\n" +
				"     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 23:59:01\n" +
				"     Sof Zman Tefila - MG'A                          :  0:06:53\n" +
				"     Sof Zman Tefila - MG'A 120 Minutes              : 23:50:53\n" +
				"     Sof Zman Tefila - MG'A 16.1 deg                 :  0:06:47\n" +
				"     Sof Zman Tefila - MG'A 19.8 deg                 :  0:00:58\n" +
				"     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      :  0:08:17\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes               :  0:00:53\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      :  0:02:38\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes               : 23:58:53\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      :  0:00:45\n" +
				"             Chatzot - Mid-day                       :  2:23:54\n" +
				"       Mincha Gedola - GR'A                          :  2:52:10\n" +
				"       Mincha Gedola - MG'A 16.1 deg                 :  2:58:13\n" +
				"       Mincha Gedola - MG'A 72 Minutes               :  2:58:10\n" +
				"       Mincha Gedola - Chacham Yosef Harari-Raful    :  2:42:45\n" +
				"       Mincha Ketana - GR'A                          :  5:41:42\n" +
				"       Mincha Ketana - MG'A 16.1 deg                 :  6:23:56\n" +
				"       Mincha Ketana - MG'A 72 Minutes               :  6:23:42\n" +
				"       Mincha Ketana - Chacham Yosef Harari-Raful    :  5:59:14\n" +
				"       Plag HaMincha - GR'A                          :  6:52:20\n" +
				"       Plag HaMincha - MG'A 120 Minutes              :  8:27:20\n" +
				"       Plag HaMincha - MG'A 120 Minutes Zmaniyot     :  8:21:49\n" +
				"       Plag HaMincha - MG'A 16.1 deg                 :  7:49:39\n" +
				"       Plag HaMincha - MG'A 18 deg                   :  7:56:44\n" +
				"       Plag HaMincha - MG'A 19.8 deg                 :  8:03:26\n" +
				"       Plag HaMincha - MG'A 26 deg                   :  8:26:33\n" +
				"       Plag HaMincha - MG'A 60 Minutes               :  7:39:50\n" +
				"       Plag HaMincha - MG'A 72 Minutes               :  7:49:20\n" +
				"       Plag HaMincha - MG'A 72 Minutes Zmaniyot      :  7:46:01\n" +
				"       Plag HaMincha - MG'A 90 Minutes               :  8:03:35\n" +
				"       Plag HaMincha - MG'A 90 Minutes Zmaniyot      :  7:59:26\n" +
				"       Plag HaMincha - MG'A 96 Minutes               :  8:08:20\n" +
				"       Plag HaMincha - MG'A 96 Minutes Zmaniyot      :  8:03:55\n" +
				"       Plag HaMincha - Chacham Yosef Harari-Raful    :  7:21:06\n" +
				"               Shkia - Sunset                        :  8:02:58\n" +
				"       Latest Mincha - Mishna Berura                 :  8:02:58\n" +
				"       Latest Mincha - Hacham Ovadia Yosef           :  8:15:41\n" +
				"       Latest Mincha - Hacham Ben Sion               :  8:28:58\n" +
				"     Bein Hashmashot - 13 Degrees                    :  9:01:54\n" +
				"     Bein Hashmashot - 58.5 Minutes                  :  9:01:28\n" +
				"     Bein Hashmashot - 13.5 Minutes                  :  8:18:53\n" +
				"     Bein Hashmashot - 2 Stars                       :  8:27:55\n" +
				"    Tzait Hacochavim - MG'A 120 Minutes              : 10:02:58\n" +
				"    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     :  9:56:00\n" +
				"    Tzait Hacochavim - 3.5 deg                       :  8:15:44\n" +
				"    Tzait Hacochavim - 6.5 deg                       :  8:30:01\n" +
				"    Tzait Hacochavim - 8.5 deg                       :  8:39:30\n" +
				"    Tzait Hacochavim - 16.1 deg                      :  9:15:22\n" +
				"    Tzait Hacochavim - 18 deg                        :  9:24:19\n" +
				"    Tzait Hacochavim - 19.8 deg                      :  9:32:47\n" +
				"    Tzait Hacochavim - 26 deg                        : 10:01:59\n" +
				"    Tzait Hacochavim - Chavas Yair and Divray Malkiel:  9:02:58\n" +
				"    Tzait Hacochavim - Rambam / Rabainu Tam          :  9:14:58\n" +
				"    Tzait Hacochavim - 72 Minutes Zmaniyot           :  9:10:47\n" +
				"    Tzait Hacochavim - MG'A 90 Minutes               :  9:32:58\n" +
				"    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      :  9:27:44\n" +
				"    Tzait Hacochavim - 96 Minutes                    :  9:38:58\n" +
				"    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      :  9:33:23\n" +
				"    Tzait Hacochavim - Chacham Yosef Harari-Raful    :  8:42:58\n" +
				"    Tzait Hacochavim - Geonim 3.65 deg               :  8:16:27\n" +
				"    Tzait Hacochavim - Geonim 4.37 deg               :  8:19:53\n" +
				"    Tzait Hacochavim - Geonim 4.61 deg               :  8:21:01\n" +
				"    Tzait Hacochavim - Geonim 4.8 deg                :  8:21:56\n" +
				"    Tzait Hacochavim - Geonim 5.88 deg               :  8:27:04\n" +
				"    Tzait Hacochavim - Geonim 5.95 deg               :  8:27:24\n" +
				"    Tzait Hacochavim - Geonim 7.083 deg              :  8:32:23\n" +
				"    Tzait Hacochavim - Geonim 8.5 deg                :  8:39:30\n" +
				"    Tzait Hacochavim - Seattle - SBH                 :  8:44:58\n" +
				"    Tzait Hacochavim - Seattle - EB                  :  8:47:58\n" +
				"            Midnight - Mid-night                     : 14:24:17\n";

		HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 19 );
		verifyZmanim( ezraBessaroth, date, dailyZmanim, ezraBessarothExpected );
		verifyZmanim( yeshivaUniviersity, date, dailyZmanim, yeshivaUniversityExpected );
		verifyZmanim( haRambanSynagogue, date, dailyZmanim, haRambanSynagogueExpected );
	}


	/**
	 * A regular Erev Shabbat
	 */
	@Test
	public void october_24_2014() {

		HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 24 );
		verifyZmanim( ezraBessaroth, date, erevShabbatZmanim, "" );
	}


	/**
	 * A regular Shabbat
	 */
	@Test
	public void october_25_2014() {

		HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.APRIL, 25 );
		verifyZmanim( ezraBessaroth, date, shabbatZmanim, "" );
	}


	/**
	 * This is a day where Pesach begins as Shabbat ends.
	 */
	@Test
	public void april_4_2015() {

		HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.APRIL, 4 );
		verifyZmanim( ezraBessaroth, date, erevPesachOnShabbatZmanim,
			"Ezra Bessaroth\n" +
				"      Alot Hashachar - 120 Minutes                   :  4:42:03\n" +
				"      Alot Hashachar - 120 Minutes Zmaniyot          :  4:31:54\n" +
				"      Alot Hashachar - 16.1 deg                      :  5:06:13\n" +
				"      Alot Hashachar - 18 deg                        :  4:53:18\n" +
				"      Alot Hashachar - 19.8 deg                      :  4:40:43\n" +
				"      Alot Hashachar - 26 deg                        :  3:53:34\n" +
				"      Alot Hashachar - 60 Minutes                    :  5:42:03\n" +
				"      Alot Hashachar - 72 Minutes                    :  5:30:03\n" +
				"      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:23:58\n" +
				"      Alot Hashachar - 90 Minutes                    :  5:12:03\n" +
				"      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:04:26\n" +
				"      Alot Hashachar - 96 Minutes                    :  5:06:03\n" +
				"      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:57:56\n" +
				"       Talit/Tefilin - 10.2 deg                      :  5:44:33\n" +
				"       Talit/Tefilin - 11 deg                        :  5:39:29\n" +
				"       Talit/Tefilin - 11.5 deg                      :  5:36:18\n" +
				"              Hanetz - Sunrise                       :  6:42:03\n" +
				"              Hanetz - Sunrise (with elevation)      :  6:42:03\n" +
				"      Sof Zman Shema - 3 Hours before Chatzot        : 10:12:31\n" +
				"      Sof Zman Shema - 16.1 deg to Sunset            :  8:45:25\n" +
				"      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:54:48\n" +
				"      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:08:43\n" +
				"      Sof Zman Shema - GR'A                          :  9:57:17\n" +
				"      Sof Zman Shema - GR'A Kol Eliyahu              :  9:55:33\n" +
				"      Sof Zman Shema - MG'A                          :  9:21:17\n" +
				"      Sof Zman Shema - MG'A 120 Minutes              :  8:57:17\n" +
				"      Sof Zman Shema - MG'A 16.1 deg                 :  9:09:25\n" +
				"      Sof Zman Shema - MG'A 19.8 deg                 :  8:56:41\n" +
				"      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:18:15\n" +
				"      Sof Zman Shema - MG'A 90 Minutes               :  9:12:17\n" +
				"      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:08:29\n" +
				"      Sof Zman Shema - 96 Minutes                    :  9:09:17\n" +
				"      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:05:14\n" +
				"     Sof Zman Tefila - 2 Hours before Chatzot        : 11:12:31\n" +
				"     Sof Zman Tefila - GR'A                          : 11:02:22\n" +
				"     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:23:38\n" +
				"     Sof Zman Tefila - MG'A                          : 10:38:22\n" +
				"     Sof Zman Tefila - MG'A 120 Minutes              : 10:22:22\n" +
				"     Sof Zman Tefila - MG'A 16.1 deg                 : 10:30:29\n" +
				"     Sof Zman Tefila - MG'A 19.8 deg                 : 10:22:01\n" +
				"     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:36:20\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes               : 10:32:22\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:29:50\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes               : 10:30:22\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:27:40\n" +
				"             Chatzot - Mid-day                       : 13:12:31\n" +
				"       Mincha Gedola - GR'A                          : 13:45:04\n" +
				"       Mincha Gedola - MG'A 16.1 deg                 : 13:53:09\n" +
				"       Mincha Gedola - MG'A 72 Minutes               : 13:51:04\n" +
				"       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:30:56\n" +
				"       Mincha Ketana - GR'A                          : 17:00:18\n" +
				"       Mincha Ketana - MG'A 16.1 deg                 : 17:56:20\n" +
				"       Mincha Ketana - MG'A 72 Minutes               : 17:42:18\n" +
				"       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:15:42\n" +
				"       Plag HaMincha - GR'A                          : 18:21:39\n" +
				"       Plag HaMincha - MG'A 120 Minutes              : 19:56:39\n" +
				"       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:04:41\n" +
				"       Plag HaMincha - MG'A 16.1 deg                 : 19:37:40\n" +
				"       Plag HaMincha - MG'A 18 deg                   : 19:47:56\n" +
				"       Plag HaMincha - MG'A 19.8 deg                 : 19:57:56\n" +
				"       Plag HaMincha - MG'A 26 deg                   : 20:35:31\n" +
				"       Plag HaMincha - MG'A 60 Minutes               : 19:09:09\n" +
				"       Plag HaMincha - MG'A 72 Minutes               : 19:18:39\n" +
				"       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:23:28\n" +
				"       Plag HaMincha - MG'A 90 Minutes               : 19:32:54\n" +
				"       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:38:55\n" +
				"       Plag HaMincha - MG'A 96 Minutes               : 19:37:39\n" +
				"       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:44:04\n" +
				"       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:49:20\n" +
				"               Shkia - Sunset                        : 19:42:59\n" +
				"       Latest Mincha - Mishna Berura                 : 19:42:59\n" +
				"       Latest Mincha - Hacham Ovadia Yosef           : 19:57:38\n" +
				"       Latest Mincha - Hacham Ben Sion               : 20:08:59\n" +
				"     Bein Hashmashot - 13 Degrees                    : 21:00:05\n" +
				"     Bein Hashmashot - 58.5 Minutes                  : 20:41:29\n" +
				"     Bein Hashmashot - 13.5 Minutes                  : 20:07:03\n" +
				"     Bein Hashmashot - 2 Stars                       : 20:16:42\n" +
				"    Tzait Hacochavim - MG'A 120 Minutes              : 21:42:59\n" +
				"    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 21:53:09\n" +
				"    Tzait Hacochavim - 3.5 deg                       : 19:59:06\n" +
				"    Tzait Hacochavim - 6.5 deg                       : 20:17:28\n" +
				"    Tzait Hacochavim - 8.5 deg                       : 20:29:53\n" +
				"    Tzait Hacochavim - 16.1 deg                      : 21:19:00\n" +
				"    Tzait Hacochavim - 18 deg                        : 21:31:57\n" +
				"    Tzait Hacochavim - 19.8 deg                      : 21:44:35\n" +
				"    Tzait Hacochavim - 26 deg                        : 22:32:01\n" +
				"    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:42:59\n" +
				"    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:54:59\n" +
				"    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:01:05\n" +
				"    Tzait Hacochavim - MG'A 90 Minutes               : 21:12:59\n" +
				"    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:20:36\n" +
				"    Tzait Hacochavim - 96 Minutes                    : 21:18:59\n" +
				"    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:27:07\n" +
				"    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:22:59\n" +
				"    Tzait Hacochavim - Geonim 3.65 deg               : 20:00:01\n" +
				"    Tzait Hacochavim - Geonim 4.37 deg               : 20:04:24\n" +
				"    Tzait Hacochavim - Geonim 4.61 deg               : 20:05:52\n" +
				"    Tzait Hacochavim - Geonim 4.8 deg                : 20:07:01\n" +
				"    Tzait Hacochavim - Geonim 5.88 deg               : 20:13:39\n" +
				"    Tzait Hacochavim - Geonim 5.95 deg               : 20:14:04\n" +
				"    Tzait Hacochavim - Geonim 7.083 deg              : 20:20:33\n" +
				"    Tzait Hacochavim - Geonim 8.5 deg                : 20:29:53\n" +
				"    Tzait Hacochavim - Seattle - SBH                 : 20:24:59\n" +
				"    Tzait Hacochavim - Seattle - EB                  : 20:27:59\n" +
				"            Midnight - Mid-night                     :  1:11:32\n"
		);
	}


	/**
	 * Erev pesach, which has a number of additional zmanim
	 */
	@Test
	public void april_14_2014() {

		HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.APRIL, 14 );
		verifyZmanim( ezraBessaroth, date, erevPesachZmanim,
			"Ezra Bessaroth\n" +
				"      Alot Hashachar - 120 Minutes                   :  4:22:31\n" +
				"      Alot Hashachar - 120 Minutes Zmaniyot          :  4:06:45\n" +
				"      Alot Hashachar - 16.1 deg                      :  4:42:32\n" +
				"      Alot Hashachar - 18 deg                        :  4:28:35\n" +
				"      Alot Hashachar - 19.8 deg                      :  4:14:50\n" +
				"      Alot Hashachar - 26 deg                        :  3:20:55\n" +
				"      Alot Hashachar - 60 Minutes                    :  5:22:31\n" +
				"      Alot Hashachar - 72 Minutes                    :  5:10:31\n" +
				"      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:01:03\n" +
				"      Alot Hashachar - 90 Minutes                    :  4:52:31\n" +
				"      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:40:42\n" +
				"      Alot Hashachar - 96 Minutes                    :  4:46:31\n" +
				"      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:33:54\n" +
				"       Talit/Tefilin - 10.2 deg                      :  5:23:07\n" +
				"       Talit/Tefilin - 11 deg                        :  5:17:48\n" +
				"       Talit/Tefilin - 11.5 deg                      :  5:14:27\n" +
				"              Hanetz - Sunrise                       :  6:22:31\n" +
				"              Hanetz - Sunrise (with elevation)      :  6:22:31\n" +
				"      Sof Zman Shema - 3 Hours before Chatzot        : 10:09:48\n" +
				"      Sof Zman Shema - 16.1 deg to Sunset            :  8:31:10\n" +
				"      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:40:50\n" +
				"      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:55:04\n" +
				"      Sof Zman Shema - GR'A                          :  9:46:09\n" +
				"      Sof Zman Shema - GR'A Kol Eliyahu              :  9:45:47\n" +
				"      Sof Zman Shema - MG'A                          :  9:10:09\n" +
				"      Sof Zman Shema - MG'A 120 Minutes              :  8:46:09\n" +
				"      Sof Zman Shema - MG'A 16.1 deg                 :  8:56:14\n" +
				"      Sof Zman Shema - MG'A 19.8 deg                 :  8:42:25\n" +
				"      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:05:26\n" +
				"      Sof Zman Shema - MG'A 90 Minutes               :  9:01:09\n" +
				"      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:55:15\n" +
				"      Sof Zman Shema - 96 Minutes                    :  8:58:09\n" +
				"      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:51:51\n" +
				"     Sof Zman Tefila - 2 Hours before Chatzot        : 11:09:48\n" +
				"     Sof Zman Tefila - GR'A                          : 10:54:02\n" +
				"     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:13:04\n" +
				"     Sof Zman Tefila - MG'A                          : 10:30:02\n" +
				"     Sof Zman Tefila - MG'A 120 Minutes              : 10:14:02\n" +
				"     Sof Zman Tefila - MG'A 16.1 deg                 : 10:20:47\n" +
				"     Sof Zman Tefila - MG'A 19.8 deg                 : 10:11:36\n" +
				"     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:26:53\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes               : 10:24:02\n" +
				"     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:20:06\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes               : 10:22:02\n" +
				"     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:17:50\n" +
				"     Achilat Chametz - 2 Hours before Chatzot        : 11:09:48\n" +
				"     Achilat Chametz - GR'A                          : 10:54:02\n" +
				"     Achilat Chametz - Chacham Yosef Harari-Raful    : 10:13:04\n" +
				"     Achilat Chametz - MG'A                          : 10:30:02\n" +
				"     Achilat Chametz - MG'A 120 Minutes              : 10:14:02\n" +
				"     Achilat Chametz - MG'A 16.1 deg                 : 10:20:47\n" +
				"     Achilat Chametz - MG'A 19.8 deg                 : 10:11:36\n" +
				"     Achilat Chametz - MG'A 72 Minutes Zmaniyot      : 10:30:02\n" +
				"     Achilat Chametz - MG'A 90 Minutes               : 10:24:02\n" +
				"     Achilat Chametz - MG'A 90 Minutes Zmaniyot      : 10:20:06\n" +
				"     Achilat Chametz - MG'A 96 Minutes               : 10:22:02\n" +
				"     Achilat Chametz - MG'A 96 Minutes Zmaniyot      : 10:17:50\n" +
				"        Biur Chametz - GR'A                          : 12:01:55\n" +
				"        Biur Chametz - MG'A 72 Minutes               : 11:49:55\n" +
				"        Biur Chametz - MG'A 16.1 deg                 : 11:45:21\n" +
				"             Chatzot - Mid-day                       : 13:09:48\n" +
				"       Mincha Gedola - GR'A                          : 13:43:44\n" +
				"       Mincha Gedola - MG'A 16.1 deg                 : 13:52:12\n" +
				"       Mincha Gedola - MG'A 72 Minutes               : 13:49:44\n" +
				"       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:28:04\n" +
				"       Mincha Ketana - GR'A                          : 17:07:23\n" +
				"       Mincha Ketana - MG'A 16.1 deg                 : 18:05:54\n" +
				"       Mincha Ketana - MG'A 72 Minutes               : 17:49:23\n" +
				"       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:22:05\n" +
				"       Plag HaMincha - GR'A                          : 18:32:14\n" +
				"       Plag HaMincha - MG'A 120 Minutes              : 20:07:14\n" +
				"       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:19:42\n" +
				"       Plag HaMincha - MG'A 16.1 deg                 : 19:51:36\n" +
				"       Plag HaMincha - MG'A 18 deg                   : 20:02:42\n" +
				"       Plag HaMincha - MG'A 19.8 deg                 : 20:13:39\n" +
				"       Plag HaMincha - MG'A 26 deg                   : 20:56:43\n" +
				"       Plag HaMincha - MG'A 60 Minutes               : 19:19:44\n" +
				"       Plag HaMincha - MG'A 72 Minutes               : 19:29:14\n" +
				"       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:36:43\n" +
				"       Plag HaMincha - MG'A 90 Minutes               : 19:43:29\n" +
				"       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:52:50\n" +
				"       Plag HaMincha - MG'A 96 Minutes               : 19:48:14\n" +
				"       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:58:13\n" +
				"       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:59:35\n" +
				"     Candle Lighting - 18 Minutes                    : 19:39:05\n" +
				"     Candle Lighting - 15 Minutes                    : 19:42:05\n" +
				"     Candle Lighting - 20 Minutes                    : 19:37:05\n" +
				"     Candle Lighting - 21 Minutes                    : 19:36:05\n" +
				"     Candle Lighting - 22 Minutes                    : 19:35:05\n" +
				"     Candle Lighting - 30 Minutes                    : 19:27:05\n" +
				"     Candle Lighting - 40 Minutes                    : 19:17:05\n" +
				"               Shkia - Sunset                        : 19:57:05\n" +
				"       Latest Mincha - Mishna Berura                 : 19:57:05\n" +
				"       Latest Mincha - Hacham Ovadia Yosef           : 20:12:21\n" +
				"       Latest Mincha - Hacham Ben Sion               : 20:23:05\n" +
				"     Bein Hashmashot - 13 Degrees                    : 21:17:08\n" +
				"     Bein Hashmashot - 58.5 Minutes                  : 20:55:35\n" +
				"     Bein Hashmashot - 13.5 Minutes                  : 20:22:14\n" +
				"     Bein Hashmashot - 2 Stars                       : 20:32:33\n" +
				"           Fast Ends - MG'A 120 Minutes              : 21:57:05\n" +
				"           Fast Ends - GR'A 120 Minutes Zmaniyot     : 22:12:51\n" +
				"           Fast Ends - 3.5 deg                       : 20:13:36\n" +
				"           Fast Ends - 6.5 deg                       : 20:32:32\n" +
				"           Fast Ends - 8.5 deg                       : 20:45:26\n" +
				"           Fast Ends - 16.1 deg                      : 21:37:18\n" +
				"           Fast Ends - 18 deg                        : 21:51:19\n" +
				"           Fast Ends - 19.8 deg                      : 22:05:08\n" +
				"           Fast Ends - 26 deg                        : 22:59:30\n" +
				"           Fast Ends - Chavas Yair and Divray Malkiel: 20:57:05\n" +
				"           Fast Ends - Rambam / Rabainu Tam          : 21:09:05\n" +
				"           Fast Ends - 72 Minutes Zmaniyot           : 21:18:32\n" +
				"           Fast Ends - MG'A 90 Minutes               : 21:27:05\n" +
				"           Fast Ends - GR'A 90 Minutes Zmaniyot      : 21:38:54\n" +
				"           Fast Ends - 96 Minutes                    : 21:33:05\n" +
				"           Fast Ends - GR'A 96 Minutes Zmaniyot      : 21:45:41\n" +
				"           Fast Ends - Chacham Yosef Harari-Raful    : 20:37:05\n" +
				"           Fast Ends - Geonim 3.65 deg               : 20:14:32\n" +
				"           Fast Ends - Geonim 4.37 deg               : 20:19:02\n" +
				"           Fast Ends - Geonim 4.61 deg               : 20:20:33\n" +
				"           Fast Ends - Geonim 4.8 deg                : 20:21:45\n" +
				"           Fast Ends - Geonim 5.88 deg               : 20:28:35\n" +
				"           Fast Ends - Geonim 5.95 deg               : 20:29:02\n" +
				"           Fast Ends - Geonim 7.083 deg              : 20:35:44\n" +
				"           Fast Ends - Geonim 8.5 deg                : 20:45:26\n" +
				"           Fast Ends - Seattle Sephardic             : 20:27:05\n" +
				"    Tzait Hacochavim - MG'A 120 Minutes              : 21:57:05\n" +
				"    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 22:12:51\n" +
				"    Tzait Hacochavim - 3.5 deg                       : 20:13:36\n" +
				"    Tzait Hacochavim - 6.5 deg                       : 20:32:32\n" +
				"    Tzait Hacochavim - 8.5 deg                       : 20:45:26\n" +
				"    Tzait Hacochavim - 16.1 deg                      : 21:37:18\n" +
				"    Tzait Hacochavim - 18 deg                        : 21:51:19\n" +
				"    Tzait Hacochavim - 19.8 deg                      : 22:05:08\n" +
				"    Tzait Hacochavim - 26 deg                        : 22:59:30\n" +
				"    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:57:05\n" +
				"    Tzait Hacochavim - Rambam / Rabainu Tam          : 21:09:05\n" +
				"    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:18:32\n" +
				"    Tzait Hacochavim - MG'A 90 Minutes               : 21:27:05\n" +
				"    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:38:54\n" +
				"    Tzait Hacochavim - 96 Minutes                    : 21:33:05\n" +
				"    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:45:41\n" +
				"    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:37:05\n" +
				"    Tzait Hacochavim - Geonim 3.65 deg               : 20:14:32\n" +
				"    Tzait Hacochavim - Geonim 4.37 deg               : 20:19:02\n" +
				"    Tzait Hacochavim - Geonim 4.61 deg               : 20:20:33\n" +
				"    Tzait Hacochavim - Geonim 4.8 deg                : 20:21:45\n" +
				"    Tzait Hacochavim - Geonim 5.88 deg               : 20:28:35\n" +
				"    Tzait Hacochavim - Geonim 5.95 deg               : 20:29:02\n" +
				"    Tzait Hacochavim - Geonim 7.083 deg              : 20:35:44\n" +
				"    Tzait Hacochavim - Geonim 8.5 deg                : 20:45:26\n" +
				"    Tzait Hacochavim - Seattle - SBH                 : 20:39:05\n" +
				"    Tzait Hacochavim - Seattle - EB                  : 20:42:05\n" +
				"            Midnight - Mid-night                     :  1:08:51\n"
		);
	}


	private void verifyZmanim(
		ZmanimLocation    location,
		HebrewDate        date,
		List<Zmanim.Type> expectedZmanim,
		String            expected
	) {

		List<Zmanim> zmanimForDay = date.getZmanim( location );

		for ( int i = 0; i < expectedZmanim.size(); i++ ) {
			Assert.assertEquals( expectedZmanim.get( i ), zmanimForDay.get( i ).type );
		}
		Assert.assertEquals( expectedZmanim.size(), zmanimForDay.size() );

		StringBuilder output = new StringBuilder();
		output.append( location.getLocationName() ).append( "\n" );
		for ( Zmanim zmanim : zmanimForDay ) {
			for ( Zman zman : zmanim.asList() ) {
				Date zmanTime = zman.getTime();
				output.append( String.format( "%20s - %-30s: %2d:%02d:%02d\n", zmanim.type.name, zman.name, zmanTime.getHours(), zmanTime.getMinutes(), zmanTime.getSeconds() ) );
			}
		}

		Assert.assertEquals( expected, output.toString() );
	}


} // End of UT_Zmanim class
