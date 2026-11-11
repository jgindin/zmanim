/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.gindin.zmanim.android.location.ZmanLocationUtil
import com.gindin.zmanlib.location.BundledGeoidGrid
import com.gindin.zmanlib.location.LocationQuery
import com.gindin.zmanlib.location.LocationState
import com.gindin.zmanlib.location.UnavailableReason
import com.gindin.zmanlib.location.ZmanimLocation
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UT_FusedLocationSourceFactory {

  private val app = ApplicationProvider.getApplicationContext<Application>()

  @Before
  @After
  fun forgetShared() {
    FusedLocationSource.forgetShared()
  }

  @Test
  fun withoutLocationAccessTheSourceOffersTheLocationSavedByTheLastRun() = runBlocking {
    val saved =
      ZmanimLocation.Builder()
        .locationName("Saved")
        .atLatitude(31.7683)
        .atLongitude(35.2137)
        .inTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"))
        .build()
    ZmanLocationUtil.save(app, saved)

    val states =
      withTimeout(10.seconds) {
        FusedLocationSource.create(app).locations(LocationQuery(acceptRecent = true)).toList()
      }

    val unavailable = states.single() as LocationState.Unavailable
    assertEquals(UnavailableReason.NoPermission, unavailable.reason)
    assertEquals("Saved", unavailable.lastKnown?.locationName)
  }

  @Test
  fun theGeoidGridIsTheBundledAsset() = runBlocking {
    val geoid = BundledGeoidGrid({ app.assets.open(FusedLocationSource.GEOID_ASSET) })

    assertNotNull(geoid.heightMeters(31.7683, 35.2137))
    assertNull(geoid.loadFailure)
  }
}
