/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.AbstractView;
import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;

import org.jetbrains.annotations.Nullable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
  private final TextView                                    dateDescriptionView;

  private final DatePickerDialog.OnDateSetListener          dateSetListener;


  DateViewImpl(
    FragmentActivity owner
  ) {
    super( owner );

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
    dateDescriptionView = (TextView)owner.findViewById( R.id.holiday );
  }


  @Override
  public void displayDate(
    final String formattedHebrewDate,
    final String formattedSecularDate
  ) {

    owner.runOnUiThread(
      new Runnable() {
        @Override
        public void run() {
          hebrewDateView.setText( formattedHebrewDate );
          secularDateView.setText(  Html.fromHtml( "<u>" + formattedSecularDate + "</u>" ) );
        }
      }
    );

  }


  @Override
  public void allowUserToChangeDate( HebrewDate currentDate ) {

    // Yes...I know that I should be using Fragment#setArguments, but that's just overkill.
    ChangeDateFragment changeDateFragment = new ChangeDateFragment();
    changeDateFragment.init( currentDate, dateSetListener );

    changeDateFragment.show( owner.getSupportFragmentManager(), "secularDatePicker" );
  }


  @Override
  public void displayDescriptionOfTheDay( @Nullable final String descriptionOfTheDay ) {

    owner.runOnUiThread(
      new Runnable() {
        @Override
        public void run() {

          if ( ( descriptionOfTheDay == null ) || descriptionOfTheDay.trim().isEmpty() ) {
            owner.findViewById( R.id.holiday_row ).setVisibility( View.GONE );
          }
          else {
            owner.findViewById( R.id.holiday_row ).setVisibility( View.VISIBLE );
            dateDescriptionView.setText( descriptionOfTheDay );
          }
        }
      }
    );
  }


  public static class ChangeDateFragment
    extends DialogFragment {

    private HebrewDate                                  currentDate;

    private DatePickerDialog.OnDateSetListener          dateSetListener;


    public ChangeDateFragment() {
      setRetainInstance( true );
    }


    void init(
      HebrewDate                          currentDate,
      DatePickerDialog.OnDateSetListener  dateSetListener
    ) {
      this.currentDate = currentDate;
      this.dateSetListener = dateSetListener;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

      // I don't know how it's possible...I'm guessing that somehow this fragment got weirded out???
      //  After all, if you look at how this is called, the allowUserToChangeDate() method is already
      //  only invoked if the HebrewDate is not null.
      // Nonetheless, I have stack traces:
      //  java.lang.NullPointerException: Attempt to invoke virtual method 'int com.gindin.zmanlib.calendar.HebrewDate.getGregorianYear()' on a null object reference
      if ( currentDate == null ) {
        // Uh...just default to today's date...
        currentDate = HebrewCalendar.today();
      }

      return new DatePickerDialog( getActivity(), dateSetListener,
        currentDate.getGregorianYear(), currentDate.getGregorianMonth(), currentDate.getGregorianDayOfMonth() );
    }

  }


} // End of DateViewImpl class
