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
	public List<TextInfo> getTextInfoForDate( final HebrewDate date ) {

		final Details dateDetails = date.getDetails();
		final boolean isPurim = dateDetails.isPurim();
		final boolean isRoshChodesh = dateDetails.isRoshChodesh();

		List<TextInfo> files = new ArrayList<>();

		// Assembling Arvit:
		if ( isRoshChodesh ) {
			files.add( new TextInfo( "tefilla/arvit/rosh_hodesh_intro.txt" ) );
		}

		if ( isPurim ) {
			files.add( new TextInfo( "tefilla/arvit/purim_intro.txt" ) );
		}

		files.add( new TextInfo( "tefilla/arvit/mourning_intro.txt" ) );
		files.add( new TextInfo( "tefilla/arvit/utshuat_tzadikim.txt" ) );

		if ( !isRoshChodesh ) {
			files.add( new TextInfo( "tefilla/arvit/amonai_tzevaot.txt" ) );
		}

		files.add( new TextInfo( "tefilla/kaddish_chatzi.txt" ) );
		files.add( new TextInfo( "tefilla/arvit/vehu_rachum.txt" ) );
		files.add( new TextInfo( "tefilla/barekhu.txt" ) );
		files.add( new TextInfo( "tefilla/arvit/arvit_shema_berakha_before.txt" ) );
		files.add( new TextInfo( "tefilla/shema.txt" ) );
		files.add( new TextInfo( "tefilla/arvit/arvit_shema_berakha_after.txt" ) );
		files.add( new TextInfo( "tefilla/kaddish_chatzi.txt" ) );

		files.addAll( getAmidahFileNamesForDate( date, Type.Arvit ) );

		files.add( new TextInfo( "tefilla/arvit/yehi_shem.txt" ) );
		files.add( new TextInfo( "tefilla/kaddish_titkabal.txt" ) );
		files.add( new TextInfo( "tefilla/arvit/shir_lamaalot.txt" ) );
		files.add( new TextInfo( "tefilla/kaddish_yehe_shlama_raba.txt" ) );
		files.add( new TextInfo( "tefilla/barekhu.txt" ) );
		files.add( new TextInfo( "tefilla/alenu.txt" ) );

		// TODO: On Motzae Shabbat, we're done.
		files.add( new TextInfo( "tefilla/uvtoratecha.txt" ) );
		files.add( new TextInfo( "tefilla/ldavid_amonai_ori.txt" ) );
		files.add( new TextInfo( "tefilla/kaddish_yehe_shlama_raba.txt" ) );
		files.add( new TextInfo( "tefilla/arvit/arvit_conclusion.txt" ) );

		return files;
	}

} // End of Arvit class
