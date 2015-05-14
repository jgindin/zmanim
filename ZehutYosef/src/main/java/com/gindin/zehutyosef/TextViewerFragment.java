package com.gindin.zehutyosef;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
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
				TextView textView = (TextView)rootView.findViewById( R.id.textView );
				textView.setText( text );
			}
		};

		loadTextTask.execute();

		return rootView;
	}


	@Override
	public void onAttach( Activity activity ) {
		super.onAttach( activity );
		((ZehutYosefActivity)activity).onSectionAttached( getArguments().getInt( ARG_SECTION_TITLE_ID ) );
	}


	/**
	 * Perform the work necessary to load the text. This WILL be called on a background thread.
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
