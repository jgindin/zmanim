/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zehutyosef;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.entity.Library;

import android.content.Context;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 */
class About {

  private static final Comparator<Library>                  libraryComparator = new LibraryComparator();

  private LibsBuilder                                       aboutBuilder;


  void show( Context context ) {

    if ( aboutBuilder == null ) {
      build( context );
    }

    aboutBuilder.start( context );
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
