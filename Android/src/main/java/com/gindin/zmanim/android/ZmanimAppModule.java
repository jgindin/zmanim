package com.gindin.zmanim.android;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ZmanimAppModule {

  @ContributesAndroidInjector
  abstract ZmanimActivity contributeZmanimActivityInjector();
}
