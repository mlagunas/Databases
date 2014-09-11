SELECT NombreClub, temporada, annodesaparacion 
FROM (SELECT g.NombreClub, g.temporada, c.annodesaparacion 
	FROM (SELECT DISTINCT(Nombreclub), Temporada
		FROM Clasifica
		WHERE posicion = '1' AND division = '2') g
	INNER JOIN Club c ON c.NombreClub=g.NombreClub
	WHERE annodesaparacion IS NOT NULL)
WHERE (annodesaparacion - temporada < 5) AND (annodesaparacion - temporada > -1)