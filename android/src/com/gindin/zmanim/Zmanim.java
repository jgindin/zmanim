/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim;

import java.util.*;

/**
 * Contains a set of related zmanim.
 */
public class Zmanim {

	public final Type                                         type;

	/** Stored in sorted order, from earliest to latest. */
	private final List<Zman>                                  zmanim;


	protected Zmanim(
		Type      type,
	  Zman ...  zmanim
	) {
		this.type = type;

		Comparator<Zman> zmanStartComparator = new Comparator<Zman>() {
			public int compare( Zman z1, Zman z2 ) {

				return z1.getTime().compareTo( z2.getTime() );
			}
		};

		this.zmanim = Arrays.asList( zmanim );
		Collections.sort( this.zmanim, zmanStartComparator );
	}


	public int size() {
		return zmanim.size();
	}


	public Zman get( int position ) {
		if ( position >= zmanim.size() ) {
			return null;
		}

		return zmanim.get( position );
	}


	public Iterator<Zman> iterator() {
		return zmanim.iterator();
	}


	/**
	 * Used for easier, programmatic access to the zmanim. Use these to get the actual zmanim for each zman.
	 */
	public enum Type {

		Alot( "Alot Hashachar", "Dawn" ),
		Talit( "Misheyakir", "Earliest Tefilin" ),
		Hanetz( "Hanetz", "Sunrise" ),
		SofZmanShma( "Sof Zman Shema", "Latest Time for Shema" ),
		SofZmanTefila( "Sof Zman Tefila", "Latest Time for Shacharit" ),
		Chatzot( "Chatzot", "Mid-day" ),
		MinchaGedola( "Mincha Gedola", "Earliest Mincha" ),
		MinchaKetana( "Mincha Ketana", "Earliest Mincha (Rambam)" ),
		PlagHaMincha( "Plag HaMincha", "Earliest Arvit" ),
		Candlelighting( "Candle Lighting", "" ),
		Shkia( "Shkia", "Sunset" ),
		LatestMincha( "Latest Mincha", "" ),
		TzaitHachocavim( "Tzait Hachocavim", "Nightfall" );

		/** Name of the set of zmanim. E.g., "Sof Zman Shema" */
		public final String                                       name;

		/** Describes the set of zmanim. E.g., "Latest time to say Shema". */
		public final String                                       description;


		Type(
			String name,
			String description
		) {
			this.name = name;
			this.description = description;
		}
	}
	
}  // End of Zmanim class
