/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib;

import org.junit.Test;

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
        + "Hanetz              | 7:29:11 AM     | Fri Nov 25 07:14:11 PST 2016  | true              |\n"
        + "SofZmanShema        | 8:53:34 AM     | Fri Nov 25 08:38:34 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:27:07 AM    | Fri Nov 25 10:12:07 PST 2016  | true              |\n"
        + "Chatzot             | 11:56:08 AM    | Fri Nov 25 11:41:08 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:18:20 PM    | Fri Nov 25 12:03:20 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:31:48 PM     | Fri Nov 25 14:16:48 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:27:24 PM     | Fri Nov 25 15:12:24 PST 2016  | true              |\n"
        + "CandleLighting      | 4:05:00 PM     | Fri Nov 25 15:50:00 PST 2016  | true              |\n"
        + "Shkia               | 4:23:00 PM     | Fri Nov 25 16:08:00 PST 2016  | false             |\n"
        + "LatestMincha        | 4:33:01 PM     | Fri Nov 25 16:18:01 PST 2016  | false             |\n"
        + "BeinHashmashot      | 4:56:29 PM     | Fri Nov 25 16:41:29 PST 2016  | false             |\n"
        + "TzaitHacochavim     | 5:13:46 PM     | Fri Nov 25 16:58:46 PST 2016  | false             |\n"
        + "NightChatzot        | 11:56:17 PM    | Fri Nov 25 23:41:17 PST 2016  | false             |\n"
        + "Alot                | 5:52:12 AM     | Sat Nov 26 05:37:12 PST 2016  | false             |\n"
        + "Talit               | 6:28:48 AM     | Sat Nov 26 06:13:48 PST 2016  | false             |\n"
        + "Hanetz              | 7:30:31 AM     | Sat Nov 26 07:15:31 PST 2016  | false             |\n"
        + "SofZmanShema        | 8:54:18 AM     | Sat Nov 26 08:39:18 PST 2016  | false             |\n"
        + "SofZmanTefila       | 10:27:47 AM    | Sat Nov 26 10:12:47 PST 2016  | false             |\n"
        + "Chatzot             | 11:56:27 AM    | Sat Nov 26 11:41:27 PST 2016  | false             |\n"
        + "MinchaGedola        | 12:18:35 PM    | Sat Nov 26 12:03:35 PST 2016  | false             |\n"
        + "MinchaKetana        | 2:31:32 PM     | Sat Nov 26 14:16:32 PST 2016  | false             |\n"
        + "PlagHaMincha        | 3:26:56 PM     | Sat Nov 26 15:11:56 PST 2016  | false             |\n"
        + "Shkia               | 4:22:20 PM     | Sat Nov 26 16:07:20 PST 2016  | false             |\n"
        + "LatestMincha        | 4:32:18 PM     | Sat Nov 26 16:17:18 PST 2016  | false             |\n"
        + "BeinHashmashot      | 4:55:52 PM     | Sat Nov 26 16:40:52 PST 2016  | false             |\n"
        + "TzaitHacochavim     | 5:13:13 PM     | Sat Nov 26 16:58:13 PST 2016  | false             |\n"
        + "ShabbatEnd          | 5:07:20 PM     | Sat Nov 26 16:52:20 PST 2016  | false             |\n"
        + "NightChatzot        | 11:56:37 PM    | Sat Nov 26 23:41:37 PST 2016  | true              |\n"
        + "Alot                | 5:53:19 AM     | Sun Nov 27 05:38:19 PST 2016  | true              |\n"
        + "Talit               | 6:29:59 AM     | Sun Nov 27 06:14:59 PST 2016  | true              |\n"
        + "Hanetz              | 7:31:50 AM     | Sun Nov 27 07:16:50 PST 2016  | true              |\n"
        + "SofZmanShema        | 8:55:02 AM     | Sun Nov 27 08:40:02 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:28:27 AM    | Sun Nov 27 10:13:27 PST 2016  | true              |\n"
        + "Chatzot             | 11:56:46 AM    | Sun Nov 27 11:41:46 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:18:51 PM    | Sun Nov 27 12:03:51 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:31:18 PM     | Sun Nov 27 14:16:18 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:26:30 PM     | Sun Nov 27 15:11:30 PST 2016  | true              |\n"
        + "Shkia               | 4:21:42 PM     | Sun Nov 27 16:06:42 PST 2016  | true              |\n"
        + "LatestMincha        | 4:31:38 PM     | Sun Nov 27 16:16:38 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:55:18 PM     | Sun Nov 27 16:40:18 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:12:43 PM     | Sun Nov 27 16:57:43 PST 2016  | true              |\n"
        + "NightChatzot        | 11:56:57 PM    | Sun Nov 27 23:41:57 PST 2016  | true              |\n"
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
        + "Chatzot             | 12:55:43 PM    | Tue Oct 11 12:40:43 PDT 2016  | true              |\n"
        + "MinchaGedola        | 1:22:54 PM     | Tue Oct 11 13:07:54 PDT 2016  | true              |\n"
        + "MinchaKetana        | 4:09:01 PM     | Tue Oct 11 15:54:01 PDT 2016  | true              |\n"
        + "PlagHaMincha        | 5:18:14 PM     | Tue Oct 11 17:03:14 PDT 2016  | true              |\n"
        + "CandleLighting      | 6:09:28 PM     | Tue Oct 11 17:54:28 PDT 2016  | true              |\n"
        + "Shkia               | 6:27:28 PM     | Tue Oct 11 18:12:28 PDT 2016  | false             |\n"
        + "LatestMincha        | 6:39:55 PM     | Tue Oct 11 18:24:55 PDT 2016  | false             |\n"
        + "BeinHashmashot      | 6:58:54 PM     | Tue Oct 11 18:43:54 PDT 2016  | false             |\n"
        + "TzaitHacochavim     | 7:13:08 PM     | Tue Oct 11 18:58:08 PDT 2016  | false             |\n"
        + "NightChatzot        | 12:55:36 AM    | Wed Oct 12 00:40:36 PDT 2016  | false             |\n"
        + "Alot                | 5:53:25 AM     | Wed Oct 12 05:38:25 PDT 2016  | false             |\n"
        + "Talit               | 6:28:29 AM     | Wed Oct 12 06:13:29 PDT 2016  | false             |\n"
        + "Hanetz              | 7:24:23 AM     | Wed Oct 12 07:09:23 PDT 2016  | false             |\n"
        + "SofZmanShema        | 9:24:09 AM     | Wed Oct 12 09:09:09 PDT 2016  | false             |\n"
        + "SofZmanTefila       | 11:04:46 AM    | Wed Oct 12 10:49:46 PDT 2016  | false             |\n"
        + "Chatzot             | 12:55:28 PM    | Wed Oct 12 12:40:28 PDT 2016  | false             |\n"
        + "MinchaGedola        | 1:22:31 PM     | Wed Oct 12 13:07:31 PDT 2016  | false             |\n"
        + "MinchaKetana        | 4:07:48 PM     | Wed Oct 12 15:52:48 PDT 2016  | false             |\n"
        + "PlagHaMincha        | 5:16:40 PM     | Wed Oct 12 17:01:40 PDT 2016  | false             |\n"
        + "Shkia               | 6:25:33 PM     | Wed Oct 12 18:10:33 PDT 2016  | false             |\n"
        + "LatestMincha        | 6:37:56 PM     | Wed Oct 12 18:22:56 PDT 2016  | false             |\n"
        + "BeinHashmashot      | 6:56:58 PM     | Wed Oct 12 18:41:58 PDT 2016  | false             |\n"
        + "TzaitHacochavim     | 7:11:16 PM     | Wed Oct 12 18:56:16 PDT 2016  | false             |\n"
        + "ShabbatEnd          | 7:10:33 PM     | Wed Oct 12 18:55:33 PDT 2016  | false             |\n"
        + "NightChatzot        | 12:55:21 AM    | Thu Oct 13 00:40:21 PDT 2016  | true              |\n"
        + "Alot                | 5:54:49 AM     | Thu Oct 13 05:39:49 PDT 2016  | true              |\n"
        + "Talit               | 6:29:52 AM     | Thu Oct 13 06:14:52 PDT 2016  | true              |\n"
        + "Hanetz              | 7:25:49 AM     | Thu Oct 13 07:10:49 PDT 2016  | true              |\n"
        + "SofZmanShema        | 9:24:43 AM     | Thu Oct 13 09:09:43 PDT 2016  | true              |\n"
        + "SofZmanTefila       | 11:05:05 AM    | Thu Oct 13 10:50:05 PDT 2016  | true              |\n"
        + "Chatzot             | 12:55:13 PM    | Thu Oct 13 12:40:13 PDT 2016  | true              |\n"
        + "MinchaGedola        | 1:22:08 PM     | Thu Oct 13 13:07:08 PDT 2016  | true              |\n"
        + "MinchaKetana        | 4:06:35 PM     | Thu Oct 13 15:51:35 PDT 2016  | true              |\n"
        + "PlagHaMincha        | 5:15:07 PM     | Thu Oct 13 17:00:07 PDT 2016  | true              |\n"
        + "Shkia               | 6:23:38 PM     | Thu Oct 13 18:08:38 PDT 2016  | true              |\n"
        + "LatestMincha        | 6:35:58 PM     | Thu Oct 13 18:20:58 PDT 2016  | true              |\n"
        + "BeinHashmashot      | 6:55:04 PM     | Thu Oct 13 18:40:04 PDT 2016  | true              |\n"
        + "TzaitHacochavim     | 7:09:25 PM     | Thu Oct 13 18:54:25 PDT 2016  | true              |\n"
        + "NightChatzot        | 12:55:06 AM    | Fri Oct 14 00:40:06 PDT 2016  | true              |\n"
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
