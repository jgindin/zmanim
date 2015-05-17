package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.TextViewerFragment;

/**
 *
 */
public class BirkatAmmazonViewerFragment
	extends TextViewerFragment {

	@Override
	protected String loadText() {

		return loadTextAsset( "berachot/birkat_ammazon.txt" );
	}

} // End of BirkatAmmazonViewerFragment class
