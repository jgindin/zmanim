package com.gindin.zmanim.android;

import android.app.Dialog;
import android.content.Context;

/**
 * Shows the about box for the Zmanim application.
 */
public class AboutDialog
	extends Dialog {


	public AboutDialog( Context context ) {

		super( context );

		setContentView( R.layout.about );
		setTitle( R.string.about_header );
	}



}  // End of AboutDialog class
