package com.gindin.zehutyosef.berachot;

import com.gindin.zehutyosef.TextViewerFragment;
import com.gindin.zmanlib.calendar.holiday.Details;

/**
 *
 */
public class MeenShaloshViewerFragment
	extends TextViewerFragment {


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	protected String loadText() {

		Details dateDetails = getDate().getDetails();

		// Assembling the before / after blessings:
		//  1. meen_shalosh_before
		//  2. meen_shalosh_after_intro
		//  3. meen_shalosh_shabbat
		//  4. meen_shalosh_rosh_chodesh
		//  5. meen_shalosh_pesach or meen_shalosh_sukkot or meen_shalosh_shemini_atzeret_simchat_torah or meen_shalosh_shavuot
		//  6. meen_shalosh_conclusion
		//  7. other_before_and_after_blessings

		String text =
			loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_before.txt" ) +
			loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_after_intro.txt" );

		if ( dateDetails.isShabbat() ) {
			text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_shabbat.txt" );
		}

		if ( dateDetails.isRoshChodesh() ) {
			text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_rosh_chodesh.txt" );
		}

		if ( dateDetails.isPesach() ) {
			text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_pesach.txt" );
		}
		else if ( dateDetails.isSukkot() ) {
			text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_sukkot.txt" );
		}
		else if ( dateDetails.isShminiAtzeret() ) {
			text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_shemini_atzeret_simchat_torah.txt" );
		}
		else if ( dateDetails.isShavuot() ) {
			text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_shavuot.txt" );
		}

		text += loadTextAsset( "berachot/other_before_and_after_food/meen_shalosh_conclusion.txt" );
		text += loadTextAsset( "berachot/other_before_and_after_food/other_before_and_after_blessings.txt" );

		return text;
	}

} // End of MeenShaloshViewerFragment class
