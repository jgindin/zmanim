/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.util;

/**
 * Just a simple tuple container. Can't use the android.util.Pair because that was introduced too late...
 */
public class Pair<FIRST_TYPE, SECOND_TYPE> {
	
	public final FIRST_TYPE                                   first;
	public final SECOND_TYPE                                  second;


	public Pair(
		FIRST_TYPE  first,
		SECOND_TYPE second
	) {
		this.first = first;
		this.second = second;
	}

}  // End of Pair class
