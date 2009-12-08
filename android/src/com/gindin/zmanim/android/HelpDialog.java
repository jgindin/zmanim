package com.gindin.zmanim.android;

import android.app.Dialog;
import android.content.Context;

/**
 * Shows a dialog box with some help in it.
 */
public class HelpDialog
	extends Dialog {


	public HelpDialog( Context context ) {

		super( context );

		setContentView( R.layout.help );
		setTitle( R.string.help_header );
	}


}  // End of AboutDialog class
