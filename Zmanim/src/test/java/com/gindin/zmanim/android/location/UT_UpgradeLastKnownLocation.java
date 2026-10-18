/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.zmanim.android.upgrade.UpgradeFixtures;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.TimeZone;

/**
 * The last known location file written by the released app is read back through the app's own
 *  {@link ZmanLocationUtil#load}.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_UpgradeLastKnownLocation {

  private Context                                           context;


  @Before
  public void installFilesWrittenByReleasedApp() throws IOException {

    context = ApplicationProvider.getApplicationContext();
    UpgradeFixtures.install( context );
  }


  @Test
  public void loadsTheSavedLocation() {

    ZmanimLocation location = ZmanLocationUtil.load( context );

    Assert.assertFalse( location.isInvalid );
    Assert.assertEquals( 42.6526, location.getLatitude(), 1e-9 );
    Assert.assertEquals( -73.7562, location.getLongitude(), 1e-9 );
    Assert.assertEquals( 47.0, location.getElevation(), 1e-9 );
    Assert.assertEquals( TimeZone.getTimeZone( "America/New_York" ), location.getTimeZone() );
    Assert.assertEquals( "fused", location.providerName );
    Assert.assertEquals( 23.456f, location.accuracy, 1e-4f );
    Assert.assertFalse( location.hasName() );
    Assert.assertEquals( "", location.originalUserName );
  }

}
