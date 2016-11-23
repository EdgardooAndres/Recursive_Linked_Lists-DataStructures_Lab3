package systemClasses;

import java.io.IOException;
import java.util.Stack;

import menuClasses.MainMenu;
import menuClasses.Menu;
import menuClasses.Option;

/**
 * Initiates the execution of P3's system
 * 
 * @author pedroirivera-vega
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		SystemController sc = SystemController.getInstance(); 
		sc.run();        // initiates the execution of system controller
		sc.close();     // saves modified index data
	}
}
