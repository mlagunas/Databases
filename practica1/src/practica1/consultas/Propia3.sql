create view EstadioSegundaJ as
select s.nombreEstadio, count(*) as numero
from SeJuegaEn s
inner join (select id,jornada
		from Partido
		where jornada='2') p
	on p.id=s.id
inner join pertenece per on per.id=s.id
where per.division='1' 
	and per.temporada BETWEEN 1970 and 1979
group by nombreEstadio

select nombreEstadio, numero
from EstadioSegundaJ
where numero = (select(max(numero) from EstadioSegundaJ)

------------------------------------------------------
select nombreEstadio, numero from
(select s.nombreEstadio, count(*) as numero
from SeJuegaEn s
inner join (select id,jornada
		from Partido
		where jornada='2') p
	on p.id=s.id
inner join pertenece per on per.id=s.id
where per.division='1' 
	and per.temporada BETWEEN 1970 and 1979
group by nombreEstadio) t
where numero = (select max(numero) from 
(select s.nombreEstadio, count(*) as numero
from SeJuegaEn s
inner join (select id,jornada
		from Partido
		where jornada='2') p
	on p.id=s.id
inner join pertenece per on per.id=s.id
where per.division='1' 
	and per.temporada BETWEEN 1970 and 1979
group by nombreEstadio));
