-- Oveja negra de nuestro Reeeeeeeeeeeeeeeeeeeeeeeeeeeaaaaaaaaaaaaaal Zaaaaaaaaaaaaragozaaaaaaaaaaaaaaaaaaaaaa

create view partlocal as 
select l.nombreClub as nombrelocal, v.nombreClub as nombreVisitante, p.golLocal, p.golVisitante 
from club c 
inner join visitante v on v.nombreClub = c.nombreClub 
inner join local l on l.nombreClub = c.nombreClub
inner join partido p on (p.id = v.id) and (p.id = l.id)
	where l.nombreClub = 'Real Zaragoza' 
	and p.golvisitante > p.gollocal; 

create view partvisit as 
select l.nombreClub as nombrelocal, v.nombreClub as nombreVisitante, p.golLocal, p.golVisitante 
from club c 
inner join visitante v on v.nombreClub = c.nombreClub 
inner join local l on l.nombreClub = c.nombreClub
inner join partido p on (p.id = v.id) and (p.id = l.id)
	where v.nombreClub = 'Real Zaragoza' 
	and p.gollocal > p.golvisitante; 

create view cuentavisitante as 
select nombreVisitante, count(*) as totalvisitante
from partlocal
group by nombreVisitante;

create view cuentalocal as 
select nombreLocal, count(*) as totallocal
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
drop view totallocal;
drop view totalvisitante;
drop view info;
