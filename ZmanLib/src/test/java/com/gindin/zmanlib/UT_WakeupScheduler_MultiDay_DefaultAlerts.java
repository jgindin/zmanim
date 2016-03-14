package com.gindin.zmanlib;

import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Test;

/**
 *
 */
public class UT_WakeupScheduler_MultiDay_DefaultAlerts
	extends WakeupSchedulerBase_MutliDay {


	@Test
	public void december_7_2014_15_minute_alerts() {

		december_7_2014( new AlwaysFifteenMinuteAlertsZmanCallback(),
			"Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" +
			"------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" +
			"12/07/14 06:34:00 | Notify: Talit@6:39:55 AM (5:55:244)           | Hanetz          |     7:42:53 AM |  30:00:000    |\n" +
			"12/07/14 07:04:00 | Sleep                                         | Hanetz          |     7:42:53 AM |  23:53:481    |\n" +
			"12/07/14 07:27:53 | Notify: Hanetz@7:42:53 AM (15:0:0)            | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			"12/07/14 07:57:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			"12/07/14 08:27:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  18:46:642    |\n" +
			"12/07/14 08:46:40 | Notify: SofZmanShema@9:01:40 AM (15:0:0)      | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 09:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 09:46:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 10:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  02:55:558    |\n" +
			"12/07/14 10:19:35 | Notify: SofZmanTefila@10:34:35 AM (15:0:0)    | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 10:49:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 11:19:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  25:51:100    |\n" +
			"12/07/14 11:45:26 | Notify: Chatzot@12:00:26 PM (15:0:0)          | MinchaGedola    |    12:21:54 PM |  21:27:775    |\n" +
			"12/07/14 12:06:54 | Notify: MinchaGedola@12:21:54 PM (15:0:0)     | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 12:36:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 13:06:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 13:36:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 14:06:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  08:46:650    |\n" +
			"12/07/14 14:15:41 | Notify: MinchaKetana@2:30:41 PM (15:0:0)      | PlagHaMincha    |     3:24:20 PM |  30:00:000    |\n" +
			"12/07/14 14:45:41 | Sleep                                         | PlagHaMincha    |     3:24:20 PM |  23:39:437    |\n" +
			"12/07/14 15:09:20 | Notify: PlagHaMincha@3:24:20 PM (15:0:0)      | Shkia           |     4:18:00 PM |  30:00:000    |\n" +
			"12/07/14 15:39:20 | Sleep                                         | Shkia           |     4:18:00 PM |  23:39:443    |\n" +
			"12/07/14 16:03:00 | Notify: Shkia@4:18:00 PM (15:0:0)             | LatestMincha    |     4:27:39 PM |  09:39:487    |\n" +
			"12/07/14 16:12:39 | Notify: LatestMincha@4:27:39 PM (15:0:0)      | BeinHashmashot  |     4:52:04 PM |  24:24:788    |\n" +
			"12/07/14 16:37:04 | Notify: BeinHashmashot@4:52:04 PM (15:0:0)    | TzaitHacochavim |     5:10:02 PM |  17:57:775    |\n" +
			"12/07/14 16:55:02 | Notify: TzaitHacochavim@5:10:02 PM (15:0:0)   | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 17:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 17:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 18:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 18:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 19:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 19:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 20:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 20:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 21:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 21:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 22:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 22:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 23:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  20:56:081    |\n" +
			"12/07/14 23:45:58 | Notify: NightChatzot@12:00:58 AM (15:0:0)     | Alot            |     6:03:46 AM |  30:00:000    |\n"
		);
	}


	@Test
	public void december_7_2014_default_product_alerts() {

		december_7_2014( new DefaultAlertsZmanCallback(),
			"Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" +
			"------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" +
			"12/07/14 06:34:00 | Notify: Talit@6:39:55 AM (5:55:244)           | Hanetz          |     7:42:53 AM |  30:00:000    |\n" +
			"12/07/14 07:04:00 | Sleep                                         | Hanetz          |     7:42:53 AM |  30:00:000    |\n" +
			"12/07/14 07:34:00 | Sleep                                         | Hanetz          |     7:42:53 AM |  08:53:481    |\n" +
			"12/07/14 07:42:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			"12/07/14 08:12:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			"12/07/14 08:42:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  03:46:642    |\n" +
			"12/07/14 08:46:40 | Notify: SofZmanShema@9:01:40 AM (15:0:0)      | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 09:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 09:46:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 10:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  02:55:558    |\n" +
			"12/07/14 10:19:35 | Notify: SofZmanTefila@10:34:35 AM (15:0:0)    | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 10:49:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 11:19:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 11:49:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  10:51:100    |\n" +
			"12/07/14 12:00:26 | Sleep                                         | MinchaGedola    |    12:21:54 PM |  21:27:775    |\n" +
			"12/07/14 12:21:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 12:51:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 13:21:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 13:51:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 14:21:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  08:46:650    |\n" +
			"12/07/14 14:30:41 | Sleep                                         | PlagHaMincha    |     3:24:20 PM |  30:00:000    |\n" +
			"12/07/14 15:00:41 | Sleep                                         | PlagHaMincha    |     3:24:20 PM |  08:39:437    |\n" +
			"12/07/14 15:09:20 | Notify: PlagHaMincha@3:24:20 PM (15:0:0)      | Shkia           |     4:18:00 PM |  30:00:000    |\n" +
			"12/07/14 15:39:20 | Sleep                                         | Shkia           |     4:18:00 PM |  23:39:443    |\n" +
			"12/07/14 16:03:00 | Notify: Shkia@4:18:00 PM (15:0:0)             | LatestMincha    |     4:27:39 PM |  09:39:487    |\n" +
			"12/07/14 16:12:39 | Notify: LatestMincha@4:27:39 PM (15:0:0)      | BeinHashmashot  |     4:52:04 PM |  30:00:000    |\n" +
			"12/07/14 16:42:39 | Sleep                                         | BeinHashmashot  |     4:52:04 PM |  09:24:788    |\n" +
			"12/07/14 16:52:04 | Sleep                                         | TzaitHacochavim |     5:10:02 PM |  17:57:775    |\n" +
			"12/07/14 17:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 17:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 18:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 18:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 19:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 19:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 20:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 20:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 21:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 21:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 22:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 22:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 23:10:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 23:40:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  20:56:081    |\n"
		);
	}


	@Test
	public void december_21_2015_1_week() {

		december_21_2015_1_week( new AlwaysFifteenMinuteAlertsZmanCallback(),
			"Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" +
			"------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" +
			"12/21/15 00:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 00:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 01:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 01:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 02:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 02:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 03:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 03:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 04:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 04:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 05:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 05:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  27:06:204    |\n" +
			"12/21/15 05:58:06 | Notify: Alot@6:13:06 AM (15:0:0)              | Talit           |     6:50:27 AM |  30:00:000    |\n" +
			"12/21/15 06:28:06 | Sleep                                         | Talit           |     6:50:27 AM |  07:21:279    |\n" +
			"12/21/15 06:35:27 | Notify: Talit@6:50:27 AM (15:0:0)             | Hanetz          |     7:54:13 AM |  30:00:000    |\n" +
			"12/21/15 07:05:27 | Sleep                                         | Hanetz          |     7:54:13 AM |  30:00:000    |\n" +
			"12/21/15 07:35:27 | Sleep                                         | Hanetz          |     7:54:13 AM |  03:45:549    |\n" +
			"12/21/15 07:39:13 | Notify: Hanetz@7:54:13 AM (15:0:0)            | SofZmanShema    |     9:10:09 AM |  30:00:000    |\n" +
			"12/21/15 08:09:13 | Sleep                                         | SofZmanShema    |     9:10:09 AM |  30:00:000    |\n" +
			"12/21/15 08:39:13 | Sleep                                         | SofZmanShema    |     9:10:09 AM |  15:56:685    |\n" +
			"12/21/15 08:55:09 | Notify: SofZmanShema@9:10:09 AM (15:0:0)      | SofZmanTefila   |    10:42:53 AM |  30:00:000    |\n" +
			"12/21/15 09:25:09 | Sleep                                         | SofZmanTefila   |    10:42:53 AM |  30:00:000    |\n" +
			"12/21/15 09:55:09 | Sleep                                         | SofZmanTefila   |    10:42:53 AM |  30:00:000    |\n" +
			"12/21/15 10:25:09 | Sleep                                         | SofZmanTefila   |    10:42:53 AM |  02:43:411    |\n" +
			"12/21/15 10:27:53 | Notify: SofZmanTefila@10:42:53 AM (15:0:0)    | Chatzot         |    12:07:13 PM |  30:00:000    |\n" +
			"12/21/15 10:57:53 | Sleep                                         | Chatzot         |    12:07:13 PM |  30:00:000    |\n" +
			"12/21/15 11:27:53 | Sleep                                         | Chatzot         |    12:07:13 PM |  24:20:048    |\n" +
			"12/21/15 11:52:13 | Notify: Chatzot@12:07:13 PM (15:0:0)          | MinchaGedola    |    12:28:18 PM |  21:05:012    |\n" +
			"12/21/15 12:13:18 | Notify: MinchaGedola@12:28:18 PM (15:0:0)     | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 12:43:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 13:13:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 13:43:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 14:13:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  06:30:072    |\n" +
			"12/21/15 14:19:48 | Notify: MinchaKetana@2:34:48 PM (15:0:0)      | PlagHaMincha    |     3:27:30 PM |  30:00:000    |\n" +
			"12/21/15 14:49:48 | Sleep                                         | PlagHaMincha    |     3:27:30 PM |  22:42:530    |\n" +
			"12/21/15 15:12:30 | Notify: PlagHaMincha@3:27:30 PM (15:0:0)      | Shkia           |     4:20:13 PM |  30:00:000    |\n" +
			"12/21/15 15:42:30 | Sleep                                         | Shkia           |     4:20:13 PM |  22:42:532    |\n" +
			"12/21/15 16:05:13 | Notify: Shkia@4:20:13 PM (15:0:0)             | LatestMincha    |     4:29:42 PM |  09:29:254    |\n" +
			"12/21/15 16:14:42 | Notify: LatestMincha@4:29:42 PM (15:0:0)      | BeinHashmashot  |     4:54:37 PM |  24:55:135    |\n" +
			"12/21/15 16:39:37 | Notify: BeinHashmashot@4:54:37 PM (15:0:0)    | TzaitHacochavim |     5:12:53 PM |  18:15:957    |\n" +
			"12/21/15 16:57:53 | Notify: TzaitHacochavim@5:12:53 PM (15:0:0)   | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 17:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 17:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 18:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 18:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 19:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 19:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 20:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 20:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 21:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 21:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 22:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 22:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 23:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  24:34:557    |\n" +
			"12/21/15 23:52:28 | Notify: NightChatzot@12:07:28 AM (15:0:0)     | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 00:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 00:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 01:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 01:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 02:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 02:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 03:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 03:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 04:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 04:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 05:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 05:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  06:08:007    |\n" +
			"12/22/15 05:58:36 | Notify: Alot@6:13:36 AM (15:0:0)              | Talit           |     6:50:57 AM |  30:00:000    |\n" +
			"12/22/15 06:28:36 | Sleep                                         | Talit           |     6:50:57 AM |  07:21:297    |\n" +
			"12/22/15 06:35:57 | Notify: Talit@6:50:57 AM (15:0:0)             | Hanetz          |     7:54:43 AM |  30:00:000    |\n" +
			"12/22/15 07:05:57 | Sleep                                         | Hanetz          |     7:54:43 AM |  30:00:000    |\n" +
			"12/22/15 07:35:57 | Sleep                                         | Hanetz          |     7:54:43 AM |  03:45:599    |\n" +
			"12/22/15 07:39:43 | Notify: Hanetz@7:54:43 AM (15:0:0)            | SofZmanShema    |     9:10:39 AM |  30:00:000    |\n" +
			"12/22/15 08:09:43 | Sleep                                         | SofZmanShema    |     9:10:39 AM |  30:00:000    |\n" +
			"12/22/15 08:39:43 | Sleep                                         | SofZmanShema    |     9:10:39 AM |  15:56:767    |\n" +
			"12/22/15 08:55:39 | Notify: SofZmanShema@9:10:39 AM (15:0:0)      | SofZmanTefila   |    10:43:23 AM |  30:00:000    |\n" +
			"12/22/15 09:25:39 | Sleep                                         | SofZmanTefila   |    10:43:23 AM |  30:00:000    |\n" +
			"12/22/15 09:55:39 | Sleep                                         | SofZmanTefila   |    10:43:23 AM |  30:00:000    |\n" +
			"12/22/15 10:25:39 | Sleep                                         | SofZmanTefila   |    10:43:23 AM |  02:43:589    |\n" +
			"12/22/15 10:28:23 | Notify: SofZmanTefila@10:43:23 AM (15:0:0)    | Chatzot         |    12:07:43 PM |  30:00:000    |\n" +
			"12/22/15 10:58:23 | Sleep                                         | Chatzot         |    12:07:43 PM |  30:00:000    |\n" +
			"12/22/15 11:28:23 | Sleep                                         | Chatzot         |    12:07:43 PM |  24:20:178    |\n" +
			"12/22/15 11:52:43 | Notify: Chatzot@12:07:43 PM (15:0:0)          | MinchaGedola    |    12:28:48 PM |  21:05:044    |\n" +
			"12/22/15 12:13:48 | Notify: MinchaGedola@12:28:48 PM (15:0:0)     | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 12:43:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 13:13:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 13:43:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 14:13:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  06:30:267    |\n" +
			"12/22/15 14:20:18 | Notify: MinchaKetana@2:35:18 PM (15:0:0)      | PlagHaMincha    |     3:28:01 PM |  30:00:000    |\n" +
			"12/22/15 14:50:18 | Sleep                                         | PlagHaMincha    |     3:28:01 PM |  22:42:611    |\n" +
			"12/22/15 15:13:01 | Notify: PlagHaMincha@3:28:01 PM (15:0:0)      | Shkia           |     4:20:44 PM |  30:00:000    |\n" +
			"12/22/15 15:43:01 | Sleep                                         | Shkia           |     4:20:44 PM |  22:42:619    |\n" +
			"12/22/15 16:05:44 | Notify: Shkia@4:20:44 PM (15:0:0)             | LatestMincha    |     4:30:13 PM |  09:29:268    |\n" +
			"12/22/15 16:15:13 | Notify: LatestMincha@4:30:13 PM (15:0:0)      | FastEnd         |     4:50:44 PM |  20:30:732    |\n" +
			"12/22/15 16:35:44 | Notify: FastEnd@4:50:44 PM (15:0:0)           | BeinHashmashot  |     4:55:08 PM |  04:24:411    |\n" +
			"12/22/15 16:40:08 | Notify: BeinHashmashot@4:55:08 PM (15:0:0)    | TzaitHacochavim |     5:13:24 PM |  18:15:778    |\n" +
			"12/22/15 16:58:24 | Notify: TzaitHacochavim@5:13:24 PM (15:0:0)   | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 17:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 17:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 18:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 18:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 19:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 19:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 20:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 20:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 21:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 21:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 22:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 22:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 23:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  24:32:972    |\n" +
			"12/22/15 23:52:57 | Notify: NightChatzot@12:07:57 AM (15:0:0)     | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 00:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 00:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 01:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 01:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 02:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 02:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 03:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 03:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 04:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 04:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 05:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 05:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  06:06:834    |\n" +
			"12/23/15 05:59:04 | Notify: Alot@6:14:04 AM (15:0:0)              | Talit           |     6:51:25 AM |  30:00:000    |\n" +
			"12/23/15 06:29:04 | Sleep                                         | Talit           |     6:51:25 AM |  07:21:144    |\n" +
			"12/23/15 06:36:25 | Notify: Talit@6:51:25 AM (15:0:0)             | Hanetz          |     7:55:10 AM |  30:00:000    |\n" +
			"12/23/15 07:06:25 | Sleep                                         | Hanetz          |     7:55:10 AM |  30:00:000    |\n" +
			"12/23/15 07:36:25 | Sleep                                         | Hanetz          |     7:55:10 AM |  03:45:184    |\n" +
			"12/23/15 07:40:10 | Notify: Hanetz@7:55:10 AM (15:0:0)            | SofZmanShema    |     9:11:09 AM |  30:00:000    |\n" +
			"12/23/15 08:10:10 | Sleep                                         | SofZmanShema    |     9:11:09 AM |  30:00:000    |\n" +
			"12/23/15 08:40:10 | Sleep                                         | SofZmanShema    |     9:11:09 AM |  15:58:523    |\n" +
			"12/23/15 08:56:09 | Notify: SofZmanShema@9:11:09 AM (15:0:0)      | SofZmanTefila   |    10:43:52 AM |  30:00:000    |\n" +
			"12/23/15 09:26:09 | Sleep                                         | SofZmanTefila   |    10:43:52 AM |  30:00:000    |\n" +
			"12/23/15 09:56:09 | Sleep                                         | SofZmanTefila   |    10:43:52 AM |  30:00:000    |\n" +
			"12/23/15 10:26:09 | Sleep                                         | SofZmanTefila   |    10:43:52 AM |  02:43:901    |\n" +
			"12/23/15 10:28:52 | Notify: SofZmanTefila@10:43:52 AM (15:0:0)    | Chatzot         |    12:08:14 PM |  30:00:000    |\n" +
			"12/23/15 10:58:52 | Sleep                                         | Chatzot         |    12:08:14 PM |  30:00:000    |\n" +
			"12/23/15 11:28:52 | Sleep                                         | Chatzot         |    12:08:14 PM |  24:21:212    |\n" +
			"12/23/15 11:53:14 | Notify: Chatzot@12:08:14 PM (15:0:0)          | MinchaGedola    |    12:29:19 PM |  21:05:303    |\n" +
			"12/23/15 12:14:19 | Notify: MinchaGedola@12:29:19 PM (15:0:0)     | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 12:44:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 13:14:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 13:44:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 14:14:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  06:31:818    |\n" +
			"12/23/15 14:20:51 | Notify: MinchaKetana@2:35:51 PM (15:0:0)      | PlagHaMincha    |     3:28:34 PM |  30:00:000    |\n" +
			"12/23/15 14:50:51 | Sleep                                         | PlagHaMincha    |     3:28:34 PM |  22:43:257    |\n" +
			"12/23/15 15:13:34 | Notify: PlagHaMincha@3:28:34 PM (15:0:0)      | Shkia           |     4:21:17 PM |  30:00:000    |\n" +
			"12/23/15 15:43:34 | Sleep                                         | Shkia           |     4:21:17 PM |  22:43:266    |\n" +
			"12/23/15 16:06:17 | Notify: Shkia@4:21:17 PM (15:0:0)             | LatestMincha    |     4:30:47 PM |  09:29:376    |\n" +
			"12/23/15 16:15:47 | Notify: LatestMincha@4:30:47 PM (15:0:0)      | BeinHashmashot  |     4:55:42 PM |  24:54:858    |\n" +
			"12/23/15 16:40:42 | Notify: BeinHashmashot@4:55:42 PM (15:0:0)    | TzaitHacochavim |     5:13:57 PM |  18:15:395    |\n" +
			"12/23/15 16:58:57 | Notify: TzaitHacochavim@5:13:57 PM (15:0:0)   | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 17:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 17:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 18:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 18:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 19:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 19:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 20:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 20:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 21:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 21:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 22:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 22:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 23:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  24:29:064    |\n" +
			"12/23/15 23:53:26 | Notify: NightChatzot@12:08:26 AM (15:0:0)     | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 00:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 00:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 01:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 01:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 02:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 02:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 03:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 03:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 04:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 04:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 05:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 05:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  06:03:569    |\n" +
			"12/24/15 05:59:30 | Notify: Alot@6:14:30 AM (15:0:0)              | Talit           |     6:51:50 AM |  30:00:000    |\n" +
			"12/24/15 06:29:30 | Sleep                                         | Talit           |     6:51:50 AM |  07:20:820    |\n" +
			"12/24/15 06:36:50 | Notify: Talit@6:51:50 AM (15:0:0)             | Hanetz          |     7:55:35 AM |  30:00:000    |\n" +
			"12/24/15 07:06:50 | Sleep                                         | Hanetz          |     7:55:35 AM |  30:00:000    |\n" +
			"12/24/15 07:36:50 | Sleep                                         | Hanetz          |     7:55:35 AM |  03:44:304    |\n" +
			"12/24/15 07:40:35 | Notify: Hanetz@7:55:35 AM (15:0:0)            | SofZmanShema    |     9:11:37 AM |  30:00:000    |\n" +
			"12/24/15 08:10:35 | Sleep                                         | SofZmanShema    |     9:11:37 AM |  30:00:000    |\n" +
			"12/24/15 08:40:35 | Sleep                                         | SofZmanShema    |     9:11:37 AM |  16:01:953    |\n" +
			"12/24/15 08:56:37 | Notify: SofZmanShema@9:11:37 AM (15:0:0)      | SofZmanTefila   |    10:44:21 AM |  30:00:000    |\n" +
			"12/24/15 09:26:37 | Sleep                                         | SofZmanTefila   |    10:44:21 AM |  30:00:000    |\n" +
			"12/24/15 09:56:37 | Sleep                                         | SofZmanTefila   |    10:44:21 AM |  30:00:000    |\n" +
			"12/24/15 10:26:37 | Sleep                                         | SofZmanTefila   |    10:44:21 AM |  02:44:347    |\n" +
			"12/24/15 10:29:21 | Notify: SofZmanTefila@10:44:21 AM (15:0:0)    | Chatzot         |    12:08:44 PM |  30:00:000    |\n" +
			"12/24/15 10:59:21 | Sleep                                         | Chatzot         |    12:08:44 PM |  30:00:000    |\n" +
			"12/24/15 11:29:21 | Sleep                                         | Chatzot         |    12:08:44 PM |  24:23:150    |\n" +
			"12/24/15 11:53:44 | Notify: Chatzot@12:08:44 PM (15:0:0)          | MinchaGedola    |    12:29:50 PM |  21:05:787    |\n" +
			"12/24/15 12:14:50 | Notify: MinchaGedola@12:29:50 PM (15:0:0)     | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 12:44:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 13:14:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 13:44:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 14:14:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  06:34:725    |\n" +
			"12/24/15 14:21:25 | Notify: MinchaKetana@2:36:25 PM (15:0:0)      | PlagHaMincha    |     3:29:09 PM |  30:00:000    |\n" +
			"12/24/15 14:51:25 | Sleep                                         | PlagHaMincha    |     3:29:09 PM |  22:44:469    |\n" +
			"12/24/15 15:14:09 | Notify: PlagHaMincha@3:29:09 PM (15:0:0)      | Shkia           |     4:21:54 PM |  30:00:000    |\n" +
			"12/24/15 15:44:09 | Sleep                                         | Shkia           |     4:21:54 PM |  22:44:471    |\n" +
			"12/24/15 16:06:54 | Notify: Shkia@4:21:54 PM (15:0:0)             | LatestMincha    |     4:31:23 PM |  09:29:592    |\n" +
			"12/24/15 16:16:23 | Notify: LatestMincha@4:31:23 PM (15:0:0)      | BeinHashmashot  |     4:56:17 PM |  24:54:267    |\n" +
			"12/24/15 16:41:17 | Notify: BeinHashmashot@4:56:17 PM (15:0:0)    | TzaitHacochavim |     5:14:32 PM |  18:14:810    |\n" +
			"12/24/15 16:59:32 | Notify: TzaitHacochavim@5:14:32 PM (15:0:0)   | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 17:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 17:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 18:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 18:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 19:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 19:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 20:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 20:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 21:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 21:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 22:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 22:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 23:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  24:22:833    |\n" +
			"12/24/15 23:53:55 | Notify: NightChatzot@12:08:55 AM (15:0:0)     | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 00:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 00:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 01:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 01:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 02:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 02:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 03:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 03:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 04:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 04:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 05:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 05:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  05:58:216    |\n" +
			"12/25/15 05:59:53 | Notify: Alot@6:14:53 AM (15:0:0)              | Talit           |     6:52:14 AM |  30:00:000    |\n" +
			"12/25/15 06:29:53 | Sleep                                         | Talit           |     6:52:14 AM |  07:20:325    |\n" +
			"12/25/15 06:37:14 | Notify: Talit@6:52:14 AM (15:0:0)             | Hanetz          |     7:55:57 AM |  30:00:000    |\n" +
			"12/25/15 07:07:14 | Sleep                                         | Hanetz          |     7:55:57 AM |  30:00:000    |\n" +
			"12/25/15 07:37:14 | Sleep                                         | Hanetz          |     7:55:57 AM |  03:42:961    |\n" +
			"12/25/15 07:40:57 | Notify: Hanetz@7:55:57 AM (15:0:0)            | SofZmanShema    |     9:12:04 AM |  30:00:000    |\n" +
			"12/25/15 08:10:57 | Sleep                                         | SofZmanShema    |     9:12:04 AM |  30:00:000    |\n" +
			"12/25/15 08:40:57 | Sleep                                         | SofZmanShema    |     9:12:04 AM |  16:07:049    |\n" +
			"12/25/15 08:57:04 | Notify: SofZmanShema@9:12:04 AM (15:0:0)      | SofZmanTefila   |    10:44:49 AM |  30:00:000    |\n" +
			"12/25/15 09:27:04 | Sleep                                         | SofZmanTefila   |    10:44:49 AM |  30:00:000    |\n" +
			"12/25/15 09:57:04 | Sleep                                         | SofZmanTefila   |    10:44:49 AM |  30:00:000    |\n" +
			"12/25/15 10:27:04 | Sleep                                         | SofZmanTefila   |    10:44:49 AM |  02:44:927    |\n" +
			"12/25/15 10:29:49 | Notify: SofZmanTefila@10:44:49 AM (15:0:0)    | Chatzot         |    12:09:15 PM |  30:00:000    |\n" +
			"12/25/15 10:59:49 | Sleep                                         | Chatzot         |    12:09:15 PM |  30:00:000    |\n" +
			"12/25/15 11:29:49 | Sleep                                         | Chatzot         |    12:09:15 PM |  24:25:988    |\n" +
			"12/25/15 11:54:15 | Notify: Chatzot@12:09:15 PM (15:0:0)          | MinchaGedola    |    12:30:21 PM |  21:06:497    |\n" +
			"12/25/15 12:15:21 | Notify: MinchaGedola@12:30:21 PM (15:0:0)     | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 12:45:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 13:15:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 13:45:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 14:15:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  06:38:982    |\n" +
			"12/25/15 14:22:00 | Notify: MinchaKetana@2:37:00 PM (15:0:0)      | PlagHaMincha    |     3:29:46 PM |  30:00:000    |\n" +
			"12/25/15 14:52:00 | Sleep                                         | PlagHaMincha    |     3:29:46 PM |  22:46:242    |\n" +
			"12/25/15 15:14:46 | Notify: PlagHaMincha@3:29:46 PM (15:0:0)      | CandleLighting  |     4:04:33 PM |  30:00:000    |\n" +
			"12/25/15 15:44:46 | Sleep                                         | CandleLighting  |     4:04:33 PM |  04:46:243    |\n" +
			"12/25/15 15:49:33 | Notify: CandleLighting@4:04:33 PM (15:0:0)    | Shkia           |     4:22:33 PM |  18:00:000    |\n" +
			"12/25/15 16:07:33 | Notify: Shkia@4:22:33 PM (15:0:0)             | LatestMincha    |     4:32:02 PM |  09:29:916    |\n" +
			"12/25/15 16:17:02 | Notify: LatestMincha@4:32:02 PM (15:0:0)      | BeinHashmashot  |     4:56:56 PM |  24:53:372    |\n" +
			"12/25/15 16:41:56 | Notify: BeinHashmashot@4:56:56 PM (15:0:0)    | TzaitHacochavim |     5:15:10 PM |  18:14:022    |\n" +
			"12/25/15 17:00:10 | Notify: TzaitHacochavim@5:15:10 PM (15:0:0)   | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 17:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 18:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 18:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 19:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 19:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 20:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 20:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 21:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 21:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 22:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 22:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 23:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 23:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  24:14:285    |\n" +
			"12/25/15 23:54:24 | Notify: NightChatzot@12:09:24 AM (15:0:0)     | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 00:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 00:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 01:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 01:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 02:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 02:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 03:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 03:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 04:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 04:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 05:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 05:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  05:50:777    |\n" +
			"12/26/15 06:00:15 | Notify: Alot@6:15:15 AM (15:0:0)              | Talit           |     6:52:35 AM |  30:00:000    |\n" +
			"12/26/15 06:30:15 | Sleep                                         | Talit           |     6:52:35 AM |  07:19:661    |\n" +
			"12/26/15 06:37:35 | Notify: Talit@6:52:35 AM (15:0:0)             | Hanetz          |     7:56:16 AM |  30:00:000    |\n" +
			"12/26/15 07:07:35 | Sleep                                         | Hanetz          |     7:56:16 AM |  30:00:000    |\n" +
			"12/26/15 07:37:35 | Sleep                                         | Hanetz          |     7:56:16 AM |  03:41:158    |\n" +
			"12/26/15 07:41:16 | Notify: Hanetz@7:56:16 AM (15:0:0)            | SofZmanShema    |     9:12:30 AM |  30:00:000    |\n" +
			"12/26/15 08:11:16 | Sleep                                         | SofZmanShema    |     9:12:30 AM |  30:00:000    |\n" +
			"12/26/15 08:41:16 | Sleep                                         | SofZmanShema    |     9:12:30 AM |  16:13:809    |\n" +
			"12/26/15 08:57:30 | Notify: SofZmanShema@9:12:30 AM (15:0:0)      | SofZmanTefila   |    10:45:15 AM |  30:00:000    |\n" +
			"12/26/15 09:27:30 | Sleep                                         | SofZmanTefila   |    10:45:15 AM |  30:00:000    |\n" +
			"12/26/15 09:57:30 | Sleep                                         | SofZmanTefila   |    10:45:15 AM |  30:00:000    |\n" +
			"12/26/15 10:27:30 | Sleep                                         | SofZmanTefila   |    10:45:15 AM |  02:45:635    |\n" +
			"12/26/15 10:30:15 | Notify: SofZmanTefila@10:45:15 AM (15:0:0)    | Chatzot         |    12:09:45 PM |  30:00:000    |\n" +
			"12/26/15 11:00:15 | Sleep                                         | Chatzot         |    12:09:45 PM |  30:00:000    |\n" +
			"12/26/15 11:30:15 | Sleep                                         | Chatzot         |    12:09:45 PM |  24:29:722    |\n" +
			"12/26/15 11:54:45 | Notify: Chatzot@12:09:45 PM (15:0:0)          | MinchaGedola    |    12:30:52 PM |  21:07:430    |\n" +
			"12/26/15 12:15:52 | Notify: MinchaGedola@12:30:52 PM (15:0:0)     | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 12:45:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 13:15:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 13:45:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 14:15:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  06:44:583    |\n" +
			"12/26/15 14:22:37 | Notify: MinchaKetana@2:37:37 PM (15:0:0)      | PlagHaMincha    |     3:30:25 PM |  30:00:000    |\n" +
			"12/26/15 14:52:37 | Sleep                                         | PlagHaMincha    |     3:30:25 PM |  22:48:576    |\n" +
			"12/26/15 15:15:25 | Notify: PlagHaMincha@3:30:25 PM (15:0:0)      | Shkia           |     4:23:14 PM |  30:00:000    |\n" +
			"12/26/15 15:45:25 | Sleep                                         | Shkia           |     4:23:14 PM |  22:48:586    |\n" +
			"12/26/15 16:08:14 | Notify: Shkia@4:23:14 PM (15:0:0)             | LatestMincha    |     4:32:44 PM |  09:30:334    |\n" +
			"12/26/15 16:17:44 | Notify: LatestMincha@4:32:44 PM (15:0:0)      | BeinHashmashot  |     4:57:37 PM |  24:52:186    |\n" +
			"12/26/15 16:42:37 | Notify: BeinHashmashot@4:57:37 PM (15:0:0)    | ShabbatEnd      |     5:08:14 PM |  10:37:480    |\n" +
			"12/26/15 16:53:14 | Notify: ShabbatEnd@5:08:14 PM (15:0:0)        | TzaitHacochavim |     5:15:50 PM |  07:35:556    |\n" +
			"12/26/15 17:00:50 | Notify: TzaitHacochavim@5:15:50 PM (15:0:0)   | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 17:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 18:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 18:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 19:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 19:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 20:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 20:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 21:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 21:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 22:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 22:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 23:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 23:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  24:03:429    |\n" +
			"12/26/15 23:54:53 | Notify: NightChatzot@12:09:53 AM (15:0:0)     | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 00:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 00:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 01:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 01:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 02:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 02:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 03:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 03:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 04:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 04:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 05:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 05:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  05:41:255    |\n" +
			"12/27/15 06:00:34 | Notify: Alot@6:15:34 AM (15:0:0)              | Talit           |     6:52:53 AM |  30:00:000    |\n" +
			"12/27/15 06:30:34 | Sleep                                         | Talit           |     6:52:53 AM |  07:18:829    |\n" +
			"12/27/15 06:37:53 | Notify: Talit@6:52:53 AM (15:0:0)             | Hanetz          |     7:56:32 AM |  30:00:000    |\n" +
			"12/27/15 07:07:53 | Sleep                                         | Hanetz          |     7:56:32 AM |  30:00:000    |\n" +
			"12/27/15 07:37:53 | Sleep                                         | Hanetz          |     7:56:32 AM |  03:38:901    |\n" +
			"12/27/15 07:41:32 | Notify: Hanetz@7:56:32 AM (15:0:0)            | SofZmanShema    |     9:12:54 AM |  30:00:000    |\n" +
			"12/27/15 08:11:32 | Sleep                                         | SofZmanShema    |     9:12:54 AM |  30:00:000    |\n" +
			"12/27/15 08:41:32 | Sleep                                         | SofZmanShema    |     9:12:54 AM |  16:22:217    |\n" +
			"12/27/15 08:57:54 | Notify: SofZmanShema@9:12:54 AM (15:0:0)      | SofZmanTefila   |    10:45:41 AM |  30:00:000    |\n" +
			"12/27/15 09:27:54 | Sleep                                         | SofZmanTefila   |    10:45:41 AM |  30:00:000    |\n" +
			"12/27/15 09:57:54 | Sleep                                         | SofZmanTefila   |    10:45:41 AM |  30:00:000    |\n" +
			"12/27/15 10:27:54 | Sleep                                         | SofZmanTefila   |    10:45:41 AM |  02:46:487    |\n" +
			"12/27/15 10:30:41 | Notify: SofZmanTefila@10:45:41 AM (15:0:0)    | Chatzot         |    12:10:15 PM |  30:00:000    |\n" +
			"12/27/15 11:00:41 | Sleep                                         | Chatzot         |    12:10:15 PM |  30:00:000    |\n" +
			"12/27/15 11:30:41 | Sleep                                         | Chatzot         |    12:10:15 PM |  24:34:352    |\n" +
			"12/27/15 11:55:15 | Notify: Chatzot@12:10:15 PM (15:0:0)          | MinchaGedola    |    12:31:24 PM |  21:08:588    |\n" +
			"12/27/15 12:16:24 | Notify: MinchaGedola@12:31:24 PM (15:0:0)     | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 12:46:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 13:16:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 13:46:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 14:16:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  06:51:528    |\n" +
			"12/27/15 14:23:15 | Notify: MinchaKetana@2:38:15 PM (15:0:0)      | PlagHaMincha    |     3:31:07 PM |  30:00:000    |\n" +
			"12/27/15 14:53:15 | Sleep                                         | PlagHaMincha    |     3:31:07 PM |  22:51:470    |\n" +
			"12/27/15 15:16:07 | Notify: PlagHaMincha@3:31:07 PM (15:0:0)      | Shkia           |     4:23:58 PM |  30:00:000    |\n" +
			"12/27/15 15:46:07 | Sleep                                         | Shkia           |     4:23:58 PM |  22:51:473    |\n" +
			"12/27/15 16:08:58 | Notify: Shkia@4:23:58 PM (15:0:0)             | LatestMincha    |     4:33:29 PM |  09:30:861    |\n" +
			"12/27/15 16:18:29 | Notify: LatestMincha@4:33:29 PM (15:0:0)      | BeinHashmashot  |     4:58:20 PM |  24:50:699    |\n" +
			"12/27/15 16:43:20 | Notify: BeinHashmashot@4:58:20 PM (15:0:0)    | TzaitHacochavim |     5:16:32 PM |  18:11:852    |\n" +
			"12/27/15 17:01:32 | Notify: TzaitHacochavim@5:16:32 PM (15:0:0)   | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 17:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 18:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 18:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 19:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 19:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 20:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 20:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 21:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 21:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 22:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 22:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 23:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 23:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  23:50:272    |\n" +
			"12/27/15 23:55:22 | Notify: NightChatzot@12:10:22 AM (15:0:0)     | Alot            |     6:15:51 AM |  30:00:000    |\n"
		);
	}


	private static class AlwaysFifteenMinuteAlertsZmanCallback
		extends ZmanCallbackForTests {


		@Override
		public int getZmanAlertTime( final Zmanim.Type type ) {

			return FIFTEEN_MINUTES;
		}
	}


	private static class DefaultAlertsZmanCallback
		extends ZmanCallbackForTests {


		@Override
		public int getZmanAlertTime( final Zmanim.Type type ) {

			// Intended to match the default values in personal_prefs.xml.
			//noinspection Duplicates
			switch ( type ) {
				case Alot:
					return ZmanUtils.NO_ALERT;
				case Talit:
					return FIFTEEN_MINUTES;
				case Hanetz:
					return ZmanUtils.NO_ALERT;
				case SofZmanShema:
					return FIFTEEN_MINUTES;
				case SofZmanTefila:
					return FIFTEEN_MINUTES;
				case SofZmanAchilatChametz:
					return FIFTEEN_MINUTES;
				case SofZmanBiurChametz:
					return FIFTEEN_MINUTES;
				case Chatzot:
					return ZmanUtils.NO_ALERT;
				case MinchaGedola:
					return ZmanUtils.NO_ALERT;
				case MinchaKetana:
					return ZmanUtils.NO_ALERT;
				case PlagHaMincha:
					return FIFTEEN_MINUTES;
				case CandleLighting:
					return FIFTEEN_MINUTES;
				case Shkia:
					return FIFTEEN_MINUTES;
				case BeinHashmashot:
					return ZmanUtils.NO_ALERT;
				case LatestMincha:
					return FIFTEEN_MINUTES;
				case FastEnd:
					return FIFTEEN_MINUTES;
				case TzaitHacochavim:
					return ZmanUtils.NO_ALERT;
				case ShabbatEnd:
					return ZmanUtils.NO_ALERT;
				case NightChatzot:
					return ZmanUtils.NO_ALERT;
			}

			assert false;
			return ZmanUtils.NO_ALERT;
		}
	}

} // End of UT_WakeupScheduler_MultiDay class
