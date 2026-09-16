/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.ZmanimBaseTest;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Spot-checks some dates to verify the expected zmanim are present and properly calculated.
 */
@SuppressWarnings( { "MagicNumber", "SpellCheckingInspection" } )
public class UT_ZmanimWithElevation
  extends ZmanimBaseTest {


  /**
   * Just a regular day....
   */
  @Test
  public void october_19_2014() {

    final String ezraBessarothExpected =
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:32:14\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:45:06\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:57:40\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:02:25\n" +
        "      Alot Hashachar - 18 deg                        :  5:51:07\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:40:23\n" +
        "      Alot Hashachar - 26 deg                        :  5:02:57\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:32:14\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:20:14\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:27:57\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:02:14\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:11:53\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:56:14\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:06:32\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:52:38\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:41:36\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:37:26\n" +
        "       Talit/Tefilin - 11 deg                        :  6:32:41\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:29:43\n" +
        "              Hanetz - Sunrise                       :  7:33:46\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:32:14\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:54:03\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:05:33\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:14:35\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:34:42\n" +
        "      Sof Zman Shema - GR'A                          : 10:12:55\n" +
        "      Sof Zman Shema - MG'A                          :  9:36:55\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:12:55\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:58\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:16:56\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:40:46\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:27:55\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:32:44\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:24:55\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:30:04\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:10:04\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:11:23\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:30:05\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:35:44\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:35:39\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:44:39\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:20:39\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:54:03\n" +
        "     Sof Zman Tefila - GR'A                          : 11:06:28\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:36:57\n" +
        "     Sof Zman Tefila - MG'A                          : 10:42:28\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:26:28\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:36:29\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:29:07\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:45:03\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:36:28\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:39:41\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:34:28\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:37:54\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:05:27\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:16:47\n" +
        "             Chatzot - Mid-Day                       : 12:54:03\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:20:22\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:27:46\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:24:03\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:24:03\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:26:22\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:12:35\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:20:37\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:24:03\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:25:14\n" +
        "       Mincha Ketana - GR'A                          : 16:01:03\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:53:19\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:43:03\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:19:20\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:02:50\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:55:23\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:07:30\n" +
        "       Plag HaMincha - GR'A                          : 17:08:00\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:43:00\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:32:49\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:18:58\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:27:53\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:36:22\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 19:05:53\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:55:30\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 18:05:00\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:58:53\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:19:15\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:11:37\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:24:00\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:15:51\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:37:09\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:38:37\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:31:03\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:58:39\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:10:25\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:13:24\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:11:14\n" +
        "               Shkia - Sunset                        : 18:13:25\n" +
        "               Shkia - Sunset (with elevation)       : 18:14:57\n" +
        "       Latest Mincha - Mishna Berura                 : 18:13:25\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:25:25\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:39:25\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:27:41\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:13:27\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:37:37\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:46:02\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:14:57\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:02:05\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:29:37\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:44:39\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:47:38\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:59:34\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:44:37\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:55:53\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 20:06:35\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:43:54\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:14:57\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:26:57\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:19:14\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:44:57\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:35:18\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:50:57\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:40:39\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:54:57\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:30:31\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:34:52\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:36:18\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:37:27\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:43:56\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:44:21\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:51:07\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:59:34\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 19:03:25\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:58:25\n" +
        "            Midnight - Mid-night                     :  0:53:58\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n";

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 19 );
    verifyZmanim( ezraBessaroth, date, true, dailyZmanim, ezraBessarothExpected );
  }


  @Test
  public void october_19_2014_yeshiva_university() {
    final String yeshivaUniversityExpected =
      "Yeshiva University\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:10:04\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:19:58\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:45:43\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:49:57\n" +
        "      Alot Hashachar - 18 deg                        :  5:39:54\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:30:21\n" +
        "      Alot Hashachar - 26 deg                        :  4:57:18\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:10:04\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:58:04\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:04:01\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:40:04\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:47:30\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:34:04\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:42:00\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:34:46\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:24:55\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:21:12\n" +
        "       Talit/Tefilin - 11 deg                        :  6:16:58\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:14:18\n" +
        "              Hanetz - Sunrise                       :  7:11:22\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:10:04\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:40:43\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:55:07\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:03:11\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:15:40\n" +
        "      Sof Zman Shema - GR'A                          :  9:55:12\n" +
        "      Sof Zman Shema - MG'A                          :  9:19:12\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:55:12\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:15:07\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:05:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:22:10\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:10:12\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:13:55\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:07:12\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:11:10\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:59:09\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:53:49\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:17:48\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:22:50\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:17:53\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:26:53\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:02:53\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:40:43\n" +
        "     Sof Zman Tefila - GR'A                          : 10:50:15\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:19:33\n" +
        "     Sof Zman Tefila - MG'A                          : 10:26:15\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:10:15\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:23:30\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:16:58\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:28:14\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:20:15\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:22:43\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:18:15\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:20:53\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:49:20\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:00:30\n" +
        "             Chatzot - Mid-Day                       : 12:40:43\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:55:42\n" +
        "       Mincha Gedola - GR'A                          : 13:07:51\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:14:29\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:10:43\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:10:43\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:13:51\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:59:15\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:08:05\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:10:43\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:12:10\n" +
        "       Mincha Ketana - GR'A                          : 15:53:00\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:39:39\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:35:00\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:10:54\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:54:36\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:47:56\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 15:59:24\n" +
        "       Plag HaMincha - GR'A                          : 17:01:48\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:36:48\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:28:57\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:05:08\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:13:05\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:20:37\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:46:42\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:49:18\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:58:48\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:54:05\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:13:03\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:07:10\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:17:48\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:11:31\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:30:45\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:29:06\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:22:20\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:53:27\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:03:59\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:06:37\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:05:00\n" +
        "               Shkia - Sunset                        : 18:09:18\n" +
        "               Shkia - Sunset (with elevation)       : 18:10:36\n" +
        "       Latest Mincha - Mishna Berura                 : 18:09:18\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:21:38\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:35:18\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:15:30\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:09:06\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:29:21\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:38:18\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:10:36\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:00:42\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:23:41\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:37:04\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:39:45\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:50:23\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:30:37\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:40:39\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:50:11\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:23:09\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:10:36\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:22:36\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:16:40\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:40:36\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:33:10\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:46:36\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:38:41\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:50:36\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:24:30\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:28:21\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:29:38\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:30:40\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:36:26\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:36:48\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:42:51\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:50:23\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:59:18\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:54:18\n" +
        "            Midnight - Mid-night                     :  0:40:38\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:55:42\n";

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 19 );
    verifyZmanim( yeshivaUniviersity, date, true, dailyZmanim, yeshivaUniversityExpected );
  }

  @Test
  public void october_19_2014_haramban() {

    final String haRambanSynagogueExpected =
      "HaRamban Synagogue\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:40:43\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:46:20\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:28:48\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:32:34\n" +
        "      Alot Hashachar - 18 deg                        :  5:23:37\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:15:09\n" +
        "      Alot Hashachar - 26 deg                        :  4:45:56\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:40:43\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:28:43\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:32:05\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:10:43\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:14:56\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:04:43\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:09:13\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:12:29\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:03:43\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:00:24\n" +
        "       Talit/Tefilin - 11 deg                        :  5:56:37\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:54:16\n" +
        "              Hanetz - Sunrise                       :  6:44:59\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:40:43\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:24:08\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:41:10\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:47:33\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:50:48\n" +
        "      Sof Zman Shema - GR'A                          :  9:32:16\n" +
        "      Sof Zman Shema - MG'A                          :  8:56:16\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:32:16\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  8:58:11\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:49:28\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  8:57:57\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  8:47:16\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:49:23\n" +
        "      Sof Zman Shema - 96 Minutes                    :  8:44:16\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:46:31\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:43:58\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:32:36\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:01:21\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:05:49\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  8:54:53\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:03:53\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  9:39:53\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:24:08\n" +
        "     Sof Zman Tefila - GR'A                          : 10:29:27\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  9:57:02\n" +
        "     Sof Zman Tefila - MG'A                          : 10:05:27\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              :  9:49:27\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:06:43\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:00:55\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:06:35\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               :  9:59:27\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:00:52\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               :  9:57:27\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      :  9:58:57\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:29:41\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:39:37\n" +
        "             Chatzot - Mid-Day                       : 12:24:08\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:39:04\n" +
        "       Mincha Gedola - GR'A                          : 12:52:25\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 12:58:04\n" +
        "       Mincha Gedola - 30 Minutes                    : 12:54:08\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 12:54:08\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 12:58:25\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:42:38\n" +
        "       Mincha Gedola - Baal HaTanya                  : 12:52:22\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 12:54:08\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 12:55:58\n" +
        "       Mincha Ketana - GR'A                          : 15:43:59\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:23:41\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:25:59\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:01:21\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:43:35\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:37:38\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 15:50:20\n" +
        "       Plag HaMincha - GR'A                          : 16:55:28\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:30:28\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:26:00\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 17:49:22\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 17:56:26\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:03:08\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:26:14\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:42:58\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:52:28\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:49:47\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:06:43\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:03:22\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:11:28\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:07:54\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:24:09\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:17:15\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:11:14\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:48:22\n" +
        "       Plag HaMincha - Baal HaTanya                  : 16:54:56\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 16:57:15\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 16:58:38\n" +
        "               Shkia - Sunset                        : 18:02:41\n" +
        "               Shkia - Sunset (with elevation)       : 18:06:57\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:41\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:15:23\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:41\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:01:34\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:05:27\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:18:58\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:30:43\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:06:57\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:01:19\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:15:26\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:27:20\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:29:42\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:39:11\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:15:02\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:23:58\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:32:26\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:01:37\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:06:57\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:18:57\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:15:34\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:36:57\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:32:43\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:42:57\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:38:26\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:46:57\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:16:09\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:19:35\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:20:43\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:21:38\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:26:46\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:27:06\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:32:28\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:39:11\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:41\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:41\n" +
        "            Midnight - Mid-night                     :  0:24:03\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:39:04\n";

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 19 );
    verifyZmanim( haRambanSynagogue, date, true, dailyZmanim, haRambanSynagogueExpected );
  }

    /**
     * A regular Erev Shabbat
     */
  @Test
  public void october_24_2014() {

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 24 );
    verifyZmanim( ezraBessaroth, date, true, erevShabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:39:35\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:55:10\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:04:31\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:09:15\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:59\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:47:17\n" +
        "      Alot Hashachar - 26 deg                        :  5:10:09\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:39:35\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:27:35\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:36:56\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:09:35\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:21:16\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:03:35\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:16:03\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:59:36\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:48:31\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:44:20\n" +
        "       Talit/Tefilin - 11 deg                        :  6:39:34\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:36:35\n" +
        "              Hanetz - Sunrise                       :  7:41:08\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:39:35\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:53:15\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:08:28\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:17:36\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:39:13\n" +
        "      Sof Zman Shema - GR'A                          : 10:16:13\n" +
        "      Sof Zman Shema - MG'A                          :  9:40:13\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:16:13\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:31:01\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:20:01\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:44:53\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:31:13\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:37:03\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:28:13\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:34:27\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:13:02\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:14:40\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:33:31\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:39:09\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:39:19\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:48:19\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:24:19\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:53:15\n" +
        "     Sof Zman Tefila - GR'A                          : 11:08:25\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:39:59\n" +
        "     Sof Zman Tefila - MG'A                          : 10:44:25\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:28:25\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:38:16\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:30:56\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:47:32\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:38:25\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:42:19\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:36:25\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:40:35\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:07:24\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:19:14\n" +
        "             Chatzot - Mid-Day                       : 12:53:15\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:18:57\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:26:24\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:23:15\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:23:15\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:24:57\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:11:54\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:19:12\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:23:15\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:23:48\n" +
        "       Mincha Ketana - GR'A                          : 15:55:35\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:48:10\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:37:35\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:14:12\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:57:22\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:49:57\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:02:20\n" +
        "       Plag HaMincha - GR'A                          : 17:00:50\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:35:50\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:23:30\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:12:14\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:21:08\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:29:35\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:58:53\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:48:20\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:57:50\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:50:26\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:12:05\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:02:50\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:16:50\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:06:58\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:30:09\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:31:46\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:24:09\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:51:26\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:03:17\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:06:21\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:04:13\n" +
        "     Candle Lighting - 18 Minutes                    : 17:46:33\n" +
        "     Candle Lighting - 15 Minutes                    : 17:49:33\n" +
        "     Candle Lighting - 20 Minutes                    : 17:44:33\n" +
        "     Candle Lighting - 21 Minutes                    : 17:43:33\n" +
        "     Candle Lighting - 22 Minutes                    : 17:42:33\n" +
        "     Candle Lighting - 30 Minutes                    : 17:34:33\n" +
        "     Candle Lighting - 40 Minutes                    : 17:24:33\n" +
        "               Shkia - Sunset                        : 18:04:33\n" +
        "               Shkia - Sunset (with elevation)       : 18:06:06\n" +
        "       Latest Mincha - Mishna Berura                 : 18:04:33\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:16:14\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:30:33\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:19:21\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:04:36\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:29:08\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:37:18\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:06:06\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:50:31\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:20:56\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:36:06\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:39:07\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:51:08\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:36:18\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:47:33\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:58:13\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:35:14\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:06:06\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:18:06\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:08:45\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:36:06\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:24:25\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:42:06\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:29:38\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:46:06\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:21:51\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:26:14\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:27:41\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:28:51\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:35:23\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:35:48\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:42:38\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:51:08\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:54:33\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:49:33\n" +
        "            Midnight - Mid-night                     :  0:53:12\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  /**
   * A regular Shabbat
   */
  @Test
  public void october_25_2014() {

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 25 );
    verifyZmanim( ezraBessaroth, date, true, shabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:41:04\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:57:11\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:05:52\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:10:37\n" +
        "      Alot Hashachar - 18 deg                        :  5:59:21\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:39\n" +
        "      Alot Hashachar - 26 deg                        :  5:11:34\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:41:04\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:29:04\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:38:44\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:11:04\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:23:09\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:05:04\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:17:57\n" +
        "       Talit/Tefilin - 7.65 deg                      :  7:01:00\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:49:54\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:45:43\n" +
        "       Talit/Tefilin - 11 deg                        :  6:40:56\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:37:57\n" +
        "              Hanetz - Sunrise                       :  7:42:37\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:41:04\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:53:08\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:09:04\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:18:13\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:40:09\n" +
        "      Sof Zman Shema - GR'A                          : 10:16:54\n" +
        "      Sof Zman Shema - MG'A                          :  9:40:54\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:16:54\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:31:38\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:20:39\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:45:44\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:31:54\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:37:56\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:28:54\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:35:20\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:13:38\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:15:21\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:34:12\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:39:50\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:40:04\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:49:04\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:25:04\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:53:08\n" +
        "     Sof Zman Tefila - GR'A                          : 11:08:50\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:40:37\n" +
        "     Sof Zman Tefila - MG'A                          : 10:44:50\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:28:50\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:38:39\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:31:19\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:48:04\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:38:50\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:42:52\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:36:50\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:41:08\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:07:49\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:19:44\n" +
        "             Chatzot - Mid-Day                       : 12:53:08\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:18:42\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:26:10\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:23:08\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:23:08\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:24:42\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:11:48\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:18:57\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:23:08\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:23:32\n" +
        "       Mincha Ketana - GR'A                          : 15:54:32\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:47:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:36:32\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:13:13\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:56:20\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:48:55\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:01:20\n" +
        "       Plag HaMincha - GR'A                          : 16:59:28\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:34:28\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:21:42\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:10:57\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:19:51\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:28:18\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:57:34\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:46:58\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:56:28\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:48:48\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:10:43\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:01:09\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:15:28\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:05:15\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:28:48\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:30:27\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:22:50\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:50:02\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:01:54\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:05:00\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:02:52\n" +
        "               Shkia - Sunset                        : 18:02:50\n" +
        "               Shkia - Sunset (with elevation)       : 18:04:23\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:50\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:14:28\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:50\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:17:46\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:02:53\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:27:30\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:35:37\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:04:23\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:48:17\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:19:16\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:34:28\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:37:29\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:49:30\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:34:43\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:45:58\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:56:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:33:36\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:04:23\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:16:23\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:06:43\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:34:23\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:22:18\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:40:23\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:27:30\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:44:23\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:20:11\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:24:34\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:26:02\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:27:11\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:33:44\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:34:09\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:41:00\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:49:30\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:50\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:50\n" +
        "            Havdalah - MG'A 120 Minutes              : 20:04:23\n" +
        "            Havdalah - GR'A 120 Minutes Zmaniyot     : 19:48:17\n" +
        "            Havdalah - 3.5 deg                       : 18:19:16\n" +
        "            Havdalah - 6.5 deg                       : 18:37:29\n" +
        "            Havdalah - 8.5 deg                       : 18:49:30\n" +
        "            Havdalah - 16.1 deg                      : 19:34:43\n" +
        "            Havdalah - 18 deg                        : 19:45:58\n" +
        "            Havdalah - 19.8 deg                      : 19:56:37\n" +
        "            Havdalah - 26 deg                        : 20:33:36\n" +
        "            Havdalah - Chavas Yair and Divray Malkiel: 19:04:23\n" +
        "            Havdalah - Rambam / Rabainu Tam          : 19:16:23\n" +
        "            Havdalah - 72 Minutes Zmaniyot           : 19:06:43\n" +
        "            Havdalah - MG'A 90 Minutes               : 19:34:23\n" +
        "            Havdalah - GR'A 90 Minutes Zmaniyot      : 19:22:18\n" +
        "            Havdalah - 96 Minutes                    : 19:40:23\n" +
        "            Havdalah - GR'A 96 Minutes Zmaniyot      : 19:27:30\n" +
        "            Havdalah - Chacham Yosef Harari-Raful    : 18:44:23\n" +
        "            Havdalah - Geonim 3.65 deg               : 18:20:11\n" +
        "            Havdalah - Geonim 4.37 deg               : 18:24:34\n" +
        "            Havdalah - Geonim 4.61 deg               : 18:26:02\n" +
        "            Havdalah - Geonim 4.8 deg                : 18:27:11\n" +
        "            Havdalah - Geonim 5.88 deg               : 18:33:44\n" +
        "            Havdalah - Geonim 5.95 deg               : 18:34:09\n" +
        "            Havdalah - Geonim 7.083 deg              : 18:41:00\n" +
        "            Havdalah - Geonim 8.5 deg                : 18:49:30\n" +
        "            Havdalah - Rabbi Moshe Feinstein         : 18:52:50\n" +
        "            Havdalah - Seattle Sephardic             : 18:47:50\n" +
        "            Midnight - Mid-night                     :  0:53:04\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  /**
   * This is a day where Pesach begins as Shabbat ends.
   */
  @Test
  public void april_4_2015() {

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.APRIL, 4 );
    verifyZmanim( ezraBessaroth, date, true, erevPesachOnShabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:40:31\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:29:51\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:00:58\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:06:21\n" +
        "      Alot Hashachar - 18 deg                        :  4:53:28\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:40:54\n" +
        "      Alot Hashachar - 26 deg                        :  3:53:54\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:40:31\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:28:31\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:22:07\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:10:31\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:02:31\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:04:31\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:55:59\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:00:32\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:49:01\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:44:36\n" +
        "       Talit/Tefilin - 11 deg                        :  5:39:33\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:36:22\n" +
        "              Hanetz - Sunrise                       :  6:42:01\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:40:31\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:12:10\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:45:52\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:55:02\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:07:42\n" +
        "      Sof Zman Shema - GR'A                          :  9:56:30\n" +
        "      Sof Zman Shema - MG'A                          :  9:20:30\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:56:30\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:09:32\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:56:52\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:17:18\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:11:30\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:07:30\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:08:30\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:04:14\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:50:26\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:55:00\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:01:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:07:42\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:09:47\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:18:47\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  9:54:47\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:12:10\n" +
        "     Sof Zman Tefila - GR'A                          : 11:01:50\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:22:54\n" +
        "     Sof Zman Tefila - MG'A                          : 10:37:50\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:21:50\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:30:35\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:22:11\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:35:42\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:31:50\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:29:10\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:29:50\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:26:59\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:00:50\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:59:33\n" +
        "             Chatzot - Mid-Day                       : 13:12:10\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:45:09\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:53:15\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:42:10\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:45:09\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:51:09\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:30:53\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:45:24\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:45:24\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:49:25\n" +
        "       Mincha Ketana - GR'A                          : 17:01:08\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 17:56:25\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:43:08\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:16:28\n" +
        "       Mincha Ketana - Baal HaTanya                  : 17:02:53\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:54:32\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:59:42\n" +
        "       Plag HaMincha - GR'A                          : 18:22:48\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 19:57:48\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:06:14\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:37:45\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:48:02\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 19:58:04\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:35:47\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:10:18\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:19:48\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:24:51\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:34:03\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:40:22\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:38:48\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:45:32\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:50:28\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:53:41\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:45:47\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:12:59\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:25:10\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 18:27:43\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 18:22:05\n" +
        "               Shkia - Sunset                        : 19:42:56\n" +
        "               Shkia - Sunset (with elevation)       : 19:44:27\n" +
        "       Latest Mincha - Mishna Berura                 : 19:42:56\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:57:35\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:08:56\n" +
        "     Bein Hashmashot - 13 Degrees                    : 21:00:07\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:42:57\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:07:34\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:17:41\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:44:27\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 21:55:07\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 19:59:04\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:14:22\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:17:27\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:29:53\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:19:05\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:32:04\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 21:44:43\n" +
        "    Tzait Hacochavim - 26 deg                        : 22:32:17\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:44:27\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:56:27\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:02:51\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:14:27\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:22:27\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:20:27\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:28:59\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:24:27\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 19:59:59\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 20:04:22\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 20:05:50\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 20:07:00\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 20:13:38\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 20:14:04\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:21:04\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:29:53\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:32:56\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:27:56\n" +
        "     Candle Lighting - MG'A 120 Minutes              : 21:44:27\n" +
        "     Candle Lighting - GR'A 120 Minutes Zmaniyot     : 21:55:07\n" +
        "     Candle Lighting - 3.5 deg                       : 19:59:04\n" +
        "     Candle Lighting - 6.5 deg                       : 20:17:27\n" +
        "     Candle Lighting - 8.5 deg                       : 20:29:53\n" +
        "     Candle Lighting - 16.1 deg                      : 21:19:05\n" +
        "     Candle Lighting - 18 deg                        : 21:32:04\n" +
        "     Candle Lighting - 19.8 deg                      : 21:44:43\n" +
        "     Candle Lighting - 26 deg                        : 22:32:17\n" +
        "     Candle Lighting - Chavas Yair and Divray Malkiel: 20:44:27\n" +
        "     Candle Lighting - Rambam / Rabainu Tam          : 20:56:27\n" +
        "     Candle Lighting - 72 Minutes Zmaniyot           : 21:02:51\n" +
        "     Candle Lighting - MG'A 90 Minutes               : 21:14:27\n" +
        "     Candle Lighting - GR'A 90 Minutes Zmaniyot      : 21:22:27\n" +
        "     Candle Lighting - 96 Minutes                    : 21:20:27\n" +
        "     Candle Lighting - GR'A 96 Minutes Zmaniyot      : 21:28:59\n" +
        "     Candle Lighting - Chacham Yosef Harari-Raful    : 20:24:27\n" +
        "     Candle Lighting - Geonim 3.65 deg               : 19:59:59\n" +
        "     Candle Lighting - Geonim 4.37 deg               : 20:04:22\n" +
        "     Candle Lighting - Geonim 4.61 deg               : 20:05:50\n" +
        "     Candle Lighting - Geonim 4.8 deg                : 20:07:00\n" +
        "     Candle Lighting - Geonim 5.88 deg               : 20:13:38\n" +
        "     Candle Lighting - Geonim 5.95 deg               : 20:14:04\n" +
        "     Candle Lighting - Geonim 7.083 deg              : 20:21:04\n" +
        "     Candle Lighting - Geonim 8.5 deg                : 20:29:53\n" +
        "     Candle Lighting - Rabbi Moshe Feinstein         : 20:32:56\n" +
        "            Midnight - Mid-night                     :  1:12:01\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  /**
   * Erev pesach, which has a number of additional zmanim
   */
  @Test
  public void april_14_2014() {

    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.APRIL, 14 );
    verifyZmanim( ezraBessaroth, date, true, erevPesachZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:20:31\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:04:06\n" +
        "      Alot Hashachar - Baal HaTanya                  :  4:36:20\n" +
        "      Alot Hashachar - 16.1 deg                      :  4:42:08\n" +
        "      Alot Hashachar - 18 deg                        :  4:28:12\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:14:27\n" +
        "      Alot Hashachar - 26 deg                        :  3:20:34\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:20:31\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:08:31\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  4:58:40\n" +
        "      Alot Hashachar - 90 Minutes                    :  4:50:31\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:38:13\n" +
        "      Alot Hashachar - 96 Minutes                    :  4:44:31\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:31:23\n" +
        "       Talit/Tefilin - 7.65 deg                      :  5:39:19\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:27:18\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:22:42\n" +
        "       Talit/Tefilin - 11 deg                        :  5:17:23\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:14:03\n" +
        "              Hanetz - Sunrise                       :  6:22:03\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:20:31\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:09:23\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:31:21\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:40:46\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:53:45\n" +
        "      Sof Zman Shema - GR'A                          :  9:45:08\n" +
        "      Sof Zman Shema - MG'A                          :  9:09:08\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:45:08\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  8:56:05\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:42:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:04:12\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:00:08\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:53:58\n" +
        "      Sof Zman Shema - 96 Minutes                    :  8:57:08\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:50:34\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:36:03\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:43:36\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  8:48:38\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  8:55:36\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  8:59:47\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:08:47\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  9:44:47\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:09:23\n" +
        "     Sof Zman Tefila - GR'A                          : 10:53:20\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:12:06\n" +
        "     Sof Zman Tefila - MG'A                          : 10:29:20\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:13:20\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:20:44\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:11:36\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:26:03\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:23:20\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:19:14\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:21:20\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:16:57\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:52:19\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:52:53\n" +
        "     Achilat Chametz - 2 Hours before Chatzot        : 11:09:23\n" +
        "     Achilat Chametz - GR'A                          : 10:53:20\n" +
        "     Achilat Chametz - Chacham Yosef Harari-Raful    : 10:12:06\n" +
        "     Achilat Chametz - MG'A                          : 10:29:20\n" +
        "     Achilat Chametz - MG'A 120 Minutes              : 10:13:20\n" +
        "     Achilat Chametz - MG'A 16.1 deg                 : 10:20:44\n" +
        "     Achilat Chametz - MG'A 19.8 deg                 : 10:11:36\n" +
        "     Achilat Chametz - MG'A 72 Minutes Zmaniyot      : 10:29:20\n" +
        "     Achilat Chametz - MG'A 90 Minutes               : 10:23:20\n" +
        "     Achilat Chametz - MG'A 90 Minutes Zmaniyot      : 10:19:14\n" +
        "     Achilat Chametz - MG'A 96 Minutes               : 10:21:20\n" +
        "     Achilat Chametz - MG'A 96 Minutes Zmaniyot      : 10:16:57\n" +
        "     Achilat Chametz - Baal HaTanya                  : 10:52:19\n" +
        "        Biur Chametz - GR'A                          : 12:01:32\n" +
        "        Biur Chametz - MG'A 72 Minutes               : 11:49:32\n" +
        "        Biur Chametz - MG'A 16.1 deg                 : 11:45:22\n" +
        "        Biur Chametz - Baal HaTanya                  : 12:01:02\n" +
        "             Chatzot - Mid-Day                       : 13:09:23\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:43:50\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:52:21\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:39:23\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:43:50\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:49:50\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:28:00\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:44:06\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:44:06\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:48:16\n" +
        "       Mincha Ketana - GR'A                          : 17:08:27\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 18:06:17\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:50:27\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:23:04\n" +
        "       Mincha Ketana - Baal HaTanya                  : 17:10:15\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 17:01:19\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 17:08:10\n" +
        "       Plag HaMincha - GR'A                          : 18:33:42\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 20:08:42\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:21:42\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:52:06\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 20:03:15\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 20:14:16\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:57:45\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:21:12\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:30:42\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:38:30\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:44:57\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:54:42\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:49:42\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 20:00:06\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 19:01:01\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 19:05:28\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:57:15\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:23:27\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:36:08\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 18:38:34\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 18:33:34\n" +
        "     Candle Lighting - 18 Minutes                    : 19:39:25\n" +
        "     Candle Lighting - 15 Minutes                    : 19:42:25\n" +
        "     Candle Lighting - 20 Minutes                    : 19:37:25\n" +
        "     Candle Lighting - 21 Minutes                    : 19:36:25\n" +
        "     Candle Lighting - 22 Minutes                    : 19:35:25\n" +
        "     Candle Lighting - 30 Minutes                    : 19:27:25\n" +
        "     Candle Lighting - 40 Minutes                    : 19:17:25\n" +
        "               Shkia - Sunset                        : 19:57:25\n" +
        "               Shkia - Sunset (with elevation)       : 19:58:57\n" +
        "       Latest Mincha - Mishna Berura                 : 19:57:25\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 20:12:42\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:23:25\n" +
        "     Bein Hashmashot - 13 Degrees                    : 21:17:39\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:57:27\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:23:11\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:33:58\n" +
        "           Fast Ends - MG'A 120 Minutes              : 21:58:57\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 22:15:22\n" +
        "           Fast Ends - 3.5 deg                       : 20:13:57\n" +
        "           Fast Ends - Baal HaTanya                  : 20:29:44\n" +
        "           Fast Ends - 6.5 deg                       : 20:32:56\n" +
        "           Fast Ends - 8.5 deg                       : 20:45:51\n" +
        "           Fast Ends - 16.1 deg                      : 21:37:54\n" +
        "           Fast Ends - 18 deg                        : 21:51:58\n" +
        "           Fast Ends - 19.8 deg                      : 22:05:52\n" +
        "           Fast Ends - 26 deg                        : 23:00:40\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 20:58:57\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 21:10:57\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 21:20:48\n" +
        "           Fast Ends - MG'A 90 Minutes               : 21:28:57\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 21:41:16\n" +
        "           Fast Ends - 96 Minutes                    : 21:34:57\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 21:48:05\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 20:38:57\n" +
        "           Fast Ends - Geonim 3.65 deg               : 20:14:53\n" +
        "           Fast Ends - Geonim 4.37 deg               : 20:19:25\n" +
        "           Fast Ends - Geonim 4.61 deg               : 20:20:55\n" +
        "           Fast Ends - Geonim 4.8 deg                : 20:22:07\n" +
        "           Fast Ends - Geonim 5.88 deg               : 20:28:58\n" +
        "           Fast Ends - Geonim 5.95 deg               : 20:29:25\n" +
        "           Fast Ends - Geonim 7.083 deg              : 20:36:41\n" +
        "           Fast Ends - Geonim 8.5 deg                : 20:45:51\n" +
        "           Fast Ends - Rabbi Moshe Feinstein         : 20:47:25\n" +
        "           Fast Ends - Seattle Sephardic             : 20:27:25\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:58:57\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 22:15:22\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 20:13:57\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:29:44\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:32:56\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:45:51\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:37:54\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:51:58\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 22:05:52\n" +
        "    Tzait Hacochavim - 26 deg                        : 23:00:40\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:58:57\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 21:10:57\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:20:48\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:28:57\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:41:16\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:34:57\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:48:05\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:38:57\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 20:14:53\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 20:19:25\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 20:20:55\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 20:22:07\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 20:28:58\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 20:29:25\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:36:41\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:45:51\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:47:25\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:42:25\n" +
        "            Midnight - Mid-night                     :  1:09:16\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  /**
   * Erev pesach which falls on Shabbat
   */
  @Test
  public void march_27_2021() {

    // NOTE: There's no candle lighting when Pesach starts motzae Shabbat...
    List<Zmanim.Type> erevPesachShabbatZmanim = new ArrayList<>( erevPesachZmanim );
    erevPesachShabbatZmanim.remove( Zmanim.Type.CandleLighting );

    HebrewDate date = HebrewCalendar.getForDate( 2021, Calendar.MARCH, 27 );
    verifyZmanim( ezraBessaroth, date, true, erevPesachShabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:55:29\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:49:06\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:18:22\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:23:32\n" +
        "      Alot Hashachar - 18 deg                        :  5:11:12\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:59:16\n" +
        "      Alot Hashachar - 26 deg                        :  4:15:32\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:55:29\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:43:29\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:39:39\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:25:29\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:20:42\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:19:29\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:14:22\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:16:07\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:04:51\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:00:33\n" +
        "       Talit/Tefilin - 11 deg                        :  5:55:37\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:52:32\n" +
        "              Hanetz - Sunrise                       :  6:56:59\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:55:29\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:14:23\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:56:07\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:05:08\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:18:12\n" +
        "      Sof Zman Shema - GR'A                          : 10:05:05\n" +
        "      Sof Zman Shema - MG'A                          :  9:29:05\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:05:05\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:19:12\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:07:06\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:27:10\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:20:05\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:17:41\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:17:05\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:14:32\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:00:36\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:03:36\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:10:08\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:16:18\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:17:17\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:26:17\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:02:17\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:14:23\n" +
        "     Sof Zman Tefila - GR'A                          : 11:08:17\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:31:03\n" +
        "     Sof Zman Tefila - MG'A                          : 10:44:17\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:28:17\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:37:45\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:29:43\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:43:00\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:38:17\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:36:41\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:36:17\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:34:35\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:07:18\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:04:32\n" +
        "     Achilat Chametz - 2 Hours before Chatzot        : 11:14:23\n" +
        "     Achilat Chametz - GR'A                          : 11:08:17\n" +
        "     Achilat Chametz - Chacham Yosef Harari-Raful    : 10:31:03\n" +
        "     Achilat Chametz - MG'A                          : 10:44:17\n" +
        "     Achilat Chametz - MG'A 120 Minutes              : 10:28:17\n" +
        "     Achilat Chametz - MG'A 16.1 deg                 : 10:37:45\n" +
        "     Achilat Chametz - MG'A 19.8 deg                 : 10:29:43\n" +
        "     Achilat Chametz - MG'A 72 Minutes Zmaniyot      : 10:44:17\n" +
        "     Achilat Chametz - MG'A 90 Minutes               : 10:38:17\n" +
        "     Achilat Chametz - MG'A 90 Minutes Zmaniyot      : 10:36:41\n" +
        "     Achilat Chametz - MG'A 96 Minutes               : 10:36:17\n" +
        "     Achilat Chametz - MG'A 96 Minutes Zmaniyot      : 10:34:35\n" +
        "     Achilat Chametz - Baal HaTanya                  : 11:07:18\n" +
        "        Biur Chametz - GR'A                          : 12:11:29\n" +
        "        Biur Chametz - MG'A 72 Minutes               : 11:59:29\n" +
        "        Biur Chametz - MG'A 16.1 deg                 : 11:56:19\n" +
        "        Biur Chametz - Baal HaTanya                  : 12:10:59\n" +
        "             Chatzot - Mid-Day                       : 13:14:23\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:46:17\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:54:09\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:44:23\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:46:17\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:52:17\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:33:11\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:46:32\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:46:32\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:50:28\n" +
        "       Mincha Ketana - GR'A                          : 16:55:52\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 17:49:50\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:37:52\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:11:44\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:57:37\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:49:34\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:53:32\n" +
        "       Plag HaMincha - GR'A                          : 18:14:52\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 19:49:52\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 19:54:56\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:28:01\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:37:51\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 19:47:23\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:22:22\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:02:22\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:11:52\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:14:55\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:26:07\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:29:55\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:30:52\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:34:55\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:42:48\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:45:20\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:37:36\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:05:18\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:17:14\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 18:19:50\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 18:13:42\n" +
        "               Shkia - Sunset                        : 19:32:22\n" +
        "               Shkia - Sunset (with elevation)       : 19:33:52\n" +
        "       Latest Mincha - Mishna Berura                 : 19:32:22\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:46:32\n" +
        "       Latest Mincha - Hacham Ben Sion               : 19:58:22\n" +
        "     Bein Hashmashot - 13 Degrees                    : 20:47:58\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:32:22\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 19:56:26\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:06:09\n" +
        "           Fast Ends - MG'A 120 Minutes              : 21:33:52\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 21:40:16\n" +
        "           Fast Ends - 3.5 deg                       : 19:48:19\n" +
        "           Fast Ends - Baal HaTanya                  : 20:03:22\n" +
        "           Fast Ends - 6.5 deg                       : 20:06:23\n" +
        "           Fast Ends - 8.5 deg                       : 20:18:34\n" +
        "           Fast Ends - 16.1 deg                      : 21:06:13\n" +
        "           Fast Ends - 18 deg                        : 21:18:38\n" +
        "           Fast Ends - 19.8 deg                      : 21:30:39\n" +
        "           Fast Ends - 26 deg                        : 22:14:47\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 20:33:52\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 20:45:52\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 20:49:42\n" +
        "           Fast Ends - MG'A 90 Minutes               : 21:03:52\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 21:08:40\n" +
        "           Fast Ends - 96 Minutes                    : 21:09:52\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 21:14:59\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 20:13:52\n" +
        "           Fast Ends - Geonim 3.65 deg               : 19:49:13\n" +
        "           Fast Ends - Geonim 4.37 deg               : 19:53:32\n" +
        "           Fast Ends - Geonim 4.61 deg               : 19:54:59\n" +
        "           Fast Ends - Geonim 4.8 deg                : 19:56:07\n" +
        "           Fast Ends - Geonim 5.88 deg               : 20:02:38\n" +
        "           Fast Ends - Geonim 5.95 deg               : 20:03:04\n" +
        "           Fast Ends - Geonim 7.083 deg              : 20:09:56\n" +
        "           Fast Ends - Geonim 8.5 deg                : 20:18:34\n" +
        "           Fast Ends - Rabbi Moshe Feinstein         : 20:22:22\n" +
        "           Fast Ends - Seattle Sephardic             : 20:02:22\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:33:52\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 21:40:16\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 19:48:19\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:03:22\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:06:23\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:18:34\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:06:13\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:18:38\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 21:30:39\n" +
        "    Tzait Hacochavim - 26 deg                        : 22:14:47\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:33:52\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:45:52\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 20:49:42\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:03:52\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:08:40\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:09:52\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:14:59\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:13:52\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 19:49:13\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 19:53:32\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 19:54:59\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 19:56:07\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 20:02:38\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 20:03:04\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:09:56\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:18:34\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:22:22\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:17:22\n" +
        "            Midnight - Mid-night                     :  1:14:14\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }

    @Test
  public void asara_bTevet_2015() {

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.JANUARY, 1 );
    verifyZmanim( ezraBessaroth, date, true, fastDayZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:55:17\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  6:29:28\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:11:38\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:16:34\n" +
        "      Alot Hashachar - 18 deg                        :  6:04:52\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:53:53\n" +
        "      Alot Hashachar - 26 deg                        :  5:16:36\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:55:17\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:43:17\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  7:03:48\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:25:17\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:50:56\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:19:17\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:46:38\n" +
        "       Talit/Tefilin - 7.65 deg                      :  7:10:22\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:58:17\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:53:46\n" +
        "       Talit/Tefilin - 11 deg                        :  6:48:38\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:45:26\n" +
        "              Hanetz - Sunrise                       :  7:57:05\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:55:17\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:12:25\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:49:58\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:00:16\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:35:24\n" +
        "      Sof Zman Shema - GR'A                          : 10:04:01\n" +
        "      Sof Zman Shema - MG'A                          :  9:28:01\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:04:01\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:14:39\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:03:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:38:16\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:19:01\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:31:50\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:16:01\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:29:41\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:55:09\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:02:15\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:06:58\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:12:49\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:17:11\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:26:11\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:02:11\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:12:25\n" +
        "     Sof Zman Tefila - GR'A                          : 10:46:55\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:25:56\n" +
        "     Sof Zman Tefila - MG'A                          : 10:22:55\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:06:55\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:14:01\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:06:28\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:29:46\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:16:55\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:25:28\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:14:55\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:24:02\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:45:45\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:44:28\n" +
        "             Chatzot - Mid-Day                       : 12:12:25\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:09:04\n" +
        "       Mincha Gedola - GR'A                          : 12:34:12\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 12:42:26\n" +
        "       Mincha Gedola - 30 Minutes                    : 12:42:25\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 12:42:25\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 12:40:12\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:32:16\n" +
        "       Mincha Gedola - Baal HaTanya                  : 12:34:30\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 12:42:25\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 12:42:25\n" +
        "       Mincha Ketana - GR'A                          : 14:42:55\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 15:40:31\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 15:24:55\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 15:03:52\n" +
        "       Mincha Ketana - Baal HaTanya                  : 14:45:00\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 14:37:25\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 14:41:23\n" +
        "       Plag HaMincha - GR'A                          : 15:36:34\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 17:11:34\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 16:44:30\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 16:54:44\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 17:03:59\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 17:12:41\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 17:42:13\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 16:24:04\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 16:33:34\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 16:17:19\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 16:47:49\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 16:27:31\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 16:52:34\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 16:30:55\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 16:07:02\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 16:11:31\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 16:03:10\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 15:26:16\n" +
        "       Plag HaMincha - Baal HaTanya                  : 15:39:22\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 15:43:19\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 15:35:47\n" +
        "               Shkia - Sunset                        : 16:28:24\n" +
        "               Shkia - Sunset (with elevation)       : 16:30:12\n" +
        "       Latest Mincha - Mishna Berura                 : 16:28:24\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 16:37:59\n" +
        "       Latest Mincha - Hacham Ben Sion               : 16:54:24\n" +
        "     Bein Hashmashot - 13 Degrees                    : 17:51:05\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 17:28:42\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 16:57:52\n" +
        "     Bein Hashmashot - 2 Stars                       : 17:03:55\n" +
        "           Fast Ends - MG'A 120 Minutes              : 18:30:12\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 17:56:01\n" +
        "           Fast Ends - 3.5 deg                       : 16:47:09\n" +
        "           Fast Ends - Baal HaTanya                  : 17:04:09\n" +
        "           Fast Ends - 6.5 deg                       : 17:07:30\n" +
        "           Fast Ends - 8.5 deg                       : 17:20:42\n" +
        "           Fast Ends - 16.1 deg                      : 18:08:56\n" +
        "           Fast Ends - 18 deg                        : 18:20:38\n" +
        "           Fast Ends - 19.8 deg                      : 18:31:37\n" +
        "           Fast Ends - 26 deg                        : 19:08:55\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 17:30:12\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 17:42:12\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 17:21:41\n" +
        "           Fast Ends - MG'A 90 Minutes               : 18:00:12\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 17:34:33\n" +
        "           Fast Ends - 96 Minutes                    : 18:06:12\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 17:38:51\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 17:10:12\n" +
        "           Fast Ends - Geonim 3.65 deg               : 16:48:11\n" +
        "           Fast Ends - Geonim 4.37 deg               : 16:53:08\n" +
        "           Fast Ends - Geonim 4.61 deg               : 16:54:46\n" +
        "           Fast Ends - Geonim 4.8 deg                : 16:56:03\n" +
        "           Fast Ends - Geonim 5.88 deg               : 17:03:21\n" +
        "           Fast Ends - Geonim 5.95 deg               : 17:03:49\n" +
        "           Fast Ends - Geonim 7.083 deg              : 17:11:22\n" +
        "           Fast Ends - Geonim 8.5 deg                : 17:20:42\n" +
        "           Fast Ends - Rabbi Moshe Feinstein         : 17:18:24\n" +
        "           Fast Ends - Seattle Sephardic             : 16:58:24\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 18:30:12\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 17:56:01\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 16:47:09\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 17:04:09\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 17:07:30\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 17:20:42\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 18:08:56\n" +
        "    Tzait Hacochavim - 18 deg                        : 18:20:38\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 18:31:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 19:08:55\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 17:30:12\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 17:42:12\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 17:21:41\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 18:00:12\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 17:34:33\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 18:06:12\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 17:38:51\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 17:10:12\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 16:48:11\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 16:53:08\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 16:54:46\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 16:56:03\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 17:03:21\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 17:03:49\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 17:11:22\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 17:20:42\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 17:18:24\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 17:13:24\n" +
        "            Midnight - Mid-night                     :  0:12:39\n" +
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

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.FEBRUARY, 13 );
    verifyZmanim( oakPark, date, true, erevShabbatZmanim,
      "Oak Park, Michigan\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:32:32\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:47:37\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:03:50\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:08:10\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:52\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:06\n" +
        "      Alot Hashachar - 26 deg                        :  5:14:27\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:32:32\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:20:32\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:29:35\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:02:32\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:13:51\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:56:32\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:08:36\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:54:24\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:44:12\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:40:21\n" +
        "       Talit/Tefilin - 11 deg                        :  6:35:58\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:33:14\n" +
        "              Hanetz - Sunrise                       :  7:32:32\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:32:32\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:47:01\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:06:39\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:15:24\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:32:42\n" +
        "      Sof Zman Shema - GR'A                          : 10:09:55\n" +
        "      Sof Zman Shema - MG'A                          :  9:33:55\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:09:55\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:45\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:17:44\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:38:27\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:24:55\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:30:34\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:21:55\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:27:57\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:11:12\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:07:48\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:15:20\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:20:29\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:17:40\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:26:40\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:02:40\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:47:01\n" +
        "     Sof Zman Tefila - GR'A                          : 11:02:23\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:33:45\n" +
        "     Sof Zman Tefila - MG'A                          : 10:38:23\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:22:23\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:17\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:27:36\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:41:24\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:32:23\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:36:09\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:30:23\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:34:24\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:00:58\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:52:42\n" +
        "             Chatzot - Mid-Day                       : 12:47:01\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:32:48\n" +
        "       Mincha Gedola - GR'A                          : 13:13:32\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:20:36\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:17:01\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:17:01\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:19:32\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:06:20\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:13:53\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:17:01\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:17:26\n" +
        "       Mincha Ketana - GR'A                          : 15:50:54\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:40:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:32:54\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:09:27\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:53:23\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:46:35\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 15:44:52\n" +
        "       Plag HaMincha - GR'A                          : 16:56:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:31:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:19:32\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:03:21\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:11:32\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:19:16\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:45:58\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:43:59\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:53:29\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:46:19\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:07:44\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 17:58:47\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:12:29\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:02:56\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:25:45\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:26:06\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:19:05\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:47:42\n" +
        "       Plag HaMincha - Baal HaTanya                  : 16:59:51\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:02:42\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 16:53:27\n" +
        "     Candle Lighting - 18 Minutes                    : 17:44:03\n" +
        "     Candle Lighting - 15 Minutes                    : 17:47:03\n" +
        "     Candle Lighting - 20 Minutes                    : 17:42:03\n" +
        "     Candle Lighting - 21 Minutes                    : 17:41:03\n" +
        "     Candle Lighting - 22 Minutes                    : 17:40:03\n" +
        "     Candle Lighting - 30 Minutes                    : 17:32:03\n" +
        "     Candle Lighting - 40 Minutes                    : 17:22:03\n" +
        "               Shkia - Sunset                        : 18:02:03\n" +
        "               Shkia - Sunset (with elevation)       : 18:02:03\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:03\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:13:51\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:03\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:10:56\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:00:33\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:23:36\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:31:04\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:02:03\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:46:58\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:17:07\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:31:05\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:33:52\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:44:56\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:26:31\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:36:50\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:46:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:20:20\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:02:03\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:14:03\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:05:00\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:32:03\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:20:45\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:38:03\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:25:59\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:42:03\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:17:58\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:22:00\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:23:20\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:24:24\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:30:25\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:30:49\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:37:06\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:44:56\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:03\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:03\n" +
        "            Midnight - Mid-night                     :  0:47:00\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:32:48\n"
    );
  }


  @Test
  public void oakParkMichigan_with_elevation() {

    // See YouTrack issue #90
    ZmanimLocation oakParkWithElevation = new ZmanimLocation.Builder()
      .atLatitude( 42.5 )
      .atLongitude( -83.2 )
      .locationName( "Oak Park, Michigan" )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .atElevation( 189 )
      .build();

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.FEBRUARY, 13 );
    verifyZmanim( oakParkWithElevation, date, true, erevShabbatZmanim,
      "Oak Park, Michigan\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:30:02\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:44:17\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:03:50\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:08:10\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:52\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:06\n" +
        "      Alot Hashachar - 26 deg                        :  5:14:27\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:30:02\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:18:02\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:26:35\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:00:02\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:10:43\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:54:02\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:05:26\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:54:24\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:44:12\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:40:21\n" +
        "       Talit/Tefilin - 11 deg                        :  6:35:58\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:33:14\n" +
        "              Hanetz - Sunrise                       :  7:32:32\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:30:02\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:47:01\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:07:16\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:15:24\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:31:05\n" +
        "      Sof Zman Shema - GR'A                          : 10:08:40\n" +
        "      Sof Zman Shema - MG'A                          :  9:32:40\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:08:40\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:45\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:17:44\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:36:56\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:23:40\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:29:00\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:20:40\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:26:22\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:11:12\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:07:48\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:15:20\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:20:29\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:16:25\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:25:25\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:01:25\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:47:01\n" +
        "     Sof Zman Tefila - GR'A                          : 11:01:33\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:32:35\n" +
        "     Sof Zman Tefila - MG'A                          : 10:37:33\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:21:33\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:17\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:27:36\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:40:24\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:31:33\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:35:06\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:29:33\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:33:21\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:00:58\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:51:52\n" +
        "             Chatzot - Mid-Day                       : 12:47:01\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:32:48\n" +
        "       Mincha Gedola - GR'A                          : 13:13:44\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:20:36\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:17:01\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:17:01\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:19:44\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:06:19\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:13:53\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:17:01\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:17:26\n" +
        "       Mincha Ketana - GR'A                          : 15:52:22\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:40:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:34:22\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:10:49\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:53:23\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 15:46:35\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 15:46:20\n" +
        "       Plag HaMincha - GR'A                          : 16:58:28\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:33:28\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:22:11\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:03:21\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:11:32\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:19:16\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:45:58\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:45:58\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:55:28\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:48:42\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:09:43\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:01:16\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:14:28\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:05:27\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:27:41\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:26:06\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:19:05\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:49:56\n" +
        "       Plag HaMincha - Baal HaTanya                  : 16:59:51\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:02:42\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 16:55:27\n" +
        "     Candle Lighting - 18 Minutes                    : 17:44:03\n" +
        "     Candle Lighting - 15 Minutes                    : 17:47:03\n" +
        "     Candle Lighting - 20 Minutes                    : 17:42:03\n" +
        "     Candle Lighting - 21 Minutes                    : 17:41:03\n" +
        "     Candle Lighting - 22 Minutes                    : 17:40:03\n" +
        "     Candle Lighting - 30 Minutes                    : 17:32:03\n" +
        "     Candle Lighting - 40 Minutes                    : 17:22:03\n" +
        "               Shkia - Sunset                        : 18:02:03\n" +
        "               Shkia - Sunset (with elevation)       : 18:04:34\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:03\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:13:51\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:03\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:10:56\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:03:04\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:23:36\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:32:53\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:04:34\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:50:19\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:17:07\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:31:05\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:33:52\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:44:56\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:26:31\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:36:50\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:46:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:20:20\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:04:34\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:16:34\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:08:01\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:34:34\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:23:53\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:40:34\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:29:10\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:44:34\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:17:58\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:22:00\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:23:20\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:24:24\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:30:25\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:30:49\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 18:37:06\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 18:44:56\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 18:52:03\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 18:47:03\n" +
        "            Midnight - Mid-night                     :  0:47:00\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:32:48\n"
    );
  }


  @Test
  public void november_26_2017() {

    final String haRambanSynagogueExpected = "HaRamban Synagogue\n" +
      "      Alot Hashachar - 120 Minutes                   :  4:12:20\n" +
      "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:27:41\n" +
      "      Alot Hashachar - Baal HaTanya                  :  4:56:40\n" +
      "      Alot Hashachar - 16.1 deg                      :  5:00:33\n" +
      "      Alot Hashachar - 18 deg                        :  4:51:19\n" +
      "      Alot Hashachar - 19.8 deg                      :  4:42:36\n" +
      "      Alot Hashachar - 26 deg                        :  4:12:50\n" +
      "      Alot Hashachar - 60 Minutes                    :  5:12:20\n" +
      "      Alot Hashachar - 72 Minutes                    :  5:00:20\n" +
      "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:09:32\n" +
      "      Alot Hashachar - 90 Minutes                    :  4:42:20\n" +
      "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:53:51\n" +
      "      Alot Hashachar - 96 Minutes                    :  4:36:20\n" +
      "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:48:37\n" +
      "       Talit/Tefilin - 7.65 deg                      :  5:42:16\n" +
      "       Talit/Tefilin - 9.5 deg                       :  5:33:02\n" +
      "       Talit/Tefilin - 10.2 deg                      :  5:29:33\n" +
      "       Talit/Tefilin - 11 deg                        :  5:25:36\n" +
      "       Talit/Tefilin - 11.5 deg                      :  5:23:07\n" +
      "              Hanetz - Sunrise                       :  6:16:55\n" +
      "              Hanetz - Sunrise (with elevation)      :  6:12:20\n" +
      "      Sof Zman Shema - 3 Hours before Chatzot        :  8:26:14\n" +
      "      Sof Zman Shema - 16.1 deg to Sunset            :  7:55:28\n" +
      "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:02:17\n" +
      "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:12:13\n" +
      "      Sof Zman Shema - GR'A                          :  8:49:18\n" +
      "      Sof Zman Shema - MG'A                          :  8:13:18\n" +
      "      Sof Zman Shema - MG'A 120 Minutes              :  7:49:18\n" +
      "      Sof Zman Shema - MG'A 16.1 deg                 :  8:13:25\n" +
      "      Sof Zman Shema - MG'A 19.8 deg                 :  8:04:26\n" +
      "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  8:17:54\n" +
      "      Sof Zman Shema - MG'A 90 Minutes               :  8:04:18\n" +
      "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:10:04\n" +
      "      Sof Zman Shema - 96 Minutes                    :  8:01:18\n" +
      "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:07:27\n" +
      "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  7:58:27\n" +
      "      Sof Zman Shema - Baal HaTanya                  :  8:49:40\n" +
      "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  8:15:11\n" +
      "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  8:19:49\n" +
      "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  8:10:42\n" +
      "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  8:19:42\n" +
      "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  8:55:42\n" +
      "     Sof Zman Tefila - 2 Hours before Chatzot        :  9:26:14\n" +
      "     Sof Zman Tefila - GR'A                          :  9:41:38\n" +
      "     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  9:13:06\n" +
      "     Sof Zman Tefila - MG'A                          :  9:17:38\n" +
      "     Sof Zman Tefila - MG'A 120 Minutes              :  9:01:38\n" +
      "     Sof Zman Tefila - MG'A 16.1 deg                 :  9:17:42\n" +
      "     Sof Zman Tefila - MG'A 19.8 deg                 :  9:11:43\n" +
      "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      :  9:20:42\n" +
      "     Sof Zman Tefila - MG'A 90 Minutes               :  9:11:38\n" +
      "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      :  9:15:28\n" +
      "     Sof Zman Tefila - MG'A 96 Minutes               :  9:09:38\n" +
      "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      :  9:13:43\n" +
      "     Sof Zman Tefila - Baal HaTanya                  :  9:41:52\n" +
      "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day:  9:50:09\n" +
      "             Chatzot - Mid-Day                       : 11:26:14\n" +
      "             Chatzot - Fixed Local Mid-Day           : 11:39:04\n" +
      "       Mincha Gedola - GR'A                          : 11:52:26\n" +
      "       Mincha Gedola - MG'A 16.1 deg                 : 11:58:25\n" +
      "       Mincha Gedola - 30 Minutes                    : 11:56:14\n" +
      "       Mincha Gedola - Lechumra 30 Minutes           : 11:56:14\n" +
      "       Mincha Gedola - MG'A 72 Minutes               : 11:58:26\n" +
      "       Mincha Gedola - Chacham Yosef Harari-Raful    : 11:45:20\n" +
      "       Mincha Gedola - Baal HaTanya                  : 11:52:23\n" +
      "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 11:56:14\n" +
      "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 11:56:14\n" +
      "       Mincha Ketana - GR'A                          : 14:29:25\n" +
      "       Mincha Ketana - MG'A 16.1 deg                 : 15:11:17\n" +
      "       Mincha Ketana - MG'A 72 Minutes               : 15:11:25\n" +
      "       Mincha Ketana - Chacham Yosef Harari-Raful    : 14:48:00\n" +
      "       Mincha Ketana - Baal HaTanya                  : 14:28:59\n" +
      "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 14:22:52\n" +
      "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 14:34:44\n" +
      "       Plag HaMincha - GR'A                          : 15:34:49\n" +
      "       Plag HaMincha - MG'A 120 Minutes              : 17:09:49\n" +
      "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 16:57:40\n" +
      "       Plag HaMincha - MG'A 16.1 deg                 : 16:31:38\n" +
      "       Plag HaMincha - MG'A 18 deg                   : 16:38:57\n" +
      "       Plag HaMincha - MG'A 19.8 deg                 : 16:45:51\n" +
      "       Plag HaMincha - MG'A 26 deg                   : 17:09:24\n" +
      "       Plag HaMincha - MG'A 60 Minutes               : 16:22:19\n" +
      "       Plag HaMincha - MG'A 72 Minutes               : 16:31:49\n" +
      "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 16:24:31\n" +
      "       Plag HaMincha - MG'A 90 Minutes               : 16:46:04\n" +
      "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 16:36:57\n" +
      "       Plag HaMincha - MG'A 96 Minutes               : 16:50:49\n" +
      "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 16:41:06\n" +
      "       Plag HaMincha - Chacham Yosef Harari-Raful    : 16:04:07\n" +
      "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 15:58:05\n" +
      "       Plag HaMincha - 16.1 deg - 7.083 deg          : 15:51:43\n" +
      "       Plag HaMincha - 16.1 deg - Sunset             : 15:27:20\n" +
      "       Plag HaMincha - Baal HaTanya                  : 15:34:15\n" +
      "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 15:36:51\n" +
      "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 15:37:29\n" +
      "               Shkia - Sunset                        : 16:35:38\n" +
      "               Shkia - Sunset (with elevation)       : 16:40:13\n" +
      "       Latest Mincha - Mishna Berura                 : 16:35:38\n" +
      "       Latest Mincha - Hacham Ovadia Yosef           : 16:47:14\n" +
      "       Latest Mincha - Hacham Ben Sion               : 17:01:38\n" +
      "     Bein Hashmashot - 13 Degrees                    : 17:38:00\n" +
      "     Bein Hashmashot - 58.5 Minutes                  : 17:38:43\n" +
      "     Bein Hashmashot - 13.5 Minutes                  : 16:53:56\n" +
      "     Bein Hashmashot - 2 Stars                       : 17:05:09\n" +
      "    Tzait Hacochavim - MG'A 120 Minutes              : 18:40:13\n" +
      "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 18:24:52\n" +
      "    Tzait Hacochavim - 3.5 deg                       : 16:49:19\n" +
      "    Tzait Hacochavim - Baal HaTanya                  : 17:01:59\n" +
      "    Tzait Hacochavim - 6.5 deg                       : 17:04:31\n" +
      "    Tzait Hacochavim - 8.5 deg                       : 17:14:32\n" +
      "    Tzait Hacochavim - 16.1 deg                      : 17:52:00\n" +
      "    Tzait Hacochavim - 18 deg                        : 18:01:14\n" +
      "    Tzait Hacochavim - 19.8 deg                      : 18:09:57\n" +
      "    Tzait Hacochavim - 26 deg                        : 18:39:42\n" +
      "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 17:40:13\n" +
      "    Tzait Hacochavim - Rambam / Rabainu Tam          : 17:52:13\n" +
      "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 17:43:01\n" +
      "    Tzait Hacochavim - MG'A 90 Minutes               : 18:10:13\n" +
      "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 17:58:43\n" +
      "    Tzait Hacochavim - 96 Minutes                    : 18:16:13\n" +
      "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 18:03:56\n" +
      "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 17:20:13\n" +
      "    Tzait Hacochavim - Geonim 3.65 deg               : 16:50:05\n" +
      "    Tzait Hacochavim - Geonim 4.37 deg               : 16:53:45\n" +
      "    Tzait Hacochavim - Geonim 4.61 deg               : 16:54:58\n" +
      "    Tzait Hacochavim - Geonim 4.8 deg                : 16:55:56\n" +
      "    Tzait Hacochavim - Geonim 5.88 deg               : 17:01:23\n" +
      "    Tzait Hacochavim - Geonim 5.95 deg               : 17:01:44\n" +
      "    Tzait Hacochavim - Geonim 7.083 deg              : 17:07:26\n" +
      "    Tzait Hacochavim - Geonim 8.5 deg                : 17:14:32\n" +
      "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 17:25:38\n" +
      "    Tzait Hacochavim - Seattle Sephardic             : 17:20:38\n" +
      "            Midnight - Mid-night                     : 23:26:24\n" +
      "            Midnight - Fixed Local Mid-Night         : 23:39:04\n";

    HebrewDate date = HebrewCalendar.getForDate( 2017, Calendar.NOVEMBER, 26 );
    verifyZmanim( haRambanSynagogue, date, true, dailyZmanim, haRambanSynagogueExpected );
  }


} // End of UT_Zmanim class
