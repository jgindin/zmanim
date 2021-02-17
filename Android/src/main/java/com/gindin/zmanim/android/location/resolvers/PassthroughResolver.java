/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * A resolver of last resort. Will simply resolve the location as itself.
 */
class PassthroughResolver
  extends LocationResolver {


  /**
   * Since we couldn't resolve this any other way, then the resolution here is just to leave it alone.
   */
  @Nullable
  @Override
  protected ZmanimLocation resolveFromCoordinates( @NonNull ZmanimLocation zmanimLocation ) {
    return zmanimLocation;
  }


  /**
   * Since we couldn't resolve this any other way, then the resolution here is just to leave it alone.
   */
  @Nullable
  @Override
  protected ZmanimLocation resolveFromName( @NonNull ZmanimLocation zmanimLocation ) {
    return zmanimLocation;
  }

} // End of PassthroughResolver class

