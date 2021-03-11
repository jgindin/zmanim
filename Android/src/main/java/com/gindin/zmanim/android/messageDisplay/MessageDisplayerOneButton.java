/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay;

import androidx.annotation.StringRes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

/**
 *
 */
public class MessageDisplayerOneButton
  extends MessageDisplayer {


  private final Context                                     context;

  private final int                                         buttonTextResource;

  private final DialogInterface.OnClickListener             clickListener;


  public MessageDisplayerOneButton(
    Context                         context,
    @StringRes int                  buttonTextResource,
    DialogInterface.OnClickListener clickListener
  ) {

    this.context = context;
    this.buttonTextResource = buttonTextResource;
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

    builder.setPositiveButton( context.getString( buttonTextResource ),
      ( dialog, which ) -> {
        messageDialog.dismiss();
        clickListener.onClick( messageDialog, DialogInterface.BUTTON_POSITIVE );
      }
    );
  }

} // End of MessageDisplayerOneButton class
