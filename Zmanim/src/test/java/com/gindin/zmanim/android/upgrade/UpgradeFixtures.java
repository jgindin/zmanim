/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.upgrade;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Puts the saved state written by the released app, committed under
 *  {@code src/test/resources/upgrade/zmanim-v409}, into the application's private data directory so that the
 *  production code finds it exactly where the released app left it.
 */
public final class UpgradeFixtures {

  private static final String                               ROOT = "/upgrade/zmanim-v409/";


  private UpgradeFixtures() {
  }


  /**
   * Copies {@code files/} into {@link Context#getFilesDir()} and {@code shared_prefs/} into the data directory's
   *  {@code shared_prefs}. Call before the code under test first reads them.
   */
  public static void install( Context context ) throws IOException {

    copy( "files", context.getFilesDir().toPath() );
    copy( "shared_prefs", new File( context.getApplicationInfo().dataDir, "shared_prefs" ).toPath() );
  }


  private static void copy(
    String  fixtureDirectory,
    Path    target
  ) throws IOException {

    Files.createDirectories( target );

    URL url = UpgradeFixtures.class.getResource( ROOT + fixtureDirectory );
    if ( url == null ) {
      throw new IllegalStateException( "Missing fixture directory " + fixtureDirectory );
    }

    try ( Stream<Path> sources = Files.list( new File( url.toURI() ).toPath() ) ) {
      for ( Path source : (Iterable<Path>)sources::iterator ) {
        Files.copy( source, target.resolve( source.getFileName().toString() ), StandardCopyOption.REPLACE_EXISTING );
      }
    }
    catch ( URISyntaxException e ) {
      throw new IllegalStateException( e );
    }
  }

}
