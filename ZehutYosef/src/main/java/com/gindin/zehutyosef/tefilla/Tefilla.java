package com.gindin.zehutyosef.tefilla;

import com.gindin.zehutyosef.DetermineText;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Separate out the details of which chunks to load so that we can easliy unit test.
 */
@SuppressWarnings( "MagicNumber" )
public abstract class Tefilla
	implements DetermineText {

	@SuppressWarnings( "SpellCheckingInspection" )
	enum Type {
		Shachrit,
		Musaf,
		Mincha,
		Arvit
	}



	@SuppressWarnings( "FeatureEnvy" )
	protected final List<String> getAmidahFileNamesForDate( HebrewDate date, Type tefillaType ) {

		final Details dateDetails = date.getDetails();
		final boolean isFastDay = dateDetails.isTaanit();
		final boolean isAseretYameiTeshuva = isAseretYameiTeshuva( date );

		List<String> files = new ArrayList<>();

		// Assembling Amidah:
		files.add( "tefilla/amidah/2_1_magen_avraham_intro.txt" );

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/amidah/2_2_magen_avraham_asert_yamei_teshuva.txt" );
		}

		files.add( "tefilla/amidah/2_3_magen_avraham_conclusion.txt" );
		files.add( "tefilla/amidah/3_1_atah_gibor_intro.txt" );

		if ( doWeSayMoridHaTal( date, tefillaType ) ) {
			files.add( "tefilla/amidah/3_2_atah_gibor_summer.txt" );
		}
		else {
			files.add( "tefilla/amidah/3_2_atah_gibor_winter.txt");
		}

		files.add( "tefilla/amidah/3_3_atah_gibor_middle.txt" );

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/amidah/3_4_atah_gibor_asert_yamei_teshuva.txt" );
		}

		files.add( "tefilla/amidah/3_5_atah_gibor_conclusion.txt" );

		if ( ( tefillaType == Type.Shachrit ) || ( tefillaType == Type.Mincha ) ) {
			files.add( "tefilla/amidah/4_1_kedusha_nakdishach.txt" );
		}

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/amidah/4_2_kedusha_asert_yamei_teshuva.txt" );
		}
		else {
			files.add( "tefilla/amidah/4_2_kedusha.txt" );
		}

		files.add( "tefilla/amidah/5_atah_honen_thru_goel_yisrael.txt" );

		if ( isFastDay ) {
			files.add( "tefilla/amidah/6_fast_day_hazzan_insertion.txt" );
		}

		files.add( "tefilla/amidah/7_refaenu.txt" );

		if ( doWeSayBarechenu( date, tefillaType ) ) {
			files.add( "tefilla/amidah/8_barechenu.txt" );
		}
		else {
			files.add( "tefilla/amidah/8_barech_alenu.txt");
		}

		files.add( "tefilla/amidah/9_tikah_bshofar.txt" );
		files.add( "tefilla/amidah/10_hashiva.txt" );

		if ( isAseretYameiTeshuva ) {
			files.add( "amidah/10_hashiva_asert_yamei_teshuva.txt" );
		}

		files.add( "tefilla/amidah/11_lamalshinim_thru_et_zemach.txt" );
		files.add( "tefilla/amidah/12_1_shma_kolenu_intro.txt" );

		if ( isFastDay ) {
			files.add( "tefilla/amidah/12_2_shma_kolenu_fast_day.txt" );
		}

		files.add( "tefilla/amidah/12_3_shma_kolenu_conclusion.txt" );
		files.add( "tefilla/amidah/13_1_retzeh_intro.txt" );

		if ( dateDetails.isRoshChodesh() ) {
			files.add( "tefilla/amidah/13_2_retzeh_rosh_chodesh.txt" );
		}

		files.add( "tefilla/amidah/13_3_retzeh_conclusion.txt" );
		files.add( "tefilla/amidah/14_1_modim_intro.txt" );

		if ( tefillaType != Type.Arvit ) {
			files.add( "tefilla/amidah/14_1_modim_drabanan.txt" );
		}

		if ( dateDetails.isChanukah() ) {
			files.add( "tefilla/amidah/14_2_modim_chanukah.txt" );
		}
		else if ( dateDetails.isPurim() ) {
			files.add( "tefilla/amidah/14_2_modim_purim.txt" );
		}

		files.add( "tefilla/amidah/14_3_modim_middle.txt" );

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/amidah/14_4_modim_asert_yamei_teshuva.txt" );
		}

		files.add( "tefilla/amidah/14_5_modim_conclusion.txt" );
