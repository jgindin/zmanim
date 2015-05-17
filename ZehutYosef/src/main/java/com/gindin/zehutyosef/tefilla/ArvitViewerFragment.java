package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.TextViewerFragment;

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
