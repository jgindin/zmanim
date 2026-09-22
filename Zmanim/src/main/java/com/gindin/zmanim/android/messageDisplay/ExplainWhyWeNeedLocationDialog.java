/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay;

import com.gindin.zmanim.android.R;

import androidx.annotation.StringRes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

/**
 *
 */
public class ExplainWhyWeNeedLocationDialog
  extends MessageDisplayer {


  private final Context                                     context;

  private final DialogInterface.OnClickListener             clickListener;


  public ExplainWhyWeNeedLocationDialog(
    Context                         context,
    DialogInterface.OnClickListener clickListener
  ) {

    this.context = context;
    this.clickListener = clickListener;
  }


  public void show(
    @StringRes int  titleResource,
    String          textLocation
  ) {

    final Dialog dialog = getInstance( context );

    update( titleResource, textLocation );

    dialog.show();
  }


  @Override
  protected void customize( AlertDialog.Builder builder ) {
    super.customize( builder );

    builder
      .setPositiveButton( context.getString( R.string.turn_on_location ),
        ( dialog, which ) -> {
          messageDialog.dismiss();
          clickListener.onClick( messageDialog, DialogInterface.BUTTON_POSITIVE );
        }
      )
      .setNegativeButton( context.getString( R.string.no_thanks ),
        ( dialog, which ) -> {
          messageDialog.dismiss();
          clickListener.onClick( messageDialog, DialogInterface.BUTTON_NEGATIVE );
        }
      )
      ;

  }

}
