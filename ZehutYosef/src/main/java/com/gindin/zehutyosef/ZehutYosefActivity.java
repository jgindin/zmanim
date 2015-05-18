package com.gindin.zehutyosef;

import com.gindin.zehutyosef.settings.AboutBox;
import com.gindin.zehutyosef.settings.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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

	private static final List<FontInfo>                       FONT_INFOS = new ArrayList<>();

	static {
		FONT_INFOS.add( new FontInfo( R.string.font_shlomo,           "fonts/Shlomo.ttf" ) );
		FONT_INFOS.add( new FontInfo( R.string.font_taamey_ashkenaz,  "fonts/TaameyAshkenaz-Medium.ttf" ) );
		FONT_INFOS.add( new FontInfo( R.string.font_taamey_david,     "fonts/TaameyDavidCLM-Medium.ttf" ) );
		FONT_INFOS.add( new FontInfo( R.string.font_taamey_frank,     "fonts/TaameyFrankCLM-Medium.ttf" ) );
	}


	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment                          navigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar(CharSequence)}.
	 */
	private CharSequence                                      title;

	/** Identify the font that is to be used. */
	private FontInfo                                          activeFont;

	/** Remember which section is currently active. */
	private Section                                           activeSection;

	private final List<Section>                               sections;




	public ZehutYosefActivity() {

		sections = new ArrayList<>();
	}


	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		if ( BuildConfig.DEBUG ) {
//			Debug.startMethodTracing( "zehut-yosef" );
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


		// Setup the shared preferences with the default values. Make sure the last param is false so that we don't overwrite
		//  the user's changes.
		PreferenceManager.setDefaultValues( this, R.xml.preferences, false );

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
		String fontChoice = prefs.getString( Settings.Preference.FontChoice.key, "font_shlomo" );
		updateFont( fontChoice );
	}


	@Override
	protected void onDestroy() {

		if ( BuildConfig.DEBUG ) {
//			Debug.stopMethodTracing();
		}

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
		activeSection.getViewerFragment().updateFont( activeFont );
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

		// Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch ( id ) {
			case R.id.action_settings:
				startActivity( new Intent( this, Settings.class ) );
				return true;

			case R.id.font_size_increase:
				activeFont.size += 1.0;
				activeSection.getViewerFragment().updateFont( activeFont );
				return true;

			case R.id.font_size_decrease:
				activeFont.size -= 1.0;
				activeSection.getViewerFragment().updateFont( activeFont );
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

		int fontId = getResources().getIdentifier( fontChoice, "string", getPackageName() );

		for ( FontInfo fontInfo : FONT_INFOS ) {
			if ( fontInfo.id == fontId ) {
				activeFont = fontInfo;
				fontInfo.load( getAssets() );
				break;
			}
		}

		if ( activeSection != null ) {
			activeSection.getViewerFragment().updateFont( activeFont );
		}
	}



	static class FontInfo {

		final int                                               id;

		final String                                            relativePath;

		Typeface                                                typeface;

		// Empirically, this is the default text size.
		float                                                   size = 14f;


		FontInfo(
			final int     id,
			final String  relativePath
		) {
			this.id = id;
			this.relativePath = relativePath;
		}


		private void load( AssetManager assetManager ) {

			if ( typeface == null ) {
				typeface = Typeface.createFromAsset( assetManager, relativePath );
			}
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

			if ( key.equals( Settings.Preference.FontChoice.key ) ) {
				updateFont( sharedPreferences.getString( key, "font_shlomo" ) );
			}
		}
	}

}
