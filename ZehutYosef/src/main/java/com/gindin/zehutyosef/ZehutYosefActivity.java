package com.gindin.zehutyosef;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

	private final List<Section>                               sections;




	@SuppressWarnings( "AutoBoxing" )
	public ZehutYosefActivity() {

		sections = new ArrayList<>();
	}


	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		if ( BuildConfig.DEBUG ) {
			StrictMode.enableDefaults();
		}

		setContentView( R.layout.activity_zehut_yosef );

		sections.add( new Section( R.string.mincha ) );
		sections.add( new Section( R.string.arvit ) );
		sections.add( new Section( R.string.birkat_amazon ) );
		sections.add( new Section( R.string.meen_shalosh ) );

		navigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById( R.id.navigation_drawer );
		title = getTitle();

		List<String> sectionNames = new ArrayList<>();
		for ( Section section : sections ) {
			sectionNames.add( section.title );
		}

		// Set up the drawer.
		navigationDrawerFragment.setUp(
			R.id.navigation_drawer,
			(DrawerLayout)findViewById( R.id.drawer_layout ),
			sectionNames
		);
	}


	@Override
	public void onNavigationDrawerItemSelected( int position ) {

		// update the main content by replacing fragments
		TextViewerFragment viewerFragment = sections.get( position ).getViewerFragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
			.replace( R.id.container, viewerFragment )
			.commit();
	}


	@Override
	public void showGlobalContextActionBar() {
		restoreActionBar( getString( R.string.app_name ) );
	}


	void onSectionAttached( @StringRes int sectionTitleId ) {

		title = getString( sectionTitleId );
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



	private class Section {

		final String                                            title;

		@StringRes
		private final int                                       titleId;

		@Nullable
		private TextViewerFragment                              viewerFragment;


		Section(
			@StringRes int titleId
		) {

			this.title = getString( titleId );
			this.titleId = titleId;
		}


		TextViewerFragment getViewerFragment() {

			if ( viewerFragment == null ) {
				viewerFragment = TextViewerFragment.newInstance( titleId );
			}

			return viewerFragment;
		}
	}


}
