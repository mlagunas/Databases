--Calcula el numero de goles totales marcados por cada club en toda la competición
	--Primero creamos la columna necesaria
ALTER TABLE Club
ADD TotalGoles NUMBER(5) NOT NULL DEFAULT 0;

CREATE OR REPLACE TRIGGER TotalGolesLocal
AFTER INSERT OR UPDATE ON Local
FOR EACH ROW
DECLARE
	Goles NUMBER(2);
	GolesAcumulados NUMBER(5);
BEGIN 
	SELECT GolLocal INTO Goles FROM partido p WHERE p.id=:NEW.id;
	SELECT TotalGoles INTO GolesAcumulados FROM Club c WHERE c.nombre=:NEW.nombre; 
	GolesAcumulados := GolesAcumulados + Goles;
	UPDATE Club SET TotalGoles = GolesAcumulados WHERE nombre=:NEW.nombre;
END;

CREATE OR REPLACE TRIGGER TotalGolesVisitante
AFTER INSERT OR UPDATE ON Visitante
FOR EACH ROW
DECLARE
	Goles NUMBER(2);
	GolesAcumulados NUMBER(5);
BEGIN 
	SELECT GolLocal INTO Goles FROM partido p WHERE p.id=:NEW.id;
	SELECT TotalGoles INTO GolesAcumulados FROM Club c WHERE c.nombre=:NEW.nombre; 
	GolesAcumulados := GolesAcumulados + Goles;
	UPDATE Club SET TotalGoles = GolesAcumulados WHERE nombre=:NEW.nombre;
END;


--Calculo de porcentaje de victorias o empates en cada estadio por el equipo local
	--Primero añadimos la columna correspondiente
ALTER TABLE Estadio
ADD PorcentajeVictoriaOEmpate DECIMAL(4,4) NOT NULL DEFAULT 0;

	--Problema con tabla mutante solucionado
CREATE OR REPLACE TRIGGER CalculoProcentajes
AFTER INSERT OR UPDATE ON seJuegaEn
FOR EACH ROW
BEGIN
	INSERT INTO tmpSeJuegaEn VALUES (:NEW.id, :NEW.nombre);
END;

CREATE OR REPLACE TRIGGER CalculoPorcentajesSentencia
AFTER INSERT OR UPDATE ON seJuegaEn
DECLARE
	nombreE Estadio.nombre%TYPE;
	id Partido.id%TYPE;
	NoPerdidos NUMBER(5);
	Totales NUMBER(5);
	CURSOR c IS SELECT id, nombre FROM tmpSeJuegaEn;
BEGIN 
	OPEN c;
	LOOP
	FETCH c INTO id, nombreE;
	EXIT WHEN c%NOTFOUND;
		SELECT COUNT(*) INTO NoPerdidos 
		FROM Partido p 
		WHERE (p.GolLocal >= p.GolVisitante) 
			and p.id IN (SELECT id 
						FROM seJuegaEn s
						WHERE s.nombre=nombreE);
						
		SELECT COUNT(*) INTO Totales 
		FROM seJuegaEn p 
		WHERE p.id IN (SELECT id 
						FROM seJuegaEn s
						WHERE s.nombre=nombreE);
	
	UPDATE Estadio SET PorcentajeVictoriaOEmpate = (NoPerdidos/Totales) WHERE nombre=nombreE;
	
	END LOOP;
	CLOSE c;
	DELETE FROM tmpSeJuegaEn;
END;





