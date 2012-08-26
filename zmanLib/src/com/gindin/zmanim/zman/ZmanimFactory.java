/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import net.sourceforge.zmanim.util.GeoLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle creation of zmanim.
 */
public class ZmanimFactory {

	private static final Map<GeoLocation, ZmanimFactory>      locationToFactory = new HashMap<GeoLocation, ZmanimFactory>();

	/** Cache so that we don't create the zmanim instances more than once. */
	private final Map<Zmanim.Type, Zmanim>                    typeToZmanimMap = new HashMap<Zmanim.Type, Zmanim>();

	private final ZmanimCalculator                            zmanimCalculator;



	// TODO: Problem here is that when we create a new ZmanimFactory, it's for a particular ZmanimCalculator.
	// TODO:  And, the ZmanimCalculator's super class (AstronomicalCalendar) is calling setCalendar, which initializes
	// TODO:  it to the current date. So, every time we ask for the ZmanimFactory, it's pulling out the first one
	// TODO:  that was created, and using it, but not changing the date it's associated with...
	public static ZmanimFactory getInstance( GeoLocation location ) {

		assert null != location;

		ZmanimFactory factory = locationToFactory.get( location );
		if ( null == factory ) {
			factory = new ZmanimFactory( location );
			locationToFactory.put( location, factory );
		}

		return factory;
	}

	private ZmanimFactory( GeoLocation location ) {
		this.zmanimCalculator = new ZmanimCalculator( location );
	}


	public Zmanim getOrCreate(
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
