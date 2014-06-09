/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.datePicker;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.DatePicker;

/**
 * Allow the user to change the secular date.
 */
public class SecularDatePicker {

	private final EventBus                                    eventBus;

	/** Lazily created. */
	private DatePickerDialog                                  secularDatePicker;

	private final DatePickerDialog.OnDateSetListener          dateSetListener;

	private HebrewDate                                        lastDisplayedDate;


	public SecularDatePicker(
		EventBus  eventBus
	) {
		this.eventBus = eventBus;

		dateSetListener = createDateSetListener();
	}


	public Dialog create(
		Context     owner
	) {

		// Create with an arbitrary date...
		secularDatePicker = new DatePickerDialog( owner, dateSetListener, 1970, 5, 1 );

		return secularDatePicker;
	}


	@SuppressWarnings( "FeatureEnvy" )
	public void changeDate(
		HebrewDate  hebrewDate
	) {

		lastDisplayedDate = hebrewDate;
		secularDatePicker.updateDate( hebrewDate.getGregorianYear(), hebrewDate.getGregorianMonth(), hebrewDate.getGregorianDayOfMonth() );
	}


	@SuppressWarnings( "FeatureEnvy" )
	private DatePickerDialog.OnDateSetListener createDateSetListener() {

		return new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(
				DatePicker  datePicker,
				int         year,
				int         month,
				int         date
			) {

				// Seems like this callback gets invoked multiple times. That's bad...we don't want to do all the work
				//  involved in changing dates if we're not actually changing dates...
				if ( ( lastDisplayedDate.getGregorianYear() != year ) ||
						 ( lastDisplayedDate.getGregorianMonth() != month ) ||
						 ( lastDisplayedDate.getGregorianDayOfMonth() != date ) ) {

					lastDisplayedDate = HebrewCalendar.getForDate( year, month, date );
					eventBus.fire( new HebrewDateChangedEvent( lastDisplayedDate ) );
				}
			}
		};

	}

} // End of SecularDatePicker class
