package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.zman.Zmanim;

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
    Calendar endTest = UT_ZmanUtils.configureTestTime( 2016, Calendar.NOVEMBER, 27, 10, 0 );

    TestResultsCallback testResultsCallback = new TestResultsCallback() {
      @Override
      public String getExpectedOutput() {
        return
          "Time              | Action                                        | Next (M:S:ms) |\n" +
          "------------------|-----------------------------------------------|---------------|\n" +
          "11/25/16 06:34:00 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 07:04:00 | Sleep                                         | 10:32:551     |\n" +
          "11/25/16 07:14:32 | Notify: Hanetz@7:29:32 AM (15:0:0)            | 30:00:000     |\n" +
          "11/25/16 07:44:32 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 08:14:32 | Sleep                                         | 24:17:948     |\n" +
          "11/25/16 08:38:50 | Notify: SofZmanShema@8:53:50 AM (15:0:0)      | 30:00:000     |\n" +
          "11/25/16 09:08:50 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 09:38:50 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 10:08:50 | Sleep                                         | 03:27:660     |\n" +
          "11/25/16 10:12:18 | Notify: SofZmanTefila@10:27:18 AM (15:0:0)    | 30:00:000     |\n" +
          "11/25/16 10:42:18 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 11:12:18 | Sleep                                         | 28:52:804     |\n" +
          "11/25/16 11:41:10 | Notify: Chatzot@11:56:10 AM (15:0:0)          | 22:13:201     |\n" +
          "11/25/16 12:03:24 | Notify: MinchaGedola@12:18:24 PM (15:0:0)     | 30:00:000     |\n" +
          "11/25/16 12:33:24 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 13:03:24 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 13:33:24 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 14:03:24 | Sleep                                         | 13:19:206     |\n" +
          "11/25/16 14:16:43 | Notify: MinchaKetana@2:31:43 PM (15:0:0)      | 30:00:000     |\n" +
          "11/25/16 14:46:43 | Sleep                                         | 25:33:002     |\n" +
          "11/25/16 15:12:16 | Notify: PlagHaMincha@3:27:16 PM (15:0:0)      | 30:00:000     |\n" +
          "11/25/16 15:42:16 | Sleep                                         | 07:33:006     |\n" +
          "11/25/16 15:49:49 | Notify: CandleLighting@4:04:49 PM (15:0:0)    | 18:00:000     |\n" +
          "11/25/16 16:07:49 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 16:37:49 | Sleep                                         | 03:28:181     |\n" +
          "11/25/16 16:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 17:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 17:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 18:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 18:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 19:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 19:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 20:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 20:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 21:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 21:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 22:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 22:41:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 23:11:17 | Sleep                                         | 30:00:000     |\n" +
          "11/25/16 23:41:17 | Sleep                                         | 00:33:309     |\n" +
          "11/25/16 23:41:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 00:11:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 00:41:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 01:11:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 01:41:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 02:11:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 02:41:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 03:11:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 03:41:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 04:11:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 04:41:50 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 05:11:50 | Sleep                                         | 25:43:796     |\n" +
          "11/26/16 05:37:34 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 06:07:34 | Sleep                                         | 06:35:941     |\n" +
          "11/26/16 06:14:10 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 06:44:10 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 07:14:10 | Sleep                                         | 01:41:753     |\n" +
          "11/26/16 07:15:52 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 07:45:52 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 08:15:52 | Sleep                                         | 23:41:907     |\n" +
          "11/26/16 08:39:34 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 09:09:34 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 09:39:34 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 10:09:34 | Sleep                                         | 03:23:773     |\n" +
          "11/26/16 10:12:58 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 10:42:58 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 11:12:58 | Sleep                                         | 28:32:840     |\n" +
          "11/26/16 11:41:30 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 12:11:30 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 12:41:30 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 13:11:30 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 13:41:30 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 14:11:30 | Sleep                                         | 04:57:470     |\n" +
          "11/26/16 14:16:28 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 14:46:28 | Sleep                                         | 25:20:525     |\n" +
          "11/26/16 15:11:48 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 15:41:48 | Sleep                                         | 25:20:526     |\n" +
          "11/26/16 16:07:09 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 16:37:09 | Sleep                                         | 03:31:929     |\n" +
          "11/26/16 16:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 17:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 17:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 18:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 18:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 19:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 19:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 20:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 20:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 21:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 21:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 22:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 22:40:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 23:10:41 | Sleep                                         | 30:00:000     |\n" +
          "11/26/16 23:40:41 | Sleep                                         | 01:28:860     |\n" +
          "11/26/16 23:42:10 | Notify: NightChatzot@11:57:10 PM (15:0:0)     | 30:00:000     |\n" +
          "11/27/16 00:30:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 01:00:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 01:30:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 02:00:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 02:30:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 03:00:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 03:30:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 04:00:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 04:30:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 05:00:00 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 05:30:00 | Sleep                                         | 08:41:383     |\n" +
          "11/27/16 05:38:41 | Notify: Alot@5:53:41 AM (15:0:0)              | 30:00:000     |\n" +
          "11/27/16 06:08:41 | Sleep                                         | 06:39:108     |\n" +
          "11/27/16 06:15:20 | Notify: Talit@6:30:20 AM (15:0:0)             | 30:00:000     |\n" +
          "11/27/16 06:45:20 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 07:15:20 | Sleep                                         | 01:50:486     |\n" +
          "11/27/16 07:17:10 | Notify: Hanetz@7:32:10 AM (15:0:0)            | 30:00:000     |\n" +
          "11/27/16 07:47:10 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 08:17:10 | Sleep                                         | 23:06:937     |\n" +
          "11/27/16 08:40:17 | Notify: SofZmanShema@8:55:17 AM (15:0:0)      | 30:00:000     |\n" +
          "11/27/16 09:10:17 | Sleep                                         | 30:00:000     |\n" +
          "11/27/16 09:40:17 | Sleep                                         | 30:00:000     |\n";
      }


      @Override
      public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {
        Map<HebrewDate, List<Zmanim.Type>> expectedZmanim = new HashMap<>();
        expectedZmanim.put( date,
          Arrays.asList( Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila,
            Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.CandleLighting )
        );

        expectedZmanim.put( date.forward( 1 ),
          Arrays.asList( Zmanim.Type.NightChatzot )
        );

        expectedZmanim.put( date.forward( 2 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema )
        );

        return expectedZmanim;
      }
    };

    runDays( testTime, endTest, new NoShabbatZmanCallback(), testResultsCallback );
  }


  @Test
  public void yomKippur_5777() {

    Calendar testTime = UT_ZmanUtils.configureTestTime( 2016, Calendar.OCTOBER, 11, 6, 34 );
    Calendar endTest = UT_ZmanUtils.configureTestTime( 2016, Calendar.OCTOBER, 13, 10, 0 );

    TestResultsCallback testResultsCallback = new TestResultsCallback() {
      @Override
      public String getExpectedOutput() {
        return
          "Time              | Action                                        | Next (M:S:ms) |\n" +
          "------------------|-----------------------------------------------|---------------|\n" +
          "10/11/16 06:34:00 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 07:04:00 | Sleep                                         | 04:18:295     |\n" +
          "10/11/16 07:08:18 | Notify: Hanetz@7:23:18 AM (15:0:0)            | 30:00:000     |\n" +
          "10/11/16 07:38:18 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 08:08:18 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 08:38:18 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 09:08:18 | Notify: SofZmanShema@9:23:48 AM (15:29:963)   | 30:00:000     |\n" +
          "10/11/16 09:38:18 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 10:08:18 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 10:38:18 | Sleep                                         | 11:13:508     |\n" +
          "10/11/16 10:49:31 | Notify: SofZmanTefila@11:04:31 AM (15:0:0)    | 30:00:000     |\n" +
          "10/11/16 11:19:31 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 11:49:31 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 12:19:31 | Sleep                                         | 20:36:754     |\n" +
          "10/11/16 12:40:08 | Notify: Chatzot@12:55:08 PM (15:0:0)          | 27:39:188     |\n" +
          "10/11/16 13:07:47 | Notify: MinchaGedola@1:22:47 PM (15:0:0)      | 30:00:000     |\n" +
          "10/11/16 13:37:47 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 14:07:47 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 14:37:47 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 15:07:47 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 15:37:47 | Sleep                                         | 15:55:131     |\n" +
          "10/11/16 15:53:42 | Notify: MinchaKetana@4:08:42 PM (15:0:0)      | 30:00:000     |\n" +
          "10/11/16 16:23:42 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 16:53:42 | Sleep                                         | 09:07:971     |\n" +
          "10/11/16 17:02:50 | Notify: PlagHaMincha@5:17:50 PM (15:0:0)      | 30:00:000     |\n" +
          "10/11/16 17:32:50 | Sleep                                         | 21:07:983     |\n" +
          "10/11/16 17:53:58 | Notify: CandleLighting@6:08:58 PM (15:0:0)    | 18:00:000     |\n" +
          "10/11/16 18:11:58 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 18:41:58 | Sleep                                         | 01:24:053     |\n" +
          "10/11/16 18:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 19:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 19:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 20:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 20:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 21:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 21:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 22:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 22:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 23:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/11/16 23:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 00:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 00:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 01:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 01:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 02:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 02:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 03:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 03:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 04:13:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 04:43:22 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 05:13:22 | Sleep                                         | 25:28:698     |\n" +
          "10/12/16 05:38:51 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 06:08:51 | Sleep                                         | 05:01:772     |\n" +
          "10/12/16 06:13:53 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 06:43:53 | Sleep                                         | 25:50:861     |\n" +
          "10/12/16 07:09:44 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 07:39:44 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 08:09:44 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 08:39:44 | Sleep                                         | 29:38:894     |\n" +
          "10/12/16 09:09:23 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 09:39:23 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 10:09:23 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 10:39:23 | Sleep                                         | 10:27:678     |\n" +
          "10/12/16 10:49:50 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 11:19:50 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 11:49:50 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 12:19:50 | Sleep                                         | 20:03:286     |\n" +
          "10/12/16 12:39:54 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 13:09:54 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 13:39:54 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 14:09:54 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 14:39:54 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 15:09:54 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 15:39:54 | Sleep                                         | 12:35:750     |\n" +
          "10/12/16 15:52:29 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 16:22:29 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 16:52:29 | Sleep                                         | 08:47:054     |\n" +
          "10/12/16 17:01:16 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 17:31:16 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 18:01:16 | Sleep                                         | 08:47:063     |\n" +
          "10/12/16 18:10:03 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 18:40:03 | Sleep                                         | 01:24:001     |\n" +
          "10/12/16 18:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 19:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 19:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 20:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 20:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 21:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 21:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 22:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 22:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 23:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/12/16 23:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 00:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 00:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 01:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 01:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 02:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 02:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 03:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 03:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 04:11:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 04:41:27 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 05:11:27 | Sleep                                         | 28:47:481     |\n" +
          "10/13/16 05:40:15 | Notify: Alot@5:55:15 AM (15:0:0)              | 30:00:000     |\n" +
          "10/13/16 06:10:15 | Sleep                                         | 05:00:844     |\n" +
          "10/13/16 06:15:16 | Notify: Talit@6:30:16 AM (15:0:0)             | 30:00:000     |\n" +
          "10/13/16 06:45:16 | Sleep                                         | 25:54:170     |\n" +
          "10/13/16 07:11:10 | Notify: Hanetz@7:26:10 AM (15:0:0)            | 30:00:000     |\n" +
          "10/13/16 07:41:10 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 08:11:10 | Sleep                                         | 30:00:000     |\n" +
          "10/13/16 08:41:10 | Sleep                                         | 28:47:682     |\n" +
          "10/13/16 09:09:58 | Notify: SofZmanShema@9:24:58 AM (15:0:0)      | 30:00:000     |\n" +
          "10/13/16 09:39:58 | Sleep                                         | 30:00:000     |\n";
      }


      @Override
      public Map<HebrewDate, List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date ) {
        Map<HebrewDate, List<Zmanim.Type>> expectedZmanim = new HashMap<>();
        expectedZmanim.put( date,
          Arrays.asList( Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila,
            Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
            Zmanim.Type.PlagHaMincha, Zmanim.Type.CandleLighting )
        );

        expectedZmanim.put( date.forward( 1 ),
          Collections.<Zmanim.Type>emptyList()
        );

        expectedZmanim.put( date.forward( 2 ),
          Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema )
        );

        return expectedZmanim;
      }
    };

    runDays( testTime, endTest, new NoShabbatZmanCallback(), testResultsCallback );
  }



  private static class NoShabbatZmanCallback
    extends UT_ZmanUtils.AlwaysFifteenMinuteAlertsZmanCallback {

    @Override
    public boolean shouldNotifyOnShabbat() {
      return false;
    }
  }


} // End of UT_WakeupScheduler_NoShabbat class
