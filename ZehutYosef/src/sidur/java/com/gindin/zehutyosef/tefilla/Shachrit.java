package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.R;
import com.gindin.zehutyosef.Section;
import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Shachrit
  extends Tefilla {

  public Shachrit() {
    super( R.string.shachrit );
  }


  /*@Override
  public List<TextChunk> getTextInfoForDate( final HebrewDate date ) {
    return null;
  }*/

  @Override
  protected TefillaType getTefillaType() {
    return TefillaType.Shachrit;
  }

  @SuppressWarnings( "FeatureEnvy" )
  //@Override
  protected List<TextChunk> getTefillaText( final DoWeSay doWeSay ) {

    List<TextChunk> files = new ArrayList<>();

    files.add( new TextChunk( "tefila/shachrit/hashkamat_haboker.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/patach_eliyahu.txt" ) );
    files.add( new TextChunk( "tefila/kadesh_li_kol_bekhor.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/korbanot.txt" ) );
    files.add( new TextChunk( "tefila/ribi_hananya.txt" ) );
    files.add( new TextChunk( "tefila/kadish_al_yisrael.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/hodu.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/aromimkha_perek_number.txt" ) );

    if ( doWeSay.chanukahAdditions() ) {
      files.add( new TextChunk( "tefila/shachrit/aromimkha_hanuka_intro.txt" ) );
    }

    files.add( new TextChunk( "tefila/shachrit/aromimkha" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/shachrit/amonai_hu_haelokim.txt" ) );
    }

    files.add( new TextChunk( "tefila/shachrit/amonai_melekh.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/skip_to_barukh_sheamar.txt" ) );
    files.add( new TextChunk( "tefila/ribi_hananya.txt" ) );
    files.add( new TextChunk( "tefila/kadish_al_yisrael.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/pesuke_dezimra.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/shachrit/shir_hamaalot_aseret_yeme_teshuva.txt" ) );
    }

    files.add( new TextChunk( "tefila/hatzi_kadish.txt" ) );
    files.add( new TextChunk( "tefila/barekhu.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/shema_opening_berakhot.txt" ) );
    files.add( new TextChunk( "tefila/shema.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/shema_after_berakhot.txt" ) );

    files.add( new TextChunk( "tefila/amida_opening.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/amida_ayt_zokhrenu.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_melekh_ozer_ata_gibor.txt" ) );

    if ( doWeSay.moridHaTal() ) {
      files.add( new TextChunk( "tefila/amida_morid_hatal.txt" ) );
    } else {
      files.add( new TextChunk( "tefila/amida_mashiv_haruach.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_mekhalkel_hayim.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/amida_ayt_mi_khamokha.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_veneeman_ata.txt" ) );
    files.add( new TextChunk( "tefila/amida_kedusha_opening_regular.txt" ) );
    files.add( new TextChunk( "tefila/amida_kedusha_conclusion.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/amida_ata_kadosh_ayt.txt" ) );
    } else {
      files.add( new TextChunk( "tefila/amida_ata_kadosh_regular.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_ata_honen_thru_goel_yisrael.txt" ) );

    if ( doWeSay.taanitDayInsertions() ) {
      files.add( new TextChunk( "tefila/amida_anenu_shatz.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_refaenu.txt" ) );

    if ( doWeSay.barechenu() ) {
      files.add( new TextChunk( "tefila/amida_barekhenu.txt" ) );
    } else {
      files.add( new TextChunk( "tefila/amida_barekh_alenu.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_teka_beshofar.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/amida_hashiva_ayt.txt" ) );
    } else {
      files.add( new TextChunk( "tefila/amida_hashiva_regular.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_lamalshinim_thru_shema_kolenu_opening.txt" ) );

    if ( doWeSay.taanitDayInsertions() ) {
      files.add( new TextChunk( "tefila/amida_anenu_individual.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_shema_kolenu_conclusion.txt" ) );
    files.add( new TextChunk( "tefila/amida_retze_opening.txt" ) );

    if ( doWeSay.roshChodeshAdditions() ) {
      files.add( new TextChunk( "tefila/amida_yaale_veyavo_rh.txt" ) );
    }  //TODO: Hol Hamoed insertions

    files.add( new TextChunk( "tefila/amida_retze_conclusion.txt" ) );
    files.add( new TextChunk( "tefila/amida_modim_opening.txt" ) );
    files.add( new TextChunk( "tefila/amida_modim_derabanan.txt" ) );

    if ( doWeSay.chanukahAdditions() || doWeSay.purimAdditions() ) {
      files.add( new TextChunk( "tefila/amida_al_hanisim.txt" ) );
    } if ( doWeSay.chanukahAdditions() ) {
      files.add( new TextChunk( "tefila/amida_hanuka.txt" ) );
    } else if ( doWeSay.purimAdditions() ) {
      files.add( new TextChunk( "tefila/amida_purim.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_veal_kulam_yitbarakh.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/amida_ayt_ukhtov_lehyaim_tovim.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_modim_conclusion.txt" ) );
    files.add( new TextChunk( "tefila/amida_barekhenu_baberakha.txt" ) );
    files.add( new TextChunk( "tefila/amida_sim_shalom_opening.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/amida_ayt_uvsefer_hayim.txt" ) );
    }

    files.add( new TextChunk( "tefila/amida_sim_shalom_conclusion.txt" ) );
    files.add( new TextChunk( "tefila/amida_elokai_netzor.txt" ) );

    //TODO: Halel

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/avinu_malkenu.txt" ) );
    }

    if ( doWeSay.tachanun() ) {
      files.add( new TextChunk( "tefila/tahanunim_ana.txt" ) );

      if ( doWeSay.mondayThursdayAdditions() ) {
        files.add( new TextChunk( "tefila/shachrit/tahanunim_sbh_monday_thursday.txt" ) );
      }

      files.add( new TextChunk( "tefila/tahanunim_kel_erekh_apayim.txt" ) );
      files.add( new TextChunk( "tefila/shachrit/tahanunim_rahum_vehanun.txt" ) );

      if ( doWeSay.mondayThursdayAdditions() ) {
        files.add( new TextChunk( "tefila/shachrit/tahanunim_eb_monday_thursday.txt" ) );
      }

      files.add( new TextChunk( "tefila/shachrit/tahanunim_avinu_malkenu.txt" ) );

      if ( doWeSay.mondayThursdayAdditions() ) {
        files.add( new TextChunk( "tefila/shachrit/tahanunim_monday_thursday.txt" ) );
      }
    }

    files.add( new TextChunk( "tefila/amonai_adonenu.txt" ) );
    files.add( new TextChunk( "tefila/hatzi_kadish.txt" ) );

    if ( doWeSay.mondayThursdayAdditions() ) {
      files.add( new TextChunk( doWeSay.tachanun() ? "tefila/sefer_yes_tahanunim.txt" : "tefila/sefer_no_tahanunim/txt") );
      files.add( new TextChunk( "tefila/sefer_taking_out.txt" ) );
      files.add( new TextChunk( "tefila/sefer_birkot_hatorah.txt" ) );  //TODO: inline reading
      files.add( new TextChunk( "tefila/hatzi_kadish.txt" ) );
    }

    files.add( new TextChunk( "tefila/shachrit/yehi_hasdekha.txt" ) );
    files.add( new TextChunk( "tefila/ashre.txt" ) );

    if ( doWeSay.tachanun() ) {
      files.add( new TextChunk( "tefila/shachrit/lamnatzeach_mizmor_ledavid.txt" ) );
    }

    files.add( new TextChunk( "tefila/uva_letziyon_goel.txt" ) );
    files.add( new TextChunk( "tefila/veata_kadosh.txt" ) );
    files.add( new TextChunk( "tefila/kadish_titkabal.txt" ) );

    if ( doWeSay.mondayThursdayAdditions() ) {
      files.add( new TextChunk( "tefila/returning_sefer.txt" ) );
    }

    if ( doWeSay.tachanun() ) {
      files.add( new TextChunk( "tefila/shachrit/tefila_ledavid" ) );
    }

    files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_intro.txt" ) );

    int day = doWeSay.getDayOfWeek();
    if ( day == 1) {
      files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_sunday.txt" ) );
    } else if ( day == 2) {
      files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_monday.txt" ) );
    } else if ( day == 3) {
      files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_tuesday.txt" ) );
    } else if ( day == 4) {
      files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_wednesday.txt" ) );
    } else if ( day == 5) {
      files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_thursday.txt" ) );
    } else if (day == 6) {
      files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_friday.txt" ) );
    }

    //TODO: SBH insertions for day after Kipur & Shushan Purim
    files.add( new TextChunk( "tefila/shachrit/shir_shel_yom_hoshienu.txt" ) );
    files.add( new TextChunk( "tefila/kadish_yatom.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/kave.txt" ) );
    files.add( new TextChunk( "tefila/ketoret.txt" ) );

    if ( doWeSay.aseretYameiTeshuvaInsertions() ) {
      files.add( new TextChunk( "tefila/sbh_ketoret_insert.txt" ) );
    }

    files.add( new TextChunk( "tefila/ketoret_conclusion.txt" ) );
    files.add( new TextChunk( "tefila/shachrit/ketoret_conclusion_shachrit.txt" ) ); //TODO: korbanot for Nisan
    files.add( new TextChunk( "tefila/ribi_hananya.txt" ) );
    files.add( new TextChunk( "tefila/kadish_al_yisrael.txt" ) );
    files.add( new TextChunk( "tefila/barekhu.txt" ) );
    files.add( new TextChunk( "tefila/alenu.txt" ) );
    files.add( new TextChunk( "tefila/uvtoratekha.txt" ) );
    files.add( new TextChunk( "tefila/ledavid.txt" ) );
    files.add( new TextChunk( "tefila/kadish_yatom.txt" ) );

    return files;
  }

} // End of Shachrit class
