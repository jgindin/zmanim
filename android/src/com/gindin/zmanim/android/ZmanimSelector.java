/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.gindin.zmanim.android.zmanList.ZmanRowWrapper;
import com.gindin.zmanim.android.zmanList.ZmanimListAdapter;
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
		final ZmanimListAdapter listAdapter = new ZmanimListAdapter( context, zmanim.toArray() ) {
			@Override protected ZmanRowWrapper createRowWrapper( View rowView ) {
				return new SelectorRow( rowView, zmanimCalendar );
			}
		};

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


	private static class SelectorRow
		extends ZmanRowWrapper {

		private SelectorRow(
			View                  zmanRowView,
			HebrewZmanimCalendar  zmanimCalendar
		) {
			super( zmanRowView, zmanimCalendar );
		}


		protected void updateTextFields( Zman zman ) {
			nameField.setText( zman.name );
			descriptionField.setText( zman.description );
		}
	}


}  // End of ZmanimSelector class
