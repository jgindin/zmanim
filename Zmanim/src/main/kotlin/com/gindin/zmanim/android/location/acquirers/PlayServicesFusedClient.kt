/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * [FusedClient] over Play services. The calls that need location access are made only after
 * [FusedLocationSource] has checked [grantOf], so the missing-permission lint is suppressed on
 * them.
 */
class PlayServicesFusedClient(
  private val fused: FusedLocationProviderClient,
  private val settings: SettingsClient,
  private val looper: Looper = Looper.getMainLooper(),
) : FusedClient {

  constructor(
    context: Context
  ) : this(
    LocationServices.getFusedLocationProviderClient(context),
    LocationServices.getSettingsClient(context),
  )

  @SuppressLint("MissingPermission")
  override suspend fun lastLocation(): Location? = fused.lastLocation.await()

  override suspend fun checkSettings(request: LocationRequest) {
    settings
      .checkLocationSettings(LocationSettingsRequest.Builder().addLocationRequest(request).build())
      .await()
  }

  @SuppressLint("MissingPermission")
  @Suppress("TooGenericExceptionCaught") // A failure to start is reported, whatever it is.
  override fun requestUpdates(
    request: LocationRequest,
    onLocation: (Location) -> Unit,
    onError: (Exception) -> Unit,
  ): () -> Unit {
    val callback =
      object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
          result.lastLocation?.let(onLocation)
        }
      }
    try {
      fused.requestLocationUpdates(request, callback, looper).addOnFailureListener(DIRECT, onError)
    } catch (e: RuntimeException) {
      onError(e)
    }
    return { fused.removeLocationUpdates(callback) }
  }

  companion object {

    /** How much location access [context]'s app has. */
    @JvmStatic
    fun grantOf(context: Context): LocationGrant {
      fun granted(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
      return when {
        granted(Manifest.permission.ACCESS_FINE_LOCATION) -> LocationGrant.PRECISE
        granted(Manifest.permission.ACCESS_COARSE_LOCATION) -> LocationGrant.APPROXIMATE
        else -> LocationGrant.NONE
      }
    }

    /** Whether [context]'s device has Play services it can use. */
    @JvmStatic
    fun playServicesAvailable(context: Context): Boolean =
      GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) ==
        ConnectionResult.SUCCESS
  }
}

/**
 * Runs a task's listener on the thread that completes the task, so waiting for it does not need the
 * main thread.
 */
private val DIRECT = Executor { it.run() }

/** The task's result, or its exception; cancelling the caller stops waiting for it. */
private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
  addOnCompleteListener(DIRECT) { task ->
    val exception = task.exception
    when {
      exception != null -> continuation.resumeWithException(exception)
      task.isCanceled -> continuation.cancel()
      else -> continuation.resume(task.result)
    }
  }
}
