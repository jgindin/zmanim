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
public class SofZmanBiurChametz
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "GR'A";


	SofZmanBiurChametz(
		final ZmanimCalculator zmanimCalculator
	) {
		super( Type.SofZmanBiurChametz, buildZmanim( zmanimCalculator ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman> zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Based on the day as calculated from sunrise to sunset.", Type.SofZmanBiurChametz ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanBiurChametzGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "Based on alot being 72 minutes before sunrise.", Type.SofZmanBiurChametz ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanBiurChametzMGA72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Based on alot being 16.1 degrees before sunrise.", Type.SofZmanBiurChametz ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanBiurChametzMGA16Point1Degrees();
				}
			}
		);

		return zmanim;
	}


} // End of SofZmanBiurChametz class
