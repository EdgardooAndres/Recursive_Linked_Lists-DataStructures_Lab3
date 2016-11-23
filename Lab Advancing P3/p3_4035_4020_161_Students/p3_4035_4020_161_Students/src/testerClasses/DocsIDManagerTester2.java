package testerClasses;

import java.io.IOException;
import java.util.Map;

import dataManagement.DocsIDManager;
import dataManagement.MainIndexManager;

public class DocsIDManagerTester2 {

	private static DocsIDManager docIDs; 

	public static void main(String[] args) throws IOException {	
		docIDs = DocsIDManager.getInstance();  
		docIDs.showRegisteredDocs(); 
		
		System.out.println("removing 2 and 4"); 
		docIDs.removeDocID(2);
		docIDs.removeDocID(4);
		
		docIDs.showRegisteredDocs();

		System.out.println("adding lambrusco"); 
		docIDs.addDocument("lambrusco"); 
		System.out.println("removing 1"); 
		docIDs.removeDocID(1);

		
		docIDs.showRegisteredDocs();

		
		docIDs.close();
	}

}
