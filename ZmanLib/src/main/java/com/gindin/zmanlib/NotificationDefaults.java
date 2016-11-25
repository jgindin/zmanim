package com.gindin.zmanlib;

import com.gindin.zmanlib.zman.Zmanim;

/**
 * Provides the out-of-the-box notification defaults.
 */
public final class NotificationDefaults {

  public int getDefaultZmanAlertTime( final Zmanim.Type type ) {

    switch ( type ) {
      case Alot:
        return ZmanNotificationScheduler.NO_ALERT;
      case Talit:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case Hanetz:
        return ZmanNotificationScheduler.NO_ALERT;
      case SofZmanShema:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case SofZmanTefila:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case SofZmanAchilatChametz:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case SofZmanBiurChametz:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case Chatzot:
        return ZmanNotificationScheduler.NO_ALERT;
      case MinchaGedola:
        return ZmanNotificationScheduler.NO_ALERT;
      case MinchaKetana:
        return ZmanNotificationScheduler.NO_ALERT;
      case PlagHaMincha:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case CandleLighting:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case Shkia:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case BeinHashmashot:
        return ZmanNotificationScheduler.NO_ALERT;
      case LatestMincha:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case FastEnd:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
      case TzaitHacochavim:
        return ZmanNotificationScheduler.NO_ALERT;
      case ShabbatEnd:
        return ZmanNotificationScheduler.NO_ALERT;
      case NightChatzot:
        return ZmanNotificationScheduler.NO_ALERT;
    }

    assert false;
    return ZmanNotificationScheduler.NO_ALERT;
  }

}
