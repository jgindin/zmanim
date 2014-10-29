/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.zman;


import java.util.*;

/**
 * Contains a set of related zmanim.
 */
public abstract class Zmanim {


	public final Type                                         type;

	private final ZmanimCalculator                            zmanimCalculator;

	/** The individual zmanim for this set. */
	private List<Zman>                                        allZmanim;


	protected Zmanim(
		final Type              type,
		final ZmanimCalculator  zmanimCalculator
	) {

		this.type = type;
		this.zmanimCalculator = zmanimCalculator;
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

		if ( ( zmanName == null ) || zmanName.isEmpty() )  {
			return null;
		}

		ensureZmanim();

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

		ensureZmanim();

		for ( Zman zman : allZmanim ) {
			if ( !zman.advanced ) {
				return zman;
			}
		}

		// Should never get here.
		return null;
	}


	public List<Zman> asList() {
		ensureZmanim();
		return allZmanim;
	}


	/**
	 * Called at most once to create the list of zmanim represented.
	 */
	protected abstract List<Zman> buildZmanimList( ZmanimCalculator zmanimCalculator );


	/**
	 * Called when the subclass
	 */
	private void ensureZmanim() {

		if ( allZmanim == null ) {
			allZmanim = Collections.unmodifiableList( buildZmanimList( zmanimCalculator ) );
		}
	}


	/**
	 * Used for easier, programmatic access to the zmanim. Use these to get the actual zmanim for each zman.
	 *<p>
	 * Consider the explanations of the times at http://kehillatisrael.net/hcal/zmanim.html
	 */
	public enum Type {

		Alot( "Alot Hashachar", "Dawn" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new AlotHashachar( zmanimCalculator );
			}
		},
		Talit( "Talit/Tefilin", "Earliest Time to don Talit and Tefilin" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new Talit( zmanimCalculator );
			}
		},
		Hanetz( "Hanetz", "Sunrise" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new Hanetz( zmanimCalculator );
			}
		},
		SofZmanShema( "Sof Zman Shema", "Latest time to finish Shema" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new SofZmanShema( zmanimCalculator );
			}
		},
		SofZmanTefila( "Sof Zman Tefila", "Latest time to finish Shacharit" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new SofZmanTefila( zmanimCalculator );
			}
		},
		SofZmanAchilatChametz( "Achilat Chametz", "Latest time to each Chametz" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new SofZmanAchilatChametz( zmanimCalculator );
			}
		},
		SofZmanBiurChametz( "Biur Chametz", "Latest time to dispose Chametz" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new SofZmanBiurChametz( zmanimCalculator );
			}
		},
		Chatzot( "Chatzot", "Mid-day" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new Chatzot( zmanimCalculator );
			}
		},
		MinchaGedola( "Mincha Gedola", "Earliest time to start Mincha" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new MinchaGedola( zmanimCalculator );
			}
		},
		MinchaKetana( "Mincha Ketana", "Earliest time to start Mincha (Rambam)" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new MinchaKetana( zmanimCalculator );
			}
		},
		PlagHaMincha( "Plag HaMincha", "Earliest time to start Arvit" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new PlagHaMincha( zmanimCalculator );
			}
		},
		CandleLighting( "Candle Lighting", "When candles should be lit" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new CandleLighting( zmanimCalculator );
			}
		},
		Shkia( "Shkia", "Sunset" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new Shkia( zmanimCalculator );
			}
		},
		BeinHashmashot( "Bein Hashmashot", "Twilight" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new BeinHashmashot( zmanimCalculator );
			}
		},
		LatestMincha( "Latest Mincha", "Latest time to finish Mincha" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new LatestMincha( zmanimCalculator );
			}
		},
		FastEnd( "Fast Ends", "Earliest end of fast days" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new FastEnds( zmanimCalculator );
			}
		},
		TzaitHacochavim( "Tzait Hacochavim", "Nightfall" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new TzaitHacochavim( zmanimCalculator );
			}
		},
		ShabbatEnd( "Havdalah", "Earliest Havdalah" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new ShabbatEnds( zmanimCalculator );
			}
		},
		NightChatzot( "Midnight", "Halachic Midnight" ) {
			@Override public Zmanim get( ZmanimCalculator zmanimCalculator ) {
				return new NightChatzot( zmanimCalculator );
			}
		};

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


		public abstract Zmanim get( ZmanimCalculator zmanimCalculator );
	}

}  // End of Zmanim class
