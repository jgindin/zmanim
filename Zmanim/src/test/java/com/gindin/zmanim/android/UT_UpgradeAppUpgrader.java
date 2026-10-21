/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;
import com.gindin.zmanim.android.upgrade.UpgradeFixtures;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Starting the new build over the data of the released build (version code 409) must leave the user's saved
 *  preferences as they were and record the new version.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_UpgradeAppUpgrader {

  private ZmanimActivity                                    activity;

  private Map<String, Map<String, ?>>                       before;


  @Before
  public void installPrefsWrittenByReleasedApp() throws IOException {

    Context context = ApplicationProvider.getApplicationContext();
    UpgradeFixtures.install( context );

    activity = Robolectric.buildActivity( ZmanimActivity.class ).get();

    // The flag is process-wide, so an earlier test in this JVM that ran the upgrade steps over a fresh install (as
    //  launching the main activity does) leaves it set. Start from the state of a process that has not upgraded.
    LocationToAddressConverter.wipeCacheOnStart = false;

    before = new HashMap<>();
    for ( ZmanimPrefs prefs : new ZmanimPrefs[] { ZmanimPrefs.PERSONAL, ZmanimPrefs.ZMANIM_SELECTION, ZmanimPrefs.LOCATION_MANAGEMENT } ) {
      before.put( prefs.name(), new HashMap<>( preferences( prefs ).getAll() ) );
    }
  }


  @After
  public void restoreProcessWideState() {
    // The upgrade steps set the flag; later tests in this JVM expect the cache to be loaded, not wiped.
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  /**
   * {@link AppUpgrader#handle} runs the upgrade steps only when this build's version code is above the last known one.
   *  The released version is 409, so while the build's own version code is not above that, this test does not exercise
   *  the steps and is skipped; raise the build's version code above 409 to make it meaningful.
   */
  @Test
  public void upgradingFromTheReleasedVersionKeepsThePreferences() {

    Assume.assumeTrue( "The build's version code (" + BuildConfig.VERSION_CODE + ") must be raised above the released 409 "
      + "before AppUpgrader.handle runs any upgrade step over the released data", BuildConfig.VERSION_CODE > 409 );

    Assert.assertEquals( 409, preferences( ZmanimPrefs.VERSION ).getInt( "lastKnownCodeVersion", -1 ) );

    AppUpgrader.handle( activity, new PersonalPrefsManager( activity ), new ZmanimSelectionPrefsManager( activity ) );

    assertPreferencesUnchanged();
    Assert.assertEquals( BuildConfig.VERSION_CODE, preferences( ZmanimPrefs.VERSION ).getInt( "lastKnownCodeVersion", -1 ) );
  }


  /**
   * {@link AppUpgrader#handle} only runs the upgrade steps when this build's version code is above the last known
   *  one, so these call the steps with a version code that is above it for certain.
   */
  @Test
  public void everyUpgradeStepFromTheReleasedVersionLeavesThePreferencesAlone() throws Exception {

    int laterVersionCode = 10_000;

    Assert.assertEquals( -1, new PersonalPrefsManager( activity ).onHandleUpgrade( laterVersionCode, 409 ) );
    Assert.assertEquals( -1, new ZmanimSelectionPrefsManager( activity ).onHandleUpgrade( laterVersionCode, 409 ) );

    Method upgradeElevationPref = AppUpgrader.class.getDeclaredMethod( "upgradeElevationPref", ZmanimActivity.class, int.class );
    upgradeElevationPref.setAccessible( true );
    upgradeElevationPref.invoke( null, activity, 409 );

    assertPreferencesUnchanged();
    Assert.assertFalse( LocationToAddressConverter.wipeCacheOnStart );
  }


  private void assertPreferencesUnchanged() {

    for ( Map.Entry<String, Map<String, ?>> entry : before.entrySet() ) {
      Assert.assertEquals( entry.getKey(), entry.getValue(), preferences( ZmanimPrefs.valueOf( entry.getKey() ) ).getAll() );
    }
  }


  private SharedPreferences preferences( ZmanimPrefs prefs ) {
    return activity.getSharedPreferences( prefs.name(), Context.MODE_PRIVATE );
  }

}
