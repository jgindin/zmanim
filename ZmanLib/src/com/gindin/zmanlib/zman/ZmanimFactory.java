/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import com.gindin.util.Pair;
import com.gindin.zmanlib.calendar.HebrewDate;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle creation of zmanim.
 */
public class ZmanimFactory {

	private static final Map<Pair<GeoLocation, HebrewDate>, ZmanimFactory>      locationToFactory = new HashMap<Pair<GeoLocation, HebrewDate>, ZmanimFactory>();

	/** Cache so that we don't create the zmanim instances more than once. */
	private final Map<Zmanim.Type, Zmanim>                    typeToZmanimMap = new HashMap<Zmanim.Type, Zmanim>();

	private final ZmanimCalculator                            zmanimCalculator;



	public static Zmanim get(
		GeoLocation location,
		HebrewDate  date,
		Zmanim.Type type
	) {

		assert null != location;

		Pair<GeoLocation,HebrewDate>  key = new Pair<GeoLocation, HebrewDate>( location, date );
		ZmanimFactory factory = locationToFactory.get( key );
		if ( null == factory ) {
			factory = new ZmanimFactory( location, date );
			locationToFactory.put( key, factory );
		}

		return factory.get( type );
	}

	private ZmanimFactory(
		GeoLocation location,
		HebrewDate  date
	) {
		this.zmanimCalculator = new ZmanimCalculator( location, date );
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
