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
	private final List<Zman>                                  zmanim;

	/** When we need to round a zman up or down, this is the guy to do it. */
	protected final ZmanRounder                               zmanRounder = new ZmanRounder();


	protected Zmanim(
		final Type        type,
	  final List<Zman>  zmanim
	) {

		this.type = type;
		this.zmanim = zmanim;
	}


	public int size() {
		return zmanim.size();
	}


	public Zman get(
		int position
	) {

		if ( position >= zmanim.size() ) {
			return null;
		}

		return zmanim.get( position );
	}


	public Iterator<Zman> iterator() {
		return zmanim.iterator();
	}


	/**
	 * Must be called whenever the date has changed so that we can clear our caches.
	 */
	public void clearCaches() {

		for ( Zman zman : zmanim ) {
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
