/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.LocationError;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * When the location settings need changing, Play Services reports a {@link ResolvableApiException} whose
 *  {@link Status} carries the PendingIntent that shows the settings dialog. {@link LocationError} takes that Status out
 *  of the exception by reflection on {@code ApiException.mStatus} (Play Services exposes no getter that keeps the
 *  resolution) so that it can travel in a Bundle to the activity. In a minified build that field keeps its name only
 *  through the rule in proguard-rules.pro; without it, the lookup quietly yields no Status and the user is never offered
 *  the dialog.
 *<p/>
 *
 * The exception is built here the way Play Services builds it, so no Play Services UI or location request is needed.
 */
@RunWith( AndroidJUnit4.class )
public class LocationErrorStatusTest {

  @Test
  public void theResolutionSurvivesTheTripThroughABundle() {

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    PendingIntent resolution = PendingIntent.getActivity( context, 0, new Intent( context, ZmanimActivity.class ),
      PendingIntent.FLAG_IMMUTABLE );
    Status status = new Status( CommonStatusCodes.RESOLUTION_REQUIRED, "Location settings are off", resolution );

    LocationError error = new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION,
      new ResolvableApiException( status ) );
    assertNotNull( "LocationError could not read the Status out of the exception", error.apiExceptionStatus );

    LocationError delivered = LocationError.fromBundle( error.toBundle() );
    assertNotNull( "the Status did not survive the Bundle", delivered.apiExceptionStatus );
    assertEquals( LocationError.Code.CANNOT_DETERMINE_LOCATION, delivered.errorCode );
    assertEquals( CommonStatusCodes.RESOLUTION_REQUIRED, delivered.apiExceptionStatus.getStatusCode() );
    assertTrue( "the Status lost its resolution", delivered.apiExceptionStatus.hasResolution() );
  }

}
