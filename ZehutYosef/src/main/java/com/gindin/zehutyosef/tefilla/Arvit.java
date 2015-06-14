package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Arvit
	extends Tefilla {

	@Override
	public List<String> getTextFilesForDate( final HebrewDate date ) {

		final Details dateDetails = date.getDetails();
		final boolean isPurim = dateDetails.isPurim();
		final boolean isRoshChodesh = dateDetails.isRoshChodesh();

		List<String> files = new ArrayList<>();

		// Assembling Arvit:
		if ( isRoshChodesh ) {
			files.add( "tefilla/arvit/rosh_hodesh_intro.txt" );
		}

		if ( isPurim ) {
			files.add( "tefilla/arvit/purim_intro.txt" );
		}

		files.add( "tefilla/arvit/mourning_intro.txt" );
		files.add( "tefilla/arvit/utshuat_tzadikim.txt" );

		if ( !isRoshChodesh ) {
			files.add( "tefilla/arvit/amonai_tzevaot.txt" );
		}

		files.add( "tefilla/kaddish_chatzi.txt" );
		files.add( "tefilla/arvit/vehu_rachum.txt" );
		files.add( "tefilla/barekhu.txt" );
		files.add( "tefilla/arvit/arvit_shema_berakha_before.txt" );
		files.add( "tefilla/shema.txt" );
		files.add( "tefilla/arvit/arvit_shema_berakha_after.txt" );
		files.add( "tefilla/kaddish_chatzi.txt" );

		files.addAll( getAmidahFileNamesForDate( date, Type.Arvit ) );

		files.add( "tefilla/arvit/yehi_shem.txt" );
		files.add( "tefilla/kaddish_titkabal.txt" );
		files.add( "tefilla/arvit/shir_lamaalot.txt" );
		files.add( "tefilla/kaddish_yehe_shlama_raba.txt" );
		files.add( "tefilla/barekhu.txt" );
		files.add( "tefilla/alenu.txt" );

		// TODO: On Motzae Shabbat, we're done.
		files.add( "tefilla/uvtoratecha.txt" );
		files.add( "tefilla/ldavid_amonai_ori.txt" );
		files.add( "tefilla/kaddish_yehe_shlama_raba.txt" );
		files.add( "tefilla/arvit/arvit_conclusion.txt" );

		return files;
	}

} // End of Arvit class
