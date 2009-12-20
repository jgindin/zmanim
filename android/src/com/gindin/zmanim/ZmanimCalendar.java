/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Date;

/**
 * Adds support for earliest time for Mincha.
 */
public class ZmanimCalendar
	extends ComplexZmanimCalendar {

	/**
	 * How many minutes after sunset can you still start mincha?
	 * <p/>
	 * Daily Halacha (http://www.dailyhalacha.com/Display.asp?ClipDate=6/29/2004): The latest time for mincha is: "The Ben
	 * Ish Chai says in one place that you should not pray the Amidah if you start the Amidah at the 13th minute [after
	 * sunset]. But in another place he says that you can start praying the Amidah so long as the majority of the Amidah is
	 * said before the 13.5 minute mark. But Chacham Ovadia's opinion is different. He says it would be permissible to
	 * start the Amidah at 13 minutes after sunset even if the majority is said after the 13.5 minute mark. Chacham Ovadia
	 * says it would be permissible so long as you start within the time. So technically, it would be permissible if you
	 * start the Amidah in Mincha even as late as 13 minutes and 25 seconds after sunset."
	 * <p/>
	 * NOTE: In order to be careful and avoid rounding issues, I'm NOT including the 25 seconds.
	 */
	private static final int                                  MINUTES_AFTER_SUNSET_FOR_MINCHA = 13;

	static final long MINUTE_MILLIS = 60000L;
	

	public ZmanimCalendar( GeoLocation location ) {
		super( location );
	}


	/**
	 * Method to return the latest time for mincha based on Daily Halacha's representation of
	 *  Chacham Ovadia Yosef's ruling.
	 *
	 * @return the <code>Date</code> representing the time.
	 */
	public Date getLatestMinchaOvadiaYosef() {
		return getTimeOffset( getSunset(), -MINUTES_AFTER_SUNSET_FOR_MINCHA * MINUTE_MILLIS);
	}


}  // End of ZmanimCalendar class
