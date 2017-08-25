package com.gindin.zmanlib.location;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.util.TimeZone;

/**
 *
 */
public class UT_ZmanimLocationJSON {

  @Test
  public void toJSON() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .locationName( "Seattle, Washington" )
      .atLatitude( 47.62 )
      .atLongitude( -122.33 )
      .fromProvider( "Zmanim" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    String json = ZmanLocationJSON.toJSON( location );
    Assert.assertEquals(
      "{\n" +
        "  \"ac\": -1,\n" +
        "  \"tz\": \"America/Los_Angeles\",\n" +
        "  \"pv\": \"Zmanim\",\n" +
        "  \"el\": 0,\n" +
        "  \"name\": \"Seattle, Washington\",\n" +
        "  \"lon\": -122.33,\n" +
        "  \"lat\": 47.62\n" +
        "}",
      json
    );
  }


  @Test
  public void fromJSON() throws JSONException {

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

    ZmanimLocation expected = new ZmanimLocation.Builder()
      .locationName( "Seattle, Washington" )
      .atLatitude( 47.62 )
      .atLongitude( -122.33 )
      .fromProvider( "Zmanim" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    Assert.assertEquals( expected, location );
  }
}
