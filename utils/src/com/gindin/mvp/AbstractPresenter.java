/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp;

import com.gindin.util.eventBus.EventBus;

/**
 * Abstract, base implementation of a Presenter.
 */
public abstract class AbstractPresenter<V extends View>
	implements Presenter<V> {

	private final V                                           view;

	private final EventBus                                    eventBus;


	protected AbstractPresenter( V view, EventBus eventBus ) {
		this.view = view;
		this.eventBus = eventBus;

		view.setPresenter( this );
	}


	protected final V getView() {
		return view;
	}


	protected final EventBus getEventBus() {
		return eventBus;
	}


} // End of AbstractPresenter class
