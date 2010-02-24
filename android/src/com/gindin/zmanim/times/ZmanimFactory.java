/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle creation of zmanim.
 */
public class ZmanimFactory {

	/** Cache so that we don't create the zmanim instances more than once. */
	private static final Map<Zmanim.Type, Zmanim> typeToZmanimMap = new HashMap<Zmanim.Type, Zmanim>();


	private ZmanimFactory() {
	}


	public static Zmanim getOrCreate(
		Zmanim.Type type,
		ZmanimCalendar zmanimCalendar
	) {

		Zmanim zmanim = typeToZmanimMap.get( type );
		if ( null != zmanim ) {
			return zmanim;
		}

		switch ( type ) {
			case Alot:
				zmanim = new AlotHashachar( zmanimCalendar );
				break;

			case CandleLighting:
				zmanim = new CandleLighting( zmanimCalendar );
				break;

			case Chatzot:
				zmanim = new Chatzot( zmanimCalendar );
				break;

			case Hanetz:
				zmanim = new Hanetz( zmanimCalendar );
				break;

			case LatestMincha:
				zmanim = new LatestMincha( zmanimCalendar );
				break;

			case MinchaGedola:
				zmanim = new MinchaGedola( zmanimCalendar );
				break;

			case MinchaKetana:
				zmanim = new MinchaKetana( zmanimCalendar );
				break;

			case PlagHaMincha:
				zmanim = new PlagHaMincha( zmanimCalendar );
				break;

			case Shkia:
				zmanim = new Shkia( zmanimCalendar );
				break;

			case SofZmanShema:
				zmanim = new SofZmanShema( zmanimCalendar );
				break;

			case SofZmanTefila:
				zmanim = new SofZmanTefila( zmanimCalendar );
				break;

			case Talit:
				zmanim = new Talit( zmanimCalendar );
				break;

			case TzaitHachocavim:
				zmanim = new TzaitHachocavim( zmanimCalendar );
				break;
		}

		typeToZmanimMap.put( type, zmanim );
		return zmanim;
	}
	
}  // End of ZmanimFactory class
