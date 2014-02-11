/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class LatestMincha
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "Hacham Ovadia Yosef";

	LatestMincha(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.LatestMincha, buildZmanim( zmanimCalculator ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();


		zmanim.add(
			new Zman( "Mishna Berura", "Daily Halacha's representation of the Mishna Berura's ruling, that mincha must be FINISHED by sunset.", Zmanim.Type.LatestMincha ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getLatestMinchaMishnaBerura();
				}
			}
		);

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Daily Halacha's representation of Chacham Ovadia Yosef's ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5 shaot zmaniot minutes after sunset.", Zmanim.Type.LatestMincha, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getLatestMinchaOvadiaYosef();
				}
			}
		);

		zmanim.add(
			new Zman( "Hacham Ben Sion", "Daily Halacha's representation of Chacham Ben Sion Abba Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26 minutes after sunset.", Zmanim.Type.LatestMincha, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getLatestMinchaBenSion();
				}
			}
		);

		return zmanim;
	}

}  // End of LatestMincha class
