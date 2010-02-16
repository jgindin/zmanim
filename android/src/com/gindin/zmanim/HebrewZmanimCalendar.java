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
	}


	// TODO:  Need to do some replacements and cleanup:
	//  - zmaniyos --> zmaniyot
	//  - Look for references to API
	//  - MG"A --> MG'A
	//  - Magen Avraham --> MG'A
	//  - GR"A --> GR'A
	//  - Cleanup terms that may not be immediately familiar (misheyakir...)

	public Zmanim getAlot() {

		Zmanim alot = new Zmanim( Zmanim.Type.Alot );
		alot.addZman(
			new Zman( "120 Minutes", "120 minutes before sea level sunrise (no adjustment for elevation is made) based on the time to walk the distance of 5 Mil( Ula) at 24 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 5 Mil(Ula)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos120();
				}
			}
		);

		alot.addZman(
			new Zman( "120 Minutes Zmaniyot", "120 minutes zmaniyos( GR'A and the Baal Hatanya) or 1/6th of the day before sea level sunrise. This is based on a 24 minute Mil so the time for 5 Mil is 120 minutes which is 1/6th of a day (12 * 60 = 720) / 6 =120 based on the day starting at sunrise and ending at sunset. The actual calculation is getSunrise() - (getShaahZmanisGra() * 2)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos120Zmanis();
				}
			}
		);

		alot.addZman(
			new Zman( "16.1°", "When the sun is 16.1° below the eastern geometric horizon before sunrise. This calculation is based on the same calculation of 72 minutes but uses a degree based calculation instead of 72 exact minutes. This calculation is based on the position of the sun 72 minutes before sunrise in Jerusalem in the equinox which calculates to 16.1° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos16Point1Degrees();
				}
			}
		);

		alot.addZman(
			new Zman( "18°", "When the sun is 18° below the eastern geometric horizon before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos18Degrees();
				}
			}
		);

		alot.addZman(
			new Zman( "19.8°", "When the sun is 19.8° below the eastern geometric horizon before sunrise. This calculation is based on the same calculation of 90 minutes but uses a degree based calculation instead of 90 exact minutes. This calculation is based on the position of the sun 90 minutes before sunrise in Jerusalem in the equinox which calculates to 19.8° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos19Point8Degrees();
				}
			}
		);

		alot.addZman(
			new Zman( "26°", "When the sun is 26° below the eastern geometric horizon before sunrise. This calculation is based on the same calculation of 120 minutes but uses a degree based calculation instead of 120 exact minutes. This calculation is based on the position of the sun 120 minutes before sunrise in Jerusalem in the equinox which calculates to 26° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos26Degrees();
				}
			}
		);

		alot.addZman(
			new Zman( "60 Minutes", "60 minutes before sea level sunrise on the time to walk the distance of 4 Mil at 15 minutes a Mil (the opinion of the Chavas Yair. See the Divray Malkiel). This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos60();
				}
			}
		);

		alot.addZman(
			new Zman( "72 Minutes", "72 minutes before sea level sunrise (no adjustment for elevation) based on the time to walk the distance of 4 Mil at 18 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos72();
				}
			}
		);

		alot.addZman(
			new Zman( "72 Minutes Zmaniyot", "72 minutes zmaniyot ( GR'A and the Baal Hatanya) or 1/10th of the day before sea level sunrise. This is based on an 18 minute Mil so the time for 4 Mil is 72 minutes which is 1/10th of a day (12 * 60 = 720) based on the day starting at sea level sunrise and ending at sea level sunset. The actual alculation is getSeaLevelSunrise()- ( getShaahZmanisGra() * 1.2). This calculation is used in the calendars published by Hisachdus Harabanim D'Artzos Habris Ve'Kanada." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos72Zmanis();
				}
			}
		);

		alot.addZman(
			new Zman( "90 Minutes", "90 minutes before sea level sunrise on the time to walk the distance of 4 Mil at 22.5 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos90();
				}
			}
		);

		alot.addZman(
			new Zman( "90 Minutes Zmaniyot", "90 minutes zmaniyot ( GR'A and the Baal Hatanya) or 1/8th of the day before sea level sunrise. This is based on a 22.5 minute Mil so the time for 4 Mil is 90 minutes which is 1/8th of a day (12 * 60 = 720) /8 =90 based on the day starting at sunrise and ending at sunset. The actual calculation is getSunrise() - (getShaahZmanisGra() * 1.5)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos90Zmanis();
				}
			}
		);

		alot.addZman(
			new Zman( "96 Minutes", "96 minutes before sea level sunrise based on the time to walk the distance of 4 Mil at 24 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos96();
				}
			}
		);

		alot.addZman(
			new Zman( "96 Minutes Zmaniyot", "96 minutes zmaniyos( GR'A and the Baal Hatanya) or 1/8th of the day before sea level sunrise. This is based on a 24 minute Mil so the time for 4 Mil is 90 minutes which is 1/7.5th of a day (12 * 60 = 720) / 7.5 =96 based on the day starting at sunrise and ending at sunset. The actual calculation is getSunrise() - (getShaahZmanisGra() * 1.6)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos96Zmanis();
				}
			}
		);


		return alot;
	}

	public Zmanim getTalit() {

		// TODO: See this link to enhance the options presented here:
		//  http://www.yutorah.org/_shiurim/The%20Earliest%20Time%20to%20Perform%20the%20Mitzvot%20of%20the%20Morning.html

		Zmanim talit = new Zmanim( Zmanim.Type.Talit );
		talit.addZman(
			new Zman( "10.2°", "Based on the position of the sun when it is 10.2° below geometric zenith (90°). This calculation is used for calculating misheyakir according to some opinions. This calculation is based on the position of the sun 45 minutes before sunrise in Jerusalem in the equinox which calculates to 10.2° below geometric zenith" )  {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir10Point2Degrees();
				}
			}
		);

		talit.addZman(
			new Zman( "11°", "Based on the position of the sun when it is 11° below geometric zenith (90°). This calculation is used for calculating misheyakir according to some opinions. This calculation is based on the position of the sun 48 minutes before sunrisein Jerusalem in the equinox which calculates to 11° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir11Degrees();
				}
			}
		);

		talit.addZman(
			new Zman( "11.5°", "based on the position of the sun when it is 11.5° below geometric zenith (90°). This calculation is used for calculating misheyakir according to some opinions. This calculation is based on the position of the sun 52 minutes before sunrisein Jerusalem in the equinox which calculates to 11.5° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir11Point5Degrees();
				}
			}
		);


		return talit;
	}

	public Zmanim getHanetz() {

		Zmanim hanetz = new Zmanim( Zmanim.Type.Hanetz );
		hanetz.addZman(
			new Zman( "Sunrise", "The exact sunrise time." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSunrise();
				}
			}
		);


		return hanetz;
	}

	public Zmanim getSofZmanShma() {

		Zmanim sofZmanShema = new Zmanim( Zmanim.Type.SofZmanShma );
		sofZmanShema.addZman(
			new Zman( "3 Hours before Chatzot", "3 hours (regular and not zmaniyos) before chatzot. This is the opinion of the Shach in the Nekudas Hakesef (Yora Deah 184), Shevus Yaakov, Chasan Sofer and others." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShma3HoursBeforeChatzos();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "16.1° to Sunset", "Based on the opinion that the day starts at alos 16.1° and ends at sunset. 3 shaos zmaniyos are calculated based on this day and added to alosto reach this time. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion that the day is calculated from a alos 16.1° to sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAlos16Point1ToSunset();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "16.1° and ends at tzais 7.083°", "Based on the opinion that the day starts at alos 16.1° and ends at tzais 7.083°. 3 shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 3 shaos zmaniyos (temporal hours) after alos 16.1° based on the opinion that the day is calculated from a alos 16.1° to tzais 7.083°." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset. shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 3 shaos zmaniyos (temporal hours) after alos 72 zmaniyos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAteretTorah();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalendar.getSofZmanShmaFixedLocal(), "", "" );
		sofZmanShema.addZman(
			new Zman( "GR'A", "3 shaos zmaniyos (solar hours) after sunrise based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 3 *getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaGRA();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "GR'a Kol Eliyahu", "From the GR'A in Kol Eliyahu on Berachos #173 that states that zman krias shema is calculated as half the time from sunrise to fixed local chatzos. The GR\"A himself seems to contradic this when he stated that zman krias shema is 1/4 of the day from sunrise to sunset. See Sarah Lamoed #25 in Yisroel Vehazmanim Vol III page 1016." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaKolEliyahu();
				}
			}
		);


		sofZmanShema.addZman(
			new Zman( "MG'A", "MG'A based on alos being 72 minutes before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes before sunrise to nightfall of 72 minutes after sunset. This returns the time of 3 * shaos zmaniyos after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "MG'A 120 Minutes", "MG'A based on alos being 120 minutes or 1/6th of the day before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 120 minutes before sunrise to nightfall of 120 minutes after sunset. This returns the time of 3 *getShaahZmanis120Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA120Minutes();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "MG'A 16.1°", "MG'A based on alos being 16.1° before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from dawn to nightfall with both being 16.1° below sunrise or sunset. This returns the time of 3 *getShaahZmanis16Point1Degrees() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA16Point1Degrees();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "MG'A 19.8°", "MG'A based on alos being 19.8° before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from dawn to nightfall with both being 19.8° below sunrise or sunset. This returns the time of 3 *getShaahZmanis19Point8Degrees() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalendar.getSofZmanShmaMGA72Minutes(), "MG'A 72 Minutes", "MG'A based on alos being 72 minutes zmaniyos, or 1/10th of the day before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes zmaniyos, or 1/10th of the day before sea level sunrise to nightfall of 72 minutes zmaniyos after sea level sunset. This returns the time of 3 * getShaahZmanis72MinutesZmanis() after dawn." );
		sofZmanShema.addZman(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG'A based on alos being 72 minutes zmaniyos, or 1/10th of the day before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes zmaniyos, or 1/10th of the day before sea level sunrise to nightfall of 72 minutes zmaniyos after sea level sunset. This returns the time of 3 * getShaahZmanis72MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA72MinutesZmanis();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "MG'A 90 Minutes", "MG\"A based on alos being 90 minutes before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes before sunrise to nightfall of 90 minutes after sunset. This returns the time of 3 * getShaahZmanis90Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA90Minutes();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG\"A based on alos being 90 minutes zmaniyos before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes zmaniyos before sunrise to nightfall of 90 minutes zmaniyos after sunset. This returns the time of 3 * getShaahZmanis90MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA90MinutesZmanis();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "96 Minutes", "MG\"A based on alos being 96 minutes before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes before sunrise to nightfall of 96 minutes after sunset. This returns the time of 3 * getShaahZmanis96Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA96Minutes();
				}
			}
		);

		sofZmanShema.addZman(
			new Zman( "96 Minutes Zmaniyot", "MG\"A based on alos being 96 minutes zmaniyos before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes zmaniyos before sunrise to nightfall of 96 minutes zmaniyos after sunset. This returns the time of 3 * getShaahZmanis96MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA96MinutesZmanis();
				}
			}
		);

		return sofZmanShema;
	}

	public Zmanim getSofZmanTefila() {

		Zmanim sofZmanTefila = new Zmanim( Zmanim.Type.SofZmanTefila );
		sofZmanTefila.addZman(
			new Zman( "2 Hours before Chatzot", "2 hours before (regular and not zmaniyos) chatzot. This is based on the opinions that calculate sof zman krias shema as getSofZmanShma3HoursBeforeChatzos(). This returns the time of 2 hours before ZmanimCalendar.getChatzos()" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfila2HoursBeforeChatzos();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaFixedLocal(), "", "" );
		sofZmanTefila.addZman(
			new Zman( "GR'A", "This time is 4 hours into the day based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 4 * getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaGRA();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "Chacham Yosef Harari-Raful", "Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset. shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 4 shaos zmaniyos (temporal hours) after alos 72 zmaniyos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilahAteretTorah();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "MG'A", "MG\"A based on alos being 72 minutes before sunrise. This time is 4 shaos zmaniyos (temporal hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes before sunrise to nightfall of 72 minutes after sunset. This returns the time of 4 * getShaahZmanisMGA() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "MG'A 120 Minutes", "MG\"A based on alos being 120 minutes before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 120 minutes before sunrise to nightfall of 120 minutes after sunset. This returns the time of 4 *getShaahZmanis120Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA120Minutes();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "MG'A 16.1°", "MG\"A based on alos being ( ) 16.1° before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from dawn to nightfall with both being 16.1° below sunrise or sunset. This returns the time of 4 *getShaahZmanis16Point1Degrees() after dawn" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA16Point1Degrees();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "MG'A 19.8°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaMGA72Minutes(), "", "" );
		sofZmanTefila.addZman(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG\"A based on alos being 72 minutes zmaniyos before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes zmaniyos before sunrise to nightfall of 72 minutes zmaniyos after sunset. This returns the time of 4 * getShaahZmanis72MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA72MinutesZmanis();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "MG'A 90 Minutes", "MG\"A based on alos being 90 minutes before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes before sunrise to nightfall of 90 minutes after sunset. This returns the time of 4 * getShaahZmanis90Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90Minutes();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG\"A based on alos being 90 minutes zmaniyos before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes zmaniyos before sunrise to nightfall of 90 minutes zmaniyos after sunset. This returns the time of 4 * getShaahZmanis90MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90MinutesZmanis();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "96 Minutes", "MG\"A based on alos being 96 minutes before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes before sunrise to nightfall of 96 minutes after sunset. This returns the time of 4 * getShaahZmanis96Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96Minutes();
				}
			}
		);

		sofZmanTefila.addZman(
			new Zman( "96 Minutes Zmaniyot", "MG\"A based on alos being 96 minutes zmaniyos before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes zmaniyos before sunrise to nightfall of 96 minutes zmaniyos after sunset. This returns the time of 4 * getShaahZmanis96MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96MinutesZmanis();
				}
			}
		);

		return sofZmanTefila;
	}

	public Zmanim getChatzot() {

		Zmanim chatzot = new Zmanim( Zmanim.Type.Chatzot );
		chatzot.addZman(
			new Zman( "Chatzot (mid-day)", "GR'A based on the day for Jewish halachic times starting at sunrise and ending at sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getChatzos();
				}
			}
		);


		return chatzot;
	}

	public Zmanim getMinchaGedola() {

		Zmanim minchaGedola = new Zmanim( Zmanim.Type.MinchaGedola );
		minchaGedola.addZman(
			new Zman( "GR'A", "Based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 6.5 *getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola();
				}
			}
		);

		minchaGedola.addZman(
			new Zman( "MG'A 16.1°", "Magen Avraham with the day starting and ending 16.1° below the horizon. This is the earliest time to pray mincha. For more information on this see the documentation on mincha gedola. This is calculated as 6.5 solar hours after alos. The calculation used is 6.5 * getShaahZmanis16Point1Degrees() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola16Point1Degrees();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalendar.getMinchaGedola30Minutes(), "30 minutes after chatzot", "30 minutes after chatzos and not 1/2 of a shaah zmanis after chatzos as calculated by getMinchaGedola." );
		minchaGedola.addZman(
			new Zman( "MG'A 72 Minutes", "Magen Avraham with the day starting 72 minutes before sunrise and ending 72 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola72Minutes();
				}
			}
		);

		minchaGedola.addZman(
			new Zman( "Chacham Yosef Harari-Raful", "Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedolaAteretTorah();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalendar.getMinchaGedolaGreaterThan30(), "", "" );

		return minchaGedola;
	}

	public Zmanim getMinchaKetana() {

		Zmanim minchaKetana = new Zmanim( Zmanim.Type.MinchaKetana );
		minchaKetana.addZman(
			new Zman( "9.5 Hours", "9.5 solar hours after sunrise. This calculation is calculated based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 9.5 * getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana();
				}
			}
		);

		minchaKetana.addZman(
			new Zman( "MG'A 16.1°", "According to the Magen Avraham with the day starting and ending 16.1° below the horizon. This is the perfered earliest time to pray mincha in the opinion of the Ramba\"m and others. For more information on this see the documentation on mincha gedola. This is calculated as 9.5 solar hours after alos. The calculation used is 9.5 * getShaahZmanis16Point1Degrees() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana16Point1Degrees();
				}
			}
		);

		minchaKetana.addZman(
			new Zman( "MG'A 72 Minutes", "According to the Magen Avraham with the day starting 72 minutes before sunrise and ending 72 minutes after sunset. This is the perfered earliest time to pray mincha in the opinion of the Ramba\"m and others. For more information on this see the documentation on mincha gedola. This is calculated as 9.5 getShaahZmanis72Minutes() after alos. The calculation used is 9.5 * getShaahZmanis72Minutes() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana72Minutes();
				}
			}
		);

		minchaKetana.addZman(
			new Zman( "Chacham Yosef Harari-Raful", "Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset. This is the perfered earliest time to pray mincha in the opinion of the Ramba\"m and others. For more information on this see the documentation on mincha gedola. This is calculated as 9.5 solar hours after alos. The calculation used is 9.5 * getShaahZmanisAteretTorah() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetanaAteretTorah();
				}
			}
		);


		return minchaKetana;
	}

	public Zmanim getPlagHamincha() {

		Zmanim plagHaMincha = new Zmanim( Zmanim.Type.PlagHaMincha );
		plagHaMincha.addZman(
			new Zman( "GR'A", "10.75 hours after sunrise. This calculation is calculated based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 10.75 *getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 120 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha120Minutes();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 120 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha120MinutesZmanis();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 16.1°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha16Point1Degrees();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 18°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha18Degrees();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 19.8°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha19Point8Degrees();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 26°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha26Degrees();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 60 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha60Minutes();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 72 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha72Minutes();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 72 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha72MinutesZmanis();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 90 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha90Minutes();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 90 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha90MinutesZmanis();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 96 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha96Minutes();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "MG'A 96 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha96MinutesZmanis();
				}
			}
		);

		plagHaMincha.addZman(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset. shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 10.75 shaos zmaniyos (temporal hours) after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHaminchaAteretTorah();
				}
			}
		);

		return plagHaMincha;
	}

	public Zmanim getCandlelighting() {

		Zmanim candles = new Zmanim( Zmanim.Type.Candlelighting );
		candles.addZman(
			new Zman( "Candle Lighting", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getCandelLighting();
				}
			}
		);

		return candles;
	}

	public Zmanim getShkiah() {

		Zmanim shkiah = new Zmanim( Zmanim.Type.Shkia );
		shkiah.addZman(
			new Zman( "Sunset", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSunset();
				}
			}
		);


		return shkiah;
	}

	public Zmanim getLatestMincha() {

		Zmanim mincha = new Zmanim( Zmanim.Type.LatestMincha );
		mincha.addZman(
			new Zman( "According to Mishna Berura", "Daily Halacha's representation of the Mishna Berura's ruling, that mincha must be FINISHED by sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getLatestMinchaMishnaBerura();
				}
			}
		);

		mincha.addZman(
			new Zman( "According to Hacham Ovadia Yosef", "Daily Halacha's representation of Chacham Ovadia Yosef's ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getLatestMinchaOvadiaYosef();
				}
			}
		);

		mincha.addZman(
			new Zman( "According to Hacham Ben Sion", "Daily Halacha's representation of Chacham Ben Sion Abba Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getLatestMinchaBenSion();
				}
			}
		);


		return mincha;
	}

	
	public Zmanim getTzaitHachocavim() {

		Zmanim tzait = new Zmanim( Zmanim.Type.TzaitHachocavim );
		tzait.addZman(
			new Zman( "8.5°", "When the sun is 8.5° below the western geometric horizon (90°) after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais();
				}
			}
		);

		tzait.addZman(
			new Zman( "MG'A 120 Minutes", "Magen Avraham that the time to walk the distance of a Mil in the Ramba\"ms opinion is 2/5 of an hour (24 minutes) for a total of 120 minutes based on the opinion of Ula who calculated tzais as 5 Mil after sea level shkiah (sunset)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais120();
				}
			}
		);

		tzait.addZman(
			new Zman( "GR'A 120 Minutes Zmaniyot", "120 minutes zmaniyos (GR\"A and the Baal Hatanya) after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais120Zmanis();
				}
			}
		);

		tzait.addZman(
			new Zman( "16.1°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais16Point1Degrees();
				}
			}
		);

		tzait.addZman(
			new Zman( "18°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais18Degrees();
				}
			}
		);

		tzait.addZman(
			new Zman( "19.8°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais19Point8Degrees();
				}
			}
		);

		tzait.addZman(
			new Zman( "26°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais26Degrees();
				}
			}
		);

		tzait.addZman(
			new Zman( "Chavas Yair and Divray Malkiel", "Based on the opinion of the Chavas Yair and Divray Malkiel that the time to walk the distance of a Mil is 15 minutes for a total of 60 minutes for 4 mil after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais60();
				}
			}
		);

		tzait.addZman(
			new Zman( "Rambam", "Based on the opinion of the Ramba\"m and Rabainu Tam that tzais is calculated as the time it takes to walk 4 Mil at 18 minutes a Mil for a total of 72 minutes." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais72();
				}
			}
		);

		tzait.addZman(
			new Zman( "72 Minutes Zmaniyot", "Calculated as 72 minutes zmaniyos, or 1/10th of the day after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais72Zmanis();
				}
			}
		);

		tzait.addZman(
			new Zman( "MG'A 90 Minutes", "Based on the opinion of the Magen Avraham that the time to walk the distance of a Mil in the Ramba\"m's opinion is 18 minutes for a total of 90 minutes based on the opinion of Ula who calculated tzais as 5 Mil after sea level shkiah (sunset)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais90();
				}
			}
		);

		tzait.addZman(
			new Zman( "GR'A 90 Minutes Zmaniyot", "Calculated using 90 minutes zmaniyos (GR\"A and the Baal Hatanya) after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais90Zmanis();
				}
			}
		);

		tzait.addZman(
			new Zman( "96 Minutes", "Calculated as 96 minutes after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais96();
				}
			}
		);

		tzait.addZman(
			new Zman( "GR'A 96 Minutes Zmaniyot", "Calculated using 96 minutes zmaniyos (GR\"A and the Baal Hatanya) after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais96Zmanis();
				}
			}
		);

		tzait.addZman(
			new Zman( "40 Minutes After Sunset", "Usually calculated as 40 minutes after sunset. Please note that Chacham Yosef Harari-Raful of Yeshivat Ateret Torah who uses this time, does so only for calculating various other zmanai hayom such as Sof Zman Krias Shema and Plag Hamincha. His calendars do not publish a zman for Tzais. It should also be noted that Chacham Harari-Raful provided a 25 minute zman for Israel." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisAteretTorah();
				}
			}
		);

		tzait.addZman(
			new Zman( "Geonim 5.95°", "Based on the opinion of the Geonim calculated at the sun's position at 5.95° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim5Point95Degrees();
				}
			}
		);

		tzait.addZman(
			new Zman( "Geonim 7.083°", "Based on the opinion of the Geonim calculated at the sun's position at 7.083° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim7Point083Degrees();
				}
			}
		);

		tzait.addZman(
			new Zman( "Geonim 8.5°", "Based on the opinion of the Geonim calculated at the sun's position at 8.5° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim8Point5Degrees();
				}
			}
		);

		return tzait;
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
		final Date sunset = getShkiah().iterator().next().getTime();

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
