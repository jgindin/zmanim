/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import com.gindin.zmanim.android.R;

/**
 * Lazily creates a reusable dialog, and displays HTML text.
 */
public class MessageDisplayer {


	private AlertDialog                                       messageDialog;

	private WebView helpTextView;


	public Dialog getInstance(
		Context owner
	) {

		if ( null != messageDialog ) {
			return messageDialog;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder( owner );

		// Set an empty title. If we were to use an empty string, then for some reason calling setTitle() on the
		//  actual dialog (later...in the update method) doesn't have any affect...
		builder.setTitle( "  " );

		LayoutInflater inflater = (LayoutInflater)owner.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.message_display, null );
		helpTextView = (WebView)layout.findViewById( R.id.message_display );

		builder.setView( layout );

		messageDialog = builder.create();
		return messageDialog;
	}



	public void update(
		int     titleResource,
		String  textLocation
	) {

		messageDialog.setTitle( titleResource );

		helpTextView.loadUrl( "file:///android_asset/" + textLocation );
	}


} // End of MessageDisplayer class
