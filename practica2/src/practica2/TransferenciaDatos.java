package practica2;

import java.sql.SQLException;
import java.util.Properties;

import practica2.jdbc.*;

public class TransferenciaDatos {

	public static void main(String args[]) {
		JDBCTemplate oracle = null;
		JDBCTemplate mysql = null;
		JDBCTemplate mysql2=null;
		
		Properties prop = new Properties();
		
		
		
		try {
			prop.load(EjemploCargaDatos.class
					.getResourceAsStream("practica2.properties"));
			oracle = configureOracle(prop);
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
			 * TABLA DE TITULOS
			 */
			/*for(Cursor c: mysql.executeQueryAndGetCursor("SELECT * FROM title")){
				String valuesT="";
				String headT="";
				int id = c.getInteger("id");
				String title = adecuar(c.getString("title"));
				int kind_id = c.getInteger("kind_id");
				String production_year = adecuar(c.getString("production_year"));
				String series_years = c.getString("series_years");
				String tipo="";
				
								
				for(Cursor k: mysql2.executeQueryAndGetCursor("SELECT kind FROM kind_type where id="+kind_id)){
					tipo= adecuar(k.getString("kind"));
				}
				
				
				if (series_years==null){
					headT+=", AnnosEmision";
					valuesT+=",";
				}
				else{
					headT+=", AnnosEmision";
					int series_years2=Integer.parseInt(series_years.substring(series_years.length()-9, series_years.length()-5));										
					valuesT+=","+series_years2;
				}
					
				if (production_year==null){
					headT+=", AnnoProduccion";
					valuesT+=",";
				}
				else{
					headT+=", AnnoProduccion";
					int production_year2=Integer.parseInt(production_year.substring(production_year.length()-4, production_year.length()));										
					valuesT+=","+production_year2;
				}
				System.out.println("Pelicula-->("+id+","+title+","+tipo +valuesT+")");

				
				oracle.executeSentence("INSERT INTO Titulo " +
						"(Id_Titulo, NombreTitulo, Tipo"+headT+") VALUES" +
						"("+id+",'"+title+"','"+tipo +"'"+valuesT+")");
			}*/
			/*
			 * TABLA DE PERSONAL
			 */
			
			int id_anterior = -1;
			for(Cursor cp: mysql.executeQueryAndGetCursor("SELECT person_id FROM cast_info")){
				
				String name="";
				String gender="";
				
				int id = cp.getInteger("person_id");
				if (id != id_anterior){	
					id_anterior = id;
					for(Cursor c3 : mysql2.executeQueryAndGetCursor
							("SELECT * FROM name WHERE id="+id)){
						name = adecuar(c3.getString("name"));
						gender = c3.getString("gender");
						System.out.println("Nombre: "+name+".");
					}
					
					int AnnoNacimiento = 0;
					int AnnoMuerte = 0;
					String LugarNacimiento = null;
					String LugarMuerte = null;
					String auxNacimiento = null;
					String auxMuerte = null;
					String values="";
					String head="";
					for(Cursor c2: mysql2.executeQueryAndGetCursor("SELECT * FROM person_info WHERE person_id="+id)){
						switch(c2.getInteger("info_type_id")){
						
							case 20: 	LugarNacimiento=c2.getString("info");
										values+=",'"+LugarNacimiento+"'";
										head+=",LugarNacimiento";
										break;
							case 21: 	auxNacimiento = c2.getString("info");
										AnnoNacimiento=Integer.parseInt(auxNacimiento.substring(
												auxNacimiento.length()-4, auxNacimiento.length()));										
										
										values+=",'"+AnnoNacimiento+"'";
										head+=",AnnoNacimiento";
										break;
							case 23: 	auxMuerte = c2.getString("info");						
										AnnoMuerte=Integer.parseInt(auxMuerte.substring(
												auxMuerte.length()-4, auxMuerte.length()));	
										values+=",'"+AnnoMuerte+"'";
										head+=",AnnoMuerte";
										break;
							case 39: 	LugarMuerte=c2.getString("info"); 
										values+=",'"+LugarMuerte+"'";
										head+=",LugarMuerte";
										break;
						}
					}
					if (auxNacimiento == null){
						head+=",AnnoNacimiento";
						values+=",''";
					}

					if(auxMuerte == null){
						head+=",AnnoMuerte";
						values+=",''";
					}

					if(LugarNacimiento == null){
						head+=",LugarNacimiento";
						values+=",''";
					}
					if(LugarMuerte == null){
						head+=",LugarMuerte";
						values+=",''";
					}
					System.out.println("Personal--> ("+id+",'"+name+values+",'"+gender+"','"+LugarNacimiento+
							"','"+LugarMuerte+"')");
					
					if (gender!=null)
						oracle.executeSentence("INSERT INTO Personal " +
								"(id_Personal, NombrePersonal, Genero"+head+") VALUES" +
								"("+id+",'"+name+"',''"+values+")");
					else
						oracle.executeSentence("INSERT INTO Personal " +
								"(id_Personal, NombrePersonal, Genero"+head+") VALUES" +
								"("+id+",'"+name+"','"+gender+"'"+values+")");
				}
			}
			
			/*
			 * TABLA POSEER
			 */
		/*
			for(Cursor c1: mysql.executeQueryAndGetCursor("SELECT * FROM cast_info where person_role_id is not null")){
				
				int id = c1.getInteger("person_id");
				int pelicula_id = c1.getInteger("movie_id");
				int rol_id = c1.getInteger("role_id");
				String descripcion ="";
				String rol="";
				
				if (rol_id == 1 || rol_id ==2){
					for (Cursor n:mysql2.executeQueryAndGetCursor("SELECT name FROM char_name where id="+id)){
						descripcion=n.getString("name");
					}		
				}
					
				for(Cursor r:mysql2.executeQueryAndGetCursor("SELECT role FROM role_type where id="+rol_id)){
					rol = r.getString("role");
				}
				
				
				if (rol == null)
					rol="";
				
				if (descripcion == null)
					descripcion="";
				System.out.println("Poseer --> ("+id+","+pelicula_id+",'"+rol+"','"+descripcion+"')");


				oracle.executeSentence("INSERT INTO Poseer " +
				"(Id_Personal, Id_Titulo, Rol, Descripcion) VALUES" +
				"("+id+","+pelicula_id+",'"+rol+"','"+descripcion+"')");
			}
			*/
			/*
			 * TABLA TAGS
			 */
			/*
			for(Cursor c1: mysql.executeQueryAndGetCursor("SELECT * FROM movie_keyword WHERE keyword_id ="+38174+
					" OR keyword_id ="+6254)){
				/*-------------------------------
				 * las que no son no ficción
				 * SELECT DISTINCT movie_id FROM movie_keyword WHERE keyword_id <> 46
				 * ------------------------------
				 */
	/*
				int movie_id=c1.getInteger("movie_id");
				int keyword_id = c1.getInteger("keyword_id"); 
				System.out.println(keyword_id);
				if(keyword_id==38174 || keyword_id==6254){
					for(Cursor cT: mysql.executeQueryAndGetCursor("SELECT * FROM keyword WHERE keyword_id="+keyword_id)){		
						String description=adecuar(cT.getString("keyword"));
						
				System.out.println("TAGS--> z"+movie_id+","+description);
					
					oracle.executeSentence("INSERT INTO Tener" +
							"(Id_Titulo, Tag) VALUES"+
							"("+movie_id+",'"+description+"')");	
				}
				}
				
			}
			*/
			/*
			 * TABLA EPISODIO DE
			 */
			/*
			for(Cursor c11: mysql.executeQueryAndGetCursor("SELECT * FROM title where kind_id="+7)){
				
				int id = c11.getInteger("id");
				int episode_of_id = c11.getInteger("episode_of_id");
				int season_nr = c11.getInteger("season_nr");
				int episode_nr = c11.getInteger("episode_nr");
				
				System.out.println("EpisdioDe --> ("+id+","+episode_of_id+",'"+season_nr+","+episode_nr+")");
										oracle.executeSentence("INSERT INTO EpisodioDe " +
				"(Id_Episodio, Id_Serie, NumEpisodio, NumTemporada) VALUES" +
				"("+id+","+episode_of_id+","+episode_nr+","+season_nr+")");
			}*/
				
			/*
			 * TABLA ES SECUELA
			 */
			 /*
			for(Cursor c11: mysql.executeQueryAndGetCursor("SELECT * FROM movie_link")){
				
				int movie_id1 =c11.getInteger("movie_id");
				int linked_movie_id = c11.getInteger("linked_movie_id");
				int link_type_id = c11.getInteger("link_type_id");
				String tipo1="";
				
				for(Cursor t:mysql2.executeQueryAndGetCursor("SELECT link FROM link_type WHERE id="+link_type_id)){
					tipo1=t.getString("link");
				}
				
				System.out.println("Relacionado --> ("+movie_id1+","+linked_movie_id+",'"+tipo1+"')");
				
				oracle.executeSentence("INSERT INTO EsSecuela " +
				"(Id_Pelicula, Id_Pelicula_Enlazada, Tipo) VALUES" +
				"("+movie_id1+","+linked_movie_id+",'"+tipo1+"')");
			}
			*/
				
			
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
