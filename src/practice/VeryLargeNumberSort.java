package practice;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Strategy: Priority Queue. */
// NOTE: Because this is an enum, you cannot run a JUnit test from this file!
public enum VeryLargeNumberSort {
	INSTANCE;
	//private final boolean isDebug = false;

	//private static String fileName = "numbers.bin";
	public static VeryLargeNumberSort getInstance() {
		return INSTANCE;
	}

	// This is deliberately single threaded so you cannot sort 2 files at once.
	public synchronized int sortFileandReturnNumber( String fileName, int maxNum, int rank ) {
		int result = 0;
		
		Path path = Paths.get( fileName );
		
		if ( ifFileDoesNotExist( path ) ) {
			FileCreator fileCreator = new FileCreator();
			fileCreator.createFile( maxNum, fileName );
		}

		FileSorter sortor = new FileSorter();
		result = sortor.rankedNumber( fileName, maxNum, rank );
		return result;
	}

	private boolean ifFileDoesNotExist( Path path ) {
		return Files.isRegularFile( path ) == false;
	}
}
