/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Puts the app's persistent state into a known shape before an activity is launched, and takes it away again.
 *<p/>
 *
 * The tests run inside the app's own process, so {@link Context#getSharedPreferences} hands them the very same
 *  cached instances the app reads. Copying an XML file into {@code shared_prefs} would be invisible to a preferences
 *  object the process has already loaded (an earlier test in the same run almost always loaded it), so preferences
 *  are written through the {@code SharedPreferences} API instead: the values come from the committed fixture XML, and
 *  are applied to the live object.
 *<p/>
 *
 * Plain files, such as the location cache, are read from disk each time the app needs them, so they are copied
 *  byte for byte.
 *<p/>
 *
 * The fixture is the synthetic saved state of the released app (versionCode 409) that
 *  {@code src/test/resources/upgrade/zmanim-v409} holds for the Robolectric tests; the build exposes the same
 *  directory to this source set as assets, so nothing is duplicated.
 */
final class AppState {

  private static final String                               FIXTURE_ROOT = "zmanim-v409";

  /** Preference files that make up the static latitude/longitude location (Albany, New York). */
  private static final String                               LOCATION_PREFS = ZmanimPrefs.LOCATION_MANAGEMENT.name();

  /** The location cache; it names the coordinates in {@link #LOCATION_PREFS}, so the app need not geocode them. */
  private static final String                               LOCATION_CACHE_FILE = "CachedLocations";


  private AppState() {
  }


  /**
   * Grants everything the app asks for at runtime, so that no permission dialog can sit in front of the activity
   *  under test. Permissions that do not exist on the running API level are left out; granting an unknown one fails.
   */
  static GrantPermissionRule grantRuntimePermissions() {

    List<String> permissions = new ArrayList<>();
    permissions.add( Manifest.permission.ACCESS_FINE_LOCATION );
    permissions.add( Manifest.permission.ACCESS_COARSE_LOCATION );
    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ) {
      permissions.add( Manifest.permission.ACCESS_BACKGROUND_LOCATION );
    }
    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
      permissions.add( Manifest.permission.POST_NOTIFICATIONS );
    }

    return GrantPermissionRule.grant( permissions.toArray( new String[ 0 ] ) );
  }


  /** Removes every file and preference the app has written. */
  static void reset() throws IOException {

    Context context = targetContext();

    for ( ZmanimPrefs prefs : ZmanimPrefs.values() ) {
      context.getSharedPreferences( prefs.name(), Context.MODE_PRIVATE ).edit().clear().commit();
    }

    File[] files = context.getFilesDir().listFiles();
    if ( files != null ) {
      for ( File file : files ) {
        if ( !file.delete() ) {
          throw new IOException( "Cannot delete " + file );
        }
      }
    }
  }


  /**
   * Starts from nothing, then installs a static latitude/longitude location (Albany, New York) together with the
   *  cache entry that names it, as an install of the current version would have it. The header then shows the cached
   *  name without any network access.
   *<p/>
   *
   * The cache entry only matches the saved coordinates when the time zone agrees, so the time zone override that the
   *  fixture carries is replaced by the zone the cache entry was saved with. The saved version is the current one so
   *  that no upgrade work (which wipes the cache for old installs) runs.
   */
  static void installStaticLocation() throws IOException {

    reset();
    installPreferences( LOCATION_PREFS );
    installFile( LOCATION_CACHE_FILE );

    edit( ZmanimPrefs.LOCATION_MANAGEMENT )
      .putString( "TIME_ZONE_AREA", "America" )
      .putString( "TIME_ZONE_LOCALE", "New_York" )
      .commit();
    edit( ZmanimPrefs.VERSION )
      .putInt( "lastKnownCodeVersion", BuildConfig.VERSION_CODE )
      .commit();
  }


  /** Starts from nothing, then installs every file and preference the released version 409 app had saved. */
  static void installUpgradeState() throws IOException {

    reset();

    AssetManager assets = InstrumentationRegistry.getInstrumentation().getContext().getAssets();
    for ( String name : assets.list( FIXTURE_ROOT + "/shared_prefs" ) ) {
      installPreferences( name.substring( 0, name.length() - ".xml".length() ) );
    }
    for ( String name : assets.list( FIXTURE_ROOT + "/files" ) ) {
      installFile( name );
    }
  }


  /**
   * Switches the saved location from latitude/longitude to a place the user typed in, the way the location
   *  preferences screen stores it.
   */
  static void useTypedPlace( String place ) {

    edit( ZmanimPrefs.LOCATION_MANAGEMENT )
      .putBoolean( "LATITUDE_LONGITUDE_POSITION", false )
      .putBoolean( "DYNAMIC_POSITION", false )
      .putBoolean( "STATIC_POSITION", true )
      .putString( "PLACE", place )
      .commit();
  }


  static SharedPreferences.Editor edit( ZmanimPrefs prefs ) {
    return targetContext().getSharedPreferences( prefs.name(), Context.MODE_PRIVATE ).edit();
  }


  private static void installPreferences( String preferencesName ) throws IOException {

    SharedPreferences.Editor editor = targetContext()
      .getSharedPreferences( preferencesName, Context.MODE_PRIVATE )
      .edit()
      .clear();

    try ( InputStream in = openFixture( "shared_prefs/" + preferencesName + ".xml" ) ) {

      NodeList entries = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        .parse( in ).getDocumentElement().getChildNodes();

      for ( int i = 0; i < entries.getLength(); i++ ) {
        Node node = entries.item( i );
        if ( node.getNodeType() == Node.ELEMENT_NODE ) {
          put( editor, (Element)node );
        }
      }
    }
    catch ( IOException e ) {
      throw e;
    }
    catch ( Exception e ) {
      throw new IOException( "Cannot read fixture preferences " + preferencesName, e );
    }

    if ( !editor.commit() ) {
      throw new IOException( "Cannot write preferences " + preferencesName );
    }
  }


  private static void put(
    SharedPreferences.Editor  editor,
    Element                   entry
  ) {

    String key = entry.getAttribute( "name" );
    String value = entry.getAttribute( "value" );

    switch ( entry.getTagName() ) {
      case "boolean":
        editor.putBoolean( key, Boolean.parseBoolean( value ) );
        break;
      case "int":
        editor.putInt( key, Integer.parseInt( value ) );
        break;
      case "long":
        editor.putLong( key, Long.parseLong( value ) );
        break;
      case "float":
        editor.putFloat( key, Float.parseFloat( value ) );
        break;
      case "string":
        editor.putString( key, entry.getTextContent() );
        break;
      default:
        throw new IllegalArgumentException( "Unsupported preference type " + entry.getTagName() );
    }
  }


  private static void installFile( String fileName ) throws IOException {

    try (
      InputStream in = openFixture( "files/" + fileName );
      OutputStream out = new FileOutputStream( new File( targetContext().getFilesDir(), fileName ) )
    ) {
      byte[] buffer = new byte[ 4096 ];
      int count;
      while ( ( count = in.read( buffer ) ) != -1 ) {
        out.write( buffer, 0, count );
      }
    }
  }


  private static InputStream openFixture( String path ) throws IOException {
    return InstrumentationRegistry.getInstrumentation().getContext().getAssets().open( FIXTURE_ROOT + "/" + path );
  }


  private static Context targetContext() {
    return InstrumentationRegistry.getInstrumentation().getTargetContext();
  }

}
