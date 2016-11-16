/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Activity for editing personal preferences.
 */
public class PersonalPreferences
	extends AppCompatActivity {

	public static enum Prefs {
		SHOW_SECONDS,
		USE_ELEVATION,
		SHOW_NOTIFICATIONS,
    DISABLE_NOTIFICATIONS_ON_SHABBAT,
		NOTIFICATIONS_FOR_CURRENT_LOCATION
	}


  @Override
  protected void onCreate( @Nullable Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    if ( getSupportFragmentManager().findFragmentById( android.R.id.content ) == null ) {
      getSupportFragmentManager()
        .beginTransaction()
        .add( android.R.id.content, new PersonalPrefsFragment() )
        .commit();
    }
  }


  public static class PersonalPrefsFragment
    extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(
      Bundle bundle,
      String s
    ) {

      getPreferenceManager().setSharedPreferencesName( ZmanimPrefs.PERSONAL.name() );
      addPreferencesFromResource( R.xml.personal_prefs );
    }
  }


}  // End of PersonalPreferences class
