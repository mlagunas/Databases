 package practica2;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseCreator {
	
	private String adecuar(String s){
		String aux=s;
		for (int i=0; i<s.length();i++){
			if (s.charAt(i)=='\''){
				aux= s.substring(0, i-1)+"'"+s.substring(i);
			}
		}
		return aux;
	}
	
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
	        
	        String tpNatural ="NUMBER(10)";
	        String tpAnno= "NUMBER(4)";
	        String tpNombre="VARCHAR(50)";
	        String tpGenero= "VARCHAR(1)";
	        String tpTipo="VARCHAR(25)";
	        String tpDescripcion="VARCHAR(75)";
	        		
	        
	        /*
	         * DOMINIOS
	         
	       
	        stmt.execute(
		            "CREATE DOMAIN tpNatural AS int"+
		            "CHECK(VALUE>=0)");
	        
	        stmt.execute(
		            "CREATE DOMAIN tpAnno AS int"+
		            "CHECK(VALUE>0 AND VALUE<2014)");
	        
	        stmt.execute(
		            "CREATE DOMAIN tpNombre AS VARCHAR(50)"+
		            "CHECK(VALUE IS NOT NULL)");
	        stmt.execute(
		            "CREATE DOMAIN tpGenero AS CHAR(1)"+
		            "CHECK (VALUE IN (‘m’, ‘f’)");
	        
	        stmt.execute(
		            "CREATE DOMAIN tpDescripcion AS VARCHAR(75)");
	        
	        stmt.execute(
		            "CREATE DOMAIN tpTipo AS VARCHAR(25)"+
		            "CHECK(VALUE IS NOT NULL)");
	        */

	        /*
	         * CREACI�N DE TABLAS
	         */
	        
	        /*
	         * 1. ENTIDADES
	         */
	        
	        //Tabla de Titulos
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Titulo")
	        	);
	        stmt.executeUpdate(
	            "CREATE TABLE Titulo "+
	            "( Id_Titulo "+tpNatural+" PRIMARY KEY,"+
	            " NombreTitulo "+tpNombre+" NOT NULL,"+
	            " AnnoProduccion "+tpAnno+","+
	            " AnnosEmision "+tpAnno+","+
	            " Tipo "+tpTipo+" NOT NULL)");
	        
	        System.out.println("\t Tabla Titulo.. [OK]");
	            
	        //Tabla de Personal
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Personal")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Personal "+
		            "(Id_Personal "+tpNatural+" PRIMARY KEY,"+ 
		            " NombrePersonal "+tpNombre+" NOT NULL,"+
		            " AnnoNacimiento "+tpAnno+" ,"+
		            " AnnoMuerte "+tpAnno+" ,"+
		            " Genero "+tpGenero+" ,"+
		            " LugarNacimiento "+tpDescripcion+","+
		            " LugarMuerte "+tpDescripcion+")");     
	        
	        System.out.println("\t Tabla Personal.. [OK]");
	          
	        /*
	         * 2. RELACIONES
	         */
	        
	        //Tabla Poseer
	         stmt.executeUpdate(
		        		DROP_TABLE_IF_EXISTS("Poseer")
		        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Poseer"+
		            "(Id_Personal "+tpNatural+","+
		            "FOREIGN KEY (Id_Personal) REFERENCES Personal(Id_Personal)," +
		            "FOREIGN KEY (Id_Titulo) REFERENCES Titulo(Id_Titulo),"+
		            "PRIMARY KEY (Id_Titulo, Id_Personal, Rol)," +
		            "Id_Titulo "+tpNatural+"," +
		            "Rol "+tpTipo+"," +
		            "Descripcion "+tpDescripcion+" )");
		           
	        System.out.println("\t Tabla Poseer.. [OK]");
	        
	        //Tabla EsSecuela
	       stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("EsSecuela")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE EsSecuela "+
		            "( Id_Pelicula "+tpNatural+","+
		            "Id_Pelicula_Enlazada "+tpNatural+","+
		            "PRIMARY KEY (Id_Pelicula, Id_Pelicula_Enlazada)," +
		            "FOREIGN KEY (Id_Pelicula) REFERENCES Titulo(Id_Titulo)," +
		            "FOREIGN KEY (Id_Pelicula_Enlazada) REFERENCES Titulo(Id_Titulo),"+
		            "Tipo "+tpTipo+" NOT NULL)");
	        
	        System.out.println("\t Tabla EsSecuela.. [OK]");
	        
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("Tags")
	        	);
	        stmt.executeUpdate(
		            "CREATE TABLE Tags "+
		            "( Id_Titulo "+tpNatural+" ,"+
		            "Tag "+tpDescripcion+" NOT NULL,"+
		            "PRIMARY KEY (Id_Titulo, Tag)," +
		            "FOREIGN KEY (Id_Titulo) REFERENCES Titulo(Id_Titulo))");
	        
	        System.out.println("\t Tabla Tags.. [OK]");
	        
	        stmt.executeUpdate(
	        		DROP_TABLE_IF_EXISTS("EpisodioDe")
	        	);
	        
	        stmt.executeUpdate(
		            "CREATE TABLE EpisodioDe "+
		            "( Id_Episodio "+tpNatural+","+
		            "Id_Serie "+tpNatural+","+
		            "PRIMARY KEY (Id_Episodio, Id_Serie)," +
		            "FOREIGN KEY (Id_Episodio) REFERENCES Titulo(Id_Titulo)," +
		            "FOREIGN KEY (Id_Serie) REFERENCES Titulo(Id_Titulo)," +
		            "NumEpisodio "+tpNatural+"," +
		            "NumTemporada "+tpNatural+")"); 
	        
	        System.out.println("\t Tabla EpisodioDe.. [OK]");

	        
	        /*
	         * TRIGGERS
	         */ 
	        
	        System.out.println("Creando triggers...");
	        stmt.execute(
	        "CREATE OR REPLACE TRIGGER ConsistenciaAnnosExistencia "+
	        "BEFORE INSERT OR UPDATE ON Personal  "+
	        "FOR EACH ROW "+
	        "BEGIN "+
	        "IF (:NEW.AnnoNacimiento > :NEW.AnnoMuerte) THEN "+
	        "RAISE_APPLICATION_ERROR (-20000, 'El año de nacimiento no puede ser superior al año de fallecimiento.'); "+ 
	        "END IF; "+
	        "END; ");

	        System.out.println("\t Trigger ConsistenciaAnnosExistencia.. [OK]");
	        
	        stmt.execute(
	        "CREATE OR REPLACE TRIGGER ConsistenciaCapitulos "+
	        "BEFORE INSERT OR UPDATE ON EpisodioDe "+
	        "FOR EACH ROW "+
	        "DECLARE "+
	         "numEpisodio "+tpNatural+"; numTemporada "+tpNatural+"; serie "+tpNatural+"; "+
	         "CURSOR cursorCapitulos IS SELECT id_serie, numEpisodio, numTemporada FROM EpisodioDe; "+
	        "BEGIN "+
	        "OPEN cursorCapitulos; "+
	         "LOOP "+
	         "FETCH cursorCapitulos INTO serie, numEpisodio, numTemporada; "+
	         "EXIT WHEN cursorCapitulos%NOTFOUND; "+
	          "IF (:NEW.numEpisodio = numEpisodio) AND (:NEW.id_serie = serie) AND (:NEW.numTemporada = numTemporada) THEN "+
	           "RAISE_APPLICATION_ERROR (-20001, 'Numero de episodio y temporada ya utilizadas para la serie con id= ' || serie); "+
	          "END IF; "+
	         "END LOOP; "+
	        "CLOSE cursorCapitulos; "+
	        "END; ");
		           
	        System.out.println("\t Trigger ConsistenciaCapitulos.. [OK]");
        
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
		String owner = "a586125";
		String password="";
		System.out.println("Creando tablas..");
		DataBaseCreator creator = new DataBaseCreator();
		creator.createDatabase("jdbc:oracle:thin:@hendrix-oracle.cps.unizar.es:1521:vicious",
				owner, password);
	  }

}
