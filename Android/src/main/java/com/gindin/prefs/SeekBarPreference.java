/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gindin.prefs;

import com.gindin.zmanim.android.R;
import com.gindin.zmanlib.ZmanNotificationScheduler;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Copied from: http://stackoverflow.com/questions/9012305/is-there-a-way-to-add-a-seekbar-to-my-actual-preference-screen/30236143#30236143
 * <p/>
 * See also:
 *  preference_seekbar.xml
 */
public class SeekBarPreference
	extends Preference
	implements OnSeekBarChangeListener {

	private CheckBox                                          enabled;
	private SeekBar                                           seekBar;
	private EditText                                          currentValueDisplay;

	private int                                               defaultValue;
	private int                                               progress;


	public SeekBarPreference( Context context ) {
		this( context, null, 0 );
	}


	public SeekBarPreference(
		Context       context,
		AttributeSet  attrs
	) {
		this( context, attrs, 0 );
	}


	public SeekBarPreference(
		Context       context,
		AttributeSet  attrs,
		int           defStyle
	) {
		super( context, attrs, defStyle );
		setLayoutResource( R.layout.preference_seekbar );
	}


	@Override
	protected void onBindView( View view ) {
		super.onBindView( view );

		seekBar = (SeekBar)view.findViewById( R.id.seekbar );
		seekBar.setProgress( progress );
		seekBar.setOnSeekBarChangeListener( this );
		seekBar.setMax( ZmanNotificationScheduler.MAX_ALERT_TIME / ZmanNotificationScheduler.MIN_TO_MS_CONVERSION );

		currentValueDisplay = (EditText)view.findViewById( R.id.value );
		final View.OnFocusChangeListener valueChangedFocusListener = new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(
				View    view,
				boolean hasFocus
			) {

				if ( !hasFocus ) {
					int alertTime = Integer.valueOf( currentValueDisplay.getText().toString() );
					setValue( alertTime );
				}
			}
		};

		enabled = (CheckBox)view.findViewById( R.id.enable );
		enabled.setChecked( progress >= 0 );
		View.OnClickListener enabledListener = new View.OnClickListener() {
			@Override
			public void onClick( final View v ) {
				// Reflects the new value
				enableControls( enabled.isChecked() );
				if ( enabled.isChecked() ) {
					setValue( defaultValue );
				}
				else {
					setValue( ZmanNotificationScheduler.NO_ALERT );
				}
			}
		};

		// Forces everything to get setup correctly.
		initView();

		// Add the listeners now, so we don't get anything while initializing.
		enabled.setOnClickListener( enabledListener );
		currentValueDisplay.setOnFocusChangeListener( valueChangedFocusListener );
	}


	@Override
	protected void onPrepareForRemoval() {

		enabled.setOnClickListener( null );
		currentValueDisplay.setOnFocusChangeListener( null );

		super.onPrepareForRemoval();
	}


	@Override
	public void onProgressChanged(
		SeekBar   seekBar,
		int       progress,
		boolean   fromUser
	) {
		if ( !fromUser ) {
			return;
		}

		setValue( progress );
	}


	@Override
	public void onStartTrackingTouch( SeekBar seekBar ) {
		// not used
	}


	@Override
	public void onStopTrackingTouch( SeekBar seekBar ) {
		// not used
	}


	@Override
	protected void onSetInitialValue(
		boolean restoreValue,
		Object  defaultValue
	) {

		int value;
		if ( defaultValue instanceof Integer ) {
			//noinspection UnnecessaryUnboxing
			value = ((Integer)defaultValue).intValue();
		}
		else {
			value = ZmanNotificationScheduler.NO_ALERT;
		}

		setValue( restoreValue ? getPersistedInt( progress ) : value );
	}


	@Override
	protected Object onGetDefaultValue(
		TypedArray  a,
		int         index
	) {
		defaultValue = a.getInt( index, 0 );
		return defaultValue;
	}


	private void initView() {

		enableControls( enabled.isChecked() );
		updateDisplay();
	}


	private void enableControls( boolean enabled ) {

		seekBar.setEnabled( enabled );
		currentValueDisplay.setEnabled( enabled );
	}



	private void setValue( int value ) {
		if ( shouldPersist() ) {
			persistInt( value );
		}

		if ( value != progress ) {
			progress = value;
			notifyChanged();

			// Will be null when we're setting up. The onSetInitialValue gets called before onBindView.
			if ( currentValueDisplay != null ) {
				updateDisplay();
			}
		}
	}


	private void updateDisplay() {

		if ( !enabled.isChecked() ) {
			currentValueDisplay.getText().clear();
		}
		else if ( progress >= 0 ) {
			currentValueDisplay.setText( String.valueOf( progress ) );
		}
		else {
			currentValueDisplay.setText( String.valueOf( defaultValue ) );
		}
	}
}
