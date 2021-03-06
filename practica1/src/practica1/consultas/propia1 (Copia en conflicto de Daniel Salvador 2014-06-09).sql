-- Oveja negra de nuestro Reeeeeeeeeeeeeeeeeeeeeeeeeeeaaaaaaaaaaaaaal Zaaaaaaaaaaaaragozaaaaaaaaaaaaaaaaaaaaaa

create view partlocal as 
select l.nombreClub as nombrelocal,v.nombreclub as
nombrevisitante, p.gollocal, p.golvisitante 
from partido p 
inner join visitante v on v.id =p.id 
inner join local l on l.id = p.id 
inner join club c1 on c1.nombreclub = l.nombreclub 
inner join club c2 on c2.nombreclub = v.nombreclub
	where l.nombreClub = 'Real Zaragoza'
	and p.golvisitante > p.gollocal;

create view partvisit as 
select l.nombreClub as nombrelocal,v.nombreclub as
nombrevisitante, p.gollocal, p.golvisitante 
from partido p 
inner join visitante v on v.id =p.id 
inner join local l on l.id = p.id 
inner join club c1 on c1.nombreclub = l.nombreclub 
inner join club c2 on c2.nombreclub = v.nombreclub
	where v.nombreClub = 'Real Zaragoza'
	and p.golvisitante < p.gollocal;

create view cuentaLocal as 
select nombreVisitante, count(*) as totalLocal 
from partlocal
group by nombreVisitante;

create view cuentaVisitante as 
select nombreLocal, count(*) as totalVisitante
from partvisit
group by nombreLocal;

create view info as
select v.nombreLocal, l.totallocal + v.totalvisitante as total 
from cuentaLocal l
inner join cuentaVisitante v on v.nombreLocal = l.nombrevisitante;

select nombreLocal, total 
from info 
where total=(select max(total) from info);

drop view partlocal;
drop view partvisit;
drop view cuentalocal;
drop view cuentavisitante;
drop view info;
