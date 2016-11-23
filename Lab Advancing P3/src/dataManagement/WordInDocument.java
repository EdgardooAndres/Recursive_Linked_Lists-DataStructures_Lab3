package dataManagement;

/**
 * This type of object represents a word in a document. For a particular
 * word, it contains the string that forms the particular word as well as
 * the location of that word in the file for the document that the word
 * is part of. The location is the index in file of the first character (byte)
 * in the word...
 * 
 * (NOT NECESSARILY COMPLETE)
 * 
 * @author pedroirivera-vega
 *
 */
public class WordInDocument implements Comparable<WordInDocument>{
	private String word; 
	private long location; 
	public WordInDocument(String word, long location) { 
		this.word = word; 
		this.location = location; 
	}
	
	@Override
	public int compareTo(WordInDocument other) {
		return this.word.compareTo(other.word);
	}
	
	public String getWord() {
		return word;
	}
	
	public long getLocation() {
		return location;
	}
	
	public boolean equals(Object other) { 
		if (other == null) return false; 
		if (!(other instanceof WordInDocument))
			return false; 
		WordInDocument oWID = (WordInDocument) other; 
		
		return word.equals(oWID.word); 
	}
	
	public String toString() { 
		return "["+word+", "+location+"]";
	}
}
