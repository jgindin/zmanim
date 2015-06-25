package com.gindin.zehutyosef;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 */
class SidurSection {


	@StringRes
	private final int                                         titleId;

	private final SectionText sectionText;

	private LoadTextTask                                      loadTextTask;

	private PrimaryDrawerItem                                 primaryDrawerItem;


	SidurSection(
		@StringRes int titleId,
		SectionText sectionText
	) {

		this.titleId = titleId;
		this.sectionText = sectionText;
	}


	IDrawerItem asDrawerItem() {

		if ( primaryDrawerItem == null ) {
			primaryDrawerItem = new PrimaryDrawerItem().withName( titleId ).withIdentifier( titleId );
		}

		return primaryDrawerItem;
	}


	@StringRes
	public int getTitleId() {
		return titleId;
	}


	void load(
		final HebrewDate date,
		final AssetManager assets,
		final TextView textContent
	) {

		loadTextTask = new LoadTextTask( date, sectionText, assets, textContent ) {
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
		extends AsyncTask<Void, LoadTextTask.LoadedTextHolder, Void> {

		private static final int                                INITIAL_DELAY = 300;

		private static final int                                INTER_CHUNK_DELAY = 150;

		private int                                             nextId = 0;

		class LoadedTextHolder {

			final int id;

			final String text;


			LoadedTextHolder(
				final String text
			) {
				this.id = nextId;
				this.text = text;

				nextId++;
			}
		}


		private final HebrewDate                                date;

		private final SectionText                               sectionText;

		private final AssetManager                              assets;

		private final TextView                                  textContent;


		LoadTextTask(
			final HebrewDate date,
			final SectionText sectionText,
			final AssetManager assets,
			final TextView textContent
		) {
			this.date = date;
			this.sectionText = sectionText;
			this.assets = assets;
			this.textContent = textContent;
		}


		@Override
		protected Void doInBackground( final Void... params ) {

			// A little delay before we get going to let the drawer close back up...
			try {
				Thread.sleep( INITIAL_DELAY );
			}
			catch ( InterruptedException e ) { /* never mind */ }

			List<SectionText.TextInfo> textInfos = sectionText.getTextInfoForDate( date );
			for ( SectionText.TextInfo textInfo : textInfos ) {

				if ( isCancelled() ) {
					return null;
				}

				String text = loadTextAsset( textInfo.fileName );
				publishProgress( new LoadedTextHolder( text ) );
				try {
					Thread.sleep( INTER_CHUNK_DELAY );
				}
				catch ( InterruptedException e ) { /* never mind */ }
			}

			return null;
		}


		@Override
		protected void onProgressUpdate( final LoadedTextHolder... values ) {
			super.onProgressUpdate( values );

			if ( isCancelled() ) {
				return;
			}

			LoadedTextHolder loadedTextHolder = values[ 0 ];
			if ( loadedTextHolder.id == 0 ) {
				textContent.setText( "" );
			}

			textContent.append( loadedTextHolder.text );
		}


		private String loadTextAsset(
			final String assetPath
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
