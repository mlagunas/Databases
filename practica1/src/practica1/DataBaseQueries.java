package practica1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseQueries {
	
	/**
	 * Driver para conectar con ORACLE
	 */
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	/**
	 * Conexion a una BD ORACLE
	 */
	private static final String CONNECTION = "jdbc:oracle:thin:@";

	/**
	 * Host o maquina donde reside la BD a la que se quiere conectar
	 */
	String host = "";
	/**
	 * Puerto del host en el que escucha el servidor de ORACLE, es decir, puerto
	 * al que hay que conectar para acceder a la BD ORACLE
	 */
	String port = "";
	/**
	 * Nombre de la instancia o BD a la que se desea conectar
	 */
	String sid = "";
	/**
	 * CaDena de caracteres con el nombre de usuario, o login, a emplear para
	 * conectarse a la BD
	 */
	String user = "";
	/**
	 * Cadena de caracteres con el password, o contrase√±a, a emplear para
	 * conectarse a la BD
	 */
	String password = "";
	/**
	 * Conexion con la BD
	 */
	static Connection connection = null;
	
	/**
	 * MÈtodo para realizar una pregunta SQL a la BD (una sentencia SELECT)
	 * 
	 * @param sql
	 *            sentencia SQL
	 */
	/**
	 * Metodo para establecer la conexion JDBC con la BD
	 * <p>
	 * 
	 * @throws SQLException
	 * 
	 * @exception Lanza
	 *                una excepcion en caso de que se produzca algun error
	 */
	public void connect() throws SQLException {
		// Estableciendo la conexion con la BD
		if (port == null) {
			connection = DriverManager.getConnection(CONNECTION + host
					+ ":1521:" + sid, user, password);
		} else {
			connection = DriverManager.getConnection(CONNECTION + host + ":"
					+ port + ":" + sid, user, password);
		}
	}
	
	public static void executeQuery(String sql) {

		// Creamos una sentencia para poder usarla con la conexion que
		// tenemos abierta
		Statement stmt = null;
		try {
			System.out
					.println("---------------------------------------------------------------------------------------");
			stmt = connection.createStatement();
			// Formulamos la pregunta y obtenemos el resultado
			ResultSet rs = stmt.executeQuery(sql);

			// Convertiremos el resutlado obtenido (tabla), en una cadena de
			// caracteres
			// que en pantalla tenga aspecto de tabla...

			// Creamos la cabecera de la tabla...
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();

			for (int i = 1; i <= numberOfColumns; i++) {
				System.out.print(" " + rsmd.getColumnLabel(i) + "\t | ");
			}
			System.out.println();
			System.out
					.println("---------------------------------------------------------------------------------------");

			// Creamos las filas de la tabla con la informacion de la tuplas
			// obtenidas
			while (rs.next()) {// Por cada tupla
				// creamos una linea con la informacion:
				for (int j = 1; j <= numberOfColumns; j++) {
					System.out.print(" " + rs.getString(j) + "\t | ");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			System.out
					.println("---------------------------------------------------------------------------------------");
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}

	}
	/**
	 * MÈtodo para establecer la conexiÛn con la base de datos
	 */
	public static void connection(){
		Properties properties = new Properties();
		OracleTemplate q = null;
		
		try {
			properties.load(OracleTemplate.class
					.getResourceAsStream("practica1.properties"));
			q = new OracleTemplate(properties.getProperty("database.host"),
					properties.getProperty("database.port"),
					properties.getProperty("database.sid"),
					properties.getProperty("database.user"),
					properties.getProperty("database.password"));
			System.out.println("Conectado a " + q);
			q.connect();
			System.out.println("Conectado con √©xito a " + q);
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} 
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Conectamos con la base de datos
		connection();
		
		/*
		 * QUERY 1
		 */
		String query1 ="SELECT NombreClub" +
						"FROM Club";
		
		executeQuery(query1);
	}

}
