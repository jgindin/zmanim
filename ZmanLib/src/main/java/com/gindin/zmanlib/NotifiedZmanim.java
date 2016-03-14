package com.gindin.zmanlib;

import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps track of which zmanim we've already notified the user about for a given day.
 */
public final class NotifiedZmanim
	implements Serializable {

	/** The date this applies to. */
	private final int                                         year;
	private final int                                         month;
	private final int                                         date;

	/** Which of the zmanim types have already been notified? */
	private final Set<Zmanim.Type>                            notifiedTypes = new HashSet<>();


	public NotifiedZmanim(
		final Calendar day
	) {

		this.year = day.get( Calendar.YEAR );
		this.month = day.get( Calendar.MONTH );
		this.date = day.get( Calendar.DATE );
	}


	public boolean isForDate( final Calendar other ) {

		int otherYear = other.get( Calendar.YEAR );
		if ( year != otherYear ) {
			return false;
		}

		int otherMonth = other.get( Calendar.MONTH );
		if ( month != otherMonth ) {
			return false;
		}

		int otherDate = other.get( Calendar.DATE );
		return date == otherDate;
	}


	/**
	 * Once the user has been notified about the specified type, then this method
	 */
	void notified(
		final Zman zman
	) {

		notifiedTypes.add( zman.type );
	}


	boolean contains( final Zmanim.Type type ) {
		return notifiedTypes.contains( type );
	}
}
