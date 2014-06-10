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
class SofZmanTefila
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "GR'A";
	

	SofZmanTefila(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.SofZmanTefila, buildZmanim( zmanimCalculator ) );
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
			new Zman( "2 Hours before Chatzot", "2 hours before chatzot.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfila2HoursBeforeChatzos();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalculator.getSofZmanTfilaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Based on the day as calculated from sunrise to sunset.", Zmanim.Type.SofZmanTefila, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting at 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilahAteretTorah();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "Based on alot being 72 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "Based on alot being 120 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Based on alot being 16.1 degrees before sunrise.", Zmanim.Type.SofZmanTefila, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8 deg", "Based on alot being 19.8 degrees before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalculator.getSofZmanTfilaMGA72Minutes(), "", "" );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "Based on alot being 72 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "Based on alot being 90 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "Based on alot being 90 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes", "Based on alot being 96 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes Zmaniyot", "Based on alot being 96 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanTfilaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanTefila class
