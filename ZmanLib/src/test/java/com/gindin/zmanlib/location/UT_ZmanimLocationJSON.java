package com.gindin.zmanlib.location;

import kotlinx.serialization.SerializationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.TimeZone;

/**
 * Besides the basic round trip, the {@code _JVM} and {@code _ANDROID} literals in here are verbatim output of the
 *  released location writer, captured by running it on the JVM and on Android. They pin the persisted format: the
 *  keys, numbers with no fractional part written as integers, two-space indentation and {@code userName} left out
 *  when there is none. The two captures differ in key order and in escaping "/"; the Android one is what is stored
 *  on users' devices.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ZmanimLocationJSON {

  /** The JVM output for Seattle. */
  private static final String SEATTLE_JVM =
    "{\n" +
      "  \"ac\": -1,\n" +
      "  \"tz\": \"America/Los_Angeles\",\n" +
      "  \"pv\": \"Zmanim\",\n" +
      "  \"el\": 0,\n" +
      "  \"name\": \"Seattle, Washington\",\n" +
      "  \"lon\": -122.33,\n" +
      "  \"lat\": 47.62\n" +
      "}";

  /** The device output for Seattle. */
  private static final String SEATTLE_ANDROID =
    "{\n" +
      "  \"lat\": 47.62,\n" +
      "  \"lon\": -122.33,\n" +
      "  \"el\": 0,\n" +
      "  \"tz\": \"America\\/Los_Angeles\",\n" +
      "  \"pv\": \"Zmanim\",\n" +
      "  \"ac\": -1,\n" +
      "  \"name\": \"Seattle, Washington\"\n" +
      "}";

  private static final String JERUSALEM_JVM =
    "{\n" +
      "  \"ac\": 12.5,\n" +
      "  \"tz\": \"Asia/Jerusalem\",\n" +
      "  \"pv\": \"gps\",\n" +
      "  \"el\": 754,\n" +
      "  \"name\": \"Yerushalayim \\\"Old\\\" City/ירושלים <b>\",\n" +
      "  \"lon\": 35.2354,\n" +
      "  \"userName\": \"jerusalem\",\n" +
      "  \"lat\": 31.778\n" +
      "}";

  private static final String JERUSALEM_ANDROID =
    "{\n" +
      "  \"lat\": 31.778,\n" +
      "  \"lon\": 35.2354,\n" +
      "  \"el\": 754,\n" +
      "  \"tz\": \"Asia\\/Jerusalem\",\n" +
      "  \"pv\": \"gps\",\n" +
      "  \"ac\": 12.5,\n" +
      "  \"name\": \"Yerushalayim \\\"Old\\\" City\\/ירושלים <b>\",\n" +
      "  \"userName\": \"jerusalem\"\n" +
      "}";

  /** No name, an empty user name and a tiny accuracy that needs exponent notation. */
  private static final String SYDNEY_JVM =
    "{\n" +
      "  \"ac\": 9.999999747378752E-6,\n" +
      "  \"tz\": \"Australia/Sydney\",\n" +
      "  \"pv\": \"network\",\n" +
      "  \"el\": 58.25,\n" +
      "  \"name\": \" - - \",\n" +
      "  \"lon\": 151.2093,\n" +
      "  \"userName\": \"\",\n" +
      "  \"lat\": -33.8688\n" +
      "}";

  private static final String SYDNEY_ANDROID =
    "{\n" +
      "  \"lat\": -33.8688,\n" +
      "  \"lon\": 151.2093,\n" +
      "  \"el\": 58.25,\n" +
      "  \"tz\": \"Australia\\/Sydney\",\n" +
      "  \"pv\": \"network\",\n" +
      "  \"ac\": 9.999999747378752E-6,\n" +
      "  \"name\": \" - - \",\n" +
      "  \"userName\": \"\"\n" +
      "}";

  private static final String TINY_JVM =
    "{\n" +
      "  \"ac\": 30,\n" +
      "  \"tz\": \"UTC\",\n" +
      "  \"pv\": \"Zmanim\",\n" +
      "  \"el\": 1.0E7,\n" +
      "  \"name\": \"Tiny\",\n" +
      "  \"lon\": 180,\n" +
      "  \"userName\": \"x y\",\n" +
      "  \"lat\": 1.0E-5\n" +
      "}";

  private static final String TINY_ANDROID =
    "{\n" +
      "  \"lat\": 1.0E-5,\n" +
      "  \"lon\": 180,\n" +
      "  \"el\": 10000000,\n" +
      "  \"tz\": \"UTC\",\n" +
      "  \"pv\": \"Zmanim\",\n" +
      "  \"ac\": 30,\n" +
      "  \"name\": \"Tiny\",\n" +
      "  \"userName\": \"x y\"\n" +
      "}";


  private static ZmanimLocation seattle() {
    return new ZmanimLocation.Builder()
      .locationName( "Seattle, Washington" )
      .atLatitude( 47.62 )
      .atLongitude( -122.33 )
      .fromProvider( "Zmanim" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();
  }


  private static ZmanimLocation jerusalem() {
    return new ZmanimLocation.Builder()
      .locationName( "Yerushalayim \"Old\" City/ירושלים <b>" )
      .atLatitude( 31.778 )
      .atLongitude( 35.2354 )
      .atElevation( 754.0 )
      .withAccuracy( 12.5f )
      .fromProvider( "gps" )
      .userName( "jerusalem" )
      .inTimeZone( TimeZone.getTimeZone( "Asia/Jerusalem" ) )
      .build();
  }


  private static ZmanimLocation sydney() {
    return new ZmanimLocation.Builder()
      .atLatitude( -33.8688 )
      .atLongitude( 151.2093 )
      .atElevation( 58.25 )
      .withAccuracy( 1.0E-5f )
      .fromProvider( "network" )
      .userName( "" )
      .inTimeZone( TimeZone.getTimeZone( "Australia/Sydney" ) )
      .build();
  }


  private static ZmanimLocation tiny() {
    return new ZmanimLocation.Builder()
      .locationName( "Tiny" )
      .atLatitude( 0.00001 )
      .atLongitude( 180.0 )
      .atElevation( 1.0E7 )
      .withAccuracy( 30f )
      .fromProvider( "Zmanim" )
      .userName( "x y" )
      .inTimeZone( TimeZone.getTimeZone( "UTC" ) )
      .build();
  }


  private static void assertSameLocation( ZmanimLocation expected, ZmanimLocation actual ) {

    Assertions.assertEquals( expected.getLatitude(), actual.getLatitude() );
    Assertions.assertEquals( expected.getLongitude(), actual.getLongitude() );
    Assertions.assertEquals( expected.getElevation(), actual.getElevation() );
    Assertions.assertEquals( expected.accuracy, actual.accuracy );
    Assertions.assertEquals( expected.providerName, actual.providerName );
    Assertions.assertEquals( expected.getTimeZone().getID(), actual.getTimeZone().getID() );
    Assertions.assertEquals( expected.hasName(), actual.hasName() );
    if ( expected.hasName() ) {
      Assertions.assertEquals( expected.getLocationName(), actual.getLocationName() );
    }
    Assertions.assertEquals(
      expected.originalUserName == null ? "" : expected.originalUserName,
      actual.originalUserName
    );
  }


  @Test
  public void toJSON() {

    // The key order is the one on devices.
    Assertions.assertEquals(
      "{\n" +
        "  \"lat\": 47.62,\n" +
        "  \"lon\": -122.33,\n" +
        "  \"el\": 0,\n" +
        "  \"tz\": \"America/Los_Angeles\",\n" +
        "  \"pv\": \"Zmanim\",\n" +
        "  \"ac\": -1,\n" +
        "  \"name\": \"Seattle, Washington\"\n" +
        "}",
      ZmanLocationJSON.toJSON( seattle() )
    );
  }


  @Test
  public void toJSONWritesUserNameAndNumbers() {

    Assertions.assertEquals(
      "{\n" +
        "  \"lat\": 31.778,\n" +
        "  \"lon\": 35.2354,\n" +
        "  \"el\": 754,\n" +
        "  \"tz\": \"Asia/Jerusalem\",\n" +
        "  \"pv\": \"gps\",\n" +
        "  \"ac\": 12.5,\n" +
        "  \"name\": \"Yerushalayim \\\"Old\\\" City/ירושלים <b>\",\n" +
        "  \"userName\": \"jerusalem\"\n" +
        "}",
      ZmanLocationJSON.toJSON( jerusalem() )
    );

    Assertions.assertEquals(
      "{\n" +
        "  \"lat\": -33.8688,\n" +
        "  \"lon\": 151.2093,\n" +
        "  \"el\": 58.25,\n" +
        "  \"tz\": \"Australia/Sydney\",\n" +
        "  \"pv\": \"network\",\n" +
        "  \"ac\": 9.999999747378752E-6,\n" +
        "  \"name\": \" - - \",\n" +
        "  \"userName\": \"\"\n" +
        "}",
      ZmanLocationJSON.toJSON( sydney() )
    );

    Assertions.assertEquals(
      "{\n" +
        "  \"lat\": 1.0E-5,\n" +
        "  \"lon\": 180,\n" +
        "  \"el\": 10000000,\n" +
        "  \"tz\": \"UTC\",\n" +
        "  \"pv\": \"Zmanim\",\n" +
        "  \"ac\": 30,\n" +
        "  \"name\": \"Tiny\",\n" +
        "  \"userName\": \"x y\"\n" +
        "}",
      ZmanLocationJSON.toJSON( tiny() )
    );
  }


  @Test
  public void fromJSON() {

    ZmanimLocation location = ZmanLocationJSON.fromJSON(
      "{\"lat\": \"47.62\",\n" +
        "\"lon\": \"-122.33\",\n" +
        "\"el\": \"0.0\",\n" +
        "\"tz\": \"America/Los_Angeles\",\n" +
        "\"pv\": \"Zmanim\",\n" +
        "\"ac\": \"-1.0\",\n" +
        "\"name\": \"Seattle, Washington\"\n" +
        "}"
    );

    Assertions.assertEquals( seattle(), location );
  }


  @Test
  public void readsTextWrittenByEarlierVersions() {

    assertSameLocation( seattle(), ZmanLocationJSON.fromJSON( SEATTLE_JVM ) );
    assertSameLocation( seattle(), ZmanLocationJSON.fromJSON( SEATTLE_ANDROID ) );
    assertSameLocation( jerusalem(), ZmanLocationJSON.fromJSON( JERUSALEM_JVM ) );
    assertSameLocation( jerusalem(), ZmanLocationJSON.fromJSON( JERUSALEM_ANDROID ) );
    assertSameLocation( sydney(), ZmanLocationJSON.fromJSON( SYDNEY_JVM ) );
    assertSameLocation( sydney(), ZmanLocationJSON.fromJSON( SYDNEY_ANDROID ) );
    assertSameLocation( tiny(), ZmanLocationJSON.fromJSON( TINY_JVM ) );
    assertSameLocation( tiny(), ZmanLocationJSON.fromJSON( TINY_ANDROID ) );
  }


  @Test
  public void aMissingUserNameReadsAsEmpty() {

    Assertions.assertEquals( "", ZmanLocationJSON.fromJSON( SEATTLE_ANDROID ).originalUserName );
  }


  @Test
  public void aLocationWithoutProviderCannotBeRead() {

    String json = ZmanLocationJSON.toJSON(
      new ZmanimLocation.Builder().atLatitude( 40.0 ).atLongitude( -74.0 ).fromProvider( null ).build()
    );

    Assertions.assertFalse( json.contains( "\"pv\"" ) );
    Assertions.assertThrows( SerializationException.class, () -> ZmanLocationJSON.fromJSON( json ) );
  }


  @Test
  public void unknownKeysAreIgnored() {

    ZmanimLocation location = ZmanLocationJSON.fromJSON(
      SEATTLE_ANDROID.replace( "{\n", "{\n  \"extra\": {\"nested\": [1, 2, 3]},\n  \"flag\": true,\n" )
    );

    assertSameLocation( seattle(), location );
  }


  @Test
  public void roundTripsThroughTheSavedStream() throws IOException {

    for ( ZmanimLocation original : new ZmanimLocation[] { seattle(), jerusalem(), sydney(), tiny() } ) {

      ByteArrayOutputStream output = new ByteArrayOutputStream();
      original.save( output );

      assertSameLocation( original, ZmanimLocation.load( new ByteArrayInputStream( output.toByteArray() ) ) );
    }
  }


  @Test
  public void rewritingStoredTextKeepsItsContent() {

    for ( String json : new String[] { JERUSALEM_ANDROID, SYDNEY_ANDROID, TINY_ANDROID } ) {
      String rewritten = ZmanLocationJSON.toJSON( ZmanLocationJSON.fromJSON( json ) );

      // Only the escaping of "/" differs between Android's text and ours; the content is identical.
      Assertions.assertEquals( json.replace( "\\/", "/" ), rewritten );
    }
  }


  @Test
  public void malformedTextIsRejected() {

    String[] bad = {
      "",
      "not json",
      "[1, 2]",
      "{\"lat\": 1.0",
      "{}",
      "{\"lat\": \"abc\", \"lon\": 1, \"el\": 0, \"tz\": \"UTC\", \"pv\": \"x\", \"ac\": -1, \"name\": \"n\"}",
      "{\"lat\": 1, \"lon\": 1, \"el\": 0, \"tz\": \"UTC\", \"pv\": \"x\", \"ac\": -1}",
      "{\"lat\": null, \"lon\": 1, \"el\": 0, \"tz\": \"UTC\", \"pv\": \"x\", \"ac\": -1, \"name\": \"n\"}",
    };

    for ( String json : bad ) {
      Assertions.assertThrows( IllegalArgumentException.class, () -> ZmanLocationJSON.fromJSON( json ), json );
    }
  }
}
