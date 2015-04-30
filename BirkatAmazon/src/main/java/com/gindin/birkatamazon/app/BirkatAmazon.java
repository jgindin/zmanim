package com.gindin.birkatamazon.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class BirkatAmazon
	extends Activity {


	@Override
	protected void onCreate( final Bundle savedInstanceState ) {

		setContentView( R.layout.activity_birkat_amazon );

		super.onCreate( savedInstanceState );
//		setAutoHide( false );

//		View view = findViewById( R.id.fullscreen_content );
//		setInteractive( view );

		TextView text = (TextView)findViewById( R.id.text );

		try {
			InputStream inputStream = getResources().openRawResource( R.raw.birkat_ammazon );
			BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );

			StringBuilder buffer = new StringBuilder();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				buffer.append( line ).append( "\n" );
			}

			text.setText( buffer );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}


}
