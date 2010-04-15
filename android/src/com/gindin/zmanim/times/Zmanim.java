/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.times;


import java.util.*;

/**
 * Contains a set of related zmanim.
 */
public abstract class Zmanim {


	public final Type                                         type;

	/** The individual zmanim for this set. */
	private final List<Zman>                                  allZmanim;



	protected Zmanim(
		final Type        type,
	  final List<Zman>  zmanim
	) {

		this.type = type;
		this.allZmanim = zmanim;
	}


	public int size() {
		return allZmanim.size();
	}


	public Zman get(
		int position
	) {

		if ( position >= allZmanim.size() ) {
			return null;
		}

		return allZmanim.get( position );
	}


	/**
	 * Finds the named zman.
	 *
	 * @param zmanName which zman to locate.
	 *
	 * @return the named zman, or null if it cannot be found.
	 */
	public Zman getByName(
		String zmanName
	) {

		for ( Zman zman : allZmanim ) {
			if ( zmanName.equals( zman.name ) ) {
				return zman;
			}
		}

		return null;
	}


	/**
	 * Finds a default zman to represent this group.
	 *
	 * @return As currently implemented, returns the first non-advanced zman in the group.
	 */
	public Zman getDefault() {

		for ( Zman zman : allZmanim ) {
			if ( !zman.advanced ) {
				return zman;
			}
		}

		// Should never get here.
		return null;
	}


	public Iterator<Zman> iterator() {
		return allZmanim.iterator();
	}


	public Zman[] toArray() {
		return allZmanim.toArray( new Zman[ allZmanim.size() ] );
	}


	/**
	 * Must be called whenever the date has changed so that we can clear our caches.
	 */
	public void clearCaches() {

		for ( Zman zman : allZmanim ) {
			zman.clearCache();
		}
	}


	/**
	 * Used for easier, programmatic access to the zmanim. Use these to get the actual zmanim for each zman.
	 */
	public enum Type {

		Alot( "Alot Hashachar", "Dawn" ),
		Talit( "Misheyakir", "Earliest Tefilin" ),
		Hanetz( "Hanetz", "Sunrise" ),
		SofZmanShema( "Sof Zman Shema", "Latest Time for Shema" ),
		SofZmanTefila( "Sof Zman Tefila", "Latest Time for Shacharit" ),
		Chatzot( "Chatzot", "Mid-day" ),
		MinchaGedola( "Mincha Gedola", "Earliest Mincha" ),
		MinchaKetana( "Mincha Ketana", "Earliest Mincha (Rambam)" ),
		PlagHaMincha( "Plag HaMincha", "Earliest Arvit" ),
		CandleLighting( "Candle Lighting", "" ),
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
