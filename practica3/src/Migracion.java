package practica3;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Scanner;

import practica3.jdbc.*;

public class Migracion {

	public static void main(String args[]) {
		JDBCTemplate oracle = null;

		Properties prop = new Properties();
		
		try {
			prop.load(EjemploCargaDatos.class
					.getResourceAsStream("practica3.properties"));
			oracle = configureOracle(prop);
			int hora, minutos, segundos;
			Calendar calendario = new GregorianCalendar();
			hora =calendario.get(Calendar.HOUR_OF_DAY);
			minutos = calendario.get(Calendar.MINUTE);
			segundos = calendario.get(Calendar.SECOND);
			System.out.println("Hora de comienzo: "+hora + ":" + minutos + ":" + segundos);
			
			/*
			 * TABLA AEROPUERTOS
			 */
			File f = new File("./data/practica3/csvs/AeropuertodeEEUU.csv");
			Scanner d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO AEROPUERTOdeEEUU " +
						"(IATA, airport, city, state, latitude, longitude) VALUES" +
						"('"+d.next()+"','"+adecuar(d.next())+"','"+adecuar(d.next())+"','"+d.next()+"','"
						+d.next()+"','" +d.nextLine().trim().substring(1)+"')");
			
			System.out.println("Tabla Aeropuerto.. [OK]");
			/*
			 * TABLA FABRICANTE
			 */
			f = new File("./data/practica3/csvs/fabricante.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO fabricante " +
						"(id,manufacturer) VALUES" +
						"("+d.next()+",'"+d.nextLine().trim().substring(1)+"')");
			
			System.out.println("Tabla Fabricante.. [OK]");
			
			/*
			 * TABLA AVION
			 */
			f = new File("./data/practica3/csvs/avion.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO avion " +
						"(tailnum,year) VALUES" +
						"('"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla Avion.. [OK]");
			
			/*
			 * TABLA MODELO
			 */
			f = new File("./data/practica3/csvs/modelo.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO modelo " +
						"(model,aircraft_type) VALUES" +
						"('"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla Modelo.. [OK]");
			
			
			/*
			 * TABLA ESMODELO
			 */
			f = new File("./data/practica3/csvs/esmodelo.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO esmodelo " +
						"(tailnum,model) VALUES" +
						"('"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla esModelo.. [OK]");
			
			/*
			 * TABLA CREADO
			 */
			f = new File("./data/practica3/csvs/creado.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO creado " +
						"(id,tailnum) VALUES" +
						"('"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");
			
			System.out.println("Tabla creado.. [OK]");
			
			/*
			 * TABLA CREA
			 */
			f = new File("./data/practica3/csvs/crea.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO crea " +
						"(model,id,engine_type) VALUES" +
						"('"+d.next()+"','"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla crea.. [OK]");
			/*
			 * TABLA VIAJE
			 */
			f = new File("./data/practica3/csvs/viaje.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO VIAJE " +
						"(id,flightdate, carrier, flightnum,crsDepTime," +
						" depTime, crsArrTime, arrTime, cancellation," +
						" crsElapsedTime, actualElapsedTime, airTime, distance, " +
						" carrierDelay, weatherDelay, nasDelay, securityDelay, " +
						" lateAircraftDelay, divAirportLandings, divReachedDest, divActualElapsedTime, " +
						" divArrDelay, divDistance) VALUES " +
						"("+d.next()+",'"+d.next()+"','"+d.next()+"','"+d.next() +"','"+d.next()+
						"','" +d.next()+"','"+ d.next()+"','" +d.next()+"','"+d.next()+
						"','" +d.next()+"','"+ d.next()+"','" +d.next()+"','"+d.next()+
						"','" +d.next()+"','"+ d.next()+"','" +d.next()+"','"+d.next()+
						"','" +d.next()+"','"+ d.next()+"','" +d.next()+"','"+d.next()+
						"','" +d.next()+"','"+ d.nextLine().trim().substring(1)+"')");


			System.out.println("Tabla Viaje.. [OK]");
			
			
					
			/*
			 * TRABLA VUELO
			 */
			f = new File("./data/practica3/csvs/vuelo.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO vuelo " +
						"(id,desvionum,wheelson, wheelsoff) VALUES" +
						"('"+d.next()+"','"+d.next()+"','"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla vuelo.. [OK]");
			
			
			/*
			 * TRABLA ESAVION
			 */
			f = new File("./data/practica3/csvs/esavion.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO esavion " +
						"(id,desvionum,tailnum) VALUES" +
						"('"+d.next()+"','"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla esavion.. [OK]");
			
			/*
			 * TABLA ES DESVIO
			 */
			f = new File("./data/practica3/csvs/esdesvio.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO esdesvio " +
						"(id,desvionum,IATA) VALUES" +
						"('"+d.next()+"','"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla esdesvio.. [OK]");
			
			/*
			 * TABLA ORIGEN
			 */
			f = new File("./data/practica3/csvs/origen.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO origen " +
						"(IATA,id) VALUES" +
						"('"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla origen.. [OK]");
			
			/*
			 * TABLA DESTINO
			 */
			f = new File("./data/practica3/csvs/destino.csv");
			d = new Scanner (f);
			d.useDelimiter("\\s*[;]\\s*");
			d.nextLine(); //Titulos
			while(d.hasNext())
				oracle.executeSentence("INSERT INTO destino " +
						"(IATA,id) VALUES" +
						"('"+d.next()+"','"+d.nextLine().trim().substring(1)+"')");

			System.out.println("Tabla destino.. [OK]");
			
			calendario = new GregorianCalendar();
			hora =calendario.get(Calendar.HOUR_OF_DAY);
			minutos = calendario.get(Calendar.MINUTE);
			segundos = calendario.get(Calendar.SECOND);
			System.out.println("Hora de finalización: "+hora + ":" + minutos + ":" + segundos);
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			if (oracle != null) oracle.disconnect();
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
		String aux=s;
		for (int i=0; i<s.length();i++){
			if (s.charAt(i)=='\''){
				aux= s.substring(0, i-1)+"'"+s.substring(i);
			}
		}
		return aux;
	}
}