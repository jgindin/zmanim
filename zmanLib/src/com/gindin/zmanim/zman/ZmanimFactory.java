/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle creation of zmanim.
 */
public class ZmanimFactory {

	/** Cache so that we don't create the zmanim instances more than once. */
	private final Map<Zmanim.Type, Zmanim>             typeToZmanimMap = new HashMap<Zmanim.Type, Zmanim>();

	private final ZmanimCalendar                       zmanimCalendar;


	public ZmanimFactory(
		HebrewZmanimCalendar hebrewZmanimCalendar
	) {

		this.zmanimCalendar = hebrewZmanimCalendar.getZmanimCalendar();

		HebrewZmanimCalendar.Listener listener = new HebrewZmanimCalendar.Listener() {
			public void dateChanged() {

				synchronized ( typeToZmanimMap ) {
					for ( Zmanim zmanim : typeToZmanimMap.values() ) {
						zmanim.clearCaches();
					}
				}
			}
		};

		hebrewZmanimCalendar.addListener( listener );
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
					zmanim = new AlotHashachar( zmanimCalendar );
					break;

				case Talit:
					zmanim = new Talit( zmanimCalendar );
					break;

				case Hanetz:
					zmanim = new Hanetz( zmanimCalendar );
					break;

				case SofZmanShema:
					zmanim = new SofZmanShema( zmanimCalendar );
					break;

				case SofZmanTefila:
					zmanim = new SofZmanTefila( zmanimCalendar );
					break;

				case Chatzot:
					zmanim = new Chatzot( zmanimCalendar );
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

				case CandleLighting:
					zmanim = new CandleLighting( zmanimCalendar );
					break;

				case Shkia:
					zmanim = new Shkia( zmanimCalendar );
					break;

				case BeinHashmashot:
					zmanim = new BeinHashmashot( zmanimCalendar );
					break;

				case LatestMincha:
					zmanim = new LatestMincha( zmanimCalendar );
					break;

				case FastEnd:
					zmanim = new FastEnds( zmanimCalendar );
					break;

				case TzaitHacochavim:
					zmanim = new TzaitHacochavim( zmanimCalendar );
					break;

				case ShabbatEnd:
					zmanim = new ShabbatEnds( zmanimCalendar );
					break;

				case NightChatzot:
					zmanim = new NightChatzot( zmanimCalendar );
			}

			typeToZmanimMap.put( type, zmanim );
			return zmanim;
		}
	}
	
}  // End of ZmanimFactory class
