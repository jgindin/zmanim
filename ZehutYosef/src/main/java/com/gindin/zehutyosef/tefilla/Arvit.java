package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Arvit
	extends Tefilla {

	@Override
	public List<String> getTextFilesForDate( final HebrewDate date ) {

		final Details dateDetails = date.getDetails();

		List<String> files = new ArrayList<>();

		// Assembling Arvit:

		files.add( "tefilla/arvit/arvit.txt" );

		return files;
	}

} // End of Arvit class
