package practica1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class LimpiezaCaracteresEspeciales {

	/**
	 * Función que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public static String reemplazarCaracteresRaros(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}//reemplazarCaracteresRaros
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File entrada = new File ("data/practica1/NuevosClubes.csv");
		try {
			Scanner in = new Scanner (entrada);
			PrintStream out = new PrintStream (new File ("data/practica1/NuevosClubes_SinAcentos.cvs"));
			in.useDelimiter("[\\r]");
			while (in.hasNextLine()){
			out.println(reemplazarCaracteresRaros(in.nextLine()));
			}
			out.close();
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
