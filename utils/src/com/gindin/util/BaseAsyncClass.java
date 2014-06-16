/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util;

import android.os.AsyncTask;

/**
 * An AsyncTask implementation which uses callbacks rather than subclasses.
 */
public final class BaseAsyncClass<Params, Progress, Result>
	extends AsyncTask<Params, Progress, Result> {


	public interface ProgressPublisher<Progress> {
		void publish( Progress... progress );
	}

	public static abstract class Callbacks<Params, Progress, Result> {

		protected abstract Result doInBackground(
			ProgressPublisher<Progress> publisher,
			Params...                   params
		);

		protected void onPreExecute() {}
		protected void onProgressUpdate( Progress... values ) {}
		protected void onPostExecute( Result result ) {}
		protected void onCancelled() {}
	}


	private final Callbacks<Params, Progress, Result>         callbacks;


	public BaseAsyncClass( Callbacks<Params, Progress, Result> callbacks ) {
		this.callbacks = callbacks;
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		callbacks.onPreExecute();
	}


	@Override
	protected Result doInBackground( Params... paramses ) {
		ProgressPublisher<Progress> publisher = new ProgressPublisher<Progress>() {
			@Override
			public void publish( Progress... progress ) {
				publishProgress( progress );
			}
		};
		return callbacks.doInBackground( publisher, paramses );
	}


	@Override
	protected void onProgressUpdate( Progress... values ) {
		super.onProgressUpdate( values );
		callbacks.onProgressUpdate( values );
	}


	@Override
	protected void onPostExecute( Result result ) {
		super.onPostExecute( result );

		if ( !isCancelled() ) {
			callbacks.onPostExecute( result );
		}
	}


	@Override
	protected void onCancelled() {
		super.onCancelled();
		callbacks.onCancelled();
	}

} // End of BaseAsyncClass class
