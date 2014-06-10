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
class SofZmanShema
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "MG'A 16.1 deg";
	

	SofZmanShema(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.SofZmanShema, buildZmanim( zmanimCalculator ) );
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
			new Zman( "3 Hours before Chatzot", "Shach in the Nekudas Hakesef (Yora Deah 184), Shevus Yaakov, Chasan Sofer and others.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShma3HoursBeforeChatzos();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1 deg to Sunset", "Based on the day starting at at alot 16.1 degrees and ending at sunset.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaAlos16Point1ToSunset();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1 deg to tzait 7.083 deg", "Based on the day starting at at alot 16.1 degrees and ending at tzait 7.083 degrees.",Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaAteretTorah();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalculator.getSofZmanShmaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( "GR'A", "Based on the day as calculated from sunrise to sunset.", Zmanim.Type.SofZmanShema, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaGRA();
				}
			}
		);

		// todo: deprecated
		zmanim.add(
			new Zman( "GR'A Kol Eliyahu", "Based on Kol Eliyahu on Berachos #173 (zman kriat shema is half the time from sunrise to fixed local chatzot).",Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaKolEliyahu();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "Based on alot being 72 minutes before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "Based on alot being 120 minutes or 1/6th of the day before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Based on alot being 16.1 degrees before sunrise.", Zmanim.Type.SofZmanShema, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8 deg", "Based on alot being 19.8 degrees before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalculator.getSofZmanShmaMGA72Minutes(), "MG'A 72 Minutes", "MG'A based on alot being 72 minutes zmaniyot, or 1/10th of the day before sunrise. This time is 3 shaot zmaniyot after dawn based on the opinion of the MG'A that the day is calculated from a dawn of 72 minutes zmaniyot, or 1/10th of the day before sea level sunrise to nightfall of 72 minutes zmaniyot after sea level sunset. This returns the time of 3 * getShaahZmanis72MinutesZmanis() after dawn." );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "Based on alot being 72 minutes zmaniyot, or 1/10th of the day before sunrise.",Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "Based on alot being 90 minutes before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "Based on alot being 90 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "Based on alot being 96 minutes before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "Based on alot being 96 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanShema ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSofZmanShmaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanShema class
