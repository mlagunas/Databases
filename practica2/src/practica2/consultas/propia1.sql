create view prueba as 
	select t.id_titulo, p.id_personal  
	from titulo t 
	inner join poseer pos on t.id_titulo = pos.id_titulo 
	inner join personal p on pos.id_personal = p.id_personal 
	where p.annoMuerte is not null 
			and t.annoproduccion is not null
			and t.annoproduccion >= p.annomuerte
			and t.tipo = 'movie';


create view nummuertes as
select id_titulo,count(*) as total 
	from (	select id_titulo, id_personal, count(*) as papeles 
			from prueba 
			group by id_titulo, id_personal)
	group by id_titulo;

select t.nombretitulo, n.total 
	from nummuertes n 
	inner join titulo t on t.id_titulo = n.id_titulo
	where total=(select max(total) from nummuertes);

drop view nummuertes;
drop view prueba;

