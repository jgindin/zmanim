package com.gindin.zehutyosef;

import com.gindin.zmanlib.calendar.HebrewDate;

import android.content.res.AssetManager;
import android.os.AsyncTask;
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
class LoadSectionTextTask
  extends AsyncTask<Void, LoadSectionTextTask.LoadedTextHolder, Void> {

  private static final int                                  INITIAL_DELAY = 300;

  private static final int                                  INTER_CHUNK_DELAY = 25;


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


  private final HebrewDate                                  date;

  private final Section                                     section;

  private final AssetManager                                assets;

  private final TextView                                    textContent;

  private int                                               nextId = 0;


  LoadSectionTextTask(
    final HebrewDate    date,
    final Section       section,
    final AssetManager  assets,
    final TextView      textContent
  ) {
    this.date = date;
    this.section = section;
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

    List<Section.TextChunk> textChunks = section.getTextInfoForDate( date );
    for ( Section.TextChunk textChunk : textChunks ) {

      if ( isCancelled() ) {
        return null;
      }

      String text = loadTextAsset( textChunk.fileName );
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
