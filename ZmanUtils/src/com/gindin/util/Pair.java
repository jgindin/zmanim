/*
 * Copyright (c) 2014. Jay R. Gindin
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


	@SuppressWarnings( "RedundantIfStatement" )
	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Pair pair = (Pair)o;

		if ( first != null ? !first.equals( pair.first ) : pair.first != null ) {
			return false;
		}
		if ( second != null ? !second.equals( pair.second ) : pair.second != null ) {
			return false;
		}

		return true;
	}


	@Override
	public int hashCode() {
		int result = first != null ? first.hashCode() : 0;
		result = 31 * result + ( second != null ? second.hashCode() : 0 );
		return result;
	}
}  // End of Pair class
