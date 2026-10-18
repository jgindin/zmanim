/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zehutyosef.upgrade;

import com.gindin.zehutyosef.FontInfo;
import com.gindin.zehutyosef.R;
import com.gindin.zehutyosef.ZehutYosefActivity;
import com.gindin.zehutyosef.settings.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Looper;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * The default shared preferences file written by the released BarMitzvah app (version code 77) holds the font,
 *  font size, keep-screen-on and full-screen choices. The reader and the settings screen must pick them up.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33, qualifiers = "w411dp-h869dp" )
public class UT_UpgradeReaderPreferences {

  private static final String                               FIXTURE =
    "/upgrade/barmitzvah-v77/shared_prefs/com.gindin.barmitzvah_preferences.xml";


  @Before
  public void installPrefsWrittenByReleasedApp() throws IOException, URISyntaxException {

    Context context = ApplicationProvider.getApplicationContext();

    URL fixture = UT_UpgradeReaderPreferences.class.getResource( FIXTURE );
    Assert.assertNotNull( "Missing fixture " + FIXTURE, fixture );

    // The default shared preferences file is named after the package; the flavors differ only in that name.
    Path sharedPrefs = new File( context.getApplicationInfo().dataDir, "shared_prefs" ).toPath();
    Files.createDirectories( sharedPrefs );
    Files.copy( new File( fixture.toURI() ).toPath(),
      sharedPrefs.resolve( context.getPackageName() + "_preferences.xml" ), StandardCopyOption.REPLACE_EXISTING );
  }


  @Test
  public void theKeysAreTheOnesTheReleasedAppWrote() {

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( ApplicationProvider.getApplicationContext() );

    Assert.assertEquals( "font_shlomo", prefs.getString( Settings.PreferenceKey.FontChoice.key, null ) );
    Assert.assertEquals( "20", prefs.getString( Settings.PreferenceKey.FontSize.key, null ) );
    Assert.assertFalse( prefs.getBoolean( Settings.PreferenceKey.KeepScreenOn.key, true ) );
    Assert.assertFalse( prefs.getBoolean( Settings.PreferenceKey.FullScreen.key, true ) );

    Assert.assertEquals( "pref_font_choice", Settings.PreferenceKey.FontChoice.key );
    Assert.assertEquals( "pref_font_size", Settings.PreferenceKey.FontSize.key );
    Assert.assertEquals( "pref_keep_screen_on", Settings.PreferenceKey.KeepScreenOn.key );
    Assert.assertEquals( "pref_full_screen_on", Settings.PreferenceKey.FullScreen.key );
  }


  @Test
  public void theSettingsScreenShowsTheSavedChoices() {

    // The reader sets up the fonts before the user can open the settings screen.
    Context context = ApplicationProvider.getApplicationContext();
    FontInfo.init( context, PreferenceManager.getDefaultSharedPreferences( context ) );

    try ( ActivityScenario<Settings> scenario = ActivityScenario.launch( Settings.class ) ) {

      scenario.onActivity( activity -> {

        activity.getFragmentManager().executePendingTransactions();
        PreferenceFragment fragment = (PreferenceFragment)activity.getFragmentManager().findFragmentById( android.R.id.content );
        Assert.assertNotNull( fragment );

        ListPreference font = (ListPreference)fragment.findPreference( "pref_font_choice" );
        Assert.assertEquals( "font_shlomo", font.getValue() );
        Assert.assertEquals( activity.getString( R.string.font_shlomo ), font.getSummary().toString() );

        ListPreference size = (ListPreference)fragment.findPreference( "pref_font_size" );
        Assert.assertEquals( "20", size.getValue() );
        Assert.assertEquals( "20", size.getSummary().toString() );

        Assert.assertFalse( ((CheckBoxPreference)fragment.findPreference( "pref_keep_screen_on" )).isChecked() );
        Assert.assertFalse( ((CheckBoxPreference)fragment.findPreference( "pref_full_screen_on" )).isChecked() );
      } );
    }
  }


  @Test
  public void theReaderAppliesTheSavedFontSizeFontAndScreenSetting() {

    try ( ActivityScenario<ZehutYosefActivity> scenario = ActivityScenario.launch( ZehutYosefActivity.class ) ) {

      scenario.onActivity( activity -> {

        // onCreate() reads the preferences on a background task and applies them when it finishes.
        settle();

        TextView textContent = activity.findViewById( R.id.text_content );
        Resources resources = activity.getResources();
        float expectedSizeInPixels = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 20, resources.getDisplayMetrics() );
        Assert.assertEquals( expectedSizeInPixels, textContent.getTextSize(), 0.01f );
        Assert.assertFalse( textContent.getKeepScreenOn() );
        Assert.assertEquals( "font_shlomo", activeFontId( activity ) );
      } );
    }
  }


  private static String activeFontId( ZehutYosefActivity activity ) {

    try {
      Field field = ZehutYosefActivity.class.getDeclaredField( "activeFont" );
      field.setAccessible( true );
      Object fontInfo = field.get( activity );
      return (String)fontInfo.getClass().getMethod( "getId" ).invoke( fontInfo );
    }
    catch ( ReflectiveOperationException e ) {
      throw new AssertionError( e );
    }
  }


  private static void settle() {

    for ( int attempt = 0; attempt < 25; attempt++ ) {

      Shadows.shadowOf( Looper.getMainLooper() ).idle();

      try {
        Thread.sleep( 20 );
      }
      catch ( InterruptedException interrupted ) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }

}
