select t.id, t.desvionum, a.tailnum, m.model, f.manufacturer 
from (select desvionum, id from vuelo where desvionum=(select max (desvionum) from vuelo)) t
	left join esavion ea on (ea.desvionum = t.desvionum) and (ea.id = t.id)           
	left join avion a on (ea.tailnum = a.tailnum)
	left join esmodelo em on (em.tailnum = a .tailnum)
	left join modelo m on (em.model = m.model)
	left join creado cr on (cr.tailnum = a.tailnum)
	left join fabricante f on (cr.id = f.id);

