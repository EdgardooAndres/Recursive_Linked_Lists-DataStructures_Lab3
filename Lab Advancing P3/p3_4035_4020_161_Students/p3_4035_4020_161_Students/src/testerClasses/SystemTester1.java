package testerClasses;

import java.io.IOException;

import systemClasses.SystemController;

public class SystemTester1 {

	public static void main(String[] args) throws IOException {
		SystemController sc = SystemController.getInstance(); 
		System.out.println(sc.addNewDocument("doc3"));
		
		System.out.println(sc.addNewDocument("doc1"));
		System.out.println(sc.addNewDocument("doc2"));
		System.out.println(sc.addNewDocument("doc3"));
		System.out.println(sc.addNewDocument("doc4"));
		System.out.println(sc.addNewDocument("doc5"));
		System.out.println(sc.addNewDocument("doc6"));
		System.out.println(sc.addNewDocument("doc7"));
		System.out.println(sc.addNewDocument("doc8"));
		System.out.println(sc.addNewDocument("doc9"));
		System.out.println(sc.addNewDocument("doc10"));
		System.out.println(sc.addNewDocument("doc11"));

		sc.close();

	}

}
