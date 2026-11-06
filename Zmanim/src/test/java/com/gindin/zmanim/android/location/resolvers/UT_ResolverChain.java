/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * How {@link LocationResolver#resolve} walks the chain of resolvers, and what {@link PassthroughResolver}, the last
 *  one in the app's chain, hands back.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ResolverChain {

  /** Records what it was asked, and answers with a fixed result. */
  private static class FakeResolver
    extends LocationResolver {

    private final ZmanimLocation                            answer;

    final List<String>                                      calls = new ArrayList<>();

    FakeResolver( ZmanimLocation answer ) {
      this.answer = answer;
    }

    @Override
    protected ZmanimLocation resolveFromCoordinates( ZmanimLocation zmanimLocation ) {
      calls.add( "coordinates" );
      return answer;
    }

    @Override
    protected ZmanimLocation resolveFromName( ZmanimLocation zmanimLocation ) {
      calls.add( "name" );
      return answer;
    }
  }


  private static final ZmanimLocation                       COORDINATES = new ZmanimLocation.Builder()
    .atLatitude( 47.62 )
    .atLongitude( -122.33 )
    .build();

  private static final ZmanimLocation                       TYPED_NAME = new ZmanimLocation.Builder()
    .locationName( "Seattle" )
    .userName( "Seattle" )
    .build();

  private static final ZmanimLocation                       RESOLVED = new ZmanimLocation.Builder()
    .locationName( "Seattle, Washington" )
    .atLatitude( 47.62 )
    .atLongitude( -122.33 )
    .build();


  private static ZmanimLocation resolve( ZmanimLocation location, LocationResolver first, LocationResolver... rest ) {
    return first.resolve( location, Arrays.asList( rest ) );
  }


  @Test
  public void aLocationWithCoordinatesIsLookedUpByItsCoordinates() {

    FakeResolver resolver = new FakeResolver( RESOLVED );
    resolve( COORDINATES, resolver );

    Assertions.assertEquals( List.of( "coordinates" ), resolver.calls );
  }


  @Test
  public void aLocationWithoutCoordinatesIsLookedUpByItsName() {

    FakeResolver resolver = new FakeResolver( RESOLVED );
    resolve( TYPED_NAME, resolver );

    Assertions.assertEquals( List.of( "name" ), resolver.calls );
  }


  @Test
  public void theFirstResolverThatAnswersWinsAndTheRestAreNotAsked() {

    FakeResolver first = new FakeResolver( null );
    FakeResolver second = new FakeResolver( RESOLVED );
    FakeResolver third = new FakeResolver( TYPED_NAME );

    Assertions.assertSame( RESOLVED, resolve( COORDINATES, first, second, third ) );

    Assertions.assertEquals( List.of( "coordinates" ), first.calls );
    Assertions.assertEquals( List.of( "coordinates" ), second.calls );
    Assertions.assertTrue( third.calls.isEmpty() );
  }


  @Test
  public void whenNoResolverAnswersTheResultIsNull() {
    Assertions.assertNull( resolve( COORDINATES, new FakeResolver( null ), new FakeResolver( null ) ) );
  }


  @Test
  public void thePassthroughResolverHandsBackTheLocationItWasGiven() {

    Assertions.assertSame( COORDINATES, resolve( COORDINATES, new PassthroughResolver() ) );
    Assertions.assertSame( TYPED_NAME, resolve( TYPED_NAME, new PassthroughResolver() ) );
  }


  /** So a typed name that no resolver knows ends the chain unresolved, and the app reports that it failed. */
  @Test
  public void aTypedNameNobodyKnowsEndsTheChainUnresolved() {

    ZmanimLocation result = resolve( TYPED_NAME, new FakeResolver( null ), new PassthroughResolver() );

    Assertions.assertFalse( result.isResolved() );
  }


  /** And coordinates that nobody can name end it unnamed, but with their coordinates. */
  @Test
  public void coordinatesNobodyCanNameEndTheChainWithoutAName() {

    ZmanimLocation result = resolve( COORDINATES, new FakeResolver( null ), new PassthroughResolver() );

    Assertions.assertFalse( result.hasName() );
    Assertions.assertTrue( result.hasCoordinates() );
  }

}  // End of UT_ResolverChain class
