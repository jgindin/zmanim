/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;

import java.util.Calendar;
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
		final Calendar                mustBeAfter,
		final PreferredZmanTypeLookup preferredZmanTypeLookup
	) {

		Zman nextZman = null;

		// It seems like there may be some location where night Chatzot is before midnight. In that case
		//  we won't find the next zman...
		// There *is* an issue where we're asked to find the next zman after, say midnight. Problem is that
		//  we miss night Chatzot, because that's on the previous day.
		HebrewDate hebrewDate = HebrewCalendar.getForDate( mustBeAfter );
		List<Zmanim> daysZmanim = hebrewDate.getZmanim( location );
		for ( Zmanim zmanim : daysZmanim ) {
			String zmanName = preferredZmanTypeLookup.getPreferredZman( zmanim.type );
			Zman zman;
			if ( zmanName == null ) {
				zman = zmanim.getDefault();
			}
			else {
				zman = zmanim.getByName( zmanName );
			}

			Calendar time = zman.getTime();
			if ( time.after( mustBeAfter ) ) {
				// We found the next zman!
				nextZman = zman;
				break;
			}
		}

		return nextZman;
	}


	/**
	 * Calculate the number of milliseconds between the given date and the zman.
	 */
	public static long howLongUntil(
		final Calendar  now,
		final Zman      nextZman
	) {

		long nowTime = now.getTime().getTime();
		long zmanTime = nextZman.getTime().getTime().getTime();

		if ( nowTime > zmanTime ) {
			return 0;
		}

		return zmanTime - nowTime;
	}


	private ZmanUtils() {
	}



} // End of ZmanUtils class
