package testerClasses;

import java.io.IOException;
import java.util.Map;

import dataManagement.MainIndexManager;

public class MainIndexManagerTester3 {

	private static MainIndexManager mim; 

	public static void main(String[] args) throws IOException {		
		mim = MainIndexManager.getInstance();

		showWordDocs("file");
		
		mim.close();
	}

	private static void showWordDocs(String word) { 
		System.out.println("Word " + word + " is in the following docs with frequency given: "); 
		try { 
			for (Map.Entry<Integer, Integer> e : mim.getDocsList(word)) 
				System.out.println(e);
		} catch (IllegalArgumentException e) { 
			System.out.println(e.getMessage()); 
		}
	}
}
