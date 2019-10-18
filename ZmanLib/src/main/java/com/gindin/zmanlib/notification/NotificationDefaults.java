/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.notification;

import com.gindin.zmanlib.zman.Zmanim;

/**
 * Provides the out-of-the-box notification defaults.
 */
public final class NotificationDefaults {

  public int getDefaultZmanAlertTime( final Zmanim.Type type ) {

    switch ( type ) {
      case Alot:
      case Hanetz:
      case Chatzot:
      case MinchaGedola:
      case MinchaKetana:
      case BeinHashmashot:
      case TzaitHacochavim:
      case ShabbatEnd:
      case NightChatzot:
        return ZmanNotificationScheduler.NO_ALERT;

      case Talit:
      case SofZmanShema:
      case SofZmanTefila:
      case SofZmanAchilatChametz:
      case SofZmanBiurChametz:
      case PlagHaMincha:
      case CandleLighting:
      case Shkia:
      case LatestMincha:
      case FastEnd:
      case YomTovSheniCandleLighting:
        return ZmanNotificationScheduler.FIFTEEN_MINUTES;
    }

    assert false;
    return ZmanNotificationScheduler.NO_ALERT;
  }

}
