-- Insert a Route as well as its categories returning the inserted route identifier
WITH InsertedRoute AS (
	INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) 
	VALUES ('lx', 'nm', 'dc', 5, CURRENT_DATE, to_json('[]'::text), 100)
	RETURNING Identifier AS route_id
)
INSERT INTO RouteCategory (RouteIdentifier, CategoryName) 
VALUES (
	(SELECT route_id FROM InsertedRoute), 
	UNNEST(ARRAY['Sea', 'Sports']::text[])
) RETURNING (SELECT route_id FROM InsertedRoute);

-- Update a Route as well as its categories
INSERT INTO RouteCategory (RouteIdentifier, CategoryName) VALUES (:routeIdentifier, unnest(:categories));

UPDATE Route SET (Location, Name, Description, Rating, Duration, Points) = (:location, :name, :description, :rating, :duration, to_json(:points));

WITH UpdatedRoute AS (
	UPDATE Route SET (Location, Name, Description, Rating, Duration, Points) = ('lx', 'nm', 'dc', 0, 0, to_json('[]'::text))
	WHERE Identifier = 1
	RETURNING Identifier AS route_id
)
DELETE FROM RouteCategory WHERE RouteIdentifier = 1; 
INSERT INTO RouteCategory (RouteIdentifier, CategoryName) 
VALUES (
	1, 
	UNNEST(ARRAY['Sea', 'Sports']::text[])
) RETURNING 1;

-- SELECT WITH PAGINATION
SELECT *,Count(*) OVER() AS full_count FROM Route WHERE Location = 'Lisbon' ORDER BY Rating DESC LIMIT 5 OFFSET 0;

SELECT * FROM ROUTE JOIN RouteCategory ON RouteCategory.RouteIdentifier = Route.Identifier
	
SELECT Route.Identifier, 
	array_agg(RouteCategory.CategoryName) AS categories
	FROM ROUTE 	
	JOIN RouteCategory ON RouteCategory.RouteIdentifier = Route.Identifier 
	WHERE Route.Identifier= 5
	GROUP BY Route.Identifier

INSERT INTO Person (sub)
VALUES
 (
 'Microsoft',
 'hotline@microsoft.com'
 ) 
ON CONFLICT ON CONSTRAINT customers_name_key 
DO NOTHING;
	
	
SELECT PersonIdentifier, HashedToken
FROM GoogleAuthentication
WHERE Subject = ''

SELECT * FROM Person;
SELECT * FROM Authentication;
SELECT * FROM Route;
SELECT * FROM Category;
SELECT * FROM RouteCategory;

