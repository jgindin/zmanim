package com.gindin.zmanlib;

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
	int getZmanAlertTime(
		Zmanim.Type type
	);

	/**
	 * The user should be notified about the specified zman.
	 */
	void notifyUser(
		Zman nextZman
	);

	/**
	 * Requests that the next check should be scheduled.
	 *
	 * @param now The current time.
	 * @param timeUntilNextCheck How long from now until the next check is to be scheduled.
	 * @param nextZman What is the next zman?
	 */
	void scheduleNextCheck(
		Calendar  now,
		int       timeUntilNextCheck,
		Zman      nextZman
	);


	class Adapter
		implements ZmanCallback {

		@Nullable
		@Override
		public String getPreferredZman( final Zmanim.Type type ) {
			return null;
		}


		@Override
		public int getZmanAlertTime( final Zmanim.Type type ) {
			return ZmanUtils.NO_ALERT;
		}


		@Override
		public void notifyUser( final Zman nextZman ) {
		}


		@Override
		public void scheduleNextCheck(
			final Calendar now,
			final int       timeUntilNextCheck,
			final Zman      nextZman
		) {
		}
	}
}
