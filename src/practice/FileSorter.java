package practice;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PriorityQueue;

public class FileSorter {
	int rankedNumber( String fileName, int maxNum, int rank ) {
		BufferedInputStream bufferedInputStream = null;
		DataInputStream dataInputStream = null;
		int result = 0;
	
		try {
			bufferedInputStream = new BufferedInputStream( new FileInputStream( fileName ) ) ;
			dataInputStream = new DataInputStream( bufferedInputStream );
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			result = sortFileandGetNumber( rank, dataInputStream );
			dataInputStream.close();
		} catch ( IOException e ) {
			e.printStackTrace();
			System.exit(-1);
		}
		return result;
	}
	
	private int sortFileandGetNumber( int rank, DataInputStream dataInputStream  ) throws IOException {
		int numberRead;
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<>( 10_000 );
		
		while (dataInputStream.available() > 0) {
			numberRead = dataInputStream.readInt();
			if (isQueueFull(rank, priorityQueue) == false) {
				priorityQueue.add(numberRead);
			} else {
				// This is the most important line in the entire file...
				if (isNumberGreaterThanSmallestNumberInQueue( priorityQueue, numberRead ) ) {
					priorityQueue.remove();
					priorityQueue.add( numberRead );
				}
			}
		}
	
		return priorityQueue.peek();
	}
	
	private boolean isNumberGreaterThanSmallestNumberInQueue( PriorityQueue<Integer> priorityQueue, int randomNumber ) {
		return randomNumber > priorityQueue.peek();
	}

	private boolean isQueueFull( int rank, PriorityQueue<Integer> priorityQueue ) {
		return priorityQueue.size() >= rank;
	}
}

