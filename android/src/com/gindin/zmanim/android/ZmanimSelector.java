/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;

/**
 * Displays a dialog allowing the user to select one of the applicable zmanim.
 */
public class ZmanimSelector {

	private final Context                                     context;

	private final HebrewZmanimCalendar                        zmanimCalendar;

	public ZmanimSelector(
		Context               context,
		HebrewZmanimCalendar  zmanimCalendar
	) {

		this.context = context;
		this.zmanimCalendar = zmanimCalendar;
	}


	void selectFrom(
		final Zmanim.Type       type,
	  final SelectionCallback callback
	) {

		final Zmanim zmanim = zmanimCalendar.get( type );
		final ZmanimListAdapter listAdapter = new ZmanimListAdapter( zmanim.toArray() );

		AlertDialog.Builder builder = new AlertDialog.Builder( context );
		builder.setTitle( R.string.zmanim_selector );

		LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.zmanim_selection, null );

		builder.setView( layout );
		final Dialog dialog = builder.create();

		ListView listView = (ListView)layout.findViewById( android.R.id.list );
		listView.setAdapter( listAdapter );
		listView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				public void onItemClick(
					AdapterView<?>  parent,
					View            view,
					int             position,
					long            id
				) {

					Zman selection = listAdapter.getItem( position );
					dialog.dismiss();
					callback.selected( type, selection );
				}

				public void onNothingSelected( AdapterView<?> adapterView ) {
					// Ignore
				}
			}
		);
		
		dialog.show();
	}


	public static interface SelectionCallback {

		void selected(
			Zmanim.Type type,
			Zman        selection
		);
	}


	private class ZmanimListAdapter
		extends ArrayAdapter<Zman> {

		private ZmanimListAdapter(
			Zman[] zmanim
		) {

			super( context, R.layout.zman_list_row, zmanim );
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

				wrapper = new ZmanRowWrapper( childView );
				childView.setTag( wrapper );
			}
			else {
				childView = convertView;
				wrapper = (ZmanRowWrapper)childView.getTag();
			}

			final Zman zman = getItem( position );
			wrapper.nameField.setText( zman.name );
			wrapper.descriptionField.setText( zman.description ); 

			ZmanAsyncCalculator asyncCalculator = new ZmanAsyncCalculator( zmanimCalendar, wrapper.timeField );
			asyncCalculator.execute( zman );

			return childView;
		}

	} // End of ZmanimListAdapter



	/**
	 * Wraps a zman row, making for quick & easy recycling.
	 */
	private static class ZmanRowWrapper {

		final TextView nameField;
		final TextView                                          timeField;
		final TextView                                          descriptionField;

		ZmanRowWrapper( View zmanRowView ) {
			nameField = (TextView)zmanRowView.findViewById( R.id.zman_name );
			timeField = (TextView)zmanRowView.findViewById( R.id.zman_time );
			descriptionField = (TextView)zmanRowView.findViewById( R.id.zman_description );
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


}  // End of ZmanimSelector class
