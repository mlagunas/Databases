/*
 * 1. ENTENDIDO TRAYECTO COMO UNA CONEXIÓN ENTRE UN ORIGEN Y UN DESTINO PERO
 * SIN CONTAR ESTA CONEXIÓN MÁS DE UNA VEZ.
 */
-- LISTADO DE TODAS LAS COMPAÑÍAS ORIGEN DESTINO
CREATE VIEW ViajesCompania AS
SELECT carrier, origen, destino, count(*) as total FROM (
	SELECT v.carrier, o.iata as origen, d.iata as destino
	FROM viaje v
	INNER JOIN origen o ON o.id=v.id
	INNER JOIN destino d ON d.id=v.id)
GROUP BY carrier, origen, destino;


CREATE VIEW CoincidenciasRepetidas AS
SELECT v1.carrier as carrier1, v1.origen, v1.destino, v2.carrier as carrier2
FROM ViajesCompania v1
INNER JOIN ViajesCompania v2 ON (v2.origen = v1.origen AND v2.destino = v1. destino AND v1.carrier <> v2.carrier);

--SELECCIONAMOS LAS COINCIDENCIAS ENTRE COMPAÑIAS Y LAS DIVIDIMOS POR EL TOTAL DE TRAYECTOS DE CADA UNA
SELECT carrier1, carrier2, coincidencia 
FROM(	SELECT carrier1, carrier2, (coincidencias / total) AS coincidencia 
		FROM (	SELECT c.carrier1, c.carrier2, c.coincidencias, t.total 
				FROM(	SELECT carrier1, carrier2, count(*) as coincidencias
						FROM CoincidenciasRepetidas 
						GROUP BY carrier1, carrier2) c
				INNER JOIN (	SELECT carrier,count(*) as total 
								FROM ViajesCompania 
								GROUP BY carrier) t ON t.carrier=c.carrier1))
WHERE coincidencia >=0.3
ORDER BY coincidencia DESC;

DROP VIEW CoincidenciasRepetidas;
DROP VIEW ViajesCompania;