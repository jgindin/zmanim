package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.Calendar;

/**
 * Helper for figuring out if, for a given day, which variation of different tefillot we say.
 */
@SuppressWarnings( { "SpellCheckingInspection", "MagicNumber" } )
class DoWeSay {


	private final HebrewDate                                  date;

	private final TefillaType                                 tefillaType;

	private final Details                                     dateDetails;


	DoWeSay(
		final HebrewDate date,
		final TefillaType tefillaType
	) {
		this.date = date;
		this.tefillaType = tefillaType;

		this.dateDetails = date.getDetails();
	}


	@SuppressWarnings( "FeatureEnvy" )
	final boolean barechenu() {

		HebrewDate nextYear = HebrewCalendar.getForDate( date.getGregorianYear() + 1, Calendar.JANUARY, 1 );
		final boolean isLeapYear = nextYear.isGregorianLeapYear();

		HebrewDate startOfSummer = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.NISSAN, 16 );
		HebrewDate endOfSummer = HebrewCalendar.getForDate( date.getGregorianYear(), Calendar.DECEMBER, isLeapYear ? 5 : 4 );

		// Make sure we're not in the new Hebrew year, but 'old' gregorian year...
		if ( startOfSummer.getGregorianYear() != date.getGregorianYear() ) {
			startOfSummer = HebrewCalendar.getForJewishDate( date.getHebrewYear() - 1, HebrewMonth.NISSAN, 16 );
		}


		final boolean barechenu;
		if ( date.compareTo( startOfSummer ) >= 0 ) {
			// If we're saying Arvit, then we must be strictly before the end of summer. For Shachrit or Mincha,
			//  it could be that day...
			if ( tefillaType == TefillaType.Arvit ) {
				barechenu = ( date.compareTo( endOfSummer ) < 0 );
			}
			else {
				barechenu = ( date.compareTo( endOfSummer ) <= 0 );
			}
		}
		else {
			barechenu = false;
		}

		return barechenu;
	}


	@SuppressWarnings( "FeatureEnvy" )
	final boolean moridHaTal() {

		HebrewDate firstDayOfPesach = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.NISSAN, 15 );

		// Make sure we're not in the new Hebrew year, but 'old' gregorian year...
		if ( firstDayOfPesach.getGregorianYear() != date.getGregorianYear() ) {
			firstDayOfPesach = HebrewCalendar.getForJewishDate( date.getHebrewYear() - 1, HebrewMonth.NISSAN, 15 );
		}

		// If this year is before the last day of Elul, then we have to check against *next year's* shmini Atzeret...
		HebrewDate shminiAtzeret;
		HebrewDate endOfYear = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.ELUL, 29 );
		if ( date.compareTo( endOfYear ) <= 0 ) {
			shminiAtzeret = HebrewCalendar.getForJewishDate( date.getHebrewYear() + 1, HebrewMonth.TISHREI, 22 );
		}
		else {
			shminiAtzeret = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.TISHREI, 22 );
		}


		if ( ( date.compareTo( firstDayOfPesach ) > 0 ) && ( date.compareTo( shminiAtzeret ) < 0 ) ) {
			return true;
		}

		// Now, check the edge cases...
		if ( date.compareTo( firstDayOfPesach ) == 0 ) {
			return ( tefillaType != TefillaType.Shachrit );
		}

		//noinspection SimplifiableIfStatement
		if ( date.compareTo( shminiAtzeret ) == 0 ) {
			return ( tefillaType == TefillaType.Shachrit );
		}

		return false;
	}


	@SuppressWarnings( "FeatureEnvy" )
	final boolean tachanun() {

		// See pg. 43 of Zehut Yosef.

		Details details = date.getDetails();
		if ( details.isShabbat() || details.isRoshChodesh() || details.isPesach() || details.isShavuot() || details.isSukkot() ) {
			return false;
		}

		int hebrewMonth = date.getHebrewMonth();
		if ( hebrewMonth == HebrewMonth.NISSAN ) {
			return false;
		}

		if ( details.isYomHaAtzmaut() || details.isPesachSheni() || details.isLagBOmer() || details.isYomYerushalayim() ) {
			return false;
		}

		int hebrewDayOfMonth = date.getHebrewDayOfMonth();
		if ( ( hebrewMonth == HebrewMonth.SIVAN ) && ( hebrewDayOfMonth <= 12 ) ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.AV ) && ( ( hebrewDayOfMonth == 9 ) || ( hebrewDayOfMonth == 15 ) ) ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.ELUL ) && ( hebrewDayOfMonth == 29 ) ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.TISHREI ) && ( hebrewDayOfMonth >= 9 ) ) {
			return false;
		}

		if ( details.isChanukah() ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.SHEVAT ) && ( hebrewDayOfMonth == 15 ) ) {
			return false;
		}

		if ( date.isHebrewLeapYear() && ( hebrewMonth == HebrewMonth.ADAR ) && ( ( hebrewDayOfMonth == 14 ) || ( hebrewDayOfMonth == 15 ) ) ) {
			return false;
		}

		//noinspection RedundantIfStatement
		if ( details.isPurim() || details.isShushanPurim() ) {
			return false;
		}

		return true;
	}


	final boolean aseretYameiTeshuvaInsertions() {

		int hebrewMonth = date.getHebrewMonth();
		int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		return ( hebrewMonth == HebrewMonth.TISHREI ) && ( hebrewDayOfMonth >= 1 ) && ( hebrewDayOfMonth <= 10 );
	}


	final boolean roshChodeshAdditions() {
		return dateDetails.isRoshChodesh();
	}


	final boolean chanukahAdditions() {
		return dateDetails.isChanukah();
	}


	final boolean purimAdditions() {
		return dateDetails.isPurim();
	}


	final boolean taanitDayInsertions() {
		return dateDetails.isTaanit();
	}


	final boolean erevShabbatModifications() {
		return ( date.getDayOfWeek() == Calendar.FRIDAY );
	}

} // End of DoWeSay class
