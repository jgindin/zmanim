/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.location.acquirers.DynamicLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.LatitudeLongitudeLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.StaticLocationAcquirer;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

/**
 * Which acquirer {@link ZmanimLocationManager} uses when more than one way of finding the location is turned on, or
 *  none is: the device's location wins, then a typed place, then entered coordinates.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
public class UT_ZmanimLocationManager {

  private Context                                           context;

  private SharedPreferences                                 prefs;


  @Before
  public void setup() {
    context = ApplicationProvider.getApplicationContext();
    prefs = context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    prefs.edit().clear().commit();
  }


  private Object chosenAcquirer() {

    EventBus eventBus = new EventBus();
    ZmanimLocationManager manager = new ZmanimLocationManager( context, eventBus, prefs );
    eventBus.fire( new RequestLocationUpdateEvent( RequestLocationUpdateEvent.CachedLocationUsage.NEVER ) );

    // The manager keeps the acquirer it chose in a private field; there is no other way to see which one it is.
    try {
      Field field = ZmanimLocationManager.class.getDeclaredField( "currentAcquirer" );
      field.setAccessible( true );
      return field.get( manager );
    }
    catch ( ReflectiveOperationException e ) {
      throw new AssertionError( e );
    }
  }


  private void turnOn( LocationProviders... providers ) {

    SharedPreferences.Editor editor = prefs.edit();
    for ( LocationProviders provider : providers ) {
      editor.putBoolean( provider.name(), true );
    }
    editor.commit();
  }


  @Test
  public void theDevicesLocationWinsOverEverythingElse() {

    turnOn( LocationProviders.DYNAMIC_POSITION, LocationProviders.STATIC_POSITION,
      LocationProviders.LATITUDE_LONGITUDE_POSITION );

    Assert.assertTrue( chosenAcquirer() instanceof DynamicLocationAcquirer );
  }


  @Test
  public void aTypedPlaceWinsOverEnteredCoordinates() {

    turnOn( LocationProviders.STATIC_POSITION, LocationProviders.LATITUDE_LONGITUDE_POSITION );

    Assert.assertTrue( chosenAcquirer() instanceof StaticLocationAcquirer );
  }


  @Test
  public void enteredCoordinatesAreUsedWhenTheyAreTheOnlyChoice() {

    turnOn( LocationProviders.LATITUDE_LONGITUDE_POSITION );

    Assert.assertTrue( chosenAcquirer() instanceof LatitudeLongitudeLocationAcquirer );
  }


  @Test
  public void withNothingChosenTheDevicesLocationIsUsedAndRecorded() {

    Assert.assertTrue( chosenAcquirer() instanceof DynamicLocationAcquirer );
    Assert.assertTrue( prefs.getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false ) );
  }

}  // End of UT_ZmanimLocationManager class
