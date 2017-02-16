/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.AbstractPresenter;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;
import org.jetbrains.annotations.NotNull;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
class ZmanimPresenterImpl<V extends ZmanimView>
  extends AbstractPresenter<V>
  implements ZmanimPresenter<V> {


  /** Get the chosen zmanim. */
  private final SharedPreferences                           zmanimSelectionPrefs;

  private final ZmanFormatter                               zmanFormatter;

  private final HebrewDateChangedEvent.Handler              dateChangeHandler;

  private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

  private HebrewDate                                        currentDate;

  private ZmanimLocation                                    currentLocation;

  private ZmanimCalculator                                  zmanimCalculator;


  ZmanimPresenterImpl(
    V                 view,
    EventBus          eventBus,
    SharedPreferences zmanimSelectionPrefs,
    ZmanFormatter     zmanFormatter
  ) {

    super( view, eventBus );
    this.zmanimSelectionPrefs = zmanimSelectionPrefs;
    this.zmanFormatter = zmanFormatter;

    dateChangeHandler = new HebrewDateChangedEvent.Handler() {
      @Override
      public void onHebrewDateChanged( HebrewDate newDate ) {
        handleDateChanged( newDate );
      }
    };
    eventBus.addHandler( dateChangeHandler, true );

    locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
      @Override
      public void onLocationAcquired(
        ZmanimLocation location,
        final boolean isCachedLocation
      ) {
        handleLocationChanged( location );
      }

      @Override
      public void onError(
        @NotNull LocationError  error,
        @NotNull ZmanimLocation lastKnownLocation
      ) {
        // Don't bother updating if there's an error AND we already have a location.
        if ( currentLocation == null ) {
          handleLocationChanged( lastKnownLocation );
        }
      }
    };
    eventBus.addHandler( locationAcquiredHandler, true );
  }


  @Override
  public void destroy() {
    getEventBus().removeHandler( dateChangeHandler );
    getEventBus().removeHandler( locationAcquiredHandler );
  }


  @Override
  public void userWantsToSeeZmanOptions( final Zman zman ) {

    getView().showZmanOptions( zman, currentLocation, currentDate );
  }


  @Override
  public void userChangedZman(
    final Zman selection
  ) {

    Zmanim.Type selectedType = selection.type;

    final SharedPreferences.Editor editor = zmanimSelectionPrefs.edit();
    editor.putString( selectedType.name, selection.name );
    editor.apply();

    LogUtils.logEvent( "Zman Change", Arrays.asList( new Pair<String, String>( selectedType.name, selection.name ) ) );

    // This is a bit of a hammer, but it works...
    //  I tried to find the existing zman in the list, remove it, and add the newly selected one, but
    //  apparently the List which backs the adapter doesn't support remove().
    notifyViewOfZmanim();
  }


  private void handleDateChanged( HebrewDate newDate ) {
    currentDate = newDate;

    if ( currentLocation == null ) {
      return;
    }

    notifyViewOfZmanim();
  }


  private void handleLocationChanged( @NotNull ZmanimLocation newLocation ) {

    if ( !newLocation.isInvalid && newLocation.isSignificantChangeFrom( currentLocation ) ) {
      currentLocation = newLocation;

      if ( currentDate == null ) {
        return;
      }

      notifyViewOfZmanim();
    }
  }


  private void notifyViewOfZmanim() {

    assert currentDate != null;
    assert currentLocation != null;

    List<Zmanim> daysZmanim = currentDate.getZmanim( currentLocation );
    Zman[] zmanimToDisplay = new Zman[ daysZmanim.size() ];
    for ( int i = 0, numZmanim = daysZmanim.size(); i < numZmanim; i++ ) {
      zmanimToDisplay[i] = getZman( daysZmanim.get( i ) );
    }

    // Tell the view which zmanim are applicable to this day.
    getView().displayZmanim( zmanimToDisplay );

    // And now start a calculation of each of those zmanim.
    calculateZmanim( zmanimToDisplay );
  }


  private Zman getZman(
    Zmanim      zmanim
  ) {

    String zmanName = zmanimSelectionPrefs.getString( zmanim.type.name, null );
    Zman zman = zmanim.getByName( zmanName );

    return zman;
  }


  private void calculateZmanim( Zman... zmanimToDisplay ) {

    if ( zmanimCalculator != null ) {
      zmanimCalculator.cancel( true );
    }

    zmanimCalculator = new ZmanimCalculator();
    zmanimCalculator.execute( zmanimToDisplay );
  }


  private class ZmanimCalculator
    extends AsyncTask<Zman, Pair<Zman, String>, Void> {

    @Override
    protected Void doInBackground( final Zman... zmanim ) {

      for ( Zman zman : zmanim ) {
        if ( isCancelled() ) {
          return null;
        }

        String formattedTime = zmanFormatter.formatZmanTime( zman );
        publishProgress( new Pair<Zman, String>( zman, formattedTime ) );
      }
      return null;
    }


    @Override
    protected void onProgressUpdate( final Pair<Zman, String>... values ) {
      super.onProgressUpdate( values );
      for ( Pair<Zman, String> value : values ) {
        getView().displayTimeForZman( value.first, value.second );
      }
    }
  }

} // End of DailyPresenterImpl class
