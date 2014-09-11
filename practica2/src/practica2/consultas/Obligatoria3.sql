create view peliculasJuntos as 
select a.actor,b.actriz, count(*) as peliculasJuntos
from ( select p.id_titulo, p.id_personal as actor 
		from poseer p
		inner join ( select id_titulo 
			from titulo
			where annoproduccion >= 1980 and annoproduccion <=2000) t
		on t.id_titulo = p.id_titulo
		where p.rol = 'actor') a
inner join ( select p.id_titulo, p.id_personal as actriz
				from poseer p
				inner join ( select id_titulo 
					from titulo
					where annoproduccion >= 1980 and annoproduccion <=2000) t
				on t.id_titulo = p.id_titulo
				where p.rol = 'actress') b
ON b.id_titulo = a.id_titulo
group by a.actor,b.actriz;

create view peliculasTotalesyJuntos as
select pj.actor, pj.actriz, pj.peliculasJuntos,pf.peliculasActriz as totalActriz, pm.peliculasActor as totalActor 
from peliculasJuntos pj
inner join (
			select p.id_personal, count(*) as peliculasActor 
			from poseer p 
			where p.rol = 'actor' 
			group by p.id_personal) pm
on pm.id_personal=pj.actor
inner join(
			select p.id_personal, count(*) as peliculasActriz 
			from poseer p d
			where p.rol = 'actress' 
			group by p.id_personal) pf
on pf.id_personal=pj.actriz;

create view datafinal as
select per.nombrePersonal as Actor,n.nacionalidad as nacionalidadActor, per2.nombrePersonal as Actriz, n2.nacionalidad as nacionalidadActriz, p.peliculasJuntos
from peliculasTotalesyJuntos p
inner join personal per on per.id_personal=p.actor
inner join personal per2 on per2.id_personal=p.actriz
inner join (select id_personal,substr(lugarnacimiento,instr(lugarnacimiento,',',-1)+1) as nacionalidad 
			from personal 
			where lugarnacimiento is not null) n on n.id_personal=p.actor
inner join (select id_personal,substr(lugarnacimiento,instr(lugarnacimiento,',',-1)+1) as nacionalidad 
			from personal 
			where lugarnacimiento is not null) n2 on n2.id_personal=p.actriz
where p.totalActor=p.totalActriz and p.totalActriz=p.peliculasJuntos; 

select actor, nacionalidadActor, actriz, nacionalidadActriz, peliculasJuntos
from datafinal
where nacionalidadActor <> nacionalidadActriz
order by peliculasJuntos desc;

drop view datafinal;
drop view peliculasTotalesyJuntos;
drop view peliculasJuntos;




