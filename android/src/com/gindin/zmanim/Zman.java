/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim;

import java.util.Date;

/**
 * Describes a single time.
 */
public abstract class Zman {

	public final String                                       name;

	/** Describes this zman. E.g., "Latest Shema according to the GRA". */
	public final String                                       description;

	/** At what time does this zman occur? */
	private Date                                              time;


	public Zman(
		String  name,
		String  description
	) {
		this.name = name;
		this.description = description;
	}

	public Date getTime() {

		if ( null == time ) {
			time = calculate();
		}

		return time;
	}


	/**
	 * @return Subclass must implement to return the actual time of the zman.
	 */
	protected abstract Date calculate();
	
} // End of Zman interface
