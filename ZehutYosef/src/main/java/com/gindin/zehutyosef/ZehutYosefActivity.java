package com.gindin.zehutyosef;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
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
		setContentView( R.layout.activity_zehut_yosef );

		sections.add( new Section( getString( R.string.mincha ), R.raw.mincha ) );
		sections.add( new Section( getString( R.string.arvit ), R.raw.birkat_ammazon ) );
		sections.add( new Section( getString( R.string.birkat_amazon ), R.raw.birkat_ammazon ) );
		sections.add( new Section( getString( R.string.meen_shalosh ), R.raw.meen_shalosh ) );

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


	void onSectionAttached( String sectionTitle ) {

		//noinspection AutoUnboxing
		title = sectionTitle;
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



	private static class Section {

		@NonNull
		private final String                                    title;

		@RawRes
		private final int                                       textResourceId;

		@Nullable
		private TextViewerFragment                              viewerFragment;


		Section(
			@NonNull  final String  title,
			@RawRes   final int     textResourceId
		) {
			this.title = title;
			this.textResourceId = textResourceId;
		}


		TextViewerFragment getViewerFragment() {

			if ( viewerFragment == null ) {
				viewerFragment = TextViewerFragment.newInstance( title, textResourceId );
			}

			return viewerFragment;
		}
	}


}
