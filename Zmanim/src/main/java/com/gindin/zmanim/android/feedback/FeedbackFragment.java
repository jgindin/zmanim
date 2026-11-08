/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.feedback;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanlib.location.ZmanimLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Dialog fragment for allowing the user to submit feedback. Sending hands the feedback, with the details listed in
 *  the dialog's note, to the user's email app (see FeedbackEmail).
 */
public class FeedbackFragment
  extends DialogFragment {

  private static final String LOCATION_ARG = "location";


  public FeedbackFragment() {
    // Required empty public constructor
  }


  /**
   * Shows the feedback dialog or, when the device has no email app to send it with, says where to write instead.
   *
   * @param location the location the zmanim are shown for, included in the email; null when there is none yet.
   */
  public static void show(
    @NonNull  FragmentActivity  activity,
    @Nullable ZmanimLocation    location
  ) {

    if ( !FeedbackEmail.canBeSent( activity ) ) {
      new AlertDialog.Builder( activity )
        .setTitle( R.string.feedback )
        .setMessage( R.string.feedback_no_email_app )
        .setPositiveButton( android.R.string.ok, null )
        .show();
      return;
    }

    newInstance( location ).show( activity.getSupportFragmentManager(), "feedback" );
  }


  /** @param location the location the zmanim are shown for, included in the email; null when there is none yet. */
  @NonNull
  public static FeedbackFragment newInstance(
    @Nullable ZmanimLocation location
  ) {

    Bundle args = new Bundle();
    if ( ( location != null ) && !location.isInvalid ) {
      args.putBundle( LOCATION_ARG, ZmanLocationUtil.toBundle( location ) );
    }

    FeedbackFragment fragment = new FeedbackFragment();
    fragment.setArguments( args );
    return fragment;
  }



  @NonNull
  @Override
  public Dialog onCreateDialog( Bundle savedInstanceState ) {


    LayoutInflater inflater = getActivity().getLayoutInflater();
    final View view = inflater.inflate( R.layout.fragment_feedback, null );

    final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick(
        DialogInterface dialog,
        int             which
      ) {

        final EditText feedback = (EditText)view.findViewById( R.id.feedback_input );
        handleSend( feedback.getText().toString() );
      }
    };


    AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() )
      .setTitle( R.string.feedback )
      .setPositiveButton( R.string.feedback_send, clickListener )
      .setView( view );

    return builder.create();
  }


  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater      inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle    savedInstanceState
  ) {

    View view = super.onCreateView( inflater, container, savedInstanceState );

    // Causes the soft keyboard to show.
    getDialog().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    return view;
  }


  @Override
  public void onAttach( Context context ) {
    super.onAttach( context );
  }


  @Override
  public void onDetach() {
    super.onDetach();
  }


  private void handleSend(
    String feedback
  ) {

    getDialog().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    FragmentActivity activity = requireActivity();
    Bundle locationBundle = requireArguments().getBundle( LOCATION_ARG );
    ZmanimLocation location = ( locationBundle != null ) ? ZmanLocationUtil.fromBundle( locationBundle ) : null;

    try {
      activity.startActivity( FeedbackEmail.intent( activity, feedback, location ) );
    }
    catch ( ActivityNotFoundException e ) {
      // The email app was removed, or turned off, while the dialog was open.
      Toast.makeText( activity, R.string.feedback_no_email_app, Toast.LENGTH_LONG ).show();
    }
  }

}
