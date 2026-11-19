/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.app.UiAutomation;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * With the network off, places are named from the place data bundled with the app: a city typed in is found with its
 *  name, coordinates and elevation, and coordinates, typed in or from the device, are named after the town they are
 *  near rather than shown as coordinates.
 *<p/>
 *
 * The tests also run against the R8-minified build, where the app's own classes and Play services' are renamed, so
 *  they use only what R8 keeps: the screens, the preference names and the files the app writes. What the app found
 *  for a typed place is read from its geocode cache file, which holds one JSON object a line.
 *<p/>
 *
 * The emulators the tests run on have a system geocoder and a network, and with a network the geocoder could answer
 *  instead of the bundled data. So the class turns Wi-Fi and mobile data off before its first test and back on after
 *  its last, once rather than around each test, because a network turned back on reconnects only after a few seconds,
 *  in the middle of the next test. Each test also checks that the device was still offline when it ended. Where the
 *  shell user may not turn the network off (the API 24 emulator, for example), the tests are skipped, unless the
 *  instrumentation argument {@value #REQUIRE_OFFLINE_ARGUMENT} is {@code true}: then a device with a network fails
 *  them. CI turns the network off as root and runs this class on its own with that argument (see {@code ci.yml}).
 */
@RunWith( AndroidJUnit4.class )
public class OfflinePlaceNamingTest {

  /**
   * In the Negev, 29 km (18 miles) from Dimona, the nearest town in the bundled data: too far to be named after it, near
   * enough to be near it.
   */
  private static final double                               NEGEV_LATITUDE = 30 + 51 / 60.0;

  private static final double                               NEGEV_LONGITUDE = 34 + 52 / 60.0;

  private static final String                               NEAR_DIMONA = "Near Dimona, Israel";

  /** {@code FileGeocodeCache.FILE_NAME}. */
  private static final String                               GEOCODE_CACHE_FILE = "GeocodeCache";

  /** The button in Play services' dialogs that turns the location service on: "OK" or "Turn on". */
  private static final BySelector                           PLAY_SERVICES_ACCEPT =
    By.pkg( "com.google.android.gms" ).clickable( true ).text( Pattern.compile( "(?i)ok|turn on" ) );

  private static final long                                 PLAY_SERVICES_DIALOG_WAIT_MILLIS = TimeUnit.SECONDS.toMillis( 10 );

  private static final long                                 OFFLINE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis( 15 );

  /** Instrumentation argument that makes a device that is still online fail the tests rather than skip them. */
  static final String                                       REQUIRE_OFFLINE_ARGUMENT = "requireOffline";

  @Rule
  public final GrantPermissionRule                          permissions = AppState.grantLocationPermissions();

  /** The location service with the test's GPS provider added, or null when the test has not added one. */
  @Nullable
  private LocationManager                                   mockedGps;

  /** Whether this class turned the network off, and so must turn it back on. */
  private static boolean                                    turnedTheNetworkOff;


  @BeforeClass
  public static void goOffline() throws Exception {

    if ( isOffline() ) {
      return;
    }

    shell( "svc wifi disable" );
    shell( "svc data disable" );
    turnedTheNetworkOff = true;

    long deadline = SystemClock.uptimeMillis() + OFFLINE_TIMEOUT_MILLIS;
    while ( !isOffline() && ( SystemClock.uptimeMillis() < deadline ) ) {
      Thread.sleep( 250 );
    }
  }


  @AfterClass
  public static void goBackOnline() throws Exception {

    if ( turnedTheNetworkOff ) {
      shell( "svc wifi enable" );
      shell( "svc data enable" );
      turnedTheNetworkOff = false;
    }
  }


  @Before
  public void startOfflineFromAFreshInstall() throws Exception {

    AppState.assumeSafeToWipe();
    AppState.reset();
    AppState.markNotificationQuestionAnswered();
    AppState.markExactAlarmQuestionAnswered();

    // The current version, so that no upgrade step runs.
    AppState.edit( ZmanimPrefs.VERSION ).putInt( "lastKnownCodeVersion", BuildConfig.VERSION_CODE ).commit();
    AppState.edit( ZmanimPrefs.LOCATION_MANAGEMENT )
      .putBoolean( "OVERRIDE_SYSTEM_TIME_ZONE", true )
      .putString( "TIME_ZONE_AREA", "Asia" )
      .putString( "TIME_ZONE_LOCALE", "Jerusalem" )
      .commit();

    assumeOrRequireOffline();
  }


  @After
  public void clearState() throws Exception {

    if ( mockedGps != null ) {
      mockedGps.removeTestProvider( LocationManager.GPS_PROVIDER );
    }
    AppState.reset();
  }


  @Test
  public void aTypedCityIsFoundInTheBundledDataWithItsNameAndElevation() throws Exception {

    AppState.useTypedPlace( "Jerusalem" );

    launchUntilTheHeaderShows( "Jerusalem, Israel" );

    JSONObject found = cachedPlace( "Jerusalem, Israel" );
    assertNotNull( "A place a source found is cached", found );
    assertEquals( "Jerusalem", found.getString( "userName" ) );
    assertEquals( 31.76904, found.getDouble( "lat" ), 1e-5 );
    assertEquals( 35.21633, found.getDouble( "lon" ), 1e-5 );
    assertEquals( 786.0, found.getDouble( "el" ), 1e-9 );
    assertEquals( "Asia/Jerusalem", found.getString( "tz" ) );
  }


  @Test
  public void typedCoordinatesFarFromATownAreNamedNearTheNearestOne() throws Exception {

    AppState.edit( ZmanimPrefs.LOCATION_MANAGEMENT )
      .putBoolean( "LATITUDE_LONGITUDE_POSITION", true )
      .putBoolean( "DYNAMIC_POSITION", false )
      .putBoolean( "STATIC_POSITION", false )
      .putString( "LATITUDE", "N 30 51 0" )
      .putString( "LONGITUDE", "E 34 52 0" )
      .putString( "LATITUDE_LONGITUDE_ELEVATION", "500" )
      .commit();

    launchUntilTheHeaderShows( NEAR_DIMONA );

    // A "near" name is not cached, so that the geocoder names the place once it can.
    assertNull( cachedPlace( NEAR_DIMONA ) );
  }


  @Test
  public void theDevicesLocationFarFromATownIsNamedNearTheNearestOne() throws Exception {

    AppState.edit( ZmanimPrefs.LOCATION_MANAGEMENT )
      .putBoolean( "DYNAMIC_POSITION", true )
      .putBoolean( "STATIC_POSITION", false )
      .putBoolean( "LATITUDE_LONGITUDE_POSITION", false )
      .commit();

    turnOnGooglesLocationServiceIfAsked();

    // The fix comes from a test GPS provider, which Play services' fused provider reads from. The test does not call
    //  Play services itself: in the minified build R8 renames its classes, and the test runs against that build too.
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    shell( "appops set " + context.getPackageName() + " android:mock_location allow" );
    LocationManager locations = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
    locations.addTestProvider( LocationManager.GPS_PROVIDER, false, false, false, false, true, true, true,
      Criteria.POWER_LOW, Criteria.ACCURACY_FINE );
    mockedGps = locations;
    locations.setTestProviderEnabled( LocationManager.GPS_PROVIDER, true );

    // Sent again and again until the app has named it, because a fix is only delivered to a request that is running.
    Thread mocker = new Thread( () -> {
      while ( !Thread.currentThread().isInterrupted() ) {
        try {
          locations.setTestProviderLocation( LocationManager.GPS_PROVIDER, negevFix() );
          Thread.sleep( 1000 );
        }
        catch ( InterruptedException e ) {
          return;
        }
        catch ( Exception e ) {
          // The next attempt sends it again.
        }
      }
    } );
    mocker.start();

    try {
      launchUntilTheHeaderShows( NEAR_DIMONA );
      assertNull( cachedPlace( NEAR_DIMONA ) );
    }
    finally {
      mocker.interrupt();
      mocker.join();
    }
  }


  /**
   * An emulator that has not agreed to Google's location service shows Play services' dialog asking to turn it on
   *  when the app first asks for the device's location. This opens the main screen once and accepts the dialog, as the
   *  user would, so that the location service is on before the test's own launch.
   */
  private static void turnOnGooglesLocationServiceIfAsked() {

    UiDevice device = UiDevice.getInstance( InstrumentationRegistry.getInstrumentation() );
    try ( ActivityScenario<ZmanimActivity> ignored = ActivityScenario.launch( ZmanimActivity.class ) ) {
      UiObject2 accept = device.wait( Until.findObject( PLAY_SERVICES_ACCEPT ), PLAY_SERVICES_DIALOG_WAIT_MILLIS );
      if ( accept != null ) {
        accept.click();
        device.wait( Until.gone( PLAY_SERVICES_ACCEPT ), PLAY_SERVICES_DIALOG_WAIT_MILLIS );
      }
    }
  }


  private static Location negevFix() {

    Location fix = new Location( LocationManager.GPS_PROVIDER );
    fix.setLatitude( NEGEV_LATITUDE );
    fix.setLongitude( NEGEV_LONGITUDE );
    fix.setAccuracy( 5f );
    fix.setTime( System.currentTimeMillis() );
    fix.setElapsedRealtimeNanos( SystemClock.elapsedRealtimeNanos() );
    return fix;
  }


  /**
   * Starts the main screen and waits for the header to show {@code name} and the zmanim to be listed, then checks
   *  that the device stayed offline throughout.
   */
  private static void launchUntilTheHeaderShows( String name ) throws Exception {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      ZmanimActivity activity = ViewWaits.activityOf( scenario );

      ViewWaits.until( "\"" + name + "\" in the header", () ->
        name.contentEquals( ( (TextView)activity.findViewById( R.id.location ) ).getText() ) );
      ViewWaits.until( "a non-empty zmanim list", () -> {
        ListView list = activity.findViewById( android.R.id.list );
        return ( list.getAdapter() != null ) && ( list.getAdapter().getCount() > 0 );
      } );
    }

    assertTrue( "The device must have stayed offline through the test", isOffline() );
  }


  /** The place the app cached under {@code name}, or null when it cached none. */
  @Nullable
  private static JSONObject cachedPlace( String name ) throws IOException, JSONException {

    for ( JSONObject place : cachedPlaces() ) {
      if ( name.equals( place.getString( "name" ) ) ) {
        return place;
      }
    }
    return null;
  }


  /** Every place in the app's geocode cache file: a header line, then one JSON object a line. */
  private static List<JSONObject> cachedPlaces() throws IOException, JSONException {

    File file = new File( InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir(),
      GEOCODE_CACHE_FILE );
    List<JSONObject> places = new ArrayList<>();
    if ( !file.exists() ) {
      return places;
    }

    String[] lines;
    try ( InputStream in = new FileInputStream( file ) ) {
      ByteArrayOutputStream text = new ByteArrayOutputStream();
      byte[] buffer = new byte[ 4096 ];
      for ( int read = in.read( buffer ); read != -1; read = in.read( buffer ) ) {
        text.write( buffer, 0, read );
      }
      lines = text.toString( StandardCharsets.UTF_8.name() ).split( "\n" );
    }
    for ( int i = 1; i < lines.length; i++ ) {
      if ( !lines[ i ].isEmpty() ) {
        places.add( new JSONObject( lines[ i ] ) );
      }
    }
    return places;
  }


  /**
   * Skips the test when the device still has a network, or fails it when {@value #REQUIRE_OFFLINE_ARGUMENT} is set:
   *  the shell user could not turn the network off.
   */
  private static void assumeOrRequireOffline() throws IOException {

    String stillOnline = "The device still has a network after the shell turned off Wi-Fi and mobile data";
    if ( Boolean.parseBoolean( InstrumentationRegistry.getArguments().getString( REQUIRE_OFFLINE_ARGUMENT ) ) ) {
      assertTrue( stillOnline, isOffline() );
    }
    else {
      Assume.assumeTrue( stillOnline + "; turn the network off as root and pass " + REQUIRE_OFFLINE_ARGUMENT + "=true",
        isOffline() );
    }
  }


  /** Whether the device has no default network, so that nothing in the app can reach the internet. */
  private static boolean isOffline() throws IOException {
    return shell( "dumpsys connectivity" ).contains( "Active default network: none" );
  }


  /** Runs a command as the shell user, waits for it to finish and answers what it printed. */
  private static String shell( String command ) throws IOException {

    UiAutomation automation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
    ByteArrayOutputStream printed = new ByteArrayOutputStream();
    try ( ParcelFileDescriptor output = automation.executeShellCommand( command );
          InputStream in = new FileInputStream( output.getFileDescriptor() ) ) {
      byte[] buffer = new byte[ 4096 ];
      for ( int read = in.read( buffer ); read != -1; read = in.read( buffer ) ) {
        printed.write( buffer, 0, read );
      }
    }
    return printed.toString( "UTF-8" );
  }

}  // End of OfflinePlaceNamingTest class
