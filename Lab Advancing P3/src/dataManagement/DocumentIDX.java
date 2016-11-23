package dataManagement;

import generalClasses.P3Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import systemClasses.SystemController;

/**
 * Map that contains the data in memory of the IDX file corresponding to
 * a particular document in the index. An object of this type is created
 * for any document that matches a particular search. 
 * 
 * @author pedroirivera-vega
 *
 */
public class DocumentIDX {
	private Map<String, ArrayList<Integer>> wordLocationsMap = new Hashtable<>(); 

	// for the moment, only needed for testing purposes...
	private int docID; 
	
	private int docNumberOfWords;    // total number of words registered 
	                                 // the particular document
	
	/**
	 * Initializes this instance with current content of the particular idx file
	 * that corresponds to the identified document.
	 * @param docID id of the document
	 * @throws IOException if file format does not match expected format
	 * @throws IllegalArgumentException if docID does not match any existing
	 *         idx file in the system
	 */
	public DocumentIDX(int docID) throws IOException, IllegalArgumentException { 
		this.docID = docID; 
		String fName = SystemController.makeIDXName(docID); 
		File idxFilePath = new File(P3Utils.IndexDirectoryPath, fName); 
		if (idxFilePath.exists()) {
			RandomAccessFile idxFile = new RandomAccessFile(idxFilePath, "r"); 
			loadMapContentFromIDXFile(idxFile); 
			idxFile.close();
		}
		else 
			throw new IllegalArgumentException("No document exist for id = " + docID); 
	
	}

	private void loadMapContentFromIDXFile(RandomAccessFile idxFile) throws IOException {
		long fileLength = idxFile.length(); 
		boolean completed = false; 
		docNumberOfWords = 0; 
		while (!completed) {
			try {
				String word = P3Utils.readWord(idxFile);
				ArrayList<Integer> wordLocationsList =
						new ArrayList<>();
				int docID = idxFile.readInt(); 
				while (docID != -1) { 
					wordLocationsList.add(docID); 
					docID = idxFile.readInt();  
				}
				wordLocationsMap.put(word, wordLocationsList);
				docNumberOfWords += wordLocationsList.size(); 
			} catch (IOException e) {
				if (idxFile.getFilePointer() == fileLength)
					completed = true; 
				else
					e.printStackTrace();
			} 
		}
	}
	
	public int numberOfRegisteredWords() { 
		return docNumberOfWords; 
	}
	
	public int getDocID() { 
		return docID; 
	}

	public Iterable<Integer> getWordLocations(String word) { 
		ArrayList<Integer> locationsList = new ArrayList<>(); 
		ArrayList<Integer> tempList = wordLocationsMap.get(word); 
		
		if (tempList != null) 
			for (Integer location : tempList) {
				locationsList.add(location); 
		} 
		
		return locationsList; 
	}
}
