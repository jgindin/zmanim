package com.gindin.zmanim.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
