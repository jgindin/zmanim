/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import net.sourceforge.zmanim.AstronomicalCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
class TzaitHacochavim
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "8.5 deg";


	TzaitHacochavim(
		final ZmanimCalculator zmanimCalculator
	) {

		this( Type.TzaitHacochavim, zmanimCalculator );
	}


	TzaitHacochavim(
		final Type type,
		final ZmanimCalculator zmanimCalculator
	) {
		super( type, zmanimCalculator );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	@Override
	protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

		return mergeZmanimLists( buildStandardTzaitZmanim( Type.TzaitHacochavim, zmanimCalculator ),
			buildExtraTzaitZmanim( Type.TzaitHacochavim, zmanimCalculator ) );
	}


	protected static List<Zman> mergeZmanimLists(
		List<Zman> firstList,
		List<Zman> secondList
	) {

		if ( null != secondList ) {
			firstList.addAll( secondList );
		}

		return firstList;
	}


	@SuppressWarnings( { "FeatureEnvy", "MagicNumber" } )
	protected static List<Zman> buildStandardTzaitZmanim(
		final Type              zmanType,
		final ZmanimCalculator  zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();


		zmanim.add(
			new Zman( "MG'A 120 Minutes", "Based on the time to walk a Mil in 24 minutes, for a total of 120 minutes based on the opinion of Ula who calculated tzait as 5 Mil after sea level shkiah.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais120() );
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 120 Minutes Zmaniyot", "120 minutes zmaniyot after sea level sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais120Zmanis() );
				}
			}
		);

		zmanim.add(
			new Zman( "3.5 deg", "When the sun is 3.5 degrees below the western geometric horizon after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator,
						zmanimCalculator.getSunsetOffsetByDegrees( AstronomicalCalendar.GEOMETRIC_ZENITH + 3.5 ) );
				}
			}
		);

		zmanim.add(
			new Zman( "6.5 deg", "When the sun is 6.5 degrees below the western geometric horizon after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator,
						zmanimCalculator.getSunsetOffsetByDegrees( AstronomicalCalendar.GEOMETRIC_ZENITH + 6.5 ) );
				}
			}
		);

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "When the sun is 8.5 degrees below the western geometric horizon after sunset.", zmanType, false ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais() );
				}
			}
		);

		zmanim.add(
			new Zman( "16.1 deg", "When the sun is 16.1 degrees below the western geometric horizon after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais16Point1Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "18 deg", "When the sun is 18 degrees below the western geometric horizon after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais18Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "19.8 deg", "When the sun is 19.8 degrees below the western geometric horizon after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais19Point8Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "26 deg", "When the sun is 26 degrees below the western geometric horizon after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais26Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Chavas Yair and Divray Malkiel", "Based on the time to walk 4 Mil at 15 minutes/Mil.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais60() );
				}
			}
		);

		zmanim.add(
			new Zman( "Rambam / Rabainu Tam", "Based on the time to walk 4 Mil at 18 minutes/Mil.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais72() );
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes Zmaniyot", "Calculated as 72 minutes zmaniyot, or 1/10th of the day after sea level sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais72Zmanis() );
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "Based on the time to walk 5 Mil at 18 minutes/Mil.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais90() );
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 90 Minutes Zmaniyot", "90 minutes zmaniyot after sea level sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais90Zmanis() );
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "96 minutes after sea level sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais96() );
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 96 Minutes Zmaniyot", "96 minutes zmaniyot after sea level sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzais96Zmanis() );
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Usually calculated as 40 minutes after sunset.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisAteretTorah() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 3.65 deg", "Based on the sun's position at 3.65 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim3Point65Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 4.37 deg", "Based on the sun's position at 4.37 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim4Point37Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 4.61 deg", "Based on the sun's position at 4.61 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim4Point61Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 4.8 deg", "Based on the sun's position at 4.8 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim4Point8Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 5.88 deg", "Based on the sun's position at 5.88 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim5Point88Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 5.95 deg", "Based on the sun's position at 5.95 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim5Point95Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 7.083 deg", "Based on the sun's position at 7.083 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim7Point083Degrees() );
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 8.5 deg", "Based on the sun's position at 8.5 degrees below the western horizon.", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaisGeonim8Point5Degrees() );
				}
			}
		);

		return zmanim;
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	protected static List<Zman> buildExtraTzaitZmanim(
		final Type            zmanType,
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Seattle - SBH", "Based on the Turkish Seattle Sephardic community's tradition (42 minutes).", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaitSephardicBikurHolim() );
				}
			}
		);

		zmanim.add(
			new Zman( "Seattle - EB", "Based on the Rhodesli Seattle Sephardic community's tradition (45 minutes).", zmanType ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getTzaitEzraBessaroth() );
				}
			}
		);

		return zmanim;
	}

}  // End of TzaitHacochavim class
