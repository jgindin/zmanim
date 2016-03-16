package com.gindin.zmanlib;

import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Test;

/**
 *
 */
public class UT_WakeupScheduler_MultiDay_ConfiguredAlerts
	extends WakeupSchedulerBase_MutliDay {


	@Test
	public void december_7_2014() {
		december_7_2014( new ConfiguredAlertsZmanCallback(),
			""
		);
	}


	@Test
	public void december_21_2015_1_week() {
		december_21_2015_1_week( new ConfiguredAlertsZmanCallback(),
			""
		);
	}



	private static class ConfiguredAlertsZmanCallback
		extends UT_ZmanUtils.ZmanCallbackForTests {


		@Override
		public int getZmanAlertTime( final Zmanim.Type type ) {

			switch ( type ) {
				case Alot:
					return ZmanUtils.NO_ALERT;
				case Talit:
					return UT_ZmanUtils.FIFTEEN_MINUTES;
				case Hanetz:
					return ZmanUtils.NO_ALERT;
				case SofZmanShema:
					return UT_ZmanUtils.THIRTY_MINUTES;
				case SofZmanTefila:
					return UT_ZmanUtils.FIFTEEN_MINUTES;
				case SofZmanAchilatChametz:
					return UT_ZmanUtils.ONE_HOUR;
				case SofZmanBiurChametz:
					return UT_ZmanUtils.ONE_HOUR;
				case Chatzot:
					return ZmanUtils.NO_ALERT;
				case MinchaGedola:
					return ZmanUtils.NO_ALERT;
				case MinchaKetana:
					return ZmanUtils.NO_ALERT;
				case PlagHaMincha:
					return UT_ZmanUtils.FIFTEEN_MINUTES;
				case CandleLighting:
					return UT_ZmanUtils.FIFTEEN_MINUTES;
				case Shkia:
					return UT_ZmanUtils.ZERO_MINUTES;
				case BeinHashmashot:
					return ZmanUtils.NO_ALERT;
				case LatestMincha:
					return UT_ZmanUtils.TWENTY_MINUTES;
				case FastEnd:
					return UT_ZmanUtils.FIVE_MINUTES;
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
