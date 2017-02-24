package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

/**
 * Allows access to info about a given zman.
 */
public abstract class ZmanCallback {

  /**
   * Used in conjunction with #findNextZman to allow control over which of the various zmanim for the given
   *  type is preferred by the user.
   *
   * @return For the given type, what is the name of the zman the user prefers to use? May be null to indicate
   *  the default is to be used.
   *  E.g,. for the Zmanim.Type.ShabbatEnd the user may prefer "Seattle Sephardic".
   */
  @Nullable
  public abstract String getPreferredZman( Zmanim.Type type );


  /**
   * @return For the given type, how far in advance (in minutes) does the user want to be notified? May be less than
   *  zero to indicate no notification.
   */
  public abstract int howManyMinutesToAlertBefore(
    Zmanim.Type type
  );

  /**
   * @return true if the user wants any notifications; false if they've been turned off.
   */
  public abstract boolean shouldShowAnyNotifications();

  /**
   * @return true if the user has indicated they want notifications even on Shabbat & Yom Tov.
   */
  public abstract boolean shouldNotifyOnShabbat();

  /**
   * Check if a notification should actually be displayed right now.
   */
  @SuppressWarnings( "FeatureEnvy" )
  public final boolean shouldActuallyNotify(
    final ZmanimLocation  location,
    final Calendar        now
  ) {

    if ( !shouldShowAnyNotifications() ) {
      return false;
    }

    if ( shouldNotifyOnShabbat() ) {
      return true;
    }

    now.setTimeZone( location.getTimeZone() );
    HebrewDate today = HebrewCalendar.getForDate( now );;

    Details todayDetails = today.getDetails();
    HebrewDate tomorrow = today.tomorrow();
    final Details tomorrowDetails = tomorrow.getDetails();

    // If today is erev something, and it's after candle lighting, then don't notify...
    if ( todayDetails.isErev() && today.isAfter( now, Zmanim.Type.CandleLighting, location ) ) {
      return false;
    }

    // If today is Shabbat, but it's after sunset, AND tomorrow's not a Yom Tov, then go ahead...
    if ( todayDetails.isShabbat() && today.isAfter( now, Zmanim.Type.ShabbatEnd, location ) && !tomorrowDetails.isYomTov() ) {
      return true;
    }

    // If today is YomTov, it's after sunset, and tomorrow's not a Yom Tov, then go ahead...
    if ( todayDetails.isYomTov() && today.isAfter( now, Zmanim.Type.ShabbatEnd, location ) && !tomorrowDetails.isYomTov() ) {
      return true;
    }

    return ( !todayDetails.isShabbat() && !todayDetails.isYomTov() );
  }


  /**
   * Has the user been notified of the specified zman for the specified date?
   *
   * @return true if notification has happened; false otherwise.
   */
  public abstract boolean hasBeenNotified(
    Calendar    now,
    Zmanim.Type type
  );

  /**
   * Indicate the handling of the specified zman has been completed.
   */
  public abstract void completed(
    Calendar    now,
    Zmanim.Type type
  );


  /**
   * Schedule an alert for the specified zman.
   * @param zman Which zman is the alert being scheduled for?
   * @param location The current location (as determined when the zmanim were updated).
   * @param alertAt The date & time (with the correct time zone, based on the location) for when the
   */
  public abstract void scheduleAlert(
    Zman            zman,
    ZmanimLocation  location,
    Calendar        alertAt
  );


  /**
   * @return true if the user wants to see the zman times formatted with seconds. If false, an
   *  appropriate rounding algorithm will be used.
   */
  public abstract boolean formatTimeWithSeconds();



  static class Adapter
    extends ZmanCallback {

    @Nullable
    @Override
    public String getPreferredZman( final Zmanim.Type type ) {
      return null;
    }


    @Override
    public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
      return ZmanNotificationScheduler.NO_ALERT;
    }


    @Override
    public boolean shouldShowAnyNotifications() {
      return true;
    }


    @Override
    public boolean shouldNotifyOnShabbat() {
      return true;
    }


    @Override
    public boolean hasBeenNotified(
      Calendar    now,
      Zmanim.Type type
    ) {
      return false;
    }


    @Override
    public void completed(
      Calendar    now,
      Zmanim.Type type
    ) {
    }


    @Override
    public void scheduleAlert(
      Zman            zman,
      ZmanimLocation  location,
      Calendar        alertAt
    ) {
    }


    @Override
    public boolean formatTimeWithSeconds() {
      return false;
    }

  }
}
