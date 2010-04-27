/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.flurry.android.FlurryAgent;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;

import java.io.PrintWriter;
import java.io.StringWriter;

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

				if ( 0 == zmans.length ) {
					// Never seen this happen...just being paranoid because some RuntimeException errors are showing
					//  up in the Flurry log...
					FlurryAgent.onError( "doInBackground", "empty zmans", "zmans.length == 0" );
				}
				else if ( null == zmans[ 0 ] ) {
					// Never seen this happen...just being paranoid because some RuntimeException errors are showing
					//  up in the Flurry log...
					FlurryAgent.onError( "doInBackground", "null zmans", "zmans.length: " + zmans.length + "; zmans[0]: null" );
				}
				else {
					formatted = zmanimCalendar.formatZmanTime( zmans[0] );
				}
			}
			catch ( Exception e ) {

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter( sw );
				e.printStackTrace( pw );

				final Zman zman = zmans[ 0 ];
				FlurryAgent.onError( "doInBackground", "Trying to format zman. Type: " + zman.type.name + "; zman: " + zman.name,
					sw.getBuffer().toString() );
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
