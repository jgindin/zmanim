/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar;

import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Holidays;
import com.gindin.zmanlib.calendar.holiday.Observance;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;
import com.gindin.zmanlib.zman.ZmanimList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A HebrewDate represents a specific date obtained from a HebrewCalendar. Each instance is immutable once created.
 */
@SuppressWarnings( { "UnusedDeclaration", "ClassWithTooManyFields", "ClassWithTooManyMethods" } )
public class HebrewDate
  implements Comparable<HebrewDate> {

  private final int                                         hebrewYear;

  @HebrewMonth.Constant
  private final int                                         hebrewMonth;

  private final int                                         hebrewDay;

  /** The Gregorian year */
  private final int                                         gregorianYear;

  /**
   * The month, in a format compatible with java.util.Calendar's definition of month zero being January, through to
   *  month 11 being December.
   */
  @CalendarMonth
  private final int                                         gregorianMonth;

  /** The day of the Gregorian month */
  private final int                                         gregorianDayOfMonth;

  /** 1 == Sunday, 2 == Monday, etc... */
  @CalendarDay
  private final int                                         dayOfWeek;

  /** Internal representation of the date. */
  private final int                                         gregorianAbsDate;

  private int                                               moladHours;
  private int                                               moladMinutes;
  private int                                               moladChalakim;


  /** Cache the details lookup. */
  private Details                                           dateDetails;

  /** Cache the zmanim which are applicable to today. */
  private Map<ZmanimLocation, ZmanimList>                   locationToZmanim;


  HebrewDate(
    int                       hebrewYear,
    @HebrewMonth.Constant int hebrewMonth,
    int                       hebrewDay,
    int                       gregorianYear,
    @CalendarMonth int        gregorianMonth,
    int                       gregorianDayOfMonth,
    @CalendarDay int          dayOfWeek,
    int                       gregorianAbsDate
  ) {
    this( hebrewYear, hebrewMonth, hebrewDay, gregorianYear, gregorianMonth, gregorianDayOfMonth, dayOfWeek, gregorianAbsDate, 0, 0, 0 );
  }


  HebrewDate(
    int hebrewYear,
    @HebrewMonth.Constant int hebrewMonth,
    int                       hebrewDay,
    int                       gregorianYear,
    @CalendarMonth int        gregorianMonth,
    int                       gregorianDayOfMonth,
    @CalendarDay int          dayOfWeek,
    int                       gregorianAbsDate,
    int                       moladHours,
    int                       moladMinutes,
    int                       moladChalakim
  ) {

    this.hebrewYear = hebrewYear;
    this.hebrewMonth = hebrewMonth;
    this.hebrewDay = hebrewDay;
    this.gregorianYear = gregorianYear;
    this.gregorianMonth = gregorianMonth;
    this.gregorianDayOfMonth = gregorianDayOfMonth;
    this.dayOfWeek = dayOfWeek;
    this.gregorianAbsDate = gregorianAbsDate;
    this.moladHours = moladHours;
    this.moladMinutes = moladMinutes;
    this.moladChalakim = moladChalakim;
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Find out about this date
  public String getDescription() {
    return getDetails().getDescription();
  }


  @SuppressWarnings( "FeatureEnvy" )
  public Details getDetails() {

    if ( null != dateDetails ) {
      return dateDetails;
    }

    // Create a NullDetails here...it avoids a bunch of null checking...
    dateDetails = new NullDetails( this );

    Details shabbat = Holidays.SHABBAT.getDetailsFor( this );
    if ( null != shabbat ) {
      dateDetails = dateDetails.composeWith( shabbat );
    }

    Details roshChodeshDetails = Holidays.ROSH_CHODESH.getDetailsFor( this );
    if ( null != roshChodeshDetails ) {
      dateDetails = dateDetails.composeWith( roshChodeshDetails );
    }


    Details holidayDetails;
    for ( Observance holiday : Holidays.MAJOR_HOLIDAYS ) {
      holidayDetails = holiday.getDetailsFor( this );
      if ( null != holidayDetails ) {
        dateDetails = dateDetails.composeWith( holidayDetails );
      }
    }

    for ( Observance holiday : Holidays.MINOR_HOLIDAYS ) {
      holidayDetails = holiday.getDetailsFor( this );
      if ( null != holidayDetails ) {
        dateDetails = dateDetails.composeWith( holidayDetails );
      }
    }

    for ( Observance fastDay : Holidays.FAST_DAYS ) {
      holidayDetails = fastDay.getDetailsFor( this );
      if ( null != holidayDetails ) {
        dateDetails = dateDetails.composeWith( holidayDetails );
      }
    }

    for ( Observance modernHoliday : Holidays.MODERN_HOLIDAYS ) {
      holidayDetails = modernHoliday.getDetailsFor( this );
      if ( null != holidayDetails ) {
        dateDetails = dateDetails.composeWith( holidayDetails );
      }
    }

    for ( Observance observance : Holidays.OTHER ) {
      holidayDetails = observance.getDetailsFor( this );
      if ( null != holidayDetails ) {
        dateDetails = dateDetails.composeWith( holidayDetails );
      }
    }


    return dateDetails;
  }


  public ZmanimList getZmanim(
    ZmanimLocation location
  ) {

    if ( null == locationToZmanim ) {
      locationToZmanim = new HashMap<>();
    }

    ZmanimList zmanimList = locationToZmanim.get( location );
    if ( null != zmanimList ) {
      return zmanimList;
    }

    Details details = getDetails();
    zmanimList = new ZmanimList();

    for ( Zmanim.Type type : details.getZmanim() ) {
      zmanimList.add( ZmanimFactory.get( location, this, type ) );
    }

    locationToZmanim.put( location, zmanimList );

    return zmanimList;
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Queries about this date.

  /**
   * @return the Hebrew year (e.g., 5772).
   */
  public int getHebrewYear() {
    return hebrewYear;
  }


  /**
   * @return  the Hebrew month 1-12 (or 13 years in a leap year). The month count starts with 1 for Nisan and goes to 13
   * for Adar II
   */
  @HebrewMonth.Constant
  public int getHebrewMonth() {
    return hebrewMonth;
  }


  /**
   * @return the Hebrew day of month.
   */
  public int getHebrewDayOfMonth() {
    return hebrewDay;
  }


  /**
   * @return the day of the week as a number between 1-7. Strictly compatible with Calendar.SUNDAY, etc...
   */
  @CalendarDay
  public int getDayOfWeek() {
    return dayOfWeek;
  }


  /**
   * @return the gregorian year (e.g., 2012).
   */
  public int getGregorianYear() {
    return gregorianYear;
  }


  /**
   * @return A month which can be directly compared to the months as defined in java.util.Calendar.
   */
  @CalendarMonth
  public int getGregorianMonth() {
    return gregorianMonth;
  }


  public int getGregorianDayOfMonth() {
    return gregorianDayOfMonth;
  }


  int getGregorianAbsDate() {
    return gregorianAbsDate;
  }


  public boolean isGregorianLeapYear() {

    // If the year isn't divisible by 4, it cannot be a leap year.
    // But, if it is divisible by 100, it is NOT a leap year.
    // But....if ti is divisible by 400, then it IS a leap year.

    if ( gregorianYear % 4 != 0 ) {
      return false;
    }

    //noinspection MagicNumber
    if ( gregorianYear % 400 == 0 ) {
      return true;
    }

    if ( gregorianYear % 100 == 0 ) {
      return false;
    }

    return true;
  }


  /**
   * Returns this object's date as a java.util.Date object.
   *
   * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
   *
   * @return The <code>Date</code>
   */
  public Date getTime() {
    Calendar cal = Calendar.getInstance();
    syncCalendar( cal );
    return cal.getTime();
  }


  /**
   * Get a calendar which matches the Gregorian date represented by this Hebrew date.
   *
   * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
   */
  public Calendar getCalendar() {
    Calendar cal = Calendar.getInstance();
    syncCalendar( cal );

    return cal;
  }


  public boolean isToday() {

    HebrewDate today = HebrewCalendar.today();
    return equals( today );
  }


  /**
   * Check if the specified time, given the specified location, is after the time of the specified zman.
   *
   * @param currentTime The time to check against.
   * @param type The zman we're checking.
   * @param location Where in the world to check. Should not be null, but in case it is, this
   *  method assumes that we are NOT after the zman.
   *
   * @return true if this HebrewDate represents a time after the zman for the current day.
   */
  public boolean isAfter(
    Calendar                  currentTime,
    Zmanim.Type               type,
    @Nullable ZmanimLocation  location
  ) {

    if ( location == null ) {
      return false;
    }

    HebrewDate currentDate = HebrewCalendar.getForDate( currentTime );
    if ( 0 != currentDate.compareTo( this ) ) {
      return false;
    }

    final Zmanim zmanim = getZmanim( location ).findType( type );
    if ( zmanim == null ) {
      return false;
    }

    final Calendar zmanTime = zmanim.getDefault().getTime();
    if ( zmanTime == null ) {
      // If the zman doesn't have a time then the current time cannot be after the time of the zman.
      return false;
    }

    return currentTime.getTime().after( zmanTime.getTime() );
  }


  /**
   * Check if the current SYSTEM time, given the specified location, is after sunset.
   *
   * @param location Where in the world to check. Should not be null, but in case it is, this method assumes that
   *  we are NOT after sunset.
   *
   * @return true if this HebrewDate represents a time after sunset for the current day.
   */
  public boolean isAfterSunset(
    @Nullable ZmanimLocation  location
  ) {

    return isAfter( getCalendar(), Zmanim.Type.Shkia, location );
  }


  /**
   * Updates the specified calendar to match the Gregorian date represented by this Hebrew date.
   *
   * <b>Note</b>: The hour, minute, etc... will be set according to the time at which this method is invoked.
   */
  public void syncCalendar( Calendar calendar ) {

    calendar.setTimeInMillis( System.currentTimeMillis() );
    calendar.set( gregorianYear, gregorianMonth, gregorianDayOfMonth );
  }


  /**
   * Returns if the year the calendar is set to is a Hebrew leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year
   * cycle are leap years.
   *
   * @return true if it is a leap year
   */
  public boolean isHebrewLeapYear() {
    return HebrewCalendar.isHebrewLeapYear( getHebrewYear() );
  }

  
  /**
   * Returns the number of days for the current year that the calendar is set to.
   *
   * @see #isCheshvanLong()
   * @see #isKislevShort()
   * @see #isHebrewLeapYear
   */
  public int getDaysInHebrewYear() {
    return HebrewCalendar.getElapsedDays( getHebrewYear() + 1 ) - HebrewCalendar.getElapsedDays( getHebrewYear() );
  }


  /**
   * @return true if Cheshvan is long (29 or 30 days) for the current year that the calendar is set to
   */
  public boolean isCheshvanLong() {
    return HebrewCalendar.isCheshvanLong( getHebrewYear() );
  }


  /**
   * @return true if Kislev is short (29 or 30 days) for the year that this class is set to
   */
  public boolean isKislevShort() {
    return HebrewCalendar.isKislevShort( getHebrewYear() );
  }


  /**
   * Returns the Cheshvan and Kislev kviah (whether a Hebrew year is short, regular or long). It will return {@link
   * HebrewYearType#SHALEM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#QESIDRAH} if Cheshvan is 29
   * days and Kislev is 30 days and {@link HebrewYearType#HASER} if both are 29 days.
   *
   * @return {@link HebrewYearType#SHALEM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#QESIDRAH} if
   *         Cheshvan is 29 days and Kislev is 30 days and {@link HebrewYearType#HASER} if both are 29 days.
   *
   * @see #isCheshvanLong()
   * @see #isKislevShort()
   */
  public HebrewYearType getYearType() {
    return HebrewCalendar.getYearType( getHebrewYear() );
  }


  /**
   * @return the number of days for a given Heberw month that the calendar is currently set to.
   */
  public int getDaysInHebrewMonth() {
    return HebrewCalendar.getDaysInHebrewMonth( hebrewMonth, hebrewYear );
  }
  

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Molad info

  /**
   * Returns the molad for the year and month of the specified HebrewDate. Returns a HebrewDate set to the date of
   * the molad with the molad fields properly set.
   *
   * In the current implementation, it sets the molad time based on a midnight date rollover. This means that Rosh
   * Chodesh Adar II, 5771 with a molad of 7 chalakim past midnight on Shabbos 29 Adar I / March 5, 2011 12:00 AM and 7
   * chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
   */
  public HebrewDate getMolad() {

    HebrewDate moladDate = HebrewCalendar.getForMolad( HebrewCalendar.getChalakimSinceMoladTohu( hebrewYear, hebrewMonth ) );

    if ( moladDate.getMoladHours() >= 6 ) {
      moladDate = moladDate.tomorrow();
    }
    
    moladDate.moladHours = ( moladDate.getMoladHours() + HebrewCalendar.CHALAKIM_PER_MINUTE ) % HebrewCalendar.HOURS_PER_DAY;
    return moladDate;
  }

  
  
  /**
   * Returns the molad hours. Only a HebrewDate created via the getMolad() method will have this field set. Other
   *  HeberwDates will have this field set to 0.
   *
   * @return the molad hours
   */
  public int getMoladHours() {
    return moladHours;
  }


  /**
   * Returns the molad minutes. Only a HebrewDate created via the getMolad() method will have this field set. Other
   *  HebrewDates will have this field set to 0.
   *
   * @return the molad minutes
   */
  public int getMoladMinutes() {
    return moladMinutes;
  }


  /**
   * Returns the molad chalakim/parts. Only a HebrewDate created via the getMolad() method will have this field set. Other
   *  HebrewDates will have this field set to 0.
   *
   * @return the molad chalakim/parts
   */
  public int getMoladChalakim() {
    return moladChalakim;
  }

  
  /**
   * Returns the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu to the year and
   * month passed in.
   *
   * @return the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu
   */
  public long getChalakimSinceMoladTohu() {
    return HebrewCalendar.getChalakimSinceMoladTohu( hebrewYear, hebrewMonth );
  }


  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Date navigation

  public HebrewDate tomorrow() {
    return HebrewCalendar.forward( this );
  }

  public HebrewDate forward( int numDays ) {
    return HebrewCalendar.forward( this, numDays );
  }


  public HebrewDate yesterday() {
    return HebrewCalendar.back( this );
  }

  public HebrewDate back( int numDays ) {
    return HebrewCalendar.back( this, numDays );
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Base class overrides
  
  @Override
  public int compareTo( @NotNull HebrewDate otherDate ) {

    // We use gregorian because with the Hebrew months, because Nisan is the first month, it gets more complex.
    //  E.g., Nisan 1, 5772 is AFTER Adar 29, 5772, but in the HebrewMonth definitions, NISAN < ADAR...

    if ( gregorianYear < otherDate.gregorianYear ) {
      return -1;
    }
    else if ( gregorianYear > otherDate.gregorianYear ) {
      return 1;
    }
    
    if ( gregorianMonth < otherDate.gregorianMonth ) {
      return -1;
    }
    else if ( gregorianMonth > otherDate.gregorianMonth ) {
      return 1;
    }
    
    if ( gregorianDayOfMonth < otherDate.gregorianDayOfMonth ) {
      return -1;
    }
    else if ( gregorianDayOfMonth > otherDate.gregorianDayOfMonth ) {
      return 1;
    }
    
    return 0;
  }


  @SuppressWarnings( "RedundantIfStatement" )
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( !( o instanceof HebrewDate ) ) {
      return false;
    }

    HebrewDate that = (HebrewDate)o;

    if ( hebrewDay != that.hebrewDay ) {
      return false;
    }
    if ( hebrewMonth != that.hebrewMonth ) {
      return false;
    }
    if ( hebrewYear != that.hebrewYear ) {
      return false;
    }

    return true;
  }


  @Override
  public int hashCode() {
    int result = hebrewMonth;
    result = 31 * result + hebrewYear;
    result = 31 * result + hebrewDay;
    return result;
  }


  @Override
  public String toString() {
    return "HebrewDate{" +
      "hebrewYear=" + hebrewYear +
      ", hebrewMonth=" + hebrewMonth +
      ", hebrewDay=" + hebrewDay +
      ", gregorianYear=" + gregorianYear +
      ", gregorianMonth=" + gregorianMonth +
      ", gregorianDayOfMonth=" + gregorianDayOfMonth +
      ", dateDetails=" + dateDetails +
      '}';
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //

  private static class NullDetails
    extends Details {

    NullDetails( HebrewDate date ) {
      super( date );
    }

    @Override
    public boolean isErev() {
      return false;
    }

    @Override
    public boolean lightCandles() {
      return false;
    }

    @Override
    public boolean sayHavdalah() {
      return false;
    }

    @Override
    public String getDescription() {
      return "";
    }


    @Override
    public boolean isAnything() {
      return false;
    }
  }

}  // End of HebrewDate class
