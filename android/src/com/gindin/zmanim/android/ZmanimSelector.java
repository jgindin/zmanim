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
import android.widget.*;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.Zmanim;

/**
 * Displays a dialog allowing the user to select one of the applicable zmanim.
 */
public class ZmanimSelector {

	private final Context                                     context;

	private final HebrewZmanimCalendar                        zmanimCalendar;

	/** Let's only build our dialog once and re-use it... */
	private final Dialog                                      dialog;

	/** Resource to which we append the zmanim type being selected. */
	private final String                                      baseTitle;

	/** Only one instance of the dialog means only one instance of the list... */
	private final ListView                                    listView;

	/** When we're displaying a selection dialog, what type of zmanim are being selected? */
	private Zmanim.Type                                       typeToSelectFrom;

	/** When we're displaying a selection dialog, who do we notify when we're done? */
	private SelectionCallback                                 typeSelectionCallback;


	public ZmanimSelector(
		Context               context,
		HebrewZmanimCalendar  zmanimCalendar
	) {

		this.context = context;
		this.zmanimCalendar = zmanimCalendar;

		baseTitle = context.getString( R.string.zmanim_selector );

		AlertDialog.Builder builder = new AlertDialog.Builder( context );

		LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.zmanim_selection, null );

		builder.setView( layout );
		dialog = builder.create();

		listView = (ListView)layout.findViewById( android.R.id.list );
		listView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				public void onItemClick(
					AdapterView<?>  parent,
					View            view,
					int             position,
					long            id
				) {

					final ZmanimListAdapter adapter = (ZmanimListAdapter)listView.getAdapter();
					Zman selection = adapter.getItem( position );
					dialog.dismiss();
					typeSelectionCallback.selected( typeToSelectFrom, selection );
				}

				public void onNothingSelected( AdapterView<?> adapterView ) {
					// Ignore
				}
			}
		);
	}


	void selectFrom(
		final Zmanim.Type       type,
	  final SelectionCallback callback
	) {

		typeToSelectFrom = type;
		typeSelectionCallback = callback;

		final Zmanim zmanim = zmanimCalendar.get( type );
		final ZmanimListAdapter listAdapter = new ZmanimListAdapter( zmanim.toArray() );

		listView.setAdapter( listAdapter );

		dialog.setTitle( baseTitle + " " + type.name );

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
