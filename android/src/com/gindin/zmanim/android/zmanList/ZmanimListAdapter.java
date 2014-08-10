/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.zman.Zman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Adapter for a ListView that shows a set of Zman objects.
 */
public abstract class ZmanimListAdapter
	extends ArrayAdapter<Zman> {


	/** Allows us access to various services, like an inflater. */
	private final Context                                     context;


	public ZmanimListAdapter(
		Context     context,
		List<Zman>  zmanim
	) {

		super( context, R.layout.zman_list_row, zmanim );
		this.context = context;
	}


	@Override
	public boolean hasStableIds() {
		return true;
	}


	@Override
	public long getItemId( int position ) {
		return position;
	}


	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}


	@Override
	public View getView(
		int       position,
		View      convertView,
		ViewGroup parent
	) {

		final Zman zman = getItem( position );

		final View childView;
		final ZmanRowHolder holder;

		if ( null == convertView ) {

			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			childView = inflater.inflate( R.layout.zman_list_row, parent, false );

			holder = createRowHolder( childView );
			childView.setTag( holder );

			onNewViewCreated( childView, zman );
		}
		else {

			// We can just reuse this view as-is.
			childView = convertView;
			holder = (ZmanRowHolder)childView.getTag();

			onViewRecycled( childView, zman );
		}

		holder.setZman( zman );

		return childView;
	}


	protected void onNewViewCreated(
		View childView,
		Zman zman
	) {
		// Nothing by default.
	}


	protected void onViewRecycled(
		View childView,
		Zman zman
	) {
		// Nothing by default.
	}


	/**
	 * Creates a wrapper which will cache the row view.
	 *
	 * @param rowView The actual view of the row.
	 * @return The row wrapper, which will be used to avoid creating crazy numbers of row views.
	 */
	protected abstract ZmanRowHolder createRowHolder( View rowView );


} // End of ZmanimListAdapter
