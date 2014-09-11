-- Esta consulta devuelve el modelo de avion que menos tiempo ha permanecido
-- en el aire (wheelsOn - wheelsOff) con salida desde un aeropuerto
-- perteneciente al estado con mayor número de aeropuertos
---------------------------------------------------------

create view tiempoVuelo as 
select v.tiempo, v.desvioNum, m.model, aer.state, v.id
	FROM vuelo v
		inner join esAvion e on (e.desvioNum = v.desvioNum) and (e.id = v.id)  
		inner join avion a on (a.tailNum = e.tailNum)
		inner join esModelo em on (em.tailNum = a.tailNum)
		inner join modelo m on (m.model = em.model)
		inner join destino d on (d.id = v.id)
		inner join aeropuertoDeEEUU aer on (aer.state = (select state from (select count(*) as cuenta, state 
										from aeropuertodeEEUU 
										group by state) 
										where cuenta = (select MAX (cuenta) 
											from (select count(*) as cuenta, state from aeropuertodeEEUU group by state))) and aer.IATA = d.IATA)
		where v.id not in (select id from vuelo where desvioNum > 0 );
select tiempo, id, state, model  
from tiempoVuelo 
where tiempo = (select MIN(tiempo)
	 from tiempoVuelo );
drop view tiempoVuelo;	

