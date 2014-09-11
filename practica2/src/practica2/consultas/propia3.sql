select nombreTitulo, count(*) as cuenta 
from ( select distinct p.id_personal, ti.nombreTitulo, t.id_serie 
	from episodiode t
		inner join poseer pos on pos.id_titulo = t.id_episodio 
		inner join personal p on p.id_personal = pos.id_personal 
		inner join (select id_serie from episodiode where numtemporada > 3) ed on ed.id_serie = t.id_serie
		inner join titulo ti on ti.id_titulo = t.id_serie)
group by nombreTitulo;
