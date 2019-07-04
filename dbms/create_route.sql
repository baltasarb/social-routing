INSERT INTO Route (Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier)
VALUES ('100', 'Lisbon', 'Route 1', 'Description 1', 0, 10, now(), '[]', 100),

WITH InsertedRoute AS (
	INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) 
	VALUES (:location, :name, :description, :duration, CURRENT_DATE, to_json(:points), :personIdentifier)
	RETURNING Identifier AS route_id
)
INSERT INTO RouteCategory (RouteIdentifier, CategoryName) 
VALUES ((SELECT route_id FROM InsertedRoute),UNNEST(:categories)) 
RETURNING (SELECT route_id FROM InsertedRoute);
													
		
insert into PointOfInterest (Identifier, Latitude, Longitude) 
VALUES ()
insert into Image (Reference) 
VALUES ()									
insert into Route (Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier, ImageReference, Circular, Ordered) 
VALUES()
insert into RoutePointOfInterest (RouteIdentifier, PointOfInterestIdentifier) 
VALUES()
insert into RouteCategories (RouteIdentifier, CategoryName) 
VALUES()
		
													
													