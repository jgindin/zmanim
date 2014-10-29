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
class MinchaKetana
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "GR'A";


	MinchaKetana(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.MinchaKetana, zmanimCalculator );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	@Override
	protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.", Zmanim.Type.MinchaKetana, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaKetana();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Based on the day starting and ending 16.1 degrees below the horizon.", Zmanim.Type.MinchaKetana, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaKetana16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "Based on the day starting 72 minutes before sunrise and ending 72 minutes after sunset.", Zmanim.Type.MinchaKetana ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaKetana72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.MinchaKetana ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getMinchaKetanaAteretTorah();
				}
			}
		);

		return zmanim;
	}
}  // End of MinchaKetana class
