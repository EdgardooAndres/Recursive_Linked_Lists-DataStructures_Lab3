package dataManagement;

import generalClasses.P3Utils;
import ioManagementClasses.IOComponent;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class DocsIDManager {
	public static final int NAMELENGTH = 20; 
	private static final int RECSIZE = NAMELENGTH+4;   // 20+4
	private static DocsIDManager instance = null; 
	private static IOComponent io = IOComponent.getComponent(); 
	
	private ArrayList<String> docNamesList; 
	private File fPath; 
	private RandomAccessFile file; 
	private boolean modified;         // to remember if modifications have been made...
	// needed to writeback to file is needed....
	
	public static DocsIDManager getInstance() throws IOException { 
		if (instance == null) 
			instance = new DocsIDManager(); 
		return instance; 
	}
	
	private DocsIDManager() throws IOException { 
		modified = false; 
		String fName = "docs_ID.pp3"; 
		fPath = new File(P3Utils.IndexDirectoryPath, fName); 
		if (fPath.exists()) {
			file = new RandomAccessFile(fPath, "r"); 

			int listSize = (int) (file.length()/RECSIZE);
			docNamesList = new ArrayList<String>(listSize); 
			// fill list with empty strings...
			for (int i=0; i<listSize; i++)
				docNamesList.add(""); 
			readListContentFromFile(); 
			file.close();
		}
		else 
			docNamesList = new ArrayList<String>(); 
	}

	
	private void readListContentFromFile() throws IOException {
		long fileLength = file.length(); 
		boolean completed = false; 
		while (!completed) {         // this can also be based on docNamesList.size()..,
			try {
				String name = readName();
				int docID = file.readInt(); 
				docNamesList.set(docID-1, name);   // docID can't be zero
			} catch (IOException e) {
				if (file.getFilePointer() == fileLength)
					completed = true; 
				else
					e.printStackTrace();
			} 
		}
	}

	/**
	 * Adds the name of a new document to the docs list. . 
	 * @param name the name of the new document
	 * @return -1 if the document exists already in the system; 
	 * decided by comparing names. Returns i>-1 if the assignment
	 * was successful. In that case, the value returned is the 
	 * id number for the new document,
	 */
	public int addDocument(String name) { 
		int newID = -1; 
		for (int i=0; i<docNamesList.size(); i++) {
			if (newID == -1 && docNamesList.get(i).equals(""))
				newID = i+1;    // zero can't be a doc id
			else if (docNamesList.get(i).equals(name))
				return -1;      // the document exists
		}
		if (newID == -1) {
			docNamesList.add(name); 
			newID = docNamesList.size();   // zero can't be a doc id
		}
		else 
			docNamesList.set(newID-1, name); 
		
		modified = true; 
		
		return newID; 
		
	}
			
	public void removeDocID(int docID) 
	throws IllegalArgumentException {
		try { 
			docNamesList.set(docID-1, "");
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid docID: " + docID); 
		}
		modified = true; 
	}
	
	public void close() { 
		// iterate over entries in map and write each one to file
		if (modified) {
			try {
				file = new RandomAccessFile(fPath, "rw");
				file.seek(0);
				for (int i=0; i<docNamesList.size(); i++) {
					writeNameToFile(docNamesList.get(i)); 
					file.writeInt(i+1);
				}
				file.setLength(file.getFilePointer());    // truncate the length of the file
				file.close(); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
		}

	}
	
	public String getDocName(int docID) throws IllegalArgumentException { 
		try { 
			return docNamesList.get(docID-1);
		} catch (IndexOutOfBoundsException e) { 
			throw new IllegalArgumentException("No indexed document has id = " + docID); 
		}
	}
	
	private void writeNameToFile(String name) throws IOException {
		for (int i=0; i<name.length(); i++)
			file.writeByte((byte) name.charAt(i)); 
		for (int i=name.length(); i < NAMELENGTH; i++)
		    file.writeByte((byte) ' ');         // fill with blanks remaining bytes...
	}

	/**
	 * Read next name from current file pointer in file. 
	 * @param file the random access file corresponding to the main index
	 * @return the next word
	 * @throws IOException
	 */
	private String readName() throws IOException {
		String name = ""; 
		char ch; 
		int i=0;
		while (i < NAMELENGTH) { 
			ch = (char) file.readByte();
			i++;
			if (ch != ' ')
				name += ch; 
			else
				while (i < NAMELENGTH) {   // just position FP to next int
					file.readByte(); 
					i++; 
				}
		}
		return name;    // returns "" if the name is just spaces
	}
	
	// for testing purposes
	public void showRegisteredDocs() { 
		for (int i=0; i< this.docNamesList.size(); i++) { 
			io.output("( name = " + docNamesList.get(i) 
					+ "  ID = " + (i+1) + ")\n");
		}
	}
}
