/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.Date;

/**
 * Describes a single time. Intended to be reused as the date changes, without having to be
 *  recreated.
 */
public abstract class Zman {

	public final String                                       name;

	/** Describes this zman. E.g., "Latest Shema according to the GR'A". */
	public final String                                       description;

	/** Advanced zmanim should not be shown unless the user has explicitly indicated they want to see them. */
	public final boolean                                      advanced;

	/** What type of zman is this? */
	public final Zmanim.Type                                  type;

	/** Keep the calculation cached until we're told to clear the cache. */
	private Date                                              cachedTime;


	Zman(
		String      name,
		String      description,
		Zmanim.Type type
	) {
		this( name, description, type, true );
	}


	Zman(
		String      name,
		String      description,
		Zmanim.Type type,
	  boolean     advanced
	) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.advanced = advanced;
	}

	public Date getTime() {

		synchronized ( this ) {
			if ( null == cachedTime ) {
				cachedTime = calculate();
			}
		}

		return cachedTime;
	}


	/**
	 * @return Subclass must implement to return the actual time of the zman.
	 */
	protected abstract Date calculate();


} // End of Zman interface
