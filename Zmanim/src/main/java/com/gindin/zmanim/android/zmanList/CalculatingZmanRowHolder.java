/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.zmanList;

import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * This class should really be deprecated...the view holder should *not* be performing calculations...
 */
public abstract class CalculatingZmanRowHolder
  extends ZmanRowHolder {

  private final ZmanFormatter                               zmanFormatter;

  private ZmanAsyncCalculator                               currentCalculator;



  protected CalculatingZmanRowHolder(
    View          zmanRowView,
    ZmanFormatter zmanFormatter
  ) {
    super( zmanRowView );
    this.zmanFormatter = zmanFormatter;
  }


  @Override
  public void setZman( Zman zman ) {

    // Cancel any currently executing calculation...
    if ( ( null != currentCalculator ) && ( !currentCalculator.isCancelled() ) ) {
      currentCalculator.cancel( false );
    }

    // Start the new calculation
    currentCalculator = new ZmanAsyncCalculator( zmanFormatter, timeField );
    currentCalculator.execute( zman );

    super.setZman( zman );
  }


  /**
   * Used to asynchronously calculate the zman.
   */
  private static class ZmanAsyncCalculator
    extends AsyncTask<Zman, Void, String> {

    private final ZmanFormatter                             zmanFormatter;
    private final TextView                                  timeField;


    ZmanAsyncCalculator(
      ZmanFormatter zmanFormatter,
      TextView      timeField
    ) {
      this.zmanFormatter = zmanFormatter;
      this.timeField = timeField;

      timeField.setText( "" );
    }


    @Override
    protected String doInBackground( Zman... zmans ) {

      String formatted = zmanFormatter.formatZmanTime( zmans[0] );
      return formatted;
    }


    @Override
    protected void onPostExecute( String formattedZman ) {
      super.onPostExecute( formattedZman );

      // If we got cancelled, don't do anything!
      if ( !isCancelled() ) {
        timeField.setText( formattedZman );
      }
    }
  }

} // End of CalculatingZmanRowHolder class
