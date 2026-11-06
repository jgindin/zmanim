/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withTimeoutOrNull

/** What [ResolverChain.fromCoordinates] ends with: always a location, named or not. */
public sealed interface CoordinatesResult {

  public val location: ZmanimLocation

  /** A source named the place. */
  public data class Named(override val location: ZmanimLocation) : CoordinatesResult

  /**
   * No source could name the place; [location] is the request exactly as it was given, so its
   * coordinates are never changed. It has no name, so it must not be cached as a resolved place.
   */
  public data class Unnamed(override val location: ZmanimLocation) : CoordinatesResult
}

/** A source did not answer within the chain's time limit for each source. */
public class PlaceSourceTimeoutException(public val timeout: Duration) :
  Exception("The place source did not answer within $timeout")

/**
 * Asks each source in turn, in the order given, until one finds the place. A source that fails,
 * throws or takes longer than [timeoutPerSource] is skipped. Cancelling the caller cancels the
 * source being asked, and no other source is asked.
 */
public class ResolverChain(
  private val sources: List<PlaceResolver>,
  private val timeoutPerSource: Duration = DEFAULT_TIMEOUT_PER_SOURCE,
) {

  /**
   * Finds the place the user typed. Ends in [PlaceResult.Failed] rather than [PlaceResult.NotFound]
   * when any source could not look, because then the place may well exist.
   */
  public suspend fun fromName(request: ZmanimLocation): PlaceResult {

    var firstFailure: Throwable? = null
    for (source in sources) {
      when (val result = ask(source) { it.fromName(request) }) {
        is PlaceResult.Found -> return result
        is PlaceResult.Failed -> firstFailure = firstFailure ?: result.cause
        PlaceResult.NotFound -> {}
      }
    }

    return firstFailure?.let { PlaceResult.Failed(it) } ?: PlaceResult.NotFound
  }

  /** Names the place at the request's coordinates, or hands the request back unnamed. */
  public suspend fun fromCoordinates(request: ZmanimLocation): CoordinatesResult {

    for (source in sources) {
      val result = ask(source) { it.fromCoordinates(request) }
      if (result is PlaceResult.Found) {
        return CoordinatesResult.Named(result.location)
      }
    }

    return CoordinatesResult.Unnamed(request)
  }

  // A source is code this chain does not control (the system geocoder, a file, the bundled data),
  //  so any exception it throws is that source's failure and the next source is asked.
  @Suppress("TooGenericExceptionCaught")
  private suspend fun ask(
    source: PlaceResolver,
    lookUp: suspend (PlaceResolver) -> PlaceResult,
  ): PlaceResult {

    return try {
      withTimeoutOrNull(timeoutPerSource) { lookUp(source) }
        ?: PlaceResult.Failed(PlaceSourceTimeoutException(timeoutPerSource))
    } catch (e: CancellationException) {
      // Rethrows when the caller was cancelled; otherwise the source cancelled itself.
      currentCoroutineContext().ensureActive()
      PlaceResult.Failed(e)
    } catch (e: Exception) {
      PlaceResult.Failed(e)
    }
  }

  public companion object {

    /** Long enough for the system geocoder on a slow connection. */
    public val DEFAULT_TIMEOUT_PER_SOURCE: Duration = 10.seconds
  }
}
