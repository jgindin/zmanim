/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;

import java.util.Date;
import java.util.List;

/**
 * Utilities for working with zmanim.
 */
public class ZmanUtils {

	public interface PreferredZmanTypeLookup {

		/**
		 * For the given type, what is the name of the zman the user prefers to use?
		 *
		 * E.g,. for the Zmanim.Type.ShabbatEnd the user may prefer "Seattle Sephardic".
		 *
		 */
		String getPreferredZman( Zmanim.Type type );
	}


	public static Zman findNextZman(
		final ZmanimLocation          location,
		final Date                    notAfterTime,
		final PreferredZmanTypeLookup preferredZmanTypeLookup
	) {

		Zman nextZman = null;

		HebrewDate today = HebrewCalendar.today();
		List<Zmanim> todaysZmanim = today.getZmanim( location );
		for ( Zmanim zmanim : todaysZmanim ) {
			String zmanName = preferredZmanTypeLookup.getPreferredZman( zmanim.type );
			Zman zman;
			if ( zmanName == null ) {
				zman = zmanim.getDefault();
			}
			else {
				zman = zmanim.getByName( zmanName );
			}

			Date time = zman.getTime().getTime();
			if ( time.after( notAfterTime ) ) {
				// We found the next zman!
				nextZman = zman;
				break;
			}
		}

		return nextZman;
	}


	@SuppressWarnings( "FeatureEnvy" )
	public static Zman findNextZman(
		final ZmanimLocation          location,
	  final Zman                    currentZman,
		final PreferredZmanTypeLookup preferredZmanTypeLookup
	) {

		int nextZmanType = currentZman.type.ordinal() + 1;
		Zmanim nextZmanim;

		HebrewDate today = HebrewCalendar.today();
		List<Zmanim> todaysZmanim = today.getZmanim( location );
		if ( nextZmanType < todaysZmanim.size() ) {
			nextZmanim = todaysZmanim.get( nextZmanType );
		}
		else {
			todaysZmanim = today.tomorrow().getZmanim( location );
			nextZmanim = todaysZmanim.get( 0 );
		}

		Zman nextZman;
		String zmanName = preferredZmanTypeLookup.getPreferredZman( nextZmanim.type );
		if ( zmanName == null ) {
			nextZman = nextZmanim.getDefault();
		}
		else {
			nextZman = nextZmanim.getByName( zmanName );
		}

		return nextZman;
	}



	/**
	 * Calculate the number of milliseconds between the given date and the zman.
	 */
	public static long howLongUntil(
		final Date now,
		final Zman nextZman
	) {

		long nowTime = now.getTime();
		long zmanTime = nextZman.getTime().getTime().getTime();

		if ( nowTime > zmanTime ) {
			return 0;
		}

		return zmanTime - nowTime;
	}


	private ZmanUtils() {
	}



} // End of ZmanUtils class
