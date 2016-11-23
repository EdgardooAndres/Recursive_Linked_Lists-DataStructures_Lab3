package testerClasses;

import generalClasses.P3Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import dataManagement.Document;
import dataManagement.WordInDocument;

public class DocumentTester2 {

	public static void main(String[] args) throws IOException {
		String fName = "doc1"; 
		File fPath = P3Utils.validateDocumentFile(fName); 

		RandomAccessFile file = new RandomAccessFile(fPath, "rw"); 
		
		Document doc = new Document(file); 
		
		ArrayList<Long> wp = new ArrayList<>(); 
		wp.add(30l); 
		wp.add(737l);
		wp.add(1137l);
		wp.add(1182l);
		wp.add(1653l);
		wp.add(1815l);
		wp.add(2002l);
		wp.add(2299l);
		wp.add(3351l);
		wp.add(3380l);
		wp.add(3544l);
		wp.add(3779l);
		wp.add(3854l);
		wp.add(3984l);
		wp.add(4257l);
		wp.add(4345l);
		wp.add(4399l);
		wp.add(4408l);
		wp.add(4426l);
		wp.add(4435l);
		wp.add(4487l);
		wp.add(4525l);
		wp.add(4546l);
		wp.add(4624l);
		wp.add(4632l);
		wp.add(4736l);
		wp.add(4760l);
		wp.add(4879l);
		wp.add(4896l);
		wp.add(4979l);
		
		doc.displayDocumentContent(wp, 0);
		
    	file.close();
	}

}
