/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;
import com.gindin.zmanlib.zman.Zmanim;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Pesach
  extends Observance {

  private static final String PESACH = "Pesach";


  @Override
  public String getName() {
    return PESACH;
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    final int hebrewMonth = date.getHebrewMonth();

    if ( HebrewMonth.NISSAN != hebrewMonth ) {
      return null;
    }

    final int hebrewDayOfMonth = date.getHebrewDayOfMonth();
    switch ( hebrewDayOfMonth ) {
      case 14:
        return new ErevPesachDetails( date );

      case 15:
        return new PesachYomTovDetails( date, true );

      case 16:
        return new PesachYomTovDetails( date, false );

      case 17:
      case 18:
      case 19:
        return new PesachCholHaMoedDetails( date, hebrewDayOfMonth - 14, false );

      case 20:
        return new PesachCholHaMoedDetails( date, hebrewDayOfMonth - 14, true );

      case 21:
        return new PesachYomTovDetails( date, true );

      case 22:
        return new PesachYomTovDetails( date, false );
    }

    return null;
  }


  protected static class ErevPesachDetails
    extends Details.ErevYomTov {

    
    ErevPesachDetails(
      HebrewDate  date
    ) {
      super( date, PESACH );
    }


    @Override
    public boolean isPesach() {
      return true;
    }


    @Override
    public List<Zmanim.Type> getZmanim() {

      List<Zmanim.Type> zmanim = new ArrayList<>();
      zmanim.add( Zmanim.Type.Alot );
      zmanim.add( Zmanim.Type.Talit );
      zmanim.add( Zmanim.Type.Hanetz );
      zmanim.add( Zmanim.Type.SofZmanShema );
      zmanim.add( Zmanim.Type.SofZmanTefila );
      zmanim.add( Zmanim.Type.SofZmanAchilatChametz );

      // When erev Pesach falls out on Shabbat, then we cannot do Biur. Technically, the time still exists, but
      //  it's not quite the same...
      if ( !isShabbat() ) {
        zmanim.add( Zmanim.Type.SofZmanBiurChametz );
      }

      zmanim.add( Zmanim.Type.Chatzot );
      zmanim.add( Zmanim.Type.MinchaGedola );
      zmanim.add( Zmanim.Type.MinchaKetana );
      zmanim.add( Zmanim.Type.PlagHaMincha );

      if ( lightCandles() ) {
        zmanim.add( Zmanim.Type.CandleLighting );
      }

      zmanim.add( Zmanim.Type.Shkia );
      zmanim.add( Zmanim.Type.LatestMincha );
      zmanim.add( Zmanim.Type.BeinHashmashot );
      zmanim.add( Zmanim.Type.FastEnd );
      zmanim.add( Zmanim.Type.TzaitHacochavim );
      zmanim.add( Zmanim.Type.NightChatzot );

      return zmanim;
    }
  }


  protected static class PesachYomTovDetails
    extends Details.YomTovDetails {

    PesachYomTovDetails(
      HebrewDate  date,
      boolean     lightCandles
    ) {
      super( date, PESACH, lightCandles );
    }


    @Override
    public boolean isPesach() {
      return true;
    }
  }


  protected static class PesachCholHaMoedDetails
    extends Details.CholHaMoedDetails {

    PesachCholHaMoedDetails(
      HebrewDate  date,
      int         dayOfChag,
      boolean     lightCandles
    ) {
      super( date, dayOfChag, PESACH, lightCandles );
    }


    @Override
    public boolean isPesach() {
      return true;
    }
  }

}  // End of Pesach class
