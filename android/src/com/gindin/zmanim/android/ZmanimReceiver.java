package com.gindin.zmanim.android;

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

		startWakefulService( context, zmanimService );
	}

}
