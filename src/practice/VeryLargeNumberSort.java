package practice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
/**
 * @author Harold Almon
 */
public enum VeryLargeNumberSort {
	INSTANCE;

	private static String fileName = "numbers.bin";
	public static VeryLargeNumberSort getInstance() {
		return INSTANCE;
	}
	
	@SuppressWarnings("unused")
	// This is deliberately single threaded so you cannot sort 2 files at once.
	public synchronized int sortFileandReturnNumber(String afileName, int maxNum, int rank) {
		int result = 0;
		fileName = afileName;
		
		if(false)
			System.out.printf("File = %s; Number Count=%d, Rank=%d%n", fileName, maxNum, rank);
		Path path = Paths.get(fileName);
		
		if ( ifFileDoesNotExist(path) ) {
			createFile(maxNum);
		}

		result = readInputFile(maxNum, rank);
		return result;
	}

	private boolean ifFileDoesNotExist(Path path) {
		return Files.isRegularFile(path) == false;
	}
	
	private void createFile(int maxNum) {
		DataOutputStream dataOutputStream = null;

		dataOutputStream = createDataOutputStream(dataOutputStream);
		createFileWithRandomNumbers(maxNum, dataOutputStream);
	}

	private DataOutputStream createDataOutputStream(DataOutputStream dataOutputStream) {
		OutputStream outputStream;
		try {
			outputStream = new BufferedOutputStream( new FileOutputStream(fileName));
			dataOutputStream = new DataOutputStream(outputStream);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		return dataOutputStream;
	}

	private void createFileWithRandomNumbers(int maxNum, DataOutputStream dataOutputStream) {
		for(int i = 0; i < maxNum; i++) {
			int number = (int) (maxNum * Math.random() + 1);
			try {
				dataOutputStream.writeInt(number);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			};
		} 
		try {
			dataOutputStream.flush();
			dataOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private int readInputFile(int maxNum, int rank) {
		BufferedInputStream bufferedInputStream = null;
		DataInputStream dataInputStream = null;
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(10000);

		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName)) ;
			dataInputStream = new DataInputStream(bufferedInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			sortFile(rank, dataInputStream, priorityQueue);
			dataInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return priorityQueue.peek();
	}

	private void sortFile(int rank, DataInputStream dataInputStream, PriorityQueue<Integer> priorityQueue) throws IOException {
		while (dataInputStream.available() > 0) {
			int batchCounter = 1;

			sortBatch(rank, dataInputStream, priorityQueue);
			printBatchCouter(batchCounter);
			batchCounter++;
		}
	}

	@SuppressWarnings("unused")
	private void printBatchCouter(int batchCounter) {
		if(false) 			
			System.out.printf("Batch Count = %s%n", batchCounter);
	}

	private void sortBatch(int rank, DataInputStream dataInputStream, PriorityQueue<Integer> priorityQueue) throws IOException {
		int numberRead;
		final int batchSize = 1024 * 1024;
		for (int i=0; i < batchSize; i++) {
			if(dataInputStream.available() > 0) {
				numberRead = dataInputStream.readInt();
				if( isQueueIsNotFull(rank, priorityQueue) ) {
					priorityQueue.add(numberRead);
				} else
				// This is the most important line in the entire file...
				if ( isNumberReadIsGreaterThanSmallestNumberInQueue(priorityQueue, numberRead) ) {
					priorityQueue.remove();
					priorityQueue.add(numberRead);
				}
			}
			else
				break;
		}
	}

	private boolean isNumberReadIsGreaterThanSmallestNumberInQueue(PriorityQueue<Integer> priorityQueue, int randomNumber) {
		return randomNumber > priorityQueue.peek();
	}

	private boolean isQueueIsNotFull(int rank, PriorityQueue<Integer> priorityQueue) {
		return priorityQueue.size() < rank;
	}

	@Test(timeout=500)
	public void SortOneThousandNumbers() {
		//VeryLargeNumberSort veryLargeNumberSort = LargeNumberFactory.getInstance();
		System.out.printf("SortOneThousandNumbers(\"oneThousand.bin\", 1_000, 100) = %d%n", sortFileandReturnNumber("oneThousand.bin", 1_000, 100));
	}
	
	private final int twoHours = 2 * 3_600;
	@Test(timeout = twoHours)
	@Ignore
	public void SortOneBillionNumbers() {
		// 1 Billion...THIS IS SLOW, 45 minutes on an i7 core!
		System.out.printf("SortOneBillionNumbers(\"oneBillion.bin\", 1_000_000_000, 1_000_000)) = %d%n", sortFileandReturnNumber("oneBillion.bin", 1_000_000_000, 1_000_000));
	}
	
	@Before
	public void setup() {
		System.out.println("-----------------");
	}
}
