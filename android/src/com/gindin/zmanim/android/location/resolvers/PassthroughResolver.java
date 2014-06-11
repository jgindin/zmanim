/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.location.ZmanimLocation;

/**
 * A resolver of last resort. Will simply resolve the location as itself.
 */
class PassthroughResolver
	extends LocationResolver {


	PassthroughResolver( EventBus eventBus ) {
		super( eventBus );
	}


	/**
	 * Since we couldn't resolve this any other way, then the resolution here is just to leave it alone.
	 */
	@Override
	protected ZmanimLocation resolveFromCoordinatesInBackground( ZmanimLocation zmanimLocation ) {
		return zmanimLocation;
	}


	/**
	 * Since we couldn't resolve this any other way, then the resolution here is just to leave it alone.
	 */
	@Override
	protected ZmanimLocation resolveFromNameInBackground( ZmanimLocation zmanimLocation ) {
		return zmanimLocation;
	}

} // End of PassthroughResolver class

