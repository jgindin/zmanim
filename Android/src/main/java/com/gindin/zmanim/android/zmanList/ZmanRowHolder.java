/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.zman.Zman;

import android.view.View;
import android.widget.TextView;

/**
 * Wraps a zman row, making for quick & easy recycling. Known, according to Android terms, a ViewHolder pattern.
 *  See, e.g., https://www.udacity.com/course/viewer#!/c-ud853/l-1623168625/e-1667758590/m-1667758591.
 */
public abstract class ZmanRowHolder {

  protected final TextView                                nameField;
  protected final TextView                                descriptionField;
  protected final TextView                                timeField;


  public ZmanRowHolder(
    View  zmanRowView
  ) {

    nameField = (TextView)zmanRowView.findViewById( R.id.zman_name );
    timeField = (TextView)zmanRowView.findViewById( R.id.zman_time );
    descriptionField = (TextView)zmanRowView.findViewById( R.id.zman_description );
  }


  public void setZman(
    Zman zman
  ) {

    updateTextFields( zman );
  }


  /**
   * Must be implemented to set the name & description fields.
   *
   * @param zman the zman to use when setting the fields.
   */
  protected abstract void updateTextFields( Zman zman );

}
