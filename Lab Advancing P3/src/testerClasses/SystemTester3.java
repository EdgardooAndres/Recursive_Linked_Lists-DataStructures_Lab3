package testerClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import dataManagement.MatchingSearchDocument;
import systemClasses.SystemController;

public class SystemTester3 {

	public static void main(String[] args) throws IOException {
		SystemController sc = SystemController.getInstance(); 

		ArrayList<String> wtSearchList = new ArrayList<>(); 
		
		//wtSearchList.add("the");
		//wtSearchList.add("in");
		//.add("to");
		wtSearchList.add("salary");
		//wtSearchList.add("median"); 
		//wtSearchList.add("annual"); 
		
		Map<Integer, MatchingSearchDocument> resultsList = sc.search(wtSearchList); 
		
		for (Map.Entry<Integer, MatchingSearchDocument> e : resultsList.entrySet()) { 
			MatchingSearchDocument smd = e.getValue(); 
			System.out.println("\n\nContent of document: " + e.getKey() + "\n\n");
			smd.displayDocument(0);  // display the whole document
		}
		sc.close();

	}

}
