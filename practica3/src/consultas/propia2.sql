create view orig as select a.airport, v.airtime, v.id 
from aeropuertodeeeuu a 
	inner join origen o on o.iata = a.iata 
	inner join viaje v on v.id = o.id 
		where a.city = 'New York'
		and v.airtime > 0;

create view flight as select v.id,v.airtime,f.manufacturer 
from orig v
	inner join esavion ea on (ea.id = v.id)
	inner join creado c on (c.tailnum = ea.tailnum)
	inner join fabricante f on (c.id = f.id)
	inner join (select distinct id from vuelo
	where desvionum > 0) d on (d.id <> v.id)
		where f.manufacturer = 'AIRBUS';

select round(suma/numtot , 0) as media from (select manufacturer,sum(airtime) as suma, count(airtime) as numtot 
	from flight
	group by manufacturer) ;

drop view orig;
drop view flight;

