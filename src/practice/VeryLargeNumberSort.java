package practice;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
/** Strategy: Priority Queue. */
// Because this is an enum, you cannot run a JUnit test from this file.
public enum VeryLargeNumberSort {
	INSTANCE;

	private static String fileName = "numbers.bin";
	public static VeryLargeNumberSort getInstance() {
		return INSTANCE;
	}
	
	// This is deliberately single threaded so you cannot sort 2 files at once.
	public synchronized int sortFileandReturnNumber(String afileName, int maxNum, int rank) {
		int result = 0;
		boolean isDebug = false;
		fileName = afileName;
		
		if( isDebug )
			System.out.printf("File = %s; Number Count=%d, Rank=%d%n", fileName, maxNum, rank);
		Path path = Paths.get(fileName);
		
		if ( ifFileDoesNotExist(path) ) {
			FileCreator fileCreator = new FileCreator();
			fileCreator.createFile(maxNum, fileName);
		}

		result = rankedNumber(fileName, maxNum, rank);
		return result;
	}

	private boolean ifFileDoesNotExist(Path path) {
		return Files.isRegularFile(path) == false;
	}

	private int rankedNumber(String fileName, int maxNum, int rank) {
		BufferedInputStream bufferedInputStream = null;
		DataInputStream dataInputStream = null;
		int result = 0;

		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName)) ;
			dataInputStream = new DataInputStream(bufferedInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			result = sortFileandGetNumber(rank, dataInputStream);
			dataInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return result;
	}

	private int sortFileandGetNumber(int rank, DataInputStream dataInputStream) throws IOException {
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(10000);
		while (dataInputStream.available() > 0) {
			int batchCounter = 1;

			sortBatch(rank, dataInputStream, priorityQueue);
			printBatchCouter(batchCounter);
			batchCounter++;
		}
		return priorityQueue.peek();
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
				{
					// This is the most important line in the entire file...
					if ( isNumberReadIsGreaterThanSmallestNumberInQueue(priorityQueue, numberRead) ) {
						priorityQueue.remove();
						priorityQueue.add(numberRead);
					}
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
}
