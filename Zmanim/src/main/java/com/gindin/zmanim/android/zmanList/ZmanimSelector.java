/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Displays a dialog allowing the user to select one of the applicable zmanim.
 */
public class ZmanimSelector {

  private final Context                                     context;

  private final ZmanFormatter                               zmanFormatter;

  /** Let's only build our dialog once and re-use it... */ // todo: but, please be lazy about it!
  private final Dialog                                      dialog;

  /** Where we can display the name of the zman being selected. */
  private final TextView                                    name;

  /** Where we can display the description of the zman. */
  private final TextView                                    description;

  /** Only one instance of the dialog means only one instance of the list... */
  private final ListView                                    listView;

  /** When we're displaying a selection dialog, who do we notify when we're done? */
  private SelectionCallback                                 typeSelectionCallback;



  public ZmanimSelector(
    Context       context,
    ZmanFormatter zmanFormatter
  ) {

    this.context = context;
    this.zmanFormatter = zmanFormatter;

    AlertDialog.Builder builder = new AlertDialog.Builder( context );

    LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    View layout = inflater.inflate( R.layout.zmanim_selection, null );

    builder.setView( layout );
    dialog = builder.create();
    dialog.setTitle( context.getString( R.string.zmanim_selector ) );

    name = (TextView)layout.findViewById( R.id.zman_name );
    description = (TextView)layout.findViewById( R.id.zman_description );

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
          typeSelectionCallback.selected( selection );
        }
      }
    );
  }


  public void selectFrom(
    HebrewDate              currentDate,
    ZmanimLocation          currentLocation,
    final Zmanim.Type       type,
    final SelectionCallback callback
  ) {

    typeSelectionCallback = callback;

    final Zmanim zmanim = currentDate.getZmanim( currentLocation ).findType( type );
    if ( zmanim == null ) {
      return;
    }

    final ZmanimListAdapter listAdapter = new ZmanimListAdapter( context, zmanim.asList() ) {
      @Override protected ZmanRowHolder createRowHolder( View rowView ) {
        return new SelectorRowHolder( rowView, zmanFormatter );
      }
    };

    name.setText( type.name );
    description.setText( type.description );
    listView.setAdapter( listAdapter );

    dialog.show();
  }


  public static interface SelectionCallback {

    void selected(
      Zman selection
    );
  }


  private static class SelectorRowHolder
    extends CalculatingZmanRowHolder {

    private SelectorRowHolder(
      View          zmanRowView,
      ZmanFormatter zmanFormatter
    ) {
      super( zmanRowView, zmanFormatter );
    }


    protected void updateTextFields( Zman zman ) {
      nameField.setText( zman.name );
      descriptionField.setText( zman.description );
    }
  }


}  // End of ZmanimSelector class
