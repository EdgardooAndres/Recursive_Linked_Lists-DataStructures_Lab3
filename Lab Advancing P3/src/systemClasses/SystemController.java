package systemClasses;

import generalClasses.P3Utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import menuClasses.MainMenu;
import menuClasses.Menu;
import menuClasses.Option;
import dataManagement.DocsIDManager;
import dataManagement.Document;
import dataManagement.DocumentIDX;
import dataManagement.MainIndexManager;
import dataManagement.MatchingSearchDocument;
import dataManagement.WordInDocument;

/**
 * Main class of the system. It controls fundamental operations of the
 * system. 
 * 
 * @author pedroirivera-vega
 *
 */
public class SystemController {
	private static SystemController instance = null; 
	private MainIndexManager mim;        // manager of main index data
	private DocsIDManager didm;          // manager of documents ids in the system
	private Stack<Menu> mStack;          // stack to manage actions in menus in this system

	/**
	 * Returns reference of the unique instance of SystemController in the system. 
	 * @return reference to the unique instance of SystemController
	 * @throws IOException if there are problems with files
	 */
	public static SystemController getInstance() throws IOException { 
		if (instance == null) 
			instance = new SystemController(); 

		return instance; 
	}

	/**
	 * Initiates the actions of this controller based on the main menu and
	 * user's selected choices. 
	 */
	public void run() { 
		mStack.push(MainMenu.getMainMenu());      // just execute the main menu
		while(!mStack.empty()) {
			Option opt = mStack.peek().activate(); 
			opt.getAction().execute(this); 
		} 
	}

	/**
	 * Creates an instance of SystemController -- the only instance that
	 * can exist in the system. 
	 * 
	 * @throws IOException
	 */
	private SystemController() throws IOException { 
		mim = MainIndexManager.getInstance();   // initializes mim with data in main_index.pp3 file
		didm = DocsIDManager.getInstance();     // initializes didm with data in doc_ID.pp3 file
		mStack = new Stack<Menu>();      // used to control menu operations
	}

	/**
	 * Returns reference to the stack object used to manage different
	 * states of the system. 
	 * @return reference to the stack. 
	 */
	public Stack<Menu> getMenuStack() { 
		return mStack; 
	}

	/**
	 * To be executed whenever the user initiates an action of 
	 * adding a new document to the system.
	 * 
	 * Tries to register a new document in the index system. 
	 * @param docName name of the file containing the text of the
	 * document to add
	 * @return Returns a string message summarizing the final
	 * result or status of the operation. 
	 * @throws IOException If there are problems with files
	 */
	public String addNewDocument(String docName) 
			throws IOException {

		File docFilePath;   // the path for the document's file

		// Call method in P3Utils to validate the name and file. It the file name 
		// is not valid or the corresponding file does not exist in the docs 
		// directory, it then terminates returning the message of an exception
		// with an IllegalArgumentException; in that case, it the addNewDocument
		// execution ends returning the message inside the exception object.
		try { 
			docFilePath = P3Utils.validateDocumentFile(docName); 
		} catch (IllegalArgumentException e) { 
			return e.getMessage(); 
		}

		// If passes, then the file name for the document is valid and the file exists...

		// Try to register the new document to the docsID structure (didm object). 
		// If that document does not exist, it assigns a new ID and returns
		// it. If the document exists (a document with same name has been
		// previously registered in the system), it returns -1. 
		int docID = didm.addDocument(docName); 
		if (docID == -1)
			return "Document " + docName + " already exists in index."; 

		RandomAccessFile docFile = new RandomAccessFile(docFilePath, "r"); 
		Map<String, ArrayList<Integer>> documentWordsMap = new Hashtable<>();

		// Creates the map where entries are pairs (word, list of locations), 
		// where the list of locations contains the integer values of indexes
		// for the first byte of each occurrence of the word in the document 
		// file. 
		fillMapFromDocumentText(documentWordsMap, docFile); 
		docFile.close();

		// Registers the document's words in the mim object. For each such 
		// word, it registers pair (docID, f), where docID is the document ID 
		// assigned to the new document, and f = size of list of its locations
		// in the document (the frequency of the word in the document).
		registerDataInMIM(docID, documentWordsMap); 

		// Create the IDX file corresponding to the content of the new document
		// being added to the system. For each word in the document, it writes
		// the word, followed by the list of locations of that word in the
		// document. See description of idx file in project's specs. 
		saveMapToIDXFile(docID, documentWordsMap); 

		return "Document " + docName + " was successfully added."; // things worked fine			
	}

	/**
	 * Registers all data of the new document in mim structure. For each word in the document, 
	 * there will be a pair (docID, frequency) that will be added. 
	 * @param docID the id of the new document being added
	 * @param documentWordsMap content of the document in a map with entries:
	 *          (word, list of locations)
	 */
	private void registerDataInMIM(int docID, Map<String, ArrayList<Integer>> documentWordsMap) {
		//ADD MISSING CODE HERE	(Exercise 4) TODO 

		for(String w : documentWordsMap.keySet())
			mim.registerWordInDocument(w, docID, documentWordsMap.get(w).size());


	}

