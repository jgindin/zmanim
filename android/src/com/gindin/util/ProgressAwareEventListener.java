/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.util;

import java.util.EventListener;

/**
 *
 */
public interface ProgressAwareEventListener
	extends EventListener {

	void onProgress( String message );

}  // End of ProgressAwareEventListener class
