package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;

import java.util.Map;

/**
 * A no-op prefs that always returns the default value.
 */
class TestPrefs
  implements SharedPreferences {

  @Override
  public boolean contains( String key ) {
    return false;
  }


  @Override
  public Map<String, ?> getAll() {
    return null;
  }


  @Override
  public String getString(
    String key,
    String defValue
  ) {
    return defValue;
  }


  @Override
  public int getInt(
    String  key,
    int     defValue
  ) {
    return defValue;
  }


  @Override
  public long getLong(
    String  key,
    long    defValue
  ) {
    return defValue;
  }


  @Override
  public float getFloat(
    String  key,
    float   defValue
  ) {
    return defValue;
  }


  @Override
  public boolean getBoolean(
    String  key,
    boolean defValue
  ) {
    return defValue;
  }


  @Override
  public Editor edit() {
    return null;
  }


  @Override
  public void registerOnSharedPreferenceChangeListener( OnSharedPreferenceChangeListener onSharedPreferenceChangeListener ) {

  }


  @Override
  public void unregisterOnSharedPreferenceChangeListener( OnSharedPreferenceChangeListener onSharedPreferenceChangeListener ) {

  }
}
