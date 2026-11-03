/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 *
 */
public class UT_WakeupScheduler_NoShabbat
  extends WakeupSchedulerBase {

  @Test
  public void november_25_2016() {
    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.NOVEMBER, 25, 6, 34 );
    runDays( testTime, 3, new NoShabbatZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Hanetz              | 7:29:10 AM     | Fri Nov 25 07:14:10 PST 2016  | true              |\n"
        + "SofZmanShema        | 8:53:34 AM     | Fri Nov 25 08:38:34 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:27:07 AM    | Fri Nov 25 10:12:07 PST 2016  | true              |\n"
        + "Chatzot             | 11:56:17 AM    | Fri Nov 25 11:41:17 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:18:20 PM    | Fri Nov 25 12:03:20 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:31:48 PM     | Fri Nov 25 14:16:48 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:27:25 PM     | Fri Nov 25 15:12:25 PST 2016  | true              |\n"
        + "CandleLighting      | 4:05:02 PM     | Fri Nov 25 15:50:02 PST 2016  | true              |\n"
        + "Shkia               | 4:23:02 PM     | Fri Nov 25 16:08:02 PST 2016  | false             |\n"
        + "LatestMincha        | 4:33:02 PM     | Fri Nov 25 16:18:02 PST 2016  | false             |\n"
        + "BeinHashmashot      | 4:56:30 PM     | Fri Nov 25 16:41:30 PST 2016  | false             |\n"
        + "TzaitHacochavim     | 5:13:46 PM     | Fri Nov 25 16:58:46 PST 2016  | false             |\n"
        + "NightChatzot        | 11:56:27 PM    | Fri Nov 25 23:41:27 PST 2016  | false             |\n"
        + "Alot                | 5:52:12 AM     | Sat Nov 26 05:37:12 PST 2016  | false             |\n"
        + "Talit               | 6:28:48 AM     | Sat Nov 26 06:13:48 PST 2016  | false             |\n"
        + "Hanetz              | 7:30:30 AM     | Sat Nov 26 07:15:30 PST 2016  | false             |\n"
        + "SofZmanShema        | 8:54:18 AM     | Sat Nov 26 08:39:18 PST 2016  | false             |\n"
        + "SofZmanTefila       | 10:27:47 AM    | Sat Nov 26 10:12:47 PST 2016  | false             |\n"
        + "Chatzot             | 11:56:36 AM    | Sat Nov 26 11:41:36 PST 2016  | false             |\n"
        + "MinchaGedola        | 12:18:35 PM    | Sat Nov 26 12:03:35 PST 2016  | false             |\n"
        + "MinchaKetana        | 2:31:33 PM     | Sat Nov 26 14:16:33 PST 2016  | false             |\n"
        + "PlagHaMincha        | 3:26:57 PM     | Sat Nov 26 15:11:57 PST 2016  | false             |\n"
        + "Shkia               | 4:22:21 PM     | Sat Nov 26 16:07:21 PST 2016  | false             |\n"
        + "LatestMincha        | 4:32:19 PM     | Sat Nov 26 16:17:19 PST 2016  | false             |\n"
        + "BeinHashmashot      | 4:55:53 PM     | Sat Nov 26 16:40:53 PST 2016  | false             |\n"
        + "TzaitHacochavim     | 5:13:13 PM     | Sat Nov 26 16:58:13 PST 2016  | false             |\n"
        + "ShabbatEnd          | 5:07:21 PM     | Sat Nov 26 16:52:21 PST 2016  | false             |\n"
        + "NightChatzot        | 11:56:46 PM    | Sat Nov 26 23:41:46 PST 2016  | true              |\n"
        + "Alot                | 5:53:19 AM     | Sun Nov 27 05:38:19 PST 2016  | true              |\n"
        + "Talit               | 6:29:59 AM     | Sun Nov 27 06:14:59 PST 2016  | true              |\n"
        + "Hanetz              | 7:31:49 AM     | Sun Nov 27 07:16:49 PST 2016  | true              |\n"
        + "SofZmanShema        | 8:55:02 AM     | Sun Nov 27 08:40:02 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:28:27 AM    | Sun Nov 27 10:13:27 PST 2016  | true              |\n"
        + "Chatzot             | 11:56:57 AM    | Sun Nov 27 11:41:57 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:18:51 PM    | Sun Nov 27 12:03:51 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:31:19 PM     | Sun Nov 27 14:16:19 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:26:31 PM     | Sun Nov 27 15:11:31 PST 2016  | true              |\n"
        + "Shkia               | 4:21:43 PM     | Sun Nov 27 16:06:43 PST 2016  | true              |\n"
        + "LatestMincha        | 4:31:39 PM     | Sun Nov 27 16:16:39 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:55:19 PM     | Sun Nov 27 16:40:19 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:12:43 PM     | Sun Nov 27 16:57:43 PST 2016  | true              |\n"
        + "NightChatzot        | 11:57:07 PM    | Sun Nov 27 23:42:07 PST 2016  | true              |\n"
    );
  }


  /**
   * Chol HaMoed Pesach is neither Shabbat nor Yom Tov, so notifications should continue as normal
   *  on those days even though Shabbat notifications are disabled:
   *  <ul>
   *    <li>Thu Apr 13 (Chol HaMoed): alerts all day.
   *    <li>Fri Apr 14 (Chol HaMoed, erev Shabbat): alerts until candle lighting, then suppressed
   *      because tomorrow is Shabbat.
   *    <li>Sat Apr 15 (Shabbat Chol HaMoed): suppressed all day because today IS Shabbat.
   *    <li>Sun Apr 16 (Chol HaMoed): alerts resume once Shabbat ends, continue until candle
   *      lighting, then suppressed because tomorrow is the 7th day of Pesach (Yom Tov).
   *    <li>Mon Apr 17 (7th day of Pesach, Yom Tov): suppressed all day because today IS Yom Tov.
   *  </ul>
   */
  @Test
  public void pesach_cholHamoed_2017() {
    Calendar testTime = UT_ZmanUtils.configureTestTime( 2017, Calendar.APRIL, 13, 6, 34 );
    runDays( testTime, 5, new NoShabbatZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "SofZmanShema        | 8:57:02 AM     | Thu Apr 13 08:42:02 PDT 2017  | true              |\n"
        + "SofZmanTefila       | 10:54:26 AM    | Thu Apr 13 10:39:26 PDT 2017  | true              |\n"
        + "Chatzot             | 1:09:27 PM     | Thu Apr 13 12:54:27 PDT 2017  | true              |\n"
        + "MinchaGedola        | 1:43:47 PM     | Thu Apr 13 13:28:47 PDT 2017  | true              |\n"
        + "MinchaKetana        | 5:07:01 PM     | Thu Apr 13 16:52:01 PDT 2017  | true              |\n"
        + "PlagHaMincha        | 6:31:42 PM     | Thu Apr 13 18:16:42 PDT 2017  | true              |\n"
        + "Shkia               | 7:56:22 PM     | Thu Apr 13 19:41:22 PDT 2017  | true              |\n"
        + "LatestMincha        | 8:11:37 PM     | Thu Apr 13 19:56:37 PDT 2017  | true              |\n"
        + "BeinHashmashot      | 8:31:40 PM     | Thu Apr 13 20:16:40 PDT 2017  | true              |\n"
        + "TzaitHacochavim     | 8:44:42 PM     | Thu Apr 13 20:29:42 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:19 AM     | Fri Apr 14 00:54:19 PDT 2017  | true              |\n"
        + "Alot                | 4:41:30 AM     | Fri Apr 14 04:26:30 PDT 2017  | true              |\n"
        + "Talit               | 5:22:08 AM     | Fri Apr 14 05:07:08 PDT 2017  | true              |\n"
        + "Hanetz              | 6:21:33 AM     | Fri Apr 14 06:06:33 PDT 2017  | true              |\n"
        + "SofZmanShema        | 8:55:44 AM     | Fri Apr 14 08:40:44 PDT 2017  | true              |\n"
        + "SofZmanTefila       | 10:53:38 AM    | Fri Apr 14 10:38:38 PDT 2017  | true              |\n"
        + "Chatzot             | 1:09:12 PM     | Fri Apr 14 12:54:12 PDT 2017  | true              |\n"
        + "MinchaGedola        | 1:43:41 PM     | Fri Apr 14 13:28:41 PDT 2017  | true              |\n"
        + "MinchaKetana        | 5:07:44 PM     | Fri Apr 14 16:52:44 PDT 2017  | true              |\n"
        + "PlagHaMincha        | 6:32:46 PM     | Fri Apr 14 18:17:46 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:39:47 PM     | Fri Apr 14 19:24:47 PDT 2017  | true              |\n"
        + "Shkia               | 7:57:47 PM     | Fri Apr 14 19:42:47 PDT 2017  | false             |\n"
        + "LatestMincha        | 8:13:05 PM     | Fri Apr 14 19:58:05 PDT 2017  | false             |\n"
        + "BeinHashmashot      | 8:33:17 PM     | Fri Apr 14 20:18:17 PDT 2017  | false             |\n"
        + "TzaitHacochavim     | 8:46:17 PM     | Fri Apr 14 20:31:17 PDT 2017  | false             |\n"
        + "NightChatzot        | 1:09:05 AM     | Sat Apr 15 00:54:05 PDT 2017  | false             |\n"
        + "Alot                | 4:39:07 AM     | Sat Apr 15 04:24:07 PDT 2017  | false             |\n"
        + "Talit               | 5:20:01 AM     | Sat Apr 15 05:05:01 PDT 2017  | false             |\n"
        + "Hanetz              | 6:19:40 AM     | Sat Apr 15 06:04:40 PDT 2017  | false             |\n"
        + "SofZmanShema        | 8:54:25 AM     | Sat Apr 15 08:39:25 PDT 2017  | false             |\n"
        + "SofZmanTefila       | 10:52:50 AM    | Sat Apr 15 10:37:50 PDT 2017  | false             |\n"
        + "Chatzot             | 1:08:58 PM     | Sat Apr 15 12:53:58 PDT 2017  | false             |\n"
        + "MinchaGedola        | 1:43:35 PM     | Sat Apr 15 13:28:35 PDT 2017  | false             |\n"
        + "MinchaKetana        | 5:08:28 PM     | Sat Apr 15 16:53:28 PDT 2017  | false             |\n"
        + "PlagHaMincha        | 6:33:50 PM     | Sat Apr 15 18:18:50 PDT 2017  | false             |\n"
        + "Shkia               | 7:59:12 PM     | Sat Apr 15 19:44:12 PDT 2017  | false             |\n"
        + "LatestMincha        | 8:14:34 PM     | Sat Apr 15 19:59:34 PDT 2017  | false             |\n"
        + "BeinHashmashot      | 8:34:55 PM     | Sat Apr 15 20:19:55 PDT 2017  | false             |\n"
        + "TzaitHacochavim     | 8:47:52 PM     | Sat Apr 15 20:32:52 PDT 2017  | false             |\n"
        + "ShabbatEnd          | 8:44:12 PM     | Sat Apr 15 20:29:12 PDT 2017  | false             |\n"
        + "NightChatzot        | 1:08:51 AM     | Sun Apr 16 00:53:51 PDT 2017  | true              |\n"
        + "Alot                | 4:36:43 AM     | Sun Apr 16 04:21:43 PDT 2017  | true              |\n"
        + "Talit               | 5:17:54 AM     | Sun Apr 16 05:02:54 PDT 2017  | true              |\n"
        + "Hanetz              | 6:17:47 AM     | Sun Apr 16 06:02:47 PDT 2017  | true              |\n"
        + "SofZmanShema        | 8:53:07 AM     | Sun Apr 16 08:38:07 PDT 2017  | true              |\n"
        + "SofZmanTefila       | 10:52:03 AM    | Sun Apr 16 10:37:03 PDT 2017  | true              |\n"
        + "Chatzot             | 1:08:44 PM     | Sun Apr 16 12:53:44 PDT 2017  | true              |\n"
        + "MinchaGedola        | 1:43:29 PM     | Sun Apr 16 13:28:29 PDT 2017  | true              |\n"
        + "MinchaKetana        | 5:09:11 PM     | Sun Apr 16 16:54:11 PDT 2017  | true              |\n"
        + "PlagHaMincha        | 6:34:54 PM     | Sun Apr 16 18:19:54 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:42:36 PM     | Sun Apr 16 19:27:36 PDT 2017  | true              |\n"
        + "Shkia               | 8:00:36 PM     | Sun Apr 16 19:45:36 PDT 2017  | false             |\n"
        + "LatestMincha        | 8:16:02 PM     | Sun Apr 16 20:01:02 PDT 2017  | false             |\n"
        + "BeinHashmashot      | 8:36:32 PM     | Sun Apr 16 20:21:32 PDT 2017  | false             |\n"
        + "TzaitHacochavim     | 8:49:28 PM     | Sun Apr 16 20:34:28 PDT 2017  | false             |\n"
        + "NightChatzot        | 1:08:37 AM     | Mon Apr 17 00:53:37 PDT 2017  | false             |\n"
        + "Alot                | 4:34:20 AM     | Mon Apr 17 04:19:20 PDT 2017  | false             |\n"
        + "Talit               | 5:15:47 AM     | Mon Apr 17 05:00:47 PDT 2017  | false             |\n"
        + "Hanetz              | 6:15:55 AM     | Mon Apr 17 06:00:55 PDT 2017  | false             |\n"
        + "SofZmanShema        | 8:51:48 AM     | Mon Apr 17 08:36:48 PDT 2017  | false             |\n"
        + "SofZmanTefila       | 10:51:17 AM    | Mon Apr 17 10:36:17 PDT 2017  | false             |\n"
        + "Chatzot             | 1:08:30 PM     | Mon Apr 17 12:53:30 PDT 2017  | false             |\n"
        + "MinchaGedola        | 1:43:23 PM     | Mon Apr 17 13:28:23 PDT 2017  | false             |\n"
        + "MinchaKetana        | 5:09:55 PM     | Mon Apr 17 16:54:55 PDT 2017  | false             |\n"
        + "PlagHaMincha        | 6:35:58 PM     | Mon Apr 17 18:20:58 PDT 2017  | false             |\n"
        + "Shkia               | 8:02:01 PM     | Mon Apr 17 19:47:01 PDT 2017  | false             |\n"
        + "LatestMincha        | 8:17:30 PM     | Mon Apr 17 20:02:30 PDT 2017  | false             |\n"
        + "BeinHashmashot      | 8:38:11 PM     | Mon Apr 17 20:23:11 PDT 2017  | false             |\n"
        + "TzaitHacochavim     | 8:51:04 PM     | Mon Apr 17 20:36:04 PDT 2017  | false             |\n"
        + "YomTovSheniCandleLighting| 8:51:04 PM     | Mon Apr 17 20:36:04 PDT 2017  | false             |\n"
        + "NightChatzot        | 1:08:23 AM     | Tue Apr 18 00:53:23 PDT 2017  | false             |\n"
    );
  }


  @Test
  public void yomKippur_5777() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.OCTOBER, 11, 6, 34 );
    runDays( testTime, 3, new NoShabbatZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Hanetz              | 7:22:57 AM     | Tue Oct 11 07:07:57 PDT 2016  | true              |\n"
        + "SofZmanShema        | 9:23:34 AM     | Tue Oct 11 09:08:34 PDT 2016  | true              |\n"
        + "SofZmanTefila       | 11:04:27 AM    | Tue Oct 11 10:49:27 PDT 2016  | true              |\n"
        + "Chatzot             | 12:55:36 PM    | Tue Oct 11 12:40:36 PDT 2016  | true              |\n"
        + "MinchaGedola        | 1:22:54 PM     | Tue Oct 11 13:07:54 PDT 2016  | true              |\n"
        + "MinchaKetana        | 4:09:01 PM     | Tue Oct 11 15:54:01 PDT 2016  | true              |\n"
        + "PlagHaMincha        | 5:18:15 PM     | Tue Oct 11 17:03:15 PDT 2016  | true              |\n"
        + "CandleLighting      | 6:09:28 PM     | Tue Oct 11 17:54:28 PDT 2016  | true              |\n"
        + "Shkia               | 6:27:28 PM     | Tue Oct 11 18:12:28 PDT 2016  | false             |\n"
        + "LatestMincha        | 6:39:55 PM     | Tue Oct 11 18:24:55 PDT 2016  | false             |\n"
        + "BeinHashmashot      | 6:58:54 PM     | Tue Oct 11 18:43:54 PDT 2016  | false             |\n"
        + "TzaitHacochavim     | 7:13:08 PM     | Tue Oct 11 18:58:08 PDT 2016  | false             |\n"
        + "NightChatzot        | 12:55:28 AM    | Wed Oct 12 00:40:28 PDT 2016  | false             |\n"
        + "Alot                | 5:53:25 AM     | Wed Oct 12 05:38:25 PDT 2016  | false             |\n"
        + "Talit               | 6:28:29 AM     | Wed Oct 12 06:13:29 PDT 2016  | false             |\n"
        + "Hanetz              | 7:24:23 AM     | Wed Oct 12 07:09:23 PDT 2016  | false             |\n"
        + "SofZmanShema        | 9:24:09 AM     | Wed Oct 12 09:09:09 PDT 2016  | false             |\n"
        + "SofZmanTefila       | 11:04:46 AM    | Wed Oct 12 10:49:46 PDT 2016  | false             |\n"
        + "Chatzot             | 12:55:21 PM    | Wed Oct 12 12:40:21 PDT 2016  | false             |\n"
        + "MinchaGedola        | 1:22:31 PM     | Wed Oct 12 13:07:31 PDT 2016  | false             |\n"
        + "MinchaKetana        | 4:07:48 PM     | Wed Oct 12 15:52:48 PDT 2016  | false             |\n"
        + "PlagHaMincha        | 5:16:40 PM     | Wed Oct 12 17:01:40 PDT 2016  | false             |\n"
        + "Shkia               | 6:25:33 PM     | Wed Oct 12 18:10:33 PDT 2016  | false             |\n"
        + "LatestMincha        | 6:37:56 PM     | Wed Oct 12 18:22:56 PDT 2016  | false             |\n"
        + "BeinHashmashot      | 6:56:58 PM     | Wed Oct 12 18:41:58 PDT 2016  | false             |\n"
        + "TzaitHacochavim     | 7:11:16 PM     | Wed Oct 12 18:56:16 PDT 2016  | false             |\n"
        + "ShabbatEnd          | 7:10:33 PM     | Wed Oct 12 18:55:33 PDT 2016  | false             |\n"
        + "NightChatzot        | 12:55:13 AM    | Thu Oct 13 00:40:13 PDT 2016  | true              |\n"
        + "Alot                | 5:54:49 AM     | Thu Oct 13 05:39:49 PDT 2016  | true              |\n"
        + "Talit               | 6:29:52 AM     | Thu Oct 13 06:14:52 PDT 2016  | true              |\n"
        + "Hanetz              | 7:25:49 AM     | Thu Oct 13 07:10:49 PDT 2016  | true              |\n"
        + "SofZmanShema        | 9:24:43 AM     | Thu Oct 13 09:09:43 PDT 2016  | true              |\n"
        + "SofZmanTefila       | 11:05:05 AM    | Thu Oct 13 10:50:05 PDT 2016  | true              |\n"
        + "Chatzot             | 12:55:06 PM    | Thu Oct 13 12:40:06 PDT 2016  | true              |\n"
        + "MinchaGedola        | 1:22:08 PM     | Thu Oct 13 13:07:08 PDT 2016  | true              |\n"
        + "MinchaKetana        | 4:06:35 PM     | Thu Oct 13 15:51:35 PDT 2016  | true              |\n"
        + "PlagHaMincha        | 5:15:07 PM     | Thu Oct 13 17:00:07 PDT 2016  | true              |\n"
        + "Shkia               | 6:23:38 PM     | Thu Oct 13 18:08:38 PDT 2016  | true              |\n"
        + "LatestMincha        | 6:35:58 PM     | Thu Oct 13 18:20:58 PDT 2016  | true              |\n"
        + "BeinHashmashot      | 6:55:04 PM     | Thu Oct 13 18:40:04 PDT 2016  | true              |\n"
        + "TzaitHacochavim     | 7:09:25 PM     | Thu Oct 13 18:54:25 PDT 2016  | true              |\n"
        + "NightChatzot        | 12:54:59 AM    | Fri Oct 14 00:39:59 PDT 2016  | true              |\n"
    );
  }



  private static class NoShabbatZmanCallback
    extends UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback {

    @Override
    public boolean shouldNotifyOnShabbat() {
      return false;
    }
  }


} // End of UT_WakeupScheduler_NoShabbat class
