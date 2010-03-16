/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class LatestMincha
	extends Zmanim {

	LatestMincha(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.LatestMincha, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();


		zmanim.add(
			new Zman( "According to Mishna Berura", "Daily Halacha's representation of the Mishna Berura's ruling, that mincha must be FINISHED by sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getLatestMinchaMishnaBerura();
				}
			}
		);

		zmanim.add(
			new Zman( "According to Hacham Ovadia Yosef", "Daily Halacha's representation of Chacham Ovadia Yosef's ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5 minutes after sunset.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getLatestMinchaOvadiaYosef();
				}
			}
		);

		zmanim.add(
			new Zman( "According to Hacham Ben Sion", "Daily Halacha's representation of Chacham Ben Sion Abba Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getLatestMinchaBenSion();
				}
			}
		);

		return zmanim;
	}

}  // End of LatestMincha class
