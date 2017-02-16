/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Sukkot
  extends Observance {

  private static final String                               SUKKOT = "Sukkot";
  private static final String                               HOSHANA_RABBA = "Hoshana Rabba";
  private static final String                               SHMINI_ATZERET = "Shmini Atzeret";
  private static final String                               SIMCHAT_TORAH = "Simchat Torah";



  @Override
  public String getName() {
    return SUKKOT;
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( HebrewMonth.TISHREI == date.getHebrewMonth() ) {
      final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

      switch ( hebrewDayOfMonth ) {
        case 14:
          return new Details.ErevYomTov( date, SUKKOT );

        case 15:
          return new SukkotYomTovDetails( date, true );

        case 16:
          return new SukkotYomTovDetails( date, false );

        case 17:
        case 18:
        case 19:
        case 20:
          return new SukkotCholHaModeDetails( date, hebrewDayOfMonth - 14 );

        case 21:
          return new HoshanaRabbaDetails( date );

        case 22:
          return new ShminiAtzeretDetails( date );

        case 23:
          return new Details.YomTovDetails( date, SIMCHAT_TORAH, false );
      }
    }

    return null;
  }


  private static class SukkotYomTovDetails
    extends Details.YomTovDetails {

    SukkotYomTovDetails(
      final HebrewDate  date,
      final boolean     lightCandles
    ) {
      super( date, SUKKOT, lightCandles );
    }


    @Override
    public boolean isSukkot() {
      return true;
    }
  }


  private static class SukkotCholHaModeDetails
    extends Details.CholHaMoedDetails {

    SukkotCholHaModeDetails(
      final HebrewDate  date,
      final int         dayOfChag
    ) {
      super( date, dayOfChag, SUKKOT, false );
    }


    @Override
    public boolean isSukkot() {
      return true;
    }
  }


  private static class HoshanaRabbaDetails
    extends Details.ErevYomTov {

    HoshanaRabbaDetails( HebrewDate date ) {
      super( date, HOSHANA_RABBA );
    }


    @Override
    public boolean isSukkot() {
      return true;
    }


    @Override
    public String getDescription() {
      return SUKKOT + " 7 (" + HOSHANA_RABBA + ")";
    }
  }


  private static class ShminiAtzeretDetails
    extends Details.YomTovDetails {

    public ShminiAtzeretDetails(
      final HebrewDate date
    ) {
      super( date, SHMINI_ATZERET, true );
    }


    @Override
    public boolean isShminiAtzeret() {
      return true;
    }
  }


  private static class SimchatTorahDetails
    extends Details.YomTovDetails {

    SimchatTorahDetails(
      final HebrewDate date
    ) {
      super( date, SIMCHAT_TORAH, false );
    }


    @Override
    public boolean isSimchatTorah() {
      return true;
    }
  }

}  // End of Sukkot class
