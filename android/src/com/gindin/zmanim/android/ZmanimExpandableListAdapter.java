/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

	private final Context                                     context;

	/** The calendar we use for displaying everything. */
	private final HebrewZmanimCalendar zmanimCalendar;


	public ZmanimExpandableListAdapter(
		Context               context,
		HebrewZmanimCalendar  zmanimCalendar
	) {
		this.context = context;
		this.zmanimCalendar = zmanimCalendar;
	}


	public int getGroupCount() {

		int numGroups = Zmanim.Type.values().length;
		if ( !zmanimCalendar.showCandlelighting() ) {
			numGroups--;
		}

		return numGroups;
	}


	/**
	 * Gets the number of children in a specified group.
	 */
	public int getChildrenCount( int group ) {

		Zmanim.Type type = Zmanim.Type.values()[ group ];
		final Zmanim zmanim = zmanimCalendar.get( type );

		return ( null == zmanim ? 0 : zmanim.size() );
	}


	/**
	 * Gets the data associated with the given group.
	 */
	public Zmanim getGroup( int group ) {

		Zmanim.Type type = Zmanim.Type.values()[ group ];
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

		final TextView groupView;
		if ( null == convertView ) {
			groupView = getGenericView();
		}
		else {
			groupView = (TextView)convertView;
		}

		final Zmanim zmanim = getGroup( group );
		groupView.setText( zmanim.type.name );

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

		final TextView view;
		if ( null == convertView ) {
			view = getGenericView();
		}
		else {
			view = (TextView)convertView;
		}

		final Zman zman = getChild( group, child );
		view.setText(  zmanimCalendar.formatZmanTime( zman ) );

		return view;
	}


	/**
	 * Whether the child at the specified position is selectable.
	 */
	public boolean isChildSelectable(
		int group,
		int child
	) {
		return true;
	}


	/**
	 * Called when a group is expanded.
	 */
	public void onGroupExpanded( int group ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}


	/**
	 * Called when a group is collapsed.
	 */
	public void onGroupCollapsed( int group ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}


	private TextView getGenericView() {

		// Layout parameters for the ExpandableListView
		TextView textView = new TextView( context );
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 32 );
		textView.setLayoutParams( lp );

		// Center the text vertically
		textView.setGravity( Gravity.CENTER_VERTICAL | Gravity.LEFT );

		// Set the text starting position
		textView.setPadding( 36, 0, 0, 0 );

		return textView;
	}


}  // End of ZmanimExpandableListAdapter class
