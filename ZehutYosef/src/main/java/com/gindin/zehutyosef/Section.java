package com.gindin.zehutyosef;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.TextView;

import java.util.List;

/**
 * Represents a section of the sidur (such as Mincha, Birkat Ammazon, etc...) which is represented as a top-level
 *  entry in the navigation drawer.
 */
public abstract class Section {


  /**
   * Represents one 'chunk' of the text for this section.
   */
  protected class TextChunk {

    public final String                                     fileName;


    public TextChunk( final String fileName ) {
      this.fileName = fileName;
    }
  }



  @StringRes
  private final int                                         titleId;

  @Nullable
  private LoadSectionTextTask                               loadingSectionTask;

  private PrimaryDrawerItem                                 primaryDrawerItem;


  protected Section(
    @StringRes int  titleId
  ) {

    this.titleId = titleId;
  }


  /**
   * Obtain all the text of the section for the given date.
   */
  protected abstract List<TextChunk> getTextInfoForDate( HebrewDate date );



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


  public void load(
    final HebrewDate    date,
    final AssetManager  assets,
    final TextView      textContent
  ) {

    loadingSectionTask = new LoadSectionTextTask( date, this, assets, textContent ) {
      @Override
      protected void onPostExecute( final Void aVoid ) {
        super.onPostExecute( aVoid );
        loadingSectionTask = null;
      }
    };

    loadingSectionTask.execute();
  }


  void cancel() {

    if ( loadingSectionTask != null ) {
      loadingSectionTask.cancel( false );
      loadingSectionTask = null;
    }
  }


} // End of Section
