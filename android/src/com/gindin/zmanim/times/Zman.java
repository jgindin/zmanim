/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.times;

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

	/** Keep the calculation cached until we're told to clear the cache. */
	private Date                                              cachedTime;


	public Zman(
		String  name,
		String  description
	) {
		this( name, description, true );
	}


	public Zman(
		String  name,
		String  description,
	  boolean advanced
	) {
		this.name = name;
		this.description = description;
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

	public void clearCache() {
		cachedTime = null;
	}

	/**
	 * @return Subclass must implement to return the actual time of the zman.
	 */
	protected abstract Date calculate();


} // End of Zman interface
