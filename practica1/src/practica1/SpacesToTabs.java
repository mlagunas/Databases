package practica1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class SpacesToTabs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File entrada = new File ("data/practica1/Estadios.csv");
		try {
			Scanner in = new Scanner (entrada);
			PrintStream out = new PrintStream (new File ("data/practica1/Estadios2.txt"));
			in.useDelimiter("\\t");
			while (in.hasNext()){
			out.println(in.next()+";"+ in.next()+";"+ in.next()+";"+ in.next()+";"+in.next()+";"+ in.next()+";"+ in.nextLine().trim());
			}
			out.close();
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
