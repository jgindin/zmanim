package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

/**
 *
 */
public class UT_ShemaTimes
	extends TimeCheckingBaseTest {


	@Test
	public void talitTimesForSeattle() throws ParseException {

		HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.NOVEMBER, 1 );
		HebrewDate lastDate = HebrewCalendar.getForDate( 2016, Calendar.OCTOBER, 31 );

		runTest( date, lastDate, seattle, "" );
	}


	@Override
	protected Zmanim.Type getTypeForTest() {
		return Zmanim.Type.SofZmanShema;
	}


	@Override
	protected boolean shouldRoundDown() {
		return true;
	}


} // End of UT_ShemaTimes class
