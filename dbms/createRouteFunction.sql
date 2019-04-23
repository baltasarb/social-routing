CREATE OR REPLACE FUNCTION insertRouteAndRouteCategories(
	location text,
	name text, 
	description text,
	duration int, 
	points json,
	personIdentifier int,
	VARIADIC categories text[],
	createdRouteId OUT int 
) AS $$
DECLARE 
	i integer := 0;
	categoriesLength integer := cardinality(categories);
BEGIN
	INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) 
		VALUES (location, name, description, duration, CURRENT_DATE, to_json(points), personIdentifier)
		RETURNING identifier INTO createdRouteId;
		
	-- arrays start at 1
	FOR i IN 1..categoriesLength LOOP
	  INSERT INTO RouteCategory (CategoryName, RouteIdentifier) VALUES (categories[i], createdRouteId);
	END LOOP;
END;
$$ LANGUAGE plpgsql;

--drop function insertRouteAndRouteCategories;
















