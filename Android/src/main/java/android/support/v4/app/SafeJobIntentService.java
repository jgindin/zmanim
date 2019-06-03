package android.support.v4.app;

import android.os.Build;
import android.support.annotation.RestrictTo;

/**
 * @author rwondratschek
 * <p>
 * Copied from https://raw.githubusercontent.com/evernote/android-job/master/library/src/main/java/android/support/v4/app/SafeJobIntentService.java
 *
 * <p>
 * * @see https://issuetracker.google.com/issues/63622293
 */
@RestrictTo( RestrictTo.Scope.LIBRARY )
public abstract class SafeJobIntentService
  extends JobIntentService {

  @Override
  GenericWorkItem dequeueWork() {
    try {
      return super.dequeueWork();
    }
    catch ( SecurityException e ) {
      //e.printStackTrace();
      return null;
    }
  }


  @Override
  public void onCreate() {
    super.onCreate();
    // override mJobImpl with safe class to ignore SecurityException
    if ( Build.VERSION.SDK_INT >= 26 ) {
      mJobImpl = new SafeJobServiceEngineImpl( this );
    }
    else {
      mJobImpl = null;
    }
  }
}