/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.mvp.AbstractPresenter;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.prefs.LocationProviderPreferences;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.Intent;
import android.content.res.Resources;
import androidx.fragment.app.FragmentActivity;

/**
 * Implementation of the LocationPresenter.
 */
class LocationPresenterImpl<V extends LocationView>
  extends AbstractPresenter<V>
  implements LocationPresenter<V> {

  private final FragmentActivity                            owner;

  private final Resources                                   resources;

  private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

  private ZmanimLocation                                    currentLocation;

  private final EditLocationPrefsEvent.Handler              editLocationPrefsHandler;


  LocationPresenterImpl(
    V                 view,
    EventBus          eventBus,
    FragmentActivity  owner,
    Resources         resources
  ) {
    super( view, eventBus );
    this.owner = owner;
    this.resources = resources;

    locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
      @Override
      public void onLocationAcquired(
        ZmanimLocation location
      ) {
        handleLocationChanged( location );
      }

      @Override
      public void onError(
        @NotNull  LocationError   error,
        @Nullable ZmanimLocation  location
      ) {
      }
    };
    eventBus.addHandler( locationAcquiredHandler, true );

    editLocationPrefsHandler = new EditLocationPrefsEvent.Handler() {
      @Override
      public void onEditLocationPrefs() {
        userWantsToEditLocationPrefs();
      }
    };
    eventBus.addHandler( editLocationPrefsHandler );
  }


  @Override
  public void destroy() {

    getEventBus().removeHandler( locationAcquiredHandler );
    getEventBus().removeHandler( editLocationPrefsHandler );
  }


  @Override
  public void userWantsToSeeLocationDetails() {

    String details;
    if ( currentLocation != null ) {
      details = currentLocation.toString();
    }
    else {
      details = resources.getString( R.string.unable_to_determine_location_error );
    }

    getView().showLocationDetails( details );
  }


  @Override
  public void userWantsToEditLocationPrefs() {

    owner.startActivityForResult( new Intent( owner, LocationProviderPreferences.class ), Activities.EDIT_LOCATION_PREFERENCES.ordinal() );
  }


  private void handleLocationChanged( @NotNull ZmanimLocation location ) {

    if ( location.isSignificantChangeFrom( currentLocation ) || ( location.hasName() && !currentLocation.hasName() ) ) {

      currentLocation = location;

      final String locationName;
      if ( location.isInvalid ) {
        locationName = resources.getString( R.string.unknown_location );
      }
      else {
        locationName = location.getLocationName();
      }

      getView().displayLocation( locationName );
    }

  }



} // End of LocationPresenterImpl class
