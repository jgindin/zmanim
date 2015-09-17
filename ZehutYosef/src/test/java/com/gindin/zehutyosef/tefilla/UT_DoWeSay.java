package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_DoWeSay {


	@Test
	public void may_30_2015() {

		DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2015, Calendar.MAY, 30 ), TefillaType.Arvit );
		Assert.assertTrue( doWeSay.barechenu() );
		Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
		Assert.assertFalse( doWeSay.chanukahAdditions() );
		Assert.assertFalse( doWeSay.erevShabbatModifications() );
		Assert.assertFalse( doWeSay.purimAdditions() );
		Assert.assertTrue( doWeSay.moridHaTal() );
		Assert.assertFalse( doWeSay.roshChodeshAdditions() );
		Assert.assertFalse( doWeSay.taanitDayInsertions() );
		Assert.assertFalse( doWeSay.tachanun() );
	}


	@Test
	public void sept_16_2015() {

		DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 16 ), TefillaType.Mincha );
		Assert.assertTrue( doWeSay.barechenu() );
		Assert.assertTrue( doWeSay.aseretYameiTeshuvaInsertions() );
		Assert.assertFalse( doWeSay.chanukahAdditions() );
		Assert.assertFalse( doWeSay.erevShabbatModifications() );
		Assert.assertFalse( doWeSay.purimAdditions() );
		Assert.assertTrue( doWeSay.moridHaTal() );
		Assert.assertFalse( doWeSay.roshChodeshAdditions() );
		Assert.assertTrue( doWeSay.taanitDayInsertions() );
		Assert.assertTrue( doWeSay.tachanun() );
	}



} // End of UT_DoWeSay class
