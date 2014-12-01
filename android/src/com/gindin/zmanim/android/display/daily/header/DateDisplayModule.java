/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.AbstractMvpModule;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.calendar.formatters.HebrewDateFormatter;
import com.gindin.zmanim.calendar.formatters.SecularDateFormatter;

import android.support.v4.app.FragmentActivity;

/**
 *
 */
public class DateDisplayModule
	extends AbstractMvpModule<DatePresenter<DateView>> {


	public DateDisplayModule(
		FragmentActivity      activity,
		EventBus              eventBus
	) {
		super( activity, eventBus );
	}


	@Override
	public DatePresenter<DateView> create() {

		// Seems quite likely we'll want these injected...
		HebrewDateFormatter hebrewDateFormatter = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.SHORT );
		SecularDateFormatter secularDateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.DEFAULT );

		DateViewImpl<DatePresenter> view = new DateViewImpl<DatePresenter>( activity );
		DatePresenterImpl<DateView> impl = new DatePresenterImpl<DateView>( view, eventBus, hebrewDateFormatter, secularDateFormatter );

		return impl;
	}


} // End of DatePresenterModule class
