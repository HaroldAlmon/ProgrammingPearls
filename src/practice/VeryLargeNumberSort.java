package practice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class VeryLargeNumberSort {
	private static String fileName = "numbers.bin";

	public int sortFileandReturnRank(String afileName, int maxNum, int rank) {
		int result = 0;
		fileName = afileName;

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
		createFileWithrandomNumbers(maxNum, dataOutputStream);
	}

	private DataOutputStream createDataOutputStream(DataOutputStream dataOutputStream) {
		OutputStream outputStream;
		try {
			outputStream = new BufferedOutputStream( new FileOutputStream(fileName));
			dataOutputStream = new DataOutputStream(outputStream);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return dataOutputStream;
	}

	private void createFileWithrandomNumbers(int maxNum, DataOutputStream dataOutputStream) {
		for(int i = 0; i < maxNum; i++) {
			int number = (int) (maxNum * Math.random() + 1);
			try {
				dataOutputStream.writeInt(number);
			} catch (IOException e) {
				e.printStackTrace();
			};
		} 
		try {
			dataOutputStream.flush();
			dataOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int readInputFile(int maxNum, int rank) {
		BufferedInputStream bufferedInputStream = null;
		DataInputStream dataInputStream = null;
		int randomNumber = 0;
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(10000);

		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName)) ;
			dataInputStream = new DataInputStream(bufferedInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if(dataInputStream.available() > 0) {
				randomNumber = dataInputStream.readInt();
				priorityQueue.add(randomNumber);
				
				int batchCounter=1;
				
				while(dataInputStream.available() > 0) {
					sortUpToOneMillionInts(rank, dataInputStream, priorityQueue);
					printBatchCouter(batchCounter);
					batchCounter++;
				}
			}
			dataInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return priorityQueue.peek();
	}

	@SuppressWarnings("unused")
	private void printBatchCouter(int batchCounter) {
		if(false) 			
			System.out.printf("Batch Count = %s%n", batchCounter);
	}

	private void sortUpToOneMillionInts(int rank, DataInputStream dataInputStream, PriorityQueue<Integer> priorityQueue) throws IOException {
		int randomNumber;
		final int oneMillion = 1024 * 1024;
		for (int i=0; i < oneMillion; i++) {
			if(dataInputStream.available() > 0) {
				randomNumber = dataInputStream.readInt();
				if( queueIsNotFull(rank, priorityQueue) ) {
					priorityQueue.add(randomNumber);
				} else
				if ( ifNumberIsGreaterThanAllNumbersInQueue(priorityQueue, randomNumber) ) {
					priorityQueue.remove();
					priorityQueue.add(randomNumber);
				}
			}
			else
				break;
		}
	}

	private boolean ifNumberIsGreaterThanAllNumbersInQueue(
			PriorityQueue<Integer> priorityQueue, int randomNumber) {
		return randomNumber > priorityQueue.peek();
	}

	private boolean queueIsNotFull(int rank, PriorityQueue<Integer> priorityQueue) {
		return priorityQueue.size() < rank;
	}

	@Test(timeout=500)
	public void SortOneThousandNumbers() {
		System.out.printf("Result = %d%n", sortFileandReturnRank("oneThousand.bin", 1_000, 100));
	}
	
	private final int twoHours = 2 * 3_600;
	@Test(timeout = twoHours)
	@Ignore
	public void SortOneBillionNumbers() {
		// 1 Billion...THIS IS SLOW, 45 minutes on an i7 core!
		System.out.printf("Result = %d%n", sortFileandReturnRank("oneBillion.bin", 1_000_000_000, 1_000_000));
	}
	
	@Before
	public void setup() {
		System.out.println("-----------------");
	}
}
