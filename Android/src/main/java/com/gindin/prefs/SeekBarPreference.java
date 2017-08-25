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
import com.gindin.zmanlib.notification.ZmanNotificationScheduler;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Inspired by: http://stackoverflow.com/questions/9012305/is-there-a-way-to-add-a-seekbar-to-my-actual-preference-screen/30236143#30236143
 * <p/>
 * See also:
 *  preference_seekbar.xml
 */
public class SeekBarPreference
  extends Preference {

  /**
   * Problem is that these {@link SeekBarPreference} objects get recycled. We know when this is happening because
   *  onBindViewHolder() is called and we'll already have our widgets assigned. So, at this point, we can easily
   *  cleanup any listeners, etc... The problem is that the SeekBarPreference which used to use those widgets never
   *  does find out that it's views are being recycled for someone else to use. So, when this instance is being
   *  bound, we'll see if someone else used to have these widgets, and tell them to cleanup.
   */
  private static Map<EditText, SeekBarPreference>           cache = new HashMap<>();

  private CheckBox                                          enabled;
  private SeekBar                                           seekBar;
  private EditText                                          currentValueDisplay;

  private int                                               defaultValue;
  private int                                               progress;

  private CurrentValueTextWatcher                           currentValueTextWatcher;

  private SeekBarListener                                   seekBarListener;

  private View.OnClickListener                              enabledClickListener;


  @SuppressWarnings( "unused" )
  public SeekBarPreference( Context context ) {
    this( context, null, 0 );
  }


  @SuppressWarnings( "unused" )
  public SeekBarPreference(
    Context       context,
    AttributeSet  attrs
  ) {
    this( context, attrs, 0 );
  }


  public SeekBarPreference(
    Context                 context,
    @Nullable AttributeSet  attrs,
    int                     defStyle
  ) {
    super( context, attrs, defStyle );
    setLayoutResource( R.layout.preference_seekbar );

    currentValueTextWatcher = new CurrentValueTextWatcher();
    seekBarListener = new SeekBarListener();

    enabledClickListener = new EnabledClickListener();
  }


  @Override
  public void onDetached() {
    cleanup();

    // Someone has to clear the cache...
    cache.clear();

    super.onDetached();
  }


  @Override
  public void onDependencyChanged(
    Preference  dependency,
    boolean     disableDependent
  ) {

    super.onDependencyChanged( dependency, disableDependent );

    if ( isViewReady() ) {
      enabled.setEnabled( !disableDependent );
      enableControls( !disableDependent );
    }
  }


  @Override
  protected Parcelable onSaveInstanceState() {
    final Parcelable superState = super.onSaveInstanceState();

    // Check whether this Preference is persistent (continually saved)
    if ( isPersistent() ) {
      // No need to save instance state since it's persistent, use superclass state
      return superState;
    }

    // Create instance of custom BaseSavedState
    final SavedState myState = new SavedState( superState );
    // Set the state's value with the class member that holds current setting value
    myState.value = progress;

    return myState;
  }


  @Override
  protected void onRestoreInstanceState( Parcelable state ) {

    // Check whether we saved the state in onSaveInstanceState
    if ( state == null || !state.getClass().equals( SavedState.class ) ) {
      // Didn't save the state, so call superclass
      super.onRestoreInstanceState( state );
      return;
    }

    // Cast state to custom BaseSavedState and pass to superclass
    SavedState myState = (SavedState)state;
    super.onRestoreInstanceState( myState.getSuperState() );

    // Set this Preference's widget to reflect the restored state
    progress = myState.value;
    initView();
  }


  @Override
  public void onBindViewHolder( PreferenceViewHolder holder ) {

    cleanup();

    super.onBindViewHolder( holder );

    seekBar = (SeekBar)holder.findViewById( R.id.seekbar );
    seekBar.setProgress( progress );
    seekBar.setMax( ZmanNotificationScheduler.MAX_ALERT_TIME / ZmanNotificationScheduler.MIN_TO_MS_CONVERSION );

    currentValueDisplay = (EditText)holder.findViewById( R.id.value );
    enabled = (CheckBox)holder.findViewById( R.id.enable );

    updateCache();


    // Forces everything to get setup correctly.
    initView();

    // Add the listeners now, so we don't get anything while initializing.
    enabled.setOnClickListener( enabledClickListener );
    seekBar.setOnSeekBarChangeListener( seekBarListener );
    currentValueDisplay.addTextChangedListener( currentValueTextWatcher );
  }


  private void updateCache() {

    final SeekBarPreference other = cache.remove( currentValueDisplay );
    if ( ( other != null ) && ( other != this ) ) {
      other.cleanup();
    }

    cache.put( currentValueDisplay, this );
  }


  private void cleanup() {

    if ( seekBar != null ) {
      seekBar.setOnSeekBarChangeListener( null );
    }

    if ( enabled != null ) {
      enabled.setOnClickListener( null );
    }

    if ( currentValueDisplay != null ) {
      currentValueDisplay.removeTextChangedListener( currentValueTextWatcher );
    }
  }


  @Override
  protected void onPrepareForRemoval() {

    cleanup();
    super.onPrepareForRemoval();
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

    final boolean thisAlertIsEnabled = progress >= 0;
    enabled.setChecked( thisAlertIsEnabled );

    final boolean areAlertsEnabled = getSharedPreferences().getBoolean( getDependency(), true );
    enableControls( areAlertsEnabled && thisAlertIsEnabled );
    updateValueDisplay( true );
  }


  private void enableControls( boolean isEnabled ) {

    // Do NOT try to call setEnabled() on 'this' -- since we call this method from onBindViewHolder() and other times
    //  while performing a layout, it will cause an exception.
    seekBar.setEnabled( isEnabled );
    currentValueDisplay.setEnabled( isEnabled );
  }


  private void setValue( int value ) {

    if ( value != progress ) {

       persistInt( value );
       progress = value;

      // The onSetInitialValue gets called before onBindView.
      // NOTE: The 'best practice' says you should notify here, but... we're OK. We don't need to do anything until
      //  all the preferences are set.
//      if ( isViewReady() ) {
//        notifyChanged();
//      }
    }
  }


  private void updateValueDisplay( boolean initialUpdate ) {

    currentValueDisplay.removeTextChangedListener( currentValueTextWatcher );

    if ( progress >= 0 ) {

      // Don't do anything we don't have to do. (Note if the value is blank, it won't parse to an int, and we'll blow up...)
      int alertValue = getAlertValue();

      if ( progress != alertValue ) {
        currentValueDisplay.setText( String.valueOf( progress ) );
      }
    }
    else {
      currentValueDisplay.setText( "" );
    }

    if ( !initialUpdate ) {
      currentValueDisplay.addTextChangedListener( currentValueTextWatcher );
    }
  }


  private void updateSeekBar() {
    seekBar.setProgress( progress );
  }


  private boolean isViewReady() {
    // Will be null when we're setting up.
    return currentValueDisplay != null;
  }


  private int getAlertValue() {
    final String value = currentValueDisplay.getText().toString();
    return ( value.isEmpty() ? ZmanNotificationScheduler.NO_ALERT : Integer.parseInt( value ) );
  }


  /**
   * Listen for the user to slide the seek bar, and make sure the text value is updated.
   */
  private class SeekBarListener
    implements OnSeekBarChangeListener {

    boolean                                                 isTracking = false;

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
      updateValueDisplay( false );
    }


    @Override
    public void onStartTrackingTouch( SeekBar seekBar ) {
      isTracking = true;
    }


    @Override
    public void onStopTrackingTouch( SeekBar seekBar ) {
      isTracking = false;
    }

  }


  /**
   * Listen for the user to enter a value in the text box, and make sure the seek bar is updated.
   */
  private class CurrentValueTextWatcher
    implements TextWatcher {

    @Override
    public void beforeTextChanged(
      CharSequence s,
      int start,
      int count,
      int after
    ) {

      if ( !seekBarListener.isTracking ) {
        seekBar.setOnSeekBarChangeListener( null );
      }
    }


    @Override
    public void onTextChanged(
      CharSequence s,
      int start,
      int before,
      int count
    ) {

    }


    @Override
    public void afterTextChanged( Editable s ) {

      if ( !seekBarListener.isTracking ) {
        int alertTime = getAlertValue();
        setValue( alertTime );
        updateSeekBar();

        seekBar.setOnSeekBarChangeListener( seekBarListener );
      }
    }
  }


  /**
   * Save/restore the data. See https://developer.android.com/guide/topics/ui/settings.html
   */
  private static class SavedState
    extends BaseSavedState {

    int value;


    SavedState( Parcelable superState ) {
      super( superState );
    }


    SavedState( Parcel source ) {
      super( source );
      // Get the current preference's value
      value = source.readInt();
    }


    @Override
    public void writeToParcel(
      Parcel  dest,
      int     flags
    ) {
      super.writeToParcel( dest, flags );
      // Write the preference's value
      dest.writeInt( value );
    }


    // Standard creator object using an instance of this class
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

      public SavedState createFromParcel( Parcel in ) {
        return new SavedState( in );
      }


      public SavedState[] newArray( int size ) {
        return new SavedState[ size ];
      }
    };
 }


  private class EnabledClickListener
    implements View.OnClickListener {

    @Override
    public void onClick( final View v ) {
      // Reflects the new value
      final boolean isThisAlertEnabled = enabled.isChecked();
      enableControls( isThisAlertEnabled );

      int newValue;
      if ( isThisAlertEnabled ) {
        newValue = getAlertValue();
        if ( newValue == ZmanNotificationScheduler.NO_ALERT ) {
          newValue = defaultValue;
        }
      }
      else {
        newValue = ZmanNotificationScheduler.NO_ALERT;
      }

      setValue( newValue );
      updateValueDisplay( false );
      updateSeekBar();
    }
  }
}
