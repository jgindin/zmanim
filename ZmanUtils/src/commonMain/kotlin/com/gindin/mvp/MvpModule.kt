/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp

/**
 * Intended to be the external mechanism by which a Presenter, with its associated View, are created.
 *
 * Temporary until I get Guice / Dagger / whatever-DI implemented.
 */
public interface MvpModule<P> {

  public fun create(): P
}
