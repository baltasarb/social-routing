-- returns the distance of point(lat, lon) to the closest point in points
CREATE OR REPLACE FUNCTION fun(latitude real, longitude real, points json, ordered boolean, circular boolean)
RETURNS real AS $$
DECLARE
	user_lat text:= CAST(latitude as text);
	user_lon text:= CAST(longitude as text);
BEGIN
	--IF circular == true
	--distancia mais curta entre a e qqr um dos outros
	--continue
	--if
	--se ordenada comparar so com primeiro
	--if
	--se desordenada com ultimo tbm
	
	select ST_Distance(ST_GeographyFromText('Point(-9.15510356 38.73528903)'), ST_GeographyFromText('Point(-9.15783674 38.73582778)'));	

END
$$ LANGUAGE plpgsql;
