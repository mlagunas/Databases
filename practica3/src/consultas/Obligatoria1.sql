/*
 * DOS MANERAS DE ENTENDERLO
 */
--1.Media de retrasos entendida como minutos de retraso totales / numero de d�as, osea media de retraso diaria
SELECT carrier, mediaretrasos 
from (	SELECT carrier, sum(totalRetrasos)/(select count(distinct(flightdate)) as diasRegistrados
	 										from viaje) 
	 										as mediaRetrasos 
 		from (	SELECT carrier, (weatherDelay+carrierDelay+lateAircraftDelay+nasDelay+securityDelay+DivArrDelay) as totalRetrasos 
 				from viaje) 
 		group by carrier) 
where carrier in ( --Compa��as que operan al menos 1000 vuelos cada d�a
				-- Compa��as que su m�nimo de vuelos en un d�a es mayor que 1000
 				SELECT carrier 
 				from (	SELECT carrier, min(numVuelos) as minNumVuelos 
 						from (	SELECT carrier, flightdate, count(*) as numVuelos 
 								from viaje 
 								group by carrier, flightdate) 
 						group by carrier) 
 				where minNumVuelos>=1000
 				intersect
 				--Compa��as que han operado todos los d�as registrados en la base de datos
 				SELECT carrier from (SELECT carrier, count(*) as numDias 
 				from (	SELECT carrier, flightdate, count(*) as numVuelos 
						from viaje 
						group by carrier, flightdate)
						group by carrier) 
				where numDias = (select count(distinct(flightdate)) as diasRegistrados 
								from viaje)
 				)
order by mediaretrasos;



 --2.Media de retrasos entendida como vuelosretrasados/totalvuelos
create view vista1 as 
	SELECT carrier, count(*) as numVuelosConRetraso 
	from (	SELECT carrier, totalretrasos 
			from (	SELECT carrier, (weatherDelay+carrierDelay+lateAircraftDelay+nasDelay+securityDelay+DivArrDelay) as totalRetrasos 
					from viaje) 
					where totalretrasos>0) 
					group by carrier;

create view vista2 as 
	SELECT carrier, count(*) as numVuelosSinRetraso 
	from (	SELECT carrier, totalretrasos 
			from (	SELECT carrier, (weatherDelay+carrierDelay+lateAircraftDelay+nasDelay+securityDelay+DivArrDelay) as totalRetrasos 
					from viaje) 
					where totalretrasos=0) 
					group by carrier;					

select carrier,((numVuelosConRetraso)/(numVuelosConRetraso+numVuelosSinRetraso)) as media 
from (	select v1.carrier, v1.numVuelosConRetraso,v2.numVuelosSinRetraso 
		from vista1 v1 
		inner join vista2 v2 on v1.carrier=v2.carrier) 
where carrier in (	SELECT carrier 
					from (	--Compa��as que operan al menos 1000 vuelos cada d�a
							-- Compa��as que su m�nimo de vuelos en un d�a es mayor que 1000
							SELECT carrier, min(numVuelos) as minNumVuelos 
							from (	SELECT carrier, flightdate, count(*) as numVuelos 
									from viaje 
									group by carrier, flightdate) 
							group by carrier) 
					where minNumVuelos>=1000
					intersect
	 				--Compa��as que han operado todos los d�as registrados en la base de datos
	 				SELECT carrier from (SELECT carrier, count(*) as numDias 
	 				from (	SELECT carrier, flightdate, count(*) as numVuelos 
							from viaje 
							group by carrier, flightdate)
							group by carrier) 
					where numDias = (select count(distinct(flightdate)) as diasRegistrados 
									from viaje)
				)
order by media;

drop view vista1;
drop view vista2;

 							
 							
