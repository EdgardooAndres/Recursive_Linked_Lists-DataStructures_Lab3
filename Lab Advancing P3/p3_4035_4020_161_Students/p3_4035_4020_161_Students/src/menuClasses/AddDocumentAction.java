package menuClasses;

import java.io.IOException;

import systemClasses.SystemController;
import generalClasses.P3Utils;
import ioManagementClasses.IOComponent;

public class AddDocumentAction implements Action {

	@Override
	public void execute(Object arg) {
		SystemController sc = (SystemController) arg; 
		IOComponent io = IOComponent.getComponent(); 
		io.output("\nAdding a new document to the index system:\n"); 
		String docName = io.getInput("\nEnter name of new document: ").trim(); 
		String statusMSG = null;
		try {
			statusMSG = sc.addNewDocument(docName);
			io.output(statusMSG);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


