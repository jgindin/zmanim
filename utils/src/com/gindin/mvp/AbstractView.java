/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp;

/**
 * Abstract, base implementation of a View.
 *<p/>
 *
 * Note that as of yet, there is no actual Android View here. That will come later as I understand how to play out
 *  this pattern.
 */
public abstract class AbstractView<P extends Presenter>
	implements View<P> {

	private P                                                 presenter;


	protected AbstractView() {
	}


	@Override
	public void setPresenter( P presenter ) {
		this.presenter = presenter;
	}


	protected final P getPresenter() {
		return presenter;
	}


} // End of AbstractView class
