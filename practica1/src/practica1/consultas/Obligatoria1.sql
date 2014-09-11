SELECT Nombre, TotalGoles 
FROM Club 
WHERE TotalGoles=(	SELECT MAX(TotalGoles)
					FROM Club)