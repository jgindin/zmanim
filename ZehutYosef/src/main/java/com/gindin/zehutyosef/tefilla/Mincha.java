package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.R;
import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class Mincha
	extends Tefilla {


	public Mincha() {
		super( R.string.mincha );
	}


	@Override
	public List<TextChunk> getTextInfoForDate( final HebrewDate date ) {

		final boolean isFriday = ( date.getDayOfWeek() == Calendar.FRIDAY );
		final boolean isAseretYameiTeshuva = isAseretYameiTeshuva( date );

		List<TextChunk> files = new ArrayList<>();

		// Assembling Mincha:
		files.add( new TextChunk( "tefilla/mincha/mincha_intro.txt" ) );
		files.add( new TextChunk( "tefilla/pitum_haketoret.txt" ) );

		if ( isAseretYameiTeshuva ) {
			files.add( new TextChunk( "tefilla/pitum_haketoret_turk_asert_yamei_teshuva.txt" ) );
		}

		files.add( new TextChunk( "tefilla/pitum_haketoret_conclusion.txt" ) );
		files.add( new TextChunk( "tefilla/ashre.txt" ) );

		files.add( new TextChunk( "tefilla/amidah/1_ticon_tefilati.txt" ) );
		files.add( new TextChunk( "tefilla/kaddish_chatzi.txt" ) );

		files.addAll( getAmidahFileNamesForDate( date, Type.Mincha ) );

		if ( isAseretYameiTeshuva ) {
			files.add( new TextChunk( "tefilla/avinu_malkenu.txt" ) );
		}

		if ( doWeSayTachanun( date ) ) {
			files.add( new TextChunk( "tefilla/mincha/mincha_tachanun.txt" ) );
		}

		files.add( new TextChunk( "tefilla/amonai_adonenu.txt" ) );

		files.add( new TextChunk( "tefilla/kaddish_titkabal.txt" ) );

		if ( isFriday ) {
			files.add( new TextChunk( "tefilla/amonai_malach_geut_lavesh.txt" ) );
		}
		else {
			files.add( new TextChunk( "tefilla/lamnatzeach_binginot.txt" ) );
		}

		files.add( new TextChunk( "tefilla/kaddish_yehe_shlama_raba.txt" ) );
		files.add( new TextChunk( "tefilla/alenu.txt" ) );
		files.add( new TextChunk( "tefilla/uvtoratecha.txt" ) );

		if ( isFriday ) {
			files.add( new TextChunk( "tefilla/mincha/mincha_conclusion_friday.txt" ) );
		}
		else {
			files.add( new TextChunk( "tefilla/mincha/mincha_conclusion.txt" ) );
		}


		return files;
	}

} // End of Mincha class
