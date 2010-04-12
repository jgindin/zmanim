/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;

/**
 * Actually displays the zmanim on the screen.
 */
class ZmanimDisplay {

	private final Activity                                    context;

	/** The calendar we use for displaying everything. */
	private final HebrewZmanimCalendar                        zmanimCalendar;

	/** The view where we're going to display all the zmanim. */
	private final View                                        zmanimView;


	ZmanimDisplay(
		Activity              context,
		HebrewZmanimCalendar  zmanimCalendar
	) {

		this.context = context;
		this.zmanimCalendar = zmanimCalendar;

		zmanimView = context.findViewById( R.id.zmanim_view );

		zmanimCalendar.addListener(
			new HebrewZmanimCalendar.Listener() {
				public void dateChanged() {
					update();
				}
			}
		);

		// Make sure we're starting off with the right stuff...
		update();
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	void update() {

		// TODO: Calculate on separate threads!!!!

		Zmanim zmanim = zmanimCalendar.get( Zmanim.Type.Alot );
		Zman zman = zmanim.get( 0 );
		updateField( R.id.alot, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.Talit );
		zman = zmanim.get( 0 );
		updateField( R.id.talit, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.Hanetz );
		zman = zmanim.get( 0 );
		updateField( R.id.hanetz, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.SofZmanShema );
		zman = zmanim.get( 0 );
		updateField( R.id.shema, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.SofZmanTefila );
		zman = zmanim.get( 0 );
		updateField( R.id.tefilah, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.Chatzot );
		zman = zmanim.get( 0 );
		updateField( R.id.chatzot, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.MinchaGedola );
		zman = zmanim.get( 0 );
		updateField( R.id.mincha_gedola, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.MinchaKetana );
		zman = zmanim.get( 0 );
		updateField( R.id.mincha_ketana, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.PlagHaMincha );
		zman = zmanim.get( 0 );
		updateField( R.id.plag_hamincha, zmanimCalendar.formatZmanTime( zman ) );

		if ( zmanimCalendar.showCandlelighting() ) {
			context.findViewById( R.id.candles_row ).setVisibility( View.VISIBLE );

			zmanim = zmanimCalendar.get( Zmanim.Type.CandleLighting );
			zman = zmanim.get( 0 );
			updateField( R.id.candles, zmanimCalendar.formatZmanTime( zman ) );
		}
		else {
			context.findViewById( R.id.candles_row ).setVisibility( View.GONE );
		}

		zmanim = zmanimCalendar.get( Zmanim.Type.Shkia );
		zman = zmanim.get( 0 );
		updateField( R.id.shkiah, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.LatestMincha );
		zman = zmanim.get( 0 );
		updateField( R.id.mincha, zmanimCalendar.formatZmanTime( zman ) );

		zmanim = zmanimCalendar.get( Zmanim.Type.TzaitHachocavim );
		zman = zmanim.get( 0 );
		updateField( R.id.tzait, zmanimCalendar.formatZmanTime( zman ) );
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
