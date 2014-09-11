 package practica3;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	private String DROP_SEQUENCE_IF_EXISTS(String sequence){
		return	"BEGIN "+
				"EXECUTE IMMEDIATE 'DROP SEQUENCE "+sequence+"CASCADE CONSTRAINTS ' ;"+
				"EXCEPTION "+
				"WHEN OTHERS THEN "+
        			"IF SQLCODE != -2289 THEN "+
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
	        
	        /*
	         * DOMINIOS
	         */
	        String tpNum = "DECIMAL(20,0)";
	        String tpBinary = "DECIMAL(4,0)";
    		String tpNombre = "VARCHAR(100)";
			String tpDes = "NUMBER(2)";
			String tpId = "NUMBER(10)";
			String tpHora = "VARCHAR(8)";
			String tpIATA = "VARCHAR(5)";
			String tpCom = "VARCHAR(7)";
			String tpDate = "VARCHAR(12)";
	        		
	        /*
	         * 1. ENTIDADES
	         */
	        
	        //Tabla Viaje
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Viaje")
	        	);
	        stmt.executeUpdate(
	            "CREATE TABLE Viaje "+
	            "( id "+ tpId+" PRIMARY KEY ,"+
	            " flightdate "+ tpDate +" ,"+
	        	" cancellation "+ tpNombre +" ,"+
	        	" divReachedDest "+tpBinary+" ,"+
	        	" divAirportLandings "+ tpBinary+" ,"+
	        	" distance "+tpNum+" , airTime "+tpNum+" , flightNum "+ tpNum+" ,"+
	        	" crsElapsedTime "+tpNum+" , weatherDelay "+tpNum+ " , carrierDelay "+tpNum+
	        	" , actualElapsedTime " +tpNum+" , lateAircraftDelay "+tpNum+
	        	" , securityDelay "+tpNum+", nasDelay "+tpNum+
	        	" , divArrDelay "+tpNum+" , divActualElapsedTime "+tpNum+" , divDistance "+tpNum+
	        	" , carrier "+ tpNombre+" NOT NULL,"+
	        	" depTime "+ tpHora+" ,"+
	        	" crsDepTime "+tpHora+" , crsArrTime "+tpHora+" , arrTime "+ tpHora+")");
	        
	        //Auto_Increment en Oracle, se complementa con un trigger necesario
	        /*DROP_SEQUENCE_IF_EXISTS("id_sequence");
	        stmt.executeUpdate("CREATE SEQUENCE id_sequence "+
	        					"START WITH 1 " +
	        					"INCREMENT BY 1");
	        
	        System.out.println("\t Tabla Viaje.. [OK]");*/
	           
	        
	        //Tabla Vuelo
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Vuelo")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Vuelo "+
		            "( desvioNum "+ tpDes +" , "+
		        	" id "+tpId+ " , "+
		        	" wheelsOn "+tpHora+" , wheelsOff "+ tpHora+" , "+
		        	" PRIMARY KEY (desvioNum, id) , "+
		        	" FOREIGN KEY (id) REFERENCES Viaje (id) ON DELETE CASCADE)");
	        
	        System.out.println("\t Tabla Vuelo.. [OK]");
	        
	        //Tabla Avion
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Avion")
	        	);
	        stmt.executeUpdate(
	        		"CREATE TABLE Avion "+
	        		"( tailNum "+ tpCom+" PRIMARY KEY, "+
	        		" year "+tpNum+")");
	          
	        System.out.println("\t Tabla Avion.. [OK]");
	        
	        //Tabla Modelo
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Modelo")
	        	);
	        stmt.executeUpdate(
	        		"CREATE TABLE Modelo "+
	        		"( model "+ tpNombre+" PRIMARY KEY,"+
	        		" aircraft_type "+ tpNombre+" NOT NULL)");
	          
	        System.out.println("\t Tabla Modelo.. [OK]");
	        
	        //Tabla Fabricante
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Fabricante")
	        	);
	        stmt.executeUpdate(
	        		"CREATE TABLE Fabricante "+
	        		"( id "+tpId +" PRIMARY KEY, "+
	        		" manufacturer "+tpNombre+" NOT NULL)");
	          
	        //Auto_Increment en Oracle, se complementa con un trigger necesario
	       /* DROP_SEQUENCE_IF_EXISTS("id_fabricante_sequence");
	        stmt.executeUpdate("CREATE SEQUENCE id_fabricante_sequence "+
	        					"START WITH 1 " +
	        					"INCREMENT BY 1");*/
	        
	        System.out.println("\t Tabla Fabricante.. [OK]");
	        
	        //Tabla AEROPUERTOdeEEUU
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("AEROPUERTOdeEEUU")
	        	);
	        stmt.executeUpdate(
	        "CREATE TABLE AEROPUERTOdeEEUU "+
	        "( IATA "+tpIATA+" PRIMARY KEY, "+
	    	" longitude "+tpNum+" NOT NULL, latitude "+ tpNum +" NOT NULL, "+	
	    	" airport "+tpNombre+" NOT NULL, "+" state "+tpNombre+" NOT NULL, "+
	    	" city "+tpNombre+" NOT NULL)");
	        
	        System.out.println("\t Tabla AeropuertoDeEEUU.. [OK]");
	        
	        /*
	         * 2. RELACIONES
	         */
	        
	        //Tabla Origen
	         stmt.executeUpdate(
		        		DROP_TABLE_IF_EXISTS("Origen")
		        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Origen "+
		            "( IATA "+tpIATA+" , "+
		            " id " +tpId+" , "+
	        		" PRIMARY KEY (id, IATA), "+
		            " FOREIGN KEY (id) REFERENCES Viaje(id) ON DELETE CASCADE, " +
		            " FOREIGN KEY (IATA) REFERENCES AEROPUERTOdeEEUU(IATA) ON DELETE CASCADE)");
	        
	        System.out.println("\t Tabla Origen.. [OK]");
	        
	        //Tabla Destino
	         stmt.executeUpdate(
		        		DROP_TABLE_IF_EXISTS("Destino")
		        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Destino "+
		            "( IATA "+tpIATA+" , "+
		            "id " +tpId+" , "+
		            " FOREIGN KEY (id) REFERENCES Viaje(id) ON DELETE CASCADE, " +
		            " FOREIGN KEY (IATA) REFERENCES AEROPUERTOdeEEUU(IATA) ON DELETE CASCADE, "+
		            " PRIMARY KEY (id, IATA))");
		           
	        System.out.println("\t Tabla Destino.. [OK]");
	        
	        //Tabla esAvion
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("esAvion")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE esAvion "+
		            "( id " +tpId+" , "+
		            " desvioNum "+ tpDes +" , "+
		            " tailNum "+ tpCom+" , "+
		            " FOREIGN KEY (desvioNum,id) REFERENCES Vuelo(desvioNum,id) ON DELETE CASCADE, "+
		            " FOREIGN KEY (tailNum) REFERENCES Avion(tailNum) ON DELETE CASCADE, "+
		            " PRIMARY KEY (id, desvioNum, tailNum))");
	        
	        System.out.println("\t Tabla esAvion.. [OK]");
	        
	        
	        //Tabla esDesvio
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("esDesvio")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE esDesvio "+
		            "( id " +tpId+" , "+
		            " desvioNum "+ tpDes +" , "+
		            " IATA "+ tpIATA+" , "+
		            " PRIMARY KEY (id, desvioNum, IATA), " +
		            " FOREIGN KEY (desvioNum,id) REFERENCES Vuelo(desvioNum,id) ON DELETE CASCADE, "+
		            " FOREIGN KEY (IATA) REFERENCES AEROPUERTOdeEEUU(IATA) ON DELETE CASCADE)");
	        
	        System.out.println("\t Tabla esDesvio.. [OK]");
	        
	        //Tabla esModelo
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("esModelo")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE esModelo "+
		            "( model "+tpNombre+" , "+
		            " tailNum "+ tpCom+" , "+
		            " PRIMARY KEY (model,tailNum), " +
		            " FOREIGN KEY (model) REFERENCES Modelo(model) ON DELETE CASCADE, "+
	        		" FOREIGN KEY (tailNum) REFERENCES Avion(tailNum) ON DELETE CASCADE)");
	        
	        System.out.println("\t Tabla esModelo.. [OK]");
	        
	        
	        //Tabla crea
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("crea")
	        	);
	        stmt.executeUpdate(
	        		"CREATE TABLE crea "+
		            "( model "+tpNombre+" , " +
		            " id "+ tpId+" , "+
		            " engine_type "+tpNombre+" NOT NULL, "+
		            " PRIMARY KEY (model,id), " +
		            " FOREIGN KEY (model) REFERENCES Modelo(model) ON DELETE CASCADE, "+
	        		" FOREIGN KEY (id) REFERENCES Fabricante(id) ON DELETE CASCADE)");
	         
	        System.out.println("\t Tabla crea.. [OK]");
	        
	        
	        //Tabla creado
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("creado")
	        	);
	        stmt.executeUpdate(
	        		"CREATE TABLE creado "+
		            "( tailNum "+tpCom+" , "+
		            " id "+ tpId+" , "+
		            " PRIMARY KEY (tailNum,id), " +
		            " FOREIGN KEY (tailNum) REFERENCES Avion(tailNum) ON DELETE CASCADE, "+
	        		" FOREIGN KEY (id) REFERENCES Fabricante(id) ON DELETE CASCADE)");
	        
	        System.out.println("\t Tabla creado.. [OK]");

	        
	        /*
	         * TRIGGERS
	         */ 
	        
	    /*    System.out.println("Creando triggers...");
	        //TRIGGERS PARA AUTOINCREMENT DE LOS IDS NECESARIOS
	        stmt.execute(
	        		"CREATE OR REPLACE trigger id_sequence_trigger "+
	        		"BEFORE INSERT ON Viaje "+
	        		"FOR EACH ROW "+
	        		"BEGIN "+
	        			" SELECT id_sequence.nextval INTO :new.id FROM dual; "+
	        		" END;");

	        System.out.println("\t Trigger id_sequence_trigger... [OK]");
	        
	        
	        stmt.execute(
	        		"CREATE OR REPLACE trigger id_fabricante_sequence_trigger "+
	        		"BEFORE INSERT ON Fabricante "+
	        		"FOR EACH ROW "+
	        		"BEGIN "+
	        			" SELECT id_fabricante_sequence.nextval INTO :new.id FROM dual; "+
	        		" END;");

	        System.out.println("\t Trigger id_fabricante_sequence_trigger... [OK]");*/
	        
	       
	        //El destino no puede ser igual que el origen en un vuelo
	        stmt.execute(
		        "CREATE OR REPLACE trigger origenDistintoDeDestino "+
		        "BEFORE INSERT OR UPDATE ON origen "+
		        "FOR EACH ROW "+
		        "DECLARE "+
		        "total NUMBER(2); "+
		        "BEGIN "+
		    	"SELECT COUNT(*) INTO total FROM origen where (IATA=:NEW.IATA) and (id=:NEW.id); "+
		    	"IF (total > 0) THEN "+
		        	   "RAISE_APPLICATION_ERROR (-20000, 'El origen no puede ser igual al destino(IATA:' || :NEW.IATA || ', id del Viaje:' || :NEW.id); "+
		        	"END IF; "+
		        "END;");

	        System.out.println("\t Trigger origenDistintoDeDestino... [OK]");
	        
	        stmt.execute(
		        "CREATE OR REPLACE trigger destinoDistintoDeOrigen "+
		        "BEFORE INSERT OR UPDATE ON destino "+
		        "FOR EACH ROW "+
		        "DECLARE "+
		    	"total NUMBER(2); "+
		    	"BEGIN "+
		    	"SELECT COUNT(*) INTO total FROM origen where (IATA=:NEW.IATA) and (id=:NEW.id); "+
		    	"IF (total > 0) THEN "+
		        	   "RAISE_APPLICATION_ERROR (-20000, 'El origen no puede ser igual al destino(IATA:' || :NEW.IATA || ', id del Viaje:' || :NEW.id); "+
		        	"END IF; "+
		        "END; ");
	        
	        System.out.println("\t Trigger destinoDistintoDeOrigen... [OK]");
        
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
		//System.out.print("Propietario base de datos: ");
		/*Scanner sc=new Scanner(System.in);
		String owner=sc.nextLine();
		System.out.print("Contrasena: ");
		String password=sc.nextLine();*/
		String owner = "SYSTEM";
		String password="Huge1311";
		System.out.println("Creando tablas..");
		DataBaseCreator creator = new DataBaseCreator();
		creator.createDatabase("jdbc:oracle:thin:@localhost:1521:oraclePablo",
				owner, password);
	  }

}
