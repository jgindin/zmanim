/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zehutyosef;

import com.gindin.zehutyosef.settings.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FontInfo {

  static final List<FontInfo>                               FONT_INFOS = new ArrayList<>();


  /**
   * Initiializes all the fonts that we might use, and returns the key of the default font.
   */
  public static String init( final Context context, final SharedPreferences prefs ) {

    FONT_INFOS.add( new FontInfo( "font_taamey_david",     context.getString( R.string.font_taamey_david ),    "fonts/TaameyDavidCLM-Medium.ttf" ) );
    FONT_INFOS.add( new FontInfo( "font_shlomo",           context.getString( R.string.font_shlomo ),          "fonts/Shlomo.ttf" ) );
    FONT_INFOS.add( new FontInfo( "font_taamey_ashkenaz",  context.getString( R.string.font_taamey_ashkenaz ), "fonts/TaameyAshkenaz-Medium.ttf" ) );
    FONT_INFOS.add( new FontInfo( "font_taamey_frank",     context.getString( R.string.font_taamey_frank ),    "fonts/TaameyFrankCLM-Medium.ttf" ) );

    return prefs.getString( Settings.PreferenceKey.FontChoice.key, FontInfo.FONT_INFOS.get( 0 ).id );
  }



  @NonNull
  public static FontInfo getByIdName( String fontChoice ) {

    for ( FontInfo fontInfo : FONT_INFOS ) {
      if ( fontChoice.equals( fontInfo.id ) ) {
        return fontInfo;
      }
    }

    // TODO: We should log something here...
    return FONT_INFOS.get( 0 );
  }



  private final String                                      id;

  private final String                                      name;

  private final String                                      relativePath;

  private Typeface                                          typeface;

  // Empirically, this is the default text size.
  private float                                             size = 14f;


  FontInfo(
    final String  id,
    final String  name,
    final String  relativePath
  ) {
    this.id = id;
    this.name = name;
    this.relativePath = relativePath;
  }


  public Typeface getTypeface() {
    return typeface;
  }


  public float getSize() {
    return size;
  }


  public void setSize( final float size ) {
    this.size = size;
  }


  public String getName() {
    return name;
  }


  public String getId() {
    return id;
  }


  void load( AssetManager assetManager ) {

    if ( typeface == null ) {
      typeface = Typeface.createFromAsset( assetManager, relativePath );
    }
  }
}
