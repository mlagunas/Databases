--Calcula el numero de peliculas y series en las que ha participado una persona

ALTER TABLE Personal
ADD numPeliculas NUMBER(6) NOT NULL DEFAULT 0;


CREATE OR REPLACE TRIGGER TotalPeliculas
	AFTER INSERT OR UPDATE ON Poseer	        		
	FOR EACH ROW
	BEGIN 	
		FOR rec IN ( SELECT count(*) as num, id_personal FROM poseer p 
		inner join (
			select t.id_titulo
			from titulo t
			where t.tipo = 'movie') m
		on m.id_titulo = p.id_titulo 
		GROUP BY id_personal)
		LOOP
			UPDATE personal SET numPeliculas = (rec.num) WHERE (id_personal=rec.id_personal) ;
		END LOOP;	
	END;

ALTER TABLE Personal
ADD numSeries NUMBER(6) NOT NULL DEFAULT 0;

CREATE OR REPLACE TRIGGER TotalSeries
	AFTER INSERT OR UPDATE ON Poseer	        		
	FOR EACH ROW
	BEGIN 	
		FOR rec IN ( SELECT count(*) as num, id_personal FROM poseer p 
		inner join (
			select t.id_titulo
			from titulo t
			where t.tipo = 'episode' or t.tipo = 'tv series') m
		on m.id_titulo = p.id_titulo 
		GROUP BY id_personal)
		LOOP
			UPDATE personal SET numSeries = (rec.num) WHERE (id_personal=rec.id_personal) ;
		END LOOP;	
	END;

insert into  Personal (id_Personal, NombrePersonal, Genero) VALUES (4000000,'Manuel Lagunas','m')
insert into  Poseer (Id_Personal, Id_Titulo, Rol, Descripcion) VALUES (4000000, 1592156,'actor','')

--Calculo de porcentaje de mujeres de una producción
ALTER TABLE Titulo
ADD PorcentajeMujeres DECIMAL(4,4) DEFAULT 0 NOT NULL;

--Problema con tabla mutante solucionado
CREATE OR REPLACE TRIGGER CalculoPorcentajeMujeres
AFTER INSERT OR UPDATE ON Poseer
FOR EACH ROW
BEGIN
	INSERT INTO tmpPoseer (Id_titulo, id_personal) VALUES (:NEW.id_titulo, :NEW.id_personal);
END;

CREATE OR REPLACE TRIGGER CalculoPorcentajesSentencia
AFTER INSERT OR UPDATE ON Poseer
DECLARE
	titulo Titulo.id_titulo%TYPE;
	persona Personal.id_personal%TYPE;
	NumMujeres NUMBER(5);
	Total NUMBER(5);
	CURSOR c IS SELECT id_titulo, id_personal FROM tmpPoseer;
BEGIN 
	OPEN c;
	LOOP
	FETCH c INTO titulo, persona;
	EXIT WHEN c%NOTFOUND;
		SELECT COUNT(*) INTO NumMujeres
		FROM Personal per
		WHERE per.genero='f'
			and per.id_personal IN (SELECT id_personal
							FROM Poseer p
							WHERE p.id_titulo=titulo);		
		SELECT COUNT(*) INTO Total
		FROM poseer p 
		WHERE p.id_titulo=titulo;

		UPDATE Titulo SET PorcentajeMujeres = (NumMujeres/Total) WHERE id_titulo=titulo;

	END LOOP;
	CLOSE C;
	DELETE FROM tmpPoseer;
END;

INSERT INTO Titulo (Id_Titulo, NombreTitulo, Tipo) VALUES (3000000, 'Putas Bases de Datos', 'movie')

insert into  Personal (id_Personal, NombrePersonal, Genero) VALUES (4000001,'Pepa','f')
insert into  Poseer (Id_Personal, Id_Titulo, Rol, Descripcion) VALUES (4000000, 3000000,'actor','')
insert into  Poseer (Id_Personal, Id_Titulo, Rol, Descripcion) VALUES (4000001, 3000000,'actriz','')








