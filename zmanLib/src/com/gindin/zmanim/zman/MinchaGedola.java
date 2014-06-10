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
class MinchaGedola
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "GR'A";


	MinchaGedola(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.MinchaGedola, buildZmanim( zmanimCalculator ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.", Zmanim.Type.MinchaGedola, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaGedola();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Magen Avraham, based on the day starting and ending 16.1 degrees below the horizon.", Zmanim.Type.MinchaGedola, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaGedola16Point1Degrees();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalculator.getMinchaGedola30Minutes(), "30 minutes after chatzot", "30 minutes after chatzos and not 1/2 of a shaah zmanis after chatzos as calculated by getMinchaGedola." );

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "Magen Avraham, based onthe day starting 72 minutes before sunrise and ending 72 minutes after sunset.", Zmanim.Type.MinchaGedola ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaGedola72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.MinchaGedola ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaGedolaAteretTorah();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalculator.getMinchaGedolaGreaterThan30(), "", "" );

		return zmanim;
	}

}  // End of MinchaGedola class
