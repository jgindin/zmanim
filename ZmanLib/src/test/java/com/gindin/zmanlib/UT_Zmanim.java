/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
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
      .atElevation( 61 )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    yeshivaUniviersity = new ZmanimLocation.Builder()
      .atLatitude( 40.851546 )
      .atLongitude( -73.928937 )
      .atElevation( 56 )
      .locationName( "Yeshiva University" )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .build();

    haRambanSynagogue = new ZmanimLocation.Builder()
      .atLatitude( 31.7750 )
      .atLongitude( 35.2312 )
      .atElevation( 767.49 )
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
        "              Hanetz - Sunrise (with elevation)      :  7:31:54\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:53:38\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:05:06\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:14:32\n" +
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
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:09:23\n" +
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
        "             Chatzot - Mid-Day                       : 12:53:38\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
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
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:39:01\n" +
        "               Shkia - Sunset                        : 18:13:51\n" +
        "               Shkia - Sunset (with elevation)       : 18:15:23\n" +
        "       Latest Mincha - Mishna Berura                 : 18:13:51\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:25:52\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:39:51\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:28:11\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:12:21\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:38:05\n" +
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
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:51:35\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 19:00:02\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:58:51\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 19:03:51\n" +
        "            Midnight - Mid-night                     :  0:54:22\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n";

    final String yeshivaUniversityExpected =
      "Yeshiva University\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:11:08\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:21:23\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:49:48\n" +
        "      Alot Hashachar - 18 deg                        :  5:39:44\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:30:12\n" +
        "      Alot Hashachar - 26 deg                        :  4:57:11\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:11:08\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:59:08\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:05:17\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:41:08\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:48:49\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:35:08\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:43:20\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:21:01\n" +
        "       Talit/Tefilin - 11 deg                        :  6:16:47\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:14:08\n" +
        "              Hanetz - Sunrise                       :  7:11:08\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:09:50\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:40:24\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:54:46\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:03:09\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:16:23\n" +
        "      Sof Zman Shema - GR'A                          :  9:55:46\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:03:25\n" +
        "      Sof Zman Shema - MG'A                          :  9:19:46\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:55:46\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:15:06\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:05:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:22:50\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:10:46\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:14:36\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:07:46\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:11:52\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:58:34\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:40:24\n" +
        "     Sof Zman Tefila - GR'A                          : 10:50:39\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:20:05\n" +
        "     Sof Zman Tefila - MG'A                          : 10:26:39\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:10:39\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:23:33\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:17:01\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:28:42\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:20:39\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:23:12\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:18:39\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:21:23\n" +
        "             Chatzot - Mid-Day                       : 12:40:24\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:55:42\n" +
        "       Mincha Gedola - GR'A                          : 13:07:50\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:14:38\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:13:50\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:59:19\n" +
        "       Mincha Ketana - GR'A                          : 15:52:28\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:39:57\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:34:28\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:10:25\n" +
        "       Plag HaMincha - GR'A                          : 17:01:04\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:36:04\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:27:58\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:05:30\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:13:27\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:21:00\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:47:08\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:48:34\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:58:04\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:53:12\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:12:19\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:06:14\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:17:04\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:10:35\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:30:03\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:29:26\n" +
        "               Shkia - Sunset                        : 18:09:40\n" +
        "               Shkia - Sunset (with elevation)       : 18:10:58\n" +
        "       Latest Mincha - Mishna Berura                 : 18:09:40\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:22:01\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:35:40\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:15:55\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:08:10\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:29:44\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:37:42\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:09:40\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:59:26\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:24:04\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:40:08\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:50:47\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:31:03\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:41:06\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:50:38\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:23:38\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:09:40\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:21:40\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:15:31\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:39:40\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:31:59\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:45:40\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:37:28\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:49:40\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:24:52\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:28:44\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:30:01\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:31:02\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:36:49\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:37:12\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:43:14\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:50:47\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:54:40\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:59:40\n" +
        "            Midnight - Mid-night                     :  0:40:57\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:55:42\n";

    final String haRambanSynagogueExpected =
      "HaRamban Synagogue\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:44:51\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:51:49\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:32:29\n" +
        "      Alot Hashachar - 18 deg                        :  5:23:32\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:15:04\n" +
        "      Alot Hashachar - 26 deg                        :  4:45:52\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:44:51\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:32:51\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:37:02\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:14:51\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:20:05\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:08:51\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:14:26\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:00:18\n" +
        "       Talit/Tefilin - 11 deg                        :  5:56:31\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:54:10\n" +
        "              Hanetz - Sunrise                       :  6:44:51\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:40:35\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:23:54\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:40:06\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:47:33\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:53:31\n" +
        "      Sof Zman Shema - GR'A                          :  9:34:23\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              :  9:41:57\n" +
        "      Sof Zman Shema - MG'A                          :  8:58:23\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:34:23\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  8:58:12\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:49:30\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:00:28\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  8:49:23\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:52:00\n" +
        "      Sof Zman Shema - 96 Minutes                    :  8:46:23\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:49:10\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:43:28\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:23:54\n" +
        "     Sof Zman Tefila - GR'A                          : 10:30:53\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  9:59:01\n" +
        "     Sof Zman Tefila - MG'A                          : 10:06:53\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              :  9:50:53\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:06:47\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:00:58\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:08:17\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:00:53\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:02:38\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               :  9:58:53\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:00:45\n" +
        "             Chatzot - Mid-Day                       : 12:23:54\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:39:04\n" +
        "       Mincha Gedola - GR'A                          : 12:52:10\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 12:58:13\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 12:58:10\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:42:45\n" +
        "       Mincha Ketana - GR'A                          : 15:41:42\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:23:56\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:23:42\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 15:59:14\n" +
        "       Plag HaMincha - GR'A                          : 16:52:20\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:27:20\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:21:49\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 17:49:39\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 17:56:44\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:03:26\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:26:33\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:39:50\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:49:20\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:46:01\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:03:35\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 17:59:26\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:08:20\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:03:55\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:21:06\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:17:31\n" +
        "               Shkia - Sunset                        : 18:02:58\n" +
        "               Shkia - Sunset (with elevation)       : 18:07:14\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:58\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:15:41\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:58\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:01:54\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:01:28\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:19:17\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:27:55\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:02:58\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:56:00\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:15:44\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:30:01\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:39:30\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:15:22\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:24:19\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:32:47\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:01:59\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:02:58\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:14:58\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:10:47\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:32:58\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:27:44\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:38:58\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:33:23\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:42:58\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:16:27\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:19:53\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:21:01\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:21:56\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:27:04\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:27:24\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:32:47\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:39:30\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:58\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:58\n" +
        "            Midnight - Mid-night                     :  0:24:17\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:39:04\n";

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
    verifyZmanim( ezraBessaroth, date, erevShabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:40:48\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:56:47\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:09:03\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:47\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:47:06\n" +
        "      Alot Hashachar - 26 deg                        :  5:10:00\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:40:48\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:28:48\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:38:23\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:10:48\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:22:47\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:04:48\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:17:35\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:44:05\n" +
        "       Talit/Tefilin - 11 deg                        :  6:39:19\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:36:21\n" +
        "              Hanetz - Sunrise                       :  7:40:48\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:39:15\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:52:53\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:08:02\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:17:33\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:40:02\n" +
        "      Sof Zman Shema - GR'A                          : 10:16:51\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:24:56\n" +
        "      Sof Zman Shema - MG'A                          :  9:40:51\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:16:51\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:30:59\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:20:00\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:45:38\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:31:51\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:37:50\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:28:51\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:35:14\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:12:21\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:52:53\n" +
        "     Sof Zman Tefila - GR'A                          : 11:08:52\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:40:35\n" +
        "     Sof Zman Tefila - MG'A                          : 10:44:52\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:28:52\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:38:18\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:30:59\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:48:03\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:38:52\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:42:51\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:36:52\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:41:07\n" +
        "             Chatzot - Mid-Day                       : 12:52:53\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:18:54\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:26:34\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:24:54\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:11:57\n" +
        "       Mincha Ketana - GR'A                          : 15:54:56\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:48:30\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:36:56\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:13:36\n" +
        "       Plag HaMincha - GR'A                          : 16:59:57\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:34:57\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:22:18\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:12:39\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:21:34\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:30:01\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:59:22\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:47:27\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:56:57\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:49:22\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:11:12\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:01:43\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:15:57\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:05:50\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:29:17\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:32:09\n" +
        "     Candle Lighting - 18 Minutes                    : 17:46:58\n" +
        "     Candle Lighting - 15 Minutes                    : 17:49:58\n" +
        "     Candle Lighting - 20 Minutes                    : 17:44:58\n" +
        "     Candle Lighting - 21 Minutes                    : 17:43:58\n" +
        "     Candle Lighting - 22 Minutes                    : 17:42:58\n" +
        "     Candle Lighting - 30 Minutes                    : 17:34:58\n" +
        "     Candle Lighting - 40 Minutes                    : 17:24:58\n" +
        "               Shkia - Sunset                        : 18:04:58\n" +
        "               Shkia - Sunset (with elevation)       : 18:06:31\n" +
        "       Latest Mincha - Mishna Berura                 : 18:04:58\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:16:40\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:30:58\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:19:50\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:03:28\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:29:34\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:36:33\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:04:58\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:48:59\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:21:22\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:39:33\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:51:35\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:36:47\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:48:03\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:58:44\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:35:49\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:04:58\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:16:58\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:07:23\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:34:58\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:22:59\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:40:58\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:28:11\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:44:58\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:22:17\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:26:40\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:28:07\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:29:16\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:35:49\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:36:14\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:43:04\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:51:35\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:49:58\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:54:58\n" +
        "            Midnight - Mid-night                     :  0:53:38\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  /**
   * A regular Shabbat
   */
  @Test
  public void october_25_2014() {

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 25 );
    verifyZmanim( ezraBessaroth, date, shabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:42:18\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:58:48\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:10:25\n" +
        "      Alot Hashachar - 18 deg                        :  5:59:09\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:28\n" +
        "      Alot Hashachar - 26 deg                        :  5:11:25\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:42:18\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:30:18\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:40:12\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:12:18\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:24:41\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:06:18\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:19:30\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:45:28\n" +
        "       Talit/Tefilin - 11 deg                        :  6:40:42\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:37:43\n" +
        "              Hanetz - Sunrise                       :  7:42:18\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:40:44\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:52:46\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:08:37\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:18:10\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:40:58\n" +
        "      Sof Zman Shema - GR'A                          : 10:17:32\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:25:41\n" +
        "      Sof Zman Shema - MG'A                          :  9:41:32\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:17:32\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:31:36\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:20:38\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:46:29\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:32:32\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:38:43\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:29:32\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:36:08\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:12:57\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:52:46\n" +
        "     Sof Zman Tefila - GR'A                          : 11:09:17\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:41:13\n" +
        "     Sof Zman Tefila - MG'A                          : 10:45:17\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:29:17\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:38:40\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:31:21\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:48:35\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:39:17\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:43:24\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:37:17\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:41:41\n" +
        "             Chatzot - Mid-Day                       : 12:52:46\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:18:39\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:26:20\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:24:39\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:11:51\n" +
        "       Mincha Ketana - GR'A                          : 15:53:53\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:47:32\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:35:53\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:12:37\n" +
        "       Plag HaMincha - GR'A                          : 16:58:34\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:33:34\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:20:29\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:11:22\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:20:17\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:28:44\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:58:03\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:46:04\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:55:34\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:47:43\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:09:49\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:00:01\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:14:34\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:04:06\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:27:56\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:30:50\n" +
        "               Shkia - Sunset                        : 18:03:15\n" +
        "               Shkia - Sunset (with elevation)       : 18:04:48\n" +
        "       Latest Mincha - Mishna Berura                 : 18:03:15\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:14:53\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:29:15\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:18:14\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:01:45\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:27:56\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:34:52\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:03:15\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:46:44\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:19:41\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:37:55\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:49:57\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:35:12\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:46:27\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:57:08\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:34:10\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:03:15\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:15:15\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:05:20\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:33:15\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:20:52\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:39:15\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:26:02\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:43:15\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:20:36\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:25:00\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:26:27\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:27:37\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:34:10\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:34:35\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:41:26\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:49:57\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:48:15\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:53:15\n" +
        "            Havdalah - MG'A 120 Minutes              : 20:03:15\n" +
        "            Havdalah - GR'A 120 Minutes Zmaniyot     : 19:46:44\n" +
        "            Havdalah - 3.5 deg                       : 18:19:41\n" +
        "            Havdalah - 6.5 deg                       : 18:37:55\n" +
        "            Havdalah - 8.5 deg                       : 18:49:57\n" +
        "            Havdalah - 16.1 deg                      : 19:35:12\n" +
        "            Havdalah - 18 deg                        : 19:46:27\n" +
        "            Havdalah - 19.8 deg                      : 19:57:08\n" +
        "            Havdalah - 26 deg                        : 20:34:10\n" +
        "            Havdalah - Chavas Yair and Divray Malkiel: 19:03:15\n" +
        "            Havdalah - Rambam / Rabainu Tam          : 19:15:15\n" +
        "            Havdalah - 72 Minutes Zmaniyot           : 19:05:20\n" +
        "            Havdalah - MG'A 90 Minutes               : 19:33:15\n" +
        "            Havdalah - GR'A 90 Minutes Zmaniyot      : 19:20:52\n" +
        "            Havdalah - 96 Minutes                    : 19:39:15\n" +
        "            Havdalah - GR'A 96 Minutes Zmaniyot      : 19:26:02\n" +
        "            Havdalah - Chacham Yosef Harari-Raful    : 18:43:15\n" +
        "            Havdalah - Geonim 3.65 deg               : 18:20:36\n" +
        "            Havdalah - Geonim 4.37 deg               : 18:25:00\n" +
        "            Havdalah - Geonim 4.61 deg               : 18:26:27\n" +
        "            Havdalah - Geonim 4.8 deg                : 18:27:37\n" +
        "            Havdalah - Geonim 5.88 deg               : 18:34:10\n" +
        "            Havdalah - Geonim 5.95 deg               : 18:34:35\n" +
        "            Havdalah - Geonim 7.083 deg              : 18:41:26\n" +
        "            Havdalah - Geonim 8.5 deg                : 18:49:57\n" +
        "            Havdalah - Seattle Sephardic             : 18:48:15\n" +
        "            Havdalah - Rabbi Moshe Feinstein         : 18:53:15\n" +
        "            Midnight - Mid-night                     :  0:53:31\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
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
        "              Hanetz - Sunrise (with elevation)      :  6:40:33\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:12:31\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:45:25\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:54:56\n" +
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
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:49:40\n" +
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
        "             Chatzot - Mid-Day                       : 13:12:31\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
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
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:53:40\n" +
        "               Shkia - Sunset                        : 19:42:59\n" +
        "               Shkia - Sunset (with elevation)       : 19:44:30\n" +
        "       Latest Mincha - Mishna Berura                 : 19:42:59\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:57:38\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:08:59\n" +
        "     Bein Hashmashot - 13 Degrees                    : 21:00:05\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:41:29\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:07:34\n" +
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
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:21:04\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:29:53\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:27:59\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:32:59\n" +
        "            Midnight - Mid-night                     :  1:11:32\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
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
        "              Hanetz - Sunrise (with elevation)      :  6:20:58\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:09:48\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:31:10\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:40:58\n" +
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
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:35:32\n" +
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
        "             Chatzot - Mid-Day                       : 13:09:48\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
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
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 19:05:07\n" +
        "     Candle Lighting - 18 Minutes                    : 19:39:05\n" +
        "     Candle Lighting - 15 Minutes                    : 19:42:05\n" +
        "     Candle Lighting - 20 Minutes                    : 19:37:05\n" +
        "     Candle Lighting - 21 Minutes                    : 19:36:05\n" +
        "     Candle Lighting - 22 Minutes                    : 19:35:05\n" +
        "     Candle Lighting - 30 Minutes                    : 19:27:05\n" +
        "     Candle Lighting - 40 Minutes                    : 19:17:05\n" +
        "               Shkia - Sunset                        : 19:57:05\n" +
        "               Shkia - Sunset (with elevation)       : 19:58:37\n" +
        "       Latest Mincha - Mishna Berura                 : 19:57:05\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 20:12:21\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:23:05\n" +
        "     Bein Hashmashot - 13 Degrees                    : 21:17:08\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:55:35\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:22:46\n" +
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
        "           Fast Ends - Geonim 7.083 deg              : 20:36:16\n" +
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
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:36:16\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:45:26\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:42:05\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:47:05\n" +
        "            Midnight - Mid-night                     :  1:08:51\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  @Test
  public void asara_bTevet_2014() {

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.JANUARY, 1 );
    verifyZmanim( ezraBessaroth, date, fastDayZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:57:15\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  6:32:02\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:16:44\n" +
        "      Alot Hashachar - 18 deg                        :  6:05:02\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:54:03\n" +
        "      Alot Hashachar - 26 deg                        :  5:16:46\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:57:15\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:45:15\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  7:06:07\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:27:15\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:53:20\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:21:15\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:49:05\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:53:56\n" +
        "       Talit/Tefilin - 11 deg                        :  6:48:47\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:45:36\n" +
        "              Hanetz - Sunrise                       :  7:57:15\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:55:28\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:12:55\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:49:41\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:00:26\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:36:44\n" +
        "      Sof Zman Shema - GR'A                          : 10:05:05\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:03:09\n" +
        "      Sof Zman Shema - MG'A                          :  9:29:05\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:05:05\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:14:48\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:03:28\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:39:31\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:20:05\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:33:08\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:17:05\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:31:00\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:54:38\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:12:55\n" +
        "     Sof Zman Tefila - GR'A                          : 10:47:42\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:26:56\n" +
        "     Sof Zman Tefila - MG'A                          : 10:23:42\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:07:42\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:14:10\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:06:36\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:30:39\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:17:42\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:26:23\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:15:42\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:24:58\n" +
        "             Chatzot - Mid-Day                       : 12:12:55\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:09:04\n" +
        "       Mincha Gedola - GR'A                          : 12:34:13\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 12:42:34\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 12:40:13\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:32:27\n" +
        "       Mincha Ketana - GR'A                          : 14:42:03\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 15:40:39\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 15:24:03\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 15:03:04\n" +
        "       Plag HaMincha - GR'A                          : 15:35:19\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 17:10:19\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 16:42:47\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 16:54:51\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 17:04:06\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 17:12:48\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 17:42:18\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 16:22:49\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 16:32:19\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 16:15:47\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 16:46:34\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 16:25:55\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 16:51:19\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 16:29:17\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 16:05:49\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 16:11:40\n" +
        "               Shkia - Sunset                        : 16:28:34\n" +
        "               Shkia - Sunset (with elevation)       : 16:30:22\n" +
        "       Latest Mincha - Mishna Berura                 : 16:28:34\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 16:38:10\n" +
        "       Latest Mincha - Hacham Ben Sion               : 16:54:34\n" +
        "     Bein Hashmashot - 13 Degrees                    : 17:51:12\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 17:27:04\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 16:58:01\n" +
        "     Bein Hashmashot - 2 Stars                       : 17:02:48\n" +
        "           Fast Ends - MG'A 120 Minutes              : 18:28:34\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 17:53:48\n" +
        "           Fast Ends - 3.5 deg                       : 16:47:19\n" +
        "           Fast Ends - 6.5 deg                       : 17:07:39\n" +
        "           Fast Ends - 8.5 deg                       : 17:20:50\n" +
        "           Fast Ends - 16.1 deg                      : 18:09:03\n" +
        "           Fast Ends - 18 deg                        : 18:20:44\n" +
        "           Fast Ends - 19.8 deg                      : 18:31:43\n" +
        "           Fast Ends - 26 deg                        : 19:09:00\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 17:28:34\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 17:40:34\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 17:19:42\n" +
        "           Fast Ends - MG'A 90 Minutes               : 17:58:34\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 17:32:29\n" +
        "           Fast Ends - 96 Minutes                    : 18:04:34\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 17:36:45\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 17:08:34\n" +
        "           Fast Ends - Geonim 3.65 deg               : 16:48:21\n" +
        "           Fast Ends - Geonim 4.37 deg               : 16:53:17\n" +
        "           Fast Ends - Geonim 4.61 deg               : 16:54:55\n" +
        "           Fast Ends - Geonim 4.8 deg                : 16:56:13\n" +
        "           Fast Ends - Geonim 5.88 deg               : 17:03:30\n" +
        "           Fast Ends - Geonim 5.95 deg               : 17:03:58\n" +
        "           Fast Ends - Geonim 7.083 deg              : 17:11:31\n" +
        "           Fast Ends - Geonim 8.5 deg                : 17:20:50\n" +
        "           Fast Ends - Seattle Sephardic             : 16:58:34\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 18:28:34\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 17:53:48\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 16:47:19\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 17:07:39\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 17:20:50\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 18:09:03\n" +
        "    Tzait Hacochavim - 18 deg                        : 18:20:44\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 18:31:43\n" +
        "    Tzait Hacochavim - 26 deg                        : 19:09:00\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 17:28:34\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 17:40:34\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 17:19:42\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 17:58:34\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 17:32:29\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 18:04:34\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 17:36:45\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 17:08:34\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 16:48:21\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 16:53:17\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 16:54:55\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 16:56:13\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 17:03:30\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 17:03:58\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 17:11:31\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 17:20:50\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 17:13:34\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 17:18:34\n" +
        "            Midnight - Mid-night                     :  0:12:54\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:09:04\n"
    );
  }


  @Test
  public void oakParkMichigan() {

    // See YouTrack issue #90
    ZmanimLocation oakPark = new ZmanimLocation.Builder()
      .atLatitude( 42.5 )
      .atLongitude( -83.2 )
      .locationName( "Oak Park, Michigan" )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .build();

    ZmanimLocation oakParkWithElevation = new ZmanimLocation.Builder()
      .atLatitude( 42.5 )
      .atLongitude( -83.2 )
      .locationName( "Oak Park, Michigan" )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .atElevation( 189 )
      .build();

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.FEBRUARY, 13 );
    verifyZmanim( oakPark, date, erevShabbatZmanim,
      "Oak Park, Michigan\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:32:40\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:47:45\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:08:13\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:54\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:08\n" +
        "      Alot Hashachar - 26 deg                        :  5:14:27\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:32:40\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:20:40\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:29:43\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:02:40\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:13:59\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:56:40\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:08:44\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:40:26\n" +
        "       Talit/Tefilin - 11 deg                        :  6:36:02\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:33:18\n" +
        "              Hanetz - Sunrise                       :  7:32:40\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:32:40\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:47:25\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:06:42\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:15:27\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:32:49\n" +
        "      Sof Zman Shema - GR'A                          : 10:10:02\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:02:44\n" +
        "      Sof Zman Shema - MG'A                          :  9:34:02\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:10:02\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:48\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:17:45\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:38:34\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:25:02\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:30:42\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:22:02\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:28:04\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:10:41\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:47:25\n" +
        "     Sof Zman Tefila - GR'A                          : 11:02:30\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:33:52\n" +
        "     Sof Zman Tefila - MG'A                          : 10:38:30\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:22:30\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:19\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:27:38\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:41:31\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:32:30\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:36:16\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:30:30\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:34:31\n" +
        "             Chatzot - Mid-Day                       : 12:47:25\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:32:48\n" +
        "       Mincha Gedola - GR'A                          : 13:13:38\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:20:38\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:19:38\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:06:27\n" +
        "       Mincha Ketana - GR'A                          : 15:51:01\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:40:13\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:33:01\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:09:34\n" +
        "       Plag HaMincha - GR'A                          : 16:56:35\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:31:35\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:19:38\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:03:23\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:11:33\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:19:17\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:45:57\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:44:05\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:53:35\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:46:25\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:07:50\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 17:58:52\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:12:35\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:03:02\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:25:51\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:26:10\n" +
        "     Candle Lighting - 18 Minutes                    : 17:44:09\n" +
        "     Candle Lighting - 15 Minutes                    : 17:47:09\n" +
        "     Candle Lighting - 20 Minutes                    : 17:42:09\n" +
        "     Candle Lighting - 21 Minutes                    : 17:41:09\n" +
        "     Candle Lighting - 22 Minutes                    : 17:40:09\n" +
        "     Candle Lighting - 30 Minutes                    : 17:32:09\n" +
        "     Candle Lighting - 40 Minutes                    : 17:22:09\n" +
        "               Shkia - Sunset                        : 18:02:09\n" +
        "               Shkia - Sunset (with elevation)       : 18:02:09\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:09\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:13:57\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:09\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:10:58\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:00:39\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:23:40\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:31:11\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:02:09\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:47:04\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:17:12\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:33:56\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:44:59\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:26:32\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:36:51\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:46:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:20:19\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:02:09\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:14:09\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:05:06\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:32:09\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:20:50\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:38:09\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:26:05\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:42:09\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:18:03\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:22:05\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:23:25\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:24:29\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:30:30\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:30:53\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:37:10\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:44:59\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:09\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:09\n" +
        "            Midnight - Mid-night                     :  0:46:44\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:32:48\n"
    );

    verifyZmanim( oakParkWithElevation, date, erevShabbatZmanim,
      "Oak Park, Michigan\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:32:40\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:47:45\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:08:13\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:54\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:08\n" +
        "      Alot Hashachar - 26 deg                        :  5:14:27\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:32:40\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:20:40\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:29:43\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:02:40\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:13:59\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:56:40\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:08:44\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:40:26\n" +
        "       Talit/Tefilin - 11 deg                        :  6:36:02\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:33:18\n" +
        "              Hanetz - Sunrise                       :  7:32:40\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:30:09\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:47:25\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:06:42\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:15:27\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:32:49\n" +
        "      Sof Zman Shema - GR'A                          : 10:10:02\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:02:44\n" +
        "      Sof Zman Shema - MG'A                          :  9:34:02\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:10:02\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:48\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:17:45\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:38:34\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:25:02\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:30:42\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:22:02\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:28:04\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:10:41\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:47:25\n" +
        "     Sof Zman Tefila - GR'A                          : 11:02:30\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:33:52\n" +
        "     Sof Zman Tefila - MG'A                          : 10:38:30\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:22:30\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:19\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:27:38\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:41:31\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:32:30\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:36:16\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:30:30\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:34:31\n" +
        "             Chatzot - Mid-Day                       : 12:47:25\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:32:48\n" +
        "       Mincha Gedola - GR'A                          : 13:13:38\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:20:38\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:19:38\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:06:27\n" +
        "       Mincha Ketana - GR'A                          : 15:51:01\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:40:13\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:33:01\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:09:34\n" +
        "       Plag HaMincha - GR'A                          : 16:56:35\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:31:35\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:19:38\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:03:23\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:11:33\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:19:17\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:45:57\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:44:05\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:53:35\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:46:25\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:07:50\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 17:58:52\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:12:35\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:03:02\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:25:51\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:26:10\n" +
        "     Candle Lighting - 18 Minutes                    : 17:44:09\n" +
        "     Candle Lighting - 15 Minutes                    : 17:47:09\n" +
        "     Candle Lighting - 20 Minutes                    : 17:42:09\n" +
        "     Candle Lighting - 21 Minutes                    : 17:41:09\n" +
        "     Candle Lighting - 22 Minutes                    : 17:40:09\n" +
        "     Candle Lighting - 30 Minutes                    : 17:32:09\n" +
        "     Candle Lighting - 40 Minutes                    : 17:22:09\n" +
        "               Shkia - Sunset                        : 18:02:09\n" +
        "               Shkia - Sunset (with elevation)       : 18:04:40\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:09\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:13:57\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:09\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:10:58\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:00:39\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:23:40\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:31:11\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:02:09\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:47:04\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:17:12\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:33:56\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:44:59\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:26:32\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:36:51\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:46:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:20:19\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:02:09\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:14:09\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:05:06\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:32:09\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:20:50\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:38:09\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:26:05\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:42:09\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:18:03\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:22:05\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:23:25\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:24:29\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:30:30\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:30:53\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:37:10\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:44:59\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:09\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:09\n" +
        "            Midnight - Mid-night                     :  0:46:44\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:32:48\n"
    );
  }


  @Test
  public void november_26_2017() {

    final String haRambanSynagogueExpected = "HaRamban Synagogue\n" +
      "      Alot Hashachar - 120 Minutes                   :  4:16:32\n" +
      "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:33:20\n" +
      "      Alot Hashachar - 16.1 deg                      :  5:00:16\n" +
      "      Alot Hashachar - 18 deg                        :  4:51:02\n" +
      "      Alot Hashachar - 19.8 deg                      :  4:42:20\n" +
      "      Alot Hashachar - 26 deg                        :  4:12:36\n" +
      "      Alot Hashachar - 60 Minutes                    :  5:16:32\n" +
      "      Alot Hashachar - 72 Minutes                    :  5:04:32\n" +
      "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:14:37\n" +
      "      Alot Hashachar - 90 Minutes                    :  4:46:32\n" +
      "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:59:08\n" +
      "      Alot Hashachar - 96 Minutes                    :  4:40:32\n" +
      "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:53:58\n" +
      "       Talit/Tefilin - 10.2 deg                      :  5:29:14\n" +
      "       Talit/Tefilin - 11 deg                        :  5:25:17\n" +
      "       Talit/Tefilin - 11.5 deg                      :  5:22:49\n" +
      "              Hanetz - Sunrise                       :  6:16:32\n" +
      "              Hanetz - Sunrise (with elevation)      :  6:11:57\n" +
      "      Sof Zman Shema - 3 Hours before Chatzot        :  8:26:10\n" +
      "      Sof Zman Shema - 16.1 deg to Sunset            :  7:54:09\n" +
      "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:02:06\n" +
      "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:14:54\n" +
      "      Sof Zman Shema - GR'A                          :  8:51:21\n" +
      "      Sof Zman Shema - GR'A Kol Eliyahu              :  8:57:48\n" +
      "      Sof Zman Shema - MG'A                          :  8:15:21\n" +
      "      Sof Zman Shema - MG'A 120 Minutes              :  7:51:21\n" +
      "      Sof Zman Shema - MG'A 16.1 deg                 :  8:13:14\n" +
      "      Sof Zman Shema - MG'A 19.8 deg                 :  8:04:16\n" +
      "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  8:20:23\n" +
      "      Sof Zman Shema - MG'A 90 Minutes               :  8:06:21\n" +
      "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:12:39\n" +
      "      Sof Zman Shema - 96 Minutes                    :  8:03:21\n" +
      "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:10:04\n" +
      "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  7:57:45\n" +
      "     Sof Zman Tefila - 2 Hours before Chatzot        :  9:26:10\n" +
      "     Sof Zman Tefila - GR'A                          :  9:42:57\n" +
      "     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  9:15:00\n" +
      "     Sof Zman Tefila - MG'A                          :  9:18:57\n" +
      "     Sof Zman Tefila - MG'A 120 Minutes              :  9:02:57\n" +
      "     Sof Zman Tefila - MG'A 16.1 deg                 :  9:17:33\n" +
      "     Sof Zman Tefila - MG'A 19.8 deg                 :  9:11:34\n" +
      "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      :  9:22:19\n" +
      "     Sof Zman Tefila - MG'A 90 Minutes               :  9:12:57\n" +
      "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      :  9:17:09\n" +
      "     Sof Zman Tefila - MG'A 96 Minutes               :  9:10:57\n" +
      "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      :  9:15:26\n" +
      "             Chatzot - Mid-Day                       : 11:26:10\n" +
      "             Chatzot - Fixed Local Mid-Day           : 11:39:04\n" +
      "       Mincha Gedola - GR'A                          : 11:51:58\n" +
      "       Mincha Gedola - MG'A 16.1 deg                 : 11:58:21\n" +
      "       Mincha Gedola - MG'A 72 Minutes               : 11:57:58\n" +
      "       Mincha Gedola - Chacham Yosef Harari-Raful    : 11:45:15\n" +
      "       Mincha Ketana - GR'A                          : 14:26:46\n" +
      "       Mincha Ketana - MG'A 16.1 deg                 : 15:11:18\n" +
      "       Mincha Ketana - MG'A 72 Minutes               : 15:08:46\n" +
      "       Mincha Ketana - Chacham Yosef Harari-Raful    : 14:45:32\n" +
      "       Plag HaMincha - GR'A                          : 15:31:17\n" +
      "       Plag HaMincha - MG'A 120 Minutes              : 17:06:17\n" +
      "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 16:52:59\n" +
      "       Plag HaMincha - MG'A 16.1 deg                 : 16:31:42\n" +
      "       Plag HaMincha - MG'A 18 deg                   : 16:39:01\n" +
      "       Plag HaMincha - MG'A 19.8 deg                 : 16:45:55\n" +
      "       Plag HaMincha - MG'A 26 deg                   : 17:09:28\n" +
      "       Plag HaMincha - MG'A 60 Minutes               : 16:18:47\n" +
      "       Plag HaMincha - MG'A 72 Minutes               : 16:28:17\n" +
      "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 16:20:18\n" +
      "       Plag HaMincha - MG'A 90 Minutes               : 16:42:32\n" +
      "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 16:32:34\n" +
      "       Plag HaMincha - MG'A 96 Minutes               : 16:47:17\n" +
      "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 16:36:39\n" +
      "       Plag HaMincha - Chacham Yosef Harari-Raful    : 16:00:40\n" +
      "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 15:58:10\n" +
      "               Shkia - Sunset                        : 16:35:47\n" +
      "               Shkia - Sunset (with elevation)       : 16:40:22\n" +
      "       Latest Mincha - Mishna Berura                 : 16:35:47\n" +
      "       Latest Mincha - Hacham Ovadia Yosef           : 16:47:24\n" +
      "       Latest Mincha - Hacham Ben Sion               : 17:01:47\n" +
      "     Bein Hashmashot - 13 Degrees                    : 17:38:07\n" +
      "     Bein Hashmashot - 58.5 Minutes                  : 17:34:17\n" +
      "     Bein Hashmashot - 13.5 Minutes                  : 16:54:04\n" +
      "     Bein Hashmashot - 2 Stars                       : 17:01:57\n" +
      "    Tzait Hacochavim - MG'A 120 Minutes              : 18:35:47\n" +
      "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 18:19:00\n" +
      "    Tzait Hacochavim - 3.5 deg                       : 16:49:28\n" +
      "    Tzait Hacochavim - 6.5 deg                       : 17:04:39\n" +
      "    Tzait Hacochavim - 8.5 deg                       : 17:14:40\n" +
      "    Tzait Hacochavim - 16.1 deg                      : 17:52:06\n" +
      "    Tzait Hacochavim - 18 deg                        : 18:01:20\n" +
      "    Tzait Hacochavim - 19.8 deg                      : 18:10:03\n" +
      "    Tzait Hacochavim - 26 deg                        : 18:39:48\n" +
      "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 17:35:47\n" +
      "    Tzait Hacochavim - Rambam / Rabainu Tam          : 17:47:47\n" +
      "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 17:37:43\n" +
      "    Tzait Hacochavim - MG'A 90 Minutes               : 18:05:47\n" +
      "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 17:53:11\n" +
      "    Tzait Hacochavim - 96 Minutes                    : 18:11:47\n" +
      "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 17:58:21\n" +
      "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 17:15:47\n" +
      "    Tzait Hacochavim - Geonim 3.65 deg               : 16:50:13\n" +
      "    Tzait Hacochavim - Geonim 4.37 deg               : 16:53:53\n" +
      "    Tzait Hacochavim - Geonim 4.61 deg               : 16:55:06\n" +
      "    Tzait Hacochavim - Geonim 4.8 deg                : 16:56:04\n" +
      "    Tzait Hacochavim - Geonim 5.88 deg               : 17:01:31\n" +
      "    Tzait Hacochavim - Geonim 5.95 deg               : 17:01:52\n" +
      "    Tzait Hacochavim - Geonim 7.083 deg              : 17:07:34\n" +
      "    Tzait Hacochavim - Geonim 8.5 deg                : 17:14:40\n" +
      "    Tzait Hacochavim - Seattle Sephardic             : 17:20:47\n" +
      "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 17:25:47\n" +
      "            Midnight - Mid-night                     : 23:26:35\n" +
      "            Midnight - Fixed Local Mid-Night         : 23:39:04\n";

    HebrewDate date = HebrewCalendar.getForDate( 2017, Calendar.NOVEMBER, 26 );
    verifyZmanim( haRambanSynagogue, date, dailyZmanim, haRambanSynagogueExpected );
  }


  private void verifyZmanim(
    ZmanimLocation    location,
    HebrewDate        date,
    List<Zmanim.Type> expectedZmanim,
    String            expected
  ) {

    List<Zmanim> zmanimForDay = date.getZmanim( location );

    List<Zmanim.Type> day = new ArrayList<>();
    for ( Zmanim zmanim : zmanimForDay ) {
      day.add( zmanim.type );
    }
    Assert.assertEquals( expectedZmanim.toString(), day.toString() );

    StringBuilder output = new StringBuilder();
    output.append( location.getLocationName() ).append( "\n" );
    for ( Zmanim zmanim : zmanimForDay ) {
      for ( Zman zman : zmanim.asList() ) {
        Calendar zmanTime = zman.getTime();
        if ( zmanTime != null ) {
          output.append( String.format( "%20s - %-30s: %2d:%02d:%02d\n", zmanim.type.name, zman.name,
            zmanTime.get( Calendar.HOUR_OF_DAY ), zmanTime.get( Calendar.MINUTE ), zmanTime.get( Calendar.SECOND ) ) );
        }
      }
    }

    Assert.assertEquals( expected, output.toString() );
  }


} // End of UT_Zmanim class
