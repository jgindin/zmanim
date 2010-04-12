/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;
import com.gindin.zmanim.times.ZmanimFactory;
import net.sourceforge.zmanim.util.GeoLocation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Combines the functionality of a Hebrew calendar with a Zmanim calculator.
 */
public class HebrewZmanimCalendar {


	/** Aggregated to expose all the Hebrew date functionality. */
	private final HebrewCalendar                              hebrewCalendar = new HebrewCalendar();

	/** Aggregated to expose the Zmanim functionality. */
	private final ZmanimCalendar                              zmanimCalendar;

	/** Used to format all of the dates and times. */
	private final HebrewDateFormatter                         dateFormatter;

	/** This is how we create the zmanim that we need. */
	private final ZmanimFactory                               zmanimFactory;

	/**
	 * When the user is viewing the actual day, and it's after sunset, we'll adjust the hebrew
	 *  calendar. To do this, we've got to know when they're viewing 'today'.
	 */
	private final Calendar                                    today = Calendar.getInstance();


	/** Set of listeners who want to know when the date has been changed. */
	private final List<Listener>                              listeners = new ArrayList<Listener>();


	public HebrewZmanimCalendar(
		GeoLocation location,
		DateFormat  timeFormatter,
		DateFormat  secularDateFormatter
	) {

		this.zmanimCalendar = new ZmanimCalendar( location );
		this.dateFormatter = new HebrewDateFormatter( timeFormatter, secularDateFormatter );
		this.zmanimFactory = new ZmanimFactory( this );
	}


	public void addListener(
		Listener newListener
	)  {

		synchronized ( this ) {
			listeners.add( newListener );
		}
	}


	public void removeListener(
		Listener listener
	) {

		synchronized ( this  ) {
			listeners.remove( listener );
		}
	}


	public ZmanimCalendar getZmanimCalendar() {
		return zmanimCalendar;
	}


