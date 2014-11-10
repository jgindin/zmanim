/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.Presenter;
import com.gindin.zmanim.zman.Zman;

/**
 * Deals with presenting to the user a single day's view of relevant Zmanim. Because it is for a single day,
 *  a representation of that day is also included.
 */
public interface ZmanimPresenter<V extends ZmanimView>
	extends Presenter<V> {

	/**
	 * Called when the user gestures that they want to see the various options for the specified zman.
	 */
	void userWantsToSeeZmanOptions( Zman zman );

	/**
	 * Called when the user has decided to change the option for the specified zman.
	 */
	void userChangedZman(
		Zman selection
	);

} // End of DailyPresenter class
