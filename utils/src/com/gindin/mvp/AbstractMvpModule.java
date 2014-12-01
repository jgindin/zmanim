/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp;

import com.gindin.eventBus.EventBus;

import android.support.v4.app.FragmentActivity;

/**
 * Base for a module which is used to tie together a presenter and view.
 */
public abstract class AbstractMvpModule<P extends Presenter>
	implements MvpModule<P> {

	protected final FragmentActivity                          activity;

	protected final EventBus                                  eventBus;


	protected AbstractMvpModule(
		FragmentActivity activity,
		EventBus eventBus
	) {
		this.activity = activity;
		this.eventBus = eventBus;
	}


} // End of AbstractMvpModule class
