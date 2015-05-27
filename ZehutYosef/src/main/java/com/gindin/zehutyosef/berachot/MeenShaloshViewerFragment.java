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
	protected void loadText( final TextLoadingCallback callback ) {

		List<String> files = MeenShalosh.getFileNamesForDate( getDate() );

		for ( String file : files ) {
			callback.stream( loadTextAsset( file ) );
		}

	}

} // End of MeenShaloshViewerFragment class
