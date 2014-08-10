/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.View;

/**
 * Knows how to display the current location and capture user intent for interacting with it.
 */
public interface LocationView<P extends LocationPresenter>
	extends View<P>  {

	/** Instructs the view to display the location information. */
	void displayLocation( String formattedLocation );

	/** Instruct the view to show the details of a location. */
	void showLocationDetails( String locationDetails );

} // End of LocationView class
