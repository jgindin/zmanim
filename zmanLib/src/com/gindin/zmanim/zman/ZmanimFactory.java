/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.util.Pair;
import com.gindin.zmanim.calendar.HebrewDate;
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
		HebrewDate date
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

			switch ( type ) {
				case Alot:
					zmanim = new AlotHashachar( zmanimCalculator );
					break;

				case Talit:
					zmanim = new Talit( zmanimCalculator );
					break;

				case Hanetz:
					zmanim = new Hanetz( zmanimCalculator );
					break;

				case SofZmanShema:
					zmanim = new SofZmanShema( zmanimCalculator );
					break;

				case SofZmanTefila:
					zmanim = new SofZmanTefila( zmanimCalculator );
					break;

				case Chatzot:
					zmanim = new Chatzot( zmanimCalculator );
					break;

				case MinchaGedola:
					zmanim = new MinchaGedola( zmanimCalculator );
					break;

				case MinchaKetana:
					zmanim = new MinchaKetana( zmanimCalculator );
					break;

				case PlagHaMincha:
					zmanim = new PlagHaMincha( zmanimCalculator );
					break;

				case CandleLighting:
					zmanim = new CandleLighting( zmanimCalculator );
					break;

				case Shkia:
					zmanim = new Shkia( zmanimCalculator );
					break;

				case BeinHashmashot:
					zmanim = new BeinHashmashot( zmanimCalculator );
					break;

				case LatestMincha:
					zmanim = new LatestMincha( zmanimCalculator );
					break;

				case FastEnd:
					zmanim = new FastEnds( zmanimCalculator );
					break;

				case TzaitHacochavim:
					zmanim = new TzaitHacochavim( zmanimCalculator );
					break;

				case ShabbatEnd:
					zmanim = new ShabbatEnds( zmanimCalculator );
					break;

				case NightChatzot:
					zmanim = new NightChatzot( zmanimCalculator );
			}

			typeToZmanimMap.put( type, zmanim );
			return zmanim;
		}
	}
	
}  // End of ZmanimFactory class
