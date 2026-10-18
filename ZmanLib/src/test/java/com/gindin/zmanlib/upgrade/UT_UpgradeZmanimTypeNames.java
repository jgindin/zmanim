/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.upgrade;

import com.gindin.zmanlib.zman.Zmanim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * The constant name of each {@link Zmanim.Type} is the key of its notification offset in the saved personal
 *  preferences, and its display name is the key of the saved zman selection. Neither may change, nor may the order,
 *  without a migration of the saved state.
 */
public class UT_UpgradeZmanimTypeNames {

  @Test
  public void constantAndDisplayNamesMatchTheReleasedApp() throws IOException {

    List<String> released = Files.readAllLines(
      UpgradeFixtures.file( "zmanim-type-display-names.txt" ).toPath(), StandardCharsets.UTF_8 );

    List<String> current = new ArrayList<>();
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      current.add( type.name() + "=" + type.displayName );
    }

    Assertions.assertEquals( released, current );
  }

}
