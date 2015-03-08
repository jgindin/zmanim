/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.location.LatitudeLongitudeParser;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
	private LatitudeLongitudeParser.Holder                    valueHolder;


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
			valueHolder = LatitudeLongitudeParser.LATITUDE_PARSER.parse( value );
		}
		else {
			valueHolder = LatitudeLongitudeParser.LONGITUDE_PARSER.parse( value );
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

		TextView degrees = (TextView)view.findViewById( R.id.lat_long_editor_degrees_editor );
		degrees.setText( "" + valueHolder.getDegrees() );

		TextView minutes = (TextView)view.findViewById( R.id.lat_long_editor_minutes_editor );
		minutes.setText( "" + valueHolder.getMinutes() );


		// Configuring the spinner comes straight from the Android JavaDocs:
		//  http://developer.android.com/guide/topics/ui/controls/spinner.html
		int arrayResource;
		if ( showingLatitude ) {
			arrayResource = R.array.lat_long_editor_north_south;
		}
		else {
			arrayResource = R.array.lat_long_editor_east_west;
		}

		ArrayAdapter adapter = ArrayAdapter.createFromResource( getContext(), arrayResource, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinner = (Spinner)view.findViewById( R.id.lat_long_editor_ns_ew_picker );
		spinner.setAdapter( adapter );

		LatitudeLongitudeParser.Direction direction = valueHolder.getDirection();
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
		TextView value = (TextView)view.findViewById( R.id.lat_long_editor_degrees_editor );
		String degreesText = value.getText().toString();

		boolean parseMinutes = true;

		int degrees;
		int minutes = 0;

		try {
			degrees = Integer.parseInt( degreesText );
		}
		catch ( NumberFormatException e ) {
			// Maybe the user doesn't have separate minutes & degrees, so they just entered something like "47.5461"?
			try {
				double degreesDouble = Double.parseDouble( degreesText );

				// Casting the double to an int will give us the degrees, by just lopping off the part after the decimal.
				degrees = (int)degreesDouble;

				// Now, convert the decimal portion to the minutes. http://geography.about.com/library/howto/htdegrees.htm
				minutes = (int)( ( degreesDouble - degrees ) * 60 );

				parseMinutes = false;
			}
			catch ( NumberFormatException e1 ) {
				// Nope... just give up.
				degrees = 0;
				minutes = 0;
			}
		}

		if ( parseMinutes ) {
			value = (TextView)view.findViewById( R.id.lat_long_editor_minutes_editor );
			String minutesText = value.getText().toString();

			try {
				minutes = Integer.parseInt( minutesText );
			}
			catch ( NumberFormatException e ) {
				minutes = 0;
			}
		}

		LatitudeLongitudeParser.Direction direction;
		Spinner spinner = (Spinner)view.findViewById( R.id.lat_long_editor_ns_ew_picker );
		int item = spinner.getSelectedItemPosition();
		if ( showingLatitude ) {
			direction = ( 0 == item ? LatitudeLongitudeParser.Direction.NORTH : LatitudeLongitudeParser.Direction.SOUTH );
		}
		else {
			direction = ( 0 == item ? LatitudeLongitudeParser.Direction.EAST : LatitudeLongitudeParser.Direction.WEST );
		}

		LatitudeLongitudeParser.Holder holder = new LatitudeLongitudeParser.Holder( direction, degrees, minutes );
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

//		double
//		if ( showingLatitude ) {
//			LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( initValue );
//		}
//		else {
//			LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( initValue );
//		}
		*/
	}


} // End of LongitudeLatitudePreference class
