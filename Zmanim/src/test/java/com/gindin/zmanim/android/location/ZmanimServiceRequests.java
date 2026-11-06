/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the work the location code hands to {@link ZmanimService}, which is what the screens are told. Below API 26
 *  {@code JobIntentService} starts the service with the work's intent, and Robolectric records it; tests that use
 *  this run with {@code @Config( sdk = 24 )}.
 */
public final class ZmanimServiceRequests {

  /** The names of {@code ZmanimService.ActionParams}, which is private. */
  private static final String                               ERROR = "ERROR";

  private static final String                               LOCATION = "LOCATION";


  /** One piece of work sent to {@link ZmanimService}. */
  public static final class Request {

    public final String                                     action;

    @Nullable
    public final LocationError                              error;

    /** The location sent with it: the new location, or the last known one with an error. */
    @Nullable
    public final ZmanimLocation                             location;

    Request( Intent intent ) {
      action = intent.getAction();

      Bundle errorBundle = intent.getBundleExtra( ERROR );
      error = ( errorBundle == null ) ? null : LocationError.fromBundle( errorBundle );

      Bundle locationBundle = intent.getBundleExtra( LOCATION );
      location = ( locationBundle == null ) ? null : ZmanLocationUtil.fromBundle( locationBundle );
    }
  }


  /** Every piece of work sent to {@link ZmanimService} since the last call, oldest first. */
  public static List<Request> drain() {
    return requestsIn( drainIntents() );
  }


  /** Every intent that started any service since the last call, oldest first. */
  public static List<Intent> drainIntents() {

    ShadowApplication application = Shadows.shadowOf( (Application)ApplicationProvider.getApplicationContext() );

    List<Intent> intents = new ArrayList<>();
    for ( Intent intent = application.getNextStartedService(); intent != null; intent = application.getNextStartedService() ) {
      intents.add( intent );
    }
    return intents;
  }


  /** The intents that start the given service. */
  public static List<Intent> startingService( List<Intent> intents, Class<?> serviceClass ) {

    List<Intent> starting = new ArrayList<>();
    for ( Intent intent : intents ) {
      if ( ( intent.getComponent() != null ) && serviceClass.getName().equals( intent.getComponent().getClassName() ) ) {
        starting.add( intent );
      }
    }
    return starting;
  }


  /** The work for {@link ZmanimService} among the given intents. */
  public static List<Request> requestsIn( List<Intent> intents ) {

    List<Request> requests = new ArrayList<>();
    for ( Intent intent : startingService( intents, ZmanimService.class ) ) {
      requests.add( new Request( intent ) );
    }
    return requests;
  }


  private ZmanimServiceRequests() {
  }

}  // End of ZmanimServiceRequests class
