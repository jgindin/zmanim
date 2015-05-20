package com.gindin.zehutyosef;

import com.gindin.zehutyosef.settings.AboutBox;
import com.gindin.zehutyosef.settings.Settings;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.HebrewDateFormatter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ZehutYosefActivity
	extends AppCompatActivity
	implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/** The sections of tefillah, berachot, etc... the user can choose. */
	private final List<Section>                               sections;

	/** The date for which all tefillah, etc..., are to be shown. */
	@NonNull
	private HebrewDate                                        hebrewDate;

	/** Identify the font that is to be used. */
	@SuppressWarnings( "NullableProblems" )
	@NonNull
	private FontInfo                                          activeFont;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment                          navigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar(CharSequence)}.
	 */
	private CharSequence                                      title;

	/** Remember which section is currently active. */
	private Section                                           activeSection;

	private PrefsListener                                     prefsListener;



	public ZehutYosefActivity() {

		sections = new ArrayList<>();

		// Default to showing today.
		//  (At some point we'll let the user change the date they're looking at...)
		hebrewDate = HebrewCalendar.today();
	}


	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		if ( BuildConfig.DEBUG ) {
//			Debug.startMethodTracing( "zehut-yosef" );
			StrictMode.enableDefaults();
		}

		setContentView( R.layout.activity_zehut_yosef );

		// Make sure we have a font. It may get updated / modified when we setup the shared prefs, but this will
		//  ensure we don't have to worry about NPE.
		updateFont( FontInfo.init( this ) );

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


		// Setup the shared preferences with the default values. Make sure the last param is false so that we don't overwrite
		//  the user's changes.
		prefsListener = new PrefsListener();
		PreferenceManager.getDefaultSharedPreferences( this )
			.registerOnSharedPreferenceChangeListener( prefsListener );
		PreferenceManager.setDefaultValues( this, R.xml.preferences, false );
	}


	@Override
	protected void onDestroy() {

//		if ( BuildConfig.DEBUG ) {
//			Debug.stopMethodTracing();
//		}

		PreferenceManager.getDefaultSharedPreferences( this )
			.unregisterOnSharedPreferenceChangeListener( prefsListener );

		super.onDestroy();
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

		for ( Section section : sections ) {
			if ( section.titleId == sectionTitleId ) {
				activeSection = section;
				break;
			}
		}

		// Make sure it's using the correct font.
		activeFont.load( getAssets() );
		TextViewerFragment viewerFragment = activeSection.getViewerFragment();
		viewerFragment.setFont( activeFont );
		viewerFragment.setDate( hebrewDate );
	}


	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		if ( !navigationDrawerFragment.isDrawerOpen() ) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate( R.menu.zehut_yosef, menu );
			restoreActionBar( title );

			MenuItem dateItem = menu.findItem( R.id.date );
			TextView dateView = (TextView)dateItem.getActionView();

			HebrewDateFormatter formatter = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.SHORT );
			dateView.setText( formatter.format( hebrewDate, null ) );

			return true;
		}

		return super.onCreateOptionsMenu( menu );
	}


	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {

		// Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch ( id ) {
			case R.id.action_settings:
				startActivity( new Intent( this, Settings.class ) );
				return true;

			case R.id.action_about:
				AboutBox aboutBox = new AboutBox();
				aboutBox.show( getSupportFragmentManager(), "about" );
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


	private void updateFont( final String fontChoice) {

		activeFont = FontInfo.getByIdName( fontChoice );
		activeFont.load( getAssets() );

		if ( activeSection != null ) {
			activeSection.getViewerFragment().setFont( activeFont );
		}
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


	// create shared prefs listener
	// figure out why not selecting font...

	private class PrefsListener
		implements SharedPreferences.OnSharedPreferenceChangeListener {

		@Override
		public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences,
			final String            key
		) {

			if ( key.equals( Settings.PreferenceKey.FontChoice.key ) ) {
				updateFont( sharedPreferences.getString( key, "font_shlomo" ) );
			}
			else if ( key.equals( Settings.PreferenceKey.FontSize.key ) ) {
				int newSize = Integer.parseInt( sharedPreferences.getString( key, "14" ) );
				activeFont.setSize( newSize );
				if ( activeSection != null ) {
					activeSection.getViewerFragment().setFont( activeFont );
				}
			}
		}
	}

}
