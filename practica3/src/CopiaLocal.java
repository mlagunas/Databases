package practica3;

import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Properties;
import practica3.jdbc.*;

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
					.getResourceAsStream("practica3.properties"));
			oracle = configureOracle(prop);
			mysql = configureMySQL(prop);
			
			String nfichero = "carriers.txt";
			File fichero = new File(nfichero);
			PrintStream out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM carriers")) {			
				out.println(c.getString("code")+";"+c.getString("name"));
			}
			out.close();

			//------------------------------------------------------
			
			nfichero = "cancellation.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM cancellation")) {			
				out.println(c.getString("code")+";"+c.getString("name"));
			}
			out.close();

			//----------------------------------------------------
			
			nfichero = "airports.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM airports")) {			
				out.println(c.getString("iata")+";"+c.getString("airport")
						+";"+c.getString("city")+";"+c.getString("state")
						+";"+c.getString("country")+";"+c.getString("airport")
						+";"+c.getDouble("lat")+";"+c.getDouble("lon"));
			}
			out.close();
			
			//-----------------------------------------------------
			nfichero = "planes.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM planes")) {			
				out.println(c.getString("tailnum")+";"+c.getString("manufacturer")
						+";"+c.getString("model")+";"+c.getString("aircraft_type")
						+";"+c.getString("engine_type")+";"+c.getInteger("year"));
			}
			out.close();
			
			
			//-----------------------------------------------------
			nfichero = "flights200810.txt";
			fichero = new File(nfichero);
			out = new PrintStream (fichero);
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM flights200810")) {			
				out.println(c.getString("flightDate")+";"+c.getString("carrier")
						+";"+c.getString("tailnum")+";"+c.getInteger("flightNum")
						+";"+c.getString("origin")+";"+c.getString("dest")
						+";"+c.getString("crsDepTime")+";"+c.getString("depTime")
						+";"+c.getString("wheelsOff")+";"+c.getString("wheelsOn")
						+";"+c.getString("crsArrTime")+";"+c.getString("arrTime")
						+";"+c.getInteger("cancelled")+";"+c.getString("cancellationCode")
						+";"+c.getInteger("diverted")+";"+c.getInteger("crsElapsedTime")
						+";"+c.getInteger("actualElapsedTime")+";"+c.getInteger("airTime")
						+";"+c.getInteger("distance")+";"+c.getInteger("carrierDelay")
						+";"+c.getInteger("weatherDelay")+";"+c.getInteger("nasDelay")
						+";"+c.getInteger("securityDelay")+";"+c.getInteger("lateAircraftDelay")
						+";"+c.getInteger("divAirportLandings")+";"+c.getInteger("divReachedDest")
						+";"+c.getInteger("divActualElapsedTime")+";"+c.getInteger("divArrDelay")
						+";"+c.getInteger("divDistance")+";"+c.getString("div1airport")
						+";"+c.getString("div1WheelsOm")+";"+c.getString("div1WheelsOff")
						+";"+c.getString("div1TailNum")+";"+c.getString("div2airport")
						+";"+c.getString("div2WheelsOm")+";"+c.getString("div2WheelsOff")
						+";"+c.getString("div2TailNum")
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
