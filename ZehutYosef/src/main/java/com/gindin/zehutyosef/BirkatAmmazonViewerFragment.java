package com.gindin.zehutyosef;

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
