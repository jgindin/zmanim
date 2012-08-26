/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class AlotHashachar
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "16.1 deg";


	AlotHashachar(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Alot, buildZmanim( zmanimCalculator ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	@SuppressWarnings( "FeatureEnvy" )
	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "120 Minutes", "Before sea level sunrise, based on the time to walk the distance of 5 Mil at 24 minutes a Mil.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos120();
				}
			}
		);

		zmanim.add(
			new Zman( "120 Minutes Zmaniyot", "GR'A and the Baal Hatanya, based on 1/6th of the day before sea level sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos120Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "When the sun is 16.1 degrees below the eastern geometric horizon before sunrise.", Zmanim.Type.Alot, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "18 deg", "When the sun is 18 degrees below the eastern geometric horizon before sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos18Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "19.8 deg", "When the sun is 19.8 degrees below the eastern geometric horizon before sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "26 deg", "When the sun is 26 degrees below the eastern geometric horizon before sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "60 Minutes", "Based on the time to walk 4 Mil at 15 minutes/Mil.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos60();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes", "Based on the time to walk 4 Mil at 18 minutes/Mil.", Zmanim.Type.Alot, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos72();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 72 Minutes Zmaniyot", "Based on 1/10th of the day before sea level sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos72Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "90 Minutes", "Based on the time to walk 4 Mil at 22.5 minutes a Mil.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos90();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 90 Minutes Zmaniyot", "Based on 1/8th of the day before sea level sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos90Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "Based on the time to walk the distance of 4 Mil at 24 minutes/Mil.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos96();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 96 Minutes Zmaniyot", "Based on 1/8th of the day before sea level sunrise.", Zmanim.Type.Alot ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getAlos96Zmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of AlotHashachar class
