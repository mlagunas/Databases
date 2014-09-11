package practica3;

import java.sql.SQLException;
import java.util.Properties;

import practica2.jdbc.*;

public class TransferenciaDatos {

	public static void main(String args[]) {
		JDBCTemplate oracle = null;
		JDBCTemplate oracle2 = null;
		JDBCTemplate mysql = null;
		JDBCTemplate mysql2=null;

		Properties prop = new Properties();
		
		try {
			prop.load(EjemploCargaDatos.class
					.getResourceAsStream("practica3.properties"));
			oracle = configureOracle(prop);
			oracle2 = configureOracle(prop);
			mysql = configureMySQL(prop);
			mysql2 = configureMySQL(prop);
			/**
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT title, production_year FROM title")) {

				// De cada fila extraemos los datos y los procesamos. 
				// A continuacion los insertamos en la BD Oracle.
				System.out.println("Insertando "+c.getString("title")+" - "+(2013-c.getInteger("production_year")));
				oracle.executeSentence("INSERT INTO PELICULAS_EJEMPLO(NOMBRE,ANTIGUEDAD) VALUES (?,?)", 
						c.getString("title"), 2013-c.getInteger("production_year"));
			}*/

			
			/*
			 * TABLA DE AEROPUERTOS
			 */
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM airports")){			
		
				oracle.executeSentence("INSERT INTO AEROPUERTOdeEEUU " +
						"(IATA, airport, city, state, latitude, longitude) VALUES" +
						"('"+c.getString("IATA")+"','"+c.getString("city")+"','"
						+c.getString("state") +"',"+c.getString("lat")+"," 
						+c.getString("long")+")");
			}
			
			/*
			 * TABLA FABRICANTE
			 */
			for(Cursor c: mysql.executeQueryAndGetCursor
					("SELECT DISTINCT(manufacturer) FROM planes")){	
				
				String manufacturer = c.getString("manufacturer");

				//Genera automáticamente los IDS
					oracle.executeSentence("INSERT INTO fabricante " +
							"(manufacturer) VALUES" +
							"('"+manufacturer+"')");
			}
			
			/*
			 * TABLA Avion, esModelo, creado
			 */
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM planes")){			
				
				String tailNum=c.getString("tailNum");
				oracle.executeSentence("INSERT INTO Avion " +
						"(tailNum, year) VALUES" +
						"('"+tailNum+"',"+c.getString("year")+")");
				
				String model = c.getString("model");
				if (model != null)
					oracle.executeSentence("INSERT INTO esModelo " +
							"(tailNum, model) VALUES" +
							"('"+tailNum+"','"+model+"')");
					
				
				String manufacturer = c.getString("manufacturer");
				int id_manufacturer=0;
				if (manufacturer != null){
					for(Cursor c2: oracle2.executeQueryAndGetCursor
						("SELECT id FROM fabricante where manufacturer='"+manufacturer+"'"))
						id_manufacturer=c2.getInteger("id");
					
					oracle.executeSentence("INSERT INTO creado " +
							"(tailNum, id) VALUES" +
							"('"+tailNum+"','"+id_manufacturer+"')");
				}		
				
			}
				
			/*
			 * TRABLA MODELO Y CREA
			 */
			for(Cursor c: mysql.executeQueryAndGetCursor
					("SELECT DISTINCT(Model), aircraft_type, engine_type, manufacturer " +
							"FROM planes")){	
				String model = c.getString("model");
				
				oracle.executeSentence("INSERT INTO Modelo " +
						"(model, aircraft_type) VALUES" +
						"('"+model+"','"+c.getString("aircraft_type")+"')");
				
				String manufacturer = c.getString("manufacturer");
				int id_manufacturer=0;
				if (manufacturer != null){
					for(Cursor c2: oracle2.executeQueryAndGetCursor
						("SELECT id FROM fabricante where manufacturer='"+manufacturer+"'"))
						id_manufacturer=c2.getInteger("id");
					
					oracle.executeSentence("INSERT INTO crea " +
							"(model, id, engine_type) VALUES" +
							"('"+model+"','"+id_manufacturer+"','"+c.getString("engine_type")+"')");
				}
			}
			
			
			/*
			 * RESTO DE TABLAS
			 */
			//Cargamos el contador
			int id = -1;
			for(Cursor c2: oracle2.executeQueryAndGetCursor
					("SELECT id_sequence.nextval FROM dual"))
				id=c2.getInteger("id_sequence.nextval");
			
			for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM flights200810")){			
				
				//Obtencion de carrier y cancellation
				String carrier_code = c.getString("carrier");
				String carrier="";
				if (carrier_code == null) 
					for(Cursor c2: mysql2.executeQueryAndGetCursor
							("SELECT name FROM carriers where code='"+carrier_code+"'"))
						carrier=c2.getString("name");
				
