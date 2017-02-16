/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.messageDisplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

/**
 *
 */
public class MessageDisplayerOneButton
  extends MessageDisplayer {


  public void update(
    int                                   titleResource,
    String                                textLocation,
    int                                   buttonTextResource,
    final DialogInterface.OnClickListener clickListener
  ) {
    super.update( titleResource, textLocation );

    String buttonText = messageDialog.getContext().getResources().getString( buttonTextResource );

    Button button = messageDialog.getButton( DialogInterface.BUTTON_POSITIVE );
    button.setText( buttonText );
    button.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          messageDialog.dismiss();
          clickListener.onClick( messageDialog, DialogInterface.BUTTON_POSITIVE );
        }
      }
    );
  }


  @Override
  protected void customize( AlertDialog.Builder builder ) {
    super.customize( builder );

    // Just like with the title, trying to set an empty string won't allow us to later set the real string.
    builder.setPositiveButton( " x  ", null );
  }

} // End of MessageDisplayerOneButton class
