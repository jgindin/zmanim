/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily;

import com.gindin.mvp.AbstractPresenter;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.formatters.HebrewDateFormatter;
import com.gindin.zmanim.calendar.formatters.SecularDateFormatter;
import com.gindin.zmanim.location.ZmanimLocation;

import android.content.res.Resources;

/**
 *
 */
class DatePresenterImpl<V extends DateView>
	extends AbstractPresenter<V>
	implements DatePresenter<V> {

	private final Resources                                   resources;

	private final HebrewDateFormatter                         hebrewDateFormatter;

	private final SecularDateFormatter                        secularDateFormatter;

	private final HebrewDateChangedEvent.Handler              dateChangeHandler;

	private final LocationAcquiredEvent.Handler               locationAcquiredHandler;


	private ZmanimLocation                                    currentLocation;

	private HebrewDate                                        currentDate;


	DatePresenterImpl(
		V                     view,
		EventBus              eventBus,
		Resources             resources,
		HebrewDateFormatter   hebrewDateFormatter,
		SecularDateFormatter  secularDateFormatter
	) {

		super( view, eventBus );
		this.resources = resources;
		this.hebrewDateFormatter = hebrewDateFormatter;
		this.secularDateFormatter = secularDateFormatter;

		dateChangeHandler = new HebrewDateChangedEvent.Handler() {
			@Override
			public void onHebrewDateChanged( HebrewDate newDate ) {
				handleDateChanged( newDate );
			}
		};
		eventBus.addHandler( dateChangeHandler, true );

		locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation location ) {
				handleLocationChanged( location );
			}

			@Override
			public void onError( LocationError error, ZmanimLocation lastKnownLocation ) {
				handleLocationChanged( lastKnownLocation );
			}
		};
		eventBus.addHandler( locationAcquiredHandler, true );
	}


	@Override
	public void destroy() {

		getEventBus().removeHandler( dateChangeHandler );
		getEventBus().removeHandler( locationAcquiredHandler );
	}


	@Override
	public void userWantsToChangeDate() {

		getView().allowUserToChangeDate( currentDate );
	}


	@Override
	public void userChangedDate(
		int year,
		int month,
		int date
	) {

		// Seems like this callback gets invoked multiple times. That's bad...we don't want to do all the work
		//  involved in changing dates if we're not actually changing dates...
		if ( ( currentDate.getGregorianYear() != year ) ||
				 ( currentDate.getGregorianMonth() != month ) ||
				 ( currentDate.getGregorianDayOfMonth() != date ) ) {

			currentDate = HebrewCalendar.getForDate( year, month, date );
			getEventBus().fire( new HebrewDateChangedEvent( currentDate ) );
		}
	}


	private void handleLocationChanged( ZmanimLocation location ) {
		currentLocation = location;
		displayDate();
		displayLocationName();
	}


	private void handleDateChanged( HebrewDate newDate ) {
		currentDate = newDate;
		displayDate();
		displayDateDescription();
	}


	private void displayLocationName() {

		String locationName;
		if ( null == currentLocation ) {
			locationName = resources.getString( R.string.unknown_location );
		}
		else {
			locationName = currentLocation.getLocationName();
		}

		getView().displayLocation( locationName );
	}


	private void displayDate() {

		if ( ( currentLocation == null ) || ( currentDate == null ) ) {
			return;
		}

		getView().displayDate(
			hebrewDateFormatter.format( currentDate, currentLocation ),
			secularDateFormatter.format( currentDate )
		);
	}


	private void displayDateDescription() {

		String description;
		if ( currentDate == null ) {
			description = null;
		}
		else {
			description = currentDate.getDescription();
		}

		getView().displayDescriptionOfTheDay( description );
	}


} // End of DatePresenterImpl class
