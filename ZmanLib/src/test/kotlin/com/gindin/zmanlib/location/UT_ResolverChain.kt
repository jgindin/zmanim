/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import java.io.IOException
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UT_ResolverChain {

  private val newYork = TimeZone.getTimeZone("America/New_York")

  private val typed =
    ZmanimLocation.Builder().locationName("Albany").userName("Albany").inTimeZone(newYork).build()

  private val fix =
    ZmanimLocation.Builder().atLatitude(42.65).atLongitude(-73.76).inTimeZone(newYork).build()

  private val albany =
    ZmanimLocation.Builder()
      .locationName("Albany, New York")
      .atLatitude(42.6526)
      .atLongitude(-73.7562)
      .inTimeZone(newYork)
      .build()

  private fun answering(result: PlaceResult) = FakePlaceResolver { result }

  @Test
  fun theFirstSourceThatFindsThePlaceWinsAndTheRestAreNotAsked() = runTest {
    val first = answering(PlaceResult.NotFound)
    val second = answering(PlaceResult.Found(albany))
    val third = answering(PlaceResult.Found(fix))

    val result = ResolverChain(listOf(first, second, third)).fromName(typed)

    assertEquals(PlaceResult.Found(albany), result)
    assertEquals(listOf(typed), first.nameRequests)
    assertEquals(listOf(typed), second.nameRequests)
    assertFalse(third.wasAsked)
  }

  @Test
  fun aNameIsLookedUpByNameAndCoordinatesByCoordinates() = runTest {
    val source = answering(PlaceResult.NotFound)
    val chain = ResolverChain(listOf(source))

    chain.fromName(typed)
    chain.fromCoordinates(fix)

    assertEquals(listOf(typed), source.nameRequests)
    assertEquals(listOf(fix), source.coordinateRequests)
  }

  @Test
  fun aSourceThatThrowsIsSkipped() = runTest {
    val broken = FakePlaceResolver { throw IOException("no network") }
    val next = answering(PlaceResult.Found(albany))

    assertEquals(PlaceResult.Found(albany), ResolverChain(listOf(broken, next)).fromName(typed))
  }

  @Test
  fun aSourceThatFailsIsSkipped() = runTest {
    val failing = answering(PlaceResult.Failed(IOException("no network")))
    val next = answering(PlaceResult.Found(albany))

    assertEquals(PlaceResult.Found(albany), ResolverChain(listOf(failing, next)).fromName(typed))
  }

  @Test
  fun aSourceThatTakesTooLongIsSkippedAfterItsTimeout() = runTest {
    val slow = FakePlaceResolver {
      delay(60.seconds)
      PlaceResult.Found(fix)
    }
    val next = answering(PlaceResult.Found(albany))

    val result = ResolverChain(listOf(slow, next), timeoutPerSource = 5.seconds).fromName(typed)

    assertEquals(PlaceResult.Found(albany), result)
    assertEquals(5.seconds.inWholeMilliseconds, testScheduler.currentTime)
  }

  @Test
  fun eachSourceGetsItsOwnTimeout() = runTest {
    val slowButInTime = FakePlaceResolver {
      delay(4.seconds)
      PlaceResult.NotFound
    }
    val alsoSlowButInTime = FakePlaceResolver {
      delay(4.seconds)
      PlaceResult.Found(albany)
    }

    val result =
      ResolverChain(listOf(slowButInTime, alsoSlowButInTime), timeoutPerSource = 5.seconds)
        .fromName(typed)

    assertEquals(PlaceResult.Found(albany), result)
  }

  @Test
  fun aNameNoSourceKnowsIsNotFound() = runTest {
    val chain =
      ResolverChain(listOf(answering(PlaceResult.NotFound), answering(PlaceResult.NotFound)))

    assertEquals(PlaceResult.NotFound, chain.fromName(typed))
  }

  /** "Could not look" is a different message for the user than "no such place". */
  @Test
  fun aNameThatASourceCouldNotLookUpIsAFailureNotANotFound() = runTest {
    val offline = IOException("no network")
    val chain =
      ResolverChain(listOf(answering(PlaceResult.Failed(offline)), answering(PlaceResult.NotFound)))

    val result = chain.fromName(typed)

    assertInstanceOf(PlaceResult.Failed::class.java, result)
    assertSame(offline, (result as PlaceResult.Failed).cause)
  }

  @Test
  fun aTimeoutIsReportedAsAFailure() = runTest {
    val slow = FakePlaceResolver {
      delay(60.seconds)
      PlaceResult.Found(albany)
    }

    val result = ResolverChain(listOf(slow), timeoutPerSource = 5.seconds).fromName(typed)

    assertInstanceOf(PlaceResult.Failed::class.java, result)
    assertInstanceOf(
      PlaceSourceTimeoutException::class.java,
      (result as PlaceResult.Failed).cause,
    )
  }

  @Test
  fun namedCoordinatesAreNamed() = runTest {
    val result = ResolverChain(listOf(answering(PlaceResult.Found(albany)))).fromCoordinates(fix)

    assertEquals(CoordinatesResult.Named(albany), result)
  }

  /**
   * The last resort keeps the request exactly as it was, coordinates included, and does not name
   * it.
   */
  @Test
  fun coordinatesNoSourceCanNameAreHandedBackUnnamedAndUnchanged() = runTest {
    val chain =
      ResolverChain(
        listOf(answering(PlaceResult.NotFound), answering(PlaceResult.Failed(IOException())))
      )

    val result = chain.fromCoordinates(fix)

    assertInstanceOf(CoordinatesResult.Unnamed::class.java, result)
    assertSame(fix, result.location)
    assertFalse(result.location.hasName())
  }

  @Test
  fun anEmptyChainFindsNothing() = runTest {
    val chain = ResolverChain(emptyList())

    assertEquals(PlaceResult.NotFound, chain.fromName(typed))
    assertEquals(CoordinatesResult.Unnamed(fix), chain.fromCoordinates(fix))
  }

  @Test
  fun cancellingTheLookupCancelsTheSourceAndAsksNoOther() = runTest {
    val sourceCancelled = CompletableDeferred<Boolean>()
    val waiting = FakePlaceResolver {
      try {
        awaitCancellation()
      } finally {
        sourceCancelled.complete(true)
      }
    }
    val next = answering(PlaceResult.Found(albany))

    val lookup = launch { ResolverChain(listOf(waiting, next)).fromName(typed) }
    runCurrent()
    lookup.cancel()
    lookup.join()

    assertTrue(sourceCancelled.await())
    assertTrue(lookup.isCancelled)
    assertFalse(next.wasAsked)
  }

  /** A source's own cancellation (say, its internal timeout) is its failure, not the caller's. */
  @Test
  fun aSourceThatCancelsItselfIsSkipped() = runTest {
    val cancelsItself = FakePlaceResolver { throw CancellationException("source gave up") }
    val next = answering(PlaceResult.Found(albany))

    assertEquals(
      PlaceResult.Found(albany),
      ResolverChain(listOf(cancelsItself, next)).fromName(typed),
    )
  }
}
