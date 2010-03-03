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

	/** Keep the calculation cached until we're told to clear the cache. */
	private Date                                              cachedTime;


	public Zman(
		String  name,
		String  description
	) {
		this.name = name;
		this.description = description;
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
