package testerClasses;

import generalClasses.P3Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import dataManagement.Document;
import dataManagement.WordInDocument;

public class DocumentTester3 {

	public static void main(String[] args) throws IOException {
		String fName = "doc1"; 
		File fPath = P3Utils.validateDocumentFile(fName); 

		RandomAccessFile file = new RandomAccessFile(fPath, "rw"); 
		
		Document doc = new Document(file); 
		
		ArrayList<Long> wp = new ArrayList<>(); 

		/**/
		for (WordInDocument w : doc) {
			if (w.getWord().equalsIgnoreCase("file"))
				wp.add(w.getLocation()); 
		}
		/**/
				
		doc.displayDocumentContent(wp, 0);
		
    	file.close();
	}

}
