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
	public List<String> getTextFilesForDate( final HebrewDate date ) {

		final boolean isFriday = ( date.getDayOfWeek() == Calendar.FRIDAY );
		final boolean isAseretYameiTeshuva = isAseretYameiTeshuva( date );

		List<String> files = new ArrayList<>();

		// Assembling Mincha:
		files.add( "tefilla/mincha/mincha_intro.txt" );
		files.add( "tefilla/pitum_haketoret.txt" );

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/pitum_haketoret_turk_asert_yamei_teshuva.txt" );
		}

		files.add( "tefilla/pitum_haketoret_conclusion.txt" );
		files.add( "tefilla/ashre.txt" );

		files.add( "tefilla/amidah/1_ticon_tefilati.txt" );
		files.add( "tefilla/kaddish_chatzi.txt" );

		files.addAll( getAmidahFileNamesForDate( date, Type.Mincha ) );

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/avinu_malkenu.txt" );
		}

		if ( doWeSayTachanun( date ) ) {
			files.add( "tefilla/mincha/mincha_tachanun.txt" );
		}

		files.add( "tefilla/amonai_adonenu.txt" );

		files.add( "tefilla/kaddish_titkabal.txt" );

		if ( isFriday ) {
			files.add( "tefilla/amonai_malach_geut_lavesh.txt" );
		}
		else {
			files.add( "tefilla/lamnatzeach_binginot.txt" );
		}

		files.add( "tefilla/kaddish_yehe_shlama_raba.txt" );
		files.add( "tefilla/alenu.txt" );
		files.add( "tefilla/uvtoratecha.txt" );

		if ( isFriday ) {
			files.add( "tefilla/mincha/mincha_conclusion_friday.txt" );
		}
		else {
			files.add( "tefilla/mincha/mincha_conclusion.txt" );
		}


		return files;
	}

} // End of Mincha class
