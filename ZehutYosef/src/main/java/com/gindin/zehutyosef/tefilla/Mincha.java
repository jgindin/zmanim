package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class Mincha
	extends Tefilla {

	@Override
	public List<TextInfo> getTextInfoForDate( final HebrewDate date ) {

		final boolean isFriday = ( date.getDayOfWeek() == Calendar.FRIDAY );
		final boolean isAseretYameiTeshuva = isAseretYameiTeshuva( date );

		List<TextInfo> files = new ArrayList<>();

		// Assembling Mincha:
		files.add( new TextInfo( "tefilla/mincha/mincha_intro.txt" ) );
		files.add( new TextInfo( "tefilla/pitum_haketoret.txt" ) );

		if ( isAseretYameiTeshuva ) {
			files.add( new TextInfo( "tefilla/pitum_haketoret_turk_asert_yamei_teshuva.txt" ) );
		}

		files.add( new TextInfo( "tefilla/pitum_haketoret_conclusion.txt" ) );
		files.add( new TextInfo( "tefilla/ashre.txt" ) );

		files.add( new TextInfo( "tefilla/amidah/1_ticon_tefilati.txt" ) );
		files.add( new TextInfo( "tefilla/kaddish_chatzi.txt" ) );

		files.addAll( getAmidahFileNamesForDate( date, Type.Mincha ) );

		if ( isAseretYameiTeshuva ) {
			files.add( new TextInfo( "tefilla/avinu_malkenu.txt" ) );
		}

		if ( doWeSayTachanun( date ) ) {
			files.add( new TextInfo( "tefilla/mincha/mincha_tachanun.txt" ) );
		}

		files.add( new TextInfo( "tefilla/amonai_adonenu.txt" ) );

		files.add( new TextInfo( "tefilla/kaddish_titkabal.txt" ) );

		if ( isFriday ) {
			files.add( new TextInfo( "tefilla/amonai_malach_geut_lavesh.txt" ) );
		}
		else {
			files.add( new TextInfo( "tefilla/lamnatzeach_binginot.txt" ) );
		}

		files.add( new TextInfo( "tefilla/kaddish_yehe_shlama_raba.txt" ) );
		files.add( new TextInfo( "tefilla/alenu.txt" ) );
		files.add( new TextInfo( "tefilla/uvtoratecha.txt" ) );

		if ( isFriday ) {
			files.add( new TextInfo( "tefilla/mincha/mincha_conclusion_friday.txt" ) );
		}
		else {
			files.add( new TextInfo( "tefilla/mincha/mincha_conclusion.txt" ) );
		}


		return files;
	}

} // End of Mincha class
