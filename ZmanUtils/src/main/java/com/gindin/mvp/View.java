/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp;

/**
 * Represents the UI widgets with which the user is interacting. Delegates everything to the Presenter, where
 *  all the decisions are made. The view is intended to be as dumb as possible, simply taking commands from the
 *  Presenter and rendering the data.
 */
public interface View<P extends Presenter> {

	/**
	 * Notify the view of its presenter. Using a setter (instead of a ctor param) makes it part of the contract.
	 */
	void setPresenter( P presenter );

} // End of View interface
