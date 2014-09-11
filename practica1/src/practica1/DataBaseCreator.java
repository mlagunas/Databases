package practica1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseCreator {
	
	private String DROP_TABLE_IF_EXISTS(String my_table){
			return	"BEGIN "+
					"EXECUTE IMMEDIATE 'DROP TABLE "+my_table+" CASCADE CONSTRAINTS' ;"+
					"EXCEPTION "+
					"WHEN OTHERS THEN "+
	        			"IF SQLCODE != -942 THEN "+
	        				"RAISE; "+
	        			"END IF; "+
	        		"END;";
	}
	
	public void createDatabase(String driver, String username, String password) throws ClassNotFoundException
	{
	    Connection con;
		try {
			Class.forName ("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection(
			        driver, username,
			        password);

	        Statement stmt = con.createStatement();
	        
	        String tpAnno= "NUMBER(6)";
	        String tpNombre="VARCHAR(100)";
	        String tpCapacidad= "VARCHAR(7)";
	        String tpID="VARCHAR(20)";
	        String tpJornada="VARCHAR(20)";
	        String tpGol= "NUMBER(4)";
	        String tpDivision = "NUMBER(1)";
	        String tpTemporada= "VARCHAR(12)";
	        String tpPosicion= "VARCHAR(3)";
  
	        /*
	         * CREACIÓN DE TABLAS
	         */
	        
	        /*
	         * 1. ENTIDADES
	         */
	        
	        //Tabla de Estadios
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Estadio")
	        	);
	        stmt.executeUpdate(
	            "CREATE TABLE Estadio "+
	            "( nombreEstadio "+tpNombre+" PRIMARY KEY,"+
	            " annoInauguracion "+tpAnno+" ,"+
	            " capacidad "+tpCapacidad+" )");
	        
	        System.out.println("\t Tabla Estadios.. [OK]");

	        
	        //Tabla de Clubes
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Club")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Club "+
		            "( nombreClub "+tpNombre+" PRIMARY KEY,"+
		            " annoCreacion "+tpAnno+" ,"+
		            " annoDesaparacion "+tpAnno+")");    
	        
	        System.out.println("\t Tabla Club.. [OK]");

	        
	        //Tabla de Partidos
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Partido")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Partido "+
		            "(id "+tpID+" PRIMARY KEY,"+
		            " jornada "+tpJornada+" NOT NULL,"+
		            " golLocal "+tpGol+" NOT NULL,"+
		            " golVisitante "+tpGol+" NOT NULL)");
	        
	        System.out.println("\t Tabla Partidos.. [OK]");

	        
	      //Tabla Competicion
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Competicion")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Competicion "+
		            "( temporada "+tpTemporada+" , "+
		            " division "+tpDivision+" , "+
		            " PRIMARY KEY (temporada,division))");
	        
	        System.out.println("\t Tabla Competicion.. [OK]");


	        //Tabla Lugar
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Lugar")
	        	);
	         stmt.executeUpdate(
		            "CREATE TABLE Lugar "+
		            "(ciudad "+tpNombre+" PRIMARY KEY,"+
		            " comunidad "+tpNombre+" NOT NULL)");
		            
		        System.out.println("\t Tabla Lugar.. [OK]");
		         
	        /*
	         * 2. RELACIONES
	         */
	        
	         //Tabla Posee
	         stmt.executeUpdate(
		        		DROP_TABLE_IF_EXISTS("Posee")
		        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Posee "+
		            "( nombreClub "+tpNombre+" , "+
		            " nombreEstadio "+tpNombre+" , " +
		            " FOREIGN KEY (nombreClub) REFERENCES Club(nombreClub) ON DELETE CASCADE, " +
		            " FOREIGN KEY (nombreEstadio) REFERENCES Estadio(nombreEstadio) ON DELETE CASCADE, " +
		            " PRIMARY KEY (nombreClub, nombreEstadio)) "); 
	        
	        System.out.println("\t Tabla Posee.. [OK]");

	      //Tabla Localizado
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Localizado")
	        	);
	         stmt.executeUpdate(
		            "CREATE TABLE Localizado "+
		            "( nombreEstadio "+tpNombre+" , "+
				    " ciudad "+tpNombre+" , " +
		            "FOREIGN KEY (nombreEstadio) REFERENCES Estadio(nombreEstadio) ON DELETE CASCADE," +
		            "FOREIGN KEY (ciudad) REFERENCES Lugar(ciudad) ON DELETE CASCADE,"+
		            "PRIMARY KEY (nombreEstadio,ciudad))");
	       
		        System.out.println("\t Tabla Localizado.. [OK]");

		            
		    //Tabla SeJuegaEn
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("SeJuegaEn")
	        	);
	         stmt.executeUpdate(
		            "CREATE TABLE SeJuegaEn "+
		            "( id "+tpID+" , "+
				    " nombreEstadio "+tpNombre+" , " +
		            "FOREIGN KEY (id) REFERENCES Partido(id) ON DELETE CASCADE," +
		            "FOREIGN KEY (nombreEstadio) REFERENCES Estadio(nombreEstadio) ON DELETE CASCADE,"+
		            "PRIMARY KEY (id,nombreEstadio))");
	         
		        stmt.executeUpdate(
		        		DROP_TABLE_IF_EXISTS("tmpSeJuegaEn")
		        	);
		         stmt.executeUpdate(
			            "CREATE TABLE tmpSeJuegaEn "+
			            "( id "+tpID+" , "+
					    " nombreEstadio "+tpNombre+" , " +
			            "FOREIGN KEY (id) REFERENCES Partido(id) ON DELETE CASCADE," +
			            "FOREIGN KEY (nombreEstadio) REFERENCES Estadio(nombreEstadio) ON DELETE CASCADE,"+
			            "PRIMARY KEY (id,nombreEstadio))");
	         
		        System.out.println("\t Tabla SeJuegaEN.. [OK]");

		            
		    //Tabla Local
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Local")
	        	);
	         stmt.executeUpdate(
		            "CREATE TABLE Local "+
		            "( id "+tpID+" , "+
				    " nombreClub "+tpNombre+" , " +
		            "FOREIGN KEY (id) REFERENCES Partido(id) ON DELETE CASCADE," +
		            "FOREIGN KEY (nombreClub) REFERENCES Club(nombreClub) ON DELETE CASCADE,"+
		            "PRIMARY KEY (id,nombreClub))");
		            
		        System.out.println("\t Tabla Local.. [OK]");

		    //Tabla Visitante
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Visitante")
	        	);
	         stmt.executeUpdate(
		            "CREATE TABLE Visitante "+
		            "( id "+tpID+" , "+
		            " nombreClub "+tpNombre+" , " +
		            "FOREIGN KEY (id) REFERENCES Partido(id) ON DELETE CASCADE," +
		            "FOREIGN KEY (nombreClub) REFERENCES Club(nombreClub) ON DELETE CASCADE,"+
		            "PRIMARY KEY (id,nombreClub))");
		      
		        System.out.println("\t Tabla Visitante.. [OK]");
	         
	         //Tabla Pertenece
		        stmt.executeUpdate(
		        		DROP_TABLE_IF_EXISTS("Pertenece")
		        	);
		         stmt.executeUpdate(
			            "CREATE TABLE Pertenece "+
			            "( id "+tpID+" , "+
						" temporada "+tpTemporada+" , " +
						" division "+tpDivision+" , " +
			            "FOREIGN KEY (id) REFERENCES Partido(id) ON DELETE CASCADE, " +
			            "FOREIGN KEY (temporada,division) REFERENCES Competicion (temporada,division) ON DELETE CASCADE, "+
			            "PRIMARY KEY (id, temporada, division))");
		         
			        System.out.println("\t Tabla Pertenece.. [OK]");

		       //Tabla Clasifica
			        stmt.executeUpdate(
			        		DROP_TABLE_IF_EXISTS("Clasifica")
			        	);
			         stmt.executeUpdate(
				            "CREATE TABLE Clasifica "+
				            "( nombreClub "+tpNombre+" , "+
							" temporada "+tpTemporada+" , " +
							" division "+tpDivision+" , " +
				            " posicion "+tpPosicion+" NOT NULL, "+
				            "FOREIGN KEY (nombreClub) REFERENCES Club(nombreClub) ON DELETE CASCADE, " +
				            "FOREIGN KEY (temporada,division) REFERENCES Competicion(temporada,division) ON DELETE CASCADE, "+
				            "PRIMARY KEY (nombreClub, temporada, division))");
			         
				        System.out.println("\t Tabla Clasifica.. [OK]");

			         
				     
				     /*
				      * TRIGGERS
				      */
			         stmt.execute(
			        		 
			         "CREATE OR REPLACE trigger LocalDistintoDeVisitante "+
			         "BEFORE INSERT OR UPDATE ON Local "+
			         "FOR EACH ROW "+
			         "DECLARE "+
			         	"total NUMBER(2); "+
			         "BEGIN "+
			         	"SELECT COUNT(*) INTO total FROM visitante v where v.id=:NEW.id and v.nombreClub=:NEW.nombreClub; "+
			         	"IF (total > 0) THEN "+
			         		"RAISE_APPLICATION_ERROR (-20000, 'El equipo ' || :NEW.nombreClub || ', no puede jugar como local y visitante para el partido ' || :NEW.id); "+
			         	"END IF; "+
			         "END; ");
			         
				     System.out.println("\t Trigger LocaldistintoVisitante.. [OK]");

			         	
			         stmt.execute(
			        		 
			         "CREATE OR REPLACE trigger VisitanteDistintoDeLocal "+
			         "BEFORE INSERT OR UPDATE ON Visitante "+
			         "FOR EACH ROW "+
			         "DECLARE "+
			         	"total NUMBER(2); "+
			         "BEGIN "+ 
			         	"SELECT COUNT(*) INTO total FROM Local l where l.id=:NEW.id and l.nombreClub=:NEW.nombreClub; "+ 
			         	"IF (total > 0) THEN "+
			         		"RAISE_APPLICATION_ERROR (-20000, 'El equipo ' || :NEW.nombreClub || ', no puede jugar como local y visitante para el partido ' || :NEW.id); "+
			         	"END IF; "+
			         "END; ");
	        
				     System.out.println("\t Trigger VisitanteDistintoLocal[OK] ");
				     
				 //Calcula el numero de goles totales marcados por cada club en toda la competición
				 //Primero creamos la columna necesaria
				 
				 
				     stmt.executeUpdate(
				     "ALTER TABLE Club "+
				     "ADD ( TotalGoles NUMBER(5) DEFAULT 0 NOT NULL )");

				     stmt.execute(
				     "CREATE OR REPLACE TRIGGER TotalGolesLocal "+
				     "AFTER INSERT OR UPDATE ON Local "+
				     "FOR EACH ROW "+
				     "DECLARE "+
				     	"Goles NUMBER(2); "+
				     	"GolesAcumulados NUMBER(5); "+
				     "BEGIN "+ 
				 		"SELECT GolLocal INTO Goles FROM partido p WHERE id=:NEW.id; "+
				 		"SELECT TotalGoles INTO GolesAcumulados FROM Club c WHERE nombreClub=:NEW.nombreClub; "+
				 		"GolesAcumulados := GolesAcumulados + Goles; "+
				 		"UPDATE Club SET TotalGoles = GolesAcumulados WHERE nombreClub=:NEW.nombreClub; "+
				 	"END; ");

				    stmt.execute(
				 	"CREATE OR REPLACE TRIGGER TotalGolesVisitante "+
				 	"AFTER INSERT OR UPDATE ON Visitante "+
				 	"FOR EACH ROW "+
				 	"DECLARE "+
				 		"Goles NUMBER(2); "+
				 		"GolesAcumulados NUMBER(5); "+
				 	"BEGIN "+
				 		"SELECT GolLocal INTO Goles FROM partido p WHERE id=:NEW.id; "+
				 		"SELECT TotalGoles INTO GolesAcumulados FROM Club c WHERE nombreClub=:NEW.nombreClub; "+ 
				 		"GolesAcumulados := GolesAcumulados + Goles; "+
				 		"UPDATE Club SET TotalGoles = GolesAcumulados WHERE nombreClub=:NEW.nombreClub; "+
				 	"END; ");


				 //Calculo de porcentaje de victorias o empates en cada estadio por el equipo local
				 	//Primero añadimos la columna correspondiente
				    
				 	stmt.executeUpdate(
				 	"ALTER TABLE Estadio "+
				 	"ADD PorcentajeVictoriaOEmpate NUMBER DEFAULT 0 NOT NULL ");

				 	//Problema con tabla mutante solucionado
				 	stmt.execute(
				 	"CREATE OR REPLACE TRIGGER CalculoProcentajes "+
				 	"AFTER INSERT OR UPDATE ON seJuegaEn "+
				 	"FOR EACH ROW "+
				 	"BEGIN "+
				 		"INSERT INTO tmpSeJuegaEn VALUES (:NEW.id, :NEW.nombreEstadio); "+
				 	"END; ");
				 	
				 	stmt.execute(

				 	"CREATE OR REPLACE TRIGGER CalculoPorcentajesSentencia "+
				 	"AFTER INSERT OR UPDATE ON seJuegaEn "+
				 	"DECLARE "+
				 		"nombreE Estadio.nombreEstadio%TYPE; "+
				 		"id Partido.id%TYPE; "+
				 		"NoPerdidos NUMBER(5); "+
				 		"Totales NUMBER(5); "+
				 		"CURSOR c IS SELECT id, nombreEstadio FROM tmpSeJuegaEn; "+
				 	"BEGIN "+ 
				 		"OPEN c; "+
				 		"LOOP "+
				 		"FETCH c INTO id, nombreE; "+
				 		"EXIT WHEN c%NOTFOUND; "+
				 			"SELECT COUNT(*) INTO NoPerdidos "+ 
				 			"FROM Partido p "+
				 			"WHERE (p.GolLocal >= p.GolVisitante) "+ 
				 				"and p.id IN (SELECT id "+
				 						"FROM seJuegaEn s "+
				 						"WHERE s.nombreEstadio=nombreE); "+
				 						
				 			"SELECT COUNT(*) INTO Totales "+ 
				 			"FROM seJuegaEn p "+
				 			"WHERE p.id IN (SELECT id "+ 
				 						"FROM seJuegaEn s "+
				 						"WHERE s.nombreEstadio=nombreE); "+
				 	
				 			"UPDATE Estadio SET PorcentajeVictoriaOEmpate = (NoPerdidos/Totales) WHERE nombreEstadio=nombreE; "+
				 	
				 			"END LOOP; "+
				 			"CLOSE c; "+
				 			"DELETE FROM tmpSeJuegaEn; "+
				 		"END; ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException 
	{
		System.out.print("Propietario base de datos: ");
		/*Scanner sc=new Scanner(System.in);
		String owner=sc.nextLine();
		System.out.print("Contrasena: ");
		String password=sc.nextLine();*/
		/*String owner = "A504185";
		String password="oracle123456";
		DataBaseCreator creator = new DataBaseCreator();
		creator.createDatabase("jdbc:oracle:thin:@hendrix-oracle.cps.unizar.es:1521:vicious",
				owner, password);*/
		
		String owner = "SYSTEM";
		String password="Huge1311";
		DataBaseCreator creator = new DataBaseCreator();
		creator.createDatabase("jdbc:oracle:thin:@localhost:1521:oraclePablo",
				owner, password);
	  }

}
