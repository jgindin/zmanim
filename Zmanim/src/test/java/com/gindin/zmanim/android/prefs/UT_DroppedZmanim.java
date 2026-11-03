/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Users who selected a zman the app no longer offers are told about it, and go back to the default zman.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_DroppedZmanim {

  private ZmanimActivity                                    activity;

  private SharedPreferences                                 selections;


  @Before
  public void createActivity() {
    activity = Robolectric.buildActivity( ZmanimActivity.class ).get();
    selections = activity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
    selections.edit().clear().commit();
  }


  @Test
  public void aDroppedZmanIsClearedAndTheUserIsTold() {

    selections.edit()
      .putString( Zmanim.Type.TzaitHacochavim.displayName, "Geonim 3.65 deg" )
      .putString( Zmanim.Type.ShabbatEnd.displayName, "Geonim 5.88 deg" )
      .putString( Zmanim.Type.MinchaGedola.displayName, "Baal HaTanya Lechumra 30 Minutes" )
      .commit();

    Assert.assertEquals( R.string.dropped_zmanim, new ZmanimSelectionPrefsManager( activity ).removeDroppedZmanim() );

    Assert.assertNull( selections.getString( Zmanim.Type.TzaitHacochavim.displayName, null ) );
    Assert.assertNull( selections.getString( Zmanim.Type.ShabbatEnd.displayName, null ) );
    Assert.assertNull( selections.getString( Zmanim.Type.MinchaGedola.displayName, null ) );
  }


  @Test
  public void aKeptZmanStaysAndTheUserIsNotTold() {

    selections.edit()
      .putString( Zmanim.Type.TzaitHacochavim.displayName, "Geonim 4.8 deg" )
      .commit();

    Assert.assertEquals( -1, new ZmanimSelectionPrefsManager( activity ).removeDroppedZmanim() );

    Assert.assertEquals( "Geonim 4.8 deg", selections.getString( Zmanim.Type.TzaitHacochavim.displayName, null ) );
  }


  @Test
  public void aFreshInstallIsNotTold() {
    Assert.assertEquals( -1, new ZmanimSelectionPrefsManager( activity ).removeDroppedZmanim() );
  }


  @Test
  public void theUserIsOnlyToldOnce() {

    selections.edit()
      .putString( Zmanim.Type.TzaitHacochavim.displayName, "Geonim 3.65 deg" )
      .commit();

    ZmanimSelectionPrefsManager manager = new ZmanimSelectionPrefsManager( activity );
    manager.removeDroppedZmanim();

    Assert.assertEquals( -1, manager.removeDroppedZmanim() );
  }

}  // End of UT_DroppedZmanim class
