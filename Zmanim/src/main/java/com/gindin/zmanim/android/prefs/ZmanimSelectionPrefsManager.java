/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.StringRes;

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
  public @StringRes int onHandleUpgrade(
    int   thisCodeVersion,
    int   lastKnownCodeVersion
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

    if ( lastKnownCodeVersion <= 92 ) {

      // We added a separate Zman for when Shabbat ends. So, copy that over to the new ShabbatEnd zman.
      final String oldTzait = prefs.getString( Zmanim.Type.TzaitHacochavim.displayName, "" );

      final SharedPreferences.Editor editor = prefs.edit();
      editor.putString( Zmanim.Type.ShabbatEnd.displayName, oldTzait );

      editor.apply();
    }

    if ( lastKnownCodeVersion <= 229 ) {

      SharedPreferences.Editor editor = null;

      // There used to be separate zmanim for SBH and EB for Tzait and when Shabbat ends, but now the two
      //  synagogues are the same.
      String previousSetting = prefs.getString( Zmanim.Type.TzaitHacochavim.displayName, "" );
      if ( previousSetting.startsWith( "Seattle" ) ) {
        editor = prefs.edit();
        editor.putString( Zmanim.Type.TzaitHacochavim.displayName, "Seattle Sephardic" );
      }

      previousSetting = prefs.getString( Zmanim.Type.ShabbatEnd.displayName, "" );
      if ( previousSetting.startsWith( "Seattle" ) ) {
        if ( editor == null ) {
          editor = prefs.edit();
        }

        editor.putString( Zmanim.Type.ShabbatEnd.displayName, "Seattle Sephardic" );
      }


      if ( editor != null ) {
        editor.apply();
      }
    }

    return -1;
  }


  @Override
  protected Activities getPrefActivity() {

    // For now, this is OK, because we don't expect (at this time) to have an activity that can actually
    //  be invoked...
    return null;
  }

}  // End of ZmanimSelectionPrefsManager class
