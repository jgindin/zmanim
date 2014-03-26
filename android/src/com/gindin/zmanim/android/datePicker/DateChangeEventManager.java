/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.datePicker;

import com.gindin.util.LogUtils;
import com.gindin.util.events.EventManager;
import com.gindin.zmanim.calendar.HebrewDate;

/**
 * Manages date change events.
 *
 * TODO: Inject this to those who need it!
 */
public class DateChangeEventManager
	extends EventManager<DateChangedEvent.Listener, HebrewDate, DateChangedEvent> {

	@Override
	protected void dispatch(
		DateChangedEvent.Listener listener,
		DateChangedEvent          event
	) {
		listener.onDateChanged( event );
	}


	@Override
	protected void handleException( Exception e ) {
		LogUtils.logError( "date change dispatch", e.getMessage(), e );
	}
}
