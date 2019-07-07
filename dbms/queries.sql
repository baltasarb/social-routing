SELECT * FROM Person;
SELECT * FROM Authentication;
SELECT * FROM Route;
SELECT * FROM Category;
SELECT * FROM RouteCategory;
SELECT * FROM GoogleAuthentication;
SELECT * FROM PointOfInterest;
SELECT * FROM RoutePointOfInterest;

CREATE EXTENSION postgis;

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



SELECT  (
	SELECT COUNT(*) FROM Route WHERE PersonIdentifier = 100
) as count, Identifier, Name, Rating, PersonIdentifier 
FROM Route 
WHERE PersonIdentifier = 100
ORDER BY Rating DESC 
LIMIT 3
OFFSET 0;

SELECT Count(*) as count, Identifier, Name, Rating, PersonIdentifier 
	FROM Route 
	WHERE PersonIdentifier = 100 
GROUP BY(Route.Identifier)
ORDER BY Rating DESC 
LIMIT 3
OFFSET 0;

-- search by location
SELECT COUNT(*) OVER() as count, Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier
FROM Route
JOIN RouteCategory
ON RouteCategory.RouteIdentifier = Route.Identifier
WHERE Location = 'location 1'
AND (RouteCategory.CategoryName = 'Sea' OR RouteCategory.CategoryName = 'Sports')
AND Route.Duration = 0
GROUP BY Route.identifier
ORDER BY Rating DESC
LIMIT 3
OFFSET 0;
	
-- 38.73528903 lat
-- -9.15510356 lon
	
-- 38.73582778
-- -9.15783674

																			
select ST_Distance(ST_GeographyFromText('Point(-9.15510356 38.73528903)'), ST_GeographyFromText('Point(-9.15783674 38.73582778)'));	
																		
SELECT Route.Identifier, Route.LocationIdentifier, Route.Name, Route.Description, Route.Rating, Route.Duration, 
Route.DateCreated, Route.Points, Route.PersonIdentifier, Route.Elevation, RouteCategory.CategoryName
FROM Route
JOIN RouteCategory ON RouteCategory.RouteIdentifier = Route.Identifier
JOIN (
	SELECT PointOfInterest.Identifier, PointOfInterest.Latitude, PointOfInterest.Longitude FROM PointOfInterest 
	WHERE PointOfInterest.Identifier IN ( 
		SELECT RoutePointOfInterest.PointOfInterestIdentifier
		FROM RoutePointOfInterest 																								
		WHERE RoutePointOfInterest.RouteIdentifier = 4
	)
) ON PointOfRoute.Identifier = 4
WHERE Route.Identifier = 4
GROUP BY Route.Identifier, RouteCategory.CategoryName
																								
SELECT PointOfInterest.Identifier, PointOfInterest.Latitude, PointOfInterest.Longitude FROM PointOfInterest 
WHERE PointOfInterest.Identifier IN ( 
	SELECT RoutePointOfInterest.PointOfInterestIdentifier
	FROM RoutePointOfInterest 																								
	WHERE RoutePointOfInterest.RouteIdentifier = 4
) 
																						

-- SELECT FULL ROUTE
SELECT Route.Identifier, Route.LocationIdentifier, Route.Name, Route.Description, Route.Rating, Route.Duration, 
Route.DateCreated, Route.Points, Route.PersonIdentifier, Route.Elevation, Route.Circular, Route.Ordered, Route.ImageReference, RouteCategory.CategoryName, PointOfInterest.Identifier as PointOfInterestIdentifier, 
PointOfInterest.Latitude, PointOfInterest.Longitude
FROM Route
JOIN RouteCategory ON RouteCategory.RouteIdentifier = Route.Identifier
JOIN RoutePointOfInterest ON RoutePointOfInterest.RouteIdentifier = Route.Identifier
JOIN PointOfInterest ON PointOfInterest.Identifier = RoutePointOfInterest.PointOfInterestIdentifier
WHERE Route.Identifier = 4
GROUP BY Route.Identifier, RouteCategory.CategoryName, PointOfInterest.Identifier;
																								
-- search by coordinates
SELECT DISTINCT ON(Identifier) * 
FROM (
	SELECT COUNT(*) as Count,  RouteCategory.RouteIdentifier as Identifier, Route.Name, Route.Rating, Route.PersonIdentifier, Route.Points, Route.Circular, Route.Ordered FROM RouteCategory 
	JOIN Route ON RouteCategory.RouteIdentifier = Route.Identifier
	WHERE (CategoryName = 'Sea' OR CategoryName = 'Sports')	AND Route.Duration = 'Short'
	GROUP BY RouteCategory.RouteIdentifier, Route.Name, Route.Rating, Route.PersonIdentifier, Route.Points, Route.Circular, Route.Ordered
) results
WHERE routeBelongsToUserArea(0,99999999, 0,0, results.points, results.circular, results.ordered) = TRUE
LIMIT 5
OFFSET 0
																								
																								
SELECT * FROM (SELECT DISTINCT COUNT(*) as Count,  RouteCategory.RouteIdentifier, Route.Name, Route.Rating, Route.PersonIdentifier FROM RouteCategory 
JOIN Route ON RouteCategory.RouteIdentifier = Route.Identifier
WHERE (CategoryName = 'Sea' OR CategoryName = 'Sports')	AND Route.Duration = 'Short'
GROUP BY RouteCategory.RouteIdentifier, Route.Name, Route.Rating, Route.PersonIdentifier) results
WHERE results.RouteIdentifier > 1

																								
																								
ORDER BY fun(lat real, long real, Route.Points)
				
																								

																							 
																							 
																							 
																							 