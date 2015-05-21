package com.gindin.zehutyosef.berachot;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.ArrayList;
import java.util.List;

/**
 * Separate out the details of which chunks to load so that we can easliy unit test.
 */
class MeenShalosh {

	@SuppressWarnings( "FeatureEnvy" )
	static List<String> getFileNamesForDate( HebrewDate date ) {

		final Details dateDetails = date.getDetails();
		
		List<String> files = new ArrayList<>();
	
		// Assembling the before / after blessings:
		//  1. meen_shalosh_before
		//  2. meen_shalosh_after_intro
		//  3. meen_shalosh_shabbat
		//  4. meen_shalosh_rosh_chodesh
		//  5. meen_shalosh_pesach or meen_shalosh_sukkot or meen_shalosh_shemini_atzeret_simchat_torah or meen_shalosh_shavuot
		//  6. meen_shalosh_conclusion
		//  7. other_before_and_after_blessings

		files.add( "berachot/other_before_and_after_food/1_meen_shalosh_before.txt" );
		files.add( "berachot/other_before_and_after_food/2_meen_shalosh_after_intro.txt" );

		if ( dateDetails.isShabbat() ) {
			files.add( "berachot/other_before_and_after_food/3_meen_shalosh_shabbat.txt" );
		}

		if ( dateDetails.isRoshChodesh() ) {
			files.add( "berachot/other_before_and_after_food/3_meen_shalosh_rosh_chodesh.txt" );
		}

		if ( dateDetails.isPesach() ) {
			files.add( "berachot/other_before_and_after_food/4_meen_shalosh_pesach.txt" );
		}
		else if ( dateDetails.isSukkot() ) {
			files.add( "berachot/other_before_and_after_food/4_meen_shalosh_sukkot.txt" );
		}
		else if ( dateDetails.isShminiAtzeret() ) {
			files.add( "berachot/other_before_and_after_food/4_meen_shalosh_shemini_atzeret_simchat_torah.txt" );
		}
		else if ( dateDetails.isShavuot() ) {
			files.add( "berachot/other_before_and_after_food/4_meen_shalosh_shavuot.txt" );
		}

		files.add( "berachot/other_before_and_after_food/5_meen_shalosh_conclusion.txt" );
		files.add( "berachot/other_before_and_after_food/6_other_before_and_after_blessings.txt" );
		
		return files;
	}
	
} // End of MeenShalosh class
