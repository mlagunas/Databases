package practica1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Conversor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
        String data="data/DatosClubes.csv";
        File fichero = new File (data);
        
        File salidaF = new File("data/NuevosClubes.csv");
        PrintStream salida;
		Scanner origen;
		try {
			salida = new PrintStream (salidaF);
			origen = new Scanner (fichero);

		origen.useDelimiter("[\\t\\r]");
		
		System.out.println(origen.nextLine());
        
		while (origen.hasNext()){
			String club = origen.next().trim();
			//System.out.println(club);
			
			
			String ciudad = origen.next();
		//	System.out.println(ciudad);
			
			origen.nextInt();
			int fundacion = origen.nextInt();
		//	System.out.println(fundacion);
			
			String nombreCorto = origen.next();
		//	System.out.println(nombreCorto);
			
			String estadio = origen.next();
		//	System.out.println(estadio);
			
			String division = origen.next();
		//	System.out.println(division);
			String desaparicion=null;
			if (origen.hasNext()) {
				desaparicion = origen.nextLine().trim();
			}
			salida.println(club+","+ciudad+","+fundacion+","+fundacion+","+nombreCorto+","
			+estadio+","+division+","+desaparicion);
		} 
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
		

}
}
