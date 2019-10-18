/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings( "AutoBoxing" )
public class ZmanimBaseTest {
  protected static final ZmanimLocation                       ezraBessaroth;

  protected static final ZmanimLocation                       yeshivaUniviersity;

  protected static final ZmanimLocation                       haRambanSynagogue;

  protected static final List<Zmanim.Type>                    dailyZmanim = new ArrayList<>();

  protected static final List<Zmanim.Type>                    erevShabbatZmanim = new ArrayList<>();

  protected static final List<Zmanim.Type>                    shabbatZmanim = new ArrayList<>();

  protected static final List<Zmanim.Type>                    fastDayZmanim = new ArrayList<>();

  protected static final List<Zmanim.Type>                    erevPesachZmanim = new ArrayList<>();

  protected static final List<Zmanim.Type>                    erevPesachOnShabbatZmanim = new ArrayList<>();

  protected static final List<Zmanim.Type>                    yomSheniZemanim = new ArrayList<>();

  static {

    ezraBessaroth = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .locationName( "Ezra Bessaroth" )
      .atElevation( 61 )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    yeshivaUniviersity = new ZmanimLocation.Builder()
      .atLatitude( 40.851546 )
      .atLongitude( -73.928937 )
      .atElevation( 56 )
      .locationName( "Yeshiva University" )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .build();

    haRambanSynagogue = new ZmanimLocation.Builder()
      .atLatitude( 31.7750 )
      .atLongitude( 35.2312 )
      .atElevation( 767.49 )
      .locationName( "HaRamban Synagogue" )
      .inTimeZone( TimeZone.getTimeZone( "Asia/Jerusalem" ) )
      .build();

    Collections.addAll( dailyZmanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,        Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,      Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
      Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
      Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
    );

    Collections.addAll( erevShabbatZmanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,            Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,          Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
      Zmanim.Type.PlagHaMincha,     Zmanim.Type.CandleLighting,   Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha,
      Zmanim.Type.BeinHashmashot,   Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
    );

    Collections.addAll( shabbatZmanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,        Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,      Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
      Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
      Zmanim.Type.TzaitHacochavim,  Zmanim.Type.ShabbatEnd,   Zmanim.Type.NightChatzot
    );

    Collections.addAll( fastDayZmanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,            Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,          Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
      Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
      Zmanim.Type.FastEnd,          Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
    );

    Collections.addAll( erevPesachZmanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,                  Zmanim.Type.Hanetz,             Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.SofZmanAchilatChametz,  Zmanim.Type.SofZmanBiurChametz, Zmanim.Type.Chatzot,
      Zmanim.Type.MinchaGedola,     Zmanim.Type.MinchaKetana,           Zmanim.Type.PlagHaMincha,       Zmanim.Type.CandleLighting,
      Zmanim.Type.Shkia,            Zmanim.Type.LatestMincha,           Zmanim.Type.BeinHashmashot,     Zmanim.Type.FastEnd,
      Zmanim.Type.TzaitHacochavim,  Zmanim.Type.NightChatzot
    );

    Collections.addAll( erevPesachOnShabbatZmanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,        Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,      Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
      Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,        Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
      Zmanim.Type.TzaitHacochavim,  Zmanim.Type.YomTovSheniCandleLighting,              Zmanim.Type.NightChatzot
    );

    Collections.addAll( yomSheniZemanim,
      Zmanim.Type.Alot,             Zmanim.Type.Talit,            Zmanim.Type.Hanetz,       Zmanim.Type.SofZmanShema,
      Zmanim.Type.SofZmanTefila,    Zmanim.Type.Chatzot,          Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
      Zmanim.Type.PlagHaMincha,     Zmanim.Type.Shkia,            Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot,
      Zmanim.Type.TzaitHacochavim,  Zmanim.Type.YomTovSheniCandleLighting,                  Zmanim.Type.NightChatzot
    );
  }


  protected final void verifyZmanim(
    ZmanimLocation    location,
    HebrewDate        date,
    List<Zmanim.Type> expectedZmanim,
    String            expected
  ) {

    List<Zmanim> zmanimForDay = date.getZmanim( location );

    List<Zmanim.Type> day = new ArrayList<>();
    for ( Zmanim zmanim : zmanimForDay ) {
      day.add( zmanim.type );
    }
    Assert.assertEquals( expectedZmanim.toString(), day.toString() );

    StringBuilder output = new StringBuilder();
    output.append( location.getLocationName() ).append( "\n" );
    for ( Zmanim zmanim : zmanimForDay ) {
      for ( Zman zman : zmanim.asList() ) {
        Calendar zmanTime = zman.getTime();
        if ( zmanTime != null ) {
          output.append( String.format( "%20s - %-30s: %2d:%02d:%02d\n", zmanim.type.name, zman.name,
            zmanTime.get( Calendar.HOUR_OF_DAY ), zmanTime.get( Calendar.MINUTE ), zmanTime.get( Calendar.SECOND ) ) );
        }
      }
    }

    Assert.assertEquals( expected, output.toString() );
  }
}
