package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.TextViewerFragment;

/**
 *
 */
public class MeenShaloshViewerFragment
	extends TextViewerFragment {


	@Override
	protected String loadText() {

		return loadTextAsset( "berachot/meen_shalosh.txt" );
	}

} // End of MeenShaloshViewerFragment class
