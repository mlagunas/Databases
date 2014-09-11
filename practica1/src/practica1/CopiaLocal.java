package practica1;

import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Properties;
import jdbc.*;

public class CopiaLocal {

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		JDBCTemplate oracle = null;
		
		Properties prop = new Properties();
			
		try {
			prop.load(EjemploCargaDatos.class
					.getResourceAsStream("practica1.properties"));
			oracle = configureOracle(prop);
			
			String nfichero = "estadios.txt";
			File fichero = new File(nfichero);
			PrintStream out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Estadio")) {			
				out.println(c.getString("nombreEstadio")+";"+c.getString("annoInauguracion")+";"+c.getString("capacidad"));
			}
			out.close();

			//------------------------------------------------------
			
			nfichero = "clubes.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Club")) {			
				out.println(c.getString("nombreClub")+";"+c.getString("annoCreacion")+";"+c.getString("annoDesaparicion"));
			}
			out.close();

			//----------------------------------------------------
			
			nfichero = "partidos.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Partido")) {			
				out.println(c.getString("id")+";"+c.getString("jornada")
						+";"+c.getString("golLocal")+";"+c.getString("golVisitante"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "competiciones.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Competicion")) {			
				out.println(c.getString("temporada")+";"+c.getString("division"));
			}
			out.close();
			
			
			//-----------------------------------------------------
			nfichero = "lugares.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Lugar")) {			
				out.println(c.getString("ciudad")+";"+c.getString("comunidad"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "poseer.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Lugar")) {			
				out.println(c.getString("nombreClub")+";"+c.getString("nombreEstadio"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "localicados.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Localizado")) {			
				out.println(c.getString("nombreEstadio")+";"+c.getString("ciudad"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "sejuegaen.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM SeJuegaEn")) {			
				out.println(c.getString("id")+";"+c.getString("nombreEstadio"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "locales.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Local")) {			
				out.println(c.getString("id")+";"+c.getString("nombreClub"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "visitantes.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Visitante")) {			
				out.println(c.getString("id")+";"+c.getString("nombreClub"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "pertenece.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Pertenece")) {			
				out.println(c.getString("id")+";"+c.getString("temporada")+";"+c.getString("division"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "clasifican.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: oracle.executeQueryAndGetCursor("SELECT * FROM Clasifica")) {			
				out.println(c.getString("nombreClub")+";"+c.getString("temporada")+";"+c.getString("division"));
			}
			out.close();
			
			
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (oracle != null) oracle.disconnect();		}
	}

	private static JDBCTemplate configureOracle(Properties prop)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		JDBCTemplate oracle;
		oracle = new JDBCTemplate(new OracleConfiguration(prop.getProperty("database.oracle.host"),
				prop.getProperty("database.oracle.port"),
				prop.getProperty("database.oracle.sid")),
				prop.getProperty("database.oracle.user"),
				prop.getProperty("database.oracle.password")
				);
		oracle.connect();
		System.out.println("Conectado a " + oracle);
		return oracle;
	}
	
	private static String adecuar(String s){
		String aux = "";
		for (int i=0; i<s.length();i++){
			if (s.charAt(i)=='\''){
				if(i == 0){
					aux = "''";
				}
				else{
					aux= aux +"''";
				}
			}
			else{
				aux = aux+s.charAt(i);
			}
		}
		System.out.println("Esto es aux: "+aux);
		return aux;
	}


}
