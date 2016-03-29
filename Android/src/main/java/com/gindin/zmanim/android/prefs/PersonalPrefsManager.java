/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zmanim;

/**
 * Takes care of dealing with the user's preferences--invoking the activity, notifying the main app
 *  that it needs to update, etc...
 */
public class PersonalPrefsManager
	extends AbstractPrefsManager<Class<PersonalPreferences>> {


	public PersonalPrefsManager( ZmanimActivity activity ) {
		super( activity, ZmanimPrefs.PERSONAL, PersonalPreferences.class );

	}


	@Override
	@SuppressWarnings( { "MagicNumber" } )
	public @StringRes	int onHandleUpgrade(
		int   thisCodeVersion,
		int   lastKnownCodeVersion
	) {

		int upgradeNotesId = -1;

		if ( lastKnownCodeVersion <= 66 ) {
			final SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false );
			editor.apply();
		}

		if ( lastKnownCodeVersion <= 229 ) {
			final SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true );
			editor.apply();

			upgradeNotesId = R.string.notification_info;
		}


		if ( lastKnownCodeVersion <= 283 ) {

			// Early beta versions only allowed the alerts to be on or off.
			final SharedPreferences.Editor editor = prefs.edit();

			ZmanNotificationScheduler.NotificationDefaults notificationDefaults = new ZmanNotificationScheduler.NotificationDefaults();

			for ( Zmanim.Type type : Zmanim.Type.values() ) {
				editor.remove( type.name() );
				editor.putInt( type.name, notificationDefaults.getDefaultZmanAlertTime( type ) );
			}

			editor.apply();
		}

		return upgradeNotesId;
	}


	@Override
	protected Activities getPrefActivity() {
		return Activities.PERSONAL_PREFERENCS;
	}


}  // End of PersonalPrefsManager class
