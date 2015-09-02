/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.ZmanimActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.annotation.StringRes;

import java.util.Map;

/**
 *
 */
public abstract class AbstractPrefsManager<PREF_ACTIVITY_CLASS extends Class<? extends PreferenceActivity>> {

	/** Need to know the activity so we can do things like build dialogs and spawn other activities. */
	protected final ZmanimActivity                            activity;

	private final PREF_ACTIVITY_CLASS                         prefActivityClass;

	protected final SharedPreferences                         prefs;

	/**
	 * Prior to invoking the user's preferences activity, tuck away the current prefs so that if
	 *  the user doesn't make any changes, then we don't do anything upon return.
 	 */
	private Map<String,?>                                     prefsBeforeEditing;


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

		// Capture all the prefs so that if the user doesn't make any changes, we don't do a bunch of recalculations.
		prefsBeforeEditing = prefs.getAll();

		Activities prefActivity = getPrefActivity();
		activity.startActivityForResult( new Intent( activity, prefActivityClass ), prefActivity.ordinal() );
	}


	public void personalPrefsChanged() {

		final Map<String, ?> prefsAfterEditing = prefs.getAll();
		if ( !prefsAfterEditing.equals( prefsBeforeEditing ) ) {
			onPrefsChanged();
		}

		prefsBeforeEditing = null;
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


	/**
	 * Called after the preferences are done being edited. Default action is to rebuild the entire UI.
	 */
	protected void onPrefsChanged() {
		activity.rebuildUI();
	}

}  // End of AbstractPrefsManager class
