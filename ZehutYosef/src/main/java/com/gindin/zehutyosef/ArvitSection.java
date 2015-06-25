package com.gindin.zehutyosef;

import com.gindin.zmanlib.calendar.HebrewDate;

import android.content.res.AssetManager;
import android.support.annotation.StringRes;
import android.widget.TextView;

/**
 * Special section because when you view Arvit, you're actually viewing the next day.
 */
class ArvitSection
	extends SidurSection {

	ArvitSection(
		@StringRes final int titleId,
		final SectionText sectionText
	) {
		super( titleId, sectionText );
	}


	@Override
	void load(
		final HebrewDate date,
		final AssetManager assets,
		final TextView textContent
	) {
		super.load( date.tomorrow(), assets, textContent );
	}
}
