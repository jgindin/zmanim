/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.AbstractMvpModule;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.zmanList.ZmanimSelector;
import com.gindin.zmanlib.zman.ZmanFormatter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

/**
 *
 */
public class ZmanimDisplayModule
  extends AbstractMvpModule<ZmanimPresenter<ZmanimView>> {

  private final PersonalPrefsManager                        personalPrefsManager;


  public ZmanimDisplayModule(
    FragmentActivity      activity,
    EventBus              eventBus,
    PersonalPrefsManager  personalPrefsManager
  ) {
    super( activity, eventBus );
    this.personalPrefsManager = personalPrefsManager;
  }


  @Override
  public ZmanimPresenter<ZmanimView> create() {

    boolean showSeconds = personalPrefsManager.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
    ZmanFormatter zmanFormatter = new ZmanFormatter( showSeconds );

    ZmanimViewImpl view = new ZmanimViewImpl( activity, new ZmanimSelector( activity, zmanFormatter ) );

    SharedPreferences zmanSelectionPrefs = activity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

    return new ZmanimPresenterImpl<ZmanimView>( view, eventBus, zmanSelectionPrefs, zmanFormatter );
  }

} // End of ZmanimDisplayModule class