	/**
	 * Generates the idx file that corresponds to the new document. 
	 * @param docID id of the new document
	 * @param documentWordsMap map containing the words in the document, and, for each word, 
	 * the list of locations where it appears in the document
	 * @throws IOException if there are problems with file: file format, etc. 
	 */
	private void saveMapToIDXFile(int docID, Map<String, ArrayList<Integer>> documentWordsMap) 
			throws IOException 
	{
		String fName = makeIDXName(docID); 
		File idxFilePath = new File(P3Utils.IndexDirectoryPath, fName); 
		if (!idxFilePath.exists()) {
			RandomAccessFile idxFile = new RandomAccessFile(idxFilePath, "rw"); 
			for (Entry<String, ArrayList<Integer>> e: documentWordsMap.entrySet()) { 
				String word = e.getKey(); 
				P3Utils.writeWordToFile(word, idxFile);
				for (Integer location : e.getValue())
					idxFile.writeInt(location);
				idxFile.writeInt(-1);            // marks the end of the list in the file
			}

			idxFile.close();
		}
		else 
			throw new IllegalArgumentException("INTERNAL ERROR: An idx file exist for docid = " + docID); 	
	}

	/**
	 * This method executes and important part of the process of adding a new document
	 * to the index: the part of extracting the words from the new document and each one
	 * of the locations (byte indexes) where the word appears in the document. 
	 * Reads the content in document's file. For each different word it ends with 
	 * one entry whose key is the word itself, and the value is the list of locations
	 * of that word in the document. That content is saved in a map collection, in 
	 * entries in which the word is the key and the list of locations is its value.
	 * 
	 * @param documentWordsMap the map to be created
	 * @param docFile the file where the document's content is located.
	 */
	private void fillMapFromDocumentText(Map<String, ArrayList<Integer>> documentWordsMap, 
			RandomAccessFile docFile) 
	{
		Document document = new Document(docFile); 
		for (WordInDocument wid : document) {
			ArrayList<Integer> wordLocsList = documentWordsMap.get(wid.getWord());	
			// ADD MISSING CODE HERE (Exercise 4)  TODO
			
			if(wordLocsList == null) //no hay nada con esa palabra todavia
			{
				//crear y poner
				wordLocsList = new ArrayList<>((int) wid.getLocation());
				documentWordsMap.put(wid.getWord(), wordLocsList);
			}
			else // y EXISTE ALGO ASI QUE AÒAde.
				wordLocsList.add((int) wid.getLocation());
		}
	}

	/** 
	 * Initiates the search for words in a given list. 
	 * @param wtSearchList the list of words to search
	 * @return A map whose entries are of the form: 
	 *    key = docID, value = list of locations in the particular
	 *    document where one of the words in the search list
	 *    begins (really, the index of its first byte in the file)
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	public Map<Integer, MatchingSearchDocument> search(ArrayList<String> wtSearchList) 
			throws IllegalArgumentException, IOException { 
		Map<Integer, MatchingSearchDocument> matchingDocuments =
				new Hashtable<>(); 

		// mim is the MainIndexMaganer
		// fills the map of matching documents with as many entries as documents containing at least one
		// of the words in the search list. For each such document, it adds an entry composed of
		// (docID, SearchMatchingDocument object). The search matching document corresponding to 
		// a particular documents in the map will also contain a list of all the words in the search
		// list that it contains. All words as treated as in lower case 
		for (String word : wtSearchList) { 
			Iterable<Entry<Integer, Integer>> docAndWFEntry = 
					mim.getDocsList(word.toLowerCase());  // pairs (d, f) -- one for each doc containing word
			if (docAndWFEntry != null)
				for (Entry<Integer, Integer> docFreqPair : docAndWFEntry)
					addToMatchingDocumentsMap(matchingDocuments, docFreqPair.getKey(), word.toLowerCase()); 
		}

		for (Entry<Integer, MatchingSearchDocument> e : matchingDocuments.entrySet()) {
			MatchingSearchDocument smd = e.getValue(); 
			smd.buildMatchingLocations();
		}

		return matchingDocuments; 	
	}

	/**
	 * To the collection of matching documents add entry for each matching
	 * document, which includes entries consisting of the docID and a
	 * corresponding object of type SearchMatchingDocument. This object
	 * will contain the list of locations in the document that correspond
	 * to the first character in any occurrence of any of the search words
	 * that have been identified as belonging to the document. 
	 * @param mdm Collection of matching documents, initially, an empty map.
	 * @param docID the id number for the matching document
	 * @param word the given word
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void addToMatchingDocumentsMap(
			Map<Integer, MatchingSearchDocument> mdm,
			int docID, String word) 
					throws IllegalArgumentException, IOException {
		MatchingSearchDocument docMD = mdm.get(docID); 
		if (docMD == null) {
			docMD = new MatchingSearchDocument(docID); 
			mdm.put(docID, docMD); 
		}
		docMD.addMatchingWord(word);
	}

	/**
	 * Closes the SystemController object. Must be done when exiting the system. 
	 */
	public void close() { 
		mim.close(); 
		didm.close();
	}

	/**
	 * Makes a valid name of the idx file corresponding to document with given ID. 
	 * @param docID the id of the document. 
	 * @return the name
	 */
	public static String makeIDXName(int docID) { 
		String s = String.format("idx_%05d.pp3", docID);
		return s; 
	}
}
