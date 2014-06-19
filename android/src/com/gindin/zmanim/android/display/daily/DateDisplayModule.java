/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily;

import com.gindin.mvp.MvpModule;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.calendar.formatters.HebrewDateFormatter;
import com.gindin.zmanim.calendar.formatters.SecularDateFormatter;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

/**
 *
 */
public class DateDisplayModule
	implements MvpModule<DatePresenter<DateView>> {

	private final FragmentActivity                            activity;

	private final EventBus                                    eventBus;



	public DateDisplayModule(
		FragmentActivity      activity,
		EventBus              eventBus
	) {
		this.activity = activity;
		this.eventBus = eventBus;
	}


	@Override
	public DatePresenter<DateView> create() {

		// Seems quite likely we'll want these injected...
		HebrewDateFormatter hebrewDateFormatter = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.SHORT );
		SecularDateFormatter secularDateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.DEFAULT );

		Resources resources = activity.getResources();

		DateViewImpl<DatePresenter> view = new DateViewImpl<DatePresenter>( activity );
		DatePresenterImpl<DateView> impl = new DatePresenterImpl<DateView>( view, eventBus, resources, hebrewDateFormatter, secularDateFormatter );

		return impl;
	}


} // End of DatePresenterModule class
