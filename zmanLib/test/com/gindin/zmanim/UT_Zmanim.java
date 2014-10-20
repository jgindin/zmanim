/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim;

import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.Zmanim;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Spot-checks some dates to verify the expected zmanim are present and properly calculated.
 */
@SuppressWarnings( { "MagicNumber", "AutoBoxing", "SpellCheckingInspection" } )
public class UT_Zmanim {

	private static ZmanimLocation                             ezraBessaroth;

	private static ZmanimLocation                             yeshivaUniviersity;

	private static ZmanimLocation                             haRambanSynagogue;


	@BeforeClass
	public static void setup() {

		ezraBessaroth = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		yeshivaUniviersity = new ZmanimLocation.Builder()
			.atLatitude( 40.851546 )
			.atLongitude( -73.928937 )
			.locationName( "Yeshiva University" )
			.inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
			.build();

		haRambanSynagogue = new ZmanimLocation.Builder()
			.atLatitude( 31.7750 )
			.atLongitude( 35.2312 )
			.locationName( "HaRamban Synagogue" )
			.inTimeZone( TimeZone.getTimeZone( "Asia/Jerusalem" ) )
			.build();
	}


	@Test
	public void october_19_2014() {

		final String ezraBessarothExpected =
			"Ezra Bessaroth\n" +
			"      Alot Hashachar:  6:02:11\n" +
			"       Talit/Tefilin:  6:37:10\n" +
			"              Hanetz:  7:33:26\n" +
			"      Sof Zman Shema:  9:27:55\n" +
			"     Sof Zman Tefila: 11:06:54\n" +
			"             Chatzot: 12:53:38\n" +
			"       Mincha Gedola: 13:20:20\n" +
			"       Mincha Ketana: 16:00:26\n" +
			"       Plag HaMincha: 17:07:09\n" +
			"               Shkia: 18:13:51\n" +
			"       Latest Mincha: 18:25:52\n" +
			"     Bein Hashmashot: 18:45:19\n" +
			"    Tzait Hacochavim: 19:00:02\n" +
			"            Midnight:  0:54:22\n";

		final String yeshivaUniversityExpected =
			"Yeshiva University\n" +
			"      Alot Hashachar:  2:49:48\n" +
			"       Talit/Tefilin:  3:21:01\n" +
			"              Hanetz:  4:11:08\n" +
			"      Sof Zman Shema:  6:15:06\n" +
			"     Sof Zman Tefila:  7:50:39\n" +
			"             Chatzot:  9:40:24\n" +
			"       Mincha Gedola: 10:07:50\n" +
			"       Mincha Ketana: 12:52:28\n" +
			"       Plag HaMincha: 14:01:04\n" +
			"               Shkia: 15:09:40\n" +
			"       Latest Mincha: 15:22:01\n" +
			"     Bein Hashmashot: 15:37:42\n" +
			"    Tzait Hacochavim: 15:50:47\n" +
			"            Midnight: 21:40:57\n";

		final String haRambanSynagogueExpected =
			"HaRamban Synagogue\n" +
			"      Alot Hashachar: 19:32:29\n" +
			"       Talit/Tefilin: 20:00:18\n" +
			"              Hanetz: 20:44:51\n" +
			"      Sof Zman Shema: 22:58:12\n" +
			"     Sof Zman Tefila:  0:30:53\n" +
			"             Chatzot:  2:23:54\n" +
			"       Mincha Gedola:  2:52:10\n" +
			"       Mincha Ketana:  5:41:42\n" +
			"       Plag HaMincha:  6:52:20\n" +
			"               Shkia:  8:02:58\n" +
			"       Latest Mincha:  8:15:41\n" +
			"     Bein Hashmashot:  8:27:55\n" +
			"    Tzait Hacochavim:  8:39:30\n" +
			"            Midnight: 14:24:17\n";

		HebrewDate date = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 19 );
		verifyDailyZmanim( ezraBessaroth, date, ezraBessarothExpected );
		verifyDailyZmanim( yeshivaUniviersity, date, yeshivaUniversityExpected );
		verifyDailyZmanim( haRambanSynagogue, date, haRambanSynagogueExpected );
	}


	private void verifyDailyZmanim(
		ZmanimLocation  location,
		HebrewDate      date,
		String          expected
	) {

		List<Zmanim> zmanimForDay = date.getZmanim( location );

		Assert.assertEquals( 14, zmanimForDay.size() );
		Assert.assertEquals( Zmanim.Type.Alot, zmanimForDay.get( 0 ).type );
		Assert.assertEquals( Zmanim.Type.Talit, zmanimForDay.get( 1 ).type );
		Assert.assertEquals( Zmanim.Type.Hanetz, zmanimForDay.get( 2 ).type );
		Assert.assertEquals( Zmanim.Type.SofZmanShema, zmanimForDay.get( 3 ).type );
		Assert.assertEquals( Zmanim.Type.SofZmanTefila, zmanimForDay.get( 4 ).type );
		Assert.assertEquals( Zmanim.Type.Chatzot, zmanimForDay.get( 5 ).type );
		Assert.assertEquals( Zmanim.Type.MinchaGedola, zmanimForDay.get( 6 ).type );
		Assert.assertEquals( Zmanim.Type.MinchaKetana, zmanimForDay.get( 7 ).type );
		Assert.assertEquals( Zmanim.Type.PlagHaMincha, zmanimForDay.get( 8 ).type );
		Assert.assertEquals( Zmanim.Type.Shkia, zmanimForDay.get( 9 ).type );
		Assert.assertEquals( Zmanim.Type.LatestMincha, zmanimForDay.get( 10 ).type );
		Assert.assertEquals( Zmanim.Type.BeinHashmashot, zmanimForDay.get( 11 ).type );
		Assert.assertEquals( Zmanim.Type.TzaitHacochavim, zmanimForDay.get( 12 ).type );
		Assert.assertEquals( Zmanim.Type.NightChatzot, zmanimForDay.get( 13 ).type );

		StringBuilder output = new StringBuilder();
		output.append( location.getLocationName() ).append( "\n" );
		for ( Zmanim zmanim : zmanimForDay ) {
			Date zmanTime = zmanim.getDefault().getTime();
			output.append( String.format( "%20s: %2d:%02d:%02d\n", zmanim.type.name, zmanTime.getHours(), zmanTime.getMinutes(), zmanTime.getSeconds() ) );
		}

		Assert.assertEquals( expected, output.toString() );
	}


} // End of UT_Zmanim class
