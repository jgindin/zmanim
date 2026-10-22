/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.eventBus.EventBus;
import com.gindin.mvp.Presenter;
import com.gindin.util.EdgeToEdgeUtil;
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
import com.gindin.zmanim.android.messageDisplay.DeterminingLocation;
import com.gindin.zmanim.android.messageDisplay.MessageDisplayer;
import com.gindin.zmanim.android.messageDisplay.ExplainWhyWeNeedLocationDialog;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;
import com.gindin.zmanim.android.service.ZmanimReceiver;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.HebrewDateFormatter;
import com.gindin.zmanlib.location.LatitudeLongitudeParser;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.ZmanimFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.snackbar.Snackbar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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

  private static final boolean                              NEED_TO_REQUEST_BACKGROUND_LOCATION_PERMISSION = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

  /**
   * Request code for the notification permission. The location permission requests use the ordinals of
   *  {@link LocationError.Code}, so this sits well clear of them.
   */
  static final int                                          NOTIFICATION_PERMISSION_REQUEST_CODE = 100;

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

  /**
   * True while the user is being asked about location access (an explanation or the system prompt is on screen).
   *  The notification permission waits until this is over, so that the user is never shown two requests at once.
   */
  private boolean                                           locationPermissionFlowActive;

  /** Explains why the app wants to post notifications, before the system asks for the permission. */
  private AlertDialog                                       notificationPermissionDialog;


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
  @SuppressWarnings( "FeatureEnvy" )
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
    EdgeToEdgeUtil.padForActionBarOverlay( this, findViewById( R.id.main_layout ) );

    determiningLocationDialog = new DeterminingLocation( this );

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
      // We'll get the location and then, in onResume, the refresh() call will get everything going...
      final Bundle bundle = savedInstanceState.getBundle( LOCATION_BUNDLE_KEY );
      lastKnownLocation = ZmanLocationUtil.fromBundle( bundle );
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

    final SharedPreferences locationPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    final boolean isUsingElevation = locationPrefs.getBoolean( LocationProviders.USE_ELEVATION.name(), false );
    ZmanimFactory.useElevation( isUsingElevation );

    refresh( ( lastKnownLocation == null ) || lastKnownLocation.isInvalid );
  }


  @Override
  protected void onPause() {
    super.onPause();
  }


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
    if ( notificationPermissionDialog != null ) {
      notificationPermissionDialog.dismiss();
    }

    for ( Presenter<?> presenter : loadedPresenters ) {
      presenter.destroy();
    }
    loadedPresenters.clear();

    super.onDestroy();
  }


  @Override
  protected void onSaveInstanceState( @NonNull Bundle outState ) {
    super.onSaveInstanceState( outState );

    final Bundle bundle = ZmanLocationUtil.toBundle( lastKnownLocation );
    outState.putBundle( LOCATION_BUNDLE_KEY, bundle );
  }


  @Override
  protected void onActivityResult(
    int     requestCode,
    int     resultCode,
    Intent  data
  ) {

    super.onActivityResult( requestCode, resultCode, data );

    Activities activity = Activities.values()[ requestCode ];

    switch ( activity ) {

      case EDIT_LOCATION_PREFERENCES:
        if ( resultCode == Activity.RESULT_OK ) {
          // Reset so that we'll have to go re-acquire.
          lastKnownLocation = new ZmanimLocation.Builder().invalidLocation();
        }
        break;

      case PERSONAL_PREFERENCS:
        // Nothing to do.
        break;

      case PLAY_SERVICES_NOT_AVAILABLE:
        if ( resultCode == Activity.RESULT_CANCELED ) {
          // Tell the user they'll have to choose either a specific place or a lat/long, but we can't use their location...
          handleNoLocationPermissions();
          showErrorMessage( getResources().getString( R.string.play_services_not_available ), R.string.edit_location,
            v -> eventBus.fire( new EditLocationPrefsEvent() )
          );
        }
        break;

      case REQUEST_CHECK_SETTINGS:
        if ( resultCode != Activity.RESULT_OK ) {
          // Tell the user they'll have to choose either a specific place or a lat/long, but we can't use their location...
          handleNoLocationPermissions();
          showErrorMessage( getResources().getString( R.string.no_location_management_action ), R.string.edit_location,
            v -> eventBus.fire( new EditLocationPrefsEvent() )
          );
        }
        break;
    }
  }


  private void rebuildUI(
    @Nullable ZmanimLocation  location,
    @Nullable LocationError   error
  ) {

    if ( ( location != null ) && !location.isInvalid ) {
      lastKnownLocation = location;
    }

    // Make sure to clean up!
    determiningLocationDialog.dismiss();

    if ( null != error ) {
      handleLocationError( error );
    }
    else {
      // A location arrived without any error, so nothing about location is waiting on the user.
      requestNotificationPermissionIfNeeded();
    }
  }


  /**
   * On Android 13 and later, explains why the app wants to post notifications and then asks for the permission, once.
   *  Nothing happens when the permission is not needed, is already granted, the user turned notifications off, or the
   *  user was asked before.
   */
  private void requestNotificationPermissionIfNeeded() {

    if ( locationPermissionFlowActive ) {
      return;
    }

    if ( ( notificationPermissionDialog != null ) && notificationPermissionDialog.isShowing() ) {
      return;
    }

    if ( !NotificationPermission.shouldAsk( this ) ) {
      return;
    }

    // Not cancelable, so that the only ways out are the two answers, and both are remembered.
    notificationPermissionDialog = new AlertDialog.Builder( this )
      .setTitle( R.string.notification_permission_title )
      .setMessage( R.string.notification_permission_explanation )
      .setCancelable( false )
      .setPositiveButton( R.string.turn_on_notifications, ( dialog, which ) -> {
        NotificationPermission.markAsked( ZmanimActivity.this );
        ActivityCompat.requestPermissions( ZmanimActivity.this,
          new String[] { Manifest.permission.POST_NOTIFICATIONS },
          NOTIFICATION_PERMISSION_REQUEST_CODE
        );
      } )
      .setNegativeButton( R.string.no_thanks,
        ( dialog, which ) -> NotificationPermission.markAsked( ZmanimActivity.this )
      )
      .create();

    notificationPermissionDialog.show();
  }


  private void handleLocationError(
    LocationError error
  ) {

    ExplainWhyWeNeedLocationDialog displayer;

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

        locationPermissionFlowActive = true;
        displayer = new ExplainWhyWeNeedLocationDialog( this,
          ( dialog, which ) -> {
            if ( which == DialogInterface.BUTTON_POSITIVE ) {
              ActivityCompat.requestPermissions( ZmanimActivity.this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                LocationError.Code.NO_PERMISSIONS.ordinal()
              );
            }
            else {
              locationPermissionFlowActive = false;
              eventBus.fire( new EditLocationPrefsEvent() );
            }
          }
        );

        displayer.show( R.string.why_we_need_location_title, "com/gindin/zmanim/android/why_location.html" );

        errorMessage = null;
        break;


      case NO_BACKGROUND_PERMISSIONS:
        if ( NEED_TO_REQUEST_BACKGROUND_LOCATION_PERMISSION ) {

          locationPermissionFlowActive = true;
          displayer = new ExplainWhyWeNeedLocationDialog( this,
            ( dialog, which ) -> {

              if ( which == DialogInterface.BUTTON_POSITIVE ) {
                ActivityCompat.requestPermissions( ZmanimActivity.this,
                  new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION },
                  LocationError.Code.NO_BACKGROUND_PERMISSIONS.ordinal()
                );
              }
              else {
                locationPermissionFlowActive = false;
                eventBus.fire( new EditLocationPrefsEvent() );
              }
            }
          );

          displayer.show( R.string.why_we_need_location_background_title, "com/gindin/zmanim/android/why_background_location.html" );
        }

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

    if ( requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE ) {

      if ( ( grantResults.length <= 0 ) || ( grantResults[ 0 ] != PackageManager.PERMISSION_GRANTED ) ) {
        showErrorMessage( R.string.notification_permission_denied, R.string.action_dismiss, v -> {} );
      }
    }
    else if ( requestCode == LocationError.Code.NO_PERMISSIONS.ordinal() ) {

      // Asking for background access below starts the flow again.
      locationPermissionFlowActive = false;

      if ( ( grantResults.length <= 0 ) || ( grantResults[ 0 ] != PackageManager.PERMISSION_GRANTED ) ) {
        handleNoLocationPermissions();
        showErrorMessage( R.string.cannot_use_dynamic_location, R.string.edit_location,
          v -> eventBus.fire( new EditLocationPrefsEvent() )
        );
      }
      else if ( NEED_TO_REQUEST_BACKGROUND_LOCATION_PERMISSION ) {
        // Now make sure we can ask for background permissions too...

        if ( shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_BACKGROUND_LOCATION ) ) {
          handleLocationError( new LocationError( LocationError.Code.NO_BACKGROUND_PERMISSIONS ) );
        }
      }
      else {
        refresh( true );
      }

    }
    else if ( requestCode == LocationError.Code.NO_BACKGROUND_PERMISSIONS.ordinal() ) {

      locationPermissionFlowActive = false;

      //  So, if we were granted permission, we don't have to do anything here... We'll get an onResume() which will do
      //  the refresh...
      if ( ( grantResults.length <= 0 ) || ( grantResults[ 0 ] != PackageManager.PERMISSION_GRANTED ) ) {
        showErrorMessage( R.string.cannot_show_notifications, R.string.action_dismiss,
          v -> eventBus.fire( new EditLocationPrefsEvent() )
        );
      }
      else {
        refresh( true );
      }
    }

  }


  private void handleNoLocationPermissions() {

    final SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

    // The user didn't grant our permissions. The app can work, but they'll have to tell us where we are.
    if ( ( lastKnownLocation != null ) && !lastKnownLocation.isInvalid ) {

      LatitudeLongitudeParser holder = LatitudeLongitudeParser.latitude(
        LatitudeLongitudeParser.Direction.NORTH, Double.toString( lastKnownLocation.getLatitude() ), "", "" );
      String latitude = holder.toFullString( false );

      holder = LatitudeLongitudeParser.longitude(
        LatitudeLongitudeParser.Direction.EAST, Double.toString( lastKnownLocation.getLongitude() ), "", "" );
      String longitude = holder.toFullString( false );

      locationManagementPrefs
        .edit()
        .putBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), true )
        .putBoolean( LocationProviders.STATIC_POSITION.name(), false )
        .putBoolean( LocationProviders.DYNAMIC_POSITION.name(), false )
        .putString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), latitude )
        .putString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), longitude )
        .apply();

    }
    else {

      locationManagementPrefs
        .edit()
        .putBoolean( LocationProviders.STATIC_POSITION.name(), true )
        .putBoolean( LocationProviders.DYNAMIC_POSITION.name(), false )
        .putBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), false )
        .putString( LocationProviders.StaticProviderOptions.PLACE.name(), "Jerusalem, Israel" )
        .apply();
    }

  }


  private void showErrorMessage( String errorMessage ) {
    showErrorMessage( errorMessage, R.string.action_dismiss, v -> {} );
  }


  private void showErrorMessage(
    @StringRes int                  message,
    @StringRes int                  actionTitle,
    @Nullable View.OnClickListener  action
  ) {
    showErrorMessage( getResources().getString( message ), actionTitle, action );
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

/*
      case R.id.feedback:
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        feedbackFragment.show( getSupportFragmentManager(), "feedback" );
        return true;
*/

      case R.id.privacy:
      case R.id.about:
      case R.id.help:
        showDialog( id );
        return true;

      case R.id.refresh:
        refresh( true );
        return true;
    }

    return false;
  }


  @Nullable
  @Override
  protected Dialog onCreateDialog( int dialogID ) {

    switch ( dialogID ) {
      case R.id.privacy:
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
      case R.id.privacy:
        messageDisplayer.update( R.string.privacy, "com/gindin/zmanim/android/privacy.html" );
        break;

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


  private void refresh( boolean acquireLocation ) {

    if ( acquireLocation ) {
      ZmanimService.enqueueWork( this, ZmanimService.Actions.FIND_LOCATION, true );
    }
    else {
      ZmanimService.enqueueWork( this, lastKnownLocation );
    }
  }



  private class ZmanimGestureListener
    extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;


    @Override
    public boolean onFling(
      @NonNull MotionEvent startMotionEvent,
      @NonNull MotionEvent endMotionEvent,
      float       xVelocity,
      float       yVelocity
    ) {

      // Did the user want to move to the next/prev day (swiping right or left) or next/prev month
      //  (swiping up or down)?
      // TODO: Re-enable the up/down swiping if we can...

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

      runOnUiThread( () -> rebuildUI( location, null ) );
    }


    @Override
    public void onError(
      @NonNull  LocationError   error,
      @Nullable ZmanimLocation  location
    ) {

      runOnUiThread( () -> rebuildUI( location, error ) );
    }
  }

}  // End of ZmanimActivity class
