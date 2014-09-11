-- Estadio en el que se han jugado más partidos en la última jornada
create view jornadasOK as
select p.id,temporada, division,case when length(jornada)=1 then concat ('0', jornada) else jornada end JornadaOK 
from partido p
inner join pertenece pe on pe.id= p.id;

create view maximasjornadas as
select temporada, division, max(jornadaOK) as maxJornada from (
select temporada, division, jornadaOK
from jornadasOK
group by temporada, division, jornadaOK)
group by temporada, division;


create view total as
select nombreEstadio, count(*) as total 
from (select nombreEstadio, t.id 
from (select id from jornadasOK j
inner join maximasjornadas mj on mj.temporada=j.temporada and mj.division=j.division and mj.maxjornada=j.jornadaOK) t
inner join sejuegaen sj on t.id=sj.id)
group by nombreEstadio;

select nombreEstadio, total from total
where total = (select max(total) from total );

drop view total;
drop view maximasjornadas;
drop view jornadasOK;

