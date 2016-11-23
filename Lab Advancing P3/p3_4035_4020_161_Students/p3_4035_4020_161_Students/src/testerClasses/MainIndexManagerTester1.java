package testerClasses;

import java.io.IOException;
import java.util.Map;

import dataManagement.MainIndexManager;

public class MainIndexManagerTester1 {

	private static MainIndexManager mim; 

	public static void main(String[] args) throws IOException {		
		mim = MainIndexManager.getInstance(); 
		mim.registerWordInDocument("pedro", 1, 2);  // (word, doc id, frequency)
		mim.registerWordInDocument("orlando", 1, 3);
		mim.registerWordInDocument("pedro", 2, 2);
		mim.registerWordInDocument("orlando", 3, 3);  	
		mim.registerWordInDocument("juan", 3, 5);
		
		showWordDocs("orlando"); 
		showWordDocs("pedro"); 
		showWordDocs("juan"); 
		
		mim.close();
	}

	private static void showWordDocs(String word) { 
		System.out.println("Word " + word + " is in the following docs with frequency given: "); 
		for (Map.Entry<Integer, Integer> e : mim.getDocsList(word)) 
			System.out.println(e);

	}
}
