package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.TextViewerFragment;

import java.util.List;

/**
 *
 */
public class ArvitViewerFragment
	extends TextViewerFragment {

	@Override
	protected String loadText() {

		List<String> files = Tefilla.getArvitFileNamesForDate( getDate() );

		String text = "";
		for ( String file : files ) {
			text += loadTextAsset( file );
		}

		return text;
	}

} // End of ArvitViewerFragment class
