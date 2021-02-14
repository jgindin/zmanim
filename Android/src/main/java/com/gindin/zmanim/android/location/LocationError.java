/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;

/**
 * Describes an error while trying to determine the user's location.
 */
public class LocationError {

  /**
   * Enumerates the possible errors when trying to determine the location.
   */
  public enum Code {
    CANNOT_DETERMINE_LOCATION,
    BAD_INPUT,
    NO_LOCATION_MANAGER_SERVICE,
    NO_PROVIDERS,
    NO_PERMISSIONS,
    NO_PLAY_SERVICES
  }

  private static final String                               ERROR_CODE = "error code";
  private static final String                               EXCEPTION = "exception";
  private static final String                               MESSAGE = "message";

  @NonNull
  public final Code                                         errorCode;

  @Nullable
  public final String                                       message;

  @Nullable
  public final Status                                       apiExceptionStatus;


  public LocationError(
    @NonNull Code    errorCode
  ) {
    this( errorCode, null, (Status)null );
  }

  
  public LocationError(
    @NonNull  Code    errorCode,
    @Nullable String  message
  ) {
    this( errorCode, message, (Status)null );
  }


  public LocationError(
    @NonNull Code                     errorCode,
    @Nullable ResolvableApiException  exception
  ) {
    this( errorCode, null, exception );
  }


  private LocationError(
    @NonNull  Code                    errorCode,
    @Nullable String                  message,
    @Nullable ResolvableApiException  exception
  ) {

    this( errorCode, message, extractStatus( exception ) );
  }


  private LocationError(
    @NonNull  Code    errorCode,
    @Nullable String  message,
    @Nullable Status  apiExceptionStatus
  ) {
    this.errorCode = errorCode;
    this.message = message;
    this.apiExceptionStatus = apiExceptionStatus;
  }


  @Override
  public String toString() {
    return "LocationError{" +
      "errorCode=" + errorCode +
      ", message='" + message + '\'' +
      '}';
  }


  public Bundle toBundle() {


    Bundle bundle = new Bundle();
    bundle.putSerializable( ERROR_CODE, errorCode );
    bundle.putParcelable( EXCEPTION, apiExceptionStatus );
    bundle.putString( MESSAGE, message );

    return bundle;
  }


  public static LocationError fromBundle( Bundle bundle ) {

    return new LocationError(
      (Code)bundle.getSerializable( ERROR_CODE ),
      bundle.getString( MESSAGE ),
      (Status)bundle.getParcelable( EXCEPTION )
    );
  }


  @Nullable
  private static Status extractStatus( @Nullable ResolvableApiException exception ) {

    // I'm not proud of this, but... We need to be able to send the exception, or at least enough information, in a
    //  bundle so that it can make its way all the way back to the ZmanimActivity where the error can be handled.
    //  I originally was putting the exception in the bundle as a Serializable. Because...it is. Except, it isn't.
    //  The Status member does not implement Serializable. It does implement Parceable, though, so, that's good.
    //  Except, there's no way to actually get the Status field, and there's not enough information exposed to be
    //  able to recreate it. Hence, this ugly fix -- resort to reflection :-(
    Status status;
    try {
      final Field statusField = ApiException.class.getDeclaredField( "mStatus" );
      statusField.setAccessible( true );
      status = (Status)statusField.get( exception );
    }
    catch ( Exception e ) {
      // Well...something went wrong.
      status = null;
    }

    return status;
  }


}
