create view series as select t.id_titulo, t.tipo, pos.rol, p.id_personal, p.nombrepersonal from titulo t inner join poseer pos on pos.id_titulo = t.id_titulo inner join personal p on p.id_personal = pos.id_personal where rol='actor' and tipo='episode'

create view total as select id_personal,count(*) as total from series group by id_personal

create view finalM as select t.total, p.nombrepersonal,t.id_personal from total t inner join personal p on p.id_personal = t.id_personal where total=(select max(total) from total)

