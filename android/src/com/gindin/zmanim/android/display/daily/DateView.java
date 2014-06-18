/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily;

import com.gindin.mvp.View;
import com.gindin.zmanim.calendar.HebrewDate;

/**
 * Knows how to render the Hebrew & Secular dates, and notify the presenter when the user initiates an interaction
 *  with either one of them.
 */
public interface DateView<P extends DatePresenter>
	extends View<P> {

	void displayDate(
		String formattedHebrewDate,
		String formattedSecularDate
	);


	void allowUserToChangeDate( HebrewDate currentDate );

} // End of DateView interface
