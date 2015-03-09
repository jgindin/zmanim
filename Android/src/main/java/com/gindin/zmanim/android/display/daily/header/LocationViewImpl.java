/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.AbstractView;
import com.gindin.zmanim.android.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Implementation of the LocationView.
 */
class LocationViewImpl<P extends LocationPresenter>
	extends AbstractView<P>
	implements LocationView<P> {

	private final TextView                                    locationView;

	/** When the user clicks on the location, they're indicating they want to change it. */
	private final View.OnClickListener                        locationClickListener = new View.OnClickListener() {
		public void onClick( View view ) {
			getPresenter().userWantsToEditLocationPrefs();
		}
	};


	/** When the user long-clicks on the location, they're indicating they want to see some details about it. */
	private final View.OnLongClickListener                    locationLongClickListener = new View.OnLongClickListener() {
		public boolean onLongClick( View view ) {
			getPresenter().userWantsToSeeLocationDetails();
			return true;
		}
	};


	LocationViewImpl(
		FragmentActivity owner
	) {

		super( owner );
		locationView = (TextView)owner.findViewById( R.id.location );
		locationView.setOnClickListener( locationClickListener );
		locationView.setOnLongClickListener( locationLongClickListener );
	}

	@Override
	public void displayLocation( final String formattedLocation ) {

		owner.runOnUiThread(
			new Runnable() {
				@Override
				public void run() {
					locationView.setText( formattedLocation );
				}
			}
		);

	}


	@Override
	public void showLocationDetails( String locationDetails ) {

		ShowLocationDetailsDialogFragment fragment = new ShowLocationDetailsDialogFragment( locationDetails );
		fragment.show( owner.getSupportFragmentManager(), "showLocationDetails" );
	}



	private static class ShowLocationDetailsDialogFragment
		extends DialogFragment {

		private final String                                    locationDetails;


		ShowLocationDetailsDialogFragment( String locationDetails ) {
			this.locationDetails = locationDetails;
		}


		@Override
		public Dialog onCreateDialog( Bundle savedInstanceState ) {

			AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
			builder.setTitle( R.string.current_location_title );

			builder.setMessage( locationDetails );

			return builder.create();
		}
	}


} // End of LocationViewImpl class
