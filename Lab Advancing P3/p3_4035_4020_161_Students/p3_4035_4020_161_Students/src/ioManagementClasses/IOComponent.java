package ioManagementClasses;

import java.util.Scanner;

/**
 * Singlenton classs to manage I/O
 * @author pirvos
 *
 */
public class IOComponent {
	private static final  IOComponent 	COMPONENT = new IOComponent(); 
	private Scanner sc; 
	
	private IOComponent() { 
		sc=new Scanner(System.in);
	}
	
	public static IOComponent getComponent() { 
		return COMPONENT; 
	}

	public String getInput(String msg) { 
		System.out.print(msg); 
		return sc.nextLine(); 
	}
	
	public void output(String line) { 
		System.out.print(line);
	}
	
	public int getInputInteger(String msg) { 
		int value = 0;    // value to read
		boolean repite; 
		do { 
		    repite = false; 
			try {
				value = Integer.parseInt(this.getInput(msg)); 
			} catch(Exception e) { 
				repite = true; 
				System.out.println("  ... invalid integer --");
			}
		} while (repite); 

		return value; 
	}
}
