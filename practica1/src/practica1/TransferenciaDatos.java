package practica1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import practica1.jdbc.*;


public class TransferenciaDatos {
	
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
	
	
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, SQLException, IOException {
		JDBCTemplate oracle = null;		
		Properties prop = new Properties();	
		try {
			prop.load(EjemploCargaDatos.class
					.getResourceAsStream("practica1.properties"));
			oracle = configureOracle(prop);
		 
        /**
         * AÑADIR DATOS
         *//*
	    String data="data/practica1/Estadios_SinAcentos.csv";
        File fichero = new File (data);
        
		Scanner origen = null;
		try {
			origen = new Scanner (fichero);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		origen.useDelimiter("[;\\r]");
		
		System.out.println(origen.nextLine());
		
		while (origen.hasNext()){
			
			String estadio = adecuar(origen.next().trim());
			String ciudad = adecuar(origen.next().trim());

			String comunidad = adecuar(origen.next().trim());

			String inauguracion = ""+origen.nextInt()+"";
			if (inauguracion.equals("-1")){
				inauguracion="";
			}
			int capacidad = origen.nextInt();
			origen.nextLine(); 
			//System.out.println(capacidad);
			
			//System.out.println(estadio+","+inauguracion+","+capacidad+","+comunidad);
			
			oracle.executeSentence("INSERT INTO Estadio (nombreEstadio, annoInauguracion, capacidad) " +
					"VALUES ('"+estadio+"','"+inauguracion+"',"+ capacidad+")");
			
			//System.out.println(ciudad+", "+comunidad);
			oracle.executeSentence("INSERT INTO Lugar (ciudad, comunidad) VALUES ('"+ciudad+"','"+comunidad+"')");
			
			//System.out.println(estadio+", "+ciudad);
			oracle.executeSentence("INSERT INTO Localizado (nombreEstadio, ciudad) VALUES ('"+estadio+"','"+ciudad+"')");
			
		}
		
		String [] estadios = new String [172];
		String [] nombres = new String [172];
		
		String data1="data/practica1/NuevosClubes_SinAcentos.csv";
        File fichero1 = new File (data1);
        
		Scanner origen1 = null;
		try {
			origen1 = new Scanner (fichero1);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		origen1.useDelimiter("[,\\r]");
		
		origen1.nextLine();
        
		int j =0;
		while (origen1.hasNext()){
			String club = adecuar(origen1.next().trim()).substring(1);
			nombres[j]=club;
			
			String ciudad = adecuar(origen1.next());
			
			origen1.nextInt();
			int fundacion = origen1.nextInt();
		//	System.out.println(fundacion);
			
			String nombreCorto = adecuar(origen1.next());
		//	System.out.println(nombreCorto);
			
			String estadio = adecuar(origen1.next());
		//	System.out.println(estadio);
			estadios[j]=estadio;
			
			String division = adecuar(origen1.next());
		//	System.out.println(division);
			String desaparicion=origen1.next();
			origen1.nextLine();
			j++;
			//System.out.println(club+";"+fundacion+";"+estadio+";"+desaparicion);
			oracle.executeSentence("INSERT INTO Club (nombreClub, annoCreacion, annoDesaparacion) "+
				"VALUES ('"+club+"',"+fundacion+",'"+desaparicion+"')");	
			
			//System.out.println(club+", "+estadio);
			oracle.executeSentence("INSERT INTO Posee (nombreEstadio, nombreClub) "+
			"VALUES ('"+estadio+"','"+club+"')");
		}
		
		for(int i=1972;i<2015;i++){
			String primera = "1";
			String segunda = "2";
			//System.out.println(i+","+primera);
			oracle.executeSentence("INSERT INTO Competicion (temporada, division) "+
					"VALUES ("+i+",'"+primera+"')");
			oracle.executeSentence("INSERT INTO Competicion (temporada, division) "+
					"VALUES ("+i+",'"+segunda+"')");
		}
		
		
		data = "data/practica1/data_Jornadas_2_Sin.txt";
		fichero = new File (data);
        
		origen = null;
		try {
			origen = new Scanner (fichero);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		origen.useDelimiter("[;\\r]+");
		
        int i =0;
		while (origen.hasNext()){
			String temporada = adecuar(origen.next().trim());
			
			String añoI=temporada.substring(0, 4);
			//System.out.println(añoI);
			//System.out.println(club);
					
			String division = adecuar(origen.next());
		//	System.out.println(division);
			String jornada ="";
			if (!( division.length() > 3))
			jornada = ""+origen.nextInt();
		//	System.out.println(jornada);
						
			String local = adecuar(origen.next());
			if (local.charAt(0) == ' '){
				local=local.substring(1, local.length());
			}
			//System.out.println(local);
		//	System.out.println(local);			
			
			String visitante = adecuar(origen.next());
			if (visitante.charAt(0) == ' '){
				visitante=visitante.substring(1, visitante.length());
			}
		//	System.out.println(visitante);
			
			String goles =  origen.next();
			int golesLocal=goles.charAt(0)-48;
			int golesVisitante=goles.charAt(2)-48;
			origen.nextLine();
			
			//System.out.println(i+", "+añoI+", "+jornada+", "+goles+", "+golesLocal+", "+golesVisitante);
			//System.out.println(local+", "+visitante+", "+division+", "+temporada);
			
			oracle.executeSentence("INSERT INTO Partido (id, jornada, golLocal, golVisitante) "+
				"VALUES ("+i+",'"+jornada+"',"+golesLocal+","+golesVisitante+")");	
			
			//System.out.println(i+", "+local);
			oracle.executeSentence("INSERT INTO Local (id, nombreClub) "+
			"VALUES ("+i+",'"+local+"')");
			
			//System.out.println(i+", "+visitante);
			oracle.executeSentence("INSERT INTO Visitante (id, nombreClub) "+
			"VALUES ("+i+",'"+visitante+"')");
			
			//System.out.println(i+", "+añoI+", "+division);
			oracle.executeSentence("INSERT INTO Pertenece (id, temporada, division) "+
			"VALUES ("+i+",'"+añoI+"','"+division+"')");
			
			for (int h =0; h<nombres.length; h++)
				if (local.equals(nombres[h])){
					String estadio=estadios[h];
					oracle.executeSentence("INSERT INTO SeJuegaEn (id, nombreEstadio) "+
			"VALUES ("+i+",'"+estadio+"')");
				}
			
			i++;
		}	
		
		String data = "data/practica1/ClasiPrimera_SinAcentos.csv";
		File fichero = new File (data);
        
		Scanner origen = null;
		try {
			origen = new Scanner (fichero);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		origen.useDelimiter("[;\\r]");
		origen.nextLine();  
		while (origen.hasNext()){
			int añoI = origen.nextInt();
			int añoF = origen.nextInt();
			
			String primero = adecuar(origen.next().trim());
			String segundo = adecuar(origen.next().trim());
			String tercero = adecuar(origen.next().trim());
			origen.nextLine();
			
			String division= "1";
			
			//System.out.println(añoI+","+primero+","+segundo+","+tercero+","+division);
			oracle.executeSentence("INSERT INTO Clasifica (nombreClub, temporada, division, posicion) "+
					"VALUES ('"+primero+"',"+añoI+",'"+division+"',"+1+")");
			oracle.executeSentence("INSERT INTO Clasifica (nombreClub, temporada, division, posicion) "+
					"VALUES ('"+segundo+"',"+añoI+",'"+division+"',"+2+")");
			oracle.executeSentence("INSERT INTO Clasifica (nombreClub, temporada, division, posicion) "+
					"VALUES ('"+tercero+"',"+añoI+",'"+division+"',"+3+")");
		}
		*/
		String data="data/practica1/ClasiSegunda_SinAcentos.csv";
		File fichero = new File (data);
        
		Scanner origen = null;
		try {
			origen = new Scanner (fichero);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		origen.useDelimiter("[;\\r]");
		origen.nextLine();  
		while (origen.hasNext()){
			int añoI = origen.nextInt();
			int añoF = origen.nextInt();
			
			String primero = adecuar(origen.next());
			String segundo = adecuar(origen.next());
			String tercero = adecuar(origen.next());
			origen.nextLine();
			
			String division= "2";
			
			//System.out.println(añoI+", "+primero+", "+segundo+", "+tercero);
			oracle.executeSentence("INSERT INTO Clasifica (nombreClub, temporada, division, posicion) "+
					"VALUES ('"+primero+"',"+añoI+",'"+division+"',"+1+")");
			oracle.executeSentence("INSERT INTO Clasifica (nombreClub, temporada, division, posicion) "+
					"VALUES ('"+segundo+"',"+añoI+",'"+division+"',"+2+")");
			oracle.executeSentence("INSERT INTO Clasifica (nombreClub, temporada, division, posicion) "+
					"VALUES ('"+tercero+"',"+añoI+",'"+division+"',"+3+")");
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
