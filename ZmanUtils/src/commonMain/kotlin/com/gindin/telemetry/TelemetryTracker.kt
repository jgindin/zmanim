/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.telemetry

public interface TelemetryTracker {
  public fun trackScreen(screenName: String)

  public fun trackEvent(name: String, parameters: Map<String, Any?> = emptyMap())

  public fun setUserProperty(key: String, value: String)

  public fun recordNonFatal(throwable: Throwable)
}
