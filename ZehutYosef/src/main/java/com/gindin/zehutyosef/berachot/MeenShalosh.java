package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.R;
import com.gindin.zehutyosef.Section;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.ArrayList;
import java.util.List;

/**
 * Separate out the details of which chunks to load so that we can easliy unit test.
 */
public class MeenShalosh
  extends Section {


  public MeenShalosh() {
    super( R.string.meen_shalosh );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Override
  public List<TextChunk> getTextInfoForDate( final HebrewDate date ) {

    final Details dateDetails = date.getDetails();
    
    List<TextChunk> files = new ArrayList<>();
  
    // Assembling the before / after blessings:
    files.add( new TextChunk( "berachot/other_before_and_after_food/1_meen_shalosh_before.txt" ) );
    files.add( new TextChunk( "berachot/other_before_and_after_food/2_meen_shalosh_after_intro.txt" ) );

    if ( dateDetails.isShabbat() ) {
      files.add( new TextChunk( "berachot/other_before_and_after_food/3_meen_shalosh_shabbat.txt" ) );
    }

    if ( dateDetails.isRoshChodesh() ) {
      files.add( new TextChunk( "berachot/other_before_and_after_food/3_meen_shalosh_rosh_chodesh.txt" ) );
    }

    if ( dateDetails.isPesach() ) {
      files.add( new TextChunk( "berachot/other_before_and_after_food/4_meen_shalosh_pesach.txt" ) );
    }
    else if ( dateDetails.isSukkot() ) {
      files.add( new TextChunk( "berachot/other_before_and_after_food/4_meen_shalosh_sukkot.txt" ) );
    }
    else if ( dateDetails.isShminiAtzeret() ) {
      files.add( new TextChunk( "berachot/other_before_and_after_food/4_meen_shalosh_shemini_atzeret_simchat_torah.txt" ) );
    }
    else if ( dateDetails.isShavuot() ) {
      files.add( new TextChunk( "berachot/other_before_and_after_food/4_meen_shalosh_shavuot.txt" ) );
    }

    files.add( new TextChunk( "berachot/other_before_and_after_food/5_meen_shalosh_conclusion.txt" ) );
    files.add( new TextChunk( "berachot/other_before_and_after_food/6_other_before_and_after_blessings.txt" ) );
    
    return files;
  }
  
} // End of MeenShalosh class
