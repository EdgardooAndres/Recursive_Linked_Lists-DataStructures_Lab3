package dataManagement;

import generalClasses.P3Utils;
import ioManagementClasses.IOComponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import systemClasses.SystemController;

/**
 * This is the type of object that represents a document. A document
 * is associated to a file whose content is the document. It allows 
 * important operations on the document content: to display the document's
 * content, to read valid words from the document, to iterate over words
 * in the document, to iterate over lines in the document, ...
 * 
 * Useful when a new document is being indexed and when the content of
 * a document needs to be displayed. 
 * 
 * (NOT NECESSARILY COMPLETE...)
 * 
 * @author pedroirivera-vega
 *
 */
public class Document implements Iterable<WordInDocument> {
	private RandomAccessFile file;
	private IOComponent io = IOComponent.getComponent();   // for I/O from/to user

	/**
	 * Creates a new instance of Document. 
	 * The document this object represents is the one whose content
	 * is at the specified file. This is useful when the document is
	 * being added to the index. 
	 * 
	 * @param file the document's content.
	 */
	public Document(RandomAccessFile file) { 
		this.file = file; 
	}
	
	/**
	 * Creates a new instance of Document. The document that this
	 * instance represents is the one whose ID is given as parameter. 
	 * This method presumes that the document already has an ID 
	 * assigned. 
	 * 
	 * @param docID the ID of the corresponding document
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public Document(int docID) throws IllegalArgumentException, IOException { 
		String fName = DocsIDManager.getInstance().getDocName(docID); 
		File fPath = new File(P3Utils.DocsDirectoryPath, fName); 
		if (!fPath.exists())
			throw new IllegalStateException("Document's file does not exist in system: " + fName); 
		file = new RandomAccessFile(fPath, "r"); 
	}
	
	/**
	 * Displays the current content of the the first lines of a document, 
	 * where that number is given as parameter. If that number is <= 0, then
	 * the whole document will be displayed. It underlines words
	 * beginning at positions listed in wp and which appear on any
	 * of the lines being displayed. 
	 * @param wp ordered list of positions (indexes) where a word begins
	 * and which needs to be underlined (or somehow emphasized) in 
	 * displayed output. This list must be in increasing order of its
	 * values.
	 * @param nLines the number of lines to display. A value 0 or less
	 * causes the whole document to be displayed; otherwise, it displays
	 * the first nLines lines from the document. 
	 */
	public void displayDocumentContent(ArrayList<Long> wp, int nLines) { 
		// reads character per character, and displays. Then the character
		// position matches next position in wp, it is assumed that a word 
		// begins there. Then the word is read and displayed underlined. 
		
		String w = ""; 
		long location = 0;
		int wpIndex = 0; 
		char ch; 
		int countLines = (nLines <= 0 ? -1 : 0); 
		try { 
			file.seek(0);
			long lastLocation = file.length(); 
			while (location != lastLocation && countLines < nLines) {

				if (wpIndex < wp.size() && location == wp.get(wpIndex)) { 
					w = this.readNextWordFromFile().getWord(); 
					location += w.length(); 
					file.seek(location);    // just position on top of first character
					                        // after the word.....
					wpIndex++; 
					io.output("\033[5m\033[1m" + w.toUpperCase() + "\033[0m");
				} else {
					ch = (char) (file.readByte()); 
					location++; 
					io.output(ch+""); 
					if (nLines > 0 && ch == '\n') countLines++; 
				}
			}
		} catch (IOException e) {
			// just continue....
		}

		
	}
	

	/**
	 * The following method reads the next word from the document's file
	 * at or after the current location in the file. A valid word in this
	 * case is any maximal sequence of letters. 
	 * 
	 * @return WordInDocument object containing the word and its location. 
	 *         null if no more words are found in the document from the 
	 *         current location. 
	 */
	public WordInDocument readNextWordFromFile() throws IOException { 
		String w = ""; 
		long location = 0; 
		char ch; 
		try { 
			ch = (char) (file.readByte()); 
			while (!Character.isAlphabetic(ch))
				ch = (char) (file.readByte()); 
			// the file pointer is located right after the first character
			// of the next word in the file. 
			location = file.getFilePointer() - 1; 
			while (Character.isAlphabetic(ch)) { 
				w = w + ch; 
				ch = (char) (file.readByte()); 
			}
		} catch (IOException e) {
			// just continue....
		}
		
		if (w.equals(""))
			   return null; 
			else 
			   return new WordInDocument(w.toLowerCase(), location); 
	}
	
	
	@Override
	/**
	 * Iterator of words as WordInDocument objects.
	 */
	public Iterator<WordInDocument> iterator() {
		return new WordIterator();
	} 
	
	/**
	 * Iterable of lines. 
	 * @return Iterable that allows iteration over lines in the 
	 * document. 
	 */
	public Iterable<String> lineIterable() { 
		return new LineIterable(); 
	}
	
	/**
	 * Iterable class for lines in the document. 
	 * @author pedroirivera-vega
	 *
	 */
	private class LineIterable implements Iterable<String> { 
		public Iterator<String> iterator() {
			return new LineIterator(); 
		}
	}
	
	/**
	 * Iterator class for lines in the document.
	 * @author pedroirivera-vega
	 *
	 */
	private class LineIterator implements Iterator<String> { 
		private String nextLine; 
		public LineIterator() { 
			try {
				file.seek(0); 
				nextLine = readNextLineFromFile(); 
			} catch (Exception e) { 
				nextLine = null; 
			}
		}
		
		@Override
		public boolean hasNext() {
			return nextLine != null;
		}

		@Override
		public String next() {
			if (!hasNext()) throw new IllegalStateException("No more words in file"); 
			String ltr = null;
			ltr = nextLine; 
			nextLine = readNextLineFromFile();      // prepare for the next next()
			return ltr;
		} 
		
		private String readNextLineFromFile() { 
			String s = ""; 
			char ch; 
			try { 
				ch = (char) (file.readByte()); 
				while (ch != '\n') {
					s += ch; 
					ch = (char) (file.readByte()); 
				}
			} catch (IOException e) {
				if (s.equals(""))
					s = null; 
			}
			return s; 
		}
		
	}
	
	/**
	 * Iterator class for words in the document.
	 * @author pedroirivera-vega
	 *
	 */
	private class WordIterator implements Iterator<WordInDocument> {
		private WordInDocument nextWord; 
		public WordIterator() { 
			try {
				file.seek(0); 
				nextWord = readNextWordFromFile(); 
			} catch (Exception e) { 
				nextWord = null; 
			}
		}
		
		@Override
		public boolean hasNext() {
			return nextWord != null;
		}

		@Override
		public WordInDocument next() {
			if (!hasNext()) throw new IllegalStateException("No more words in file"); 
			WordInDocument widTR = null;
			try {
				widTR = nextWord; 
				nextWord = readNextWordFromFile();      // prepare for the next next()
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return widTR;
		} 
	}
}
