package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.TextViewerFragment;

import java.util.List;

/**
 *
 */
public class MinchaViewerFragment
	extends TextViewerFragment {


	@Override
	protected void loadText( final TextLoadingCallback callback ) {

		List<String> files = Tefilla.getMinchaFileNamesForDate( getDate() );

		for ( String file : files ) {
			callback.stream( loadTextAsset( file ) );
		}

	}

} // End of MinchaViewerFragmentImpl class
