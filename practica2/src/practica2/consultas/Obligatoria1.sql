create procedure consulta1 as

create view totalPersonal as 
select t.id_titulo, t.AnnosEmision,p.id_personal, p.annonacimiento,
		t.AnnosEmision - p.annonacimiento as annos 
from titulo  t 
inner join poseer pos on t.id_titulo = pos.id_titulo 
inner join personal p on pos.id_personal = p.id_personal 
where t.annosemision is not null 
		and (pos.rol = 'actor' or pos.rol = 'actress')
		and p.annonacimiento is not null 
		and t.tipo = 'tv series';

create view finaldata as 
select tp.id_titulo, sum(tp.annos) as totalannos, count(tp.id_personal) as numActores 
from totalPersonal tp 
group by tp.id_titulo;

create view mediaEdad as 
select fd.id_titulo, round(fd.totalannos / fd.numActores , 0) as media 
from finaldata fd;

select t.nombreTitulo 
from titulo t 
inner join mediaEdad me on me.id_titulo = t.id_titulo 
where me.media = 25;

drop view mediaEdad;
drop view finaldata;
drop view totalPersonal;