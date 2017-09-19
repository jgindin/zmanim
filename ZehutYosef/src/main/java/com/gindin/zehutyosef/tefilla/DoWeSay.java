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

    HebrewDate firstDayOfPesach;
    HebrewDate shminiAtzeret;

    // First, check if we're before or after the Jewish New Year.
    //  - If we're before, then the 1st day of Pesach that we're going to check against is in the same Hebrew year,
    //    and the Shmini Atzeret we compare against is the next Hebrew year.
    //  - Otherwise, we're comparing against the previous Hebrew year's Pesach and the current Hebrew year's
    //    Shmini Atzeret.
    //
    HebrewDate endOfYear = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.ELUL, 29 );

    // Make sure we're not in the new Hebrew year, but 'old' gregorian year...
    if ( endOfYear.getGregorianYear() != date.getGregorianYear() ) {
      endOfYear = HebrewCalendar.getForJewishDate( date.getHebrewYear() - 1, HebrewMonth.ELUL, 29 );
    }


    if ( date.compareTo( endOfYear ) <= 0 ) {
      firstDayOfPesach = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.NISSAN, 15 );
      shminiAtzeret = HebrewCalendar.getForJewishDate( date.getHebrewYear() + 1, HebrewMonth.TISHREI, 22 );
    }
    else {
      firstDayOfPesach = HebrewCalendar.getForJewishDate( date.getHebrewYear() - 1, HebrewMonth.NISSAN, 15 );
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

    // There are never tachanun at night. (Well, except on Yom Kippur...)
    if ( tefillaType == TefillaType.Arvit ) {
      return false;
    }

    // See pg. 43 of Zehut Yosef.

    Details today = date.getDetails();
    if ( !today.sayTachanun() ) {
      return false;
    }

    if ( tefillaType == TefillaType.Mincha ) {
      HebrewDate tomorrowDate = date.tomorrow();
      Details tomorrow = tomorrowDate.getDetails();

      // There are cases (e.g., Erev Rosh Hashana & Erev Yom Kippu) where we don't say tachanun,
      //  but we still do say it the day before the erev...
      if ( !tomorrow.sayTachanun() ) {
        Details dayAfterTomorrow = tomorrowDate.tomorrow().getDetails();
        return dayAfterTomorrow.isRoshHashana() || dayAfterTomorrow.isYomKippur();
      }
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

  final boolean mondayThursdayAdditions() {
    return ( date.getDayOfWeek() == Calendar.MONDAY || date.getDayOfWeek() == Calendar.THURSDAY );
  }

  final int getDayOfWeek() {
    return date.getDayOfWeek();
  }

} // End of DoWeSay class
