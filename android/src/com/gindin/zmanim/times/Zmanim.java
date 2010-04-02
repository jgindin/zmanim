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

	/**
	 * The list of zmanim includes 'simple' and 'advanced'. We want to be able to differentiate. The contents of this
	 *  list are just references to the zmanim in the allZmanim list.
	 */
	private final List<Zman>                                  simpleZmanim = new ArrayList<Zman>();


	protected Zmanim(
		final Type        type,
	  final List<Zman>  zmanim
	) {

		this.type = type;
		this.allZmanim = zmanim;

		for ( Zman zman : zmanim ) {
			if ( !zman.advanced ) {
				simpleZmanim.add( zman );
			}
		}
	}


	public int size() {
		return simpleZmanim.size();
	}


	public int advancedSize() {
		return allZmanim.size();
	}


	public Zman get(
		int position
	) {

		if ( position >= simpleZmanim.size() ) {
			return null;
		}

		return simpleZmanim.get( position );
	}


	public Zman advancedGet(
		int position
	) {

		if ( position >= allZmanim.size() ) {
			return null;
		}

		return allZmanim.get( position );
	}


	public Iterator<Zman> iterator() {
		return simpleZmanim.iterator();
	}


	public Iterator<Zman> advancedIterator() {
		return allZmanim.iterator();
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
