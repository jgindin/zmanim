/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.AbstractPresenter;
import com.gindin.util.BaseAsyncClass;
import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.ZmanFormatter;
import com.gindin.zmanim.zman.Zmanim;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.List;

/**
 *
 */
class ZmanimPresenterImpl<V extends ZmanimView>
	extends AbstractPresenter<V>
	implements ZmanimPresenter<V> {


	/** Get the chosen zmanim. */
	private final SharedPreferences                           zmanimSelectionPrefs;

	private final ZmanFormatter                               zmanFormatter;

	private final HebrewDateChangedEvent.Handler              dateChangeHandler;

	private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

	private HebrewDate                                        currentDate;

	private ZmanimLocation                                    currentLocation;

	private AsyncTask<?,?,?>                                  zmanimCalculator;


	ZmanimPresenterImpl(
		V                 view,
		EventBus          eventBus,
		SharedPreferences zmanimSelectionPrefs,
		ZmanFormatter     zmanFormatter
	) {

		super( view, eventBus );
		this.zmanimSelectionPrefs = zmanimSelectionPrefs;
		this.zmanFormatter = zmanFormatter;

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
				// Don't bother updating if there's an error AND we already have a location.
				if ( currentLocation == null ) {
					handleLocationChanged( lastKnownLocation );
				}
			}
		};
		eventBus.addHandler( locationAcquiredHandler, true );
	}


	@Override
	public void destroy() {
		getEventBus().removeHandler( dateChangeHandler );
		getEventBus().removeHandler( locationAcquiredHandler );
	}


	private void handleDateChanged( HebrewDate newDate ) {
		currentDate = newDate;

		if ( currentLocation == null ) {
			return;
		}

		notifyViewOfZmanim();
	}


	private void handleLocationChanged( ZmanimLocation newLocation ) {

		currentLocation = newLocation;

		if ( currentDate == null ) {
			return;
		}

		notifyViewOfZmanim();
	}


	private void notifyViewOfZmanim() {

		assert currentDate != null;
		assert currentLocation != null;

		List<Zmanim> daysZmanim = currentDate.getZmanim( currentLocation );
		Zman[] zmanimToDisplay = new Zman[ daysZmanim.size() ];
		for ( int i = 0; i < daysZmanim.size(); i++ ) {
			zmanimToDisplay[i] = getZman( daysZmanim.get( i ) );
		}

		// Tell the view which zmanim are applicable to this day.
		getView().displayZmanim( zmanimToDisplay );

		// And now start a calculation of each of those zmanim.
		calculateZmanim( zmanimToDisplay );
	}


	private Zman getZman(
		Zmanim      zmanim
	) {

		String zmanName = zmanimSelectionPrefs.getString( zmanim.type.name, null );
		Zman zman = zmanim.getByName( zmanName );
		if ( null == zman ) {
			zman = zmanim.getDefault();
		}

		return zman;
	}


	private void calculateZmanim( Zman... zmanimToDisplay ) {

		if ( zmanimCalculator != null ) {
			zmanimCalculator.cancel( true );
		}

		BaseAsyncClass.Callbacks<Zman,Pair<Zman,String>,Void> callbacks = new BaseAsyncClass.Callbacks<Zman, Pair<Zman,String>, Void>() {

			@Override
			protected Void doInBackground(
				BaseAsyncClass.ProgressPublisher<Pair<Zman, String>>  publisher,
				BaseAsyncClass.CancelChecker                          cancelChecker,
				Zman...                                               zmanim
			) {
				for ( Zman zman : zmanim ) {
					if ( cancelChecker.isCancelled() ) {
						return null;
					}

					String formattedTime = zmanFormatter.formatZmanTime( zman );
		      publisher.publish( new Pair<Zman, String>( zman, formattedTime ) );
				}
				return null;
			}


			@Override
			protected void onProgressUpdate( Pair<Zman,String>... values ) {
				for ( Pair<Zman, String> value : values ) {
					getView().displayTimeForZman( value.first, value.second );
				}
			}
		};

		zmanimCalculator = new BaseAsyncClass<Zman, Pair<Zman, String>, Void>( callbacks );
		zmanimCalculator.execute( zmanimToDisplay );
	}


} // End of DailyPresenterImpl class
