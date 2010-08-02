/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;

/**
 * Wraps a zman row, making for quick & easy recycling.
 */
public abstract class ZmanRowWrapper {

	protected final TextView                                nameField;
	protected final TextView                                descriptionField;

	private final TextView                                  timeField;

	private final HebrewZmanimCalendar                      zmanimCalendar;

	private ZmanAsyncCalculator                             currentCalculator;


	public ZmanRowWrapper(
		View                  zmanRowView,
		HebrewZmanimCalendar  zmanimCalendar
	) {

		this.zmanimCalendar = zmanimCalendar;

		nameField = (TextView)zmanRowView.findViewById( R.id.zman_name );
		timeField = (TextView)zmanRowView.findViewById( R.id.zman_time );
		descriptionField = (TextView)zmanRowView.findViewById( R.id.zman_description );
	}


	public void setZman(
		Zman zman
	) {

		// Cancel any currently executing calculation...
		if ( ( null != currentCalculator ) && ( !currentCalculator.isCancelled() ) ) {
			currentCalculator.cancel( false );
		}

		// Start the new calculation
		currentCalculator = new ZmanAsyncCalculator( zmanimCalendar, timeField );
		currentCalculator.execute( zman );
		updateTextFields( zman );

	}


	/**
	 * Must be implemented to set the name & description fields.
	 *
	 * @param zman the zman to use when setting the fields.
	 */
	protected abstract void updateTextFields( Zman zman );


	/**
	 * Used to asynchronously calculate the zman.
	 */
	private static class ZmanAsyncCalculator
		extends AsyncTask<Zman, Void, String> {

		private final HebrewZmanimCalendar                      zmanimCalendar;
		private final TextView                                  timeField;


		ZmanAsyncCalculator(
			HebrewZmanimCalendar  zmanimCalendar,
			TextView              timeField
		) {
			this.zmanimCalendar = zmanimCalendar;
			this.timeField = timeField;

			timeField.setText( "" );
		}


		@Override
		protected String doInBackground( Zman... zmans ) {

			String formatted = "";

			try {
				formatted = zmanimCalendar.formatZmanTime( zmans[0] );
			}
			catch ( Exception e ) {
				final Zman zman = zmans[ 0 ];
				LogUtils.logError( "doInBackground", "Format zman: " + zman.type.name + "; zman: " + zman.name + "; ", e );
			}

			return formatted;
		}


		@Override
		protected void onPostExecute( String formattedZman ) {
			super.onPostExecute( formattedZman );

			// If we got cancelled, don't do anything!
			if ( !isCancelled() ) {
				timeField.setText( formattedZman );
			}
		}
	}
}
