/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily;

import com.gindin.mvp.AbstractView;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.calendar.HebrewDate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 *
 */
class DateViewImpl<P extends DatePresenter>
	extends AbstractView<P>
	implements DateView<P> {

	private final TextView                                    hebrewDateView;
	private final TextView                                    secularDateView;

	private final FragmentActivity                            owner;

	private final DatePickerDialog.OnDateSetListener          dateSetListener;


	DateViewImpl(
		FragmentActivity owner
	) {
		this.owner = owner;

		dateSetListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(
				DatePicker datePicker,
				int year,
				int month,
				int date
			) {
				getPresenter().userChangedDate( year, month, date );
			}
		};

		secularDateView = (TextView)owner.findViewById( R.id.secular_date );
		secularDateView.setOnClickListener(
			new View.OnClickListener() {
				public void onClick( View v ) {
					getPresenter().userWantsToChangeDate();
				}
			}
		);

		hebrewDateView = (TextView)owner.findViewById( R.id.hebrew_date );
	}


	@Override
	public void displayDate(
		String formattedHebrewDate,
		String formattedSecularDate
	) {

		hebrewDateView.setText( formattedHebrewDate );
		secularDateView.setText(  Html.fromHtml( "<u>" + formattedSecularDate + "</u>" ) );
	}


	@Override
	public void allowUserToChangeDate( HebrewDate currentDate ) {

		ChangeDateFragment changeDateFragment = new ChangeDateFragment( currentDate, dateSetListener );
		changeDateFragment.show( owner.getSupportFragmentManager(), "secularDatePicker" );
	}


	private static class ChangeDateFragment
		extends DialogFragment {

		private final HebrewDate                                  currentDate;

		private final DatePickerDialog.OnDateSetListener          dateSetListener;



		ChangeDateFragment(
			HebrewDate                          currentDate,
			DatePickerDialog.OnDateSetListener  dateSetListener
		) {
			this.currentDate = currentDate;
			this.dateSetListener = dateSetListener;
		}



		@Override
		public Dialog onCreateDialog( Bundle savedInstanceState ) {
			return new DatePickerDialog( getActivity(), dateSetListener,
				currentDate.getGregorianYear(), currentDate.getGregorianMonth(), currentDate.getGregorianDayOfMonth() );
		}

	}


} // End of DateViewImpl class
