/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.upgrade.UpgradeFixtures;

import androidx.appcompat.app.AlertDialog;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowDialog;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * People upgrading from the released version (409) are told, in What's New, that the privacy policy changed: the
 *  feedback email they can choose to send includes their location. People installing for the first time read the
 *  current policy anyway, so they are not told it changed.
 */
@RunWith( RobolectricTestRunner.class )
// Below Android 13 the app does not ask about notifications, so no permission dialog competes with What's New.
@Config( sdk = 32 )
public class UT_PrivacyPolicyChangeNote {

  @Before
  public void allowExactAlarms() {
    ShadowAlarmManager.setCanScheduleExactAlarms( true );
  }


  /** Running the upgrade steps leaves this process-wide flag set; see UT_UpgradeAppUpgrader. */
  @After
  public void clearProcessWideUpgradeFlag() {
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  @Test
  public void upgradingFromTheReleasedVersionSaysThePolicyChanged() throws IOException {

    Assume.assumeTrue( "The upgrade notes are shown only by a build above the released 409",
      BuildConfig.VERSION_CODE > 409 );

    UpgradeFixtures.install( context() );

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        String whatsNew = openWhatsNew( activity );
        assertNotNull( "Upgrading from 409 should offer What's New", whatsNew );
        assertTrue( whatsNew, whatsNew.contains( context().getString( R.string.privacy_policy_feedback_email ) ) );
      } );
    }
  }


  @Test
  public void aFreshInstallIsNotToldThePolicyChanged() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        String whatsNew = openWhatsNew( activity );
        if ( whatsNew != null ) {
          assertFalse( whatsNew, whatsNew.contains( context().getString( R.string.privacy_policy_feedback_email ) ) );
        }
      } );
    }
  }


  /** Presses View on the What's New snackbar and returns the notes it shows, or null when there is no snackbar. */
  private static String openWhatsNew( ZmanimActivity activity ) {

    idle();
    View view = activity.findViewById( com.google.android.material.R.id.snackbar_action );
    if ( view == null ) {
      return null;
    }

    view.performClick();
    idle();

    Dialog dialog = ShadowDialog.getLatestDialog();
    assertTrue( "View should show the notes", dialog instanceof AlertDialog );
    TextView message = dialog.findViewById( android.R.id.message );
    assertNotNull( message );
    return message.getText().toString();
  }


  private static void idle() {
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  private static Context context() {
    return ApplicationProvider.getApplicationContext();
  }

}  // End of UT_PrivacyPolicyChangeNote class
