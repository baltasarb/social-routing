CREATE OR REPLACE FUNCTION insertRouteAndRouteCategories(
	location text,
	name text, 
	description text,
	duration int, 
	points json,
	personIdentifier int,
	categories text[],
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

select insertRouteAndRouteCategories('loc 3','t2','d2',0,' [{"latitude": 3,"longitude": 4},{"latitude": 3,"longitude": 4}]',100,ARRAY['Sea', 'Sports']);

drop function insertRouteAndRouteCategories;

		
drop function test();

CREATE FUNCTION test()
RETURNS text as $$
DECLARE																																			 
	categories text[] := ARRAY['Sea', 'Sports'];
BEGIN
	RETURN categories[1];
END;
$$ LANGUAGE plpgsql;																																					 
				
select * from test();
																																					 
select * from person;
																																					  
select * from route;																																				

select * from RouteCategory;

















