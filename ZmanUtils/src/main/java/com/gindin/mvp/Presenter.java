/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp;

/**
 * Controls the View, telling it what to do and when.
 */
public interface Presenter<V extends View> {

	/**
	 * Called when the presenter is no longer needed.
	 */
	void destroy();

} // End of Presenter interface
