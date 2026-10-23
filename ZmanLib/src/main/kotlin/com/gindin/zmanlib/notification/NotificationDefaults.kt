/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.notification

import com.gindin.zmanlib.zman.Zmanim

/** Provides the out-of-the-box notification defaults. */
public class NotificationDefaults {

  public fun getDefaultZmanAlertTime(type: Zmanim.Type): Int {

    return when (type) {
      Zmanim.Type.Alot,
      Zmanim.Type.Hanetz,
      Zmanim.Type.Chatzot,
      Zmanim.Type.MinchaGedola,
      Zmanim.Type.MinchaKetana,
      Zmanim.Type.BeinHashmashot,
      Zmanim.Type.TzaitHacochavim,
      Zmanim.Type.ShabbatEnd,
      Zmanim.Type.NightChatzot -> ZmanNotificationScheduler.NO_ALERT

      Zmanim.Type.Talit,
      Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,
      Zmanim.Type.SofZmanAchilatChametz,
      Zmanim.Type.SofZmanBiurChametz,
      Zmanim.Type.PlagHaMincha,
      Zmanim.Type.CandleLighting,
      Zmanim.Type.Shkia,
      Zmanim.Type.LatestMincha,
      Zmanim.Type.FastEnd,
      Zmanim.Type.YomTovSheniCandleLighting -> ZmanNotificationScheduler.FIFTEEN_MINUTES
    }
  }
}
