package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.R;
import com.gindin.zmanlib.calendar.HebrewDate;

import android.content.res.AssetManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Special section because when you view Arvit, you're actually viewing the next day.
 */
public class Arvit
	extends Tefilla {


	public Arvit() {
		super( R.string.arvit );
	}


	@Override
	public void load(
		final HebrewDate    date,
		final AssetManager  assets,
		final TextView      textContent
	) {
		super.load( date.tomorrow(), assets, textContent );
	}


	@Override
	protected TefillaType getTefillaType() {
		return TefillaType.Arvit;
	}


	@Override
	protected List<TextChunk> getTefillaText( final DoWeSay doWeSay ) {

		List<TextChunk> files = new ArrayList<>();

		// Assembling Arvit:
		if ( doWeSay.roshChodeshAdditions() ) {
			files.add( new TextChunk( "tefilla/arvit/rosh_hodesh_intro.txt" ) );
		}

		if ( doWeSay.purimAdditions() ) {
			files.add( new TextChunk( "tefilla/arvit/purim_intro.txt" ) );
		}

		files.add( new TextChunk( "tefilla/arvit/mourning_intro.txt" ) );
		files.add( new TextChunk( "tefilla/arvit/utshuat_tzadikim.txt" ) );

		if ( !doWeSay.roshChodeshAdditions() ) {
			files.add( new TextChunk( "tefilla/arvit/amonai_tzevaot.txt" ) );
		}

		files.add( new TextChunk( "tefilla/kaddish_chatzi.txt" ) );
		files.add( new TextChunk( "tefilla/arvit/vehu_rachum.txt" ) );
		files.add( new TextChunk( "tefilla/barekhu.txt" ) );
		files.add( new TextChunk( "tefilla/arvit/arvit_shema_berakha_before.txt" ) );
		files.add( new TextChunk( "tefilla/shema.txt" ) );
		files.add( new TextChunk( "tefilla/arvit/arvit_shema_berakha_after.txt" ) );
		files.add( new TextChunk( "tefilla/kaddish_chatzi.txt" ) );

		files.addAll( getAmidahFileNamesForDate( doWeSay ) );

		files.add( new TextChunk( "tefilla/arvit/yehi_shem.txt" ) );
		files.add( new TextChunk( "tefilla/kaddish_titkabal.txt" ) );
		files.add( new TextChunk( "tefilla/arvit/shir_lamaalot.txt" ) );
		files.add( new TextChunk( "tefilla/kaddish_yehe_shlama_raba.txt" ) );
		files.add( new TextChunk( "tefilla/barekhu.txt" ) );
		files.add( new TextChunk( "tefilla/alenu.txt" ) );

		// TODO: On Motzae Shabbat, we're done.
		files.add( new TextChunk( "tefilla/uvtoratecha.txt" ) );
		files.add( new TextChunk( "tefilla/ldavid_amonai_ori.txt" ) );
		files.add( new TextChunk( "tefilla/kaddish_yehe_shlama_raba.txt" ) );
		files.add( new TextChunk( "tefilla/arvit/arvit_conclusion.txt" ) );

		return files;
	}

} // End of Arvit class
