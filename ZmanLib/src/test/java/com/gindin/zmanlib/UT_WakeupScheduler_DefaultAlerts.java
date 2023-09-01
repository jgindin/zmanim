/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.notification.NotificationDefaults;
import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Test;

import java.util.*;

/**
 *
 */
@SuppressWarnings( { "Duplicates", "MagicNumber" } )
public class UT_WakeupScheduler_DefaultAlerts
  extends WakeupSchedulerBase {


  @Test
  public void december_7_2014_15_minute_alerts() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Talit               | 6:40:05 AM     | Sun Dec 07 06:34:00 PST 2014  | true              |\n"
        + "Hanetz              | 7:43:08 AM     | Sun Dec 07 07:28:08 PST 2014  | true              |\n"
        + "SofZmanShema        | 9:01:46 AM     | Sun Dec 07 08:46:46 PST 2014  | true              |\n"
        + "SofZmanTefila       | 10:34:44 AM    | Sun Dec 07 10:19:44 PST 2014  | true              |\n"
        + "Chatzot             | 12:00:26 PM    | Sun Dec 07 11:45:26 PST 2014  | true              |\n"
        + "MinchaGedola        | 12:22:00 PM    | Sun Dec 07 12:07:00 PST 2014  | true              |\n"
        + "MinchaKetana        | 2:30:42 PM     | Sun Dec 07 14:15:42 PST 2014  | true              |\n"
        + "PlagHaMincha        | 3:24:20 PM     | Sun Dec 07 15:09:20 PST 2014  | true              |\n"
        + "Shkia               | 4:17:58 PM     | Sun Dec 07 16:02:58 PST 2014  | true              |\n"
        + "LatestMincha        | 4:27:37 PM     | Sun Dec 07 16:12:37 PST 2014  | true              |\n"
        + "BeinHashmashot      | 4:52:04 PM     | Sun Dec 07 16:37:04 PST 2014  | true              |\n"
        + "TzaitHacochavim     | 5:10:00 PM     | Sun Dec 07 16:55:00 PST 2014  | true              |\n"
        + "NightChatzot        | 12:00:39 AM    | Sun Dec 07 23:45:39 PST 2014  | true              |\n"
    );
  }


  @Test
  public void dec7_10_2016() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, 3, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Talit               | 6:40:34 AM     | Wed Dec 07 06:34:00 PST 2016  | true              |\n"
        + "Hanetz              | 7:43:40 AM     | Wed Dec 07 07:28:40 PST 2016  | true              |\n"
        + "SofZmanShema        | 9:02:07 AM     | Wed Dec 07 08:47:07 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:35:04 AM    | Wed Dec 07 10:20:04 PST 2016  | true              |\n"
        + "Chatzot             | 12:00:39 PM    | Wed Dec 07 11:45:39 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:22:12 PM    | Wed Dec 07 12:07:12 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:30:45 PM     | Wed Dec 07 14:15:45 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:24:19 PM     | Wed Dec 07 15:09:19 PST 2016  | true              |\n"
        + "Shkia               | 4:17:53 PM     | Wed Dec 07 16:02:53 PST 2016  | true              |\n"
        + "LatestMincha        | 4:27:31 PM     | Wed Dec 07 16:12:31 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:52:00 PM     | Wed Dec 07 16:37:00 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:09:58 PM     | Wed Dec 07 16:54:58 PST 2016  | true              |\n"
        + "NightChatzot        | 12:00:52 AM    | Wed Dec 07 23:45:52 PST 2016  | true              |\n"
        + "Alot                | 6:04:21 AM     | Thu Dec 08 05:49:21 PST 2016  | true              |\n"
        + "Talit               | 6:41:30 AM     | Thu Dec 08 06:26:30 PST 2016  | true              |\n"
        + "Hanetz              | 7:44:41 AM     | Thu Dec 08 07:29:41 PST 2016  | true              |\n"
        + "SofZmanShema        | 9:02:47 AM     | Thu Dec 08 08:47:47 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:35:42 AM    | Thu Dec 08 10:20:42 PST 2016  | true              |\n"
        + "Chatzot             | 12:01:06 PM    | Thu Dec 08 11:46:06 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:22:36 PM    | Thu Dec 08 12:07:36 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:30:52 PM     | Thu Dec 08 14:15:52 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:24:19 PM     | Thu Dec 08 15:09:19 PST 2016  | true              |\n"
        + "Shkia               | 4:17:45 PM     | Thu Dec 08 16:02:45 PST 2016  | true              |\n"
        + "LatestMincha        | 4:27:22 PM     | Thu Dec 08 16:12:22 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:51:55 PM     | Thu Dec 08 16:36:55 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:09:55 PM     | Thu Dec 08 16:54:55 PST 2016  | true              |\n"
        + "NightChatzot        | 12:01:19 AM    | Thu Dec 08 23:46:19 PST 2016  | true              |\n"
        + "Alot                | 6:05:13 AM     | Fri Dec 09 05:50:13 PST 2016  | true              |\n"
        + "Talit               | 6:42:24 AM     | Fri Dec 09 06:27:24 PST 2016  | true              |\n"
        + "Hanetz              | 7:45:40 AM     | Fri Dec 09 07:30:40 PST 2016  | true              |\n"
        + "SofZmanShema        | 9:03:27 AM     | Fri Dec 09 08:48:27 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:36:20 AM    | Fri Dec 09 10:21:20 PST 2016  | true              |\n"
        + "Chatzot             | 12:01:32 PM    | Fri Dec 09 11:46:32 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:23:01 PM    | Fri Dec 09 12:08:01 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:31:01 PM     | Fri Dec 09 14:16:01 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:24:21 PM     | Fri Dec 09 15:09:21 PST 2016  | true              |\n"
        + "CandleLighting      | 3:59:41 PM     | Fri Dec 09 15:44:41 PST 2016  | true              |\n"
        + "Shkia               | 4:17:41 PM     | Fri Dec 09 16:02:41 PST 2016  | true              |\n"
        + "LatestMincha        | 4:27:17 PM     | Fri Dec 09 16:12:17 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:51:53 PM     | Fri Dec 09 16:36:53 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:09:55 PM     | Fri Dec 09 16:54:55 PST 2016  | true              |\n"
        + "NightChatzot        | 12:01:46 AM    | Fri Dec 09 23:46:46 PST 2016  | true              |\n"
    );
  }


  @Test
  public void december_7_2014_default_product_alerts() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, new DefaultAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
        "--------------------|----------------|-------------------------------|-------------------|\n" +
        "Talit               | 6:40:05 AM     | Sun Dec 07 06:34:00 PST 2014  | true              |\n" +
        "SofZmanShema        | 9:01:46 AM     | Sun Dec 07 08:46:46 PST 2014  | true              |\n" +
        "SofZmanTefila       | 10:34:44 AM    | Sun Dec 07 10:19:44 PST 2014  | true              |\n" +
        "PlagHaMincha        | 3:24:20 PM     | Sun Dec 07 15:09:20 PST 2014  | true              |\n" +
        "Shkia               | 4:17:58 PM     | Sun Dec 07 16:02:58 PST 2014  | true              |\n" +
        "LatestMincha        | 4:27:37 PM     | Sun Dec 07 16:12:37 PST 2014  | true              |\n"
    );
  }


  @Test
  public void december_21_2015_1_week() {

    december_21_2015_1_week( new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Alot                | 6:13:01 AM     | Mon Dec 21 05:58:01 PST 2015  | true              |\n"
        + "Talit               | 6:50:23 AM     | Mon Dec 21 06:35:23 PST 2015  | true              |\n"
        + "Hanetz              | 7:54:09 AM     | Mon Dec 21 07:39:09 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:10:05 AM     | Mon Dec 21 08:55:05 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:42:50 AM    | Mon Dec 21 10:27:50 PST 2015  | true              |\n"
        + "Chatzot             | 12:06:55 PM    | Mon Dec 21 11:51:55 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:28:15 PM    | Mon Dec 21 12:13:15 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:34:45 PM     | Mon Dec 21 14:19:45 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:27:28 PM     | Mon Dec 21 15:12:28 PST 2015  | true              |\n"
        + "Shkia               | 4:20:10 PM     | Mon Dec 21 16:05:10 PST 2015  | true              |\n"
        + "LatestMincha        | 4:29:39 PM     | Mon Dec 21 16:14:39 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:54:35 PM     | Mon Dec 21 16:39:35 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:12:51 PM     | Mon Dec 21 16:57:51 PST 2015  | true              |\n"
        + "NightChatzot        | 12:07:10 AM    | Mon Dec 21 23:52:10 PST 2015  | true              |\n"
        + "Alot                | 6:13:31 AM     | Tue Dec 22 05:58:31 PST 2015  | true              |\n"
        + "Talit               | 6:50:53 AM     | Tue Dec 22 06:35:53 PST 2015  | true              |\n"
        + "Hanetz              | 7:54:39 AM     | Tue Dec 22 07:39:39 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:10:35 AM     | Tue Dec 22 08:55:35 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:43:20 AM    | Tue Dec 22 10:28:20 PST 2015  | true              |\n"
        + "Chatzot             | 12:07:25 PM    | Tue Dec 22 11:52:25 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:28:45 PM    | Tue Dec 22 12:13:45 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:35:15 PM     | Tue Dec 22 14:20:15 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:27:58 PM     | Tue Dec 22 15:12:58 PST 2015  | true              |\n"
        + "Shkia               | 4:20:41 PM     | Tue Dec 22 16:05:41 PST 2015  | true              |\n"
        + "LatestMincha        | 4:30:10 PM     | Tue Dec 22 16:15:10 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:55:06 PM     | Tue Dec 22 16:40:06 PST 2015  | true              |\n"
        + "FastEnd             | 4:50:41 PM     | Tue Dec 22 16:35:41 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:13:22 PM     | Tue Dec 22 16:58:22 PST 2015  | true              |\n"
        + "NightChatzot        | 12:07:40 AM    | Tue Dec 22 23:52:40 PST 2015  | true              |\n"
        + "Alot                | 6:13:59 AM     | Wed Dec 23 05:58:59 PST 2015  | true              |\n"
        + "Talit               | 6:51:20 AM     | Wed Dec 23 06:36:20 PST 2015  | true              |\n"
        + "Hanetz              | 7:55:06 AM     | Wed Dec 23 07:40:06 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:11:05 AM     | Wed Dec 23 08:56:05 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:43:49 AM    | Wed Dec 23 10:28:49 PST 2015  | true              |\n"
        + "Chatzot             | 12:07:55 PM    | Wed Dec 23 11:52:55 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:29:16 PM    | Wed Dec 23 12:14:16 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:35:47 PM     | Wed Dec 23 14:20:47 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:28:31 PM     | Wed Dec 23 15:13:31 PST 2015  | true              |\n"
        + "Shkia               | 4:21:14 PM     | Wed Dec 23 16:06:14 PST 2015  | true              |\n"
        + "LatestMincha        | 4:30:43 PM     | Wed Dec 23 16:15:43 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:55:39 PM     | Wed Dec 23 16:40:39 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:13:55 PM     | Wed Dec 23 16:58:55 PST 2015  | true              |\n"
        + "NightChatzot        | 12:08:09 AM    | Wed Dec 23 23:53:09 PST 2015  | true              |\n"
        + "Alot                | 6:14:25 AM     | Thu Dec 24 05:59:25 PST 2015  | true              |\n"
        + "Talit               | 6:51:46 AM     | Thu Dec 24 06:36:46 PST 2015  | true              |\n"
        + "Hanetz              | 7:55:31 AM     | Thu Dec 24 07:40:31 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:11:33 AM     | Thu Dec 24 08:56:33 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:44:17 AM    | Thu Dec 24 10:29:17 PST 2015  | true              |\n"
        + "Chatzot             | 12:08:24 PM    | Thu Dec 24 11:53:24 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:29:46 PM    | Thu Dec 24 12:14:46 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:36:21 PM     | Thu Dec 24 14:21:21 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:29:06 PM     | Thu Dec 24 15:14:06 PST 2015  | true              |\n"
        + "Shkia               | 4:21:50 PM     | Thu Dec 24 16:06:50 PST 2015  | true              |\n"
        + "LatestMincha        | 4:31:20 PM     | Thu Dec 24 16:16:20 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:56:14 PM     | Thu Dec 24 16:41:14 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:14:30 PM     | Thu Dec 24 16:59:30 PST 2015  | true              |\n"
        + "NightChatzot        | 12:08:39 AM    | Thu Dec 24 23:53:39 PST 2015  | true              |\n"
        + "Alot                | 6:14:48 AM     | Fri Dec 25 05:59:48 PST 2015  | true              |\n"
        + "Talit               | 6:52:09 AM     | Fri Dec 25 06:37:09 PST 2015  | true              |\n"
        + "Hanetz              | 7:55:52 AM     | Fri Dec 25 07:40:52 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:11:59 AM     | Fri Dec 25 08:56:59 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:44:45 AM    | Fri Dec 25 10:29:45 PST 2015  | true              |\n"
        + "Chatzot             | 12:08:54 PM    | Fri Dec 25 11:53:54 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:30:17 PM    | Fri Dec 25 12:15:17 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:36:56 PM     | Fri Dec 25 14:21:56 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:29:43 PM     | Fri Dec 25 15:14:43 PST 2015  | true              |\n"
        + "CandleLighting      | 4:04:29 PM     | Fri Dec 25 15:49:29 PST 2015  | true              |\n"
        + "Shkia               | 4:22:29 PM     | Fri Dec 25 16:07:29 PST 2015  | true              |\n"
        + "LatestMincha        | 4:31:59 PM     | Fri Dec 25 16:16:59 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:56:52 PM     | Fri Dec 25 16:41:52 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:15:07 PM     | Fri Dec 25 17:00:07 PST 2015  | true              |\n"
        + "NightChatzot        | 12:09:09 AM    | Fri Dec 25 23:54:09 PST 2015  | true              |\n"
        + "Alot                | 6:15:10 AM     | Sat Dec 26 06:00:10 PST 2015  | true              |\n"
        + "Talit               | 6:52:30 AM     | Sat Dec 26 06:37:30 PST 2015  | true              |\n"
        + "Hanetz              | 7:56:11 AM     | Sat Dec 26 07:41:11 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:12:25 AM     | Sat Dec 26 08:57:25 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:45:11 AM    | Sat Dec 26 10:30:11 PST 2015  | true              |\n"
        + "Chatzot             | 12:09:24 PM    | Sat Dec 26 11:54:24 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:30:48 PM    | Sat Dec 26 12:15:48 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:37:33 PM     | Sat Dec 26 14:22:33 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:30:21 PM     | Sat Dec 26 15:15:21 PST 2015  | true              |\n"
        + "Shkia               | 4:23:10 PM     | Sat Dec 26 16:08:10 PST 2015  | true              |\n"
        + "LatestMincha        | 4:32:40 PM     | Sat Dec 26 16:17:40 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:57:33 PM     | Sat Dec 26 16:42:33 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:15:47 PM     | Sat Dec 26 17:00:47 PST 2015  | true              |\n"
        + "ShabbatEnd          | 5:08:10 PM     | Sat Dec 26 16:53:10 PST 2015  | true              |\n"
        + "NightChatzot        | 12:09:38 AM    | Sat Dec 26 23:54:38 PST 2015  | true              |\n"
        + "Alot                | 6:15:29 AM     | Sun Dec 27 06:00:29 PST 2015  | true              |\n"
        + "Talit               | 6:52:48 AM     | Sun Dec 27 06:37:48 PST 2015  | true              |\n"
        + "Hanetz              | 7:56:27 AM     | Sun Dec 27 07:41:27 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:12:50 AM     | Sun Dec 27 08:57:50 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:45:36 AM    | Sun Dec 27 10:30:36 PST 2015  | true              |\n"
        + "Chatzot             | 12:09:53 PM    | Sun Dec 27 11:54:53 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:31:19 PM    | Sun Dec 27 12:16:19 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:38:11 PM     | Sun Dec 27 14:23:11 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:31:02 PM     | Sun Dec 27 15:16:02 PST 2015  | true              |\n"
        + "Shkia               | 4:23:54 PM     | Sun Dec 27 16:08:54 PST 2015  | true              |\n"
        + "LatestMincha        | 4:33:25 PM     | Sun Dec 27 16:18:25 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:58:16 PM     | Sun Dec 27 16:43:16 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:16:29 PM     | Sun Dec 27 17:01:29 PST 2015  | true              |\n"
        + "NightChatzot        | 12:10:08 AM    | Sun Dec 27 23:55:08 PST 2015  | true              |\n"
    );
  }


  @Test
  public void pesach2017() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2017, Calendar.APRIL, 10, 6, 34 );
    runDays( testTime, 10, new PesachAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "SofZmanAchilatChametz| 10:56:52 AM    | Mon Apr 10 09:56:52 PDT 2017  | true              |\n"
        + "SofZmanBiurChametz  | 12:03:46 PM    | Mon Apr 10 11:03:46 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:34:09 PM     | Mon Apr 10 19:19:09 PDT 2017  | true              |\n"
        + "FastEnd             | 8:22:09 PM     | Mon Apr 10 20:17:09 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:10:13 AM     | Tue Apr 11 00:55:13 PDT 2017  | true              |\n"
        + "Alot                | 4:48:39 AM     | Tue Apr 11 04:33:39 PDT 2017  | true              |\n"
        + "YomTovSheniCandleLighting| 8:41:33 PM     | Tue Apr 11 20:26:33 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:57 AM     | Wed Apr 12 00:54:57 PDT 2017  | true              |\n"
        + "Alot                | 4:46:16 AM     | Wed Apr 12 04:31:16 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:42 AM     | Thu Apr 13 00:54:42 PDT 2017  | true              |\n"
        + "Alot                | 4:43:53 AM     | Thu Apr 13 04:28:53 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:27 AM     | Fri Apr 14 00:54:27 PDT 2017  | true              |\n"
        + "Alot                | 4:41:30 AM     | Fri Apr 14 04:26:30 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:39:47 PM     | Fri Apr 14 19:24:47 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:12 AM     | Sat Apr 15 00:54:12 PDT 2017  | true              |\n"
        + "Alot                | 4:39:07 AM     | Sat Apr 15 04:24:07 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:58 AM     | Sun Apr 16 00:53:58 PDT 2017  | true              |\n"
        + "Alot                | 4:36:43 AM     | Sun Apr 16 04:21:43 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:42:37 PM     | Sun Apr 16 19:27:37 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:44 AM     | Mon Apr 17 00:53:44 PDT 2017  | true              |\n"
        + "Alot                | 4:34:20 AM     | Mon Apr 17 04:19:20 PDT 2017  | true              |\n"
        + "YomTovSheniCandleLighting| 8:51:04 PM     | Mon Apr 17 20:36:04 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:30 AM     | Tue Apr 18 00:53:30 PDT 2017  | true              |\n"
        + "Alot                | 4:31:56 AM     | Tue Apr 18 04:16:56 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:17 AM     | Wed Apr 19 00:53:17 PDT 2017  | true              |\n"
        + "Alot                | 4:29:32 AM     | Wed Apr 19 04:14:32 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:04 AM     | Thu Apr 20 00:53:04 PDT 2017  | true              |\n"
    );
  }


  /**
   * Make sure that just because a day is Erev Rosh Chodesh that we'll still notify after candle lighting.
   */
  @Test
  public void may_25_2017() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2017, Calendar.MAY, 25, 6, 0 );
    runDays( testTime, 2, new DefaultAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
        "--------------------|----------------|-------------------------------|-------------------|\n" +
        "SofZmanShema        | 8:07:46 AM     | Thu May 25 07:52:46 PDT 2017  | true              |\n" +
        "SofZmanTefila       | 10:31:10 AM    | Thu May 25 10:16:10 PDT 2017  | true              |\n" +
        "PlagHaMincha        | 7:15:02 PM     | Thu May 25 19:00:02 PDT 2017  | true              |\n" +
        "Shkia               | 8:52:03 PM     | Thu May 25 20:37:03 PDT 2017  | true              |\n" +
        "LatestMincha        | 9:09:31 PM     | Thu May 25 20:54:31 PDT 2017  | true              |\n" +
        "Talit               | 4:06:40 AM     | Fri May 26 03:51:40 PDT 2017  | true              |\n" +
        "SofZmanShema        | 8:06:53 AM     | Fri May 26 07:51:53 PDT 2017  | true              |\n" +
        "SofZmanTefila       | 10:30:56 AM    | Fri May 26 10:15:56 PDT 2017  | true              |\n" +
        "PlagHaMincha        | 7:15:54 PM     | Fri May 26 19:00:54 PDT 2017  | true              |\n" +
        "CandleLighting      | 8:35:07 PM     | Fri May 26 20:20:07 PDT 2017  | true              |\n" +
        "Shkia               | 8:53:07 PM     | Fri May 26 20:38:07 PDT 2017  | true              |\n" +
        "LatestMincha        | 9:10:37 PM     | Fri May 26 20:55:37 PDT 2017  | true              |\n"
    );
  }

  public static class DefaultAlertsZmanCallback
    extends UT_ZmanUtils.ZmanCallbackForTests {

    private final NotificationDefaults notificationDefaults = new NotificationDefaults();


    @Override
    public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
      return notificationDefaults.getDefaultZmanAlertTime( type );
    }
  }


  private static class PesachAlertsZmanCallback
    extends UT_ZmanUtils.ZmanCallbackForTests {

    @Override
    public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {

      switch ( type ) {
        case Alot:
        case CandleLighting:
        case YomTovSheniCandleLighting:
        case NightChatzot:
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
        case Talit:
        case Hanetz:
        case SofZmanShema:
        case SofZmanTefila:
        case Chatzot:
        case MinchaGedola:
        case MinchaKetana:
        case PlagHaMincha:
        case Shkia:
        case BeinHashmashot:
        case LatestMincha:
        case TzaitHacochavim:
        case ShabbatEnd:
          return ZmanNotificationScheduler.NO_ALERT;
        case SofZmanAchilatChametz:
        case SofZmanBiurChametz:
          return ZmanNotificationScheduler.ONE_HOUR;
        case FastEnd:
          return UT_ZmanUtils.FIVE_MINUTES;
      }

      assert false;
      return ZmanNotificationScheduler.NO_ALERT;
    }
  }


} // End of UT_WakeupScheduler_MultiDay class
