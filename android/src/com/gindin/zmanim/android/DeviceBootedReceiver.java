package com.gindin.zmanim.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receives notification after the device has booted so that we can schedule our next alarm.
 */
public class DeviceBootedReceiver
	extends BroadcastReceiver {

	@Override
	public void onReceive(
		Context context,
		Intent  intent
	) {

		Intent zmanimService = new Intent( context, ZmanimService.class );
		zmanimService.putExtra( ZmanimService.Commands.SCHEDULE_ALARM.name(), true );
		context.startService( zmanimService );
	}

}
