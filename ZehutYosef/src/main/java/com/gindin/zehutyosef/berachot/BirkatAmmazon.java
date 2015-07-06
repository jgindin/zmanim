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
public class BirkatAmmazon
	extends Section {


	public BirkatAmmazon() {
		super( R.string.birkat_amazon );
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	public List<TextChunk> getTextInfoForDate( final HebrewDate date ) {

		List<TextChunk> files = new ArrayList<>();
		
		// Assembling Birkat Ammazon:

		final Details dateDetails = date.getDetails();
		final boolean isShabbat = dateDetails.isShabbat();
		final boolean isRoshChodesh = dateDetails.isRoshChodesh();
		final boolean isSukkot = dateDetails.isSukkot();
		final boolean isYomTov = dateDetails.isYomTov();
		final boolean isCholHaMoed = dateDetails.isCholHaMoed();

		files.add( new TextChunk( "berachot/birkat_ammazon/1_intro.txt" ) );
		if ( isShabbat ) {
			if ( isYomTov ) {
				files.add( new TextChunk( "berachot/birkat_ammazon/2_hav_lan_shabbat_yom_tov.txt" ) );
			}
			else {
				files.add( new TextChunk( "berachot/birkat_ammazon/2_hav_lan_shabbat.txt" ) );
			}
		}
		else if ( isYomTov ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/2_hav_lan_yom_tov.txt" ) );
		}
		else {
			files.add( new TextChunk( "berachot/birkat_ammazon/2_hav_lan.txt" ) );
		}

		files.add( new TextChunk( "berachot/birkat_ammazon/3_vaydaber_elay.txt" ) );
		files.add( new TextChunk( "berachot/birkat_ammazon/4_first_beracha_plus_nodeh.txt" ) );

		if ( dateDetails.isPurim() ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/5_purim.txt" ) );
		}
		else if ( dateDetails.isChanukah() ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/5_hanukkah.txt" ) );
		}

		files.add( new TextChunk( "berachot/birkat_ammazon/6_val_hakol_and_rachem.txt" ) );

		if ( isShabbat ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/7_retzeh_shabbat.txt" ) );
		}

		if ( isRoshChodesh ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/8_yaaleh_vayvao_rosh_hodesh.txt" ) );
		}

		if ( dateDetails.isPesach() ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/8_yaaleh_vayvao_pesach.txt" ) );
		}
		else if ( dateDetails.isShavuot() ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/8_yaaleh_vayvao_shavuot.txt" ) );
		}
		else if ( isSukkot ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/8_yaaleh_vayvao_sukkot.txt" ) );
		}
		else if ( dateDetails.isShminiAtzeret() || dateDetails.isSimchatTorah() ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/8_yaaleh_vayvao_shmini_atzeret_simchat_torah.txt" ) );
		}
		else if ( dateDetails.isRoshHashana() ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/8_yaaleh_vayvao_rosh_ashana.txt" ) );
		}

		files.add( new TextChunk( "berachot/birkat_ammazon/9_vtivneh_and_arachamans.txt" ) );

		if ( isShabbat ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/10_arachaman_shabbat.txt" ) );
		}

		if ( isRoshChodesh ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/10_arachaman_rosh_hodesh.txt" ) );
		}

		if ( isYomTov ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/10_arachaman_yom_tov.txt" ) );
		}
		else if ( isCholHaMoed ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/10_arachaman_chol_ha_moed.txt" ) );
		}

		if ( isSukkot ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/10_arachaman_sukkot.txt" ) );
		}

		files.add( new TextChunk( "berachot/birkat_ammazon/11_arachamans.txt" ) );

		if ( isShabbat || isRoshChodesh || isYomTov || isCholHaMoed ) {
			files.add( new TextChunk( "berachot/birkat_ammazon/12_conclusion_shabbat_rosh_hodesh_festivals.txt" ) );
		}
		else {
			files.add( new TextChunk( "berachot/birkat_ammazon/12_conclusion_weekday.txt" ) );
		}		
		
		return files;
	}
	
	
} // End of BirkatAmmazon class
