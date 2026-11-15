/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanlib.location.LocationSource;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.SupervisorKt;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage;

/**
 * The device's location, from Play services' fused location provider through {@link FusedLocationSource}.
 *  {@link DeviceLocationBridge} hands each answer to the {@code ZmanimService}: a fix to be named, or the
 *  {@link LocationError} code the screens show when there is none.
 */
public class DynamicLocationAcquirer
  extends LocationAcquirer {

  private final DeviceLocationBridge                        bridge;


  public DynamicLocationAcquirer(
    SharedPreferences     locationManagementPrefs,
    @NonNull Context      context
  ) {

    this( locationManagementPrefs, context, FusedLocationSource.create( context ),
      CoroutineScopeKt.CoroutineScope( SupervisorKt.SupervisorJob( null ).plus( Dispatchers.getDefault() ) ) );
  }


  @VisibleForTesting
  DynamicLocationAcquirer(
    SharedPreferences     locationManagementPrefs,
    @NonNull Context      context,
    LocationSource        source,
    CoroutineScope        scope
  ) {

    super( locationManagementPrefs, context );

    DeviceLocationSink sink = new DeviceLocationSink() {
      @Override
      public void acquired( @NonNull ZmanimLocation location ) {
        fireLocationAcquired( location );
      }


      @Override
      public void notAcquired( @NonNull LocationError error, @Nullable ZmanimLocation lastKnown ) {
        fireLocationNotAcquired( error, lastKnown );
      }
    };

    bridge = new DeviceLocationBridge( source, sink,
      location -> {
        ZmanLocationUtil.save( context, location );
        return kotlin.Unit.INSTANCE;
      },
      () -> ZmanLocationUtil.load( context ),
      scope
    );
  }


  @Override
  public void acquire( final CachedLocationUsage cachedLocationUsage ) {
    bridge.start( cachedLocationUsage == CachedLocationUsage.PREFER );
  }


  @Override
  public void awaitCompletion() {
    bridge.awaitCompletion();
  }


  @Override
  public void cancel() {

    bridge.cancel();
    super.cancel();
  }

}  // End of DynamicLocationAcquirer class
