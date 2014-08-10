/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.View;
import com.gindin.zmanim.zman.Zman;

/**
 * Knows how to render a set of zmanim for a single day and notify the presenter when the user initiates an interaction
 *  with one of the zmanim.
 */
public interface ZmanimView<P extends ZmanimPresenter>
	extends View<P> {

	/**
	 * Provides the list of zmanim that apply to the day. At this point, however, the exact times
	 *  may not have been calculated, and so the view must NOT ask any of these Zman objects for
	 *  their times.
	 * @param zmanimForTheDay
	 */
	void displayZmanim( Zman[] zmanimForTheDay );

	/**
	 * Invoked after the time for this zman has been calculated.
	 */
	void displayTimeForZman( Zman zman, String formattedTime );

} // End of DailyView interface
