package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.TextViewerFragment;

import java.util.List;

/**
 *
 */
public class MinchaViewerFragment
	extends TextViewerFragment {


	@Override
	protected String loadText() {

		List<String> files = Tefilla.getMinchaFileNamesForDate( getDate() );

		String text = "";
		for ( String file : files ) {
			text += loadTextAsset( file );
		}
		return text;

	}

} // End of MinchaViewerFragmentImpl class
