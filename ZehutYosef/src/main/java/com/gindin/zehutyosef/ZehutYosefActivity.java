package com.gindin.zehutyosef;

import com.gindin.zehutyosef.berachot.BirkatAmmazon;
import com.gindin.zehutyosef.berachot.MeenShalosh;
import com.gindin.zehutyosef.settings.AboutBox;
import com.gindin.zehutyosef.settings.Settings;
import com.gindin.zehutyosef.tefilla.Arvit;
import com.gindin.zehutyosef.tefilla.Mincha;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.HebrewDateFormatter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ZehutYosefActivity
	extends AppCompatActivity {

	/** The sections of tefillah, berachot, etc... the user can choose. */
	private final List<Section>                               sections;

	/** The date for which all tefillah, etc..., are to be shown. */
	@NonNull
	private HebrewDate                                        hebrewDate;

	/** Identify the font that is to be used. */
	@SuppressWarnings( "NullableProblems" )
	@NonNull
	private FontInfo                                          activeFont;


	/** Remember which section is currently active. */
	private Section                                           activeSection;

	private PrefsListener                                     prefsListener;

	private Drawer                                            drawer;

	private TextView                                          textContent;


	public ZehutYosefActivity() {

		sections = new ArrayList<>();

		// Default to showing today.
		//  (At some point we'll let the user change the date they're looking at...)
		hebrewDate = HebrewCalendar.today();
	}


	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		if ( BuildConfig.DEBUG ) {
//			Debug.startMethodTracing( "zehut-yosef" );
			StrictMode.enableDefaults();
		}

		sections.add( new Section( R.string.mincha, new Mincha() ) );
		sections.add( new Section( R.string.arvit, new Arvit() ) );
		sections.add( new Section( R.string.birkat_amazon, new BirkatAmmazon() ) );
		sections.add( new Section( R.string.meen_shalosh, new MeenShalosh() ) );

		// Do this after the initial setup, because when we get rotated, the fragments are preserved and so don't have to go through
		//  the whole lifecycle. This means that if we were to invoked super.onCreate() first from this method,
		//  the fragments would get attached and try to call back to let us know this, but we wouldn't be ready
		//  to handle it yet...
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_zehut_yosef );

		textContent = (TextView)findViewById( R.id.text_content );

		// Set up the drawer.
		initDrawer( savedInstanceState );


		// Setup the shared preferences with the default values. Make sure the last param is false so that we don't overwrite
		//  the user's changes.
		prefsListener = new PrefsListener();
		PreferenceManager.getDefaultSharedPreferences( this )
			.registerOnSharedPreferenceChangeListener( prefsListener );

		// Doing this work inline was causing a StrictMode violation.
		AsyncTask<Void, Void, SharedPreferences> readPrefsTask = new AsyncTask<Void, Void, SharedPreferences>() {
			@Override
			protected SharedPreferences doInBackground( final Void... params ) {

				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( ZehutYosefActivity.this );
				FontInfo.init( ZehutYosefActivity.this, prefs);

				return prefs;
			}


			@Override
			protected void onPostExecute( final SharedPreferences prefs ) {
				super.onPostExecute( prefs );
				// Wait until here so that we know our font is set....
				drawer.setSelection( sections.get( 0 ).asDrawerItem() );

				// NOTE: the typeface MUST be updated first, as that ensures that we have a font!
				updateTypeface( prefs );
				updateFontSize( prefs );
				updateKeepScreenOn( prefs );
			}
		};

		readPrefsTask.execute();
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
	public boolean onCreateOptionsMenu( Menu menu ) {

		getMenuInflater().inflate( R.menu.zehut_yosef, menu );

		MenuItem dateItem = menu.findItem( R.id.date );
		TextView dateView = (TextView)dateItem.getActionView();

		HebrewDateFormatter formatter = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.SHORT );
		dateView.setText( formatter.format( hebrewDate, null ) );

		return true;
	}



	@Override
	public void onBackPressed() {

		// Handle the back press :D close the drawer first and if the drawer is closed close the activity
		if ( drawer != null && drawer.isDrawerOpen() ) {
			drawer.closeDrawer();
		}
		else {
			super.onBackPressed();
		}
	}


	private void initDrawer( final Bundle savedInstanceState ) {

		// Handle Toolbar
		final Toolbar toolbar = (Toolbar)findViewById( R.id.toolbar );
		setSupportActionBar( toolbar );

		IDrawerItem[] drawerItems = new IDrawerItem[ sections.size() ];
		for ( int i = 0; i < drawerItems.length; i++ ) {
			drawerItems[i] = sections.get( i ).asDrawerItem();
		}

		AccountHeader header = new AccountHeaderBuilder()
			.withActivity( this )
			.withHeaderBackground( R.drawable.header )
			.build();

		drawer = new DrawerBuilder()
			.withActivity( this )
			.withToolbar( toolbar )
			.withAccountHeader( header )
			.addDrawerItems( drawerItems )
			.addStickyDrawerItems(
				new SecondaryDrawerItem()
					.withName( R.string.action_settings )
					.withIcon( FontAwesome.Icon.faw_cog )
					.withIdentifier( R.string.action_settings ),
				new SecondaryDrawerItem()
					.withName( R.string.action_about )
					.withIcon( GoogleMaterial.Icon.gmd_help )
					.withIdentifier( R.string.action_about )
			)
			.withOnDrawerItemClickListener(
				new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(
						final AdapterView<?>  adapterView,
						final View            view,
						final int             position,
						final long            id,
						final IDrawerItem     drawerItem
					) {

						handleItem( drawerItem );
						drawer.closeDrawer();
						return true;
					}
				}
			)
			.withSavedInstance( savedInstanceState )
	    .withShowDrawerOnFirstLaunch( true )
			.build();
	}


	private void handleItem( final IDrawerItem drawerItem ) {

		boolean handled = false;
		switch ( drawerItem.getIdentifier() ) {
			case R.string.action_settings:
				startActivity( new Intent( this, Settings.class ) );
				handled = true;
				drawer.setSelection( activeSection.asDrawerItem() );
				break;

			case R.string.action_about:
				AboutBox aboutBox = new AboutBox();
				aboutBox.show( getSupportFragmentManager(), "about" );
				handled = true;
				drawer.setSelection( activeSection.asDrawerItem() );
				break;
		}

		if ( !handled ) {
			for ( Section section : sections ) {
				if ( section.titleId == drawerItem.getIdentifier() ) {
					activeSection = section;
					section.load( getAssets(), textContent, hebrewDate );
					getSupportActionBar().setTitle( section.titleId );
				}
				else {
					section.cancel();
				}
			}
		}
	}


	@SuppressWarnings( "FeatureEnvy" )
	private void updateTypeface(
		final SharedPreferences sharedPreferences
	) {
		activeFont = FontInfo.getByIdName( sharedPreferences.getString( Settings.PreferenceKey.FontChoice.key, FontInfo.FONT_INFOS.get( 0 ).getId() ) );
		activeFont.load( getAssets() );
		textContent.setTypeface( activeFont.getTypeface() );
	}


	private void updateFontSize(
		final SharedPreferences sharedPreferences
	) {
		int newSize = Integer.parseInt( sharedPreferences.getString( Settings.PreferenceKey.FontSize.key, "14" ) );
		activeFont.setSize( newSize );
		textContent.setTextSize( TypedValue.COMPLEX_UNIT_SP, activeFont.getSize() );
	}


	private void updateKeepScreenOn(
		final SharedPreferences sharedPreferences
	) {
		boolean keepScreenOn = sharedPreferences.getBoolean( Settings.PreferenceKey.KeepScreenOn.key, true );
		textContent.setKeepScreenOn( keepScreenOn );
	}


	private static class Section {


		@StringRes
		private final int                                       titleId;

		private final DetermineText                             determineText;

		private LoadTextTask                                    loadTextTask;

		private PrimaryDrawerItem                               primaryDrawerItem;


		Section(
			@StringRes int  titleId,
		  DetermineText   determineText
		) {

			this.titleId = titleId;
			this.determineText = determineText;
		}


		IDrawerItem asDrawerItem() {

			if ( primaryDrawerItem == null ) {
				primaryDrawerItem = new PrimaryDrawerItem().withName( titleId ).withIdentifier( titleId );
			}

			return primaryDrawerItem;
		}


		void load( final AssetManager assets, final TextView textContent, final HebrewDate date ) {

			loadTextTask = new LoadTextTask( date, determineText, assets, textContent ) {
				@Override
				protected void onPostExecute( final Void aVoid ) {
					super.onPostExecute( aVoid );
					loadTextTask = null;
				}
			};

			loadTextTask.execute();
		}


		void cancel() {

			if ( loadTextTask != null ) {
				loadTextTask.cancel( false );
				loadTextTask = null;
			}
		}



		private static class LoadTextTask
			extends AsyncTask<Void, LoadTextTask.Chunk, Void> {

			private int                                           nextId = 0;

			class Chunk {

				final int                                           id;
				final String                                        text;


				Chunk(
					final String text
				) {
					this.id = nextId;
					this.text = text;

					nextId++;
				}
			}


			private final HebrewDate                              date;
			private final DetermineText                           determineText;
			private final AssetManager                            assets;
			private final TextView                                textContent;



			LoadTextTask(
				final HebrewDate date,
				final DetermineText determineText,
				final AssetManager assets,
				final TextView textContent
			) {
				this.date = date;
				this.determineText = determineText;
				this.assets = assets;
				this.textContent = textContent;
			}


			@Override
			protected Void doInBackground( final Void... params ) {

				// A little delay before we get going to let the drawer close back up...
				try {  Thread.sleep( 300 ); } catch ( InterruptedException e ) { /* never mind */ }

				List<String> files = determineText.getTextFilesForDate( date );
				for ( String file : files ) {

					if ( isCancelled() ) {
						return null;
					}

					String text = loadTextAsset( file );
					publishProgress( new Chunk( text ) );
					try {  Thread.sleep( 150 ); } catch ( InterruptedException e ) { /* never mind */ }
				}

				return null;
			}


			@Override
			protected void onProgressUpdate( final Chunk... values ) {
				super.onProgressUpdate( values );

				if ( isCancelled() ) {
					return;
				}

				Chunk chunk = values[0];
				if ( chunk.id == 0 ) {
					textContent.setText( "" );
				}

				textContent.append( chunk.text );
			}


			private String loadTextAsset(
				final String        assetPath
			) {

				StringBuilder buffer = new StringBuilder();

				InputStream input = null;
				try {
					input = assets.open( assetPath );
					BufferedReader reader = new BufferedReader( new InputStreamReader( input ) );

					String line;
					//noinspection NestedAssignment
					while ( ( line = reader.readLine() ) != null ) {
						buffer.append( line ).append( "\n" );
					}

				}
				catch ( IOException e ) {
					Log.e( "zehut-yosef", "Error loading: '" + assetPath + "'" );
					if ( input != null ) {
						try {
							input.close();
						}
						catch ( IOException e1 ) {
							// Oh well.
						}
					}
				}

				return buffer.toString();
			}

		} // End of LoadTextTask


	} // End of Section



	private class PrefsListener
		implements SharedPreferences.OnSharedPreferenceChangeListener {

		@SuppressWarnings( "FeatureEnvy" )
		@Override
		public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences,
			final String            key
		) {

			if ( key.equals( Settings.PreferenceKey.FontChoice.key ) ) {
				updateTypeface( sharedPreferences );
			}
			else if ( key.equals( Settings.PreferenceKey.FontSize.key ) ) {
				updateFontSize( sharedPreferences );
			}
			else if ( key.equals( Settings.PreferenceKey.KeepScreenOn.key ) ) {
				updateKeepScreenOn( sharedPreferences );
			}
		}
	}

}
