package com.gindin.zehutyosef;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.RawRes;
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
public class TextViewerFragment
	extends Fragment {


	/** The fragment argument representing the section title for this fragment. */
	private static final String                               ARG_SECTION_TITLE = "section_title";

	/** The fragment argument representing the id of the text resource to load and display. */
	private static final String                               ARG_TEXT_RESOURCE_ID = "textResourceId";



	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static TextViewerFragment newInstance( String sectionTitle, @RawRes int textResourceId ) {

		TextViewerFragment fragment = new TextViewerFragment();
		Bundle args = new Bundle();
		args.putString( ARG_SECTION_TITLE, sectionTitle );
		args.putInt( ARG_TEXT_RESOURCE_ID, textResourceId );

		fragment.setArguments( args );

		return fragment;
	}



	@Override
	public View onCreateView(
		LayoutInflater  inflater,
		ViewGroup       container,
		Bundle          savedInstanceState
	) {
		View rootView = inflater.inflate( R.layout.fragment_zehut_yosef, container, false );

		TextView text = (TextView)rootView.findViewById( R.id.textView );

		try {
			int resource = getArguments().getInt( ARG_TEXT_RESOURCE_ID );
			InputStream inputStream = getResources().openRawResource( resource );
			BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );

			StringBuilder buffer = new StringBuilder();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				buffer.append( line ).append( "\n" );
			}

			text.setText( buffer );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}


		return rootView;
	}


	@Override
	public void onAttach( Activity activity ) {
		super.onAttach( activity );
		((ZehutYosefActivity)activity).onSectionAttached( getArguments().getString( ARG_SECTION_TITLE ) );
	}
}
