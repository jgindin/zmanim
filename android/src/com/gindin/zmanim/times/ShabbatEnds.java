/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 */
class ShabbatEnds
	extends TzaitHacochavim {


	ShabbatEnds(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.ShabbatEnd, zmanimCalendar, buildZmanim( zmanimCalendar ) );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Seattle - EB", "Based on the Rhodesli Seattle Sephardic community's tradition.", Type.ShabbatEnd ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaitEzraBessaroth();
				}
			}
		);

		zmanim.add(
			new Zman( "Seattle - SBH", "Based on the Turkish Seattle Sephardic community's tradition.", Type.ShabbatEnd ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaitSephardicBikurHolim();
				}
			}
		);

		return zmanim;
	}

}  // End of TzaitHacochavim class
