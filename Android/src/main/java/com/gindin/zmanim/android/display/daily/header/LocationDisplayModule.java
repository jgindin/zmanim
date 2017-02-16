/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.AbstractMvpModule;
import com.gindin.eventBus.EventBus;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

/**
 * Module to handle presentation and interaction with the current location.
 */
public class LocationDisplayModule
  extends AbstractMvpModule<LocationPresenter<LocationView>> {


  public LocationDisplayModule(
    FragmentActivity activity,
    EventBus eventBus
  ) {
    super( activity, eventBus );
  }


  @Override
  public LocationPresenter<LocationView> create() {

    Resources resources = activity.getResources();

    LocationViewImpl view = new LocationViewImpl<LocationPresenter>( activity );
    return new LocationPresenterImpl<LocationView>( view, eventBus, activity, resources );
  }


} // End of LocationDisplayModule class
