package com.gindin.zmanlib;

import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Test;

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
      "Talit               | 6:39:55 AM     | Sun Dec 07 06:39:55 PST 2014  | true              |\n" +
      "Shkia               | 4:18:00 PM     | Sun Dec 07 16:03:00 PST 2014  | true              |\n" +
      "LatestMincha        | 4:27:39 PM     | Sun Dec 07 15:57:39 PST 2014  | true              |\n" +
      "BeinHashmashot      | 4:52:04 PM     | Sun Dec 07 15:52:04 PST 2014  | true              |\n" +
      "TzaitHacochavim     | 5:10:02 PM     | Sun Dec 07 16:50:02 PST 2014  | true              |\n"
    );
  }


  @Test
  public void december_21_2015_1_week() {

    december_21_2015_1_week( new OverlappingAlertsZmanCallback(),
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
      "Talit               | 6:50:27 AM     | Mon Dec 21 06:50:27 PST 2015  | true              |\n" +
      "Shkia               | 4:20:13 PM     | Mon Dec 21 16:05:13 PST 2015  | true              |\n" +
      "LatestMincha        | 4:29:42 PM     | Mon Dec 21 15:59:42 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:54:37 PM     | Mon Dec 21 15:54:37 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:12:53 PM     | Mon Dec 21 16:52:53 PST 2015  | true              |\n" +
      "Talit               | 6:50:57 AM     | Tue Dec 22 06:50:57 PST 2015  | true              |\n" +
      "Shkia               | 4:20:44 PM     | Tue Dec 22 16:05:44 PST 2015  | true              |\n" +
      "LatestMincha        | 4:30:13 PM     | Tue Dec 22 16:00:13 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:55:08 PM     | Tue Dec 22 15:55:08 PST 2015  | true              |\n" +
      "FastEnd             | 4:50:44 PM     | Tue Dec 22 16:35:44 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:13:24 PM     | Tue Dec 22 16:53:24 PST 2015  | true              |\n" +
      "Talit               | 6:51:25 AM     | Wed Dec 23 06:51:25 PST 2015  | true              |\n" +
      "Shkia               | 4:21:17 PM     | Wed Dec 23 16:06:17 PST 2015  | true              |\n" +
      "LatestMincha        | 4:30:47 PM     | Wed Dec 23 16:00:47 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:55:42 PM     | Wed Dec 23 15:55:42 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:13:57 PM     | Wed Dec 23 16:53:57 PST 2015  | true              |\n" +
      "Talit               | 6:51:50 AM     | Thu Dec 24 06:51:50 PST 2015  | true              |\n" +
      "Shkia               | 4:21:54 PM     | Thu Dec 24 16:06:54 PST 2015  | true              |\n" +
      "LatestMincha        | 4:31:23 PM     | Thu Dec 24 16:01:23 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:56:17 PM     | Thu Dec 24 15:56:17 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:14:32 PM     | Thu Dec 24 16:54:32 PST 2015  | true              |\n" +
      "Talit               | 6:52:14 AM     | Fri Dec 25 06:52:14 PST 2015  | true              |\n" +
      "CandleLighting      | 4:04:33 PM     | Fri Dec 25 15:44:33 PST 2015  | true              |\n" +
      "Shkia               | 4:22:33 PM     | Fri Dec 25 16:07:33 PST 2015  | true              |\n" +
      "LatestMincha        | 4:32:02 PM     | Fri Dec 25 16:02:02 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:56:56 PM     | Fri Dec 25 15:56:56 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:15:10 PM     | Fri Dec 25 16:55:10 PST 2015  | true              |\n" +
      "Talit               | 6:52:35 AM     | Sat Dec 26 06:52:35 PST 2015  | true              |\n" +
      "Shkia               | 4:23:14 PM     | Sat Dec 26 16:08:14 PST 2015  | true              |\n" +
      "LatestMincha        | 4:32:44 PM     | Sat Dec 26 16:02:44 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:57:37 PM     | Sat Dec 26 15:57:37 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:15:50 PM     | Sat Dec 26 16:55:50 PST 2015  | true              |\n" +
      "Talit               | 6:52:53 AM     | Sun Dec 27 06:52:53 PST 2015  | true              |\n" +
      "Shkia               | 4:23:58 PM     | Sun Dec 27 16:08:58 PST 2015  | true              |\n" +
      "LatestMincha        | 4:33:29 PM     | Sun Dec 27 16:03:29 PST 2015  | true              |\n" +
      "BeinHashmashot      | 4:58:20 PM     | Sun Dec 27 15:58:20 PST 2015  | true              |\n" +
      "TzaitHacochavim     | 5:16:32 PM     | Sun Dec 27 16:56:32 PST 2015  | true              |\n"
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
      "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
      "--------------------|----------------|-------------------------------|-------------------|\n" +
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
      "TzaitHacochavim     | 5:15:50 PM     | Sat Dec 26 17:00:50 PST 2015  | true              |\n" +
      "ShabbatEnd          | 5:08:14 PM     | Sat Dec 26 16:53:14 PST 2015  | true              |\n" +
      "NightChatzot        | 12:09:53 AM    | Sat Dec 26 23:54:53 PST 2015  | true              |\n"
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
       "Zman                | Time           | Action Time                   | Actually Alert?   | \n" +
       "--------------------|----------------|-------------------------------|-------------------|\n" +
       "Alot                | 3:24:02 AM     | Fri Jul 21 03:09:02 PDT 2017  | true              |\n" +
       "Talit               | 4:21:29 AM     | Fri Jul 21 04:06:29 PDT 2017  | true              |\n" +
       "Hanetz              | 5:33:16 AM     | Fri Jul 21 05:18:16 PDT 2017  | true              |\n" +
       "SofZmanShema        | 8:19:28 AM     | Fri Jul 21 08:04:28 PDT 2017  | true              |\n" +
       "SofZmanTefila       | 10:41:11 AM    | Fri Jul 21 10:26:11 PDT 2017  | true              |\n" +
       "Chatzot             | 1:15:09 PM     | Fri Jul 21 13:00:09 PDT 2017  | true              |\n" +
       "MinchaGedola        | 1:53:38 PM     | Fri Jul 21 13:38:38 PDT 2017  | true              |\n" +
       "MinchaKetana        | 5:44:35 PM     | Fri Jul 21 17:29:35 PDT 2017  | true              |\n" +
       "PlagHaMincha        | 7:20:48 PM     | Fri Jul 21 19:05:48 PDT 2017  | true              |\n" +
       "CandleLighting      | 8:39:01 PM     | Fri Jul 21 20:24:01 PDT 2017  | true              |\n" +
       "Shkia               | 8:57:01 PM     | Fri Jul 21 20:42:01 PDT 2017  | true              |\n" +
       "LatestMincha        | 9:14:21 PM     | Fri Jul 21 20:59:21 PDT 2017  | true              |\n" +
       "TzaitHacochavim     | 9:54:28 PM     | Fri Jul 21 21:39:28 PDT 2017  | true              |\n" +
       "NightChatzot        | 1:15:43 AM     | Sat Jul 22 01:00:43 PDT 2017  | true              |\n" +
       "Alot                | 3:26:08 AM     | Sat Jul 22 03:11:08 PDT 2017  | true              |\n" +
       "Talit               | 4:22:59 AM     | Sat Jul 22 04:07:59 PDT 2017  | true              |\n" +
       "Hanetz              | 5:34:24 AM     | Sat Jul 22 05:19:24 PDT 2017  | true              |\n" +
       "SofZmanShema        | 8:20:32 AM     | Sat Jul 22 08:05:32 PDT 2017  | true              |\n" +
       "SofZmanTefila       | 10:41:35 AM    | Sat Jul 22 10:26:35 PDT 2017  | true              |\n" +
       "Chatzot             | 1:15:11 PM     | Sat Jul 22 13:00:11 PDT 2017  | true              |\n" +
       "MinchaGedola        | 1:53:35 PM     | Sat Jul 22 13:38:35 PDT 2017  | true              |\n" +
       "MinchaKetana        | 5:43:58 PM     | Sat Jul 22 17:28:58 PDT 2017  | true              |\n" +
       "PlagHaMincha        | 7:19:58 PM     | Sat Jul 22 19:04:58 PDT 2017  | true              |\n" +
       "Shkia               | 8:55:58 PM     | Sat Jul 22 20:40:58 PDT 2017  | true              |\n" +
       "LatestMincha        | 9:13:14 PM     | Sat Jul 22 20:58:14 PDT 2017  | true              |\n" +
       "TzaitHacochavim     | 9:53:08 PM     | Sat Jul 22 21:38:08 PDT 2017  | true              |\n" +
       "ShabbatEnd          | 9:40:58 PM     | Sat Jul 22 21:25:58 PDT 2017  | true              |\n" +
       "NightChatzot        | 1:15:45 AM     | Sun Jul 23 01:00:45 PDT 2017  | true              |\n"
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
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
        case LatestMincha:
          return UT_ZmanUtils.THIRTY_MINUTES;
        case BeinHashmashot:
          return ZmanNotificationScheduler.ONE_HOUR;
        case TzaitHacochavim:
          return UT_ZmanUtils.TWENTY_MINUTES;
        case CandleLighting:
          return UT_ZmanUtils.TWENTY_MINUTES;
        case FastEnd:
          return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      }

      assert false;
      return ZmanNotificationScheduler.NO_ALERT;
    }
  }

} // End of UT_WakeupScheduler_MultiDay class
