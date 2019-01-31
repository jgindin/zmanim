/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.ZmanimActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;

/**
 *
 */
public abstract class AbstractPrefsManager<PREF_ACTIVITY_CLASS extends Class<? extends Activity>> {

  /** Need to know the activity so we can do things like build dialogs and spawn other activities. */
  protected final ZmanimActivity                            activity;

  private final PREF_ACTIVITY_CLASS                         prefActivityClass;

  protected final SharedPreferences                         prefs;



  protected AbstractPrefsManager(
    ZmanimActivity      activity,
    ZmanimPrefs         prefsType,
    PREF_ACTIVITY_CLASS prefActivityClass
    ) {
    this.activity = activity;
    this.prefActivityClass = prefActivityClass;
    prefs = activity.getSharedPreferences( prefsType.name(), Context.MODE_PRIVATE );
  }


  /**
   * Called at app startup to perform any upgrade tasks.
   *
   * @param thisCodeVersion The current code version
   * @param lastKnownCodeVersion The last code version which was installed
   *
   * @return Identifier of release notes to display or -1 if there are none.
   */
  public abstract @StringRes int onHandleUpgrade(
    int thisCodeVersion,
    int lastKnownCodeVersion
  );


  /**
   * Called to invoke the activity which will allow the preferences to be edited.
   */
  public void editPrefs() {

    Activities prefActivity = getPrefActivity();
    activity.startActivityForResult( new Intent( activity, prefActivityClass ), prefActivity.ordinal() );
  }


  public boolean getBoolean(
    String  prefName,
    boolean defaultValue
  ) {

    return prefs.getBoolean( prefName, defaultValue );
  }


  /**
   * Called so we know what identifier to associate with the launched activity.
   *
   * @return The appropriate value from the Activities enum.
   */
  protected abstract Activities getPrefActivity();


}  // End of AbstractPrefsManager class
