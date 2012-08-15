/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.zmanList.ZmanRowWrapper;
import com.gindin.zmanim.android.zmanList.ZmanimListAdapter;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.ZmanFormatter;
import com.gindin.zmanim.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import java.util.List;


/**
 * Actually displays the zmanim on the screen.
 */
@SuppressWarnings( { "UnnecessaryBoxing", "AutoUnboxing", "UnnecessaryUnboxing" } )
class ZmanimDisplay {

	/** The activity we're associated with. */
	private final ZmanimActivity                              zmanimActivity;

	private final ZmanFormatter                               zmanFormatter;

	/** Manages the selection process, allowing the user to select a different zman. */
	private final ZmanimSelector                              selector;

	/** Set/Get the choosen zmanim. */
	private final SharedPreferences                           preferences;

	private HebrewDate                                        currentDate;


	ZmanimDisplay(
		final ZmanimActivity  zmanimActivity,
		final ZmanFormatter   zmanFormatter
	) {

		this.zmanimActivity = zmanimActivity;
		this.zmanFormatter = zmanFormatter;

		selector = new ZmanimSelector( zmanimActivity, zmanFormatter );
		preferences = zmanimActivity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

		listenToRows();

		// Adding the listener will cause the event to get fired.
		DateChangedEvent.Listener dateChangeListener = new DateChangedEvent.Listener() {
			@Override
			public void onDateChanged( DateChangedEvent event ) {
				currentDate = event.getSource();
				zmanimActivity.setListAdapter( getListAdapter( currentDate ) );
			}
		};
		zmanimActivity.addDateChangeListener( dateChangeListener );
	}


	@SuppressWarnings( { "ValueOfIncrementOrDecrementUsed" } )
	private ListAdapter getListAdapter( HebrewDate date ) {

		List<Zmanim> daysZmanim = date.getZmanim();
		int numZmanim = daysZmanim.size();
		Zman[] zmanimToDisplay = new Zman[ numZmanim ];
		for ( int i = 0; i < numZmanim; i++ ) {
			zmanimToDisplay[i] = getZman( daysZmanim.get( i ) );
		}

		return new ListAdapter( zmanimActivity, zmanimToDisplay );
	}


	private void listenToRows() {

		final ZmanimSelector.SelectionCallback selectionCallback = new ZmanimSelector.SelectionCallback() {
			public void selected( Zmanim.Type type, Zman selection ) {

				final SharedPreferences.Editor editor = preferences.edit();
				editor.putString( type.name, selection.name );
				editor.commit();

				// This is a bit of a hammer, but it works...
				//  I tried to find the existing zman in the list, remove it, and add the newly selected one, but
				//  apparently the List which backs the adapter doesn't support remove().
				zmanimActivity.setListAdapter( getListAdapter( currentDate ) );
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

					ListAdapter adapter = (ListAdapter)zmanimActivity.getListAdapter();
					Zman zman = adapter.getItem( position );
					selector.selectFrom( currentDate, zman.type, selectionCallback );
				}
			}
		);

	}


	private Zman getZman(
		Zmanim      zmanim
	) {

		String zmanName = preferences.getString( zmanim.type.name, null );
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
			Zman[]  zmanim
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
