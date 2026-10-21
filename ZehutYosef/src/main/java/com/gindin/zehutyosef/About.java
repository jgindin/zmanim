/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zehutyosef;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.WeakHashMap;

/**
 *
 */
class About {

  private static final Comparator<Library>                  libraryComparator = new LibraryComparator();

  private static boolean                                   insetsHandlerRegistered;

  private LibsBuilder                                       aboutBuilder;


  void show( Context context ) {

    if ( aboutBuilder == null ) {
      build( context );
    }

    registerInsetsHandler( context );

    aboutBuilder.start( context );
  }


  /**
   * LibsActivity is created by the library, so this is the only place to reach it. Once the platform draws apps edge to
   *  edge (targetSdk 35+), its Toolbar would sit under the status bar and its list under the navigation bar.
   */
  private static synchronized void registerInsetsHandler( Context context ) {

    if ( !insetsHandlerRegistered ) {
      ( (Application)context.getApplicationContext() ).registerActivityLifecycleCallbacks( new LibsInsetsHandler() );
      insetsHandlerRegistered = true;
    }
  }


  private void build( Context context ) {

    String description = context.getString( R.string.about_description_text ) + "<br/><br/>" +
      "<small><i>Type: " + BuildConfig.BUILD_TYPE + "</i></small>";

    aboutBuilder = new LibsBuilder()
      .withFields( "define_zehut_yosef", "define_zmanim", "define_kosherjava", "define_materialdrawer", "define_AboutLibraries" )
      .withAutoDetect( false )
      .withAboutAppName( context.getString( R.string.app_name ) )
      .withAboutDescription( description )
      .withAboutVersionShownCode( false )
      .withActivityTheme( R.style.EliPurple_NoActionBar )
      .withActivityTitle( context.getString( R.string.action_about_title ) )
      .withLibraryComparator( libraryComparator );

  }


  /**
   * Keeps LibsActivity's content out of the system bars. The Toolbar grows by the status bar's height instead of the
   *  whole content being pushed down, so that its color also fills the area behind the status bar icons, which stay
   *  readable against it. Only LibsActivity is touched; any other activity of this app is left alone.
   */
  private static class LibsInsetsHandler
    implements Application.ActivityLifecycleCallbacks {

    // The listener is installed once per instance, because the Toolbar's original size is captured when it is.
    private final Set<Activity>                             handled = Collections.newSetFromMap( new WeakHashMap<>() );


    @Override
    public void onActivityStarted( final Activity activity ) {

      if ( !( activity instanceof LibsActivity ) || !handled.add( activity ) ) {
        return;
      }

      // The content view is only set after the library's onCreate has started, which is after onActivityCreated.
      View content = activity.findViewById( android.R.id.content );
      Toolbar toolbar = activity.findViewById( R.id.toolbar );
      if ( content == null || toolbar == null ) {
        return;
      }

      final int toolbarHeight = toolbar.getLayoutParams().height;
      final int toolbarPaddingTop = toolbar.getPaddingTop();
      ViewCompat.setOnApplyWindowInsetsListener( content, ( view, windowInsets ) -> {
        Insets bars = windowInsets.getInsets( WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout() );

        view.setPadding( bars.left, 0, bars.right, bars.bottom );

        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
        params.height = toolbarHeight + bars.top;
        toolbar.setLayoutParams( params );
        toolbar.setPadding( toolbar.getPaddingLeft(), toolbarPaddingTop + bars.top, toolbar.getPaddingRight(), toolbar.getPaddingBottom() );

        return windowInsets;
      } );
      ViewCompat.requestApplyInsets( content );
    }


    @Override public void onActivityCreated( final Activity activity, final Bundle savedInstanceState ) { }
    @Override public void onActivityResumed( final Activity activity ) { }
    @Override public void onActivityPaused( final Activity activity ) { }
    @Override public void onActivityStopped( final Activity activity ) { }
    @Override public void onActivitySaveInstanceState( final Activity activity, final Bundle outState ) { }
    @Override public void onActivityDestroyed( final Activity activity ) { }
  }


  private static class LibraryComparator
    implements Comparator<Library>, Serializable {

    @Override
    public int compare(
      final Library lhs,
      final Library rhs
    ) {

      // I'm not a fan of this, but...I really want these things ordered particularly...
      String lhAuthor = lhs.getAuthor();
      String rhAuthor = rhs.getAuthor();
      if ( lhAuthor.equals( rhAuthor ) ) {
        return 0;
      }

      if ( "Isaac Azose".equals( lhAuthor ) ) {
        return -1;
      }
      if ( "Isaac Azose".equals( rhAuthor ) ) {
        return 1;
      }

      if ( "Jay Gindin".equals( lhAuthor ) ) {
        return -1;
      }
      if ( "Jay Gindin".equals( rhAuthor ) ) {
        return 1;
      }

      if ( "Eliyahu Hershfeld".equals( lhAuthor ) ) {
        return -1;
      }
      if ( "Eliyahu Hershfeld".equals( rhAuthor ) ) {
        return 1;
      }

      return lhs.compareTo( rhs );
    }
  }
} // End of About class
