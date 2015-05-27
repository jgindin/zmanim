package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.TextViewerFragment;

import java.util.List;

/**
 *
 */
public class BirkatAmmazonViewerFragment
	extends TextViewerFragment {

	@Override
	protected void loadText( final TextLoadingCallback callback ) {

		List<String> files = BirkatAmmazon.getFileNamesForDate( getDate() );

		for ( String file : files ) {
			callback.stream( loadTextAsset( file ) );
		}

	}

} // End of BirkatAmmazonViewerFragment class
