/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.zmanList.ZmanRowWrapper;
import com.gindin.zmanim.android.zmanList.ZmanimListAdapter;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;


/**
 * Actually displays the zmanim on the screen.
 */
@SuppressWarnings( { "UnnecessaryBoxing", "AutoUnboxing", "UnnecessaryUnboxing" } )
class ZmanimDisplay {

	/** The activity we're associated with. */
	private final ListActivity                                listActivity;

	/** The calendar we use for displaying everything. */
	private final HebrewZmanimCalendar                        zmanimCalendar;

	/** Manages the selection process, allowing the user to select a different zman. */
	private final ZmanimSelector                              selector;

	/** Set/Get the choosen zmanim. */
	private final SharedPreferences                           preferences;


	ZmanimDisplay(
		final ListActivity    listActivity,
		HebrewZmanimCalendar  zmanimCalendar
	) {

		this.listActivity = listActivity;
		this.zmanimCalendar = zmanimCalendar;

		selector = new ZmanimSelector( listActivity, zmanimCalendar );

		listenToRows();

		zmanimCalendar.addListener(
			new HebrewZmanimCalendar.Listener() {
				public void dateChanged() {
					listActivity.setListAdapter( getListAdapter() );
				}
			}
		);

		preferences = listActivity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

		listActivity.setListAdapter( getListAdapter() );
	}


	@SuppressWarnings( { "ValueOfIncrementOrDecrementUsed" } )
	private ListAdapter getListAdapter() {

		int numZmanim = Zmanim.Type.values().length;

		final boolean showCandles = zmanimCalendar.showCandlelighting();
		if ( !showCandles ) {
			numZmanim--;
		}

		Zman[] zmanimToDisplay = new Zman[ numZmanim ];

		int index = 0;
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.Alot );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.Talit );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.Hanetz );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.SofZmanShema );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.SofZmanTefila );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.Chatzot );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.MinchaGedola );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.MinchaKetana );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.PlagHaMincha );

		if ( showCandles ) {
			zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.CandleLighting );
		}

		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.Shkia );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.LatestMincha );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.BeinHashmashot );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.TzaitHacochavim );
		zmanimToDisplay[ index++ ] = getZmanForType( Zmanim.Type.NightChatzot );

		return new ListAdapter( listActivity, zmanimToDisplay );
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
				listActivity.setListAdapter( getListAdapter() );
			}
		};

		listActivity.getListView().setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				public void onItemClick(
					AdapterView<?>  adapterView,
					View            view,
					int             position,
					long            id
				) {

					ListAdapter adapter = (ListAdapter)listActivity.getListAdapter();
					Zman zman = adapter.getItem( position );
					selector.selectFrom( zman.type, selectionCallback );
				}
			}
		);

	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private Zman getZmanForType(
		Zmanim.Type type
	) {

		Zmanim zmanim = zmanimCalendar.get( type );
		String zmanName = preferences.getString( type.name, zmanim.getDefault().name );
		Zman zman = zmanim.getByName( zmanName );
		if ( null == zman ) {
			// Should only happen in development builds if I change the name of a zman...
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
			return new RowWrapper( rowView, zmanimCalendar );
		}

	}


	private static class RowWrapper
		extends ZmanRowWrapper {

		private RowWrapper(
			View                  zmanRowView,
			HebrewZmanimCalendar  zmanimCalendar
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
