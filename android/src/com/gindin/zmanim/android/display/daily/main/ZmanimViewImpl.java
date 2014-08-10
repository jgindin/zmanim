/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.main;

import com.gindin.mvp.AbstractView;
import com.gindin.zmanim.android.zmanList.ZmanRowHolder;
import com.gindin.zmanim.android.zmanList.ZmanimListAdapter;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.Zmanim;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
class ZmanimViewImpl<P extends ZmanimPresenter>
	extends AbstractView<P>
	implements ZmanimView<P> {

	private final ListView                                    listView;



	public ZmanimViewImpl(
		FragmentActivity owner
	) {

		super( owner );
		listView = (ListView)owner.findViewById( android.R.id.list );

		listView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(
					AdapterView<?>  adapterView,
					View            view,
					int             position,
					long            id
				) {

					ZmanimViewListAdapter adapter = (ZmanimViewListAdapter)adapterView.getAdapter();
					Zman zman = adapter.getItem( position );

					// Notify presenter. Old code from ZmanimDisplay:
//					if ( null == selector ) {
//						selector = new ZmanimSelector( zmanimActivity, zmanFormatter );
//					}
//
//					final ZmanimSelector.SelectionCallback selectionCallback = new ZmanimSelector.SelectionCallback() {
//						public void selected( Zmanim.Type type, Zman selection ) {
//
//							final SharedPreferences.Editor editor = zmanimSelectionPrefs.edit();
//							editor.putString( type.name, selection.name );
//							editor.apply();
//
//							LogUtils.logEvent( "Zman Change", Arrays.asList( new Pair<String, String>( type.name, selection.name ) ) );
//
//							// This is a bit of a hammer, but it works...
//							//  I tried to find the existing zman in the list, remove it, and add the newly selected one, but
//							//  apparently the List which backs the adapter doesn't support remove().
//							zmanimActivity.setAdapter( getListAdapter( currentDate, currentLocation ) );
//						}
//					};
//
//					selector.selectFrom( currentDate, currentLocation, zman.type, selectionCallback );
				}
			}
		);
	}


	@Override
	public void displayZmanim( Zman[] zmanimForTheDay ) {

		// TODO: Rather than creating a new ListAdapter *every* time, it seems that  we ought to be
		// TODO:  able to update and reuse...
		listView.setAdapter( new ZmanimViewListAdapter( owner, Arrays.asList( zmanimForTheDay )) );
	}


	@Override
	public void displayTimeForZman(
		Zman    zman,
		String  formattedTime
	) {

		ZmanimViewListAdapter adapter = (ZmanimViewListAdapter)listView.getAdapter();
		ZmanViewRowHolder rowHolder = adapter.findRowHolder( zman.type );
		if ( rowHolder != null ) {
			rowHolder.updateTime( formattedTime );
		}
	}



	private static class ZmanimViewListAdapter
		extends ZmanimListAdapter {

		/**
		 * Because we're going to update the view asynchronously, we have to be able to find the row holder
		 *  once a Zman is done calculating. Note, however, that we must not assume that we'll find any given
		 *  Zman in the map. The map will ONLY have an entry if we have a view for that Zman. And...the view can
		 *  get recycled and used for different zmanim.
	 	 */
		private final Map<Zmanim.Type,ZmanViewRowHolder>        zmanZmanRowHolderMap = new HashMap<Zmanim.Type, ZmanViewRowHolder>();


		ZmanimViewListAdapter(
			Context     context,
			List<Zman>  zmanim
		) {
			super( context, zmanim );
		}


		@Override
		protected void onNewViewCreated(
			View childView,
			Zman zman
		) {
			super.onNewViewCreated( childView, zman );

			zmanZmanRowHolderMap.put( zman.type, (ZmanViewRowHolder)childView.getTag() );
		}


		@Override
		protected void onViewRecycled(
			View childView,
			Zman zman
		) {
			super.onViewRecycled( childView, zman );

			// Find and remove the Zman which was associated with this view.
			ZmanViewRowHolder zmanRowHolder = (ZmanViewRowHolder)childView.getTag();
			for ( Map.Entry<Zmanim.Type, ZmanViewRowHolder> entry : zmanZmanRowHolderMap.entrySet() ) {
				if ( zmanRowHolder.equals( entry.getValue() ) ) {
					zmanZmanRowHolderMap.remove( entry.getKey() );
					break;
				}
			}

			// Don't forget to update the map!
			zmanZmanRowHolderMap.put( zman.type, zmanRowHolder );
		}


		@Override
		protected ZmanRowHolder createRowHolder( View rowView ) {
			return new ZmanViewRowHolder( rowView );
		}


		ZmanViewRowHolder findRowHolder( Zmanim.Type type ) {

			return zmanZmanRowHolderMap.get( type );
		}

	}


	private static class ZmanViewRowHolder
		extends ZmanRowHolder {

		ZmanViewRowHolder( View rowView ) {
			super( rowView );
		}


		@Override
		protected void updateTextFields( Zman zman ) {
			nameField.setText( zman.type.name );
			descriptionField.setText( zman.name );

			// The time will get displayed later...
			timeField.setText( "" );
		}


		void updateTime( String formattedTime ) {
			timeField.setText( formattedTime );
		}
	}

} // End of ZmanimViewImpl class
