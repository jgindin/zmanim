package com.gindin.zehutyosef.settings;

import com.gindin.zehutyosef.BuildConfig;
import com.gindin.zehutyosef.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
// TODO: Consolidate w/ Zmanim...
public class AboutBox
	extends DialogFragment {


	private static final String                               VERSION_PLACEHOLDER = "$VERSION_GOES_HERE$";



	@NonNull
	@Override
	public Dialog onCreateDialog( final Bundle savedInstanceState ) {

		FragmentActivity owner = getActivity();

		LayoutInflater inflater = (LayoutInflater)owner.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.message_display, null );
		TextView helpTextView = (TextView)layout.findViewById( R.id.message_display );

		loadText( owner.getAssets(), helpTextView );

		AlertDialog.Builder builder = new AlertDialog.Builder( owner )
			.setTitle( R.string.action_about_title )
			.setView( layout );

		return builder.create();
	}


	private static void loadText(
		final AssetManager  assetManager,
		final TextView      textView
	) {

		AsyncTask<Void,Void,Spanned> loadTextTasks = new AsyncTask<Void, Void, Spanned>() {
			@Override
			protected Spanned doInBackground( final Void... params ) {

				StringBuilder buffer = new StringBuilder();

				InputStream input = null;
				try {
					input = assetManager.open( "about.html" );
					BufferedReader reader = new BufferedReader( new InputStreamReader( input ) );

					String line;
					//noinspection NestedAssignment
					while ( ( line = reader.readLine() ) != null ) {
						buffer.append( line ).append( "\n" );
					}

					// Update the version information manually...
					String hgVersion = ( BuildConfig.HG_VERSION.isEmpty() ? "DEBUG" : BuildConfig.HG_VERSION );

					int versionLocation = buffer.indexOf( VERSION_PLACEHOLDER );
					buffer.replace( versionLocation, versionLocation + VERSION_PLACEHOLDER.length(),
						BuildConfig.VERSION_NAME + "." + hgVersion + " Type: " + BuildConfig.BUILD_TYPE + " built on: " + BuildConfig.BUILD_TIME );

				}
				catch ( IOException e ) {
					if ( input != null ) {
						try {
							input.close();
						}
						catch ( IOException e1 ) {
							// Oh well.
						}
					}
					e.printStackTrace();
				}

				return Html.fromHtml( buffer.toString() );
			}


			@Override
			protected void onPostExecute( final Spanned text ) {
				super.onPostExecute( text );

				textView.setText( text );
			}
		};

		loadTextTasks.execute();
	}


} // End of AboutBox class
