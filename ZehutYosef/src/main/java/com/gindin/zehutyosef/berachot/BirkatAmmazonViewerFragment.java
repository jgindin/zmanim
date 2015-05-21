package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.TextViewerFragment;

import java.util.List;

/**
 *
 */
public class BirkatAmmazonViewerFragment
	extends TextViewerFragment {

	@Override
	protected String loadText() {

		List<String> files = BirkatAmmazon.getFileNamesForDate( getDate() );

		String text = "";
		for ( String file : files ) {
			text += loadTextAsset( file );
		}

		return text;
	}

} // End of BirkatAmmazonViewerFragment class
