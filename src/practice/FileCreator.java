package practice;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileCreator {
	void createFile(int maxNum, String fileName) {

		DataOutputStream dataOutputStream = null;

		dataOutputStream = createDataOutputStream(dataOutputStream, fileName);
		generateRandomNumbers(maxNum, dataOutputStream);
	}
	
	private void generateRandomNumbers(int maxNum, DataOutputStream dataOutputStream) {
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

	private DataOutputStream createDataOutputStream(DataOutputStream dataOutputStream, String fileName) {
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
}

