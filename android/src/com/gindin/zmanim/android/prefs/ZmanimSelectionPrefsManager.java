/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Takes care of managing the preferences around zmanim selection.
 */
public class ZmanimSelectionPrefsManager
	extends AbstractPrefsManager<Class<ZmanimSelectionPreferences>> {



	public ZmanimSelectionPrefsManager(
		ZmanimActivity activity
	) {

		super( activity, ZmanimPrefs.ZMANIM_SELECTION, ZmanimSelectionPreferences.class );
	}


	@SuppressWarnings( { "MagicNumber" } )
	public void onHandleUpgrade(
		int             thisCodeVersion,
		int             lastKnownCodeVersion
	) {

		if ( -1 == lastKnownCodeVersion ) {

			// We used to store the zmanim selection under the shared prefs key "ZmanimSelection", but now we have an enum
			//  and it's the com.gindin.zmanim.android.prefs.ZmanimPrefs.ZMANIM_SELECTION.
			final SharedPreferences oldPrefs = activity.getSharedPreferences( "ZmanimSelection", Context.MODE_PRIVATE );
			final Map<String,?> zmanimSelections = oldPrefs.getAll();
			if ( null != zmanimSelections ) {

				final SharedPreferences newPrefs = activity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
				final SharedPreferences.Editor newPrefsEditor = newPrefs.edit();

				for ( Map.Entry<String, ?> entry : zmanimSelections.entrySet() ) {
					// We know that we only stored strings here.
					newPrefsEditor.putString( entry.getKey(), (String)entry.getValue() );
				}

				newPrefsEditor.apply();

				// Now remove the old prefs.
				final SharedPreferences.Editor oldPrefsEditor = oldPrefs.edit();
				oldPrefsEditor.clear();
				oldPrefsEditor.apply();
			}
		}
		else if ( lastKnownCodeVersion <= 92 ) {

			// We added a separate Zman for when Shabbat ends. So, copy that over to the new ShabbatEnd zman.
			final String oldTzait = prefs.getString( Zmanim.Type.TzaitHacochavim.name, "" );

			final SharedPreferences.Editor editor = prefs.edit();
			editor.putString( Zmanim.Type.ShabbatEnd.name, oldTzait );

			editor.apply();
		}
	}


	@Override
	protected Activities getPrefActivity() {

		// For now, this is OK, because we don't expect (at this time) to have an activity that can actually
		//  be invoked...
		return null;
	}

}  // End of ZmanimSelectionPrefsManager class
