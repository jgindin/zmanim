/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;


/**
 * Adapts the zmanim that we can display for the expandable list view.
 */
public class ZmanimExpandableListAdapter
	extends BaseExpandableListAdapter {

	private static final int                                  MAX_GROUPS = Zmanim.Type.values().length;

	private final int                                         UNKNOWN = -1;

	private final Context                                     context;

	/** The calendar we use for displaying everything. */
	private final HebrewZmanimCalendar                        zmanimCalendar;

	/**
	 * Cache this so we don't have to constantly ask if we're supposed to show candle lighting, which
	 *  causes a calculation.
 	 */
	private int                                               currentNumberOfGroups = UNKNOWN;

	public ZmanimExpandableListAdapter(
		Context               context,
		HebrewZmanimCalendar  zmanimCalendar
	) {
		this.context = context;
		this.zmanimCalendar = zmanimCalendar;
	}


	@Override
	public void notifyDataSetChanged() {

		currentNumberOfGroups = UNKNOWN;
		super.notifyDataSetChanged();
	}


	@Override
	public void notifyDataSetInvalidated() {

		currentNumberOfGroups = UNKNOWN;
		super.notifyDataSetInvalidated();
	}


	public int getGroupCount() {

		if ( UNKNOWN == currentNumberOfGroups ) {

			currentNumberOfGroups = MAX_GROUPS;
			if ( !zmanimCalendar.showCandlelighting() ) {
				currentNumberOfGroups--;
			}
		}

		return currentNumberOfGroups;
	}


	/**
	 * Gets the number of children in a specified group.
	 */
	public int getChildrenCount( int group ) {

		Zmanim.Type type = Zmanim.Type.values()[ group ];

		// If we're not showing candle lighting, then we have to skip it.
		if ( ( Zmanim.Type.CandleLighting == type ) && ( MAX_GROUPS != currentNumberOfGroups ) ) {
			type = Zmanim.Type.values()[ group + 1 ];
		}

		final Zmanim zmanim = zmanimCalendar.get( type );

		return ( null == zmanim ? 0 : zmanim.size() );
	}


	/**
	 * Gets the data associated with the given group.
	 */
	public Zmanim getGroup( int group ) {

		Zmanim.Type type = Zmanim.Type.values()[ group ];

		// If we're not showing candle lighting, then we have to skip it.
		if ( ( type.ordinal() >= Zmanim.Type.CandleLighting.ordinal() ) && ( MAX_GROUPS != currentNumberOfGroups ) ) {
			type = Zmanim.Type.values()[ group + 1 ];
		}

		return zmanimCalendar.get( type );
	}


	/**
	 * Gets the data associated with the given child within the given group.
	 */
	public Zman getChild(
		int group,
		int child
	) {

		Zmanim.Type type = Zmanim.Type.values()[ group ];

		// If we're not showing candle lighting, then we have to skip it.
		if ( ( type.ordinal() >= Zmanim.Type.CandleLighting.ordinal() ) && ( MAX_GROUPS != currentNumberOfGroups ) ) {
			type = Zmanim.Type.values()[ group + 1 ];
		}

		final Zmanim zmanim = zmanimCalendar.get( type );
		if ( null == zmanim ) {
			return null;
		}

		return zmanim.get( child );
	}


	/**
	 * Gets the ID for the group at the given position. This group ID must be unique across groups. The combined ID
	 *  (see getCombinedGroupId(long)) must be unique across ALL items (groups and all children).
	 */
	public long getGroupId( int group ) {
		return group;
	}


	/**
	 * Gets the ID for the given child within the given group. This ID must be unique across all children within the group.
	 *  The combined ID (see getCombinedChildId(long, long)) must be unique across ALL items (groups and all children).
	 */
	public long getChildId(
		int group,
		int child
	) {
		return child;
	}


	/**
	 * Indicates whether the child and group IDs are stable across changes to the underlying data.
	 */
	public boolean hasStableIds() {
		return true;
	}


	/**
	 * Gets a View that displays the given group. This View is only for the group--the Views for the group's
	 *  children will be fetched using getChildrenView.
	 *
	 * @param group the position of the group for which the View is returned
	 * @param isExpanded whether the group is expanded or collapsed
	 * @param convertView the old view to reuse, if possible. You should check that this view is non-null and of an
	 *          appropriate type before using. If it is not possible to convert this view to display the correct data,
	 *          this method can create a new view. It is not guaranteed that the convertView will have been previously
	 *          created by getGroupView(int, boolean, View, ViewGroup).
	 * @param parent the parent that this view will eventually be attached to
	 */
	public View getGroupView(
		int       group,
		boolean   isExpanded,
		View      convertView,
		ViewGroup parent
	) {

		final View groupView;
		final ZmanimRowWrapper wrapper;
		if ( null == convertView ) {

			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			groupView = inflater.inflate( R.layout.zmanim_list_row, parent, false );

			wrapper = new ZmanimRowWrapper( groupView );
			groupView.setTag( wrapper );
		}
		else {
			groupView = convertView;
			wrapper = (ZmanimRowWrapper)groupView.getTag();
		}

		final Zmanim zmanim = getGroup( group );
		wrapper.nameField.setText( zmanim.type.name );

		// Just for testing, show the first time...
		Zman zman = zmanim.get( 0 );
		wrapper.timeField.setText( zmanimCalendar.formatZmanTime( zman ) );

		return groupView;
	}


	/**
	 * Gets a View that displays the data for the given child within the given group.
	 *
	 * @param group the position of the group that contains the child
	 * @param child the position of the child (for which the View is returned) within the group
	 * @param isLastChild Whether the child is the last child within the group
	 * @param convertView the old view to reuse, if possible. You should check that this view is non-null and of an
	 *          appropriate type before using. If it is not possible to convert this view to display the correct data,
	 *          this method can create a new view. It is not guaranteed that the convertView will have been previously
	 *          created by getChildView(int, int, boolean, View, ViewGroup).
	 * @param parent the parent that this view will eventually be attached to
	 */
	public View getChildView(
		int       group,
		int       child,
		boolean   isLastChild,
		View      convertView,
		ViewGroup parent
	) {

		final View childView;
		final ZmanRowWrapper wrapper;

		if ( null == convertView ) {

			LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			childView = inflater.inflate( R.layout.zman_list_row, parent, false );

			wrapper = new ZmanRowWrapper( childView );
			childView.setTag( wrapper );
		}
		else {
			childView = convertView;
			wrapper = (ZmanRowWrapper)childView.getTag();
		}

		final Zman zman = getChild( group, child );
		wrapper.nameField.setText( zman.name );

		ZmanAsyncCalculator asyncCalculator = new ZmanAsyncCalculator( zmanimCalendar, wrapper.timeField );
		asyncCalculator.execute( zman );

		return childView;
	}


	/**
	 * Whether the child at the specified position is selectable.
	 */
	public boolean isChildSelectable(
		int group,
		int child
	) {
		return false;
	}


	/**
	 * Called when a group is expanded.
	 */
	public void onGroupExpanded( int group ) {

		// TODO: Should we kick off a pre-calc of the zmanim in this group?

	}


	/**
	 * Wraps a zmanim group, making for quick & easy recycling.
	 */
	private static class ZmanimRowWrapper {

		final TextView                                          nameField;
		final TextView                                          timeField;


		ZmanimRowWrapper( View zmanimRowView ) {
			nameField = (TextView)zmanimRowView.findViewById( R.id.zmanim_name );
			timeField = (TextView)zmanimRowView.findViewById( R.id.zmanim_time );
		}
	}


	/**
	 * Wraps a zman row, making for quick & easy recycling.
	 */
	private static class ZmanRowWrapper {

		final TextView                                          nameField;
		final TextView                                          timeField;
		final TextView                                          descriptionField;

		ZmanRowWrapper( View zmanRowView ) {
			nameField = (TextView)zmanRowView.findViewById( R.id.zman_name );
			timeField = (TextView)zmanRowView.findViewById( R.id.zman_time );
			descriptionField = (TextView)zmanRowView.findViewById( R.id.zman_description );

			// TODO: Not showing descriptions just yet...
			descriptionField.setVisibility( View.GONE );
		}
	}


	/**
	 * Used to asynchronously calculate the zman.
	 */
	private static class ZmanAsyncCalculator
		extends AsyncTask<Zman, Void, String> {

		private final HebrewZmanimCalendar                      zmanimCalendar;
		private final TextView                                  timeField;


		ZmanAsyncCalculator(
			HebrewZmanimCalendar  zmanimCalendar,
			TextView              timeField
		) {
			this.zmanimCalendar = zmanimCalendar;
			this.timeField = timeField;
		}


		@Override
		protected String doInBackground( Zman... zmans ) {
			return zmanimCalendar.formatZmanTime( zmans[0] );
		}


		@Override
		protected void onPostExecute( String formattedZman ) {
			super.onPostExecute( formattedZman );
			timeField.setText( formattedZman ); 
		}
	}

}  // End of ZmanimExpandableListAdapter class
