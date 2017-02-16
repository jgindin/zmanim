package com.gindin.zmanlib;

import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

/**
 * Allows access to info about a given zman.
 */
public interface ZmanCallback {

  /**
   * Used in conjunction with #findNextZman to allow control over which of the various zmanim for the given
   *  type is preferred by the user.
   *
   * @return For the given type, what is the name of the zman the user prefers to use? May be null to indicate
   *  the default is to be used.
   *  E.g,. for the Zmanim.Type.ShabbatEnd the user may prefer "Seattle Sephardic".
   */
  @Nullable
  String getPreferredZman( Zmanim.Type type );


  /**
   * @return For the given type, how far in advance (in minutes) does the user want to be notified? May be less than
   *  zero to indicate no notification.
   */
  int howManyMinutesToAlertBefore(
    Zmanim.Type type
  );

  /**
   * @return true if the user wants any notifications; false if they've been turned off.
   */
  boolean shouldShowAnyNotifications();

  /**
   * @return true if the user has indicated they want notifications even on Shabbat & Yom Tov.
   */
  boolean shouldNotifyOnShabbat();

  /**
   * Check if a notification should actually be displayed right now.
   */
  boolean shouldActuallyNotify( ZmanimLocation location );


  /**
   * Has the user been notified of the specified zman for the specified date?
   *
   * @return true if notification has happened; false otherwise.
   */
  boolean hasBeenNotified(
    Calendar  now,
    Zmanim.Type type
  );

  /**
   * Indicate the handling of the specified zman has been completed.
   */
  void completed(
    Calendar    now,
    Zmanim.Type type
  );


  /**
   * Schedule an alert for the specified zman.
   *
   * @param zmanNotification Which zman and how long from now (in milliseconds) until the alert
   *  should be raised.
   * @param location The current location (as determined when the zmanim were updated).
   * @param now The current date & time, with the correct time zone (based on the location).
   */
  void scheduleAlert(
    ZmanNotification  zmanNotification,
    ZmanimLocation    location,
    Calendar          now
  );


  /**
   * @return true if the user wants to see the zman times formatted with seconds. If false, an
   *  appropriate rounding algorithm will be used.
   */
  boolean formatTimeWithSeconds();



  class Adapter
    implements ZmanCallback {

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
    public boolean shouldActuallyNotify( ZmanimLocation location ) {
      return shouldShowAnyNotifications() && shouldNotifyOnShabbat();
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
      ZmanNotification  zmanNotification,
      ZmanimLocation    location,
      Calendar          now
    ) {
    }


    @Override
    public boolean formatTimeWithSeconds() {
      return false;
    }

  }
}
