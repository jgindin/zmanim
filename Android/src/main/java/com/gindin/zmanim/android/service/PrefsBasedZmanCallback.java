package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.NotifiedZmanim;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.File;
import java.util.Calendar;

/**
 * Zman callback that works from the shared preferences.
 */
abstract class PrefsBasedZmanCallback
  extends ZmanCallback {

  private final SharedPreferences                           zmanimSelectionPrefs;

  private final SharedPreferences                           personalPrefs;

  /**
   * Where to read/write the notified zmanim.
   */
  private final File                                        filesDir;

  /**
   * DO NOT access this directly! Always use the getNotifiedZmanim method to ensure using for the correct date!
   */
  private NotifiedZmanim                                    notifiedZmanim;


  PrefsBasedZmanCallback(
    Context context
  ) {
    this.zmanimSelectionPrefs = context.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
    this.personalPrefs = context.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
    this.filesDir = context.getFilesDir();
  }


  @Override
  public String getPreferredZman( final Zmanim.Type type ) {
    return zmanimSelectionPrefs.getString( type.name, null );
  }


  @Override
  public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
    return personalPrefs.getInt( type.name(), -1 );
  }


  @Override
  public boolean shouldShowAnyNotifications() {
    return personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true );
  }


  @Override
  public boolean shouldNotifyOnShabbat() {
    return personalPrefs.getBoolean( PersonalPreferences.Prefs.ENABLE_NOTIFICATIONS_ON_SHABBAT.name(), false );
  }


  @Override
  public boolean hasBeenNotified(
    Calendar    now,
    Zmanim.Type type
  ) {

    getNotifiedZmanim( now );
    return notifiedZmanim.contains( type );
  }


  @Override
  public void completed(
    Calendar    now,
    Zmanim.Type type
  ) {

    getNotifiedZmanim( now );
    notifiedZmanim.notified( type );

    SaveNotifiedZmanimTask task = new SaveNotifiedZmanimTask();
    task.execute( notifiedZmanim );
  }


  @Override
  public boolean formatTimeWithSeconds() {
    return personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
  }


  /**
   * Because of the process to possibly load the previously notified zmanim, it is assumed that this method will NOT
   * be invoked on the main thread.
   */
  private void getNotifiedZmanim( final Calendar now ) {

    if ( notifiedZmanim == null ) {
      notifiedZmanim = NotifiedZmanim.restore( filesDir );
    }

    if ( notifiedZmanim != null ) {
      if ( !notifiedZmanim.isForDate( now ) ) {
        notifiedZmanim = null;
      }
    }

    if ( notifiedZmanim == null ) {
      notifiedZmanim = new NotifiedZmanim( now );
    }
  }


  private class SaveNotifiedZmanimTask
    extends AsyncTask<NotifiedZmanim, Void, Void> {

    @Override
    protected Void doInBackground( NotifiedZmanim... params ) {
      params[ 0 ].save( filesDir );

      return null;
    }
  }

}
