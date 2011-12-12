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
class SofZmanTefila
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "GR'A";
	

	SofZmanTefila(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.SofZmanTefila, buildZmanim( zmanimCalendar ) );
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
			new Zman( "2 Hours before Chatzot", "2 hours before chatzot.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfila2HoursBeforeChatzos();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Based on the day as calculated from sunrise to sunset.", Zmanim.Type.SofZmanTefila, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting at 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilahAteretTorah();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "Based on alot being 72 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "Based on alot being 120 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Based on alot being 16.1 degrees before sunrise.", Zmanim.Type.SofZmanTefila, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8 deg", "Based on alot being 19.8 degrees before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaMGA72Minutes(), "", "" );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "Based on alot being 72 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "Based on alot being 90 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "Based on alot being 90 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes", "Based on alot being 96 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes Zmaniyot", "Based on alot being 96 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanTefila class
