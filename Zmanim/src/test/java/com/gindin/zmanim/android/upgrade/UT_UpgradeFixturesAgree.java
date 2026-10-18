/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.upgrade;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * The {@code files/} fixtures exist in both :Zmanim and :ZmanLib so that each module's tests can read them from its
 *  own resources. They must stay the same bytes, since both describe the same data written by the released app.
 */
public class UT_UpgradeFixturesAgree {

  private static final String                               ZMANLIB_COPY =
    "../ZmanLib/src/test/resources/upgrade/zmanim-v409/files";

  private static final String                               ZMANIM_COPY =
    "src/test/resources/upgrade/zmanim-v409/files";


  @Test
  public void bothCopiesOfTheFilesDirectoryAreIdentical() throws IOException {

    File[] zmanLibFiles = new File( ZMANLIB_COPY ).listFiles();
    File[] zmanimFiles = new File( ZMANIM_COPY ).listFiles();
    Assert.assertNotNull( "Run from the :Zmanim project directory: " + ZMANLIB_COPY, zmanLibFiles );
    Assert.assertNotNull( ZMANIM_COPY, zmanimFiles );

    Assert.assertEquals( names( zmanLibFiles ), names( zmanimFiles ) );

    for ( File zmanLibFile : zmanLibFiles ) {
      byte[] expected = Files.readAllBytes( zmanLibFile.toPath() );
      byte[] actual = Files.readAllBytes( new File( ZMANIM_COPY, zmanLibFile.getName() ).toPath() );
      Assert.assertArrayEquals( zmanLibFile.getName(), expected, actual );
    }
  }


  private static String names( File[] files ) {

    String[] names = new String[ files.length ];
    for ( int i = 0; i < files.length; i++ ) {
      names[ i ] = files[ i ].getName();
    }

    Arrays.sort( names );
    return String.join( ",", names );
  }

}
