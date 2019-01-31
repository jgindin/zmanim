/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.eventBus.EventBus;
import com.gindin.mvp.Presenter;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.display.daily.header.DateDisplayModule;
import com.gindin.zmanim.android.display.daily.header.EditLocationPrefsEvent;
import com.gindin.zmanim.android.display.daily.header.LocationDisplayModule;
import com.gindin.zmanim.android.display.daily.main.ZmanimDisplayModule;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.messageDisplay.MessageDisplayer;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;
import com.gindin.zmanim.android.service.ZmanimReceiver;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.HebrewDateFormatter;
import com.gindin.zmanlib.location.ZmanimLocation;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.*;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimActivity
  extends AppCompatActivity {

  private static final String                               LOCATION_BUNDLE_KEY = "last known location";

  private final List<Presenter<?>>                          loadedPresenters = new ArrayList<>();

  /** What date is currently being viewed? */
  private HebrewDate                                        currentDate;

  /** We want to detect swiping. */
  private GestureDetector                                   gestureDetector;

  /** Listen for requests to get a new location. */
  private RequestLocationUpdateEvent.Handler                locationRequestHandler;

  private LocationAcquiredEvent.Handler                     rawLocationAcquiredHandler;

  // TODO: Inject this
  private PersonalPrefsManager                              personalPrefsManager;

  // TODO: Inject this
  private EventBus                                          eventBus;


  /** We use a progress dialog to let the user know we're working. */
  private ProgressDialog                                    determiningLocationDialog;

  // TODO: Inject this
  private MessageDisplayer                                  messageDisplayer = new MessageDisplayer();

  private HebrewDateChangedEvent.Handler                    dateChangedHandler;

  private ZmanimLocation                                    lastKnownLocation;

  private Snackbar                                          snackbar;


  //  See the lifecycle: http://developer.android.com/reference/android/app/Activity.html
  //    onCreate
  //      onRestart
  //      onStart
  //        onResume
  //        onPause
  //      onStop
  //    onDestroy
  //
  // The entire lifetime of an activity happens between the first call to onCreate(Bundle) through to a single final
  //  call to onDestroy(). An activity will do all setup of "global" state in onCreate(), and release all remaining
  //  resources in onDestroy(). For example, if it has a thread running in the background to download data from the
  //  network, it may create that thread in onCreate() and then stop the thread in onDestroy().
  //
  // The visible lifetime of an activity happens between a call to onStart() until a corresponding call to onStop().
  //  During this time the user can see the activity on-screen, though it may not be in the foreground and interacting
  //  with the user. Between these two methods you can maintain resources that are needed to show the activity to the
  //  user. For example, you can register a BroadcastReceiver in onStart() to monitor for changes that impact your UI,
  //  and unregister it in onStop() when the user an no longer see what you are displaying. The onStart() and onStop()
  //  methods can be called multiple times, as the activity becomes visible and hidden to the user.
  //
  // The foreground lifetime of an activity happens between a call to onResume() until a corresponding call to onPause().
  //  During this time the activity is in front of all other activities and interacting with the user. An activity can
  //  frequently go between the resumed and paused states -- for example when the device goes to sleep, when an activity
  //  result is delivered, when a new intent is delivered -- so the code in these methods should be fairly lightweight.
  //
  // Specifically, http://www.screaming-penguin.com/node/7746 says that if you have an indeterminate dialog showing
  //  it must get cleaned up in the onPause, or else it will leak.


  /** Called when the activity is first created. */
  @Override
  public void onCreate(
    Bundle savedInstanceState
  ) {

    // Enable this to get more detailed info regarding disk or network access occurring on the UI thread.
    //  See http://stackoverflow.com/questions/704311/android-how-do-i-investigate-an-anr.
    if ( BuildConfig.DEBUG ) {

      StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      );

      StrictMode.setVmPolicy(
        new StrictMode.VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      );
    }

    super.onCreate( savedInstanceState );

    personalPrefsManager = new PersonalPrefsManager( this );

    AppUpgrader.handle( this, personalPrefsManager, new ZmanimSelectionPrefsManager( this ) );

    setContentView( R.layout.main );

    determiningLocationDialog = new ProgressDialog( this );
    determiningLocationDialog.setIndeterminate( true );
    determiningLocationDialog.setMessage( getString( R.string.determining_location ) );

    eventBus = ZmanimService.ZMANIM_EVENT_BUS;

    // todo: Move to background thread????????

    DateDisplayModule dateDisplayModule = new DateDisplayModule( this, eventBus );
    loadedPresenters.add( dateDisplayModule.create() );

    LocationDisplayModule locationDisplayModule = new LocationDisplayModule( this, eventBus );
    loadedPresenters.add( locationDisplayModule.create() );

    ZmanimDisplayModule zmanimDisplayModule = new ZmanimDisplayModule( this, eventBus, personalPrefsManager);
    loadedPresenters.add( zmanimDisplayModule.create() );

    setupViewListeners();

    dateChangedHandler = new HebrewDateChangeHandler();
    eventBus.addHandler( dateChangedHandler );

    locationRequestHandler = new RequestLocationUpdateEvent.Handler() {
      @Override
      public void onLocationUpdateRequested( final RequestLocationUpdateEvent event ) {
        runOnUiThread( () -> determiningLocationDialog.show() );
      }
    };
    eventBus.addHandler( locationRequestHandler );

    // Now start listening for location changes...
    rawLocationAcquiredHandler = new RawLocationAcquiredHandler();
    eventBus.addHandler( rawLocationAcquiredHandler );

    // Have to get setup for today.
    // TODO:  We should actually be storing the date the user is viewing in the savedInstanceState!
    currentDate = HebrewCalendar.today();
    eventBus.fire( new HebrewDateChangedEvent( currentDate ) );

    // Make sure that we're going to update our alarms...
    ZmanimReceiver.scheduleRepeatingChecks( this );

    if ( savedInstanceState != null ) {
      final Bundle bundle = savedInstanceState.getBundle( LOCATION_BUNDLE_KEY );
      lastKnownLocation = ZmanLocationUtil.fromBundle( bundle );
      if ( !lastKnownLocation.isInvalid ) {
        eventBus.fire( new LocationAcquiredEvent( lastKnownLocation ) );
      }
    }
  }


  @Override
  protected void onRestart() {
    super.onRestart();
  }


  @Override
  protected void onStart() {
    super.onStart();
  }


  @Override
  protected void onResume() {
    super.onResume();

    refresh();
  }


  @Override
  protected void onPause() {
    super.onPause();
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Override
  protected void onStop() {

    super.onStop();
  }


  @Override
  protected void onDestroy() {

    eventBus.removeHandler( locationRequestHandler );
    eventBus.removeHandler( dateChangedHandler );
    eventBus.removeHandler( rawLocationAcquiredHandler );

    determiningLocationDialog.dismiss();

    for ( Presenter<?> presenter : loadedPresenters ) {
      presenter.destroy();
    }
    loadedPresenters.clear();

    super.onDestroy();
  }


  @Override
  protected void onSaveInstanceState( Bundle outState ) {
    super.onSaveInstanceState( outState );

    final Bundle bundle = ZmanLocationUtil.toBundle( lastKnownLocation );
    outState.putBundle( LOCATION_BUNDLE_KEY, bundle );
  }


  @SuppressWarnings( { "FeatureEnvy" } )
  @Override
  protected void onActivityResult(
    int     requestCode,
    int     resultCode,
    Intent  data
  ) {

    super.onActivityResult( requestCode, resultCode, data );

    Activities activity = Activities.values()[ requestCode ];

    switch ( activity ) {

      case ENABLE_LOCATION_SERVICES:
        refresh();
        break;

      case PERSONAL_PREFERENCS:
        personalPrefsManager.personalPrefsChanged();
        break;

      case PLAY_SERVICES_NOT_AVAILABLE:
        if ( resultCode == Activity.RESULT_CANCELED ) {
          // Tell the user they'll have to choose either a specific place or a lat/long, but we can't use their location...
          showErrorMessage( R.string.play_services_not_available );
        }
        break;

      case REQUEST_CHECK_SETTINGS:
        if ( resultCode == Activity.RESULT_OK ) {
          // Nothing to do; we'll ask for a location update in onResume.
        }
        else if ( resultCode == Activity.RESULT_CANCELED ) {
          // Tell the user they'll have to choose either a specific place or a lat/long, but we can't use their location...
          showErrorMessage( R.string.no_location_management_action );
        }
        break;
    }
  }


  private void rebuildUI(
    ZmanimLocation            location,
    @Nullable LocationError   error
  ) {

    if ( ( location != null ) && !location.isInvalid ) {
      lastKnownLocation = location;
    }

    buildFooter( error );

    // Make sure to clean up!
    determiningLocationDialog.dismiss();
  }


  private void buildFooter(
    @Nullable final LocationError error
  ) {

    if ( null != error ) {
      handleLocationError( error );
    }
  }


  private void handleLocationError(
    LocationError error
  ) {

    // Now...why is it we couldn't determine our location?
    String errorMessage;
    switch ( error.errorCode ) {

      case CANNOT_DETERMINE_LOCATION:

        if ( null != lastKnownLocation ) {
          final String lastTime = DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.SHORT )
            .format( new Date( lastKnownLocation.locationTime ) );
          errorMessage = getResources().getString( R.string.cannot_update_location, lastTime );
        }
        else {
          errorMessage = getResources().getString( R.string.cannot_determine_location  );
        }

        // TODO: Need to add click listener that will invoke a dialog allowing the user to:
        //  1. try obtaining location again; or
        //  2. manually specify where they are
        break;

      case NO_PERMISSIONS:
        // See onRequestPermissionsResult()
        ActivityCompat.requestPermissions( ZmanimActivity.this,
          new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
          0
        );

        errorMessage = null;
        break;

      case NO_PROVIDERS:
      case NO_LOCATION_MANAGER_SERVICE:
        try {
          // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
          error.apiExceptionStatus.startResolutionForResult( this, Activities.REQUEST_CHECK_SETTINGS.ordinal() );
          errorMessage = null;
        }
        catch ( IntentSender.SendIntentException sendEx ) {
          errorMessage = sendEx.getMessage();
        }
        break;

      case BAD_INPUT:
        errorMessage = error.message;
        break;


      case NO_PLAY_SERVICES:
        final int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable( this );
        GooglePlayServicesUtil
          .getErrorDialog( result, this, Activities.PLAY_SERVICES_NOT_AVAILABLE.ordinal() )
          .show();

        errorMessage = null;
        break;

      default:
        // Seems necessary to have a default case, even though we've specified all the cases, so that the
        //  clickListener gets initialized.
        errorMessage = getResources().getString( R.string.unable_to_determine_location_error );
    }


    if ( errorMessage != null ) {
      showErrorMessage( errorMessage );
    }
  }


  @Override
  public void onRequestPermissionsResult(
    int               requestCode,
    @NonNull String[] permissions,
    @NonNull int[]    grantResults
  ) {
    super.onRequestPermissionsResult( requestCode, permissions, grantResults );

    // We're only asking for location permissions, so no need to check what permission was asked for.
    if ( ( grantResults.length > 0 ) && ( grantResults[0] == PackageManager.PERMISSION_GRANTED ) ) {
      // Don't have to do anything here... We'll get an onResume() which will do the refresh...
    }
    else {
      // The user didn't grant our permissions. The app can work, but they'll have to tell us where we are.
      final SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
      locationManagementPrefs
        .edit()
        .putBoolean( LocationProviders.STATIC_POSITION.name(), true )
        .putBoolean( LocationProviders.DYNAMIC_POSITION.name(), false )
        .putString( LocationProviders.StaticProviderOptions.PLACE.name(), "Jerusalem, Israel" )
        .apply();

      showErrorMessage( getResources().getString( R.string.cannot_use_dynamic_location ), R.string.edit_location,
        v -> eventBus.fire( new EditLocationPrefsEvent() )
      );
    }
  }


  private void showErrorMessage( @StringRes int errorMessage ) {
    showErrorMessage( getResources().getString( errorMessage ), R.string.action_dismiss, v -> {} );
  }


  private void showErrorMessage( String errorMessage ) {
    showErrorMessage( errorMessage, R.string.action_dismiss, v -> {} );
  }


  private void showErrorMessage(
    String                          errorMessage,
    @StringRes int                  actionTitle,
    @Nullable View.OnClickListener  action
  ) {

    if ( snackbar == null ) {
      snackbar = Snackbar
        .make( findViewById( android.R.id.content ), errorMessage, Snackbar.LENGTH_INDEFINITE );
    }
    else {
      snackbar.setText( errorMessage );
    }

    snackbar.setAction( actionTitle, action );

    snackbar.show();
  }


  private void setupViewListeners() {

    // Handle gestures. In our case, we're listening for swiping left, right, up & down...
    View.OnTouchListener touchListener = ( view, event ) -> gestureDetector.onTouchEvent( event );

    View mainView = findViewById( R.id.main_layout );
    mainView.setOnTouchListener( touchListener );

    View listing = findViewById( android.R.id.list );
    listing.setOnTouchListener( touchListener );

    // How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
    // Note that it *must* be run on the UI thread...
    gestureDetector = new GestureDetector( ZmanimActivity.this, new ZmanimGestureListener() );
  }


  @Override
  public boolean onTouchEvent( MotionEvent event ) {
    return gestureDetector.onTouchEvent( event );
  }


  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate( R.menu.main_menu, menu );

    return true;
  }


  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {

    int id = item.getItemId();
    switch ( id ) {

      case R.id.personal_prefs:
        personalPrefsManager.editPrefs();
        return true;

      case R.id.location_prefs:
        eventBus.fire( new EditLocationPrefsEvent() );
        return true;

      case R.id.about:
        showDialog( id );
        return true;

/*
      case R.id.feedback:
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        feedbackFragment.show( getSupportFragmentManager(), "feedback" );
        return true;
*/

      case R.id.help:
        showDialog( id );
        return true;

      case R.id.refresh:
        refresh();
        return true;
    }

    return false;
  }


  @Override
  protected Dialog onCreateDialog( int dialogID ) {

    switch ( dialogID ) {
      case R.id.about:
      case R.id.help:
        return messageDisplayer.getInstance( this );
    }

    return null;
  }


  @Override
  protected void onPrepareDialog(
    int     dialogID,
    Dialog  dialog
  ) {
    super.onPrepareDialog( dialogID, dialog );

    switch ( dialogID ) {
      case R.id.about:
        messageDisplayer.update( R.string.about_header, "com/gindin/zmanim/android/about.html" );
        break;

      case R.id.help:
        messageDisplayer.update( R.string.help_header, "com/gindin/zmanim/android/help.html" );
        break;

      default:
        // nothing for the other dialogs.
    }
  }


  private void refresh() {

    // Just kick the service and let it do the work...
    ZmanimService.enqueueWork( this, ZmanimService.Actions.FIND_LOCATION, true );
  }


  public void personalPrefsChanged() {

    Intent zmanimService = new Intent( this, ZmanimService.class );
    zmanimService.setAction( ZmanimService.Actions.SCHEDULE_ALARM.name() );
    zmanimService.putExtra( ZmanimService.ActionParams.FORCE_SCHEDULE.name(), true );

    // Have to guard against a bit of a corner case ... the first time the user uses the application,
    //  if we don't have a location and they go into the preferences, make a change, and then come back,
    //  we'll want to rebuild the UI; but if we don't have a location, we can't...
    if ( lastKnownLocation != null ) {
      rebuildUI( lastKnownLocation, null );
    }
  }


  private class ZmanimGestureListener
    extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;


    @Override
    public boolean onFling(
      MotionEvent startMotionEvent,
      MotionEvent endMotionEvent,
      float       xVelocity,
      float       yVelocity
    ) {

      // Did the user want to move to the next/prev day (swiping right or left) or next/prev month
      //  (swiping up or down)?
      // TODO: Re-enable the up/down swiping if we can...

      // From a bit of StackOverflow reading, it seems that startMotionEvent could possibly be null if, e.g.,
      //  something else has consumed the initial gesture.
      //  See https://stackoverflow.com/questions/4151385/android-simpleongesturelistener-onfling-getting-a-null-motionevent
      // Anyways, just because we're going to be paranoid now, we might as well guard against the endMotionEvent
      //  being null too...
      if ( ( startMotionEvent == null ) || ( endMotionEvent == null ) ) {
        return false;
      }

      float xDiff = startMotionEvent.getRawX() - endMotionEvent.getRawX();

      if ( Math.abs( xDiff ) < SWIPE_MIN_DISTANCE ) {
        return false;
      }

      if ( Math.abs( xVelocity ) > Math.abs( yVelocity ) ) {
        HebrewDateChangedEvent event;
        if ( xDiff > 0 ) {
          event = new HebrewDateChangedEvent( currentDate.tomorrow() );
        }
        else {
          event = new HebrewDateChangedEvent( currentDate.yesterday() );
        }

        eventBus.fire( event );
        return true;
      }

      return false;
    }

  }



  private class HebrewDateChangeHandler
    extends HebrewDateChangedEvent.Handler {

    @Override
    public void onHebrewDateChanged( HebrewDate newDate ) {
      currentDate = newDate;

      final String formattedDate = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.LONG )
        .format( newDate, lastKnownLocation );
      LogUtils.setContext( "HebrewDate", formattedDate  );
    }
  }


  private class RawLocationAcquiredHandler
    extends LocationAcquiredEvent.Handler {

    @Override
    public void onLocationAcquired(
      final ZmanimLocation location
    ) {

      LogUtils.setContext( "Location", location.toString() );

      runOnUiThread(
        () -> {
          rebuildUI( location, null );
        }
      );
    }


    @Override
    public void onError(
      @NotNull final LocationError  error
    ) {

      runOnUiThread(
        () -> {

          rebuildUI( lastKnownLocation, error );
        }
      );
    }
  }

}  // End of ZmanimActivity class
