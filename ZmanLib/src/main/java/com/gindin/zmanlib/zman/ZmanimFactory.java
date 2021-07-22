/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import com.gindin.util.Pair;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.kosherjava.zmanim.util.GeoLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle creation of zmanim.
 */
public class ZmanimFactory {

  private static final Map<Pair<GeoLocation, HebrewDate>, ZmanimFactory>      locationToFactory = new HashMap<>();

  private static boolean                                                      useElevation = false;

  public static Zmanim get(
    GeoLocation location,
    HebrewDate  date,
    Zmanim.Type type
  ) {

    assert null != location;

    Pair<GeoLocation,HebrewDate>  key = new Pair<>( location, date );
    ZmanimFactory factory = locationToFactory.get( key );
    if ( null == factory ) {
      factory = new ZmanimFactory( location, date );
      locationToFactory.put( key, factory );
    }

    factory.usingElevation( useElevation );

    return factory.get( type );
  }


  public static void useElevation( boolean use ) {
    useElevation = use;
    for ( ZmanimFactory factory : locationToFactory.values() ) {
      factory.usingElevation( use );
    }
  }



  /** Cache so that we don't create the zmanim instances more than once. */
  private final Map<Zmanim.Type, Zmanim>                    typeToZmanimMap = new HashMap<>();

  private final ZmanimCalculator                            zmanimCalculator;

  private boolean                                           usingElevation;


  // TODO: If we were using Dagger to create as needed, we could just have each instance get
  //  injected with something that could be called when the user wants (doesn't want) to use
  //  elevation. Instead...well, we've got something more complicated to deal with...
  private ZmanimFactory(
    GeoLocation location,
    HebrewDate  date
  ) {
    this.zmanimCalculator = new ZmanimCalculator( location, date );
  }


  void usingElevation( boolean useElevation ) {

    if ( usingElevation != useElevation ) {
      usingElevation = useElevation;
      zmanimCalculator.setUseElevation( useElevation );

      typeToZmanimMap.clear();
    }
  }


  private Zmanim get(
    Zmanim.Type type
  ) {

    synchronized ( typeToZmanimMap ) {

      Zmanim zmanim = typeToZmanimMap.get( type );
      if ( null != zmanim ) {
        return zmanim;
      }

      zmanim = type.get( zmanimCalculator );
      typeToZmanimMap.put( type, zmanim );
      return zmanim;
    }
  }
  
}  // End of ZmanimFactory class
