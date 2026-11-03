/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

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
 * Users who selected a zman that the app now offers under another name keep it, under its new name, and are told.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_RenamedZmanim {

  private ZmanimActivity                                    activity;

  private SharedPreferences                                 selections;


  @Before
  public void createActivity() {
    activity = Robolectric.buildActivity( ZmanimActivity.class ).get();
    selections = activity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
    selections.edit().clear().commit();
  }


  @Test
  public void aRenamedZmanKeepsItsSelectionAndTheUserIsTold() {

    selections.edit()
      .putString( Zmanim.Type.TzaitHacochavim.displayName, "Geonim 4.37 deg" )
      .putString( Zmanim.Type.ShabbatEnd.displayName, "Geonim 4.61 deg" )
      .commit();

    Assert.assertEquals( R.string.renamed_zmanim, new ZmanimSelectionPrefsManager( activity ).renameZmanim() );

    Assert.assertEquals( "Geonim 4.42 deg", selections.getString( Zmanim.Type.TzaitHacochavim.displayName, null ) );
    Assert.assertEquals( "Geonim 4.66 deg", selections.getString( Zmanim.Type.ShabbatEnd.displayName, null ) );
  }


  @Test
  public void theNewNamesAreOnesTheAppOffers() {

    selections.edit()
      .putString( Zmanim.Type.FastEnd.displayName, "Geonim 4.37 deg" )
      .putString( Zmanim.Type.YomTovSheniCandleLighting.displayName, "Geonim 4.61 deg" )
      .commit();

    new ZmanimSelectionPrefsManager( activity ).renameZmanim();

    for ( Zmanim.Type type : new Zmanim.Type[] { Zmanim.Type.FastEnd, Zmanim.Type.YomTovSheniCandleLighting } ) {
      ZmanimLocation location = new ZmanimLocation.Builder().atLatitude( 47.6 ).atLongitude( -122.3 ).build();
      Zmanim zmanim = ZmanimFactory.get( location, HebrewCalendar.today(), type );

      String saved = selections.getString( type.displayName, null );
      Assert.assertEquals( saved, zmanim.getByName( saved ).name );
    }
  }


  @Test
  public void anUnchangedZmanStaysAndTheUserIsNotTold() {

    selections.edit()
      .putString( Zmanim.Type.TzaitHacochavim.displayName, "Geonim 4.8 deg" )
      .commit();

    Assert.assertEquals( -1, new ZmanimSelectionPrefsManager( activity ).renameZmanim() );

    Assert.assertEquals( "Geonim 4.8 deg", selections.getString( Zmanim.Type.TzaitHacochavim.displayName, null ) );
  }


  @Test
  public void aFreshInstallIsNotTold() {
    Assert.assertEquals( -1, new ZmanimSelectionPrefsManager( activity ).renameZmanim() );
  }


  @Test
  public void theUserIsOnlyToldOnce() {

    selections.edit()
      .putString( Zmanim.Type.TzaitHacochavim.displayName, "Geonim 4.37 deg" )
      .commit();

    ZmanimSelectionPrefsManager manager = new ZmanimSelectionPrefsManager( activity );
    manager.renameZmanim();

    Assert.assertEquals( -1, manager.renameZmanim() );
  }

}  // End of UT_RenamedZmanim class
