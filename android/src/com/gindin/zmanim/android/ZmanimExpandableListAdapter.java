/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.gindin.zmanim.HebrewZmanimCalendar;
import com.gindin.zmanim.ZmanRounder;
import com.gindin.zmanim.Zmanim;

/**
 * Adapts the zmanim that we can display for the expandable list view.
 */
public class ZmanimExpandableListAdapter
	extends BaseExpandableListAdapter {

	/** The calendar we use for displaying everything. */
	private final HebrewZmanimCalendar                        zmanimCalendar;

	/** When we need to round a zman up or down, this is the guy to do it. */
	private final ZmanRounder                                 zmanRounder = new ZmanRounder();


	public ZmanimExpandableListAdapter( HebrewZmanimCalendar zmanimCalendar ) {
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
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}


	/**
	 * Gets the data associated with the given group.
	 */
	public Object getGroup( int group ) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}


	/**
	 * Gets the data associated with the given child within the given group.
	 */
	public Object getChild(
		int group,
		int child
	) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}


	/**
	 * Gets the ID for the group at the given position. This group ID must be unique across groups. The combined ID
	 *  (see getCombinedGroupId(long)) must be unique across ALL items (groups and all children).
	 */
	public long getGroupId( int group ) {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}


	/**
	 * Gets the ID for the given child within the given group. This ID must be unique across all children within the group.
	 *  The combined ID (see getCombinedChildId(long, long)) must be unique across ALL items (groups and all children).
	 */
	public long getChildId(
		int group,
		int child
	) {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
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
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}


	/**
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
		return null;  //To change body of implemented methods use File | Settings | File Templates.
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


	public boolean areAllItemsEnabled() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}


	public boolean isEmpty() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
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


}  // End of ZmanimExpandableListAdapter class
