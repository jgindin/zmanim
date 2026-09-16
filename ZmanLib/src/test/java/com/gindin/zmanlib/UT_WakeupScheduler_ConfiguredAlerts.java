/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 *
 */
public class UT_WakeupScheduler_ConfiguredAlerts
  extends WakeupSchedulerBase {


  @Test
  public void december_7_2014() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
    runDays( testTime, new OverlappingAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
        "--------------------|----------------|-------------------------------|-------------------|\n" +
        "Talit               | 6:40:05 AM     | Sun Dec 07 06:40:05 PST 2014  | true              |\n" +
        "Shkia               | 4:17:58 PM     | Sun Dec 07 16:02:58 PST 2014  | true              |\n" +
        "LatestMincha        | 4:27:37 PM     | Sun Dec 07 15:57:37 PST 2014  | true              |\n" +
        "BeinHashmashot      | 4:52:04 PM     | Sun Dec 07 15:52:04 PST 2014  | true              |\n" +
        "TzaitHacochavim     | 5:10:00 PM     | Sun Dec 07 16:50:00 PST 2014  | true              |\n"
    );
  }


  @Test
  public void december_21_2015_1_week() {

    december_21_2015_1_week( new OverlappingAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
        "--------------------|----------------|-------------------------------|-------------------|\n" +
        "Talit               | 6:50:23 AM     | Mon Dec 21 06:50:23 PST 2015  | true              |\n" +
        "Shkia               | 4:20:10 PM     | Mon Dec 21 16:05:10 PST 2015  | true              |\n" +
        "LatestMincha        | 4:29:39 PM     | Mon Dec 21 15:59:39 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:54:35 PM     | Mon Dec 21 15:54:35 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:12:51 PM     | Mon Dec 21 16:52:51 PST 2015  | true              |\n" +
        "Talit               | 6:50:53 AM     | Tue Dec 22 06:50:53 PST 2015  | true              |\n" +
        "Shkia               | 4:20:41 PM     | Tue Dec 22 16:05:41 PST 2015  | true              |\n" +
        "LatestMincha        | 4:30:10 PM     | Tue Dec 22 16:00:10 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:55:06 PM     | Tue Dec 22 15:55:06 PST 2015  | true              |\n" +
        "FastEnd             | 4:50:41 PM     | Tue Dec 22 16:35:41 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:13:22 PM     | Tue Dec 22 16:53:22 PST 2015  | true              |\n" +
        "Talit               | 6:51:20 AM     | Wed Dec 23 06:51:20 PST 2015  | true              |\n" +
        "Shkia               | 4:21:14 PM     | Wed Dec 23 16:06:14 PST 2015  | true              |\n" +
        "LatestMincha        | 4:30:43 PM     | Wed Dec 23 16:00:43 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:55:39 PM     | Wed Dec 23 15:55:39 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:13:55 PM     | Wed Dec 23 16:53:55 PST 2015  | true              |\n" +
        "Talit               | 6:51:46 AM     | Thu Dec 24 06:51:46 PST 2015  | true              |\n" +
        "Shkia               | 4:21:50 PM     | Thu Dec 24 16:06:50 PST 2015  | true              |\n" +
        "LatestMincha        | 4:31:20 PM     | Thu Dec 24 16:01:20 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:56:14 PM     | Thu Dec 24 15:56:14 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:14:30 PM     | Thu Dec 24 16:54:30 PST 2015  | true              |\n" +
        "Talit               | 6:52:09 AM     | Fri Dec 25 06:52:09 PST 2015  | true              |\n" +
        "CandleLighting      | 4:04:29 PM     | Fri Dec 25 15:44:29 PST 2015  | true              |\n" +
        "Shkia               | 4:22:29 PM     | Fri Dec 25 16:07:29 PST 2015  | true              |\n" +
        "LatestMincha        | 4:31:59 PM     | Fri Dec 25 16:01:59 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:56:52 PM     | Fri Dec 25 15:56:52 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:15:07 PM     | Fri Dec 25 16:55:07 PST 2015  | true              |\n" +
        "Talit               | 6:52:30 AM     | Sat Dec 26 06:52:30 PST 2015  | true              |\n" +
        "Shkia               | 4:23:10 PM     | Sat Dec 26 16:08:10 PST 2015  | true              |\n" +
        "LatestMincha        | 4:32:40 PM     | Sat Dec 26 16:02:40 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:57:33 PM     | Sat Dec 26 15:57:33 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:15:47 PM     | Sat Dec 26 16:55:47 PST 2015  | true              |\n" +
        "Talit               | 6:52:48 AM     | Sun Dec 27 06:52:48 PST 2015  | true              |\n" +
        "Shkia               | 4:23:54 PM     | Sun Dec 27 16:08:54 PST 2015  | true              |\n" +
        "LatestMincha        | 4:33:25 PM     | Sun Dec 27 16:03:25 PST 2015  | true              |\n" +
        "BeinHashmashot      | 4:58:16 PM     | Sun Dec 27 15:58:16 PST 2015  | true              |\n" +
        "TzaitHacochavim     | 5:16:29 PM     | Sun Dec 27 16:56:29 PST 2015  | true              |\n"
    );
  }


  @Test
  public void shabbatWithAlerts_winter() {

    UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback perTestCallback = new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback() {
      @Override
      public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
        if ( type == Zmanim.Type.BeinHashmashot ) {
          return ZmanNotificationScheduler.NO_ALERT;
        }

        return super.howManyMinutesToAlertBefore( type );
      }
    };


    Calendar testTime = UT_ZmanUtils.configureTestTime( 2015, Calendar.DECEMBER, 25, 0, 1 );
    runDays( testTime, 2, perTestCallback,
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
        + "--------------------|----------------|-------------------------------|-------------------|\n"
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
        + "TzaitHacochavim     | 5:15:47 PM     | Sat Dec 26 17:00:47 PST 2015  | true              |\n"
        + "ShabbatEnd          | 5:08:10 PM     | Sat Dec 26 16:53:10 PST 2015  | true              |\n"
        + "NightChatzot        | 12:09:38 AM    | Sat Dec 26 23:54:38 PST 2015  | true              |\n"
    );
  }


  @Test
   public void shabbatWithAlerts_summer() {

     UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback perTestCallback = new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback() {

       @Override
       public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
         if ( type == Zmanim.Type.BeinHashmashot ) {
           return ZmanNotificationScheduler.NO_ALERT;
         }

         return super.howManyMinutesToAlertBefore( type );
       }
     };


     Calendar testTime = UT_ZmanUtils.configureTestTime( 2017, Calendar.JULY, 21, 0, 1 );
     runDays( testTime, 2, perTestCallback,
       "Zman                | Time           | Action Time                   | Actually Alert?   | \n"
         + "--------------------|----------------|-------------------------------|-------------------|\n"
         + "Alot                | 3:24:58 AM     | Fri Jul 21 03:09:58 PDT 2017  | true              |\n"
         + "Talit               | 4:22:14 AM     | Fri Jul 21 04:07:14 PDT 2017  | true              |\n"
         + "Hanetz              | 5:33:55 AM     | Fri Jul 21 05:18:55 PDT 2017  | true              |\n"
         + "SofZmanShema        | 8:19:49 AM     | Fri Jul 21 08:04:49 PDT 2017  | true              |\n"
         + "SofZmanTefila       | 10:41:25 AM    | Fri Jul 21 10:26:25 PDT 2017  | true              |\n"
         + "Chatzot             | 1:15:30 PM     | Fri Jul 21 13:00:30 PDT 2017  | true              |\n"
         + "MinchaGedola        | 1:53:36 PM     | Fri Jul 21 13:38:36 PDT 2017  | true              |\n"
         + "MinchaKetana        | 5:44:14 PM     | Fri Jul 21 17:29:14 PDT 2017  | true              |\n"
         + "PlagHaMincha        | 7:20:19 PM     | Fri Jul 21 19:05:19 PDT 2017  | true              |\n"
         + "CandleLighting      | 8:38:25 PM     | Fri Jul 21 20:23:25 PDT 2017  | true              |\n"
         + "Shkia               | 8:56:25 PM     | Fri Jul 21 20:41:25 PDT 2017  | true              |\n"
         + "LatestMincha        | 9:13:43 PM     | Fri Jul 21 20:58:43 PDT 2017  | true              |\n"
         + "TzaitHacochavim     | 9:53:37 PM     | Fri Jul 21 21:38:37 PDT 2017  | true              |\n"
         + "NightChatzot        | 1:15:31 AM     | Sat Jul 22 01:00:31 PDT 2017  | true              |\n"
         + "Alot                | 3:27:05 AM     | Sat Jul 22 03:12:05 PDT 2017  | true              |\n"
         + "Talit               | 4:23:45 AM     | Sat Jul 22 04:08:45 PDT 2017  | true              |\n"
         + "Hanetz              | 5:35:03 AM     | Sat Jul 22 05:20:03 PDT 2017  | true              |\n"
         + "SofZmanShema        | 8:20:53 AM     | Sat Jul 22 08:05:53 PDT 2017  | true              |\n"
         + "SofZmanTefila       | 10:41:49 AM    | Sat Jul 22 10:26:49 PDT 2017  | true              |\n"
         + "Chatzot             | 1:15:32 PM     | Sat Jul 22 13:00:32 PDT 2017  | true              |\n"
         + "MinchaGedola        | 1:53:32 PM     | Sat Jul 22 13:38:32 PDT 2017  | true              |\n"
         + "MinchaKetana        | 5:43:36 PM     | Sat Jul 22 17:28:36 PDT 2017  | true              |\n"
         + "PlagHaMincha        | 7:19:28 PM     | Sat Jul 22 19:04:28 PDT 2017  | true              |\n"
         + "Shkia               | 8:55:20 PM     | Sat Jul 22 20:40:20 PDT 2017  | true              |\n"
         + "LatestMincha        | 9:12:35 PM     | Sat Jul 22 20:57:35 PDT 2017  | true              |\n"
         + "TzaitHacochavim     | 9:52:16 PM     | Sat Jul 22 21:37:16 PDT 2017  | true              |\n"
         + "ShabbatEnd          | 9:40:20 PM     | Sat Jul 22 21:25:20 PDT 2017  | true              |\n"
         + "NightChatzot        | 1:15:33 AM     | Sun Jul 23 01:00:33 PDT 2017  | true              |\n"
     );
   }



  private static class OverlappingAlertsZmanCallback
    extends UT_ZmanUtils.ZmanCallbackForTests {

    @Override
    public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {

      switch ( type ) {
        case Alot:
        case Hanetz:
        case SofZmanShema:
        case SofZmanTefila:
        case SofZmanAchilatChametz:
        case SofZmanBiurChametz:
        case Chatzot:
        case MinchaGedola:
        case MinchaKetana:
        case PlagHaMincha:
        case ShabbatEnd:
        case NightChatzot:
          return ZmanNotificationScheduler.NO_ALERT;

        case Talit:
          return UT_ZmanUtils.ZERO_MINUTES;
        case Shkia:
        case FastEnd:
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
        case LatestMincha:
          return UT_ZmanUtils.THIRTY_MINUTES;
        case BeinHashmashot:
          return ZmanNotificationScheduler.ONE_HOUR;
        case TzaitHacochavim:
        case CandleLighting:
        case YomTovSheniCandleLighting:
          return UT_ZmanUtils.TWENTY_MINUTES;
      }

      assert false;
      return ZmanNotificationScheduler.NO_ALERT;
    }
  }

} // End of UT_WakeupScheduler_MultiDay class
