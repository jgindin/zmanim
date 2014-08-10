/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.Presenter;

/**
 * Deals with presenting to the user a single day's view of relevant Zmanim. Because it is for a single day,
 *  a representation of that day is also included.
 */
public interface ZmanimPresenter<V extends ZmanimView>
	extends Presenter<V> {


} // End of DailyPresenter class
