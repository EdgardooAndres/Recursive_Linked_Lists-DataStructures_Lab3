package dataManagement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An object of this type is created for each document that 
 * matches a particular search; that is, a document that contains
 * at least one of the words in the searching list. 
 * 
 * @author pedroirivera-vega
 *
 */
public class MatchingSearchDocument {
	private DocumentIDX docIDX;    // contains data read from the idx file of the document
	private Document document;     // the correponding document....
	
	// the following is the list of words (from the search list)
	// that are part of the document
	private ArrayList<String> matchingWords;       // words
	private ArrayList<Long> matchingLocations;  // locations in document
	
	public MatchingSearchDocument(int docID) throws IllegalArgumentException, IOException { 
		docIDX = new DocumentIDX(docID);        // instantiates object with data from IDX file
		matchingWords = new ArrayList<>(); 
		matchingLocations = null; 
		document = null; 
	}
	
	/**
	 * Add a new word from the search list, which is identified as part of
	 * the document.
	 * @param word
	 */
	public void addMatchingWord(String word) { 
		matchingWords.add(word); 
	}
	
	/**
	 * Constructs the list of all locations in the document where one of the matching
	 * words begins. For each matching word, all its locations are included as part
	 * of this list. That list is finally sorted in increasing order.
	 */
	public void buildMatchingLocations() { 
		matchingLocations = new ArrayList<>(); 
		for (String word : matchingWords) 
			for (Integer location : docIDX.getWordLocations(word))
				matchingLocations.add((long) location); 
		
		matchingLocations.sort(null);
	}
	
	/**
	 * Get a copy of that locations in document that contain one of the words
	 * in the list of words to search
	 * 
	 * @return the list of locations
	 */
	public ArrayList<Long> getMathingWordsLocations() { 
		if (matchingWords == null) 
			buildMatchingLocations(); 
		ArrayList<Long> result = new ArrayList<>(); 
		for (Long location : matchingLocations) 
			result.add(location); 
		
		return result; 
	}
	
	/**
	 * Displays a specific number of lines from the specified document. 
	 * If the number of lines is given as 0, then que whole document is
	 * displayed. 
	 * @param nLines number of initial lines from the document to be displayed
	 * @throws IOException
	 */
	public void displayDocument(int nLines) throws IOException { 
		if (document == null) 
			document = new Document(docIDX.getDocID()); 
		document.displayDocumentContent(matchingLocations, nLines);
	}

}
