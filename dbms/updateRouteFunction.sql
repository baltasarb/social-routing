CREATE OR REPLACE FUNCTION updateRouteAndRouteCategories(
	identifier int,
	loc text,
	nam text, 
	des text,
	rat double precision, 
	dur int,
	poi json,
	VARIADIC cat text[]
) RETURNS void AS $$
DECLARE 
	i integer := 0;
	categoriesLength integer := cardinality(cat);
	initialCreatedDate date;
BEGIN
	
	UPDATE Route SET (Location, Name, Description, Rating, Duration, Points) = (loc, nam, des, rat, dur, to_json(poi));

	DELETE FROM RouteCategory WHERE RouteIdentifier = identifier;
	
	-- arrays start at 1
	FOR i IN 1..categoriesLength LOOP
	  INSERT INTO RouteCategory (CategoryName, RouteIdentifier) VALUES (cat[i], identifier);
	END LOOP;
END;
$$ LANGUAGE plpgsql;
																																					 
