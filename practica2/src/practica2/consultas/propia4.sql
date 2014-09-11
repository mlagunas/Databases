create view masPelisQueSeries as
select p.actor, p.totalPelis, s.totalSeries
from ( select p.id_personal as actor, count(*) as totalPelis
	from poseer p
	inner join ( select t.id_titulo
		from titulo t
		where t.tipo = 'movie'
		and annoProduccion BETWEEN 1990 AND 2000 ) m
	on m.id_titulo = p.id_titulo
	where p.rol='actor' or p.rol='actress'
	group by p.id_personal) p
inner join ( select p.id_personal as actor, count(*) as totalSeries
	from poseer p
	inner join ( select t.id_titulo
		from titulo t
		where t.tipo = 'episode' or t.tipo='tv series'
		and annoProduccion BETWEEN 1990 AND 2000 ) m
	on m.id_titulo = p.id_titulo
	where p.rol='actor' or p.rol='actress'
	group by p.id_personal ) s on p.actor = s.actor
where p.totalPelis > s.totalSeries;

select p.nombrePersona,n2.nacionalidad, ms.totalPelis, ms.totalSeries  
from personal p
inner join masPelisQueSeries ms on p.id_personal=ms.actor
inner join (select id_personal,substr(lugarnacimiento,instr(lugarnacimiento,',',-1)+1) as nacionalidad 
			from personal 
			where lugarnacimiento is not null) n2 on n2.id_personal=p.id_personal
where n2.nacionalidad = 'Spain' 

drop view masPelisQueSeries





