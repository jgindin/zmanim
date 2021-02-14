/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.feedback;

import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.R;

import org.acra.ACRA;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Dialog fragment for allowing the user to submit feedback.
 */
public class FeedbackFragment
  extends DialogFragment {


  public FeedbackFragment() {
    // Required empty public constructor
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
        final EditText email = (EditText)view.findViewById( R.id.feedback_email );
        handleSend( feedback.getText().toString(), email.getText().toString() );
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
    String feedback,
    String emailText
  ) {

    getDialog().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( getContext() );
    prefs.edit().putString( ACRA.PREF_USER_EMAIL_ADDRESS, emailText ).apply();
    LogUtils.logFeedback( feedback );
  }

}
