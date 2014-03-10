/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.zman.Zman;

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

		final View childView;
		final ZmanRowWrapper wrapper;

		if ( null == convertView ) {

			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			childView = inflater.inflate( R.layout.zman_list_row, parent, false );

			wrapper = createRowWrapper( childView );
			childView.setTag( wrapper );
		}
		else {
			childView = convertView;
			wrapper = (ZmanRowWrapper)childView.getTag();
		}

		final Zman zman = getItem( position );
		wrapper.setZman( zman );

		return childView;
	}


	/**
	 * Creates a wrapper which will cache the row view.
	 *
	 * @param rowView The actual view of the row.
	 * @return The row wrapper, which will be used to avoid creating crazy numbers of row views.
	 */
	protected abstract ZmanRowWrapper createRowWrapper( View rowView );


} // End of ZmanimListAdapter
