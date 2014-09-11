--TRIGGERS PARA AUTOINCREMENT DE LOS IDS NECESARIOS
CREATE OR REPLACE trigger id_sequence_trigger 
BEFORE INSERT ON Viaje 
FOR EACH ROW 
BEGIN 
	SELECT id_sequence.nextval INTO :new.id FROM dual;
END;

CREATE OR REPLACE trigger id_frabricante_sequence_trigger 
BEFORE INSERT ON Frabricante 
FOR EACH ROW 
BEGIN 
	SELECT id_frabricante_sequence.nextval INTO :new.id FROM dual;
END;


-- El destino no puede ser igual que el origen en un vuelo
CREATE OR REPLACE trigger origenDistintoDeDestino
BEFORE INSERT OR UPDATE ON origen
FOR EACH ROW
DECLARE
	total NUMBER(2);
BEGIN 
	SELECT COUNT(*) INTO total FROM destino d where d.IATA=:NEW.IATA and d.id=:NEW.id; 
	IF (total > 0) THEN
		RAISE_APPLICATION_ERROR (-20000, 'El origen no puede ser igual al destino(IATA:' || :NEW.IATA || ', id del Viaje:' || :NEW.id);
	END IF;
END;

CREATE OR REPLACE trigger destinoDistintoDeOrigen
BEFORE INSERT OR UPDATE ON destino
FOR EACH ROW
DELCARE
	total NUMBER(2);
BEGIN 
	SELECT COUNT(*) INTO total FROM origen  where IATA=:NEW.IATA and id=:NEW.id; 
	IF (total > 0) THEN
	   RAISE_APPLICATION_ERROR (-20000, 'El origen no puede ser igual al destino(IATA:' || :NEW.IATA || ', id del Viaje:' || :NEW.id);
	END IF;
END;