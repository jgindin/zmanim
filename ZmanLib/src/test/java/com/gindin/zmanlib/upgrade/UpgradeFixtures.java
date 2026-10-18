/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.upgrade;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Gives the upgrade tests access to the saved state written by the released app, which is committed under
 *  {@code src/test/resources/upgrade/zmanim-v409}.
 */
final class UpgradeFixtures {

  private static final String                               ROOT = "/upgrade/zmanim-v409/";


  private UpgradeFixtures() {
  }


  static File file( String relativePath ) {

    URL url = UpgradeFixtures.class.getResource( ROOT + relativePath );
    if ( url == null ) {
      throw new IllegalStateException( "Missing fixture " + relativePath );
    }

    try {
      return new File( url.toURI() );
    }
    catch ( URISyntaxException e ) {
      throw new IllegalStateException( e );
    }
  }


  /**
   * Copies the fixture's {@code files/} directory into {@code target}, the way the app's own files directory
   *  would hold them. The copies get a fresh modification time, so they are not mistaken for stale files.
   */
  static void copyFilesDirectory( Path target ) throws IOException {

    try ( Stream<Path> files = Files.list( file( "files" ).toPath() ) ) {
      for ( Path source : (Iterable<Path>)files::iterator ) {
        Files.copy( source, target.resolve( source.getFileName().toString() ), StandardCopyOption.REPLACE_EXISTING );
      }
    }
  }

}
