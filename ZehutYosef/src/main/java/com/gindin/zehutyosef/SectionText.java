package com.gindin.zehutyosef;

import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.List;

/**
 *
 */
public interface SectionText {

	class TextInfo {

		public final String                                     fileName;


		public TextInfo( final String fileName ) {
			this.fileName = fileName;
		}
	}

	List<TextInfo> getTextInfoForDate( HebrewDate date );

} // End of DetermineText interface
