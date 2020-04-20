/*
 * Copyright (c) 2020. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.ZmanimBaseTest;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Spot-checks some dates to verify the expected zmanim are present and properly calculated.
 */
@SuppressWarnings( { "MagicNumber", "SpellCheckingInspection" } )
public class UT_Zmanim
  extends ZmanimBaseTest {


  /**
   * Just a regular day....
   */
  @Test
  public void october_19_2014() {

    final String ezraBessarothExpected =
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:33:46\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:47:09\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:57:40\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:02:25\n" +
        "      Alot Hashachar - 18 deg                        :  5:51:07\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:40:23\n" +
        "      Alot Hashachar - 26 deg                        :  5:02:57\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:32:14\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:21:46\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:29:48\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:03:46\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:13:48\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:57:46\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:08:28\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:52:38\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:41:36\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:37:26\n" +
        "       Talit/Tefilin - 11 deg                        :  6:32:41\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:29:43\n" +
        "              Hanetz - Sunrise                       :  7:33:46\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:32:14\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:53:36\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:05:10\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:14:35\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:35:42\n" +
        "      Sof Zman Shema - GR'A                          : 10:13:41\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:21:25\n" +
        "      Sof Zman Shema - MG'A                          :  9:37:41\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:13:41\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:27:58\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:16:56\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:41:42\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:28:41\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:33:42\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:25:41\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:31:02\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:10:04\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:11:23\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:53:36\n" +
        "     Sof Zman Tefila - GR'A                          : 11:06:59\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:37:40\n" +
        "     Sof Zman Tefila - MG'A                          : 10:42:59\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:26:59\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:36:29\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:29:07\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:45:40\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:36:59\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:40:20\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:34:59\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:38:33\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:05:27\n" +
        "             Chatzot - Mid-Day                       : 12:53:36\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:20:15\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:27:46\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:23:36\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:23:36\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:26:15\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:12:36\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:20:37\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:23:36\n" +
        "       Mincha Ketana - GR'A                          : 16:00:10\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:53:19\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:42:10\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:18:30\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:02:50\n" +
        "       Plag HaMincha - GR'A                          : 17:06:48\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:41:48\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:31:12\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:18:58\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:27:53\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:36:22\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 19:05:53\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:54:08\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 18:03:48\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:57:26\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:18:03\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:10:06\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:22:48\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:14:19\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:35:58\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:38:37\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:31:03\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:57:17\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:10:25\n" +
        "               Shkia - Sunset                        : 18:13:25\n" +
        "               Shkia - Sunset (with elevation)       : 18:14:57\n" +
        "       Latest Mincha - Mishna Berura                 : 18:13:25\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:25:25\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:39:25\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:27:41\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:11:55\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:37:37\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:44:55\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:13:25\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:00:02\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:29:37\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:44:39\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:47:38\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:59:34\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:44:37\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:55:53\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 20:06:35\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:43:54\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:13:25\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:25:25\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:17:23\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:43:25\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:33:23\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:49:25\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:38:43\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:53:25\n" +
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
        "            Midnight - Mid-night                     :  0:54:20\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n";

    final String yeshivaUniversityExpected =
      "Yeshiva University\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:11:22\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:21:43\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:45:43\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:49:57\n" +
        "      Alot Hashachar - 18 deg                        :  5:39:54\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:30:21\n" +
        "      Alot Hashachar - 26 deg                        :  4:57:18\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:10:04\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:59:22\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:05:34\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:41:22\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:49:08\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:35:22\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:43:39\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:34:46\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:24:55\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:21:12\n" +
        "       Talit/Tefilin - 11 deg                        :  6:16:58\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:14:18\n" +
        "              Hanetz - Sunrise                       :  7:11:22\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:10:04\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:40:20\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:54:47\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:03:11\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:16:30\n" +
        "      Sof Zman Shema - GR'A                          :  9:55:51\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:03:32\n" +
        "      Sof Zman Shema - MG'A                          :  9:19:51\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:55:51\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:15:07\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:05:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:22:57\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:10:51\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:14:44\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:07:51\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:11:59\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:59:09\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:53:49\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:40:20\n" +
        "     Sof Zman Tefila - GR'A                          : 10:50:41\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:20:09\n" +
        "     Sof Zman Tefila - MG'A                          : 10:26:41\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:10:41\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:23:30\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:16:58\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:28:45\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:20:41\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:23:16\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:18:41\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:21:26\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:49:20\n" +
        "             Chatzot - Mid-Day                       : 12:40:20\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:55:42\n" +
        "       Mincha Gedola - GR'A                          : 13:07:45\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:14:29\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:10:20\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:10:20\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:13:45\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:59:16\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:08:05\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:10:20\n" +
        "       Mincha Ketana - GR'A                          : 15:52:14\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:39:39\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:34:14\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:10:12\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:54:36\n" +
        "       Plag HaMincha - GR'A                          : 17:00:46\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:35:46\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:27:35\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:05:08\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:13:05\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:20:37\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:46:42\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:48:08\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:57:46\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:52:51\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:12:01\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:05:53\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:16:46\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:10:13\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:29:45\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:29:06\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:22:20\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:52:17\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:03:59\n" +
        "               Shkia - Sunset                        : 18:09:18\n" +
        "               Shkia - Sunset (with elevation)       : 18:10:36\n" +
        "       Latest Mincha - Mishna Berura                 : 18:09:18\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:21:38\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:35:18\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:15:30\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:07:48\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:29:21\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:37:22\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:09:18\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:58:57\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:23:41\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:37:04\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:39:45\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:50:23\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:30:37\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:40:39\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:50:11\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:23:09\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:09:18\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:21:18\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:15:06\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:39:18\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:31:33\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:45:18\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:37:02\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:49:18\n" +
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
        "            Midnight - Mid-night                     :  0:40:53\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:55:42\n";

    final String haRambanSynagogueExpected =
      "HaRamban Synagogue\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:44:59\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:52:02\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:28:48\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:32:34\n" +
        "      Alot Hashachar - 18 deg                        :  5:23:37\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:15:09\n" +
        "      Alot Hashachar - 26 deg                        :  4:45:56\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:40:43\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:32:59\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:37:13\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:14:59\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:20:16\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:08:59\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:14:37\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:12:29\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:03:43\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:00:24\n" +
        "       Talit/Tefilin - 11 deg                        :  5:56:37\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:54:16\n" +
        "              Hanetz - Sunrise                       :  6:44:59\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:40:43\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:23:50\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:40:06\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:47:33\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:53:35\n" +
        "      Sof Zman Shema - GR'A                          :  9:34:24\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              :  9:42:01\n" +
        "      Sof Zman Shema - MG'A                          :  8:58:24\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:34:24\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  8:58:11\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:49:28\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:00:31\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  8:49:24\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:52:03\n" +
        "      Sof Zman Shema - 96 Minutes                    :  8:46:24\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:49:13\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:43:58\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:32:36\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:23:50\n" +
        "     Sof Zman Tefila - GR'A                          : 10:30:53\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  9:59:02\n" +
        "     Sof Zman Tefila - MG'A                          : 10:06:53\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              :  9:50:53\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:06:43\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:00:55\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:08:17\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:00:53\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:02:39\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               :  9:58:53\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:00:46\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:29:41\n" +
        "             Chatzot - Mid-Day                       : 12:23:50\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:39:04\n" +
        "       Mincha Gedola - GR'A                          : 12:52:04\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 12:58:04\n" +
        "       Mincha Gedola - 30 Minutes                    : 12:53:50\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 12:53:50\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 12:58:04\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:42:40\n" +
        "       Mincha Gedola - Baal HaTanya                  : 12:52:22\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 12:53:50\n" +
        "       Mincha Ketana - GR'A                          : 15:41:29\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:23:41\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:23:29\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 15:59:02\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:43:35\n" +
        "       Plag HaMincha - GR'A                          : 16:52:05\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:27:05\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:21:30\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 17:49:22\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 17:56:26\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:03:08\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:26:14\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:39:08\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:49:05\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:45:44\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:03:20\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 17:59:09\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:08:05\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:03:37\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:20:52\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:17:15\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:11:14\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:44:33\n" +
        "       Plag HaMincha - Baal HaTanya                  : 16:54:56\n" +
        "               Shkia - Sunset                        : 18:02:41\n" +
        "               Shkia - Sunset (with elevation)       : 18:06:57\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:41\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:15:23\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:41\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:01:34\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:01:11\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:18:58\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:27:38\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:02:41\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:55:38\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:15:26\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:27:20\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:29:42\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:39:11\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:15:02\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:23:58\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:32:26\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:01:37\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:02:41\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:14:41\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:10:27\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:32:41\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:27:23\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:38:41\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:33:02\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:42:41\n" +
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
        "            Midnight - Mid-night                     :  0:24:12\n" +
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
        "      Alot Hashachar - 120 Minutes                   :  5:41:08\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:57:14\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:04:31\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:09:15\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:59\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:47:17\n" +
        "      Alot Hashachar - 26 deg                        :  5:10:09\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:39:35\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:29:08\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:38:48\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:11:08\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:23:12\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:05:08\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:18:01\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:59:36\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:48:31\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:44:20\n" +
        "       Talit/Tefilin - 11 deg                        :  6:39:34\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:36:35\n" +
        "              Hanetz - Sunrise                       :  7:41:08\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:39:35\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:52:51\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:08:05\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:17:36\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:40:14\n" +
        "      Sof Zman Shema - GR'A                          : 10:16:59\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:25:06\n" +
        "      Sof Zman Shema - MG'A                          :  9:40:59\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:16:59\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:31:01\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:20:01\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:45:49\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:31:59\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:38:02\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:28:59\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:35:26\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:13:02\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:14:40\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:52:51\n" +
        "     Sof Zman Tefila - GR'A                          : 11:08:56\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:40:43\n" +
        "     Sof Zman Tefila - MG'A                          : 10:44:56\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:28:56\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:38:16\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:30:56\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:48:10\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:38:56\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:42:58\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:36:56\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:41:14\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:07:24\n" +
        "             Chatzot - Mid-Day                       : 12:52:51\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:18:49\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:26:24\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:22:51\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:22:51\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:24:49\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:11:55\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:19:12\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:22:51\n" +
        "       Mincha Ketana - GR'A                          : 15:54:40\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:48:10\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:36:40\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:13:21\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:57:22\n" +
        "       Plag HaMincha - GR'A                          : 16:59:37\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:34:37\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:21:52\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:12:14\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:21:08\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:29:35\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:58:53\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:46:57\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:56:37\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:48:58\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:10:52\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:01:18\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:15:37\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:05:25\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:28:57\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:31:46\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:24:09\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:50:02\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:03:17\n" +
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
        "     Bein Hashmashot - 58.5 Minutes                  : 19:03:03\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:29:08\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:36:11\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:04:33\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:48:27\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:20:56\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:36:06\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:39:07\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:51:08\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:36:18\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:47:33\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:58:13\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:35:14\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:04:33\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:16:33\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:06:54\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:34:33\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:22:29\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:40:33\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:27:40\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:44:33\n" +
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
        "            Midnight - Mid-night                     :  0:53:35\n" +
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
        "      Alot Hashachar - 120 Minutes                   :  5:42:37\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:59:15\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:05:52\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:10:37\n" +
        "      Alot Hashachar - 18 deg                        :  5:59:21\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:39\n" +
        "      Alot Hashachar - 26 deg                        :  5:11:34\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:41:04\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:30:37\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:40:36\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:12:37\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:25:06\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:06:37\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:19:56\n" +
        "       Talit/Tefilin - 7.65 deg                      :  7:01:00\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:49:54\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:45:43\n" +
        "       Talit/Tefilin - 11 deg                        :  6:40:56\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:37:57\n" +
        "              Hanetz - Sunrise                       :  7:42:37\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:41:04\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:52:44\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:08:40\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:18:13\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:41:10\n" +
        "      Sof Zman Shema - GR'A                          : 10:17:41\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:25:50\n" +
        "      Sof Zman Shema - MG'A                          :  9:41:41\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:17:41\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:31:38\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:20:39\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:46:40\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:32:41\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:38:55\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:29:41\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:36:20\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:13:38\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:15:21\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:52:44\n" +
        "     Sof Zman Tefila - GR'A                          : 11:09:22\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:41:21\n" +
        "     Sof Zman Tefila - MG'A                          : 10:45:22\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:29:22\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:38:39\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:31:19\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:48:41\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:39:22\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:43:31\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:37:22\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:41:48\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:07:49\n" +
        "             Chatzot - Mid-Day                       : 12:52:44\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:18:34\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:26:10\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:22:44\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:22:44\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:24:34\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:11:49\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:18:57\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:22:44\n" +
        "       Mincha Ketana - GR'A                          : 15:53:37\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:47:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:35:37\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:12:22\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:56:20\n" +
        "       Plag HaMincha - GR'A                          : 16:58:14\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:33:14\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:20:04\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:10:57\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:19:51\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:28:18\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:57:34\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:45:34\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 17:55:14\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 17:47:20\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:09:29\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 17:59:36\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:14:14\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:03:42\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:27:36\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:30:27\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:22:50\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 16:48:39\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:01:54\n" +
        "               Shkia - Sunset                        : 18:02:50\n" +
        "               Shkia - Sunset (with elevation)       : 18:04:23\n" +
        "       Latest Mincha - Mishna Berura                 : 18:02:50\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:14:28\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:28:50\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:17:46\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:01:20\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:27:30\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:34:29\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:02:50\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 19:46:12\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:19:16\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:34:28\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:37:29\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 18:49:30\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:34:43\n" +
        "    Tzait Hacochavim - 18 deg                        : 19:45:58\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 19:56:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:33:36\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:02:50\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:14:50\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:04:51\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:32:50\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:20:22\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:38:50\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:25:32\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 18:42:50\n" +
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
        "            Havdalah - MG'A 120 Minutes              : 20:02:50\n" +
        "            Havdalah - GR'A 120 Minutes Zmaniyot     : 19:46:12\n" +
        "            Havdalah - 3.5 deg                       : 18:19:16\n" +
        "            Havdalah - 6.5 deg                       : 18:37:29\n" +
        "            Havdalah - 8.5 deg                       : 18:49:30\n" +
        "            Havdalah - 16.1 deg                      : 19:34:43\n" +
        "            Havdalah - 18 deg                        : 19:45:58\n" +
        "            Havdalah - 19.8 deg                      : 19:56:37\n" +
        "            Havdalah - 26 deg                        : 20:33:36\n" +
        "            Havdalah - Chavas Yair and Divray Malkiel: 19:02:50\n" +
        "            Havdalah - Rambam / Rabainu Tam          : 19:14:50\n" +
        "            Havdalah - 72 Minutes Zmaniyot           : 19:04:51\n" +
        "            Havdalah - MG'A 90 Minutes               : 19:32:50\n" +
        "            Havdalah - GR'A 90 Minutes Zmaniyot      : 19:20:22\n" +
        "            Havdalah - 96 Minutes                    : 19:38:50\n" +
        "            Havdalah - GR'A 96 Minutes Zmaniyot      : 19:25:32\n" +
        "            Havdalah - Chacham Yosef Harari-Raful    : 18:42:50\n" +
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
        "            Midnight - Mid-night                     :  0:53:28\n" +
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
        "      Alot Hashachar - 120 Minutes                   :  4:42:01\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:31:52\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:00:58\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:06:21\n" +
        "      Alot Hashachar - 18 deg                        :  4:53:28\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:40:54\n" +
        "      Alot Hashachar - 26 deg                        :  3:53:54\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:40:31\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:30:01\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:23:56\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:12:01\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:04:24\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:06:01\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:57:54\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:00:32\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:49:01\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:44:36\n" +
        "       Talit/Tefilin - 11 deg                        :  5:39:33\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:36:22\n" +
        "              Hanetz - Sunrise                       :  6:42:01\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:40:31\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:12:29\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:45:30\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:55:02\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:08:41\n" +
        "      Sof Zman Shema - GR'A                          :  9:57:15\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              :  9:55:32\n" +
        "      Sof Zman Shema - MG'A                          :  9:21:15\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:57:15\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:09:32\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:56:52\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:18:12\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:12:15\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:08:27\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:09:15\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:05:11\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:50:26\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:55:00\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:12:29\n" +
        "     Sof Zman Tefila - GR'A                          : 11:02:20\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:23:36\n" +
        "     Sof Zman Tefila - MG'A                          : 10:38:20\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:22:20\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:30:35\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:22:11\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:36:18\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:32:20\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:29:47\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:30:20\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:27:37\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:00:50\n" +
        "             Chatzot - Mid-Day                       : 13:12:29\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:45:01\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:53:15\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:42:29\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:45:01\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:51:01\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:30:54\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:45:24\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:45:24\n" +
        "       Mincha Ketana - GR'A                          : 17:00:15\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 17:56:25\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:42:15\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:15:39\n" +
        "       Mincha Ketana - Baal HaTanya                  : 17:02:53\n" +
        "       Plag HaMincha - GR'A                          : 18:21:36\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 19:56:36\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:04:38\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:37:45\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:48:02\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 19:58:04\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:35:47\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:08:56\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:18:36\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:23:25\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:32:51\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:38:52\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:37:36\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:44:02\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:49:18\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:53:41\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:45:47\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:11:38\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:25:10\n" +
        "               Shkia - Sunset                        : 19:42:56\n" +
        "               Shkia - Sunset (with elevation)       : 19:44:27\n" +
        "       Latest Mincha - Mishna Berura                 : 19:42:56\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:57:35\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:08:56\n" +
        "     Bein Hashmashot - 13 Degrees                    : 21:00:07\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:41:26\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:07:34\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:16:35\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:42:56\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 21:53:06\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 19:59:04\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:14:22\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:17:27\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:29:53\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:19:05\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:32:04\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 21:44:43\n" +
        "    Tzait Hacochavim - 26 deg                        : 22:32:17\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:42:56\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:54:56\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:01:02\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:12:56\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:20:33\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:18:56\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:27:04\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:22:56\n" +
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
        "     Candle Lighting - MG'A 120 Minutes              : 21:42:56\n" +
        "     Candle Lighting - GR'A 120 Minutes Zmaniyot     : 21:53:06\n" +
        "     Candle Lighting - 3.5 deg                       : 19:59:04\n" +
        "     Candle Lighting - 6.5 deg                       : 20:17:27\n" +
        "     Candle Lighting - 8.5 deg                       : 20:29:53\n" +
        "     Candle Lighting - 16.1 deg                      : 21:19:05\n" +
        "     Candle Lighting - 18 deg                        : 21:32:04\n" +
        "     Candle Lighting - 19.8 deg                      : 21:44:43\n" +
        "     Candle Lighting - 26 deg                        : 22:32:17\n" +
        "     Candle Lighting - Chavas Yair and Divray Malkiel: 20:42:56\n" +
        "     Candle Lighting - Rambam / Rabainu Tam          : 20:54:56\n" +
        "     Candle Lighting - 72 Minutes Zmaniyot           : 21:01:02\n" +
        "     Candle Lighting - MG'A 90 Minutes               : 21:12:56\n" +
        "     Candle Lighting - GR'A 90 Minutes Zmaniyot      : 21:20:33\n" +
        "     Candle Lighting - 96 Minutes                    : 21:18:56\n" +
        "     Candle Lighting - GR'A 96 Minutes Zmaniyot      : 21:27:04\n" +
        "     Candle Lighting - Chacham Yosef Harari-Raful    : 20:22:56\n" +
        "     Candle Lighting - Geonim 3.65 deg               : 19:59:59\n" +
        "     Candle Lighting - Geonim 4.37 deg               : 20:04:22\n" +
        "     Candle Lighting - Geonim 4.61 deg               : 20:05:50\n" +
        "     Candle Lighting - Geonim 4.8 deg                : 20:07:00\n" +
        "     Candle Lighting - Geonim 5.88 deg               : 20:13:38\n" +
        "     Candle Lighting - Geonim 5.95 deg               : 20:14:04\n" +
        "     Candle Lighting - Geonim 7.083 deg              : 20:21:04\n" +
        "     Candle Lighting - Geonim 8.5 deg                : 20:29:53\n" +
        "     Candle Lighting - Rabbi Moshe Feinstein         : 20:32:56\n" +
        "            Midnight - Mid-night                     :  1:11:29\n" +
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
        "      Alot Hashachar - 120 Minutes                   :  4:22:03\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:06:10\n" +
        "      Alot Hashachar - Baal HaTanya                  :  4:36:20\n" +
        "      Alot Hashachar - 16.1 deg                      :  4:42:08\n" +
        "      Alot Hashachar - 18 deg                        :  4:28:12\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:14:27\n" +
        "      Alot Hashachar - 26 deg                        :  3:20:34\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:20:31\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:10:03\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:00:31\n" +
        "      Alot Hashachar - 90 Minutes                    :  4:52:03\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:40:08\n" +
        "      Alot Hashachar - 96 Minutes                    :  4:46:03\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:33:20\n" +
        "       Talit/Tefilin - 7.65 deg                      :  5:39:19\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:27:18\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:22:42\n" +
        "       Talit/Tefilin - 11 deg                        :  5:17:23\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:14:03\n" +
        "              Hanetz - Sunrise                       :  6:22:03\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:20:31\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:09:44\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:30:57\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:40:46\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:54:45\n" +
        "      Sof Zman Shema - GR'A                          :  9:45:54\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              :  9:45:33\n" +
        "      Sof Zman Shema - MG'A                          :  9:09:54\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:45:54\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  8:56:05\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:42:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:05:08\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:00:54\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:54:56\n" +
        "      Sof Zman Shema - 96 Minutes                    :  8:57:54\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:51:32\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:36:03\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:43:36\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:09:44\n" +
        "     Sof Zman Tefila - GR'A                          : 10:53:50\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:12:49\n" +
        "     Sof Zman Tefila - MG'A                          : 10:29:50\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:13:50\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:20:44\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:11:36\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:26:40\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:23:50\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:19:52\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:21:50\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:17:36\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:52:19\n" +
        "     Achilat Chametz - 2 Hours before Chatzot        : 11:09:44\n" +
        "     Achilat Chametz - GR'A                          : 10:53:50\n" +
        "     Achilat Chametz - Chacham Yosef Harari-Raful    : 10:12:49\n" +
        "     Achilat Chametz - MG'A                          : 10:29:50\n" +
        "     Achilat Chametz - MG'A 120 Minutes              : 10:13:50\n" +
        "     Achilat Chametz - MG'A 16.1 deg                 : 10:20:44\n" +
        "     Achilat Chametz - MG'A 19.8 deg                 : 10:11:36\n" +
        "     Achilat Chametz - MG'A 72 Minutes Zmaniyot      : 10:29:50\n" +
        "     Achilat Chametz - MG'A 90 Minutes               : 10:23:50\n" +
        "     Achilat Chametz - MG'A 90 Minutes Zmaniyot      : 10:19:52\n" +
        "     Achilat Chametz - MG'A 96 Minutes               : 10:21:50\n" +
        "     Achilat Chametz - MG'A 96 Minutes Zmaniyot      : 10:17:36\n" +
        "     Achilat Chametz - Baal HaTanya                  : 10:52:19\n" +
        "        Biur Chametz - GR'A                          : 12:01:47\n" +
        "        Biur Chametz - MG'A 72 Minutes               : 11:49:47\n" +
        "        Biur Chametz - MG'A 16.1 deg                 : 11:45:22\n" +
        "        Biur Chametz - Baal HaTanya                  : 12:01:02\n" +
        "             Chatzot - Mid-Day                       : 13:09:44\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:43:42\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:52:21\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:39:44\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:43:42\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:49:42\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:28:00\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:44:06\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:44:06\n" +
        "       Mincha Ketana - GR'A                          : 17:07:33\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 18:06:17\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:49:33\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:22:14\n" +
        "       Mincha Ketana - Baal HaTanya                  : 17:10:15\n" +
        "       Plag HaMincha - GR'A                          : 18:32:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 20:07:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:20:04\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:52:06\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 20:03:15\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 20:14:16\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:57:45\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:19:49\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:29:29\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:37:02\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:43:44\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:53:10\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:48:29\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:58:33\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:59:49\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 19:05:28\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:57:15\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:22:04\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:36:08\n" +
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
        "     Bein Hashmashot - 58.5 Minutes                  : 20:55:55\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:23:11\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:32:51\n" +
        "           Fast Ends - MG'A 120 Minutes              : 21:57:25\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 22:13:18\n" +
        "           Fast Ends - 3.5 deg                       : 20:13:57\n" +
        "           Fast Ends - Baal HaTanya                  : 20:29:44\n" +
        "           Fast Ends - 6.5 deg                       : 20:32:56\n" +
        "           Fast Ends - 8.5 deg                       : 20:45:51\n" +
        "           Fast Ends - 16.1 deg                      : 21:37:54\n" +
        "           Fast Ends - 18 deg                        : 21:51:58\n" +
        "           Fast Ends - 19.8 deg                      : 22:05:52\n" +
        "           Fast Ends - 26 deg                        : 23:00:40\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 20:57:25\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 21:09:25\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 21:18:57\n" +
        "           Fast Ends - MG'A 90 Minutes               : 21:27:25\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 21:39:20\n" +
        "           Fast Ends - 96 Minutes                    : 21:33:25\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 21:46:08\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 20:37:25\n" +
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
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:57:25\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 22:13:18\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 20:13:57\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:29:44\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:32:56\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:45:51\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:37:54\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:51:58\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 22:05:52\n" +
        "    Tzait Hacochavim - 26 deg                        : 23:00:40\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:57:25\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 21:09:25\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:18:57\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:27:25\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:39:20\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:33:25\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:46:08\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:37:25\n" +
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
        "            Midnight - Mid-night                     :  1:08:47\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  @Test
  public void asara_bTevet_2015() {

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.JANUARY, 1 );
    verifyZmanim( ezraBessaroth, date, fastDayZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:57:05\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  6:31:52\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:11:38\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:16:34\n" +
        "      Alot Hashachar - 18 deg                        :  6:04:52\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:53:53\n" +
        "      Alot Hashachar - 26 deg                        :  5:16:36\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:55:17\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:45:05\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  7:05:57\n" +
        "      Alot Hashachar - 90 Minutes                    :  6:27:05\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:53:10\n" +
        "      Alot Hashachar - 96 Minutes                    :  6:21:05\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  6:48:55\n" +
        "       Talit/Tefilin - 7.65 deg                      :  7:10:22\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:58:17\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:53:46\n" +
        "       Talit/Tefilin - 11 deg                        :  6:48:38\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:45:26\n" +
        "              Hanetz - Sunrise                       :  7:57:05\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:55:17\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:12:45\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:49:32\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:00:16\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:36:34\n" +
        "      Sof Zman Shema - GR'A                          : 10:04:55\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:03:04\n" +
        "      Sof Zman Shema - MG'A                          :  9:28:55\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:04:55\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:14:39\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:03:19\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:39:21\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:19:55\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:32:57\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:16:55\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:30:50\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:55:09\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:02:15\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:12:45\n" +
        "     Sof Zman Tefila - GR'A                          : 10:47:31\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:26:46\n" +
        "     Sof Zman Tefila - MG'A                          : 10:23:31\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:07:31\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:14:01\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:06:28\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:30:29\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:17:31\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:26:13\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:15:31\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:24:48\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:45:45\n" +
        "             Chatzot - Mid-Day                       : 12:12:45\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:09:04\n" +
        "       Mincha Gedola - GR'A                          : 12:34:03\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 12:42:26\n" +
        "       Mincha Gedola - 30 Minutes                    : 12:42:45\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 12:42:45\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 12:40:03\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 12:32:17\n" +
        "       Mincha Gedola - Baal HaTanya                  : 12:34:30\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 12:42:45\n" +
        "       Mincha Ketana - GR'A                          : 14:41:52\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 15:40:31\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 15:23:52\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 15:02:53\n" +
        "       Mincha Ketana - Baal HaTanya                  : 14:45:00\n" +
        "       Plag HaMincha - GR'A                          : 15:35:08\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 17:10:08\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 16:42:36\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 16:54:44\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 17:03:59\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 17:12:41\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 17:42:13\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 16:22:27\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 16:32:08\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 16:15:37\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 16:46:23\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 16:25:44\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 16:51:08\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 16:29:06\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 16:05:39\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 16:11:31\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 16:03:10\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 15:24:40\n" +
        "       Plag HaMincha - Baal HaTanya                  : 15:39:22\n" +
        "               Shkia - Sunset                        : 16:28:24\n" +
        "               Shkia - Sunset (with elevation)       : 16:30:12\n" +
        "       Latest Mincha - Mishna Berura                 : 16:28:24\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 16:37:59\n" +
        "       Latest Mincha - Hacham Ben Sion               : 16:54:24\n" +
        "     Bein Hashmashot - 13 Degrees                    : 17:51:05\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 17:26:54\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 16:57:52\n" +
        "     Bein Hashmashot - 2 Stars                       : 17:02:37\n" +
        "           Fast Ends - MG'A 120 Minutes              : 18:28:24\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 17:53:37\n" +
        "           Fast Ends - 3.5 deg                       : 16:47:09\n" +
        "           Fast Ends - Baal HaTanya                  : 17:04:09\n" +
        "           Fast Ends - 6.5 deg                       : 17:07:30\n" +
        "           Fast Ends - 8.5 deg                       : 17:20:42\n" +
        "           Fast Ends - 16.1 deg                      : 18:08:56\n" +
        "           Fast Ends - 18 deg                        : 18:20:38\n" +
        "           Fast Ends - 19.8 deg                      : 18:31:37\n" +
        "           Fast Ends - 26 deg                        : 19:08:55\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 17:28:24\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 17:40:24\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 17:19:32\n" +
        "           Fast Ends - MG'A 90 Minutes               : 17:58:24\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 17:32:19\n" +
        "           Fast Ends - 96 Minutes                    : 18:04:24\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 17:36:34\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 17:08:24\n" +
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
        "    Tzait Hacochavim - MG'A 120 Minutes              : 18:28:24\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 17:53:37\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 16:47:09\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 17:04:09\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 17:07:30\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 17:20:42\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 18:08:56\n" +
        "    Tzait Hacochavim - 18 deg                        : 18:20:38\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 18:31:37\n" +
        "    Tzait Hacochavim - 26 deg                        : 19:08:55\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 17:28:24\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 17:40:24\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 17:19:32\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 17:58:24\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 17:32:19\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 18:04:24\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 17:36:34\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 17:08:24\n" +
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
        "            Midnight - Mid-night                     :  0:12:44\n" +
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
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:47:18\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:06:39\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:15:24\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:32:42\n" +
        "      Sof Zman Shema - GR'A                          : 10:09:55\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:02:40\n" +
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
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:47:18\n" +
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
        "             Chatzot - Mid-Day                       : 12:47:18\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:32:48\n" +
        "       Mincha Gedola - GR'A                          : 13:13:32\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:20:36\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:17:18\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:17:18\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:19:32\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:06:20\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:13:53\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:17:18\n" +
        "       Mincha Ketana - GR'A                          : 15:50:54\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:40:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:32:54\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:09:27\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:53:23\n" +
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
        "            Midnight - Mid-night                     :  0:46:37\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:32:48\n"
    );

    verifyZmanim( oakParkWithElevation, date, erevShabbatZmanim,
      "Oak Park, Michigan\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:32:32\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:47:37\n" +
        "      Alot Hashachar - Baal HaTanya                  :  6:03:50\n" +
        "      Alot Hashachar - 16.1 deg                      :  6:08:10\n" +
        "      Alot Hashachar - 18 deg                        :  5:57:52\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:48:06\n" +
        "      Alot Hashachar - 26 deg                        :  5:14:27\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:30:02\n" +
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
        "              Hanetz - Sunrise (with elevation)      :  7:30:02\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:47:18\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:06:39\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:15:24\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:32:42\n" +
        "      Sof Zman Shema - GR'A                          : 10:09:55\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:02:40\n" +
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
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:47:18\n" +
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
        "             Chatzot - Mid-Day                       : 12:47:18\n" +
        "             Chatzot - Fixed Local Mid-Day           : 12:32:48\n" +
        "       Mincha Gedola - GR'A                          : 13:13:32\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:20:36\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:17:18\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:17:18\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:19:32\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:06:20\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:13:53\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:17:18\n" +
        "       Mincha Ketana - GR'A                          : 15:50:54\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:40:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:32:54\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:09:27\n" +
        "       Mincha Ketana - Baal HaTanya                  : 15:53:23\n" +
        "       Plag HaMincha - GR'A                          : 16:56:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:31:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:19:32\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:03:21\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:11:32\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:19:16\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 18:45:58\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 17:43:43\n" +
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
        "            Midnight - Mid-night                     :  0:46:37\n" +
        "            Midnight - Fixed Local Mid-Night         :  0:32:48\n"
    );
  }


  @Test
  public void november_26_2017() {

    final String haRambanSynagogueExpected = "HaRamban Synagogue\n" +
      "      Alot Hashachar - 120 Minutes                   :  4:16:55\n" +
      "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:33:48\n" +
      "      Alot Hashachar - Baal HaTanya                  :  4:56:40\n" +
      "      Alot Hashachar - 16.1 deg                      :  5:00:33\n" +
      "      Alot Hashachar - 18 deg                        :  4:51:19\n" +
      "      Alot Hashachar - 19.8 deg                      :  4:42:36\n" +
      "      Alot Hashachar - 26 deg                        :  4:12:50\n" +
      "      Alot Hashachar - 60 Minutes                    :  5:12:20\n" +
      "      Alot Hashachar - 72 Minutes                    :  5:04:55\n" +
      "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:15:03\n" +
      "      Alot Hashachar - 90 Minutes                    :  4:46:55\n" +
      "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:59:35\n" +
      "      Alot Hashachar - 96 Minutes                    :  4:40:55\n" +
      "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:54:25\n" +
      "       Talit/Tefilin - 7.65 deg                      :  5:42:16\n" +
      "       Talit/Tefilin - 9.5 deg                       :  5:33:02\n" +
      "       Talit/Tefilin - 10.2 deg                      :  5:29:33\n" +
      "       Talit/Tefilin - 11 deg                        :  5:25:36\n" +
      "       Talit/Tefilin - 11.5 deg                      :  5:23:07\n" +
      "              Hanetz - Sunrise                       :  6:16:55\n" +
      "              Hanetz - Sunrise (with elevation)      :  6:12:20\n" +
      "      Sof Zman Shema - 3 Hours before Chatzot        :  8:26:17\n" +
      "      Sof Zman Shema - 16.1 deg to Sunset            :  7:54:19\n" +
      "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:02:17\n" +
      "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:15:12\n" +
      "      Sof Zman Shema - GR'A                          :  8:51:36\n" +
      "      Sof Zman Shema - GR'A Kol Eliyahu              :  8:58:00\n" +
      "      Sof Zman Shema - MG'A                          :  8:15:36\n" +
      "      Sof Zman Shema - MG'A 120 Minutes              :  7:51:36\n" +
      "      Sof Zman Shema - MG'A 16.1 deg                 :  8:13:25\n" +
      "      Sof Zman Shema - MG'A 19.8 deg                 :  8:04:26\n" +
      "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  8:20:40\n" +
      "      Sof Zman Shema - MG'A 90 Minutes               :  8:06:36\n" +
      "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:12:56\n" +
      "      Sof Zman Shema - 96 Minutes                    :  8:03:36\n" +
      "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:10:21\n" +
      "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  7:58:27\n" +
      "      Sof Zman Shema - Baal HaTanya                  :  8:49:40\n" +
      "     Sof Zman Tefila - 2 Hours before Chatzot        :  9:26:17\n" +
      "     Sof Zman Tefila - GR'A                          :  9:43:09\n" +
      "     Sof Zman Tefila - Chacham Yosef Harari-Raful    :  9:15:15\n" +
      "     Sof Zman Tefila - MG'A                          :  9:19:09\n" +
      "     Sof Zman Tefila - MG'A 120 Minutes              :  9:03:09\n" +
      "     Sof Zman Tefila - MG'A 16.1 deg                 :  9:17:42\n" +
      "     Sof Zman Tefila - MG'A 19.8 deg                 :  9:11:43\n" +
      "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      :  9:22:32\n" +
      "     Sof Zman Tefila - MG'A 90 Minutes               :  9:13:09\n" +
      "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      :  9:17:23\n" +
      "     Sof Zman Tefila - MG'A 96 Minutes               :  9:11:09\n" +
      "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      :  9:15:39\n" +
      "     Sof Zman Tefila - Baal HaTanya                  :  9:41:52\n" +
      "             Chatzot - Mid-Day                       : 11:26:17\n" +
      "             Chatzot - Fixed Local Mid-Day           : 11:39:04\n" +
      "       Mincha Gedola - GR'A                          : 11:52:03\n" +
      "       Mincha Gedola - MG'A 16.1 deg                 : 11:58:25\n" +
      "       Mincha Gedola - 30 Minutes                    : 11:56:17\n" +
      "       Mincha Gedola - Lechumra 30 Minutes           : 11:56:17\n" +
      "       Mincha Gedola - MG'A 72 Minutes               : 11:58:03\n" +
      "       Mincha Gedola - Chacham Yosef Harari-Raful    : 11:45:22\n" +
      "       Mincha Gedola - Baal HaTanya                  : 11:52:23\n" +
      "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 11:56:17\n" +
      "       Mincha Ketana - GR'A                          : 14:26:44\n" +
      "       Mincha Ketana - MG'A 16.1 deg                 : 15:11:17\n" +
      "       Mincha Ketana - MG'A 72 Minutes               : 15:08:44\n" +
      "       Mincha Ketana - Chacham Yosef Harari-Raful    : 14:45:31\n" +
      "       Mincha Ketana - Baal HaTanya                  : 14:28:59\n" +
      "       Plag HaMincha - GR'A                          : 15:31:11\n" +
      "       Plag HaMincha - MG'A 120 Minutes              : 17:06:11\n" +
      "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 16:52:49\n" +
      "       Plag HaMincha - MG'A 16.1 deg                 : 16:31:38\n" +
      "       Plag HaMincha - MG'A 18 deg                   : 16:38:57\n" +
      "       Plag HaMincha - MG'A 19.8 deg                 : 16:45:51\n" +
      "       Plag HaMincha - MG'A 26 deg                   : 17:09:24\n" +
      "       Plag HaMincha - MG'A 60 Minutes               : 16:18:12\n" +
      "       Plag HaMincha - MG'A 72 Minutes               : 16:28:11\n" +
      "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 16:20:10\n" +
      "       Plag HaMincha - MG'A 90 Minutes               : 16:42:26\n" +
      "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 16:32:25\n" +
      "       Plag HaMincha - MG'A 96 Minutes               : 16:47:11\n" +
      "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 16:36:30\n" +
      "       Plag HaMincha - Chacham Yosef Harari-Raful    : 16:00:34\n" +
      "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 15:58:05\n" +
      "       Plag HaMincha - 16.1 deg - 7.083 deg          : 15:51:43\n" +
      "       Plag HaMincha - 16.1 deg - Sunset             : 15:23:14\n" +
      "       Plag HaMincha - Baal HaTanya                  : 15:34:15\n" +
      "               Shkia - Sunset                        : 16:35:38\n" +
      "               Shkia - Sunset (with elevation)       : 16:40:13\n" +
      "       Latest Mincha - Mishna Berura                 : 16:35:38\n" +
      "       Latest Mincha - Hacham Ovadia Yosef           : 16:47:14\n" +
      "       Latest Mincha - Hacham Ben Sion               : 17:01:38\n" +
      "     Bein Hashmashot - 13 Degrees                    : 17:38:00\n" +
      "     Bein Hashmashot - 58.5 Minutes                  : 17:34:08\n" +
      "     Bein Hashmashot - 13.5 Minutes                  : 16:53:56\n" +
      "     Bein Hashmashot - 2 Stars                       : 17:01:50\n" +
      "    Tzait Hacochavim - MG'A 120 Minutes              : 18:35:38\n" +
      "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 18:18:45\n" +
      "    Tzait Hacochavim - 3.5 deg                       : 16:49:19\n" +
      "    Tzait Hacochavim - Baal HaTanya                  : 17:01:59\n" +
      "    Tzait Hacochavim - 6.5 deg                       : 17:04:31\n" +
      "    Tzait Hacochavim - 8.5 deg                       : 17:14:32\n" +
      "    Tzait Hacochavim - 16.1 deg                      : 17:52:00\n" +
      "    Tzait Hacochavim - 18 deg                        : 18:01:14\n" +
      "    Tzait Hacochavim - 19.8 deg                      : 18:09:57\n" +
      "    Tzait Hacochavim - 26 deg                        : 18:39:42\n" +
      "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 17:35:38\n" +
      "    Tzait Hacochavim - Rambam / Rabainu Tam          : 17:47:38\n" +
      "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 17:37:30\n" +
      "    Tzait Hacochavim - MG'A 90 Minutes               : 18:05:38\n" +
      "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 17:52:58\n" +
      "    Tzait Hacochavim - 96 Minutes                    : 18:11:38\n" +
      "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 17:58:08\n" +
      "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 17:15:38\n" +
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
      "            Midnight - Mid-night                     : 23:26:42\n" +
      "            Midnight - Fixed Local Mid-Night         : 23:39:04\n";

    HebrewDate date = HebrewCalendar.getForDate( 2017, Calendar.NOVEMBER, 26 );
    verifyZmanim( haRambanSynagogue, date, dailyZmanim, haRambanSynagogueExpected );
  }


} // End of UT_Zmanim class
