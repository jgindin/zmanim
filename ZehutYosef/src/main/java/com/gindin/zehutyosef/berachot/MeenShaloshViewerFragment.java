package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.TextViewerFragment;

import java.util.List;

/**
 *
 */
public class MeenShaloshViewerFragment
	extends TextViewerFragment {


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	protected String loadText() {

		List<String> files = MeenShalosh.getFileNamesForDate( getDate() );

		String text = "";
		for ( String file : files ) {
			text += loadTextAsset( file );
		}

		return text;
	}

} // End of MeenShaloshViewerFragment class
