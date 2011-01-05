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
public class BeinHashmashot
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "2 Stars";


	public BeinHashmashot( ZmanimCalendar zmanimCalendar ) {
		super( Type.BeinHashmashot, buildZmanim( zmanimCalendar ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "13 Degrees", "Based on Rabainu Tam's calculation as the time the sun is 13° below geometric zenith (90°).", Zmanim.Type.BeinHashmashot ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getBainHasmashosRT13Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "58.5 Minutes", "Based on Rabainu Tam's calculation as a 58.5 minute offset after sunset.", Zmanim.Type.BeinHashmashot ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getBainHasmashosRT58Point5Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "13.5 Minutes", "Based on 13.5 minutes (3/4 of an 18 minute mil before shkiah calculated as 7.083°.", Zmanim.Type.BeinHashmashot ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getBainHasmashosRT13Point5MinutesBefore7Point083Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "2 Stars", "Based on Rabainu Tam's calculation in the opinion of the Divray Yosef (see Yisrael Vehazmanim) of 5/18th (27.77%) of the time between alot (calculated as 19.8° before sunrise) and sunrise.", Zmanim.Type.BeinHashmashot ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getBainHasmashosRT2Stars();
				}
			}
		);



		return zmanim;
	}

}  // End of BeinHashmashot class
