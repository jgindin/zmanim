/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim;

import net.sourceforge.zmanim.util.GeoLocation;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Combines the functionality of a Hebrew calendar with a Zmanim calculator.
 */
public class HebrewZmanimCalendar {

	/**
	 * Aggregated to expose all the Hebrew date functionality.
	 */
	private final HebrewCalendar                              hebrewCalendar = new HebrewCalendar();

	/**
	 * Aggregated to expose the Zmanim functionality.
	 */
	private final ZmanimCalendar                              zmanimCalendar;

	/** Used to format all of the dates and times. */
	private final HebrewDateFormatter                         dateFormatter;


	/**
	 * When the user is viewing the actual day, and it's after sunset, we'll adjust the hebrew
	 *  calendar. To do this, we've got to know when they're viewing 'today'.
	 */
	private final Calendar                                    today = Calendar.getInstance();



	public HebrewZmanimCalendar(
		GeoLocation location,
		DateFormat  timeFormatter,
		DateFormat  secularDateFormatter
	) {

		this.zmanimCalendar = new ZmanimCalendar( location );
		this.dateFormatter = new HebrewDateFormatter( timeFormatter, secularDateFormatter );

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
		Date zman
	) {

		return dateFormatter.formatZmanTime( zman );
	}


	/**
	 * Moves the calendar to the next day after where it is currently set.
	 */
	public void nextDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, 1 );
		zmanimCalendar.setCalendar( calendar );

		hebrewCalendar.add( Calendar.DATE, 1 );
	}


	/**
	 * Moves the calendar to the next month after where it is currently set, attempting to keep
	 *  the day of the month the same. Appropriate wrapping will be applied.
	 */
	public void nextMonth() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.MONTH, 1 );
		zmanimCalendar.setCalendar( calendar );

		hebrewCalendar.add( Calendar.MONTH, 1 );
	}


	/**
	 * Moves the calendar to the day before where it is currently set.
	 */
	public void previousDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, -1 );
		zmanimCalendar.setCalendar( calendar );

		hebrewCalendar.add( Calendar.DATE, -1 );
	}


	/**
	 * Moves the calendar to the month before where it is currently set, attempting to keep
	 *  the day of the month the same. Appropriate wrapping will be applied.
	 */
	public void previousMonth() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.MONTH, -1 );
		zmanimCalendar.setCalendar( calendar );

		hebrewCalendar.add( Calendar.MONTH, -1 );
	}


	public void goToSecularDate(
		int year,
	  int month,
	  int date
	) {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.set( year, month, date );
		zmanimCalendar.setCalendar( calendar );

		hebrewCalendar.setTime( calendar.getTime() );
	}


	public Calendar getSecularDate() {

		Calendar c = Calendar.getInstance();
		c.setTime( zmanimCalendar.getCalendar().getTime() );

		return c;
	}


	public Date getAlot() {
		return zmanimCalendar.getAlos16Point1Degrees();
	}

	public Date getTalit() {
		return zmanimCalendar.getMisheyakir11Point5Degrees();
	}

	public Date getHanetz() {
		return zmanimCalendar.getSunrise();
	}

	public Date getSofZmanShma() {
		return zmanimCalendar.getSofZmanShmaMGA();
	}

	public Date getSofZmanTefila() {
		return zmanimCalendar.getSofZmanTfilaMGA();
	}

	public Date getChatzot() {
		return zmanimCalendar.getChatzos();
	}

	public Date getMinchaGedola() {
		return zmanimCalendar.getMinchaGedola();
	}

	public Date getMinchaKetana() {
		return zmanimCalendar.getMinchaKetana();
	}

	public Date getPlagHamincha() {
		return zmanimCalendar.getPlagHamincha();
	}

	public Date getCandlelighting() {
		return zmanimCalendar.getCandelLighting();
	}

	public Date getShkiah() {
		return zmanimCalendar.getSunset();
	}

	public Date getLatestMincha() {
		return zmanimCalendar.getLatestMinchaOvadiaYosef();
	}

	public Date getTzaitHachocavim() {
		return zmanimCalendar.getTzais();
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

		final Date sunset = getShkiah();

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

}  // End of HebrewZmanimCalendar class
