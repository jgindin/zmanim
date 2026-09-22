/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.location.LatitudeLongitudeParser;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * An Android Preference that allows for easy editing of a Latitude or Longitude.
 *<p>
 *
 * Compared to 'normal' Android Preference editors, this one requires that the code using it call the "init" method
 *  so that it will know if it's editing latitude or longitude.
 */
public class LongitudeLatitudePreference
  extends DialogPreference {

  private View                                              view;

  private boolean                                           showingLatitude;
  private LatitudeLongitudeParser                           valueHolder;


  public LongitudeLatitudePreference(
    Context       context,
    AttributeSet  attrs
  ) {
    super( context, attrs );
  }


  public void init(
    String  value,
    boolean showingLatitude
  ) {
    this.showingLatitude = showingLatitude;

    if ( showingLatitude ) {
      valueHolder = LatitudeLongitudeParser.latitude( value );
    }
    else {
      valueHolder = LatitudeLongitudeParser.longitude( value );
    }
  }


  @Override
  protected View onCreateDialogView() {

    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    view = inflater.inflate( R.layout.lat_long, null );

    return view;
  }


  @Override
  protected void onBindDialogView( View view ) {

    super.onBindDialogView( view );

    ValueFilter degreesFilter;
    if ( showingLatitude ) {
      degreesFilter = new ValueFilter( -90, 90 );
    }
    else {
      degreesFilter = new ValueFilter( -180, 180 );
    }

    ValueFilter minutesFilter = new ValueFilter( 0, 60 );

    EditText degrees = view.findViewById( R.id.lat_long_editor_degrees_editor );
    degrees.setText( String.format( "%d", valueHolder.degrees ) );
    degrees.setFilters( new InputFilter[] { degreesFilter } );

    EditText minutes = view.findViewById( R.id.lat_long_editor_minutes_editor );
    minutes.setText( String.format( "%d", valueHolder.minutes ) );
    minutes.setFilters( new InputFilter[] { minutesFilter } );

    TextView seconds = view.findViewById( R.id.lat_long_editor_seconds_editor );
    seconds.setText( String.format( "%f", valueHolder.seconds ) );


    // Configuring the spinner comes straight from the Android JavaDocs:
    //  http://developer.android.com/guide/topics/ui/controls/spinner.html
    int arrayResource;
    String labelText;
    if ( showingLatitude ) {
      arrayResource = R.array.lat_long_editor_north_south;
      labelText = getContext().getString( R.string.lat_long_editor_ns_text );
    }
    else {
      arrayResource = R.array.lat_long_editor_east_west;
      labelText = getContext().getString( R.string.lat_long_editor_ew_text );
    }

    ArrayAdapter adapter = ArrayAdapter.createFromResource( getContext(), arrayResource, android.R.layout.simple_spinner_item );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    Spinner spinner = view.findViewById( R.id.lat_long_editor_ns_ew_picker );
    spinner.setAdapter( adapter );

    TextView label = view.findViewById( R.id.lat_long_editor_ns_text );
    label.setText( labelText );

    LatitudeLongitudeParser.Direction direction = valueHolder.direction;
    if ( ( LatitudeLongitudeParser.Direction.NORTH == direction ) || ( LatitudeLongitudeParser.Direction.EAST == direction ) ) {
      spinner.setSelection( 0, true );
    }
    else {
      spinner.setSelection( 1, true );
    }
  }


  @Override
  protected void onDialogClosed( boolean positiveResult ) {
    super.onDialogClosed( positiveResult );

    if ( !positiveResult ) {
      return;
    }

    // Have to convert the degrees, minutes, N/S (or E/W) to something we can store...
    TextView value = view.findViewById( R.id.lat_long_editor_degrees_editor );
    String degreesText = value.getText().toString();

    value = view.findViewById( R.id.lat_long_editor_minutes_editor );
    String minutesText = value.getText().toString();

    value = view.findViewById( R.id.lat_long_editor_seconds_editor );
    String secondsText = value.getText().toString();

    LatitudeLongitudeParser.Direction direction;
    Spinner spinner = view.findViewById( R.id.lat_long_editor_ns_ew_picker );
    int item = spinner.getSelectedItemPosition();
    LatitudeLongitudeParser holder;
    if ( showingLatitude ) {
      direction = ( 0 == item ? LatitudeLongitudeParser.Direction.NORTH : LatitudeLongitudeParser.Direction.SOUTH );
      holder = LatitudeLongitudeParser.latitude( direction, degreesText, minutesText, secondsText );
    }
    else {
      direction = ( 0 == item ? LatitudeLongitudeParser.Direction.EAST : LatitudeLongitudeParser.Direction.WEST );
      holder = LatitudeLongitudeParser.longitude( direction, degreesText, minutesText, secondsText );
    }


    String newValue = holder.toFullString( false );

    if ( callChangeListener( newValue ) ) {
      valueHolder = holder;
      persistString( newValue );
    }
  }


  @Override
  protected Object onGetDefaultValue(
    TypedArray  a,
    int         index
  ) {

    // TODO: Is this right???
    return a.getString( index );
  }


  @Override
  protected void onSetInitialValue(
    boolean restorePersistedValue,
    Object  defaultValue
  ) {

    /*
      I'm really not sure what this method is supposed to do. But, not having it do anything doesn't seem to cause
      any problems...

    // Have to parse the default value...
    String initValue;
    if ( restorePersistedValue ) {
      if ( null == defaultValue ) {
        initValue = getPersistedString( "" );
      }
      else {
        initValue = getPersistedString( defaultValue.toString() );
      }
    }
    else {
      initValue = defaultValue.toString();
    }

//    double
//    if ( showingLatitude ) {
//      LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( initValue );
//    }
//    else {
//      LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( initValue );
//    }
    */
  }


  /**
   * Ensure the value in an EditText is never less than a specified minimum value, nor greater than a specified
   *  maximum value.
   *
   * See https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
   */
  private static class ValueFilter
    implements InputFilter {

    private final int                                       min;
    private final int                                       max;

    ValueFilter( int min, int max ) {

      this.min = min;
      this.max = max;
    }


    @Override
    public CharSequence filter(
      CharSequence  source,
      int           start,
      int           end,
      Spanned       dest,
      int           dstart,
      int           dend
    ) {

      try {
        // Remove the string out of destination that is to be replaced
        String newVal = dest.toString().substring( 0, dstart ) +
          dest.toString().substring( dend, dest.toString().length() );

        // Add the new string in
        newVal = newVal.substring( 0, dstart ) + source.toString() + newVal.substring( dstart, newVal.length() );
        double input = Double.parseDouble( newVal );

        if ( input >= min && input <= max ) {
          return null;
        }
      }
      catch ( NumberFormatException nfe ) {
        // Not in the correct range...
      }

      return "";
    }

  }


} // End of LongitudeLatitudePreference class
