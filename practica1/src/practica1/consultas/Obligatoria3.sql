SELECT nombre, (PorcentajeVictoriaOEmpate * 100 || '%') as VictoriaOEmpate
FROM Estadio 
WHERE PorcentajeVictoriaOEmpate > 0,75 
