package menuClasses;

import java.util.ArrayList;

public class MainMenu extends Menu {
	private static final MainMenu MM = new MainMenu(); 
	private MainMenu() { 
		super(); 
		String title; 
		ArrayList<Option> options = new ArrayList<Option>();  
		title = "Main Menu"; 
		options.add(new Option("Add a new document", new AddDocumentAction())); 
		options.add(new Option("Remove a document", new RemoveDocumentAction())); 
		options.add(new Option("Request information about a document", new RequestInformationAction())); 
		options.add(new Option("Perform searches based on words", new PerformSearchesAction())); 
		options.add(Option.EXIT); 

		super.InitializeMenu(title, options); 
	}
	
	public static MainMenu getMainMenu() { 
		return MM; 
	}
}
