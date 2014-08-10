/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.Presenter;

/**
 *
 */
public interface DatePresenter<V extends DateView>
	extends Presenter<V> {

	/** Called when the user gestures that they want to change the current date. */
	void userWantsToChangeDate();

	/** Called when the user has made a gesture that changes the date. */
	void userChangedDate( int year, int month, int date );

} // End of DatePresenter interface
