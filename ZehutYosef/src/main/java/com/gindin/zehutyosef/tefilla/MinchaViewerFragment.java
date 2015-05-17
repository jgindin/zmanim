package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.TextViewerFragment;

/**
 *
 */
public class MinchaViewerFragment
	extends TextViewerFragment {


	@Override
	protected String loadText() {

		return loadTextAsset( "tefilla/mincha/mincha.txt" );
	}

} // End of MinchaViewerFragmentImpl class
