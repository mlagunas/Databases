package practica2;

import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Properties;

import practica2.jdbc.Cursor;
import practica2.jdbc.JDBCTemplate;
import practica2.jdbc.MySQLConfiguration;
import practica2.jdbc.OracleConfiguration;

public class CopiaLocal {

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		JDBCTemplate oracle = null;
		JDBCTemplate mysql = null;
		
		Properties prop = new Properties();
		
		
		
		try {
			prop.load(EjemploCargaDatos.class
					.getResourceAsStream("practica2.properties"));
			oracle = configureOracle(prop);
			mysql = configureMySQL(prop);
			
			String nfichero = "movie_keyword.txt";
			File fichero = new File(nfichero);
			PrintStream out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM movie_keyword")) {			
				out.println(c.getInteger("movie_id")+";"+c.getInteger("keyword_id"));
			}
			out.close();

			//------------------------------------------------------
			
			nfichero = "keyword.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM keyword")) {			
				out.println(c.getInteger("id")+";"+c.getString("keyword"));
			}
			out.close();

			//----------------------------------------------------
			
			nfichero = "kind_type.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM kind_type")) {			
				out.println(c.getInteger("id")+";"+c.getString("kind"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "char_name.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM char_name")) {			
				out.println(c.getInteger("id")+";"+c.getString("name"));
			}
			out.close();
			
			
			//-----------------------------------------------------
			nfichero = "role_type.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM role_type")) {			
				out.println(c.getInteger("id")+";"+c.getString("role"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "info_type.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM info_type")) {			
				out.println(c.getInteger("id")+";"+c.getString("info"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "link_type.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM link_type")) {			
				out.println(c.getInteger("id")+";"+c.getString("link"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "person_info.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM person_info")) {			
				out.println(c.getInteger("person_id")+";"+c.getInteger("info_type_id")
						+";"+c.getString("info"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "name.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM name")) {			
				out.println(c.getInteger("id")+";"+c.getString("name")
						+";"+c.getString("gender"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "cast_info.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM cast_info")) {			
				out.println(c.getInteger("person_id")+";"+c.getInteger("movie_id")
						+";"+c.getInteger("person_role_id") +";"+c.getString("note")
						+";"+c.getInteger("nr_order")+";"+c.getInteger("role_id")
						);
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "movie_link.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM movie_link")) {			
				out.println(c.getInteger("movie_id")+";"+c.getInteger("linked_movie_id")
						+";"+c.getInteger("link_type_id")
						);
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "title.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM title")) {			
				out.println(c.getInteger("id")+";"+c.getString("title")
						+";"+c.getInteger("kind_id")+";"+c.getInteger("production_year") 
						+";"+c.getInteger("episode_of_id")+";"+c.getInteger("season_nr")
						+";"+c.getInteger("episode_nr")+";"+c.getString("series_years")
						);
			}
			out.close();
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (oracle != null) oracle.disconnect();
			if (mysql != null) mysql.disconnect();
		}
	}

	
	private static JDBCTemplate configureMySQL(Properties prop)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		JDBCTemplate mysql;
		mysql = new JDBCTemplate(new MySQLConfiguration(prop.getProperty("database.mysql.host"),
				prop.getProperty("database.mysql.port"),
				prop.getProperty("database.mysql.dbname")),
				prop.getProperty("database.mysql.user"),
				prop.getProperty("database.mysql.password")
				);
		mysql.connect();
		System.out.println("Conectado a " + mysql);
		return mysql;
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
