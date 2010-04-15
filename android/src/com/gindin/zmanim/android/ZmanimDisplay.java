/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;

import java.util.HashMap;
import java.util.Map;

/**
 * Actually displays the zmanim on the screen.
 */
@SuppressWarnings( { "UnnecessaryBoxing", "AutoUnboxing", "UnnecessaryUnboxing" } )
class ZmanimDisplay {

	/** Maps from the Zmanim type to the ID of the field that contains it. */
	private static final Map<Zmanim.Type, Integer>            zmanimTypeToFieldID = new HashMap<Zmanim.Type, Integer>();

	private static final Map<Zmanim.Type, Integer>            zmanimTypeToRowID = new HashMap<Zmanim.Type, Integer>();
	
	static {
		zmanimTypeToFieldID.put( Zmanim.Type.Alot, Integer.valueOf( R.id.alot ) );
		zmanimTypeToFieldID.put( Zmanim.Type.Talit, Integer.valueOf( R.id.talit ) );
		zmanimTypeToFieldID.put( Zmanim.Type.Hanetz, Integer.valueOf( R.id.hanetz ) );
		zmanimTypeToFieldID.put( Zmanim.Type.SofZmanShema, Integer.valueOf( R.id.sofZmanShema ) );
		zmanimTypeToFieldID.put( Zmanim.Type.SofZmanTefila, Integer.valueOf( R.id.sofZmanTefilah ) );
		zmanimTypeToFieldID.put( Zmanim.Type.Chatzot, Integer.valueOf( R.id.chatzot ) );
		zmanimTypeToFieldID.put( Zmanim.Type.MinchaGedola, Integer.valueOf( R.id.minchaGedola ) );
		zmanimTypeToFieldID.put( Zmanim.Type.MinchaKetana, Integer.valueOf( R.id.minchaKetana ) );
		zmanimTypeToFieldID.put( Zmanim.Type.PlagHaMincha, Integer.valueOf( R.id.plagHamincha ) );
		zmanimTypeToFieldID.put( Zmanim.Type.CandleLighting, Integer.valueOf( R.id.candles ) );
		zmanimTypeToFieldID.put( Zmanim.Type.Shkia, Integer.valueOf( R.id.shkiah ) );
		zmanimTypeToFieldID.put( Zmanim.Type.LatestMincha, Integer.valueOf( R.id.mincha ) );
		zmanimTypeToFieldID.put( Zmanim.Type.TzaitHachocavim, Integer.valueOf( R.id.tzait ) );

		zmanimTypeToRowID.put( Zmanim.Type.Alot, Integer.valueOf( R.id.alot_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.Talit, Integer.valueOf( R.id.talit_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.Hanetz, Integer.valueOf( R.id.hanetz_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.SofZmanShema, Integer.valueOf( R.id.sofZmanShema_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.SofZmanTefila, Integer.valueOf( R.id.sofZmanTefilah_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.Chatzot, Integer.valueOf( R.id.chatzot_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.MinchaGedola, Integer.valueOf( R.id.minchaGedola_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.MinchaKetana, Integer.valueOf( R.id.minchaKetana_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.PlagHaMincha, Integer.valueOf( R.id.plagHamincha_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.CandleLighting, Integer.valueOf( R.id.candles_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.Shkia, Integer.valueOf( R.id.shkiah_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.LatestMincha, Integer.valueOf( R.id.mincha_row ) );
		zmanimTypeToRowID.put( Zmanim.Type.TzaitHachocavim, Integer.valueOf( R.id.tzait_row ) );
	}


	/** The calendar we use for displaying everything. */
	private final HebrewZmanimCalendar                        zmanimCalendar;

	/** The view where we're going to display all the zmanim. */
	private final View                                        zmanimView;

	/** Manages the selection process, allowing the user to select a different zman. */
	private final ZmanimSelector                              selector;

	/** Set/Get the choosen zmanim. */
	private final SharedPreferences                           preferences;


	ZmanimDisplay(
		Context               context,
		View                  zmanimView,
		HebrewZmanimCalendar  zmanimCalendar
	) {

		this.zmanimView = zmanimView;
		this.zmanimCalendar = zmanimCalendar;

		selector = new ZmanimSelector( context, zmanimCalendar );

		listenToRows();

		zmanimCalendar.addListener(
			new HebrewZmanimCalendar.Listener() {
				public void dateChanged() {
					update();
				}
			}
		);


		preferences = context.getSharedPreferences( "ZmanimSelection", Context.MODE_PRIVATE );

		// Make sure we're starting off with the right stuff...
		update();
	}


	private void listenToRows() {

		final ZmanimSelector.SelectionCallback selectionCallback = new ZmanimSelector.SelectionCallback() {
			public void selected( Zmanim.Type type, Zman selection ) {

				final SharedPreferences.Editor editor = preferences.edit();
				editor.putString( type.name, selection.name );
				editor.commit();

				displayZman( type );
			}
		};

		for ( final Map.Entry<Zmanim.Type, Integer> typeToRowIdEntry : zmanimTypeToRowID.entrySet() ) {

			View row = zmanimView.findViewById( typeToRowIdEntry.getValue() );
			row.setOnClickListener(
				new View.OnClickListener() {
					public void onClick( View view ) {
						selector.selectFrom( typeToRowIdEntry.getKey(), selectionCallback );
					}
				}
			);
		}
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	void update() {

		// TODO:
		//  1. Calculate on separate threads!!!!
		//  2. Make each row clickable, and popup a choice for which zman. Use preferences!

		displayZman( Zmanim.Type.Alot );
		displayZman( Zmanim.Type.Talit );
		displayZman( Zmanim.Type.Hanetz );
		displayZman( Zmanim.Type.SofZmanShema );
		displayZman( Zmanim.Type.SofZmanTefila );
		displayZman( Zmanim.Type.Chatzot );
		displayZman( Zmanim.Type.MinchaGedola );
		displayZman( Zmanim.Type.MinchaKetana );
		displayZman( Zmanim.Type.PlagHaMincha );

		if ( zmanimCalendar.showCandlelighting() ) {
			zmanimView.findViewById( R.id.candles_row ).setVisibility( View.VISIBLE );
			displayZman( Zmanim.Type.CandleLighting );
		}
		else {
			zmanimView.findViewById( R.id.candles_row ).setVisibility( View.GONE );
		}

		displayZman( Zmanim.Type.Shkia );
		displayZman( Zmanim.Type.LatestMincha );
		displayZman( Zmanim.Type.TzaitHachocavim );
	}


	private void displayZman(
		Zmanim.Type type
	) {

		Zmanim zmanim = zmanimCalendar.get( type );
		String zmanName = preferences.getString( type.name, zmanim.getDefault().name );
		Zman zman = zmanim.getByName( zmanName );
		if ( null == zman ) {
			// Should only happen in development builds if I change the name of a zman...
			zman = zmanim.getDefault();
		}

		updateField( zmanimTypeToFieldID.get( type ).intValue(), zmanimCalendar.formatZmanTime( zman ) );
	}


	// TODO: This same method is in ZmanimActivity....
	private void updateField(
		int     field,
		String  text
	) {

		TextView view = (TextView)zmanimView.findViewById( field );
		view.setText( text );
	}




}  // End of ZmanimDisplay class
