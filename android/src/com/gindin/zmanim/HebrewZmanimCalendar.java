/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim;

import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Calendar;
import java.util.Date;

/**
 * Combines the functionality of a Hebrew calendar with a Zmanim calculator.
 */
public class HebrewZmanimCalendar {

	private final HebrewCalendar                              hebrewCalendar = new HebrewCalendar();

	private final ZmanimCalendar                              zmanimCalendar;


	public HebrewZmanimCalendar(
		GeoLocation location
	) {
		this.zmanimCalendar = new ZmanimCalendar( location );
	}


	/**
	 * ONLY intended for use by internal classes. Modifying the state of this calendar in any way
	 *  will seriously compromise this class' ability to function properly.
	 *
	 * @return The current calendar in use.
	 */
	HebrewCalendar getInternalCalendar() {
		return hebrewCalendar;
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


	public void goToDate( Date newDate ) {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.setTime( newDate );
		zmanimCalendar.setCalendar( calendar );

		hebrewCalendar.setTime( newDate );
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

	public JewishHoliday isTodayAHoliday() {
		return hebrewCalendar.isTodayAHoliday();
	}

	public JewishHoliday isRoshChodesh() {
		return hebrewCalendar.isRoshChodesh();
	}

	public boolean showCandlelighting() {
		return hebrewCalendar.showCandlelighting();
	}

	public boolean showHavdalah() {
		return hebrewCalendar.showHavdalah();
	}

}  // End of HebrewZmanimCalendar class
