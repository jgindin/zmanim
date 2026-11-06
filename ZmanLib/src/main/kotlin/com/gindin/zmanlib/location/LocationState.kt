/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/** What a [LocationSource] knows about where the device is. */
public sealed interface LocationState {

  /**
   * A fix has arrived and is being named.
   *
   * @property approximate true when the user granted approximate location only, so the fix can be
   *   off by a few kilometers.
   */
  public data class Resolving(
    public val fix: ZmanimLocation,
    public val approximate: Boolean = false,
  ) : LocationState

  /** The fix has a name, a time zone and an elevation, and the zmanim can be calculated for it. */
  public data class Resolved(
    public val location: ZmanimLocation,
    public val approximate: Boolean = false,
  ) : LocationState

  /**
   * No fix could be had.
   *
   * @property lastKnown the location saved by an earlier run, if there is one, so the app can keep
   *   showing the zmanim for it.
   */
  public data class Unavailable(
    public val reason: UnavailableReason,
    public val lastKnown: ZmanimLocation?,
  ) : LocationState
}

/** Why a [LocationSource] has no fix. */
public sealed interface UnavailableReason {

  /** The user has not granted location access. */
  public data object NoPermission : UnavailableReason

  /**
   * Location services are turned off on the device.
   *
   * @property resolution lets the screen ask the user to turn them on; null when the system offers
   *   no way to.
   */
  public data class ServicesOff(public val resolution: SettingsResolution?) : UnavailableReason

  /** The device has no usable Google Play services, which provide the fix. */
  public data object NoPlayServices : UnavailableReason

  /** No fix arrived in time. */
  public data object Timeout : UnavailableReason

  /** Anything else went wrong while asking for a fix. */
  public data class Failed(public val cause: Throwable?) : UnavailableReason
}

/**
 * The platform's way to ask the user to turn location services on. `:ZmanLib` does not depend on
 * Android, so the Android adapter implements this around what Play services hands back.
 */
public interface SettingsResolution
