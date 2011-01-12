/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.ZmanimActivity;

import java.util.Map;

/**
 * Takes care of dealing with the user's preferences--invoking the activity, notifying the main app
 *  that it needs to update, etc...
 */
public class PersonalPrefsManager {

	/** Need to know the activity so we can do things like build dialogs and spawn other activities. */
	private final ZmanimActivity                              activity;

	private final SharedPreferences                           personalPrefs;

	/**
	 * Prior to invoking the user's preferences activity, tuck away the current prefs so that if
	 *  the user doesn't make any changes, then we don't do anything upon return.
 	 */
	private Map<String,?>                                     prefsBeforeEditing;


	public PersonalPrefsManager( ZmanimActivity activity ) {

		this.activity = activity;
		personalPrefs = activity.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
	}


	@SuppressWarnings( { "MagicNumber" } )
	public void onHandleUpgrade(
		int             thisCodeVersion,
		int             lastKnownCodeVersion
	) {

		if ( lastKnownCodeVersion <= 66 ) {
			final SharedPreferences.Editor editor = personalPrefs.edit();
			editor.putBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), true );
			editor.commit();
		}
	}


	public void editPersonalPrefs() {

		// Capture all the prefs so that if the user doesn't make any changes, we don't do a bunch of recalculations.
		prefsBeforeEditing = personalPrefs.getAll();

		activity.startActivityForResult( new Intent( activity, PersonalPreferences.class ),
			Activities.PERSONAL_PREFERENCS.ordinal() );
	}


	public void personalPrefsChanged() {

		final Map<String, ?> prefsAfterEditing = personalPrefs.getAll();
		if ( !prefsAfterEditing.equals( prefsBeforeEditing ) ) {

			activity.rebuildUI();
		}

		prefsBeforeEditing = null;
	}


	public boolean getBoolean(
		String  prefName,
    boolean defaultValue
	) {

		return personalPrefs.getBoolean( prefName, defaultValue );
	}


}  // End of PersonalPrefsManager class
