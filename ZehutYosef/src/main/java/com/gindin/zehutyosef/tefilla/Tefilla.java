/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.Section;
import com.gindin.zmanlib.calendar.HebrewDate;

import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Separate out the details of which chunks to load so that we can easliy unit test.
 */
@SuppressWarnings( "MagicNumber" )
public abstract class Tefilla
  extends Section {


  protected Tefilla(
    @StringRes final int  titleId
  ) {
    super( titleId );
  }


  protected abstract TefillaType getTefillaType();


  protected abstract List<TextChunk> getTefillaText( final DoWeSay doWeSay );


  @Override
  protected final List<TextChunk> getTextInfoForDate( final HebrewDate date ) {

    DoWeSay doWeSay = new DoWeSay( date, getTefillaType() );

    return getTefillaText( doWeSay );
  }


  @SuppressWarnings( "FeatureEnvy" )
  protected final List<TextChunk> getAmidahFileNamesForDate( DoWeSay doWeSay ) {

    final TefillaType tefillaType = getTefillaType();
    final boolean isAseretYameiTeshuva = doWeSay.aseretYameiTeshuvaInsertions();
    final boolean isTaanit = doWeSay.taanitDayInsertions();

    List<TextChunk> files = new ArrayList<>();

    // Assembling Amidah:
    files.add( new TextChunk( "tefilla/amidah/2_1_magen_avraham_intro.txt" ) );

    if ( isAseretYameiTeshuva ) {
      files.add( new TextChunk( "tefilla/amidah/2_2_magen_avraham_asert_yamei_teshuva.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/2_3_magen_avraham_conclusion.txt" ) );
    files.add( new TextChunk( "tefilla/amidah/3_1_atah_gibor_intro.txt" ) );

    if ( doWeSay.moridHaTal() ) {
      files.add( new TextChunk( "tefilla/amidah/3_2_atah_gibor_summer.txt" ) );
    }
    else {
      files.add( new TextChunk( "tefilla/amidah/3_2_atah_gibor_winter.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/3_3_atah_gibor_middle.txt" ) );

    if ( isAseretYameiTeshuva ) {
      files.add( new TextChunk( "tefilla/amidah/3_4_atah_gibor_asert_yamei_teshuva.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/3_5_atah_gibor_conclusion.txt" ) );

    if ( ( tefillaType == TefillaType.Shachrit ) || ( tefillaType == TefillaType.Mincha ) ) {
      files.add( new TextChunk( "tefilla/amidah/4_1_kedusha_nakdishach.txt" ) );
    }

    if ( isAseretYameiTeshuva ) {
      files.add( new TextChunk( "tefilla/amidah/4_2_kedusha_asert_yamei_teshuva.txt" ) );
    }
    else {
      files.add( new TextChunk( "tefilla/amidah/4_2_kedusha.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/5_atah_honen_thru_goel_yisrael.txt" ) );

    if ( isTaanit ) {
      files.add( new TextChunk( "tefilla/amidah/6_fast_day_hazzan_insertion.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/7_refaenu.txt" ) );

    if ( doWeSay.barechenu() ) {
      files.add( new TextChunk( "tefilla/amidah/8_barechenu.txt" ) );
    }
    else {
      files.add( new TextChunk( "tefilla/amidah/8_barech_alenu.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/9_tikah_bshofar.txt" ) );

    if ( isAseretYameiTeshuva ) {
      files.add( new TextChunk( "tefilla/amidah/10_hashiva_asert_yamei_teshuva.txt" ) );
    }
    else {
      files.add( new TextChunk( "tefilla/amidah/10_hashiva.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/11_lamalshinim_thru_et_zemach.txt" ) );
    files.add( new TextChunk( "tefilla/amidah/12_1_shma_kolenu_intro.txt" ) );

    if ( isTaanit ) {
      files.add( new TextChunk( "tefilla/amidah/12_2_shma_kolenu_fast_day.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/12_3_shma_kolenu_conclusion.txt" ) );
    files.add( new TextChunk( "tefilla/amidah/13_1_retzeh_intro.txt" ) );

    if ( doWeSay.roshChodeshAdditions() ) {
      files.add( new TextChunk( "tefilla/amidah/13_2_retzeh_rosh_chodesh.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/13_3_retzeh_conclusion.txt" ) );
    files.add( new TextChunk( "tefilla/amidah/14_1_modim_intro.txt" ) );

    if ( tefillaType != TefillaType.Arvit ) {
      files.add( new TextChunk( "tefilla/amidah/14_1_modim_drabanan.txt" ) );
    }

    if ( doWeSay.chanukahAdditions() ) {
      files.add( new TextChunk( "tefilla/amidah/14_2_modim_chanukah.txt" ) );
    }
    else if ( doWeSay.purimAdditions() ) {
      files.add( new TextChunk( "tefilla/amidah/14_2_modim_purim.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/14_3_modim_middle.txt" ) );

    if ( isAseretYameiTeshuva ) {
      files.add( new TextChunk( "tefilla/amidah/14_4_modim_asert_yamei_teshuva.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/14_5_modim_conclusion.txt" ) );
//    files.add( new TextChunk( "tefilla/amidah/15_birkat_cohanim NEED THIS.txt" ) );
    files.add( new TextChunk( "tefilla/amidah/16_1_sim_shalom_intro.txt" ) );

    if ( isAseretYameiTeshuva ) {
      files.add( new TextChunk( "tefilla/amidah/16_2_sim_shalom_asert_yamei_teshuva.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/16_3_sim_shalom_conclusion.txt" ) );

    if ( tefillaType != TefillaType.Arvit ) {
      files.add( new TextChunk( "tefilla/amidah/17_end_of_amidah_instructions.txt" ) );
    }

    files.add( new TextChunk( "tefilla/amidah/17_end_of_amidah.txt" ) );

    return files;
  }


} // End of MeenShalosh class
