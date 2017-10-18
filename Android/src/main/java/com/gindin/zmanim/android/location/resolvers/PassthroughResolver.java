/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.SharedPreferences;

/**
 * A resolver of last resort. Will simply resolve the location as itself.
 */
class PassthroughResolver
  extends LocationResolver {


  PassthroughResolver(
    EventBus          eventBus,
    SharedPreferences locationManagementPrefs
  ) {
    super( eventBus, locationManagementPrefs );
  }


  @Override
  public void onDestroy() {

  }


  /**
   * Since we couldn't resolve this any other way, then the resolution here is just to leave it alone.
   */
  @Nullable
  @Override
  protected ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation ) {
    return zmanimLocation;
  }


  /**
   * Since we couldn't resolve this any other way, then the resolution here is just to leave it alone.
   */
  @Nullable
  @Override
  protected ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation ) {
    return zmanimLocation;
  }

} // End of PassthroughResolver class

