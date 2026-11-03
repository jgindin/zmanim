/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.notification.NotificationDefaults;
import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.jupiter.api.Test;

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
        + "Hanetz              | 7:43:06 AM     | Sun Dec 07 07:28:06 PST 2014  | true              |\n"
        + "SofZmanShema        | 9:01:46 AM     | Sun Dec 07 08:46:46 PST 2014  | true              |\n"
        + "SofZmanTefila       | 10:34:44 AM    | Sun Dec 07 10:19:44 PST 2014  | true              |\n"
        + "Chatzot             | 12:00:39 PM    | Sun Dec 07 11:45:39 PST 2014  | true              |\n"
        + "MinchaGedola        | 12:22:00 PM    | Sun Dec 07 12:07:00 PST 2014  | true              |\n"
        + "MinchaKetana        | 2:30:43 PM     | Sun Dec 07 14:15:43 PST 2014  | true              |\n"
        + "PlagHaMincha        | 3:24:21 PM     | Sun Dec 07 15:09:21 PST 2014  | true              |\n"
        + "Shkia               | 4:17:59 PM     | Sun Dec 07 16:02:59 PST 2014  | true              |\n"
        + "LatestMincha        | 4:27:38 PM     | Sun Dec 07 16:12:38 PST 2014  | true              |\n"
        + "BeinHashmashot      | 4:52:05 PM     | Sun Dec 07 16:37:05 PST 2014  | true              |\n"
        + "TzaitHacochavim     | 5:10:00 PM     | Sun Dec 07 16:55:00 PST 2014  | true              |\n"
        + "NightChatzot        | 12:00:52 AM    | Sun Dec 07 23:45:52 PST 2014  | true              |\n"
    );
  }


  @Test
  public void dec7_10_2016() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, 3, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Talit               | 6:40:34 AM     | Wed Dec 07 06:34:00 PST 2016  | true              |\n"
        + "Hanetz              | 7:43:38 AM     | Wed Dec 07 07:28:38 PST 2016  | true              |\n"
        + "SofZmanShema        | 9:02:07 AM     | Wed Dec 07 08:47:07 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:35:03 AM    | Wed Dec 07 10:20:03 PST 2016  | true              |\n"
        + "Chatzot             | 12:00:52 PM    | Wed Dec 07 11:45:52 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:22:12 PM    | Wed Dec 07 12:07:12 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:30:46 PM     | Wed Dec 07 14:15:46 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:24:20 PM     | Wed Dec 07 15:09:20 PST 2016  | true              |\n"
        + "Shkia               | 4:17:54 PM     | Wed Dec 07 16:02:54 PST 2016  | true              |\n"
        + "LatestMincha        | 4:27:33 PM     | Wed Dec 07 16:12:33 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:52:01 PM     | Wed Dec 07 16:37:01 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:09:58 PM     | Wed Dec 07 16:54:58 PST 2016  | true              |\n"
        + "NightChatzot        | 12:01:06 AM    | Wed Dec 07 23:46:06 PST 2016  | true              |\n"
        + "Alot                | 6:04:21 AM     | Thu Dec 08 05:49:21 PST 2016  | true              |\n"
        + "Talit               | 6:41:30 AM     | Thu Dec 08 06:26:30 PST 2016  | true              |\n"
        + "Hanetz              | 7:44:39 AM     | Thu Dec 08 07:29:39 PST 2016  | true              |\n"
        + "SofZmanShema        | 9:02:47 AM     | Thu Dec 08 08:47:47 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:35:42 AM    | Thu Dec 08 10:20:42 PST 2016  | true              |\n"
        + "Chatzot             | 12:01:19 PM    | Thu Dec 08 11:46:19 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:22:36 PM    | Thu Dec 08 12:07:36 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:30:53 PM     | Thu Dec 08 14:15:53 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:24:20 PM     | Thu Dec 08 15:09:20 PST 2016  | true              |\n"
        + "Shkia               | 4:17:47 PM     | Thu Dec 08 16:02:47 PST 2016  | true              |\n"
        + "LatestMincha        | 4:27:24 PM     | Thu Dec 08 16:12:24 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:51:56 PM     | Thu Dec 08 16:36:56 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:09:55 PM     | Thu Dec 08 16:54:55 PST 2016  | true              |\n"
        + "NightChatzot        | 12:01:32 AM    | Thu Dec 08 23:46:32 PST 2016  | true              |\n"
        + "Alot                | 6:05:13 AM     | Fri Dec 09 05:50:13 PST 2016  | true              |\n"
        + "Talit               | 6:42:24 AM     | Fri Dec 09 06:27:24 PST 2016  | true              |\n"
        + "Hanetz              | 7:45:38 AM     | Fri Dec 09 07:30:38 PST 2016  | true              |\n"
        + "SofZmanShema        | 9:03:27 AM     | Fri Dec 09 08:48:27 PST 2016  | true              |\n"
        + "SofZmanTefila       | 10:36:20 AM    | Fri Dec 09 10:21:20 PST 2016  | true              |\n"
        + "Chatzot             | 12:01:46 PM    | Fri Dec 09 11:46:46 PST 2016  | true              |\n"
        + "MinchaGedola        | 12:23:01 PM    | Fri Dec 09 12:08:01 PST 2016  | true              |\n"
        + "MinchaKetana        | 2:31:02 PM     | Fri Dec 09 14:16:02 PST 2016  | true              |\n"
        + "PlagHaMincha        | 3:24:22 PM     | Fri Dec 09 15:09:22 PST 2016  | true              |\n"
        + "CandleLighting      | 3:59:43 PM     | Fri Dec 09 15:44:43 PST 2016  | true              |\n"
        + "Shkia               | 4:17:43 PM     | Fri Dec 09 16:02:43 PST 2016  | true              |\n"
        + "LatestMincha        | 4:27:19 PM     | Fri Dec 09 16:12:19 PST 2016  | true              |\n"
        + "BeinHashmashot      | 4:51:54 PM     | Fri Dec 09 16:36:54 PST 2016  | true              |\n"
        + "TzaitHacochavim     | 5:09:55 PM     | Fri Dec 09 16:54:55 PST 2016  | true              |\n"
        + "NightChatzot        | 12:02:00 AM    | Fri Dec 09 23:47:00 PST 2016  | true              |\n"
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
        "PlagHaMincha        | 3:24:21 PM     | Sun Dec 07 15:09:21 PST 2014  | true              |\n" +
        "Shkia               | 4:17:59 PM     | Sun Dec 07 16:02:59 PST 2014  | true              |\n" +
        "LatestMincha        | 4:27:38 PM     | Sun Dec 07 16:12:38 PST 2014  | true              |\n"
    );
  }


  @Test
  public void december_21_2015_1_week() {

    december_21_2015_1_week( new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
        + "Alot                | 6:13:01 AM     | Mon Dec 21 05:58:01 PST 2015  | true              |\n"
        + "Talit               | 6:50:23 AM     | Mon Dec 21 06:35:23 PST 2015  | true              |\n"
        + "Hanetz              | 7:54:07 AM     | Mon Dec 21 07:39:07 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:10:05 AM     | Mon Dec 21 08:55:05 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:42:49 AM    | Mon Dec 21 10:27:49 PST 2015  | true              |\n"
        + "Chatzot             | 12:07:10 PM    | Mon Dec 21 11:52:10 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:28:15 PM    | Mon Dec 21 12:13:15 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:34:46 PM     | Mon Dec 21 14:19:46 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:27:29 PM     | Mon Dec 21 15:12:29 PST 2015  | true              |\n"
        + "Shkia               | 4:20:12 PM     | Mon Dec 21 16:05:12 PST 2015  | true              |\n"
        + "LatestMincha        | 4:29:41 PM     | Mon Dec 21 16:14:41 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:54:36 PM     | Mon Dec 21 16:39:36 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:12:51 PM     | Mon Dec 21 16:57:51 PST 2015  | true              |\n"
        + "NightChatzot        | 12:07:25 AM    | Mon Dec 21 23:52:25 PST 2015  | true              |\n"
        + "Alot                | 6:13:31 AM     | Tue Dec 22 05:58:31 PST 2015  | true              |\n"
        + "Talit               | 6:50:53 AM     | Tue Dec 22 06:35:53 PST 2015  | true              |\n"
        + "Hanetz              | 7:54:37 AM     | Tue Dec 22 07:39:37 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:10:35 AM     | Tue Dec 22 08:55:35 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:43:19 AM    | Tue Dec 22 10:28:19 PST 2015  | true              |\n"
        + "Chatzot             | 12:07:40 PM    | Tue Dec 22 11:52:40 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:28:45 PM    | Tue Dec 22 12:13:45 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:35:17 PM     | Tue Dec 22 14:20:17 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:28:00 PM     | Tue Dec 22 15:13:00 PST 2015  | true              |\n"
        + "Shkia               | 4:20:43 PM     | Tue Dec 22 16:05:43 PST 2015  | true              |\n"
        + "LatestMincha        | 4:30:12 PM     | Tue Dec 22 16:15:12 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:55:07 PM     | Tue Dec 22 16:40:07 PST 2015  | true              |\n"
        + "FastEnd             | 4:50:43 PM     | Tue Dec 22 16:35:43 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:13:22 PM     | Tue Dec 22 16:58:22 PST 2015  | true              |\n"
        + "NightChatzot        | 12:07:55 AM    | Tue Dec 22 23:52:55 PST 2015  | true              |\n"
        + "Alot                | 6:13:59 AM     | Wed Dec 23 05:58:59 PST 2015  | true              |\n"
        + "Talit               | 6:51:20 AM     | Wed Dec 23 06:36:20 PST 2015  | true              |\n"
        + "Hanetz              | 7:55:04 AM     | Wed Dec 23 07:40:04 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:11:05 AM     | Wed Dec 23 08:56:05 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:43:48 AM    | Wed Dec 23 10:28:48 PST 2015  | true              |\n"
        + "Chatzot             | 12:08:09 PM    | Wed Dec 23 11:53:09 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:29:16 PM    | Wed Dec 23 12:14:16 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:35:49 PM     | Wed Dec 23 14:20:49 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:28:32 PM     | Wed Dec 23 15:13:32 PST 2015  | true              |\n"
        + "Shkia               | 4:21:16 PM     | Wed Dec 23 16:06:16 PST 2015  | true              |\n"
        + "LatestMincha        | 4:30:45 PM     | Wed Dec 23 16:15:45 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:55:40 PM     | Wed Dec 23 16:40:40 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:13:55 PM     | Wed Dec 23 16:58:55 PST 2015  | true              |\n"
        + "NightChatzot        | 12:08:24 AM    | Wed Dec 23 23:53:24 PST 2015  | true              |\n"
        + "Alot                | 6:14:25 AM     | Thu Dec 24 05:59:25 PST 2015  | true              |\n"
        + "Talit               | 6:51:46 AM     | Thu Dec 24 06:36:46 PST 2015  | true              |\n"
        + "Hanetz              | 7:55:29 AM     | Thu Dec 24 07:40:29 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:11:33 AM     | Thu Dec 24 08:56:33 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:44:17 AM    | Thu Dec 24 10:29:17 PST 2015  | true              |\n"
        + "Chatzot             | 12:08:39 PM    | Thu Dec 24 11:53:39 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:29:46 PM    | Thu Dec 24 12:14:46 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:36:22 PM     | Thu Dec 24 14:21:22 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:29:07 PM     | Thu Dec 24 15:14:07 PST 2015  | true              |\n"
        + "Shkia               | 4:21:52 PM     | Thu Dec 24 16:06:52 PST 2015  | true              |\n"
        + "LatestMincha        | 4:31:22 PM     | Thu Dec 24 16:16:22 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:56:16 PM     | Thu Dec 24 16:41:16 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:14:30 PM     | Thu Dec 24 16:59:30 PST 2015  | true              |\n"
        + "NightChatzot        | 12:08:54 AM    | Thu Dec 24 23:53:54 PST 2015  | true              |\n"
        + "Alot                | 6:14:48 AM     | Fri Dec 25 05:59:48 PST 2015  | true              |\n"
        + "Talit               | 6:52:09 AM     | Fri Dec 25 06:37:09 PST 2015  | true              |\n"
        + "Hanetz              | 7:55:50 AM     | Fri Dec 25 07:40:50 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:11:59 AM     | Fri Dec 25 08:56:59 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:44:44 AM    | Fri Dec 25 10:29:44 PST 2015  | true              |\n"
        + "Chatzot             | 12:09:09 PM    | Fri Dec 25 11:54:09 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:30:17 PM    | Fri Dec 25 12:15:17 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:36:57 PM     | Fri Dec 25 14:21:57 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:29:44 PM     | Fri Dec 25 15:14:44 PST 2015  | true              |\n"
        + "CandleLighting      | 4:04:31 PM     | Fri Dec 25 15:49:31 PST 2015  | true              |\n"
        + "Shkia               | 4:22:31 PM     | Fri Dec 25 16:07:31 PST 2015  | true              |\n"
        + "LatestMincha        | 4:32:01 PM     | Fri Dec 25 16:17:01 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:56:54 PM     | Fri Dec 25 16:41:54 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:15:07 PM     | Fri Dec 25 17:00:07 PST 2015  | true              |\n"
        + "NightChatzot        | 12:09:24 AM    | Fri Dec 25 23:54:24 PST 2015  | true              |\n"
        + "Alot                | 6:15:10 AM     | Sat Dec 26 06:00:10 PST 2015  | true              |\n"
        + "Talit               | 6:52:30 AM     | Sat Dec 26 06:37:30 PST 2015  | true              |\n"
        + "Hanetz              | 7:56:09 AM     | Sat Dec 26 07:41:09 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:12:25 AM     | Sat Dec 26 08:57:25 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:45:10 AM    | Sat Dec 26 10:30:10 PST 2015  | true              |\n"
        + "Chatzot             | 12:09:38 PM    | Sat Dec 26 11:54:38 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:30:48 PM    | Sat Dec 26 12:15:48 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:37:34 PM     | Sat Dec 26 14:22:34 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:30:23 PM     | Sat Dec 26 15:15:23 PST 2015  | true              |\n"
        + "Shkia               | 4:23:12 PM     | Sat Dec 26 16:08:12 PST 2015  | true              |\n"
        + "LatestMincha        | 4:32:42 PM     | Sat Dec 26 16:17:42 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:57:34 PM     | Sat Dec 26 16:42:34 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:15:47 PM     | Sat Dec 26 17:00:47 PST 2015  | true              |\n"
        + "ShabbatEnd          | 5:08:12 PM     | Sat Dec 26 16:53:12 PST 2015  | true              |\n"
        + "NightChatzot        | 12:09:53 AM    | Sat Dec 26 23:54:53 PST 2015  | true              |\n"
        + "Alot                | 6:15:29 AM     | Sun Dec 27 06:00:29 PST 2015  | true              |\n"
        + "Talit               | 6:52:48 AM     | Sun Dec 27 06:37:48 PST 2015  | true              |\n"
        + "Hanetz              | 7:56:25 AM     | Sun Dec 27 07:41:25 PST 2015  | true              |\n"
        + "SofZmanShema        | 9:12:50 AM     | Sun Dec 27 08:57:50 PST 2015  | true              |\n"
        + "SofZmanTefila       | 10:45:36 AM    | Sun Dec 27 10:30:36 PST 2015  | true              |\n"
        + "Chatzot             | 12:10:08 PM    | Sun Dec 27 11:55:08 PST 2015  | true              |\n"
        + "MinchaGedola        | 12:31:19 PM    | Sun Dec 27 12:16:19 PST 2015  | true              |\n"
        + "MinchaKetana        | 2:38:12 PM     | Sun Dec 27 14:23:12 PST 2015  | true              |\n"
        + "PlagHaMincha        | 3:31:04 PM     | Sun Dec 27 15:16:04 PST 2015  | true              |\n"
        + "Shkia               | 4:23:56 PM     | Sun Dec 27 16:08:56 PST 2015  | true              |\n"
        + "LatestMincha        | 4:33:27 PM     | Sun Dec 27 16:18:27 PST 2015  | true              |\n"
        + "BeinHashmashot      | 4:58:17 PM     | Sun Dec 27 16:43:17 PST 2015  | true              |\n"
        + "TzaitHacochavim     | 5:16:29 PM     | Sun Dec 27 17:01:29 PST 2015  | true              |\n"
        + "NightChatzot        | 12:10:23 AM    | Sun Dec 27 23:55:23 PST 2015  | true              |\n"
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
        + "CandleLighting      | 7:34:08 PM     | Mon Apr 10 19:19:08 PDT 2017  | true              |\n"
        + "FastEnd             | 8:22:08 PM     | Mon Apr 10 20:17:08 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:10:05 AM     | Tue Apr 11 00:55:05 PDT 2017  | true              |\n"
        + "Alot                | 4:48:39 AM     | Tue Apr 11 04:33:39 PDT 2017  | true              |\n"
        + "YomTovSheniCandleLighting| 8:41:33 PM     | Tue Apr 11 20:26:33 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:49 AM     | Wed Apr 12 00:54:49 PDT 2017  | true              |\n"
        + "Alot                | 4:46:16 AM     | Wed Apr 12 04:31:16 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:34 AM     | Thu Apr 13 00:54:34 PDT 2017  | true              |\n"
        + "Alot                | 4:43:53 AM     | Thu Apr 13 04:28:53 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:19 AM     | Fri Apr 14 00:54:19 PDT 2017  | true              |\n"
        + "Alot                | 4:41:30 AM     | Fri Apr 14 04:26:30 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:39:47 PM     | Fri Apr 14 19:24:47 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:09:05 AM     | Sat Apr 15 00:54:05 PDT 2017  | true              |\n"
        + "Alot                | 4:39:07 AM     | Sat Apr 15 04:24:07 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:51 AM     | Sun Apr 16 00:53:51 PDT 2017  | true              |\n"
        + "Alot                | 4:36:43 AM     | Sun Apr 16 04:21:43 PDT 2017  | true              |\n"
        + "CandleLighting      | 7:42:36 PM     | Sun Apr 16 19:27:36 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:37 AM     | Mon Apr 17 00:53:37 PDT 2017  | true              |\n"
        + "Alot                | 4:34:20 AM     | Mon Apr 17 04:19:20 PDT 2017  | true              |\n"
        + "YomTovSheniCandleLighting| 8:51:04 PM     | Mon Apr 17 20:36:04 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:23 AM     | Tue Apr 18 00:53:23 PDT 2017  | true              |\n"
        + "Alot                | 4:31:56 AM     | Tue Apr 18 04:16:56 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:08:10 AM     | Wed Apr 19 00:53:10 PDT 2017  | true              |\n"
        + "Alot                | 4:29:32 AM     | Wed Apr 19 04:14:32 PDT 2017  | true              |\n"
        + "NightChatzot        | 1:07:58 AM     | Thu Apr 20 00:52:58 PDT 2017  | true              |\n"
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
        "PlagHaMincha        | 7:15:01 PM     | Thu May 25 19:00:01 PDT 2017  | true              |\n" +
        "Shkia               | 8:52:01 PM     | Thu May 25 20:37:01 PDT 2017  | true              |\n" +
        "LatestMincha        | 9:09:29 PM     | Thu May 25 20:54:29 PDT 2017  | true              |\n" +
        "Talit               | 4:06:40 AM     | Fri May 26 03:51:40 PDT 2017  | true              |\n" +
        "SofZmanShema        | 8:06:53 AM     | Fri May 26 07:51:53 PDT 2017  | true              |\n" +
        "SofZmanTefila       | 10:30:57 AM    | Fri May 26 10:15:57 PDT 2017  | true              |\n" +
        "PlagHaMincha        | 7:15:53 PM     | Fri May 26 19:00:53 PDT 2017  | true              |\n" +
        "CandleLighting      | 8:35:06 PM     | Fri May 26 20:20:06 PDT 2017  | true              |\n" +
        "Shkia               | 8:53:06 PM     | Fri May 26 20:38:06 PDT 2017  | true              |\n" +
        "LatestMincha        | 9:10:36 PM     | Fri May 26 20:55:36 PDT 2017  | true              |\n"
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
