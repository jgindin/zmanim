package com.gindin.zmanim.android.service;

import com.gindin.zmanim.ZmanUtils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receives broadcast notifications, such as after the device has booted so that we can schedule our next alarm.
 */
public class ZmanimReceiver
	extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(
		Context context,
		Intent  intent
	) {

		Intent zmanimService = new Intent( context, ZmanimService.class );
		zmanimService.setAction( ZmanimService.Actions.SCHEDULE_ALARM.name() );

		String action = intent.getAction();
		if ( Intent.ACTION_BOOT_COMPLETED.equals( action ) ) {
			zmanimService.putExtra( ZmanimService.ActionParams.FORCE_SCHEDULE.name(), true );
		}


		// Unfortunately, when the intent is scheduled, the alarm intent gets the extra, and then
		//  from in here we have to create the intent that actually does the work. Because of that,
		//  we have to pass along this extra...
		ZmanUtils.NotifiedZmanim notifiedZmanim = (ZmanUtils.NotifiedZmanim)intent.getSerializableExtra( ZmanimService.ActionParams.NOTIFIED_ZMANIM_EXTRA.name() );
		zmanimService.putExtra( ZmanimService.ActionParams.NOTIFIED_ZMANIM_EXTRA.name(), notifiedZmanim );

		startWakefulService( context, zmanimService );
	}

}
