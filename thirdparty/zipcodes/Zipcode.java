package zipcode;

import java.io.*;

/**
 *
 */
public class Zipcode {

	public static void main(
	  String[] args
	  ) throws IOException {

		File shorterFile = new File( "/home/jgindin/tmp/zipcodes-new.csv" );
		Writer writer = null;
		LineNumberReader reader = null;
		String line = null;

		try {
			writer = new BufferedWriter( new FileWriter( shorterFile ) );
			File  zipcodes = new File( "/home/jgindin/tmp/zipcode.csv" );
			reader = new LineNumberReader( new FileReader( zipcodes ) );
			line = reader.readLine();
			while ( null != line ) {

				String[] parts = line.split( ";" );
				writer.write( parts[0] );
				writer.write( ";" );

				writer.write( parts[1].substring( 0, Math.min( parts[1].length(), 5 ) ) );
				writer.write( ";" );

				writer.write( parts[2].substring( 0, Math.min( parts[2].length(), 6 ) ) );
				writer.write( "\n" );

				line = reader.readLine();
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
			System.out.println( "Trying to parse: " + line );
		}
		finally {
			reader.close();
			writer.close();
		}

	}

}  // End of Zipcode class
