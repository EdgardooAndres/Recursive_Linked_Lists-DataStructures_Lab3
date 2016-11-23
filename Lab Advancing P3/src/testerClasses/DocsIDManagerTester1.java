package testerClasses;

import java.io.IOException;
import java.util.Map;

import dataManagement.DocsIDManager;
import dataManagement.MainIndexManager;

public class DocsIDManagerTester1 {

	private static DocsIDManager docIDs; 

	public static void main(String[] args) throws IOException {	
		docIDs = DocsIDManager.getInstance(); 
		docIDs.addDocument("perro"); 
		docIDs.addDocument("pepe"); 
		docIDs.addDocument("pedro"); 
		docIDs.addDocument("myriam"); 
		
		docIDs.close();
	}

}
