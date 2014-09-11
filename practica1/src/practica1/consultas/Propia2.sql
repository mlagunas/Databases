SELECT NombreClub, temporada
FROM (SELECT DISTINCT(l.NombreClub), g.temporada
	FROM (SELECT NombreClub, Temporada
		FROM Clasifica
		WHERE (posicion = '1' OR posicion = '2' OR posicion = '3') AND division = '2') g
	INNER JOIN Pertenece p ON p.temporada=(g.temporada-1)
	INNER JOIN  local l ON l.id = p.id AND l.nombreClub = g.nombreClub
	WHERE p.division =  '1')
ORDER by Temporada
