/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.ZmanFormatter;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Wraps a zman row, making for quick & easy recycling.
 */
public abstract class ZmanRowWrapper {

	protected final TextView                                nameField;
	protected final TextView                                descriptionField;

	private final TextView                                  timeField;

	private ZmanAsyncCalculator                             currentCalculator;

	private ZmanFormatter                                   zmanFormatter;


	public ZmanRowWrapper(
		View          zmanRowView,
		ZmanFormatter zmanFormatter
	) {
		this.zmanFormatter = zmanFormatter;

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
		currentCalculator = new ZmanAsyncCalculator( zmanFormatter, timeField );
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

		private final ZmanFormatter                             zmanFormatter;
		private final TextView                                  timeField;


		ZmanAsyncCalculator(
			ZmanFormatter zmanFormatter,
			TextView      timeField
		) {
			this.zmanFormatter = zmanFormatter;
			this.timeField = timeField;

			timeField.setText( "" );
		}


		@Override
		protected String doInBackground( Zman... zmans ) {

			String formatted = "";

			try {
				formatted = zmanFormatter.formatZmanTime( zmans[0] );
			}
			catch ( Exception e ) {
				final Zman zman = zmans[ 0 ];
				//noinspection unchecked
				LogUtils.logError( "ZRW::doInBackground", "Format zman", e,
					new Pair<String, String>( "type", zman.type.name ),
					new Pair<String, String>( "zman", zman.name ) );
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
