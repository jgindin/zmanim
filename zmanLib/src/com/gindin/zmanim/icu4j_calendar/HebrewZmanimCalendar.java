/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar;

import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.icu4j_calendar.holiday.JewishDay;
import com.gindin.zmanim.zman.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Combines the functionality of a Hebrew calendar with a Zmanim calculator.
 */
@Deprecated
public class HebrewZmanimCalendar {


	/** Aggregated to expose all the Hebrew date functionality. */
	private final HebrewCalendar                              hebrewCalendar = new HebrewCalendar();

	/** Aggregated to expose the Zmanim functionality. */
	private final ZmanimCalendar                              zmanimCalendar;

	/** This is how we create the zmanim that we need. */
	private final ZmanimFactory                               zmanimFactory;

	/** Used to format all of the dates. */
	private final HebrewDateFormatter                         dateFormatter;
	
	/** Used to format all the zmnanim. */
	private final ZmanFormatter                               zmanFormatter;

	/**
	 * When the user is viewing the actual day, and it's after sunset, we'll adjust the hebrew
	 *  calendar. To do this, we've got to know when they're viewing 'today'.
	 */
	private final Calendar                                    today = Calendar.getInstance();


	/** Set of listeners who want to know when the date has been changed. */
	private final List<Listener>                              listeners = new ArrayList<Listener>();


	public HebrewZmanimCalendar(
		ZmanimLocation      location,
		HebrewDateFormatter dateFormatter,
		ZmanFormatter       zmanFormatter,
		Calendar            secularDate
	) {

		// There are cases where we don't have / aren't able to get a location. In such a case, most of
		//  this class is useless, but hey...we can still provide the date.
		if ( null != location ) {
			this.zmanimCalendar = new ZmanimCalendar( location );
		}
		else {
			zmanimCalendar = null;
		}

		this.dateFormatter = dateFormatter;
		this.zmanFormatter = zmanFormatter;
		this.zmanimFactory = new ZmanimFactory( this );

		if ( null != secularDate ) {
			syncToSecularCalendar( secularDate, false );
		}
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

		if ( null == zmanimCalendar ) {
			return dateFormatter.formatSecularDate( hebrewCalendar );
		}
		else {
			return dateFormatter.formatSecularDate( zmanimCalendar.getCalendar() );
		}
	}


	public String formatZmanTime(
		Zman zman
	) {

		String time = zman.getCachedFormattedTime();
		if ( null == time ) {
			time = zmanFormatter.formatZmanTime( zman );
			zman.setCachedFormattedTime( time );
		}

		return time;
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
	 * Moves the calendar to the day before where it is currently set.
	 *<p>
	 * Operates on the secular date.
	 */
	public void previousDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, -1 );
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

		syncToSecularCalendar( calendar, true );
	}


	private void syncToSecularCalendar(
		Calendar  calendar,
	  boolean   fireEvents
	) {

		zmanimCalendar.setCalendar( calendar );
		hebrewCalendar.setTime( calendar.getTime() );

		if ( fireEvents ) {
			for ( Listener listener : listeners ) {
				listener.dateChanged();
			}
		}

	}


	public Zmanim get( Zmanim.Type type ) {

		return zmanimFactory.getOrCreate( type );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	public String getTodaysDescription() {

		StringBuffer description = new StringBuffer();
		JewishDay day = isTodayAHoliday();
		if ( null != day ) {
			description.append( day.getDescription() );
		}

		day = isTodayRoshChodesh();
		if ( null != day ) {
			if ( 0 == description.length() ) {
				description.append( day.getDescription() );
			}
			else {
				description.append( " / " ).append( day.getDescription() );
			}
		}

		day = isTodayInTheOmer();
		if ( null != day ) {
			if ( 0 == description.length() ) {
				description.append( day.getDescription() );
			}
			else {
				description.append( " / " ).append( day.getDescription() );
			}
		}

		day = isTodayATaanit();
		if ( null != day ) {
			if ( 0 == description.length() ) {
				description.append( day.getDescription() );
			}
			else {
				description.append( " / " ).append( day.getDescription() );
			}
		}

		return description.toString();
	}


	private JewishDay isTodayAHoliday() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishDay>() {
				public JewishDay dateAdjusted( boolean adjustmentWasRequired ) {
					return hebrewCalendar.isTodayAHoliday();
				}
			}
		);
	}


	// TODO: Make private -- get this info from the Day
	public JewishDay isTodayATaanit() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishDay>() {
				public JewishDay dateAdjusted( boolean adjustmentWasRequired ) {

					if ( !adjustmentWasRequired ) {
						return hebrewCalendar.isTodayATaanit();
					}
					return null;
				}
			}
		);
	}


	private JewishDay isTodayRoshChodesh() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishDay>() {
				public JewishDay dateAdjusted( boolean adjustmentWasRequired ) {
					return hebrewCalendar.isTodayRoshChodesh();
				}
			}
		);
	}


	private JewishDay isTodayInTheOmer() {

		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<JewishDay>() {
				public JewishDay dateAdjusted( boolean adjustmentWasRequired ) {
					return hebrewCalendar.isTodayInTheOmer();
				}
			}
		);
	}


	// TODO: Make private -- get this info from the Day
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


	// TODO: Make private -- get this info from the Day
	public boolean showHavdalah() {

		//noinspection UnnecessaryUnboxing
		return adjustHebrewDateIfNecessary(
			new HebrewDateAdjustedCallback<Boolean>() {
				public Boolean dateAdjusted( boolean adjustmentWasRequired ) {
					//noinspection UnnecessaryBoxing
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

		boolean adjusted;

		adjusted = ( null != zmanimCalendar ) && isTodayAfterSunset();

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
