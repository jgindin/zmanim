/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;

import org.junit.Test;

import java.util.Calendar;

@SuppressWarnings( "MagicNumber" )
public class UT_YomTovCandleLighting
  extends ZmanimBaseTest {

  @Test
  public void yomTovSheniMidWeek() {

    // 2nd day of Sukkot
    HebrewDate date = HebrewCalendar.getForDate( 2019, Calendar.OCTOBER, 14 );
    verifyZmanim( ezraBessaroth, date, false, yomSheniZemanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:26:12\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:36:43\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:50:25\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:55:12\n" +
        "      Alot Hashachar - 18 deg                        :  5:43:51\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:33:02\n" +
        "      Alot Hashachar - 26 deg                        :  4:55:10\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:24:41\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:14:12\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:20:31\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:56:12\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:04:05\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:50:12\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:58:37\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:45:23\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:34:24\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:30:14\n" +
        "       Talit/Tefilin - 11 deg                        :  6:25:30\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:22:32\n" +
        "              Hanetz - Sunrise                       :  7:26:12\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:24:41\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:55:09\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:02:10\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:11:31\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:31:10\n" +
        "      Sof Zman Shema - GR'A                          : 10:10:26\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:17:38\n" +
        "      Sof Zman Shema - MG'A                          :  9:34:26\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:10:26\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:24:53\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:13:47\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:37:35\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:25:26\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:29:22\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:22:26\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:26:38\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:07:02\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:08:10\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:26:27\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:32:08\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:32:38\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:41:38\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:16:52\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:55:09\n" +
        "     Sof Zman Tefila - GR'A                          : 11:05:10\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:34:43\n" +
        "     Sof Zman Tefila - MG'A                          : 10:41:10\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:25:10\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:47\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:27:22\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:43:17\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:35:10\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:37:48\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:33:10\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:35:59\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:03:40\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:14:16\n" +
        "             Chatzot - Mid-Day                       : 12:55:09\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:22:02\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:29:31\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:25:09\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:25:09\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:28:02\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:13:35\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:22:24\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:25:09\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:27:02\n" +
        "       Mincha Ketana - GR'A                          : 16:06:16\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:59:12\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:48:16\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:24:14\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:08:54\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:01:25\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:13:09\n" +
        "       Plag HaMincha - GR'A                          : 17:14:41\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:49:41\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:41:22\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:26:34\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:35:32\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:44:03\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 19:13:54\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 18:02:02\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 18:11:41\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 18:06:42\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:25:56\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:19:42\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:30:41\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:24:02\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:43:41\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:46:14\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:38:42\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 17:05:13\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:18:16\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:21:11\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:18:53\n" +
        "               Shkia - Sunset                        : 18:23:07\n" +
        "               Shkia - Sunset (with elevation)       : 18:24:38\n" +
        "       Latest Mincha - Mishna Berura                 : 18:23:07\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:35:26\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:49:07\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:36:59\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:21:37\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:47:00\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:54:33\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:23:07\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:12:36\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:39:09\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:54:04\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:57:02\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 19:08:55\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:53:56\n" +
        "    Tzait Hacochavim - 18 deg                        : 20:05:15\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 20:16:02\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:53:45\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:23:07\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:35:07\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:28:49\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:53:07\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:45:14\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 19:59:07\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:50:42\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 19:03:07\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:40:03\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:44:21\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:45:47\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:46:55\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:53:21\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:53:46\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 19:00:30\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 19:08:55\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 19:13:07\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 19:08:07\n" +
        "     Candle Lighting - MG'A 120 Minutes              : 20:23:07\n" +
        "     Candle Lighting - GR'A 120 Minutes Zmaniyot     : 20:12:36\n" +
        "     Candle Lighting - 3.5 deg                       : 18:39:09\n" +
        "     Candle Lighting - 6.5 deg                       : 18:57:02\n" +
        "     Candle Lighting - 8.5 deg                       : 19:08:55\n" +
        "     Candle Lighting - 16.1 deg                      : 19:53:56\n" +
        "     Candle Lighting - 18 deg                        : 20:05:15\n" +
        "     Candle Lighting - 19.8 deg                      : 20:16:02\n" +
        "     Candle Lighting - 26 deg                        : 20:53:45\n" +
        "     Candle Lighting - Chavas Yair and Divray Malkiel: 19:23:07\n" +
        "     Candle Lighting - Rambam / Rabainu Tam          : 19:35:07\n" +
        "     Candle Lighting - 72 Minutes Zmaniyot           : 19:28:49\n" +
        "     Candle Lighting - MG'A 90 Minutes               : 19:53:07\n" +
        "     Candle Lighting - GR'A 90 Minutes Zmaniyot      : 19:45:14\n" +
        "     Candle Lighting - 96 Minutes                    : 19:59:07\n" +
        "     Candle Lighting - GR'A 96 Minutes Zmaniyot      : 19:50:42\n" +
        "     Candle Lighting - Chacham Yosef Harari-Raful    : 19:03:07\n" +
        "     Candle Lighting - Geonim 3.65 deg               : 18:40:03\n" +
        "     Candle Lighting - Geonim 4.37 deg               : 18:44:21\n" +
        "     Candle Lighting - Geonim 4.61 deg               : 18:45:47\n" +
        "     Candle Lighting - Geonim 4.8 deg                : 18:46:55\n" +
        "     Candle Lighting - Geonim 5.88 deg               : 18:53:21\n" +
        "     Candle Lighting - Geonim 5.95 deg               : 18:53:46\n" +
        "     Candle Lighting - Geonim 7.083 deg              : 19:00:30\n" +
        "     Candle Lighting - Geonim 8.5 deg                : 19:08:55\n" +
        "     Candle Lighting - Rabbi Moshe Feinstein         : 19:13:07\n" +
        "            Midnight - Mid-night                     :  0:55:02\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );

  }


  @Test
  public void yomTovStartsErevShabbat() {

    // 1st day of Pesach
    HebrewDate date = HebrewCalendar.getForDate( 2018, Calendar.MARCH, 30 );
    verifyZmanim( ezraBessaroth, date, false, erevPesachZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:51:29\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:44:02\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:12:04\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:17:18\n" +
        "      Alot Hashachar - 18 deg                        :  5:04:47\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:52:39\n" +
        "      Alot Hashachar - 26 deg                        :  4:07:51\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:49:59\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:39:29\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:35:01\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:21:29\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:15:53\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:15:29\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:09:31\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:10:26\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:59:04\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:54:44\n" +
        "       Talit/Tefilin - 11 deg                        :  5:49:46\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:46:38\n" +
        "              Hanetz - Sunrise                       :  6:51:29\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:49:59\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:13:34\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:52:02\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:01:28\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:15:19\n" +
        "      Sof Zman Shema - GR'A                          : 10:02:41\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:00:16\n" +
        "      Sof Zman Shema - MG'A                          :  9:26:41\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:02:41\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:15:41\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:03:24\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:24:26\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:17:41\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:14:53\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:14:41\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:11:41\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:56:56\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:00:27\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:06:55\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:13:11\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:15:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:24:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  9:59:31\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:13:34\n" +
        "     Sof Zman Tefila - GR'A                          : 11:06:24\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:28:45\n" +
        "     Sof Zman Tefila - MG'A                          : 10:42:24\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:26:24\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:35:09\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:26:59\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:40:55\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:36:24\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:34:32\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:34:24\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:32:25\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:04:55\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:02:42\n" +
        "     Achilat Chametz - 2 Hours before Chatzot        : 11:13:34\n" +
        "     Achilat Chametz - GR'A                          : 11:06:24\n" +
        "     Achilat Chametz - Chacham Yosef Harari-Raful    : 10:28:45\n" +
        "     Achilat Chametz - MG'A                          : 10:42:24\n" +
        "     Achilat Chametz - MG'A 120 Minutes              : 10:26:24\n" +
        "     Achilat Chametz - MG'A 16.1 deg                 : 10:35:09\n" +
        "     Achilat Chametz - MG'A 19.8 deg                 : 10:26:59\n" +
        "     Achilat Chametz - MG'A 72 Minutes Zmaniyot      : 10:42:24\n" +
        "     Achilat Chametz - MG'A 90 Minutes               : 10:36:24\n" +
        "     Achilat Chametz - MG'A 90 Minutes Zmaniyot      : 10:34:32\n" +
        "     Achilat Chametz - MG'A 96 Minutes               : 10:34:24\n" +
        "     Achilat Chametz - MG'A 96 Minutes Zmaniyot      : 10:32:25\n" +
        "     Achilat Chametz - Baal HaTanya                  : 11:04:55\n" +
        "        Biur Chametz - GR'A                          : 12:10:08\n" +
        "        Biur Chametz - MG'A 72 Minutes               : 11:58:08\n" +
        "        Biur Chametz - MG'A 16.1 deg                 : 11:54:37\n" +
        "        Biur Chametz - Baal HaTanya                  : 12:09:24\n" +
        "             Chatzot - Mid-Day                       : 13:13:34\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:45:44\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:53:48\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:43:34\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:45:44\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:51:44\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:32:21\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:46:07\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:46:07\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:50:04\n" +
        "       Mincha Ketana - GR'A                          : 16:56:55\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 17:52:11\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:38:55\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:12:39\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:59:32\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:51:24\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:55:48\n" +
        "       Plag HaMincha - GR'A                          : 18:16:35\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 19:51:35\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 19:57:29\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:31:31\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:41:30\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 19:51:12\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:27:04\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:03:56\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:13:35\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:17:08\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:27:50\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:32:16\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:32:35\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:37:18\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:44:27\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:48:22\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:40:34\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:06:46\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:20:08\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 18:22:43\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 18:16:46\n" +
        "     Candle Lighting - 18 Minutes                    : 19:18:15\n" +
        "     Candle Lighting - 15 Minutes                    : 19:21:15\n" +
        "     Candle Lighting - 20 Minutes                    : 19:16:15\n" +
        "     Candle Lighting - 21 Minutes                    : 19:15:15\n" +
        "     Candle Lighting - 22 Minutes                    : 19:14:15\n" +
        "     Candle Lighting - 30 Minutes                    : 19:06:15\n" +
        "     Candle Lighting - 40 Minutes                    : 18:56:15\n" +
        "               Shkia - Sunset                        : 19:36:15\n" +
        "               Shkia - Sunset (with elevation)       : 19:37:45\n" +
        "       Latest Mincha - Mishna Berura                 : 19:36:15\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:50:35\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:02:15\n" +
        "     Bein Hashmashot - 13 Degrees                    : 20:52:21\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:34:45\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:00:29\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:09:15\n" +
        "           Fast Ends - MG'A 120 Minutes              : 21:36:15\n" +
        "           Fast Ends - GR'A 120 Minutes Zmaniyot     : 21:43:42\n" +
        "           Fast Ends - 3.5 deg                       : 19:52:14\n" +
        "           Fast Ends - Baal HaTanya                  : 20:07:22\n" +
        "           Fast Ends - 6.5 deg                       : 20:10:25\n" +
        "           Fast Ends - 8.5 deg                       : 20:22:41\n" +
        "           Fast Ends - 16.1 deg                      : 21:10:51\n" +
        "           Fast Ends - 18 deg                        : 21:23:27\n" +
        "           Fast Ends - 19.8 deg                      : 21:35:41\n" +
        "           Fast Ends - 26 deg                        : 22:20:56\n" +
        "           Fast Ends - Chavas Yair and Divray Malkiel: 20:36:15\n" +
        "           Fast Ends - Rambam / Rabainu Tam          : 20:48:15\n" +
        "           Fast Ends - 72 Minutes Zmaniyot           : 20:52:43\n" +
        "           Fast Ends - MG'A 90 Minutes               : 21:06:15\n" +
        "           Fast Ends - GR'A 90 Minutes Zmaniyot      : 21:11:50\n" +
        "           Fast Ends - 96 Minutes                    : 21:12:15\n" +
        "           Fast Ends - GR'A 96 Minutes Zmaniyot      : 21:18:13\n" +
        "           Fast Ends - Chacham Yosef Harari-Raful    : 20:16:15\n" +
        "           Fast Ends - Geonim 3.65 deg               : 19:53:09\n" +
        "           Fast Ends - Geonim 4.37 deg               : 19:57:29\n" +
        "           Fast Ends - Geonim 4.61 deg               : 19:58:56\n" +
        "           Fast Ends - Geonim 4.8 deg                : 20:00:05\n" +
        "           Fast Ends - Geonim 5.88 deg               : 20:06:38\n" +
        "           Fast Ends - Geonim 5.95 deg               : 20:07:04\n" +
        "           Fast Ends - Geonim 7.083 deg              : 20:13:59\n" +
        "           Fast Ends - Geonim 8.5 deg                : 20:22:41\n" +
        "           Fast Ends - Rabbi Moshe Feinstein         : 20:26:15\n" +
        "           Fast Ends - Seattle Sephardic             : 20:06:15\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:36:15\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 21:43:42\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 19:52:14\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:07:22\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:10:25\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:22:41\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:10:51\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:23:27\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 21:35:41\n" +
        "    Tzait Hacochavim - 26 deg                        : 22:20:56\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:36:15\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:48:15\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 20:52:43\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:06:15\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:11:50\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:12:15\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:18:13\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:16:15\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 19:53:09\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 19:57:29\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 19:58:56\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 20:00:05\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 20:06:38\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 20:07:04\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:13:59\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:22:41\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:26:15\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:21:15\n" +
        "            Midnight - Mid-night                     :  1:13:25\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  @Test
  public void yomTovSheniStartsMotzaeShabbat() {

    // 2nd day of Pesach
    HebrewDate date = HebrewCalendar.getForDate( 2018, Calendar.MARCH, 31 );
    verifyZmanim( ezraBessaroth, date, false, yomSheniZemanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:49:29\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:41:27\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:09:44\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:15:00\n" +
        "      Alot Hashachar - 18 deg                        :  5:02:25\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:50:11\n" +
        "      Alot Hashachar - 26 deg                        :  4:04:58\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:47:59\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:37:29\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:32:39\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:19:29\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:13:27\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:13:29\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:07:03\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:08:20\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:56:57\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:52:36\n" +
        "       Talit/Tefilin - 11 deg                        :  5:47:36\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:44:28\n" +
        "              Hanetz - Sunrise                       :  6:49:29\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:47:59\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:13:16\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:50:40\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:00:07\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:13:54\n" +
        "      Sof Zman Shema - GR'A                          : 10:01:31\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              :  9:59:16\n" +
        "      Sof Zman Shema - MG'A                          :  9:25:31\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:01:31\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:14:23\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:02:02\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:23:07\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:16:31\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:13:31\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:13:31\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:10:19\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:55:34\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:59:17\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:05:44\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:12:02\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:14:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:23:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  9:58:31\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:13:16\n" +
        "     Sof Zman Tefila - GR'A                          : 11:05:32\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:27:40\n" +
        "     Sof Zman Tefila - MG'A                          : 10:41:32\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:25:32\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:11\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:25:59\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:39:56\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:35:32\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:33:32\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:33:32\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:31:24\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:04:03\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:02:02\n" +
        "             Chatzot - Mid-Day                       : 13:13:16\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:45:35\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:53:41\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:43:16\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:45:35\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:51:35\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:32:02\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:45:57\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:45:57\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:49:55\n" +
        "       Mincha Ketana - GR'A                          : 16:57:37\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 17:53:04\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:39:37\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:13:17\n" +
        "       Mincha Ketana - Baal HaTanya                  : 17:00:15\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:52:03\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:56:37\n" +
        "       Plag HaMincha - GR'A                          : 18:17:38\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 19:52:38\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 19:59:00\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:32:49\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:42:52\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 19:52:37\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:28:51\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:04:59\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:14:38\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:18:27\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:28:53\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:33:40\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:33:38\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:38:44\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:45:28\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:49:29\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:41:40\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:07:48\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:21:12\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 18:23:47\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 18:17:54\n" +
        "               Shkia - Sunset                        : 19:37:40\n" +
        "               Shkia - Sunset (with elevation)       : 19:39:10\n" +
        "       Latest Mincha - Mishna Berura                 : 19:37:40\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:52:04\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:03:40\n" +
        "     Bein Hashmashot - 13 Degrees                    : 20:53:59\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:36:10\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:01:58\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:10:48\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:37:40\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 21:45:41\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 19:53:41\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:08:51\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:11:54\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:24:11\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:12:34\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:25:14\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 21:37:33\n" +
        "    Tzait Hacochavim - 26 deg                        : 22:23:16\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:37:40\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:49:40\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 20:54:29\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:07:40\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:13:41\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:13:40\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:20:05\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:17:40\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 19:54:35\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 19:58:56\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 20:00:24\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 20:01:33\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 20:08:07\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 20:08:32\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:15:28\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:24:11\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:27:40\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:22:40\n" +
        "     Candle Lighting - MG'A 120 Minutes              : 21:37:40\n" +
        "     Candle Lighting - GR'A 120 Minutes Zmaniyot     : 21:45:41\n" +
        "     Candle Lighting - 3.5 deg                       : 19:53:41\n" +
        "     Candle Lighting - 6.5 deg                       : 20:11:54\n" +
        "     Candle Lighting - 8.5 deg                       : 20:24:11\n" +
        "     Candle Lighting - 16.1 deg                      : 21:12:34\n" +
        "     Candle Lighting - 18 deg                        : 21:25:14\n" +
        "     Candle Lighting - 19.8 deg                      : 21:37:33\n" +
        "     Candle Lighting - 26 deg                        : 22:23:16\n" +
        "     Candle Lighting - Chavas Yair and Divray Malkiel: 20:37:40\n" +
        "     Candle Lighting - Rambam / Rabainu Tam          : 20:49:40\n" +
        "     Candle Lighting - 72 Minutes Zmaniyot           : 20:54:29\n" +
        "     Candle Lighting - MG'A 90 Minutes               : 21:07:40\n" +
        "     Candle Lighting - GR'A 90 Minutes Zmaniyot      : 21:13:41\n" +
        "     Candle Lighting - 96 Minutes                    : 21:13:40\n" +
        "     Candle Lighting - GR'A 96 Minutes Zmaniyot      : 21:20:05\n" +
        "     Candle Lighting - Chacham Yosef Harari-Raful    : 20:17:40\n" +
        "     Candle Lighting - Geonim 3.65 deg               : 19:54:35\n" +
        "     Candle Lighting - Geonim 4.37 deg               : 19:58:56\n" +
        "     Candle Lighting - Geonim 4.61 deg               : 20:00:24\n" +
        "     Candle Lighting - Geonim 4.8 deg                : 20:01:33\n" +
        "     Candle Lighting - Geonim 5.88 deg               : 20:08:07\n" +
        "     Candle Lighting - Geonim 5.95 deg               : 20:08:32\n" +
        "     Candle Lighting - Geonim 7.083 deg              : 20:15:28\n" +
        "     Candle Lighting - Geonim 8.5 deg                : 20:24:11\n" +
        "     Candle Lighting - Rabbi Moshe Feinstein         : 20:27:40\n" +
        "            Midnight - Mid-night                     :  1:13:07\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }


  @Test
  public void yomTovSheniIsShabbat() {

    // 8th day of Pesach
    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.APRIL, 11 );
    verifyZmanim( ezraBessaroth, date, false, shabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  4:28:16\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  4:14:11\n" +
        "      Alot Hashachar - Baal HaTanya                  :  4:44:12\n" +
        "      Alot Hashachar - 16.1 deg                      :  4:49:52\n" +
        "      Alot Hashachar - 18 deg                        :  4:36:18\n" +
        "      Alot Hashachar - 19.8 deg                      :  4:22:59\n" +
        "      Alot Hashachar - 26 deg                        :  3:31:40\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:26:45\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:16:16\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:07:49\n" +
        "      Alot Hashachar - 90 Minutes                    :  4:58:16\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  4:47:42\n" +
        "      Alot Hashachar - 96 Minutes                    :  4:52:16\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  4:41:00\n" +
        "       Talit/Tefilin - 7.65 deg                      :  5:45:59\n" +
        "       Talit/Tefilin - 9.5 deg                       :  5:34:09\n" +
        "       Talit/Tefilin - 10.2 deg                      :  5:29:37\n" +
        "       Talit/Tefilin - 11 deg                        :  5:24:23\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:21:06\n" +
        "              Hanetz - Sunrise                       :  6:28:16\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:26:45\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:10:13\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:35:36\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  8:45:19\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  8:59:04\n" +
        "      Sof Zman Shema - GR'A                          :  9:49:25\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              :  9:48:40\n" +
        "      Sof Zman Shema - MG'A                          :  9:13:25\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  8:49:25\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:00:20\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  8:46:58\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:09:11\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:04:25\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  8:59:08\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:01:25\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  8:55:47\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:40:38\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:47:08\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  8:52:41\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  8:59:28\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:03:40\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:12:40\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day:  9:47:54\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:10:13\n" +
        "     Sof Zman Tefila - GR'A                          : 10:56:28\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:16:09\n" +
        "     Sof Zman Tefila - MG'A                          : 10:32:28\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:16:28\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:23:50\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:14:58\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:29:38\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:26:28\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:22:56\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:24:28\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:20:42\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:54:57\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 10:54:57\n" +
        "             Chatzot - Mid-Day                       : 13:10:13\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:44:05\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:52:34\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:40:13\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:44:05\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:50:05\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:28:52\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:44:28\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:44:28\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:48:35\n" +
        "       Mincha Ketana - GR'A                          : 17:05:13\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 18:03:03\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:47:13\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 17:20:07\n" +
        "       Mincha Ketana - Baal HaTanya                  : 17:07:54\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:59:10\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 17:05:29\n" +
        "       Plag HaMincha - GR'A                          : 18:29:01\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 20:04:01\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 20:15:11\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 19:47:25\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:58:16\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 20:08:55\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 20:50:14\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 19:16:22\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 19:26:01\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 19:32:43\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 19:40:16\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 19:48:38\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 19:45:01\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 19:53:57\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:56:29\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 19:01:42\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:53:35\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 18:18:46\n" +
        "       Plag HaMincha - Baal HaTanya                  : 18:32:39\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 18:35:07\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 18:29:56\n" +
        "               Shkia - Sunset                        : 19:52:50\n" +
        "               Shkia - Sunset (with elevation)       : 19:54:22\n" +
        "       Latest Mincha - Mishna Berura                 : 19:52:50\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 20:07:55\n" +
        "       Latest Mincha - Hacham Ben Sion               : 20:18:50\n" +
        "     Bein Hashmashot - 13 Degrees                    : 21:11:59\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 20:51:20\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 20:18:11\n" +
        "     Bein Hashmashot - 2 Stars                       : 20:27:38\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 21:52:50\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 22:06:55\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 20:09:13\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 20:24:50\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 20:27:59\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 20:40:45\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 21:31:47\n" +
        "    Tzait Hacochavim - 18 deg                        : 21:45:28\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 21:58:55\n" +
        "    Tzait Hacochavim - 26 deg                        : 22:51:00\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 20:52:50\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 21:04:50\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 21:13:17\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 21:22:50\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 21:33:24\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 21:28:50\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 21:40:06\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 20:32:50\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 20:10:09\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 20:14:38\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 20:16:07\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 20:17:19\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 20:24:05\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 20:24:31\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 20:31:41\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 20:40:45\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 20:42:50\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 20:37:50\n" +
        "            Havdalah - MG'A 120 Minutes              : 21:52:50\n" +
        "            Havdalah - GR'A 120 Minutes Zmaniyot     : 22:06:55\n" +
        "            Havdalah - 3.5 deg                       : 20:09:13\n" +
        "            Havdalah - 6.5 deg                       : 20:27:59\n" +
        "            Havdalah - 8.5 deg                       : 20:40:45\n" +
        "            Havdalah - 16.1 deg                      : 21:31:47\n" +
        "            Havdalah - 18 deg                        : 21:45:28\n" +
        "            Havdalah - 19.8 deg                      : 21:58:55\n" +
        "            Havdalah - 26 deg                        : 22:51:00\n" +
        "            Havdalah - Chavas Yair and Divray Malkiel: 20:52:50\n" +
        "            Havdalah - Rambam / Rabainu Tam          : 21:04:50\n" +
        "            Havdalah - 72 Minutes Zmaniyot           : 21:13:17\n" +
        "            Havdalah - MG'A 90 Minutes               : 21:22:50\n" +
        "            Havdalah - GR'A 90 Minutes Zmaniyot      : 21:33:24\n" +
        "            Havdalah - 96 Minutes                    : 21:28:50\n" +
        "            Havdalah - GR'A 96 Minutes Zmaniyot      : 21:40:06\n" +
        "            Havdalah - Chacham Yosef Harari-Raful    : 20:32:50\n" +
        "            Havdalah - Geonim 3.65 deg               : 20:10:09\n" +
        "            Havdalah - Geonim 4.37 deg               : 20:14:38\n" +
        "            Havdalah - Geonim 4.61 deg               : 20:16:07\n" +
        "            Havdalah - Geonim 4.8 deg                : 20:17:19\n" +
        "            Havdalah - Geonim 5.88 deg               : 20:24:05\n" +
        "            Havdalah - Geonim 5.95 deg               : 20:24:31\n" +
        "            Havdalah - Geonim 7.083 deg              : 20:31:41\n" +
        "            Havdalah - Geonim 8.5 deg                : 20:40:45\n" +
        "            Havdalah - Rabbi Moshe Feinstein         : 20:42:50\n" +
        "            Havdalah - Seattle Sephardic             : 20:37:50\n" +
        "            Midnight - Mid-night                     :  1:10:05\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );

  }


  @Test
  public void yomTovSheniIsErevShabbat() {

    // Simchat Torah 2017
    HebrewDate date = HebrewCalendar.getForDate( 2017, Calendar.OCTOBER, 13 );
    verifyZmanim( ezraBessaroth, date, false, erevShabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:25:28\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:35:41\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:49:42\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:54:29\n" +
        "      Alot Hashachar - 18 deg                        :  5:43:07\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:32:18\n" +
        "      Alot Hashachar - 26 deg                        :  4:54:23\n" +
        "      Alot Hashachar - 60 Minutes                    :  6:23:57\n" +
        "      Alot Hashachar - 72 Minutes                    :  6:13:28\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  6:19:36\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:55:28\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  6:03:08\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:49:28\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:57:39\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:44:40\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:33:41\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:29:32\n" +
        "       Talit/Tefilin - 11 deg                        :  6:24:47\n" +
        "       Talit/Tefilin - 11.5 deg                      :  6:21:49\n" +
        "              Hanetz - Sunrise                       :  7:25:28\n" +
        "              Hanetz - Sunrise (with elevation)      :  7:23:57\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        :  9:55:17\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  9:01:53\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:11:13\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:30:43\n" +
        "      Sof Zman Shema - GR'A                          : 10:10:07\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:17:16\n" +
        "      Sof Zman Shema - MG'A                          :  9:34:07\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:10:07\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:24:35\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:13:29\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:37:11\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:25:07\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:28:57\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:22:07\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:26:13\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  9:06:44\n" +
        "      Sof Zman Shema - Baal HaTanya                  : 10:07:51\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:26:05\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:31:46\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:32:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:41:16\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:16:30\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 10:55:17\n" +
        "     Sof Zman Tefila - GR'A                          : 11:05:00\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:34:26\n" +
        "     Sof Zman Tefila - MG'A                          : 10:41:00\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:25:00\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:34:37\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:27:12\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:43:03\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:35:00\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:37:34\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:33:00\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:35:44\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 11:03:30\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:14:01\n" +
        "             Chatzot - Mid-Day                       : 12:55:17\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:22:13\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:29:42\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:25:17\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:25:17\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:28:13\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:13:42\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:22:36\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:25:17\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:27:14\n" +
        "       Mincha Ketana - GR'A                          : 16:06:53\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 16:59:49\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 16:48:53\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:24:50\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:09:31\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:02:02\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:13:43\n" +
        "       Plag HaMincha - GR'A                          : 17:15:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 18:50:29\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 18:42:24\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:27:21\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 18:36:19\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 18:44:51\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 19:14:44\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 18:02:50\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 18:12:29\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 18:07:38\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:26:44\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:20:40\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:31:29\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:25:01\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 17:44:28\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 17:47:00\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 17:39:29\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 17:06:01\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:19:04\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:21:58\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:19:40\n" +
        "     Candle Lighting - 18 Minutes                    : 18:06:06\n" +
        "     Candle Lighting - 15 Minutes                    : 18:09:06\n" +
        "     Candle Lighting - 20 Minutes                    : 18:04:06\n" +
        "     Candle Lighting - 21 Minutes                    : 18:03:06\n" +
        "     Candle Lighting - 22 Minutes                    : 18:02:06\n" +
        "     Candle Lighting - 30 Minutes                    : 17:54:06\n" +
        "     Candle Lighting - 40 Minutes                    : 17:44:06\n" +
        "               Shkia - Sunset                        : 18:24:06\n" +
        "               Shkia - Sunset (with elevation)       : 18:25:37\n" +
        "       Latest Mincha - Mishna Berura                 : 18:24:06\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 18:36:27\n" +
        "       Latest Mincha - Hacham Ben Sion               : 18:50:06\n" +
        "     Bein Hashmashot - 13 Degrees                    : 19:37:56\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:22:36\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 18:47:57\n" +
        "     Bein Hashmashot - 2 Stars                       : 18:55:32\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:24:06\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:13:52\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 18:40:07\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 18:55:01\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 18:58:00\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 19:09:52\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 19:54:54\n" +
        "    Tzait Hacochavim - 18 deg                        : 20:06:13\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 20:17:00\n" +
        "    Tzait Hacochavim - 26 deg                        : 20:54:46\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:24:06\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 19:36:06\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 19:29:58\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 19:54:06\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 19:46:26\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 20:00:06\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 19:51:55\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 19:04:06\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 18:41:01\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 18:45:19\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 18:46:45\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 18:47:53\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 18:54:18\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 18:54:43\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 19:01:27\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 19:09:52\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 19:14:06\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 19:09:06\n" +
        "            Midnight - Mid-night                     :  0:55:10\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );
  }

  @Test
  public void youTovEndsErevShabbat() {

    // 2nd day of Rosh Hashana
    HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.SEPTEMBER, 26 );
    verifyZmanim( ezraBessaroth, date, false, erevShabbatZmanim,
      "Ezra Bessaroth\n" +
        "      Alot Hashachar - 120 Minutes                   :  5:01:23\n" +
        "      Alot Hashachar - 120 Minutes Zmaniyot          :  5:01:53\n" +
        "      Alot Hashachar - Baal HaTanya                  :  5:24:44\n" +
        "      Alot Hashachar - 16.1 deg                      :  5:29:42\n" +
        "      Alot Hashachar - 18 deg                        :  5:17:53\n" +
        "      Alot Hashachar - 19.8 deg                      :  5:06:32\n" +
        "      Alot Hashachar - 26 deg                        :  4:25:49\n" +
        "      Alot Hashachar - 60 Minutes                    :  5:59:54\n" +
        "      Alot Hashachar - 72 Minutes                    :  5:49:23\n" +
        "      Alot Hashachar - GR'A 72 Minutes Zmaniyot      :  5:49:41\n" +
        "      Alot Hashachar - 90 Minutes                    :  5:31:23\n" +
        "      Alot Hashachar - GR'A 90 Minutes Zmaniyot      :  5:31:46\n" +
        "      Alot Hashachar - 96 Minutes                    :  5:25:23\n" +
        "      Alot Hashachar - GR'A 96 Minutes Zmaniyot      :  5:25:47\n" +
        "       Talit/Tefilin - 7.65 deg                      :  6:20:53\n" +
        "       Talit/Tefilin - 9.5 deg                       :  6:09:49\n" +
        "       Talit/Tefilin - 10.2 deg                      :  6:05:36\n" +
        "       Talit/Tefilin - 11 deg                        :  6:00:47\n" +
        "       Talit/Tefilin - 11.5 deg                      :  5:57:46\n" +
        "              Hanetz - Sunrise                       :  7:01:23\n" +
        "              Hanetz - Sunrise (with elevation)      :  6:59:54\n" +
        "      Sof Zman Shema - 3 Hours before Chatzot        : 10:00:29\n" +
        "      Sof Zman Shema - 16.1 deg to Sunset            :  8:51:52\n" +
        "      Sof Zman Shema - 16.1 deg to tzait 7.083 deg   :  9:01:08\n" +
        "      Sof Zman Shema - Chacham Yosef Harari-Raful    :  9:16:52\n" +
        "      Sof Zman Shema - GR'A                          : 10:00:38\n" +
        "      Sof Zman Shema - GR'A Kol Eliyahu              : 10:05:13\n" +
        "      Sof Zman Shema - MG'A                          :  9:24:38\n" +
        "      Sof Zman Shema - MG'A 120 Minutes              :  9:00:38\n" +
        "      Sof Zman Shema - MG'A 16.1 deg                 :  9:14:43\n" +
        "      Sof Zman Shema - MG'A 19.8 deg                 :  9:03:07\n" +
        "      Sof Zman Shema - MG'A 72 Minutes Zmaniyot      :  9:24:47\n" +
        "      Sof Zman Shema - MG'A 90 Minutes               :  9:15:38\n" +
        "      Sof Zman Shema - MG'A 90 Minutes Zmaniyot      :  9:15:50\n" +
        "      Sof Zman Shema - 96 Minutes                    :  9:12:38\n" +
        "      Sof Zman Shema - 96 Minutes Zmaniyot           :  9:12:50\n" +
        "      Sof Zman Shema - R. Yaakov Moshe Hillel of Bet Horaah:  8:56:41\n" +
        "      Sof Zman Shema - Baal HaTanya                  :  9:58:25\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 18° half day:  9:13:28\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 16.1° half day:  9:19:23\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 90 minutes half day:  9:20:13\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - MG'A 72 minutes half day:  9:29:13\n" +
        "      Sof Zman Shema -  Rav Moshe Feinstein - GR'A half day: 10:04:29\n" +
        "     Sof Zman Tefila - 2 Hours before Chatzot        : 11:00:29\n" +
        "     Sof Zman Tefila - GR'A                          : 11:00:23\n" +
        "     Sof Zman Tefila - Chacham Yosef Harari-Raful    : 10:25:55\n" +
        "     Sof Zman Tefila - MG'A                          : 10:36:23\n" +
        "     Sof Zman Tefila - MG'A 120 Minutes              : 10:20:23\n" +
        "     Sof Zman Tefila - MG'A 16.1 deg                 : 10:29:44\n" +
        "     Sof Zman Tefila - MG'A 19.8 deg                 : 10:21:58\n" +
        "     Sof Zman Tefila - MG'A 72 Minutes Zmaniyot      : 10:36:29\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes               : 10:30:23\n" +
        "     Sof Zman Tefila - MG'A 90 Minutes Zmaniyot      : 10:30:31\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes               : 10:28:23\n" +
        "     Sof Zman Tefila - MG'A 96 Minutes Zmaniyot      : 10:28:31\n" +
        "     Sof Zman Tefila - Baal HaTanya                  : 10:58:54\n" +
        "     Sof Zman Tefila -  Rav Moshe Feinstein - GR'A half day: 11:06:00\n" +
        "             Chatzot - Mid-Day                       : 13:00:29\n" +
        "             Chatzot - Fixed Local Mid-Day           : 13:09:04\n" +
        "       Mincha Gedola - GR'A                          : 13:29:46\n" +
        "       Mincha Gedola - MG'A 16.1 deg                 : 13:37:15\n" +
        "       Mincha Gedola - 30 Minutes                    : 13:30:29\n" +
        "       Mincha Gedola - Lechumra 30 Minutes           : 13:30:29\n" +
        "       Mincha Gedola - MG'A 72 Minutes               : 13:35:46\n" +
        "       Mincha Gedola - Chacham Yosef Harari-Raful    : 13:18:34\n" +
        "       Mincha Gedola - Baal HaTanya                  : 13:30:08\n" +
        "       Mincha Gedola - Baal HaTanya Lechumra 30 Minutes: 13:30:29\n" +
        "       Mincha Gedola - Rabbi Yaakov Moshe Hillel     : 13:34:53\n" +
        "       Mincha Ketana - GR'A                          : 16:29:01\n" +
        "       Mincha Ketana - MG'A 16.1 deg                 : 17:22:16\n" +
        "       Mincha Ketana - MG'A 72 Minutes               : 17:11:01\n" +
        "       Mincha Ketana - Chacham Yosef Harari-Raful    : 16:45:45\n" +
        "       Mincha Ketana - Baal HaTanya                  : 16:31:36\n" +
        "       Mincha Ketana - Rabbi Yaakov Moshe Hillel     : 16:23:49\n" +
        "       Mincha Ketana -  Rav Moshe Feinstein - GR'A half day: 16:33:42\n" +
        "       Plag HaMincha - GR'A                          : 17:43:42\n" +
        "       Plag HaMincha - MG'A 120 Minutes              : 19:18:42\n" +
        "       Plag HaMincha - MG'A 120 Minutes Zmaniyot     : 19:18:19\n" +
        "       Plag HaMincha - MG'A 16.1 deg                 : 18:56:02\n" +
        "       Plag HaMincha - MG'A 18 deg                   : 19:05:20\n" +
        "       Plag HaMincha - MG'A 19.8 deg                 : 19:14:16\n" +
        "       Plag HaMincha - MG'A 26 deg                   : 19:46:15\n" +
        "       Plag HaMincha - MG'A 60 Minutes               : 18:31:03\n" +
        "       Plag HaMincha - MG'A 72 Minutes               : 18:40:42\n" +
        "       Plag HaMincha - MG'A 72 Minutes Zmaniyot      : 18:40:28\n" +
        "       Plag HaMincha - MG'A 90 Minutes               : 18:54:57\n" +
        "       Plag HaMincha - MG'A 90 Minutes Zmaniyot      : 18:54:40\n" +
        "       Plag HaMincha - MG'A 96 Minutes               : 18:59:42\n" +
        "       Plag HaMincha - MG'A 96 Minutes Zmaniyot      : 18:59:23\n" +
        "       Plag HaMincha - Chacham Yosef Harari-Raful    : 18:12:04\n" +
        "       Plag HaMincha - R. Moshe Feinstein's Rabbenu Tam: 18:14:53\n" +
        "       Plag HaMincha - 16.1 deg - 7.083 deg          : 18:07:20\n" +
        "       Plag HaMincha - 16.1 deg - Sunset             : 17:34:09\n" +
        "       Plag HaMincha - Baal HaTanya                  : 17:47:13\n" +
        "       Plag HaMincha - Rabbi Yaakov Moshe Hillel     : 17:49:53\n" +
        "       Plag HaMincha -  Rav Moshe Feinstein - GR'A half day: 17:46:48\n" +
        "     Candle Lighting - 18 Minutes                    : 18:40:24\n" +
        "     Candle Lighting - 15 Minutes                    : 18:43:24\n" +
        "     Candle Lighting - 20 Minutes                    : 18:38:24\n" +
        "     Candle Lighting - 21 Minutes                    : 18:37:24\n" +
        "     Candle Lighting - 22 Minutes                    : 18:36:24\n" +
        "     Candle Lighting - 30 Minutes                    : 18:28:24\n" +
        "     Candle Lighting - 40 Minutes                    : 18:18:24\n" +
        "               Shkia - Sunset                        : 18:58:24\n" +
        "               Shkia - Sunset (with elevation)       : 18:59:53\n" +
        "       Latest Mincha - Mishna Berura                 : 18:58:24\n" +
        "       Latest Mincha - Hacham Ovadia Yosef           : 19:11:50\n" +
        "       Latest Mincha - Hacham Ben Sion               : 19:24:24\n" +
        "     Bein Hashmashot - 13 Degrees                    : 20:12:20\n" +
        "     Bein Hashmashot - 58.5 Minutes                  : 19:56:54\n" +
        "     Bein Hashmashot - 13.5 Minutes                  : 19:21:56\n" +
        "     Bein Hashmashot - 2 Stars                       : 19:30:18\n" +
        "    Tzait Hacochavim - MG'A 120 Minutes              : 20:58:24\n" +
        "    Tzait Hacochavim - GR'A 120 Minutes Zmaniyot     : 20:57:54\n" +
        "    Tzait Hacochavim - 3.5 deg                       : 19:14:11\n" +
        "    Tzait Hacochavim - Baal HaTanya                  : 19:29:00\n" +
        "    Tzait Hacochavim - 6.5 deg                       : 19:31:58\n" +
        "    Tzait Hacochavim - 8.5 deg                       : 19:43:52\n" +
        "    Tzait Hacochavim - 16.1 deg                      : 20:29:48\n" +
        "    Tzait Hacochavim - 18 deg                        : 20:41:33\n" +
        "    Tzait Hacochavim - 19.8 deg                      : 20:52:51\n" +
        "    Tzait Hacochavim - 26 deg                        : 21:33:17\n" +
        "    Tzait Hacochavim - Chavas Yair and Divray Malkiel: 19:58:24\n" +
        "    Tzait Hacochavim - Rambam / Rabainu Tam          : 20:10:24\n" +
        "    Tzait Hacochavim - 72 Minutes Zmaniyot           : 20:10:06\n" +
        "    Tzait Hacochavim - MG'A 90 Minutes               : 20:28:24\n" +
        "    Tzait Hacochavim - GR'A 90 Minutes Zmaniyot      : 20:28:01\n" +
        "    Tzait Hacochavim - 96 Minutes                    : 20:34:24\n" +
        "    Tzait Hacochavim - GR'A 96 Minutes Zmaniyot      : 20:34:00\n" +
        "    Tzait Hacochavim - Chacham Yosef Harari-Raful    : 19:38:24\n" +
        "    Tzait Hacochavim - Geonim 3.65 deg               : 19:15:04\n" +
        "    Tzait Hacochavim - Geonim 4.37 deg               : 19:19:20\n" +
        "    Tzait Hacochavim - Geonim 4.61 deg               : 19:20:45\n" +
        "    Tzait Hacochavim - Geonim 4.8 deg                : 19:21:53\n" +
        "    Tzait Hacochavim - Geonim 5.88 deg               : 19:28:17\n" +
        "    Tzait Hacochavim - Geonim 5.95 deg               : 19:28:42\n" +
        "    Tzait Hacochavim - Geonim 7.083 deg              : 19:35:26\n" +
        "    Tzait Hacochavim - Geonim 8.5 deg                : 19:43:52\n" +
        "    Tzait Hacochavim - Rabbi Moshe Feinstein         : 19:48:24\n" +
        "    Tzait Hacochavim - Seattle Sephardic             : 19:43:24\n" +
        "            Midnight - Mid-night                     :  1:00:19\n" +
        "            Midnight - Fixed Local Mid-Night         :  1:09:04\n"
    );

  }

}
