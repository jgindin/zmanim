/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class Talit
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "10.2 deg";


	Talit(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Talit, zmanimCalculator );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	@Override
	protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Based on the position of the sun when it is 10.2 degrees below geometric zenith.", Zmanim.Type.Talit, false )  {
				@Override protected Date calculate() {
					return zmanimCalculator.getMisheyakir10Point2Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "11 deg", "Based on the position of the sun when it is 11 degrees below geometric zenith.", Zmanim.Type.Talit, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMisheyakir11Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "11.5 deg", "Based on the position of the sun when it is 11.5 degrees below geometric zenith.", Zmanim.Type.Talit ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMisheyakir11Point5Degrees();
				}
			}
		);

		return zmanim;
	}

}  // End of Talit class
