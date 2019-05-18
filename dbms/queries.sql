-- INSERT a Route as well as its categories returning the inserted route id
WITH InsertedRoute AS (
	INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) 
	VALUES ('lx', 'nm', 'dc', 0, CURRENT_DATE, to_json('[]'::text), 100)
	RETURNING Identifier AS route_id
)
INSERT INTO RouteCategory (RouteIdentifier, CategoryName) 
VALUES (
	(SELECT route_id FROM InsertedRoute), 
	UNNEST(ARRAY['Sea', 'Sports']::text[])
) RETURNING (SELECT route_id FROM InsertedRoute);

	


SELECT * FROM Person;
SELECT * FROM Route;
SELECT * FROM Category;
SELECT * FROM RouteCategory;

