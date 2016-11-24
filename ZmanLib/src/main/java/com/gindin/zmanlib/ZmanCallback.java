package com.gindin.zmanlib;

import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
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
   * @return true if the user has indicated they want notifications even on Shabbat & Yom Tov.
   */
	boolean shouldNotifyOnShabbat();

	/**
	 * Has the user been notified of the specified zman for the specified date?
	 *
	 * @return true if notification has happened; false otherwise.
	 */
	boolean hasBeenNotified(
		Calendar  now,
		Zman      zman
	);


	/**
	 * The user should be notified about the specified zman.
	 */
	void notifyUser(
    ZmanimLocation  location,
    Calendar        now,
    Zman            nextZman
  );

	/**
	 * Requests that the next check should be scheduled.
	 *
	 * @param now The current time.
	 * @param timeUntilNextCheck How long from now until the next check is to be scheduled.
	 */
	void scheduleNextCheck(
		Calendar  now,
		int       timeUntilNextCheck
	);



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
    public boolean shouldNotifyOnShabbat() {
      return true;
    }


    @Override
		public boolean hasBeenNotified(
			Calendar  now,
			Zman      zman
		) {
			return false;
		}


    @Override
		public void notifyUser(
      ZmanimLocation  location,
      Calendar        now,
      Zman            nextZman
    ) {
		}


		@Override
		public void scheduleNextCheck(
			final Calendar  now,
			final int       timeUntilNextCheck
		) {
		}
  }
}
