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
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Hanetz              | 7:29:32 AM     | Fri Nov 25 07:14:32 PST 2016  | true              |\n" +
      "SofZmanShema        | 8:53:50 AM     | Fri Nov 25 08:38:50 PST 2016  | true              |\n" +
      "SofZmanTefila       | 10:27:18 AM    | Fri Nov 25 10:12:18 PST 2016  | true              |\n" +
      "Chatzot             | 11:56:10 AM    | Fri Nov 25 11:41:10 PST 2016  | true              |\n" +
      "MinchaGedola        | 12:18:24 PM    | Fri Nov 25 12:03:24 PST 2016  | true              |\n" +
      "MinchaKetana        | 2:31:43 PM     | Fri Nov 25 14:16:43 PST 2016  | true              |\n" +
      "PlagHaMincha        | 3:27:16 PM     | Fri Nov 25 15:12:16 PST 2016  | true              |\n" +
      "CandleLighting      | 4:04:49 PM     | Fri Nov 25 15:49:49 PST 2016  | true              |\n" +
      "Shkia               | 4:22:49 PM     | Fri Nov 25 16:07:49 PST 2016  | false             |\n" +
      "LatestMincha        | 4:32:49 PM     | Fri Nov 25 16:17:49 PST 2016  | false             |\n" +
      "BeinHashmashot      | 4:56:17 PM     | Fri Nov 25 16:41:17 PST 2016  | false             |\n" +
      "TzaitHacochavim     | 5:13:38 PM     | Fri Nov 25 16:58:38 PST 2016  | false             |\n" +
      "NightChatzot        | 11:56:50 PM    | Fri Nov 25 23:41:50 PST 2016  | false             |\n" +
      "Alot                | 5:52:34 AM     | Sat Nov 26 05:37:34 PST 2016  | false             |\n" +
      "Talit               | 6:29:10 AM     | Sat Nov 26 06:14:10 PST 2016  | false             |\n" +
      "Hanetz              | 7:30:52 AM     | Sat Nov 26 07:15:52 PST 2016  | false             |\n" +
      "SofZmanShema        | 8:54:34 AM     | Sat Nov 26 08:39:34 PST 2016  | false             |\n" +
      "SofZmanTefila       | 10:27:58 AM    | Sat Nov 26 10:12:58 PST 2016  | false             |\n" +
      "Chatzot             | 11:56:30 AM    | Sat Nov 26 11:41:30 PST 2016  | false             |\n" +
      "MinchaGedola        | 12:18:39 PM    | Sat Nov 26 12:03:39 PST 2016  | false             |\n" +
      "MinchaKetana        | 2:31:28 PM     | Sat Nov 26 14:16:28 PST 2016  | false             |\n" +
      "PlagHaMincha        | 3:26:48 PM     | Sat Nov 26 15:11:48 PST 2016  | false             |\n" +
      "Shkia               | 4:22:09 PM     | Sat Nov 26 16:07:09 PST 2016  | false             |\n" +
      "LatestMincha        | 4:32:07 PM     | Sat Nov 26 16:17:07 PST 2016  | false             |\n" +
      "BeinHashmashot      | 4:55:41 PM     | Sat Nov 26 16:40:41 PST 2016  | false             |\n" +
      "TzaitHacochavim     | 5:13:06 PM     | Sat Nov 26 16:58:06 PST 2016  | false             |\n" +
      "ShabbatEnd          | 5:07:09 PM     | Sat Nov 26 16:52:09 PST 2016  | false             |\n" +
      "NightChatzot        | 11:57:10 PM    | Sat Nov 26 23:42:10 PST 2016  | true              |\n" +
      "Alot                | 5:53:41 AM     | Sun Nov 27 05:38:41 PST 2016  | true              |\n" +
      "Talit               | 6:30:20 AM     | Sun Nov 27 06:15:20 PST 2016  | true              |\n" +
      "Hanetz              | 7:32:10 AM     | Sun Nov 27 07:17:10 PST 2016  | true              |\n" +
      "SofZmanShema        | 8:55:17 AM     | Sun Nov 27 08:40:17 PST 2016  | true              |\n" +
      "SofZmanTefila       | 10:28:38 AM    | Sun Nov 27 10:13:38 PST 2016  | true              |\n" +
      "Chatzot             | 11:56:51 AM    | Sun Nov 27 11:41:51 PST 2016  | true              |\n" +
      "MinchaGedola        | 12:18:54 PM    | Sun Nov 27 12:03:54 PST 2016  | true              |\n" +
      "MinchaKetana        | 2:31:15 PM     | Sun Nov 27 14:16:15 PST 2016  | true              |\n" +
      "PlagHaMincha        | 3:26:23 PM     | Sun Nov 27 15:11:23 PST 2016  | true              |\n" +
      "Shkia               | 4:21:32 PM     | Sun Nov 27 16:06:32 PST 2016  | true              |\n" +
      "LatestMincha        | 4:31:27 PM     | Sun Nov 27 16:16:27 PST 2016  | true              |\n" +
      "BeinHashmashot      | 4:55:07 PM     | Sun Nov 27 16:40:07 PST 2016  | true              |\n" +
      "TzaitHacochavim     | 5:12:36 PM     | Sun Nov 27 16:57:36 PST 2016  | true              |\n" +
      "NightChatzot        | 11:57:30 PM    | Sun Nov 27 23:42:30 PST 2016  | true              |\n"
    );
  }


  @Test
  public void yomKippur_5777() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.OCTOBER, 11, 6, 34 );
    runDays( testTime, 3, new NoShabbatZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Hanetz              | 7:23:18 AM     | Tue Oct 11 07:08:18 PDT 2016  | true              |\n" +
      "SofZmanShema        | 9:23:48 AM     | Tue Oct 11 09:08:48 PDT 2016  | true              |\n" +
      "SofZmanTefila       | 11:04:31 AM    | Tue Oct 11 10:49:31 PDT 2016  | true              |\n" +
      "Chatzot             | 12:55:08 PM    | Tue Oct 11 12:40:08 PDT 2016  | true              |\n" +
      "MinchaGedola        | 1:22:47 PM     | Tue Oct 11 13:07:47 PDT 2016  | true              |\n" +
      "MinchaKetana        | 4:08:42 PM     | Tue Oct 11 15:53:42 PDT 2016  | true              |\n" +
      "PlagHaMincha        | 5:17:50 PM     | Tue Oct 11 17:02:50 PDT 2016  | true              |\n" +
      "CandleLighting      | 6:08:58 PM     | Tue Oct 11 17:53:58 PDT 2016  | true              |\n" +
      "Shkia               | 6:26:58 PM     | Tue Oct 11 18:11:58 PDT 2016  | false             |\n" +
      "LatestMincha        | 6:39:25 PM     | Tue Oct 11 18:24:25 PDT 2016  | false             |\n" +
      "BeinHashmashot      | 6:58:22 PM     | Tue Oct 11 18:43:22 PDT 2016  | false             |\n" +
      "TzaitHacochavim     | 7:12:43 PM     | Tue Oct 11 18:57:43 PDT 2016  | false             |\n" +
      "NightChatzot        | 12:55:51 AM    | Wed Oct 12 00:40:51 PDT 2016  | false             |\n" +
      "Alot                | 5:53:51 AM     | Wed Oct 12 05:38:51 PDT 2016  | false             |\n" +
      "Talit               | 6:28:53 AM     | Wed Oct 12 06:13:53 PDT 2016  | false             |\n" +
      "Hanetz              | 7:24:44 AM     | Wed Oct 12 07:09:44 PDT 2016  | false             |\n" +
      "SofZmanShema        | 9:24:23 AM     | Wed Oct 12 09:09:23 PDT 2016  | false             |\n" +
      "SofZmanTefila       | 11:04:50 AM    | Wed Oct 12 10:49:50 PDT 2016  | false             |\n" +
      "Chatzot             | 12:54:54 PM    | Wed Oct 12 12:39:54 PDT 2016  | false             |\n" +
      "MinchaGedola        | 1:22:24 PM     | Wed Oct 12 13:07:24 PDT 2016  | false             |\n" +
      "MinchaKetana        | 4:07:29 PM     | Wed Oct 12 15:52:29 PDT 2016  | false             |\n" +
      "PlagHaMincha        | 5:16:16 PM     | Wed Oct 12 17:01:16 PDT 2016  | false             |\n" +
      "Shkia               | 6:25:03 PM     | Wed Oct 12 18:10:03 PDT 2016  | false             |\n" +
      "LatestMincha        | 6:37:26 PM     | Wed Oct 12 18:22:26 PDT 2016  | false             |\n" +
      "BeinHashmashot      | 6:56:27 PM     | Wed Oct 12 18:41:27 PDT 2016  | false             |\n" +
      "TzaitHacochavim     | 7:10:51 PM     | Wed Oct 12 18:55:51 PDT 2016  | false             |\n" +
      "ShabbatEnd          | 7:10:03 PM     | Wed Oct 12 18:55:03 PDT 2016  | false             |\n" +
      "NightChatzot        | 12:55:37 AM    | Thu Oct 13 00:40:37 PDT 2016  | true              |\n" +
      "Alot                | 5:55:15 AM     | Thu Oct 13 05:40:15 PDT 2016  | true              |\n" +
      "Talit               | 6:30:16 AM     | Thu Oct 13 06:15:16 PDT 2016  | true              |\n" +
      "Hanetz              | 7:26:10 AM     | Thu Oct 13 07:11:10 PDT 2016  | true              |\n" +
      "SofZmanShema        | 9:24:58 AM     | Thu Oct 13 09:09:58 PDT 2016  | true              |\n" +
      "SofZmanTefila       | 11:05:10 AM    | Thu Oct 13 10:50:10 PDT 2016  | true              |\n" +
      "Chatzot             | 12:54:40 PM    | Thu Oct 13 12:39:40 PDT 2016  | true              |\n" +
      "MinchaGedola        | 1:22:02 PM     | Thu Oct 13 13:07:02 PDT 2016  | true              |\n" +
      "MinchaKetana        | 4:06:17 PM     | Thu Oct 13 15:51:17 PDT 2016  | true              |\n" +
      "PlagHaMincha        | 5:14:43 PM     | Thu Oct 13 16:59:43 PDT 2016  | true              |\n" +
      "Shkia               | 6:23:09 PM     | Thu Oct 13 18:08:09 PDT 2016  | true              |\n" +
      "LatestMincha        | 6:35:28 PM     | Thu Oct 13 18:20:28 PDT 2016  | true              |\n" +
      "BeinHashmashot      | 6:54:33 PM     | Thu Oct 13 18:39:33 PDT 2016  | true              |\n" +
      "TzaitHacochavim     | 7:09:00 PM     | Thu Oct 13 18:54:00 PDT 2016  | true              |\n" +
      "NightChatzot        | 12:55:23 AM    | Fri Oct 14 00:40:23 PDT 2016  | true              |\n"
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
