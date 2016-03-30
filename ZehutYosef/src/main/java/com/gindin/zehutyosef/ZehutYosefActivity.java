package com.gindin.zehutyosef;

import com.gindin.zehutyosef.berachot.BirkatAmmazon;
import com.gindin.zehutyosef.berachot.MeenShalosh;
import com.gindin.zehutyosef.settings.Settings;
import com.gindin.zehutyosef.tefilla.Arvit;
import com.gindin.zehutyosef.tefilla.Mincha;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.HebrewDateFormatter;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ZehutYosefActivity
	extends AppCompatActivity {

	/** The sections of tefillah, berachot, etc... the user can choose. */
	private final List<Section>                          sections;

	/** The date for which all tefillah, etc..., are to be shown. */
	@NonNull
	private HebrewDate                                        hebrewDate;

	/** Identify the font that is to be used. */
	@SuppressWarnings( "NullableProblems" )
	@NonNull
	private FontInfo                                          activeFont;


	/** Remember which section is currently active. */
	private Section activeSection;

	private PrefsListener                                     prefsListener;

	private Drawer                                            drawer;

	private TextView                                          textContent;

	private SecularDatePickerFragment                         datePickerFragment;
	private DatePickerDialog.OnDateSetListener                dateChangeListener;

	private TextView                                          dateView;

	private About                                             aboutBuilder;


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

		sections.add( new Mincha() );
		sections.add( new Arvit() );
		sections.add( new BirkatAmmazon() );
		sections.add( new MeenShalosh() );

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
			}
		};

		readPrefsTask.execute();
	}


	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( ZehutYosefActivity.this );
		updateKeepScreenOn( prefs );
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
		dateView = (TextView)dateItem.getActionView();

		// 50px is empirically a good number.
		//noinspection MagicNumber
		dateView.setPadding( 0, 0, 50, 0 );

		updateHebrewDate();

		dateView.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( final View v ) {
				handleDateClick();
			}
		} );

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
				drawer.setSelection( activeSection.asDrawerItem(), false );
				break;

			case R.string.action_about:

				if ( aboutBuilder == null ) {
					aboutBuilder = new About();
				}

				aboutBuilder.show( this );

				handled = true;
				drawer.setSelection( activeSection.asDrawerItem(), false );
				break;
		}

		if ( !handled ) {
			for ( Section section : sections ) {
				if ( section.getTitleId() == drawerItem.getIdentifier() ) {
					activeSection = section;
					section.load( hebrewDate, getAssets(), textContent );

					// Not sure how getSupportActionBar() could return null...we have one, so it should always be there.
					//  I think it is declared as @Nullable because an activity doesn't *have* to have one.
					//noinspection ConstantConditions
					getSupportActionBar().setTitle( section.getTitleId() );
				}
				else {
					section.cancel();
				}
			}
		}
	}


	private void handleDateClick() {

		if ( dateChangeListener == null ) {
			dateChangeListener = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(
					final DatePicker view,
					final int year,
					final int monthOfYear,
					final int dayOfMonth
				) {

					hebrewDate = HebrewCalendar.getForDate( year, monthOfYear, dayOfMonth );
					updateHebrewDate();
				}
			};
		}

		if ( datePickerFragment == null ) {
			datePickerFragment = new SecularDatePickerFragment( dateChangeListener );
		}

		datePickerFragment.promptForNewDate( getSupportFragmentManager(), hebrewDate );
	}


	private void updateHebrewDate() {
		HebrewDateFormatter formatter = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.SHORT );
		dateView.setText( formatter.format( hebrewDate, null ) );
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


	@SuppressLint( "ValidFragment" )
	public static class SecularDatePickerFragment
		extends DialogFragment {

		private final DatePickerDialog.OnDateSetListener        listener;

		private DatePickerDialog                                datePickerDialog;

		private HebrewDate                                      startingDate;


		SecularDatePickerFragment( final DatePickerDialog.OnDateSetListener listener ) {
			this.listener = listener;
		}


		void promptForNewDate(
			final FragmentManager fragmentManager,
			final HebrewDate      currentDate
		) {

			startingDate = currentDate;
			if ( datePickerDialog != null ) {
				updateForDate();
			}

			show( fragmentManager, "datePicker" );
		}


		@NonNull
		@Override
		public Dialog onCreateDialog( final Bundle savedInstanceState ) {
			//noinspection MagicNumber
			datePickerDialog = new DatePickerDialog( getActivity(), listener, 2015, 4, 30 );

			updateForDate();

			return datePickerDialog;
		}


		@SuppressWarnings( "FeatureEnvy" )
		private void updateForDate() {
			datePickerDialog.getDatePicker().updateDate( startingDate.getGregorianYear(),
				startingDate.getGregorianMonth(), startingDate.getGregorianDayOfMonth() );
		}

	}

}
