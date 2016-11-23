package testerClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import dataManagement.MatchingSearchDocument;
import systemClasses.SystemController;

public class SystemTester2 {

	public static void main(String[] args) throws IOException {
		SystemController sc = SystemController.getInstance(); 

		ArrayList<String> wtSearchList = new ArrayList<>(); 
		
		wtSearchList.add("file");
		wtSearchList.add("pedro");
		wtSearchList.add("java");
		wtSearchList.add("public");


		
		Map<Integer, MatchingSearchDocument> resultsList = sc.search(wtSearchList); 
		
		for (Entry<Integer, MatchingSearchDocument> e : resultsList.entrySet()) { 
			System.out.println("In Doc ID = " + e.getKey() + 
					" there are " + e.getValue().getMathingWordsLocations().size() + 
					" occurrences of words in the list."); 
			System.out.println("  At locations: "); 
			for (Long location : e.getValue().getMathingWordsLocations())
				System.out.println(location); 
		}
		sc.close();

	}

}
