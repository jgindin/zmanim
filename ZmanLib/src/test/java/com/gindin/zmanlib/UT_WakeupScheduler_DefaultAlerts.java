package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Test;

import java.util.*;

/**
 *
 */
@SuppressWarnings( "Duplicates" )
public class UT_WakeupScheduler_DefaultAlerts
  extends WakeupSchedulerBase {


  @Test
  public void december_7_2014_15_minute_alerts() {

    TestResultsCallback testResultsCallback = new TestResultsCallback() {
      @Override
      public int scheduleTimeDelta() {
        return 0;
      }


      @Override
      public String getExpectedOutput() {
        return
          "Time              | Action                                        | Next (M:S:ms) |\n" +
          "------------------|-----------------------------------------------|---------------|\n" +
          "12/07/14 06:34:00 | Notify: Talit@6:39:55 AM (5:55:244)           | 30:00:000     |\n" +
          "12/07/14 07:04:00 | Sleep                                         | 23:53:481     |\n" +
          "12/07/14 07:27:53 | Notify: Hanetz@7:42:53 AM (15:0:0)            | 30:00:000     |\n" +
          "12/07/14 07:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 08:27:53 | Sleep                                         | 18:46:642     |\n" +
          "12/07/14 08:46:40 | Notify: SofZmanShema@9:01:40 AM (15:0:0)      | 30:00:000     |\n" +
          "12/07/14 09:16:40 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 09:46:40 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 10:16:40 | Sleep                                         | 02:55:558     |\n" +
          "12/07/14 10:19:35 | Notify: SofZmanTefila@10:34:35 AM (15:0:0)    | 30:00:000     |\n" +
          "12/07/14 10:49:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 11:19:35 | Sleep                                         | 25:51:100     |\n" +
          "12/07/14 11:45:26 | Notify: Chatzot@12:00:26 PM (15:0:0)          | 21:27:775     |\n" +
          "12/07/14 12:06:54 | Notify: MinchaGedola@12:21:54 PM (15:0:0)     | 30:00:000     |\n" +
          "12/07/14 12:36:54 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 13:06:54 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 13:36:54 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 14:06:54 | Sleep                                         | 08:46:650     |\n" +
          "12/07/14 14:15:41 | Notify: MinchaKetana@2:30:41 PM (15:0:0)      | 30:00:000     |\n" +
          "12/07/14 14:45:41 | Sleep                                         | 23:39:437     |\n" +
          "12/07/14 15:09:20 | Notify: PlagHaMincha@3:24:20 PM (15:0:0)      | 30:00:000     |\n" +
          "12/07/14 15:39:20 | Sleep                                         | 23:39:443     |\n" +
          "12/07/14 16:03:00 | Notify: Shkia@4:18:00 PM (15:0:0)             | 09:39:487     |\n" +
          "12/07/14 16:12:39 | Notify: LatestMincha@4:27:39 PM (15:0:0)      | 24:24:788     |\n" +
          "12/07/14 16:37:04 | Notify: BeinHashmashot@4:52:04 PM (15:0:0)    | 17:57:775     |\n" +
          "12/07/14 16:55:02 | Notify: TzaitHacochavim@5:10:02 PM (15:0:0)   | 30:00:000     |\n" +
          "12/07/14 17:25:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 17:55:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 18:25:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 18:55:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 19:25:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 19:55:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 20:25:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 20:55:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 21:25:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 21:55:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 22:25:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 22:55:02 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 23:25:02 | Sleep                                         | 20:56:081     |\n" +
          "12/07/14 23:45:58 | Notify: NightChatzot@12:00:58 AM (15:0:0)     | 30:00:000     |\n";
      }


      @Override
      public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {

        Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
        expectedZmanim.put( date,
          Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot,
            Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        return expectedZmanim;
      }
    };


    Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
    Calendar endTest = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 8, 0, 0 );
    runDays( testTime, endTest, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(), testResultsCallback );
  }


  @Test
  public void dec7_10_2016() {

    UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback perTestCallback = new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback();

    TestResultsCallback testResultsCallback = new TestResultsCallback() {
      @Override
      public int scheduleTimeDelta() {
        return 0;
      }


      @Override
      public String getExpectedOutput() {
        return
          "Time              | Action                                        | Next (M:S:ms) |\n" +
          "------------------|-----------------------------------------------|---------------|\n" +
          "12/07/16 06:34:00 | Notify: Talit@6:40:52 AM (6:52:96)            | 30:00:000     |\n" +
          "12/07/16 07:04:00 | Sleep                                         | 24:56:349     |\n" +
          "12/07/16 07:28:56 | Notify: Hanetz@7:43:56 AM (15:0:0)            | 30:00:000     |\n" +
          "12/07/16 07:58:56 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 08:28:56 | Sleep                                         | 18:24:416     |\n" +
          "12/07/16 08:47:20 | Notify: SofZmanShema@9:02:20 AM (15:0:0)      | 30:00:000     |\n" +
          "12/07/16 09:17:20 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 09:47:20 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 10:17:20 | Sleep                                         | 02:53:716     |\n" +
          "12/07/16 10:20:14 | Notify: SofZmanTefila@10:35:14 AM (15:0:0)    | 30:00:000     |\n" +
          "12/07/16 10:50:14 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 11:20:14 | Sleep                                         | 25:39:066     |\n" +
          "12/07/16 11:45:53 | Notify: Chatzot@12:00:53 PM (15:0:0)          | 21:24:766     |\n" +
          "12/07/16 12:07:18 | Notify: MinchaGedola@12:22:18 PM (15:0:0)     | 30:00:000     |\n" +
          "12/07/16 12:37:18 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 13:07:18 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 13:37:18 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 14:07:18 | Sleep                                         | 08:28:599     |\n" +
          "12/07/16 14:15:46 | Notify: MinchaKetana@2:30:46 PM (15:0:0)      | 30:00:000     |\n" +
          "12/07/16 14:45:46 | Sleep                                         | 23:31:916     |\n" +
          "12/07/16 15:09:18 | Notify: PlagHaMincha@3:24:18 PM (15:0:0)      | 30:00:000     |\n" +
          "12/07/16 15:39:18 | Sleep                                         | 23:31:920     |\n" +
          "12/07/16 16:02:50 | Notify: Shkia@4:17:50 PM (15:0:0)             | 09:38:137     |\n" +
          "12/07/16 16:12:28 | Notify: LatestMincha@4:27:28 PM (15:0:0)      | 24:28:691     |\n" +
          "12/07/16 16:36:57 | Notify: BeinHashmashot@4:51:57 PM (15:0:0)    | 18:00:262     |\n" +
          "12/07/16 16:54:57 | Notify: TzaitHacochavim@5:09:57 PM (15:0:0)   | 30:00:000     |\n" +
          "12/07/16 17:24:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 17:54:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 18:24:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 18:54:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 19:24:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 19:54:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 20:24:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 20:54:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 21:24:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 21:54:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 22:24:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 22:54:57 | Sleep                                         | 30:00:000     |\n" +
          "12/07/16 23:24:57 | Sleep                                         | 21:26:165     |\n" +
          "12/07/16 23:46:24 | Notify: NightChatzot@12:01:24 AM (15:0:0)     | 30:00:000     |\n" +
          "12/08/16 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 05:30:00 | Sleep                                         | 19:39:195     |\n" +
          "12/08/16 05:49:39 | Notify: Alot@6:04:39 AM (15:0:0)              | 30:00:000     |\n" +
          "12/08/16 06:19:39 | Sleep                                         | 07:08:156     |\n" +
          "12/08/16 06:26:47 | Notify: Talit@6:41:47 AM (15:0:0)             | 30:00:000     |\n" +
          "12/08/16 06:56:47 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 07:26:47 | Sleep                                         | 03:09:907     |\n" +
          "12/08/16 07:29:57 | Notify: Hanetz@7:44:57 AM (15:0:0)            | 30:00:000     |\n" +
          "12/08/16 07:59:57 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 08:29:57 | Sleep                                         | 18:03:651     |\n" +
          "12/08/16 08:48:00 | Notify: SofZmanShema@9:03:00 AM (15:0:0)      | 30:00:000     |\n" +
          "12/08/16 09:18:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 09:48:00 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 10:18:00 | Sleep                                         | 02:52:033     |\n" +
          "12/08/16 10:20:52 | Notify: SofZmanTefila@10:35:52 AM (15:0:0)    | 30:00:000     |\n" +
          "12/08/16 10:50:52 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 11:20:52 | Sleep                                         | 25:27:842     |\n" +
          "12/08/16 11:46:20 | Notify: Chatzot@12:01:20 PM (15:0:0)          | 21:21:960     |\n" +
          "12/08/16 12:07:42 | Notify: MinchaGedola@12:22:42 PM (15:0:0)     | 30:00:000     |\n" +
          "12/08/16 12:37:42 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 13:07:42 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 13:37:42 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 14:07:42 | Sleep                                         | 08:11:763     |\n" +
          "12/08/16 14:15:54 | Notify: MinchaKetana@2:30:54 PM (15:0:0)      | 30:00:000     |\n" +
          "12/08/16 14:45:54 | Sleep                                         | 23:24:901     |\n" +
          "12/08/16 15:09:19 | Notify: PlagHaMincha@3:24:19 PM (15:0:0)      | 30:00:000     |\n" +
          "12/08/16 15:39:19 | Sleep                                         | 23:24:905     |\n" +
          "12/08/16 16:02:44 | Notify: Shkia@4:17:44 PM (15:0:0)             | 09:36:882     |\n" +
          "12/08/16 16:12:21 | Notify: LatestMincha@4:27:21 PM (15:0:0)      | 24:32:348     |\n" +
          "12/08/16 16:36:53 | Notify: BeinHashmashot@4:51:53 PM (15:0:0)    | 18:02:584     |\n" +
          "12/08/16 16:54:56 | Notify: TzaitHacochavim@5:09:56 PM (15:0:0)   | 30:00:000     |\n" +
          "12/08/16 17:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 17:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 18:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 18:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 19:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 19:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 20:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 20:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 21:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 21:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 22:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 22:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/08/16 23:24:56 | Sleep                                         | 21:54:101     |\n" +
          "12/08/16 23:46:50 | Notify: NightChatzot@12:01:50 AM (15:0:0)     | 30:00:000     |\n" +
          "12/09/16 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 05:30:00 | Sleep                                         | 20:30:856     |\n" +
          "12/09/16 05:50:30 | Notify: Alot@6:05:30 AM (15:0:0)              | 30:00:000     |\n" +
          "12/09/16 06:20:30 | Sleep                                         | 07:10:098     |\n" +
          "12/09/16 06:27:40 | Notify: Talit@6:42:40 AM (15:0:0)             | 30:00:000     |\n" +
          "12/09/16 06:57:40 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 07:27:40 | Sleep                                         | 03:15:189     |\n" +
          "12/09/16 07:30:56 | Notify: Hanetz@7:45:56 AM (15:0:0)            | 30:00:000     |\n" +
          "12/09/16 08:00:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 08:30:56 | Sleep                                         | 17:44:370     |\n" +
          "12/09/16 08:48:40 | Notify: SofZmanShema@9:03:40 AM (15:0:0)      | 30:00:000     |\n" +
          "12/09/16 09:18:40 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 09:48:40 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 10:18:40 | Sleep                                         | 02:50:510     |\n" +
          "12/09/16 10:21:31 | Notify: SofZmanTefila@10:36:31 AM (15:0:0)    | 30:00:000     |\n" +
          "12/09/16 10:51:31 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 11:21:31 | Sleep                                         | 25:17:440     |\n" +
          "12/09/16 11:46:48 | Notify: Chatzot@12:01:48 PM (15:0:0)          | 21:19:360     |\n" +
          "12/09/16 12:08:07 | Notify: MinchaGedola@12:23:07 PM (15:0:0)     | 30:00:000     |\n" +
          "12/09/16 12:38:07 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 13:08:07 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 13:38:07 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 14:08:07 | Sleep                                         | 07:56:160     |\n" +
          "12/09/16 14:16:03 | Notify: MinchaKetana@2:31:03 PM (15:0:0)      | 30:00:000     |\n" +
          "12/09/16 14:46:03 | Sleep                                         | 23:18:400     |\n" +
          "12/09/16 15:09:22 | Notify: PlagHaMincha@3:24:22 PM (15:0:0)      | 30:00:000     |\n" +
          "12/09/16 15:39:22 | Sleep                                         | 05:18:406     |\n" +
          "12/09/16 15:44:40 | Notify: CandleLighting@3:59:40 PM (15:0:0)    | 18:00:000     |\n" +
          "12/09/16 16:02:40 | Notify: Shkia@4:17:40 PM (15:0:0)             | 09:35:707     |\n" +
          "12/09/16 16:12:16 | Notify: LatestMincha@4:27:16 PM (15:0:0)      | 24:35:768     |\n" +
          "12/09/16 16:36:52 | Notify: BeinHashmashot@4:51:52 PM (15:0:0)    | 18:04:735     |\n" +
          "12/09/16 16:54:56 | Notify: TzaitHacochavim@5:09:56 PM (15:0:0)   | 30:00:000     |\n" +
          "12/09/16 17:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 17:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 18:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 18:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 19:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 19:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 20:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 20:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 21:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 21:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 22:24:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 22:54:56 | Sleep                                         | 30:00:000     |\n" +
          "12/09/16 23:24:56 | Sleep                                         | 22:19:865     |\n" +
          "12/09/16 23:47:16 | Notify: NightChatzot@12:02:16 AM (15:0:0)     | 30:00:000     |\n";
      }


      @Override
      public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {
        Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
        expectedZmanim.put( date,
          Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola,
            Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim,
            Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 1 ),
          Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola,
            Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim,
            Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 2 ),
          Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola,
            Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim,
            Zmanim.Type.NightChatzot )
        );

        return expectedZmanim;
      }
    };

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 7, 6, 34 );
    Calendar endTest = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 10, 0, 0 );
    runDays( testTime, endTest, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(), testResultsCallback );
  }


  /**
   * This test is supposed to produce the EXACT same notifications as the dec7_10_2016() test, except
   *  that we're going to add some variability to when the wakeup is scheduled.
   */
  @Test
   public void dec7_10_2016_early_late_wakeup() {

     UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback perTestCallback = new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback();

     TestResultsCallback testResultsCallback = new TestResultsCallback() {
       private int                                          numCalls = 0;

       @Override
       public int scheduleTimeDelta() {

         numCalls++;

         if ( numCalls % 3 == 0 ) {
           return 1000;
         }
         else if ( numCalls % 7 == 0 ) {
           return -35000;
         }
         else if ( numCalls % 5 == 0 ) {
           return -20000;
         }
         else if ( numCalls % 2 == 0 ) {
           return 25000;
         }

         return 0;
       }


       @Override
       public String getExpectedOutput() {
         return
           "Time              | Action                                        | Next (M:S:ms) |\n" +
           "------------------|-----------------------------------------------|---------------|\n" +
           "12/07/16 06:34:00 | Notify: Talit@6:40:52 AM (6:52:96)            | 30:00:000     |\n" +
           "12/07/16 07:04:00 | Sleep                                         | 24:56:349     |\n" +
           "12/07/16 07:29:21 | Notify: Hanetz@7:43:56 AM (14:35:0)           | 30:00:000     |\n" +
           "12/07/16 07:59:22 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 08:29:47 | Sleep                                         | 17:33:416     |\n" +
           "12/07/16 08:47:00 | Notify: SofZmanShema@9:02:20 AM (15:20:0)     | 30:00:000     |\n" +
           "12/07/16 09:17:01 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 09:46:26 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 10:16:51 | Sleep                                         | 03:22:716     |\n" +
           "12/07/16 10:20:15 | Notify: SofZmanTefila@10:35:14 AM (14:59:0)   | 30:00:000     |\n" +
           "12/07/16 10:49:55 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 11:19:55 | Sleep                                         | 25:58:066     |\n" +
           "12/07/16 11:45:54 | Notify: Chatzot@12:00:53 PM (14:59:0)         | 21:23:766     |\n" +
           "12/07/16 12:07:18 | Notify: MinchaGedola@12:22:18 PM (15:0:0)     | 30:00:000     |\n" +
           "12/07/16 12:36:43 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 13:06:44 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 13:37:09 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 14:07:09 | Sleep                                         | 08:37:599     |\n" +
           "12/07/16 14:15:47 | Notify: MinchaKetana@2:30:46 PM (14:59:0)     | 30:00:000     |\n" +
           "12/07/16 14:45:47 | Sleep                                         | 23:30:916     |\n" +
           "12/07/16 15:08:58 | Notify: PlagHaMincha@3:24:18 PM (15:20:0)     | 30:00:000     |\n" +
           "12/07/16 15:38:59 | Sleep                                         | 23:50:920     |\n" +
           "12/07/16 16:03:15 | Notify: Shkia@4:17:50 PM (14:35:0)            | 09:13:137     |\n" +
           "12/07/16 16:12:28 | Notify: LatestMincha@4:27:28 PM (15:0:0)      | 24:28:691     |\n" +
           "12/07/16 16:36:58 | Notify: BeinHashmashot@4:51:57 PM (14:59:0)   | 17:59:262     |\n" +
           "12/07/16 16:54:37 | Notify: TzaitHacochavim@5:09:57 PM (15:20:0)  | 30:00:000     |\n" +
           "12/07/16 17:25:02 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 17:55:03 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 18:24:28 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 18:54:28 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 19:24:29 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 19:54:29 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 20:24:54 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 20:54:55 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 21:25:20 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 21:54:45 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 22:24:46 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 22:54:46 | Sleep                                         | 30:00:000     |\n" +
           "12/07/16 23:25:11 | Sleep                                         | 21:12:165     |\n" +
           "12/07/16 23:46:25 | Notify: NightChatzot@12:01:24 AM (14:59:0)    | 30:00:000     |\n" +
           "12/08/16 00:29:40 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 00:59:40 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 01:29:41 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 01:59:41 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 02:30:06 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 03:00:07 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 03:30:32 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 04:00:32 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 04:30:33 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 04:59:58 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 05:29:38 | Sleep                                         | 20:01:195     |\n" +
           "12/08/16 05:49:40 | Notify: Alot@6:04:39 AM (14:59:0)             | 30:00:000     |\n" +
           "12/08/16 06:20:05 | Sleep                                         | 06:42:156     |\n" +
           "12/08/16 06:26:47 | Notify: Talit@6:41:47 AM (15:0:0)             | 30:00:000     |\n" +
           "12/08/16 06:56:48 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 07:26:28 | Sleep                                         | 03:28:907     |\n" +
           "12/08/16 07:29:22 | Sleep                                         | 00:35:000     |\n" +
           "12/08/16 07:29:58 | Notify: Hanetz@7:44:57 AM (14:59:0)           | 30:00:000     |\n" +
           "12/08/16 08:00:23 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 08:30:23 | Sleep                                         | 17:37:651     |\n" +
           "12/08/16 08:48:01 | Notify: SofZmanShema@9:03:00 AM (14:59:0)     | 30:00:000     |\n" +
           "12/08/16 09:18:01 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 09:48:26 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 10:18:27 | Sleep                                         | 02:25:033     |\n" +
           "12/08/16 10:21:17 | Notify: SofZmanTefila@10:35:52 AM (14:35:0)   | 30:00:000     |\n" +
           "12/08/16 10:50:57 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 11:20:58 | Sleep                                         | 25:21:842     |\n" +
           "12/08/16 11:46:20 | Notify: Chatzot@12:01:20 PM (15:0:0)          | 21:21:960     |\n" +
           "12/08/16 12:08:07 | Notify: MinchaGedola@12:22:42 PM (14:35:0)    | 30:00:000     |\n" +
           "12/08/16 12:38:08 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 13:07:33 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 13:37:33 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 14:07:34 | Sleep                                         | 08:19:763     |\n" +
           "12/08/16 14:15:54 | Notify: MinchaKetana@2:30:54 PM (15:0:0)      | 30:00:000     |\n" +
           "12/08/16 14:46:19 | Sleep                                         | 22:59:901     |\n" +
           "12/08/16 15:09:20 | Notify: PlagHaMincha@3:24:19 PM (14:59:0)     | 30:00:000     |\n" +
           "12/08/16 15:39:45 | Sleep                                         | 22:58:905     |\n" +
           "12/08/16 16:02:09 | Sleep                                         | 00:35:000     |\n" +
           "12/08/16 16:02:45 | Notify: Shkia@4:17:44 PM (14:59:0)            | 09:35:882     |\n" +
           "12/08/16 16:12:21 | Notify: LatestMincha@4:27:21 PM (15:0:0)      | 24:32:348     |\n" +
           "12/08/16 16:36:33 | Notify: BeinHashmashot@4:51:53 PM (15:20:0)   | 18:22:584     |\n" +
           "12/08/16 16:54:57 | Notify: TzaitHacochavim@5:09:56 PM (14:59:0)  | 30:00:000     |\n" +
           "12/08/16 17:25:22 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 17:55:22 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 18:25:23 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 18:55:03 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 19:25:28 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 19:55:29 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 20:25:54 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 20:55:54 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 21:25:55 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 21:55:20 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 22:25:45 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 22:55:46 | Sleep                                         | 30:00:000     |\n" +
           "12/08/16 23:26:11 | Sleep                                         | 20:39:101     |\n" +
           "12/08/16 23:46:30 | Notify: NightChatzot@12:01:50 AM (15:20:0)    | 30:00:000     |\n" +
           "12/09/16 00:30:01 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 01:00:01 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 01:29:26 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 01:59:27 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 02:29:07 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 02:59:07 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 03:29:08 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 03:59:08 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 04:29:33 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 04:59:34 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 05:29:59 | Sleep                                         | 20:31:856     |\n" +
           "12/09/16 05:50:30 | Notify: Alot@6:05:30 AM (15:0:0)              | 30:00:000     |\n" +
           "12/09/16 06:20:31 | Sleep                                         | 07:09:098     |\n" +
           "12/09/16 06:27:40 | Notify: Talit@6:42:40 AM (15:0:0)             | 30:00:000     |\n" +
           "12/09/16 06:57:20 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 07:27:21 | Sleep                                         | 03:34:189     |\n" +
           "12/09/16 07:30:21 | Sleep                                         | 00:35:000     |\n" +
           "12/09/16 07:30:56 | Notify: Hanetz@7:45:56 AM (15:0:0)            | 30:00:000     |\n" +
           "12/09/16 08:00:57 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 08:30:37 | Sleep                                         | 18:03:370     |\n" +
           "12/09/16 08:49:05 | Notify: SofZmanShema@9:03:40 AM (14:35:0)     | 30:00:000     |\n" +
           "12/09/16 09:19:06 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 09:49:31 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 10:18:56 | Sleep                                         | 02:34:510     |\n" +
           "12/09/16 10:21:32 | Notify: SofZmanTefila@10:36:31 AM (14:59:0)   | 30:00:000     |\n" +
           "12/09/16 10:51:32 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 11:21:57 | Sleep                                         | 24:51:440     |\n" +
           "12/09/16 11:46:49 | Notify: Chatzot@12:01:48 PM (14:59:0)         | 21:18:360     |\n" +
           "12/09/16 12:08:32 | Notify: MinchaGedola@12:23:07 PM (14:35:0)    | 30:00:000     |\n" +
           "12/09/16 12:38:12 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 13:08:13 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 13:38:13 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 14:08:38 | Sleep                                         | 07:25:160     |\n" +
           "12/09/16 14:16:04 | Notify: MinchaKetana@2:31:03 PM (14:59:0)     | 30:00:000     |\n" +
           "12/09/16 14:45:44 | Sleep                                         | 23:37:400     |\n" +
           "12/09/16 15:09:22 | Notify: PlagHaMincha@3:24:22 PM (15:0:0)      | 30:00:000     |\n" +
           "12/09/16 15:39:23 | Sleep                                         | 05:17:406     |\n" +
           "12/09/16 15:44:05 | Sleep                                         | 00:35:000     |\n" +
           "12/09/16 15:45:05 | Notify: CandleLighting@3:59:40 PM (14:35:0)   | 17:35:000     |\n" +
           "12/09/16 16:02:41 | Notify: Shkia@4:17:40 PM (14:59:0)            | 09:34:707     |\n" +
           "12/09/16 16:12:41 | Notify: LatestMincha@4:27:16 PM (14:35:0)     | 24:10:768     |\n" +
           "12/09/16 16:36:52 | Notify: BeinHashmashot@4:51:52 PM (15:0:0)    | 18:04:735     |\n" +
           "12/09/16 16:54:57 | Notify: TzaitHacochavim@5:09:56 PM (14:59:0)  | 30:00:000     |\n" +
           "12/09/16 17:24:57 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 17:54:22 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 18:24:23 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 18:54:48 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 19:24:48 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 19:54:49 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 20:24:29 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 20:54:54 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 21:24:55 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 21:55:20 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 22:25:20 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 22:55:21 | Sleep                                         | 30:00:000     |\n" +
           "12/09/16 23:25:21 | Sleep                                         | 21:54:865     |\n" +
           "12/09/16 23:47:41 | Notify: NightChatzot@12:02:16 AM (14:35:0)    | 30:00:000     |\n";
       }


       @Override
       public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {
         Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
         expectedZmanim.put( date,
           Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
             Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola,
             Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
             Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim,
             Zmanim.Type.NightChatzot )
         );

         expectedZmanim.put( date.forward( 1 ),
           Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
             Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola,
             Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
             Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim,
             Zmanim.Type.NightChatzot )
         );

         expectedZmanim.put( date.forward( 2 ),
           Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
             Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola,
             Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
             Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim,
             Zmanim.Type.NightChatzot )
         );

         return expectedZmanim;
       }
     };

     Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 7, 6, 34 );
     Calendar endTest = UT_ZmanUtils.configureTestTime( 2016, Calendar.DECEMBER, 10, 0, 0 );
     runDays( testTime, endTest, new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(), testResultsCallback );
   }



  @Test
  public void december_7_2014_default_product_alerts() {

    TestResultsCallback testResultsCallback = new TestResultsCallback() {
      @Override
      public int scheduleTimeDelta() {
        return 0;
      }


      @Override
      public String getExpectedOutput() {
        return
          "Time              | Action                                        | Next (M:S:ms) |\n" +
          "------------------|-----------------------------------------------|---------------|\n" +
          "12/07/14 06:34:00 | Notify: Talit@6:39:55 AM (5:55:244)           | 30:00:000     |\n" +
          "12/07/14 07:04:00 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 07:34:00 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 08:04:00 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 08:34:00 | Sleep                                         | 12:40:123     |\n" +
          "12/07/14 08:46:40 | Notify: SofZmanShema@9:01:40 AM (15:0:0)      | 30:00:000     |\n" +
          "12/07/14 09:16:40 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 09:46:40 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 10:16:40 | Sleep                                         | 02:55:558     |\n" +
          "12/07/14 10:19:35 | Notify: SofZmanTefila@10:34:35 AM (15:0:0)    | 30:00:000     |\n" +
          "12/07/14 10:49:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 11:19:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 11:49:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 12:19:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 12:49:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 13:19:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 13:49:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 14:19:35 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 14:49:35 | Sleep                                         | 19:44:962     |\n" +
          "12/07/14 15:09:20 | Notify: PlagHaMincha@3:24:20 PM (15:0:0)      | 30:00:000     |\n" +
          "12/07/14 15:39:20 | Sleep                                         | 23:39:443     |\n" +
          "12/07/14 16:03:00 | Notify: Shkia@4:18:00 PM (15:0:0)             | 09:39:487     |\n" +
          "12/07/14 16:12:39 | Notify: LatestMincha@4:27:39 PM (15:0:0)      | 30:00:000     |\n" +
          "12/07/14 16:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 17:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 17:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 18:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 18:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 19:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 19:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 20:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 20:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 21:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 21:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 22:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 22:42:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 23:12:39 | Sleep                                         | 30:00:000     |\n" +
          "12/07/14 23:42:39 | Sleep                                         | 30:00:000     |\n"
          ;
      }


      @Override
      public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {
        Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
        expectedZmanim.put( date,
          Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha )
        );

        return expectedZmanim;
      }
    };


    Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
    Calendar endTest = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 8, 0, 0 );
    runDays( testTime, endTest, new DefaultAlertsZmanCallback(), testResultsCallback );
  }


  @Test
  public void december_21_2015_1_week() {

    TestResultsCallback testResultsCallback = new TestResultsCallback() {
      @Override
      public int scheduleTimeDelta() {
        return 0;
      }


      @Override
      public String getExpectedOutput() {
        return
          "Time              | Action                                        | Next (M:S:ms) |\n" +
          "------------------|-----------------------------------------------|---------------|\n" +
          "12/21/15 00:01:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 00:31:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 01:01:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 01:31:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 02:01:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 02:31:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 03:01:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 03:31:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 04:01:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 04:31:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 05:01:00 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 05:31:00 | Sleep                                         | 27:06:204     |\n" +
          "12/21/15 05:58:06 | Notify: Alot@6:13:06 AM (15:0:0)              | 30:00:000     |\n" +
          "12/21/15 06:28:06 | Sleep                                         | 07:21:279     |\n" +
          "12/21/15 06:35:27 | Notify: Talit@6:50:27 AM (15:0:0)             | 30:00:000     |\n" +
          "12/21/15 07:05:27 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 07:35:27 | Sleep                                         | 03:45:549     |\n" +
          "12/21/15 07:39:13 | Notify: Hanetz@7:54:13 AM (15:0:0)            | 30:00:000     |\n" +
          "12/21/15 08:09:13 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 08:39:13 | Sleep                                         | 15:56:685     |\n" +
          "12/21/15 08:55:09 | Notify: SofZmanShema@9:10:09 AM (15:0:0)      | 30:00:000     |\n" +
          "12/21/15 09:25:09 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 09:55:09 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 10:25:09 | Sleep                                         | 02:43:411     |\n" +
          "12/21/15 10:27:53 | Notify: SofZmanTefila@10:42:53 AM (15:0:0)    | 30:00:000     |\n" +
          "12/21/15 10:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 11:27:53 | Sleep                                         | 24:20:048     |\n" +
          "12/21/15 11:52:13 | Notify: Chatzot@12:07:13 PM (15:0:0)          | 21:05:012     |\n" +
          "12/21/15 12:13:18 | Notify: MinchaGedola@12:28:18 PM (15:0:0)     | 30:00:000     |\n" +
          "12/21/15 12:43:18 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 13:13:18 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 13:43:18 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 14:13:18 | Sleep                                         | 06:30:072     |\n" +
          "12/21/15 14:19:48 | Notify: MinchaKetana@2:34:48 PM (15:0:0)      | 30:00:000     |\n" +
          "12/21/15 14:49:48 | Sleep                                         | 22:42:530     |\n" +
          "12/21/15 15:12:30 | Notify: PlagHaMincha@3:27:30 PM (15:0:0)      | 30:00:000     |\n" +
          "12/21/15 15:42:30 | Sleep                                         | 22:42:532     |\n" +
          "12/21/15 16:05:13 | Notify: Shkia@4:20:13 PM (15:0:0)             | 09:29:254     |\n" +
          "12/21/15 16:14:42 | Notify: LatestMincha@4:29:42 PM (15:0:0)      | 24:55:135     |\n" +
          "12/21/15 16:39:37 | Notify: BeinHashmashot@4:54:37 PM (15:0:0)    | 18:15:957     |\n" +
          "12/21/15 16:57:53 | Notify: TzaitHacochavim@5:12:53 PM (15:0:0)   | 30:00:000     |\n" +
          "12/21/15 17:27:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 17:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 18:27:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 18:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 19:27:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 19:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 20:27:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 20:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 21:27:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 21:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 22:27:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 22:57:53 | Sleep                                         | 30:00:000     |\n" +
          "12/21/15 23:27:53 | Sleep                                         | 24:34:557     |\n" +
          "12/21/15 23:52:28 | Notify: NightChatzot@12:07:28 AM (15:0:0)     | 30:00:000     |\n" +
          "12/22/15 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 05:30:00 | Sleep                                         | 28:36:232     |\n" +
          "12/22/15 05:58:36 | Notify: Alot@6:13:36 AM (15:0:0)              | 30:00:000     |\n" +
          "12/22/15 06:28:36 | Sleep                                         | 07:21:297     |\n" +
          "12/22/15 06:35:57 | Notify: Talit@6:50:57 AM (15:0:0)             | 30:00:000     |\n" +
          "12/22/15 07:05:57 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 07:35:57 | Sleep                                         | 03:45:599     |\n" +
          "12/22/15 07:39:43 | Notify: Hanetz@7:54:43 AM (15:0:0)            | 30:00:000     |\n" +
          "12/22/15 08:09:43 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 08:39:43 | Sleep                                         | 15:56:767     |\n" +
          "12/22/15 08:55:39 | Notify: SofZmanShema@9:10:39 AM (15:0:0)      | 30:00:000     |\n" +
          "12/22/15 09:25:39 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 09:55:39 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 10:25:39 | Sleep                                         | 02:43:589     |\n" +
          "12/22/15 10:28:23 | Notify: SofZmanTefila@10:43:23 AM (15:0:0)    | 30:00:000     |\n" +
          "12/22/15 10:58:23 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 11:28:23 | Sleep                                         | 24:20:178     |\n" +
          "12/22/15 11:52:43 | Notify: Chatzot@12:07:43 PM (15:0:0)          | 21:05:044     |\n" +
          "12/22/15 12:13:48 | Notify: MinchaGedola@12:28:48 PM (15:0:0)     | 30:00:000     |\n" +
          "12/22/15 12:43:48 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 13:13:48 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 13:43:48 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 14:13:48 | Sleep                                         | 06:30:267     |\n" +
          "12/22/15 14:20:18 | Notify: MinchaKetana@2:35:18 PM (15:0:0)      | 30:00:000     |\n" +
          "12/22/15 14:50:18 | Sleep                                         | 22:42:611     |\n" +
          "12/22/15 15:13:01 | Notify: PlagHaMincha@3:28:01 PM (15:0:0)      | 30:00:000     |\n" +
          "12/22/15 15:43:01 | Sleep                                         | 22:42:619     |\n" +
          "12/22/15 16:05:44 | Notify: Shkia@4:20:44 PM (15:0:0)             | 09:29:268     |\n" +
          "12/22/15 16:15:13 | Notify: LatestMincha@4:30:13 PM (15:0:0)      | 20:30:732     |\n" +
          "12/22/15 16:35:44 | Notify: FastEnd@4:50:44 PM (15:0:0)           | 04:24:411     |\n" +
          "12/22/15 16:40:08 | Notify: BeinHashmashot@4:55:08 PM (15:0:0)    | 18:15:778     |\n" +
          "12/22/15 16:58:24 | Notify: TzaitHacochavim@5:13:24 PM (15:0:0)   | 30:00:000     |\n" +
          "12/22/15 17:28:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 17:58:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 18:28:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 18:58:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 19:28:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 19:58:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 20:28:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 20:58:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 21:28:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 21:58:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 22:28:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 22:58:24 | Sleep                                         | 30:00:000     |\n" +
          "12/22/15 23:28:24 | Sleep                                         | 24:32:972     |\n" +
          "12/22/15 23:52:57 | Notify: NightChatzot@12:07:57 AM (15:0:0)     | 30:00:000     |\n" +
          "12/23/15 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 05:30:00 | Sleep                                         | 29:04:198     |\n" +
          "12/23/15 05:59:04 | Notify: Alot@6:14:04 AM (15:0:0)              | 30:00:000     |\n" +
          "12/23/15 06:29:04 | Sleep                                         | 07:21:144     |\n" +
          "12/23/15 06:36:25 | Notify: Talit@6:51:25 AM (15:0:0)             | 30:00:000     |\n" +
          "12/23/15 07:06:25 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 07:36:25 | Sleep                                         | 03:45:184     |\n" +
          "12/23/15 07:40:10 | Notify: Hanetz@7:55:10 AM (15:0:0)            | 30:00:000     |\n" +
          "12/23/15 08:10:10 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 08:40:10 | Sleep                                         | 15:58:523     |\n" +
          "12/23/15 08:56:09 | Notify: SofZmanShema@9:11:09 AM (15:0:0)      | 30:00:000     |\n" +
          "12/23/15 09:26:09 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 09:56:09 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 10:26:09 | Sleep                                         | 02:43:901     |\n" +
          "12/23/15 10:28:52 | Notify: SofZmanTefila@10:43:52 AM (15:0:0)    | 30:00:000     |\n" +
          "12/23/15 10:58:52 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 11:28:52 | Sleep                                         | 24:21:212     |\n" +
          "12/23/15 11:53:14 | Notify: Chatzot@12:08:14 PM (15:0:0)          | 21:05:303     |\n" +
          "12/23/15 12:14:19 | Notify: MinchaGedola@12:29:19 PM (15:0:0)     | 30:00:000     |\n" +
          "12/23/15 12:44:19 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 13:14:19 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 13:44:19 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 14:14:19 | Sleep                                         | 06:31:818     |\n" +
          "12/23/15 14:20:51 | Notify: MinchaKetana@2:35:51 PM (15:0:0)      | 30:00:000     |\n" +
          "12/23/15 14:50:51 | Sleep                                         | 22:43:257     |\n" +
          "12/23/15 15:13:34 | Notify: PlagHaMincha@3:28:34 PM (15:0:0)      | 30:00:000     |\n" +
          "12/23/15 15:43:34 | Sleep                                         | 22:43:266     |\n" +
          "12/23/15 16:06:17 | Notify: Shkia@4:21:17 PM (15:0:0)             | 09:29:376     |\n" +
          "12/23/15 16:15:47 | Notify: LatestMincha@4:30:47 PM (15:0:0)      | 24:54:858     |\n" +
          "12/23/15 16:40:42 | Notify: BeinHashmashot@4:55:42 PM (15:0:0)    | 18:15:395     |\n" +
          "12/23/15 16:58:57 | Notify: TzaitHacochavim@5:13:57 PM (15:0:0)   | 30:00:000     |\n" +
          "12/23/15 17:28:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 17:58:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 18:28:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 18:58:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 19:28:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 19:58:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 20:28:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 20:58:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 21:28:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 21:58:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 22:28:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 22:58:57 | Sleep                                         | 30:00:000     |\n" +
          "12/23/15 23:28:57 | Sleep                                         | 24:29:064     |\n" +
          "12/23/15 23:53:26 | Notify: NightChatzot@12:08:26 AM (15:0:0)     | 30:00:000     |\n" +
          "12/24/15 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 05:30:00 | Sleep                                         | 29:30:068     |\n" +
          "12/24/15 05:59:30 | Notify: Alot@6:14:30 AM (15:0:0)              | 30:00:000     |\n" +
          "12/24/15 06:29:30 | Sleep                                         | 07:20:820     |\n" +
          "12/24/15 06:36:50 | Notify: Talit@6:51:50 AM (15:0:0)             | 30:00:000     |\n" +
          "12/24/15 07:06:50 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 07:36:50 | Sleep                                         | 03:44:304     |\n" +
          "12/24/15 07:40:35 | Notify: Hanetz@7:55:35 AM (15:0:0)            | 30:00:000     |\n" +
          "12/24/15 08:10:35 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 08:40:35 | Sleep                                         | 16:01:953     |\n" +
          "12/24/15 08:56:37 | Notify: SofZmanShema@9:11:37 AM (15:0:0)      | 30:00:000     |\n" +
          "12/24/15 09:26:37 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 09:56:37 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 10:26:37 | Sleep                                         | 02:44:347     |\n" +
          "12/24/15 10:29:21 | Notify: SofZmanTefila@10:44:21 AM (15:0:0)    | 30:00:000     |\n" +
          "12/24/15 10:59:21 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 11:29:21 | Sleep                                         | 24:23:150     |\n" +
          "12/24/15 11:53:44 | Notify: Chatzot@12:08:44 PM (15:0:0)          | 21:05:787     |\n" +
          "12/24/15 12:14:50 | Notify: MinchaGedola@12:29:50 PM (15:0:0)     | 30:00:000     |\n" +
          "12/24/15 12:44:50 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 13:14:50 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 13:44:50 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 14:14:50 | Sleep                                         | 06:34:725     |\n" +
          "12/24/15 14:21:25 | Notify: MinchaKetana@2:36:25 PM (15:0:0)      | 30:00:000     |\n" +
          "12/24/15 14:51:25 | Sleep                                         | 22:44:469     |\n" +
          "12/24/15 15:14:09 | Notify: PlagHaMincha@3:29:09 PM (15:0:0)      | 30:00:000     |\n" +
          "12/24/15 15:44:09 | Sleep                                         | 22:44:471     |\n" +
          "12/24/15 16:06:54 | Notify: Shkia@4:21:54 PM (15:0:0)             | 09:29:592     |\n" +
          "12/24/15 16:16:23 | Notify: LatestMincha@4:31:23 PM (15:0:0)      | 24:54:267     |\n" +
          "12/24/15 16:41:17 | Notify: BeinHashmashot@4:56:17 PM (15:0:0)    | 18:14:810     |\n" +
          "12/24/15 16:59:32 | Notify: TzaitHacochavim@5:14:32 PM (15:0:0)   | 30:00:000     |\n" +
          "12/24/15 17:29:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 17:59:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 18:29:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 18:59:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 19:29:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 19:59:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 20:29:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 20:59:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 21:29:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 21:59:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 22:29:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 22:59:32 | Sleep                                         | 30:00:000     |\n" +
          "12/24/15 23:29:32 | Sleep                                         | 24:22:833     |\n" +
          "12/24/15 23:53:55 | Notify: NightChatzot@12:08:55 AM (15:0:0)     | 30:00:000     |\n" +
          "12/25/15 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 05:30:00 | Sleep                                         | 29:53:812     |\n" +
          "12/25/15 05:59:53 | Notify: Alot@6:14:53 AM (15:0:0)              | 30:00:000     |\n" +
          "12/25/15 06:29:53 | Sleep                                         | 07:20:325     |\n" +
          "12/25/15 06:37:14 | Notify: Talit@6:52:14 AM (15:0:0)             | 30:00:000     |\n" +
          "12/25/15 07:07:14 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 07:37:14 | Sleep                                         | 03:42:961     |\n" +
          "12/25/15 07:40:57 | Notify: Hanetz@7:55:57 AM (15:0:0)            | 30:00:000     |\n" +
          "12/25/15 08:10:57 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 08:40:57 | Sleep                                         | 16:07:049     |\n" +
          "12/25/15 08:57:04 | Notify: SofZmanShema@9:12:04 AM (15:0:0)      | 30:00:000     |\n" +
          "12/25/15 09:27:04 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 09:57:04 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 10:27:04 | Sleep                                         | 02:44:927     |\n" +
          "12/25/15 10:29:49 | Notify: SofZmanTefila@10:44:49 AM (15:0:0)    | 30:00:000     |\n" +
          "12/25/15 10:59:49 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 11:29:49 | Sleep                                         | 24:25:988     |\n" +
          "12/25/15 11:54:15 | Notify: Chatzot@12:09:15 PM (15:0:0)          | 21:06:497     |\n" +
          "12/25/15 12:15:21 | Notify: MinchaGedola@12:30:21 PM (15:0:0)     | 30:00:000     |\n" +
          "12/25/15 12:45:21 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 13:15:21 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 13:45:21 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 14:15:21 | Sleep                                         | 06:38:982     |\n" +
          "12/25/15 14:22:00 | Notify: MinchaKetana@2:37:00 PM (15:0:0)      | 30:00:000     |\n" +
          "12/25/15 14:52:00 | Sleep                                         | 22:46:242     |\n" +
          "12/25/15 15:14:46 | Notify: PlagHaMincha@3:29:46 PM (15:0:0)      | 30:00:000     |\n" +
          "12/25/15 15:44:46 | Sleep                                         | 04:46:243     |\n" +
          "12/25/15 15:49:33 | Notify: CandleLighting@4:04:33 PM (15:0:0)    | 18:00:000     |\n" +
          "12/25/15 16:07:33 | Notify: Shkia@4:22:33 PM (15:0:0)             | 09:29:916     |\n" +
          "12/25/15 16:17:02 | Notify: LatestMincha@4:32:02 PM (15:0:0)      | 24:53:372     |\n" +
          "12/25/15 16:41:56 | Notify: BeinHashmashot@4:56:56 PM (15:0:0)    | 18:14:022     |\n" +
          "12/25/15 17:00:10 | Notify: TzaitHacochavim@5:15:10 PM (15:0:0)   | 30:00:000     |\n" +
          "12/25/15 17:30:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 18:00:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 18:30:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 19:00:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 19:30:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 20:00:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 20:30:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 21:00:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 21:30:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 22:00:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 22:30:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 23:00:10 | Sleep                                         | 30:00:000     |\n" +
          "12/25/15 23:30:10 | Sleep                                         | 24:14:285     |\n" +
          "12/25/15 23:54:24 | Notify: NightChatzot@12:09:24 AM (15:0:0)     | 30:00:000     |\n" +
          "12/26/15 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 05:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 06:00:00 | Notify: Alot@6:15:15 AM (15:15:398)           | 30:00:000     |\n" +
          "12/26/15 06:30:00 | Sleep                                         | 07:35:059     |\n" +
          "12/26/15 06:37:35 | Notify: Talit@6:52:35 AM (15:0:0)             | 30:00:000     |\n" +
          "12/26/15 07:07:35 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 07:37:35 | Sleep                                         | 03:41:158     |\n" +
          "12/26/15 07:41:16 | Notify: Hanetz@7:56:16 AM (15:0:0)            | 30:00:000     |\n" +
          "12/26/15 08:11:16 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 08:41:16 | Sleep                                         | 16:13:809     |\n" +
          "12/26/15 08:57:30 | Notify: SofZmanShema@9:12:30 AM (15:0:0)      | 30:00:000     |\n" +
          "12/26/15 09:27:30 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 09:57:30 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 10:27:30 | Sleep                                         | 02:45:635     |\n" +
          "12/26/15 10:30:15 | Notify: SofZmanTefila@10:45:15 AM (15:0:0)    | 30:00:000     |\n" +
          "12/26/15 11:00:15 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 11:30:15 | Sleep                                         | 24:29:722     |\n" +
          "12/26/15 11:54:45 | Notify: Chatzot@12:09:45 PM (15:0:0)          | 21:07:430     |\n" +
          "12/26/15 12:15:52 | Notify: MinchaGedola@12:30:52 PM (15:0:0)     | 30:00:000     |\n" +
          "12/26/15 12:45:52 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 13:15:52 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 13:45:52 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 14:15:52 | Sleep                                         | 06:44:583     |\n" +
          "12/26/15 14:22:37 | Notify: MinchaKetana@2:37:37 PM (15:0:0)      | 30:00:000     |\n" +
          "12/26/15 14:52:37 | Sleep                                         | 22:48:576     |\n" +
          "12/26/15 15:15:25 | Notify: PlagHaMincha@3:30:25 PM (15:0:0)      | 30:00:000     |\n" +
          "12/26/15 15:45:25 | Sleep                                         | 22:48:586     |\n" +
          "12/26/15 16:08:14 | Notify: Shkia@4:23:14 PM (15:0:0)             | 09:30:334     |\n" +
          "12/26/15 16:17:44 | Notify: LatestMincha@4:32:44 PM (15:0:0)      | 24:52:186     |\n" +
          "12/26/15 16:42:37 | Notify: BeinHashmashot@4:57:37 PM (15:0:0)    | 10:37:480     |\n" +
          "12/26/15 16:53:14 | Notify: ShabbatEnd@5:08:14 PM (15:0:0)        | 07:35:556     |\n" +
          "12/26/15 17:00:50 | Notify: TzaitHacochavim@5:15:50 PM (15:0:0)   | 30:00:000     |\n" +
          "12/26/15 17:30:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 18:00:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 18:30:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 19:00:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 19:30:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 20:00:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 20:30:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 21:00:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 21:30:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 22:00:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 22:30:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 23:00:50 | Sleep                                         | 30:00:000     |\n" +
          "12/26/15 23:30:50 | Sleep                                         | 24:03:429     |\n" +
          "12/26/15 23:54:53 | Notify: NightChatzot@12:09:53 AM (15:0:0)     | 30:00:000     |\n" +
          "12/27/15 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 05:30:00 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 06:00:00 | Sleep                                         | 00:34:798     |\n" +
          "12/27/15 06:00:34 | Notify: Alot@6:15:34 AM (15:0:0)              | 30:00:000     |\n" +
          "12/27/15 06:30:34 | Sleep                                         | 07:18:829     |\n" +
          "12/27/15 06:37:53 | Notify: Talit@6:52:53 AM (15:0:0)             | 30:00:000     |\n" +
          "12/27/15 07:07:53 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 07:37:53 | Sleep                                         | 03:38:901     |\n" +
          "12/27/15 07:41:32 | Notify: Hanetz@7:56:32 AM (15:0:0)            | 30:00:000     |\n" +
          "12/27/15 08:11:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 08:41:32 | Sleep                                         | 16:22:217     |\n" +
          "12/27/15 08:57:54 | Notify: SofZmanShema@9:12:54 AM (15:0:0)      | 30:00:000     |\n" +
          "12/27/15 09:27:54 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 09:57:54 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 10:27:54 | Sleep                                         | 02:46:487     |\n" +
          "12/27/15 10:30:41 | Notify: SofZmanTefila@10:45:41 AM (15:0:0)    | 30:00:000     |\n" +
          "12/27/15 11:00:41 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 11:30:41 | Sleep                                         | 24:34:352     |\n" +
          "12/27/15 11:55:15 | Notify: Chatzot@12:10:15 PM (15:0:0)          | 21:08:588     |\n" +
          "12/27/15 12:16:24 | Notify: MinchaGedola@12:31:24 PM (15:0:0)     | 30:00:000     |\n" +
          "12/27/15 12:46:24 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 13:16:24 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 13:46:24 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 14:16:24 | Sleep                                         | 06:51:528     |\n" +
          "12/27/15 14:23:15 | Notify: MinchaKetana@2:38:15 PM (15:0:0)      | 30:00:000     |\n" +
          "12/27/15 14:53:15 | Sleep                                         | 22:51:470     |\n" +
          "12/27/15 15:16:07 | Notify: PlagHaMincha@3:31:07 PM (15:0:0)      | 30:00:000     |\n" +
          "12/27/15 15:46:07 | Sleep                                         | 22:51:473     |\n" +
          "12/27/15 16:08:58 | Notify: Shkia@4:23:58 PM (15:0:0)             | 09:30:861     |\n" +
          "12/27/15 16:18:29 | Notify: LatestMincha@4:33:29 PM (15:0:0)      | 24:50:699     |\n" +
          "12/27/15 16:43:20 | Notify: BeinHashmashot@4:58:20 PM (15:0:0)    | 18:11:852     |\n" +
          "12/27/15 17:01:32 | Notify: TzaitHacochavim@5:16:32 PM (15:0:0)   | 30:00:000     |\n" +
          "12/27/15 17:31:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 18:01:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 18:31:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 19:01:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 19:31:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 20:01:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 20:31:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 21:01:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 21:31:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 22:01:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 22:31:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 23:01:32 | Sleep                                         | 30:00:000     |\n" +
          "12/27/15 23:31:32 | Sleep                                         | 23:50:272     |\n" +
          "12/27/15 23:55:22 | Notify: NightChatzot@12:10:22 AM (15:0:0)     | 30:00:000     |\n"
          ;
      }


      @Override
      public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {
        Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
        expectedZmanim.put( date,
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 1 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha, Zmanim.Type.FastEnd,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 2 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 3 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 4 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.CandleLighting, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 5 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.ShabbatEnd, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 6 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
            Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
            Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
        );

        return expectedZmanim;
      }
    };


    december_21_2015_1_week( new UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback(), testResultsCallback );
  }



  public static class DefaultAlertsZmanCallback
    extends UT_ZmanUtils.ZmanCallbackForTests {

    private final NotificationDefaults notificationDefaults = new NotificationDefaults();


    @Override
    public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
      return notificationDefaults.getDefaultZmanAlertTime( type );
    }
  }

} // End of UT_WakeupScheduler_MultiDay class
