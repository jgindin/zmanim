package com.gindin.zehutyosef.berachot;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.ArrayList;
import java.util.List;

/**
 * Separate out the details of which chunks to load so that we can easliy unit test.
 */
class BirkatAmmazon {

	
	@SuppressWarnings( "FeatureEnvy" )
	static List<String> getFileNamesForDate( HebrewDate date ) {
		
		List<String> files = new ArrayList<>();
		
		// Assembling Birkat Ammazon:
		// 1_intro
		// 2_hav_lan or 2_hav_lan_shabbat or 2_hav_lan_shabbat_yom_tov or 2_hav_lan_yom_tov
		// 3_vaydaber_elay
		// 4_first_beracha_plus_nodeh
		// ? 5_purim or 5_hanukkah
		// 6_val_hakol_and_rachem
		// ? 7_retzeh_shabbat
		// ? 8_yaaleh_vayvao_rosh_hodesh or 8_yaaleh_vayvao_pesach or 8_yaaleh_vayvao_shavuot or 8_yaaleh_vayvao_sukkot or
		//    8_yaaleh_vayvao_shmini_atzeret_simchat_torah or 8_yaaleh_vayvao_rosh_ashana
		// 9_vtivneh_and_arachamans
		// 10_arachaman_shabbat
		// 10_arachaman_rosh_hodesh
		// 10_arachaman_yom_tov
		// 10_arachaman_chol_ha_moed
		// 10_arachaman_sukkot
		// 11_arachamans
		// 12_conclusion_weekday or 12_conclusion_shabbat_rosh_hodesh_festivals

		final Details dateDetails = date.getDetails();
		final boolean isShabbat = dateDetails.isShabbat();
		final boolean isRoshChodesh = dateDetails.isRoshChodesh();
		final boolean isSukkot = dateDetails.isSukkot();
		final boolean isYomTov = dateDetails.isYomTov();
		final boolean isCholHaMoed = dateDetails.isCholHaMoed();

		files.add( "berachot/birkat_ammazon/1_intro.txt" );
		if ( isShabbat ) {
			if ( isYomTov ) {
				files.add( "berachot/birkat_ammazon/2_hav_lan_shabbat_yom_tov.txt" );
			}
			else {
				files.add( "berachot/birkat_ammazon/2_hav_lan_shabbat.txt" );
			}
		}
		else if ( isYomTov ) {
			files.add( "berachot/birkat_ammazon/2_hav_lan_yom_tov.txt" );
		}
		else {
			files.add( "berachot/birkat_ammazon/2_hav_lan.txt" );
		}

		files.add( "berachot/birkat_ammazon/3_vaydaber_elay.txt" );
		files.add( "berachot/birkat_ammazon/4_first_beracha_plus_nodeh.txt" );

		if ( dateDetails.isPurim() ) {
			files.add( "berachot/birkat_ammazon/5_purim.txt" );
		}
		else if ( dateDetails.isChanukah() ) {
			files.add( "berachot/birkat_ammazon/5_hanukkah.txt" );
		}

		files.add( "berachot/birkat_ammazon/6_val_hakol_and_rachem.txt" );

		if ( isShabbat ) {
			files.add( "berachot/birkat_ammazon/7_retzeh_shabbat.txt" );
		}

		if ( isRoshChodesh ) {
			files.add( "berachot/birkat_ammazon/8_yaaleh_vayvao_rosh_hodesh.txt" );
		}

		if ( dateDetails.isPesach() ) {
			files.add( "berachot/birkat_ammazon/8_yaaleh_vayvao_pesach.txt" );
		}
		else if ( dateDetails.isShavuot() ) {
			files.add( "berachot/birkat_ammazon/8_yaaleh_vayvao_shavuot.txt" );
		}
		else if ( isSukkot ) {
			files.add( "berachot/birkat_ammazon/8_yaaleh_vayvao_sukkot.txt" );
		}
		else if ( dateDetails.isShminiAtzeret() || dateDetails.isSimchatTorah() ) {
			files.add( "berachot/birkat_ammazon/8_yaaleh_vayvao_shmini_atzeret_simchat_torah.txt" );
		}
		else if ( dateDetails.isRoshHashana() ) {
			files.add( "berachot/birkat_ammazon/8_yaaleh_vayvao_rosh_ashana.txt" );
		}

		files.add( "berachot/birkat_ammazon/9_vtivneh_and_arachamans.txt" );

		if ( isShabbat ) {
			files.add( "berachot/birkat_ammazon/10_arachaman_shabbat.txt" );
		}

		if ( isRoshChodesh ) {
			files.add( "berachot/birkat_ammazon/10_arachaman_rosh_hodesh.txt" );
		}

		if ( isYomTov ) {
			files.add( "berachot/birkat_ammazon/10_arachaman_yom_tov.txt" );
		}
		else if ( isCholHaMoed ) {
			files.add( "berachot/birkat_ammazon/10_arachaman_chol_ha_moed.txt" );
		}

		if ( isSukkot ) {
			files.add( "berachot/birkat_ammazon/10_arachaman_sukkot.txt" );
		}

		files.add( "berachot/birkat_ammazon/11_arachamans.txt" );

		if ( isShabbat || isRoshChodesh || isYomTov || isCholHaMoed ) {
			files.add( "berachot/birkat_ammazon/12_conclusion_shabbat_rosh_hodesh_festivals.txt" );
		}
		else {
			files.add( "berachot/birkat_ammazon/12_conclusion_weekday.txt" );
		}		
		
		return files;
	}
	
	
} // End of BirkatAmmazon class
