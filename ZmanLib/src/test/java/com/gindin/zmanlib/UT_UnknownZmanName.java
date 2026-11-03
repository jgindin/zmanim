/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A saved selection can name a zman that the app no longer offers.
 */
public class UT_UnknownZmanName {

  @Test
  public void aNameThatIsNotOfferedAnyMoreGivesTheDefaultZman() {

    ZmanimLocation location = new ZmanimLocation.Builder().atLatitude( 47.6 ).atLongitude( -122.3 ).build();
    Zmanim zmanim = ZmanimFactory.get( location, HebrewCalendar.today(), Zmanim.Type.TzaitHacochavim );

    Assertions.assertSame( zmanim.getDefault(), zmanim.getByName( "Geonim 3.65 deg" ) );
  }

}  // End of UT_UnknownZmanName class
