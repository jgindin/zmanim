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


	public static abstract class Callbacks<Params, Progress, Result> {

		protected abstract Result doInBackground( Params... paramses );

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
		return callbacks.doInBackground( paramses );
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
