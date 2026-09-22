/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.Presenter;

/**
 * Knows how to present the current location and interact with the location.
 */
public interface LocationPresenter<V extends LocationView>
  extends Presenter<V> {

  /** Invoked when the user gestures that they want to see details about the current location. */
  void userWantsToSeeLocationDetails();

  /** Invoked when the user gestures that they want to modify the way their location is obtained. */
  void userWantsToEditLocationPrefs();

} // End of LocationPresenter interface
