package com.gindin.zehutyosef;

import com.gindin.zehutyosef.berachot.BirkatAmmazonViewerFragment;
import com.gindin.zehutyosef.berachot.MeenShaloshViewerFragment;
import com.gindin.zehutyosef.settings.Settings;
import com.gindin.zehutyosef.tefilla.ArvitViewerFragment;
import com.gindin.zehutyosef.tefilla.MinchaViewerFragment;
import com.gindin.zmanlib.calendar.HebrewDate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class TextViewerFragment
	extends Fragment {


	/** The fragment argument representing the section title for this fragment. */
	private static final String                               ARG_SECTION_TITLE_ID = "section_title_id";



	/** The text view we're actually rendering into. */
	private TextView                                          textView;

	private FontInfo                                          activeFont;

	private HebrewDate                                        date;


	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	static TextViewerFragment newInstance(
		@StringRes int titleId
	 ) {

		TextViewerFragment fragment;
		switch ( titleId ) {
			case R.string.mincha:
				fragment = new MinchaViewerFragment();
				break;

			case R.string.arvit:
				fragment = new ArvitViewerFragment();
				break;

			case R.string.birkat_amazon:
				fragment = new BirkatAmmazonViewerFragment();
				break;

			case R.string.meen_shalosh:
				fragment = new MeenShaloshViewerFragment();
				break;

			default:
				throw new IllegalArgumentException( "Unknown fragment id: " + titleId );
		}


		Bundle args = new Bundle();
		args.putInt( ARG_SECTION_TITLE_ID, titleId );

		fragment.setArguments( args );

		return fragment;
	}


	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		setRetainInstance( true );
	}


	@Override
	public final View onCreateView(
		LayoutInflater  inflater,
		ViewGroup       container,
		Bundle          savedInstanceState
	) {

		final View rootView = inflater.inflate( R.layout.fragment_zehut_yosef, container, false );

		AsyncTask<Void, Void, String> loadTextTask = new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground( final Void... params ) {
				return loadText();
			}


			@Override
			protected void onPostExecute( final String text ) {
				super.onPostExecute( text );

				textView = (TextView)rootView.findViewById( R.id.textView );

				if ( activeFont != null ) {
					setFont( activeFont );
				}

				textView.setText( text );
			}
		};

		loadTextTask.execute();

		// We really want the screen on while the user is on this screen. Otherwise it'll turn off while they're in
		//  the middle of something pretty important.
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( getActivity() );
		boolean keepScreenOn = preferences.getBoolean( Settings.PreferenceKey.KeepScreenOn.key, true );
		rootView.setKeepScreenOn( keepScreenOn );

		return rootView;
	}


	@Override
	public void onAttach( Activity activity ) {
		super.onAttach( activity );
		((ZehutYosefActivity)activity).onSectionAttached( getArguments().getInt( ARG_SECTION_TITLE_ID ) );
	}


	@Override
	public void onResume() {
		super.onResume();

		// Seems like this works more reliably. Dunno if it's because when the user goes out to the Settings, this
		//  Fragment is paused, and updating the font doesn't take affect?
		setFont( activeFont );
	}


	void setFont( final FontInfo newFont ) {

		activeFont = newFont;

		if ( textView != null ) {
			textView.setTypeface( newFont.getTypeface() );
			textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, newFont.getSize() );
		}
	}


	void setDate( final HebrewDate date ) {
		this.date = date;
	}


	protected HebrewDate getDate() {
		return date;
	}


	/**
	 * Perform the work necessary to load the text. This WILL be called on a background thread.
	 *
	 * Generally, this method will be one or more calls to loadTextAsset(), which will take care of reading
	 *  the specified file (as a path, relative to the assets directory).
	 */
	protected abstract String loadText();



	protected final String loadTextAsset( String assetPath ) {

		StringBuilder buffer = new StringBuilder();

		InputStream input = null;
		try {
			input = getActivity().getAssets().open( assetPath );
			BufferedReader reader = new BufferedReader( new InputStreamReader( input ) );

			String line;
			//noinspection NestedAssignment
			while ( ( line = reader.readLine() ) != null ) {
				buffer.append( line ).append( "\n" );
			}

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

		return buffer.toString();
	}


}
