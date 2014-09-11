CREATE OR REPLACE TRIGGER ConsistenciaAnnosExistencia
AFETER INSERT OR UPDATE ON Personal
FOR EACH ROW
BEGIN
IF NEW.AnnoNacimiento > NEW.AnnoMuerte THEN
 RAISE_APPLICATION_ERROR (-20000, 'El año de nacimiento no puede ser superior al año de fallecimiento.');
END IF;
END;






CREATE OR REPLACE TRIGGER ConsistenciaCapitulos
AFTER INSERT OR UPDATE ON EpisodioDe
FOR EACH ROW
DECLARE
 numEpisodio tpNatural, numTemporada tpNatural, serie tpNatural;
 CURSOR cursorCapitulos IS SELECT id_serie, numEpisodio, numTemporada FROM EpisodioDe;
BEGIN
OPEN cursorCapitulos;
 LOOP
 FETCH cursorCapitulos INTO serie, numEpisodio, numTemporada;
 EXIT WHEN cursorCapitulos%NOTFOUND;
  IF (NEW.numEpisodio = numEpisodio) AND (NEW.id_serie = serie) AND (NEW.numTemporada = numTemporada) THEN
   RAISE_APPLICATION_ERROR (-20000, 'Numero de episodio y temporada ya utilizadas para la serie con id= ' || serie);
  END IF;
 END LOOP;
CLOSE cursorCapitulos;
END;