//		files.add( "tefilla/amidah/15_birkat_cohanim NEED THIS.txt" );
		files.add( "tefilla/amidah/16_1_sim_shalom_intro.txt" );

		if ( isAseretYameiTeshuva ) {
			files.add( "tefilla/amidah/16_2_sim_shalom_asert_yamei_teshuva.txt" );
		}

		files.add( "tefilla/amidah/16_3_sim_shalom_conclusion.txt" );

		if ( tefillaType != Type.Arvit ) {
			files.add( "tefilla/amidah/17_end_of_amidah_instructions.txt" );
		}

		files.add( "tefilla/amidah/17_end_of_amidah.txt" );

		return files;
	}


	@SuppressWarnings( "FeatureEnvy" )
	protected final boolean doWeSayBarechenu(
		HebrewDate  date,
		Type        tefillaType
	) {

		HebrewDate nextYear = HebrewCalendar.getForDate( date.getGregorianYear() + 1, Calendar.JANUARY, 1 );
		final boolean isLeapYear = nextYear.isGregorianLeapYear();

		HebrewDate startOfSummer = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.NISSAN, 16 );
		HebrewDate endOfSummer = HebrewCalendar.getForDate( date.getGregorianYear(), Calendar.DECEMBER, isLeapYear ? 5 : 4 );

		final boolean barechenu;
		if ( date.compareTo( startOfSummer ) >= 0 ) {
			// If we're saying Arvit, then we must be strictly before the end of summer. For Shachrit or Mincha,
			//  it could be that day...
			if ( tefillaType == Type.Arvit ) {
				barechenu = ( date.compareTo( endOfSummer ) < 0 );
			}
			else {
				barechenu = ( date.compareTo( endOfSummer ) <= 0 );
			}
		}
		else {
			barechenu = false;
		}

		return barechenu;
	}


	@SuppressWarnings( "FeatureEnvy" )
	protected final boolean doWeSayMoridHaTal(
		HebrewDate  date,
		Type        tefillaType
	) {

		HebrewDate firstDayOfPesach = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.NISSAN, 15 );

		// If this year is before the last day of Elul, then we have to check against *next year's* shmini Atzeret...
		HebrewDate shminiAtzeret;
		HebrewDate endOfYear = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.ELUL, 29 );
		if ( date.compareTo( endOfYear ) <= 0 ) {
			shminiAtzeret = HebrewCalendar.getForJewishDate( date.getHebrewYear() + 1, HebrewMonth.TISHREI, 22 );
		}
		else {
			shminiAtzeret = HebrewCalendar.getForJewishDate( date.getHebrewYear(), HebrewMonth.TISHREI, 22 );
		}


		if ( ( date.compareTo( firstDayOfPesach ) > 0 ) && ( date.compareTo( shminiAtzeret ) < 0 ) ) {
			return true;
		}

		// Now, check the edge cases...
		if ( date.compareTo( firstDayOfPesach ) == 0 ) {
			return ( tefillaType != Type.Shachrit );
		}

		if ( date.compareTo( shminiAtzeret ) == 0 ) {
			return ( tefillaType == Type.Shachrit );
		}

		return false;
	}


	protected final boolean isAseretYameiTeshuva( HebrewDate date ) {

		int hebrewMonth = date.getHebrewMonth();
		int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		return ( hebrewMonth == HebrewMonth.TISHREI ) && ( hebrewDayOfMonth >= 1 ) && ( hebrewDayOfMonth <= 10 );
	}


	@SuppressWarnings( "FeatureEnvy" )
	protected final boolean doWeSayTachanun( HebrewDate date ) {

		// See pg. 43 of Zehut Yosef.

		Details details = date.getDetails();
		if ( details.isShabbat() || details.isRoshChodesh() || details.isPesach() || details.isShavuot() || details.isSukkot() ) {
			return false;
		}

		int hebrewMonth = date.getHebrewMonth();
		if ( hebrewMonth == HebrewMonth.NISSAN ) {
			return false;
		}

		if ( details.isYomHaAtzmaut() || details.isPesachSheni() || details.isLagBOmer() || details.isYomYerushalayim() ) {
			return false;
		}

		int hebrewDayOfMonth = date.getHebrewDayOfMonth();
		if ( ( hebrewMonth == HebrewMonth.SIVAN ) && ( hebrewDayOfMonth <= 12 ) ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.AV ) && ( ( hebrewDayOfMonth == 9 ) || ( hebrewDayOfMonth == 15 ) ) ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.ELUL ) && ( hebrewDayOfMonth == 29 ) ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.TISHREI ) && ( hebrewDayOfMonth >= 9 ) ) {
			return false;
		}

		if ( details.isChanukah() ) {
			return false;
		}

		if ( ( hebrewMonth == HebrewMonth.SHEVAT ) && ( hebrewDayOfMonth == 15 ) ) {
			return false;
		}

		if ( date.isHebrewLeapYear() && ( hebrewMonth == HebrewMonth.ADAR ) && ( ( hebrewDayOfMonth == 14 ) || ( hebrewDayOfMonth == 15 ) ) ) {
			return false;
		}

		if ( details.isPurim() || details.isShushanPurim() ) {
			return false;
		}

		return true;
	}

} // End of MeenShalosh class
