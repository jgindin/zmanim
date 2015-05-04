package com.gindin.zehutyosef;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.*;

import java.util.ArrayList;
import java.util.List;


public class ZehutYosefActivity
	extends AppCompatActivity
	implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment                          navigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar(CharSequence)}.
	 */
	private CharSequence                                      title;

	private final List<Integer>                               sections;

	/*

                TextView text = (TextView)findViewById( R.id.text );

                try {
                        InputStream inputStream = getResources().openRawResource( R.raw.birkat_ammazon );
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


	 */


	@SuppressWarnings( "AutoBoxing" )
	public ZehutYosefActivity() {

		sections = new ArrayList<>();
		sections.add( R.string.title_birkat_amazon );
		sections.add( R.string.mincha );
		sections.add( R.string.arvit );
	}


	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_zehut_yosef );

		navigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById( R.id.navigation_drawer );
		title = getTitle();

		// Set up the drawer.
		navigationDrawerFragment.setUp(
			R.id.navigation_drawer,
			(DrawerLayout)findViewById( R.id.drawer_layout ),
			sections
		);
	}


	@Override
	public void onNavigationDrawerItemSelected( int position ) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
			.replace( R.id.container, PlaceholderFragment.newInstance( position + 1 ) )
			.commit();
	}


	@Override
	public void showGlobalContextActionBar() {
		restoreActionBar( getString( R.string.app_name ) );
	}


	void onSectionAttached( int number ) {

		//noinspection AutoUnboxing
		title = getString( sections.get( number - 1 ) );
	}


	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		if ( !navigationDrawerFragment.isDrawerOpen() ) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate( R.menu.zehut_yosef, menu );
			restoreActionBar( title );
			return true;
		}
		return super.onCreateOptionsMenu( menu );
	}


	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if ( id == R.id.action_settings ) {
			return true;
		}

		return super.onOptionsItemSelected( item );
	}


	private void restoreActionBar( CharSequence actionBarTitle ) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_STANDARD );
		actionBar.setDisplayShowTitleEnabled( true );
		actionBar.setTitle( actionBarTitle );
	}




	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment
		extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";


		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance( int sectionNumber ) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt( ARG_SECTION_NUMBER, sectionNumber );
			fragment.setArguments( args );
			return fragment;
		}


		public PlaceholderFragment() {
		}


		@Override
		public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState
		) {
			View rootView = inflater.inflate( R.layout.fragment_zehut_yosef, container, false );
			return rootView;
		}


		@Override
		public void onAttach( Activity activity ) {
			super.onAttach( activity );
			( (ZehutYosefActivity)activity ).onSectionAttached( getArguments().getInt( ARG_SECTION_NUMBER ) );
		}
	}

}