	public String formatHebrewDate() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<String>() {
				public String dateAdjusted( boolean adjustmentWasRequired ) {
					String prefix = "";

					// We want to prefix with "Or le..."
					//  * After sunset. E.g., Dec 22, 2009 --> 5 Tevet 5770, but after sunset it's now 6 Tevet
					if ( adjustmentWasRequired ) {
						prefix = "Or le ";
					}
					return prefix + dateFormatter.formatHebrewDate( hebrewCalendar );
				}
			}
		);
	}


	public String formatSecularDate() {

		return dateFormatter.formatSecularDate( zmanimCalendar.getCalendar() );
	}


	public String formatZmanTime(
		Zman zman
	) {

		return dateFormatter.formatZmanTime( zman.getTime() );
	}


	/**
	 * Moves the calendar to the next day after where it is currently set.
	 *<p>
	 * Operates on the secular date.
	 */
	public void nextDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, 1 );

		syncToSecularCalendar( calendar );
	}


	/**
	 * Moves the calendar to the next month after where it is currently set, attempting to keep
	 *  the day of the month the same. Appropriate wrapping will be applied.
	 *<p>
	 * Operates on the secular date.
	 */
	public void nextMonth() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.MONTH, 1 );
		syncToSecularCalendar( calendar );
	}


	/**
	 * Moves the calendar to the day before where it is currently set.
	 *<p>
	 * Operates on the secular date.
	 */
	public void previousDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, -1 );
		syncToSecularCalendar( calendar );
	}


	/**
	 * Moves the calendar to the month before where it is currently set, attempting to keep
	 *  the day of the month the same. Appropriate wrapping will be applied.
	 *<p>
	 * Operates on the secular date.
	 */
	public void previousMonth() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.MONTH, -1 );
		syncToSecularCalendar( calendar );
	}


	public void goToSecularDate(
		int year,
	  int month,
	  int date
	) {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.set( year, month, date );
		syncToSecularCalendar( calendar );
	}


	public Calendar getSecularDate() {

		Calendar c = Calendar.getInstance();
		c.setTime( zmanimCalendar.getCalendar().getTime() );

		return c;
	}


	/**
	 * Given the specified secular calendar, make sure our zmanim & Hebrew calendars are both reflecting that date.
	 *
	 * @param calendar the secular date to now be using.
	 */
	private void syncToSecularCalendar(
		Calendar calendar
	) {

		zmanimCalendar.setCalendar( calendar );
		hebrewCalendar.setTime( calendar.getTime() );

		for ( Listener listener : listeners ) {
			listener.dateChanged();
		}
	}


	public Zmanim get( Zmanim.Type type ) {

		return zmanimFactory.getOrCreate( type );
	}


	public String getTodaysDescription() {

		StringBuffer description = new StringBuffer();
		JewishHoliday holiday = isTodayAHoliday();
		if ( null != holiday ) {
			description.append( holiday.name );
		}

		holiday = isRoshChodesh();
		if ( null != holiday ) {
			if ( 0 == description.length() ) {
				description.append( holiday.name );
			}
			else {
				description.append( " / " ).append( holiday.name );
			}
		}

		if ( 0 == description.length() ) {
			holiday = isTodayATaanit();
			if ( null != holiday ) {
				description.append( holiday.name );
			}
		}

		return description.toString(); 
	}


	public JewishHoliday isTodayAHoliday() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishHoliday>() {
				public JewishHoliday dateAdjusted( boolean adjustmentWasRequired ) {
					return hebrewCalendar.isTodayAHoliday();
				}
			}
		);
	}


	public JewishHoliday isTodayATaanit() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishHoliday>() {
				public JewishHoliday dateAdjusted( boolean adjustmentWasRequired ) {

					if ( !adjustmentWasRequired ) {
						return hebrewCalendar.isTodayATaanit();
					}
					return null;
				}
			}
		);
	}

	public JewishHoliday isRoshChodesh() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishHoliday>() {
				public JewishHoliday dateAdjusted( boolean adjustmentWasRequired ) {
					return hebrewCalendar.isRoshChodesh();
				}
			}
		);
	}

	@SuppressWarnings( { "UnnecessaryBoxing" } )
	public boolean showCandlelighting() {

		//noinspection UnnecessaryUnboxing
		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<Boolean>() {
				public Boolean dateAdjusted( boolean adjustmentWasRequired ) {
					return Boolean.valueOf( hebrewCalendar.showCandlelighting() );
				}
			}
		).booleanValue();
	}


	@SuppressWarnings( { "UnnecessaryBoxing" } )
	public boolean showHavdalah() {

		//noinspection UnnecessaryUnboxing
		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<Boolean>() {
				public Boolean dateAdjusted( boolean adjustmentWasRequired ) {
					return Boolean.valueOf( hebrewCalendar.showHavdalah() );
				}
			}
		).booleanValue();
	}


	private boolean isSetForToday() {

		// Make sure we're using the most up-to-date time.
		today.setTimeInMillis( System.currentTimeMillis() );

		final int todaysYear = today.get( Calendar.YEAR );
		final int todaysMonth = today.get( Calendar.MONTH );
		final int todaysDate = today.get( Calendar.DAY_OF_MONTH );

		final Calendar dayViewing = zmanimCalendar.getCalendar();

		if ( todaysYear == dayViewing.get( Calendar.YEAR ) ) {
			if ( todaysMonth == dayViewing.get( Calendar.MONTH ) ) {
				if ( todaysDate == dayViewing.get( Calendar.DAY_OF_MONTH ) ) {

					return true;
				}
			}
		}

		return false;
	}


	private boolean isTodayAfterSunset() {

		if ( !isSetForToday() ) {
			return false;
		}

		// We know that there's only one entry for shkia
		final Date sunset = get( Zmanim.Type.Shkia ).iterator().next().getTime();

		return today.getTime().after( sunset );
	}


	/**
	 * Any method which needs the Hebrew date to be adjusted if the user is viewing 'today' and it's after sunset,
	 *  should use this method.
	 *
	 * @param callback After any necessary adjustments, the appropriate callback will be invoked.
	 *
	 * @return Whatever is returned from the callback.
	 */
	private <T> T adjustHebrewDateIfNecessary(
		HebrewDateAdjustedCallback<T> callback
	) {

		boolean adjusted = isTodayAfterSunset();
		if ( adjusted ) {
			hebrewCalendar.add( Calendar.DATE, 1 );
		}

		T retVal = callback.dateAdjusted( adjusted );

		if ( adjusted ) {
			hebrewCalendar.add( Calendar.DATE, -1 );
		}

		return retVal;

	}


	/**
	 * Used by the adjustHebrewDateIfNecessary() method.
	 */
	private static interface HebrewDateAdjustedCallback<T> {

		T dateAdjusted( boolean adjustmentWasRequired );
	}


	public static interface Listener {

		void dateChanged();
	}

}  // End of HebrewZmanimCalendar class
