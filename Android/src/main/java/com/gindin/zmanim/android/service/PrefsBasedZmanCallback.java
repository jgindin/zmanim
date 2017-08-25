package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.notification.NotifiedZmanim;
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


  PrefsBasedZmanCallback(
    Context context
  ) {
    this.zmanimSelectionPrefs = context.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
    this.personalPrefs = context.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );

    File filesDir = context.getFilesDir();
    NotifiedZmanim.manager.init( filesDir );
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
    Calendar    zmanTime,
    Zmanim.Type type
  ) {

    NotifiedZmanim notifiedZmanim = NotifiedZmanim.manager.get( zmanTime );
    return notifiedZmanim.contains( type );
  }


  @Override
  public void completed(
    Calendar    now,
    Zmanim.Type type
  ) {

    NotifiedZmanim notifiedZmanim = NotifiedZmanim.manager.get( now );
    notifiedZmanim.notified( type );

    SaveNotifiedZmanimTask task = new SaveNotifiedZmanimTask();
    task.execute( notifiedZmanim );
  }


  @Override
  public boolean formatTimeWithSeconds() {
    return personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
  }


  private class SaveNotifiedZmanimTask
    extends AsyncTask<NotifiedZmanim, Void, Void> {

    @Override
    protected Void doInBackground( NotifiedZmanim... params ) {
      NotifiedZmanim.manager.save( params[ 0 ] );

      return null;
    }
  }

}
