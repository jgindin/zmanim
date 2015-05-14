package com.gindin.zehutyosef;

/**
 *
 */
public class ArvitViewerFragment
	extends TextViewerFragment {

	@Override
	protected String loadText() {

		return loadTextAsset( "tefilla/arvit/arvit.txt" );
	}

} // End of ArvitViewerFragment class
