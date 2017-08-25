package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.R;
import com.gindin.zehutyosef.Section;
import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.List;

/**
 *
 */
public class Shachrit
  extends Section {

  public Shachrit() {
    super( R.string.shachrit );
  }


  @Override
  public List<TextChunk> getTextInfoForDate( final HebrewDate date ) {
    return null;
  }

} // End of Shachrit class
