create view secuelas as 
	select es1.id_pelicula, t.annoproduccion, es1.tipo, es1.id_pelicula_enlazada 
	from esSecuela es1 
	inner join (select id_pelicula, count(*) 
				from esSecuela 
				where tipo = 'followed by' 
				group by id_pelicula 
				having count(*) > 2) es2 
		on es1.id_pelicula = es2.id_pelicula 
	inner join titulo t 
		on t.id_titulo = es1.id_pelicula_enlazada 
	where t.tipo = 'movie' and t.annoproduccion > 1974

create view secuelasNoRepetidas as 
	select sec.id_pelicula, sec.tipo, sec.id_pelicula_enlazada, sec.annoproduccion 
	from secuelas sec 
	right join (select distinct(id_pelicula) 
				from secuelas 
				where id_pelicula not in (select distinct(id_pelicula) 
											from secuelas where tipo='follows')) sec2 
		on sec.id_pelicula = sec2.id_pelicula


create view datafinal as 
	select t.nombretitulo, t.annoproduccion, t.id_titulo, t.id_titulo as origen, p.total 
	from titulo t 
	inner join (select id_pelicula, count(*) as total 
				from secuelasnorepetidas sec 
				group by id_pelicula) p
		on p.id_pelicula = t.id_titulo 
	union 
	select t.nombretitulo, t.annoproduccion, t.id_titulo, sec.id_pelicula as origen, p.total 
	from titulo t 
	inner join secuelasnorepetidas sec 
		on sec.id_pelicula_enlazada = t.id_titulo 
	inner join (select id_pelicula, count(*) as total 
				from secuelasnorepetidas sec 
				group by id_pelicula) p 
		on p.id_pelicula = sec.id_pelicula 
	order by total desc, origen, annoproduccion


select df.nombretitulo, df.annoproduccion, t.nombreTitulo as PeliculaOrigenDeLaSaga 
from datafinal df 
inner join titulo t 
on t.id_titulo = df.origen 
	
	
drop view secuelas;
drop view secuelasNoRepetidas;
drop view datafinal;