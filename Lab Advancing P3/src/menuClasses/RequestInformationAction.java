package menuClasses;

import ioManagementClasses.IOComponent;

import java.io.IOException;

import dataManagement.DocsIDManager;

public class RequestInformationAction implements Action {
	private static IOComponent io = IOComponent.getComponent(); 
	
	@Override
	public void execute(Object args) {
		io.output("--- NEED ADDITIONAL WORK! --- FOR THE MOMENT, SHOWS ALL resgistered documents ---\n"); 
		try {
			DocsIDManager didm = DocsIDManager.getInstance();
			didm.showRegisteredDocs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