				String cancellationcode = c.getString("cancellationcode");
				String cancellation="";
				if (cancellationcode == null) 
					for(Cursor c2: mysql2.executeQueryAndGetCursor
							("SELECT name FROM cancellation where code='"+cancellationcode+"'"))
						cancellation=c2.getString("name");
				
				
				//Población de la GRAN TABLA (oremos por no equivocarnos aquí o será un suicidio)
				oracle.executeSentence("INSERT INTO VIAJE " +
						"(flightdate, carrier, flightnum,crsDepTime," +
						" depTime, crsArrTime, arrTime, cancellation," +
						" crsElapsedTime, actualElapsedTime, airTime, distance, " +
						" carrierDelay, weatherDelay, nasDelay, securityDelay, " +
						" lateAircraftDelay, divAirportLandings, divReachedDest, divActualElapsedTime, " +
						" divArrDelay, divDistance) VALUES" +
						"('"+c.getString("flightdate")+"','"+carrier+"',"+c.getString("flightnum") +",'"+c.getString("crsDepTime")+
						"','" +c.getString("depTime")+"','"+ c.getString("crsArrTime")+"','" +c.getString("arrTime")+"','"+cancellation+
						"'," +c.getString("crsElapsedTime")+","+ c.getString("actualElapsedTime")+"," +c.getString("airTime")+","+c.getString("distance")+
						"," +c.getString("carrierDelay")+","+ c.getString("weatherDelay")+"," +c.getString("nasDelay")+","+c.getString("securityDelay")+
						"," +c.getString("lateAircraftDelay")+","+ c.getString("divAirportLandings")+"," +c.getString("divReachedDest")+","+c.getString("divActualElapsedTime")+
						"," +c.getString("divArrDelay")+","+ c.getString("divDistance")+")");

				
				/*
				 * TABLA Vuelo
				 */
				String wheelsOn=c.getString("wheelsOn");
				if (wheelsOn == null) wheelsOn="";
				String wheelsOff=c.getString("wheelsOff");
				if (wheelsOff == null) wheelsOff="";
				
				oracle.executeSentence("INSERT INTO Vuelo " +
						"(wheelsOn, wheelsOff, desvioNum, id) VALUES" +
						"('"+wheelsOn+"','"+wheelsOff+"',0,"+id +")");
				
				wheelsOn=c.getString("div1wheelsOn");
				if (wheelsOn == null) wheelsOn="";
				wheelsOff=c.getString("div1wheelsOff");
				if (wheelsOff == null) wheelsOff="";
				
				oracle.executeSentence("INSERT INTO Vuelo " +
						"(wheelsOn, wheelsOff, desvioNum, id) VALUES" +
						"('"+wheelsOn+"','"+wheelsOff+"',1,"+id +")");
				
				wheelsOn=c.getString("div2wheelsOn");
				if (wheelsOn == null) wheelsOn="";
				wheelsOff=c.getString("div2wheelsOff");
				if (wheelsOff == null) wheelsOff="";
				
				oracle.executeSentence("INSERT INTO Vuelo " +
						"(wheelsOn, wheelsOff, desvioNum, id) VALUES" +
						"('"+wheelsOn+"','"+wheelsOff+"',2,"+id +")");
				
				
				
				
				/*
				 * TABLA origen
				 */
				
				String iata=c.getString("origin");
				if (iata!=null)
					oracle.executeSentence("INSERT INTO origen " +
							"(iata, id) VALUES" +
							"('"+iata+"',"+id +")");
				
				/*
				 * TABLA destino
				 */
				
				iata=c.getString("dest");
				if (iata!=null)
					oracle.executeSentence("INSERT INTO destino " +
							"(iata, id) VALUES" +
							"('"+iata+"',"+id +")");
				
				/*
				 * TABLA esDesvio
				 */
				
				iata=c.getString("div1airport");
				if (iata!=null)
					oracle.executeSentence("INSERT INTO esDesvio " +
							"(iata, desvioNum, id) VALUES" +
							"('"+iata+"',1,"+id +")");
				
				iata=c.getString("div2airport");
				if (iata!=null)
					oracle.executeSentence("INSERT INTO esDesvio " +
							"(iata, desvioNum, id) VALUES" +
							"('"+iata+"',2,"+id +")");
				
				
				/*
				 * TABLA esAvion
				 */
				String tailNum=c.getString("tailNum");
				if (tailNum!=null)
					oracle.executeSentence("INSERT INTO esAvion " +
							"(tailNum, desvioNum, id) VALUES" +
							"('"+tailNum+"',0,"+id +")");
				
				tailNum=c.getString("div1tailNum");
				if (tailNum!=null)
					oracle.executeSentence("INSERT INTO esAvion " +
							"(tailNum, desvioNum, id) VALUES" +
							"('"+tailNum+"',1,"+id +")");
				
				tailNum=c.getString("div2tailNum");
				if (tailNum!=null)
					oracle.executeSentence("INSERT INTO esAvion " +
							"(tailNum, desvioNum, id) VALUES" +
							"('"+tailNum+"',2,"+id +")");
				
				
				//Aumentamos el contador en local para evitar una conexión
				id++;
			}
			
			
			
			
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
		//System.out.println("Esto es aux: "+aux);
		return aux;
	}

}
