-- El equipo local no puede jugar como visitante en un mismo partido
CREATE OR REPLACE trigger LocalDistintoDeVisitante
BEFORE INSERT OR UPDATE ON Local
FOR EACH ROW
DECLARE
	total NUMBER(2);
BEGIN 
	SELECT COUNT(*) INTO total FROM visitante v where v.id=:NEW.id and v.nombre=:NEW.nombre; 
	IF (total > 0) THEN
		RAISE_APPLICATION_ERROR (-20000, 'El equipo ' || :NEW.nombre || ', no puede jugar como local y visitante para el partido ' || :NEW.id);
	END IF;
END;

CREATE OR REPLACE trigger VisitanteDistintoDeLocal
BEFORE INSERT OR UPDATE ON Visitante
FOR EACH ROW
DELCARE
	total NUMBER(2);
BEGIN 
	SELECT COUNT(*) INTO total FROM Local l where l.id=:NEW.id and l.nombre=:NEW.nombre; 
	IF (total > 0) THEN
		RAISE_APPLICATION_ERROR (-20000, 'El equipo ' || :NEW.nombre || ', no puede jugar como local y visitante para el partido ' || :NEW.id);
	END IF;
END;