/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.View;
import com.gindin.zmanlib.calendar.HebrewDate;

import org.jetbrains.annotations.Nullable;

/**
 * Knows how to render the Hebrew & Secular dates, and notify the presenter when the user initiates an interaction
 *  with either one of them.
 */
public interface DateView<P extends DatePresenter>
  extends View<P> {

  /** Instructs the view to display the date. */
  void displayDate(
    String formattedHebrewDate,
    String formattedSecularDate
  );


  /** Instructs the view that it should present a UI allowing the user to change the date. */
  void allowUserToChangeDate( HebrewDate currentDate );

  /** Instructs the view do render a description for the day. May be null to indicate no description. */
  void displayDescriptionOfTheDay( @Nullable String descriptionOfTheDay );


} // End of DateView interface
