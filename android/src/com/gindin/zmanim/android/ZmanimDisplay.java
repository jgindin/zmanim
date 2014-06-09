/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RawLocationAcquiredEvent;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.zmanList.ZmanRowWrapper;
import com.gindin.zmanim.android.zmanList.ZmanimListAdapter;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.ZmanFormatter;
import com.gindin.zmanim.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import java.util.Arrays;
import java.util.List;


/**
 * Actually displays the zmanim on the screen.
 */
@SuppressWarnings( { "UnnecessaryBoxing", "AutoUnboxing", "UnnecessaryUnboxing" } )
class ZmanimDisplay {

	/** The activity we're associated with. */
	private final ZmanimActivity                              zmanimActivity;

	private final EventBus                                    eventBus;

	private ZmanFormatter                                     zmanFormatter;

	/** Manages the selection process, allowing the user to select a different zman. */
	private ZmanimSelector                                    selector;

	/** Set/Get the choosen zmanim. */
	private final SharedPreferences                           zmanimSelectionPrefs;

	private HebrewDate                                        currentDate;
	private final HebrewDateChangedEvent.Handler              dateChangeHandler;

	private ZmanimLocation                                    currentLocation;

	private final RawLocationAcquiredEvent.Handler            locationAcquiredHandler;


	ZmanimDisplay(
		final ZmanimActivity  zmanimActivity,
		final EventBus        eventBus,
		final ZmanFormatter   zmanFormatter
	) {

		this.zmanimActivity = zmanimActivity;
		this.eventBus = eventBus;
		this.zmanFormatter = zmanFormatter;

		zmanimSelectionPrefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

		listenToRows();

		dateChangeHandler = new HebrewDateChangedEvent.Handler() {
			@Override
			public void onHebrewDateChanged( HebrewDate newDate ) {
				currentDate = newDate;
				updateListAdapater();
			}
		};
		eventBus.addHandler( dateChangeHandler, true );

		locationAcquiredHandler = new RawLocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation location ) {
				handleLocationChanged( location );
			}

			@Override
			public void onError(
				LocationError   error,
				ZmanimLocation  lastKnownLocation
			) {
				handleLocationChanged( lastKnownLocation );
			}
		};
		eventBus.addHandler( locationAcquiredHandler, true );
	}


	void onDestroy() {

		eventBus.removeHandler( dateChangeHandler );
		eventBus.removeHandler( locationAcquiredHandler );
	}


	public void setZmanFormatter( ZmanFormatter zmanFormatter ) {

		if ( this.zmanFormatter != zmanFormatter ) {
			this.zmanFormatter = zmanFormatter;
			updateListAdapater();
		}
	}


	private void updateListAdapater() {
		ListAdapter listAdapter = getListAdapter( currentDate, currentLocation );
		if ( null != listAdapter ) {
			zmanimActivity.setAdapter( listAdapter );
		}
	}


	private void handleLocationChanged(
		ZmanimLocation newLocation
	) {

		ZmanimLocation lastLocation = currentLocation;
		currentLocation = newLocation;

		// Before we do anything, make sure that there's a significant change worthy of our time...
		if ( ( null != lastLocation ) && ( null != currentLocation ) ) {

			// Less than 1km? Don't bother...
			// TODO: I suppose you could run into an issue if that 1km puts you in a different time zone
			// TODO:  or something like that...
			if ( currentLocation.getGeodesicDistance( lastLocation ) < 1000 ) {
				return;
			}
		}

		SharedPreferences personalPrefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		boolean useElevation = personalPrefs.getBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false );
		if ( !useElevation && ( null != currentLocation) ) {
			currentLocation.setElevation( 0.0 );
		}

		ListAdapter listAdapter = getListAdapter( currentDate, currentLocation );
		if ( null != listAdapter ) {
			zmanimActivity.setAdapter( listAdapter );
		}
	}


	private ListAdapter getListAdapter(
		HebrewDate      date,
		ZmanimLocation  location
	) {

		// If we don't yet have the current date or the current location, just wait...
		if ( ( null == date ) || ( null == location ) ) {
			return null;
		}

		List<Zmanim> daysZmanim = date.getZmanim( location );
		int numZmanim = daysZmanim.size();
		Zman[] zmanimToDisplay = new Zman[ numZmanim ];
		for ( int i = 0; i < numZmanim; i++ ) {
			zmanimToDisplay[i] = getZman( daysZmanim.get( i ) );
		}

		// TODO: Rather than creating a new ListAdapter *every* time, it seems that  we ought to be
		// TODO:  able to update and reuse...
		return new ListAdapter( zmanimActivity, Arrays.asList( zmanimToDisplay ) );
	}


	private void listenToRows() {

		final ZmanimSelector.SelectionCallback selectionCallback = new ZmanimSelector.SelectionCallback() {
			public void selected( Zmanim.Type type, Zman selection ) {

				final SharedPreferences.Editor editor = zmanimSelectionPrefs.edit();
				editor.putString( type.name, selection.name );
				editor.commit();

				LogUtils.logEvent( "Zman Change", Arrays.asList( new Pair<String, String>( type.name, selection.name ) ) );

				// This is a bit of a hammer, but it works...
				//  I tried to find the existing zman in the list, remove it, and add the newly selected one, but
				//  apparently the List which backs the adapter doesn't support remove().
				zmanimActivity.setAdapter( getListAdapter( currentDate, currentLocation ) );
			}
		};

		zmanimActivity.getListView().setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				public void onItemClick(
					AdapterView<?>  adapterView,
					View            view,
					int             position,
					long            id
				) {

					if ( null == selector ) {
						selector = new ZmanimSelector( zmanimActivity, zmanFormatter );
					}

					ListAdapter adapter = (ListAdapter)zmanimActivity.getListView().getAdapter();
					Zman zman = adapter.getItem( position );
					selector.selectFrom( currentDate, currentLocation, zman.type, selectionCallback );
				}
			}
		);

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


	private class ListAdapter
		extends ZmanimListAdapter {

		private ListAdapter(
			Context context,
			List<Zman>  zmanim
		) {
			super( context, zmanim );
		}


		@Override
		protected ZmanRowWrapper createRowWrapper( View rowView ) {
			return new RowWrapper( rowView, zmanFormatter );
		}

	}


	private static class RowWrapper
		extends ZmanRowWrapper {

		private RowWrapper(
			View          zmanRowView,
			ZmanFormatter zmanimCalendar
		) {
			super( zmanRowView, zmanimCalendar );
		}


		@Override
		protected void updateTextFields( Zman zman ) {
			nameField.setText( zman.type.name );
			descriptionField.setText( zman.name );
		}
	}


}  // End of ZmanimDisplay class
