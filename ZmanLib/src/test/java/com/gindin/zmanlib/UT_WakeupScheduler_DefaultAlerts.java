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
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Talit               | 6:39:55 AM     | Sun Dec 07 06:34:00 PST 2014  | true              |\n" +
      "Hanetz              | 7:42:53 AM     | Sun Dec 07 07:27:53 PST 2014  | true              |\n" +
      "SofZmanShema        | 9:01:40 AM     | Sun Dec 07 08:46:40 PST 2014  | true              |\n" +
      "SofZmanTefila       | 10:34:35 AM    | Sun Dec 07 10:19:35 PST 2014  | true              |\n" +
      "Chatzot             | 12:00:26 PM    | Sun Dec 07 11:45:26 PST 2014  | true              |\n" +
      "MinchaGedola        | 12:21:54 PM    | Sun Dec 07 12:06:54 PST 2014  | true              |\n" +
      "MinchaKetana        | 2:30:41 PM     | Sun Dec 07 14:15:41 PST 2014  | true              |\n" +
      "PlagHaMincha        | 3:24:20 PM     | Sun Dec 07 15:09:20 PST 2014  | true              |\n" +
      "Shkia               | 4:18:00 PM     | Sun Dec 07 16:03:00 PST 2014  | true              |\n" +
      "LatestMincha        | 4:27:39 PM     | Sun Dec 07 16:12:39 PST 2014  | true              |\n" +
      "BeinHashmashot      | 4:52:04 PM     | Sun Dec 07 16:37:04 PST 2014  | true              |\n" +
      "TzaitHacochavim     | 5:10:02 PM     | Sun Dec 07 16:55:02 PST 2014  | true              |\n" +
      "NightChatzot        | 12:00:58 AM    | Sun Dec 07 23:45:58 PST 2014  | true              |\n"
    );
  }


  @Test
  public void dec7_10_2016() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, 3, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Talit               | 6:40:52 AM     | Wed Dec 07 06:34:00 PST 2016  | true              |\n" +
      "Hanetz              | 7:43:56 AM     | Wed Dec 07 07:28:56 PST 2016  | true              |\n" +
      "SofZmanShema        | 9:02:20 AM     | Wed Dec 07 08:47:20 PST 2016  | true              |\n" +
      "SofZmanTefila       | 10:35:14 AM    | Wed Dec 07 10:20:14 PST 2016  | true              |\n" +
      "Chatzot             | 12:00:53 PM    | Wed Dec 07 11:45:53 PST 2016  | true              |\n" +
      "MinchaGedola        | 12:22:18 PM    | Wed Dec 07 12:07:18 PST 2016  | true              |\n" +
      "MinchaKetana        | 2:30:46 PM     | Wed Dec 07 14:15:46 PST 2016  | true              |\n" +
      "PlagHaMincha        | 3:24:18 PM     | Wed Dec 07 15:09:18 PST 2016  | true              |\n" +
      "Shkia               | 4:17:50 PM     | Wed Dec 07 16:02:50 PST 2016  | true              |\n" +
      "LatestMincha        | 4:27:28 PM     | Wed Dec 07 16:12:28 PST 2016  | true              |\n" +
      "BeinHashmashot      | 4:51:57 PM     | Wed Dec 07 16:36:57 PST 2016  | true              |\n" +
      "TzaitHacochavim     | 5:09:57 PM     | Wed Dec 07 16:54:57 PST 2016  | true              |\n" +
      "NightChatzot        | 12:01:24 AM    | Wed Dec 07 23:46:24 PST 2016  | true              |\n" +
      "Alot                | 6:04:39 AM     | Thu Dec 08 05:49:39 PST 2016  | true              |\n" +
      "Talit               | 6:41:47 AM     | Thu Dec 08 06:26:47 PST 2016  | true              |\n" +
      "Hanetz              | 7:44:57 AM     | Thu Dec 08 07:29:57 PST 2016  | true              |\n" +
      "SofZmanShema        | 9:03:00 AM     | Thu Dec 08 08:48:00 PST 2016  | true              |\n" +
      "SofZmanTefila       | 10:35:52 AM    | Thu Dec 08 10:20:52 PST 2016  | true              |\n" +
      "Chatzot             | 12:01:20 PM    | Thu Dec 08 11:46:20 PST 2016  | true              |\n" +
      "MinchaGedola        | 12:22:42 PM    | Thu Dec 08 12:07:42 PST 2016  | true              |\n" +
      "MinchaKetana        | 2:30:54 PM     | Thu Dec 08 14:15:54 PST 2016  | true              |\n" +
      "PlagHaMincha        | 3:24:19 PM     | Thu Dec 08 15:09:19 PST 2016  | true              |\n" +
      "Shkia               | 4:17:44 PM     | Thu Dec 08 16:02:44 PST 2016  | true              |\n" +
      "LatestMincha        | 4:27:21 PM     | Thu Dec 08 16:12:21 PST 2016  | true              |\n" +
      "BeinHashmashot      | 4:51:53 PM     | Thu Dec 08 16:36:53 PST 2016  | true              |\n" +
      "TzaitHacochavim     | 5:09:56 PM     | Thu Dec 08 16:54:56 PST 2016  | true              |\n" +
      "NightChatzot        | 12:01:50 AM    | Thu Dec 08 23:46:50 PST 2016  | true              |\n" +
      "Alot                | 6:05:30 AM     | Fri Dec 09 05:50:30 PST 2016  | true              |\n" +
      "Talit               | 6:42:40 AM     | Fri Dec 09 06:27:40 PST 2016  | true              |\n" +
      "Hanetz              | 7:45:56 AM     | Fri Dec 09 07:30:56 PST 2016  | true              |\n" +
      "SofZmanShema        | 9:03:40 AM     | Fri Dec 09 08:48:40 PST 2016  | true              |\n" +
      "SofZmanTefila       | 10:36:31 AM    | Fri Dec 09 10:21:31 PST 2016  | true              |\n" +
      "Chatzot             | 12:01:48 PM    | Fri Dec 09 11:46:48 PST 2016  | true              |\n" +
      "MinchaGedola        | 12:23:07 PM    | Fri Dec 09 12:08:07 PST 2016  | true              |\n" +
      "MinchaKetana        | 2:31:03 PM     | Fri Dec 09 14:16:03 PST 2016  | true              |\n" +
      "PlagHaMincha        | 3:24:22 PM     | Fri Dec 09 15:09:22 PST 2016  | true              |\n" +
      "CandleLighting      | 3:59:40 PM     | Fri Dec 09 15:44:40 PST 2016  | true              |\n" +
      "Shkia               | 4:17:40 PM     | Fri Dec 09 16:02:40 PST 2016  | true              |\n" +
      "LatestMincha        | 4:27:16 PM     | Fri Dec 09 16:12:16 PST 2016  | true              |\n" +
      "BeinHashmashot      | 4:51:52 PM     | Fri Dec 09 16:36:52 PST 2016  | true              |\n" +
      "TzaitHacochavim     | 5:09:56 PM     | Fri Dec 09 16:54:56 PST 2016  | true              |\n" +
      "NightChatzot        | 12:02:16 AM    | Fri Dec 09 23:47:16 PST 2016  | true              |\n"
    );
  }


  @Test
  public void december_7_2014_default_product_alerts() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, new DefaultAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Talit               | 6:39:55 AM     | Sun Dec 07 06:34:00 PST 2014  | true              |\n" +
      "SofZmanShema        | 9:01:40 AM     | Sun Dec 07 08:46:40 PST 2014  | true              |\n" +
      "SofZmanTefila       | 10:34:35 AM    | Sun Dec 07 10:19:35 PST 2014  | true              |\n" +
      "PlagHaMincha        | 3:24:20 PM     | Sun Dec 07 15:09:20 PST 2014  | true              |\n" +
      "Shkia               | 4:18:00 PM     | Sun Dec 07 16:03:00 PST 2014  | true              |\n" +
      "LatestMincha        | 4:27:39 PM     | Sun Dec 07 16:12:39 PST 2014  | true              |\n"
    );
  }


  @Test
  public void december_21_2015_1_week() {

    december_21_2015_1_week( new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Alot                | 6:13:06 AM     | Mon Dec 21 05:58:06 PST 2015  | true              |\n" +
      "Talit               | 6:50:27 AM     | Mon Dec 21 06:35:27 PST 2015  | true              |\n" +
      "Hanetz              | 7:54:13 AM     | Mon Dec 21 07:39:13 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:10:09 AM     | Mon Dec 21 08:55:09 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:42:53 AM    | Mon Dec 21 10:27:53 PST 2015  | true              |\n" +
      "Chatzot             | 12:07:13 PM    | Mon Dec 21 11:52:13 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:28:18 PM    | Mon Dec 21 12:13:18 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:34:48 PM     | Mon Dec 21 14:19:48 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:27:30 PM     | Mon Dec 21 15:12:30 PST 2015  | true              |\n" +
      "Shkia               | 4:20:13 PM     | Mon Dec 21 16:05:13 PST 2015  | true              |\n" +
      "LatestMincha        | 4:29:42 PM     | Mon Dec 21 16:14:42 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:54:37 PM     | Mon Dec 21 16:39:37 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:12:53 PM     | Mon Dec 21 16:57:53 PST 2015  | true              |\n" +
      "NightChatzot        | 12:07:28 AM    | Mon Dec 21 23:52:28 PST 2015  | true              |\n" +
      "Alot                | 6:13:36 AM     | Tue Dec 22 05:58:36 PST 2015  | true              |\n" +
      "Talit               | 6:50:57 AM     | Tue Dec 22 06:35:57 PST 2015  | true              |\n" +
      "Hanetz              | 7:54:43 AM     | Tue Dec 22 07:39:43 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:10:39 AM     | Tue Dec 22 08:55:39 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:43:23 AM    | Tue Dec 22 10:28:23 PST 2015  | true              |\n" +
      "Chatzot             | 12:07:43 PM    | Tue Dec 22 11:52:43 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:28:48 PM    | Tue Dec 22 12:13:48 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:35:18 PM     | Tue Dec 22 14:20:18 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:28:01 PM     | Tue Dec 22 15:13:01 PST 2015  | true              |\n" +
      "Shkia               | 4:20:44 PM     | Tue Dec 22 16:05:44 PST 2015  | true              |\n" +
      "LatestMincha        | 4:30:13 PM     | Tue Dec 22 16:15:13 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:55:08 PM     | Tue Dec 22 16:40:08 PST 2015  | true              |\n" +
      "FastEnd             | 4:50:44 PM     | Tue Dec 22 16:35:44 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:13:24 PM     | Tue Dec 22 16:58:24 PST 2015  | true              |\n" +
      "NightChatzot        | 12:07:57 AM    | Tue Dec 22 23:52:57 PST 2015  | true              |\n" +
      "Alot                | 6:14:04 AM     | Wed Dec 23 05:59:04 PST 2015  | true              |\n" +
      "Talit               | 6:51:25 AM     | Wed Dec 23 06:36:25 PST 2015  | true              |\n" +
      "Hanetz              | 7:55:10 AM     | Wed Dec 23 07:40:10 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:11:09 AM     | Wed Dec 23 08:56:09 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:43:52 AM    | Wed Dec 23 10:28:52 PST 2015  | true              |\n" +
      "Chatzot             | 12:08:14 PM    | Wed Dec 23 11:53:14 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:29:19 PM    | Wed Dec 23 12:14:19 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:35:51 PM     | Wed Dec 23 14:20:51 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:28:34 PM     | Wed Dec 23 15:13:34 PST 2015  | true              |\n" +
      "Shkia               | 4:21:17 PM     | Wed Dec 23 16:06:17 PST 2015  | true              |\n" +
      "LatestMincha        | 4:30:47 PM     | Wed Dec 23 16:15:47 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:55:42 PM     | Wed Dec 23 16:40:42 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:13:57 PM     | Wed Dec 23 16:58:57 PST 2015  | true              |\n" +
      "NightChatzot        | 12:08:26 AM    | Wed Dec 23 23:53:26 PST 2015  | true              |\n" +
      "Alot                | 6:14:30 AM     | Thu Dec 24 05:59:30 PST 2015  | true              |\n" +
      "Talit               | 6:51:50 AM     | Thu Dec 24 06:36:50 PST 2015  | true              |\n" +
      "Hanetz              | 7:55:35 AM     | Thu Dec 24 07:40:35 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:11:37 AM     | Thu Dec 24 08:56:37 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:44:21 AM    | Thu Dec 24 10:29:21 PST 2015  | true              |\n" +
      "Chatzot             | 12:08:44 PM    | Thu Dec 24 11:53:44 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:29:50 PM    | Thu Dec 24 12:14:50 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:36:25 PM     | Thu Dec 24 14:21:25 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:29:09 PM     | Thu Dec 24 15:14:09 PST 2015  | true              |\n" +
      "Shkia               | 4:21:54 PM     | Thu Dec 24 16:06:54 PST 2015  | true              |\n" +
      "LatestMincha        | 4:31:23 PM     | Thu Dec 24 16:16:23 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:56:17 PM     | Thu Dec 24 16:41:17 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:14:32 PM     | Thu Dec 24 16:59:32 PST 2015  | true              |\n" +
      "NightChatzot        | 12:08:55 AM    | Thu Dec 24 23:53:55 PST 2015  | true              |\n" +
      "Alot                | 6:14:53 AM     | Fri Dec 25 05:59:53 PST 2015  | true              |\n" +
      "Talit               | 6:52:14 AM     | Fri Dec 25 06:37:14 PST 2015  | true              |\n" +
      "Hanetz              | 7:55:57 AM     | Fri Dec 25 07:40:57 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:12:04 AM     | Fri Dec 25 08:57:04 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:44:49 AM    | Fri Dec 25 10:29:49 PST 2015  | true              |\n" +
      "Chatzot             | 12:09:15 PM    | Fri Dec 25 11:54:15 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:30:21 PM    | Fri Dec 25 12:15:21 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:37:00 PM     | Fri Dec 25 14:22:00 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:29:46 PM     | Fri Dec 25 15:14:46 PST 2015  | true              |\n" +
      "CandleLighting      | 4:04:33 PM     | Fri Dec 25 15:49:33 PST 2015  | true              |\n" +
      "Shkia               | 4:22:33 PM     | Fri Dec 25 16:07:33 PST 2015  | true              |\n" +
      "LatestMincha        | 4:32:02 PM     | Fri Dec 25 16:17:02 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:56:56 PM     | Fri Dec 25 16:41:56 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:15:10 PM     | Fri Dec 25 17:00:10 PST 2015  | true              |\n" +
      "NightChatzot        | 12:09:24 AM    | Fri Dec 25 23:54:24 PST 2015  | true              |\n" +
      "Alot                | 6:15:15 AM     | Sat Dec 26 06:00:15 PST 2015  | true              |\n" +
      "Talit               | 6:52:35 AM     | Sat Dec 26 06:37:35 PST 2015  | true              |\n" +
      "Hanetz              | 7:56:16 AM     | Sat Dec 26 07:41:16 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:12:30 AM     | Sat Dec 26 08:57:30 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:45:15 AM    | Sat Dec 26 10:30:15 PST 2015  | true              |\n" +
      "Chatzot             | 12:09:45 PM    | Sat Dec 26 11:54:45 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:30:52 PM    | Sat Dec 26 12:15:52 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:37:37 PM     | Sat Dec 26 14:22:37 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:30:25 PM     | Sat Dec 26 15:15:25 PST 2015  | true              |\n" +
      "Shkia               | 4:23:14 PM     | Sat Dec 26 16:08:14 PST 2015  | true              |\n" +
      "LatestMincha        | 4:32:44 PM     | Sat Dec 26 16:17:44 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:57:37 PM     | Sat Dec 26 16:42:37 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:15:50 PM     | Sat Dec 26 17:00:50 PST 2015  | true              |\n" +
      "ShabbatEnd          | 5:08:14 PM     | Sat Dec 26 16:53:14 PST 2015  | true              |\n" +
      "NightChatzot        | 12:09:53 AM    | Sat Dec 26 23:54:53 PST 2015  | true              |\n" +
      "Alot                | 6:15:34 AM     | Sun Dec 27 06:00:34 PST 2015  | true              |\n" +
      "Talit               | 6:52:53 AM     | Sun Dec 27 06:37:53 PST 2015  | true              |\n" +
      "Hanetz              | 7:56:32 AM     | Sun Dec 27 07:41:32 PST 2015  | true              |\n" +
      "SofZmanShema        | 9:12:54 AM     | Sun Dec 27 08:57:54 PST 2015  | true              |\n" +
      "SofZmanTefila       | 10:45:41 AM    | Sun Dec 27 10:30:41 PST 2015  | true              |\n" +
      "Chatzot             | 12:10:15 PM    | Sun Dec 27 11:55:15 PST 2015  | true              |\n" +
      "MinchaGedola        | 12:31:24 PM    | Sun Dec 27 12:16:24 PST 2015  | true              |\n" +
      "MinchaKetana        | 2:38:15 PM     | Sun Dec 27 14:23:15 PST 2015  | true              |\n" +
      "PlagHaMincha        | 3:31:07 PM     | Sun Dec 27 15:16:07 PST 2015  | true              |\n" +
      "Shkia               | 4:23:58 PM     | Sun Dec 27 16:08:58 PST 2015  | true              |\n" +
      "LatestMincha        | 4:33:29 PM     | Sun Dec 27 16:18:29 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:58:20 PM     | Sun Dec 27 16:43:20 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:16:32 PM     | Sun Dec 27 17:01:32 PST 2015  | true              |\n" +
      "NightChatzot        | 12:10:22 AM    | Sun Dec 27 23:55:22 PST 2015  | true              |\n"
    );
  }


  @Test
  public void pesach2017() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2017, Calendar.APRIL, 10, 6, 34 );
    runDays( testTime, 10, new PesachAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
        "--------------------|----------------|-------------------------------|-------------------|\n" +
        "SofZmanAchilatChametz| 10:57:18 AM    | Mon Apr 10 09:57:18 PDT 2017  | true              |\n" +
        "SofZmanBiurChametz  | 12:04:04 PM    | Mon Apr 10 11:04:04 PDT 2017  | true              |\n" +
        "CandleLighting      | 7:33:27 PM     | Mon Apr 10 19:18:27 PDT 2017  | true              |\n" +
        "FastEnd             | 8:21:27 PM     | Mon Apr 10 20:16:27 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:09:52 AM     | Tue Apr 11 00:54:52 PDT 2017  | true              |\n" +
        "Alot                | 4:49:41 AM     | Tue Apr 11 04:34:41 PDT 2017  | true              |\n" +
        "CandleLighting      | 7:34:51 PM     | Tue Apr 11 19:19:51 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:09:36 AM     | Wed Apr 12 00:54:36 PDT 2017  | true              |\n" +
        "Alot                | 4:47:18 AM     | Wed Apr 12 04:32:18 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:09:21 AM     | Thu Apr 13 00:54:21 PDT 2017  | true              |\n" +
        "Alot                | 4:44:55 AM     | Thu Apr 13 04:29:55 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:09:06 AM     | Fri Apr 14 00:54:06 PDT 2017  | true              |\n" +
        "Alot                | 4:42:32 AM     | Fri Apr 14 04:27:32 PDT 2017  | true              |\n" +
        "CandleLighting      | 7:39:05 PM     | Fri Apr 14 19:24:05 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:08:51 AM     | Sat Apr 15 00:53:51 PDT 2017  | true              |\n" +
        "Alot                | 4:40:08 AM     | Sat Apr 15 04:25:08 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:08:36 AM     | Sun Apr 16 00:53:36 PDT 2017  | true              |\n" +
        "Alot                | 4:37:45 AM     | Sun Apr 16 04:22:45 PDT 2017  | true              |\n" +
        "CandleLighting      | 7:41:54 PM     | Sun Apr 16 19:26:54 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:08:22 AM     | Mon Apr 17 00:53:22 PDT 2017  | true              |\n" +
        "Alot                | 4:35:21 AM     | Mon Apr 17 04:20:21 PDT 2017  | true              |\n" +
        "CandleLighting      | 7:43:18 PM     | Mon Apr 17 19:28:18 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:08:09 AM     | Tue Apr 18 00:53:09 PDT 2017  | true              |\n" +
        "Alot                | 4:32:57 AM     | Tue Apr 18 04:17:57 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:07:55 AM     | Wed Apr 19 00:52:55 PDT 2017  | true              |\n" +
        "Alot                | 4:30:33 AM     | Wed Apr 19 04:15:33 PDT 2017  | true              |\n" +
        "NightChatzot        | 1:07:43 AM     | Thu Apr 20 00:52:43 PDT 2017  | true              |\n"
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
      "SofZmanShema        | 8:07:52 AM     | Thu May 25 07:52:52 PDT 2017  | true              |\n" +
      "SofZmanTefila       | 10:31:11 AM    | Thu May 25 10:16:11 PDT 2017  | true              |\n" +
      "PlagHaMincha        | 7:14:30 PM     | Thu May 25 18:59:30 PDT 2017  | true              |\n" +
      "Shkia               | 8:51:24 PM     | Thu May 25 20:36:24 PDT 2017  | true              |\n" +
      "LatestMincha        | 9:08:51 PM     | Thu May 25 20:53:51 PDT 2017  | true              |\n" +
      "Talit               | 4:07:05 AM     | Fri May 26 03:52:05 PDT 2017  | true              |\n" +
      "SofZmanShema        | 8:06:58 AM     | Fri May 26 07:51:58 PDT 2017  | true              |\n" +
      "SofZmanTefila       | 10:30:57 AM    | Fri May 26 10:15:57 PDT 2017  | true              |\n" +
      "PlagHaMincha        | 7:15:22 PM     | Fri May 26 19:00:22 PDT 2017  | true              |\n" +
      "CandleLighting      | 8:34:29 PM     | Fri May 26 20:19:29 PDT 2017  | true              |\n" +
      "Shkia               | 8:52:29 PM     | Fri May 26 20:37:29 PDT 2017  | true              |\n" +
      "LatestMincha        | 9:09:58 PM     | Fri May 26 20:54:58 PDT 2017  | true              |\n"
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
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
        case Talit:
          return ZmanNotificationScheduler.NO_ALERT;
        case Hanetz:
          return ZmanNotificationScheduler.NO_ALERT;
        case SofZmanShema:
          return ZmanNotificationScheduler.NO_ALERT;
        case SofZmanTefila:
          return ZmanNotificationScheduler.NO_ALERT;
        case SofZmanAchilatChametz:
          return ZmanNotificationScheduler.ONE_HOUR;
        case SofZmanBiurChametz:
          return ZmanNotificationScheduler.ONE_HOUR;
        case Chatzot:
          return ZmanNotificationScheduler.NO_ALERT;
        case MinchaGedola:
          return ZmanNotificationScheduler.NO_ALERT;
        case MinchaKetana:
          return ZmanNotificationScheduler.NO_ALERT;
        case PlagHaMincha:
          return ZmanNotificationScheduler.NO_ALERT;
        case CandleLighting:
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
        case Shkia:
          return ZmanNotificationScheduler.NO_ALERT;
        case BeinHashmashot:
          return ZmanNotificationScheduler.NO_ALERT;
        case LatestMincha:
          return ZmanNotificationScheduler.NO_ALERT;
        case FastEnd:
          return UT_ZmanUtils.FIVE_MINUTES;
        case TzaitHacochavim:
          return ZmanNotificationScheduler.NO_ALERT;
        case ShabbatEnd:
          return ZmanNotificationScheduler.NO_ALERT;
        case NightChatzot:
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      }

      assert false;
      return ZmanNotificationScheduler.NO_ALERT;
    }
  }


} // End of UT_WakeupScheduler_MultiDay class
