-- tables being inserted with ids for testing
INSERT INTO Person (Identifier, Rating) 
VALUES('100', 1.0),
	  ('200', 2.0);

INSERT INTO Authentication(CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier)
VALUES(1, 1, 'token 1', 'refresh token 1', 100),
	  (2, 2, 'token 2', 'refresh token 2', 200);

INSERT INTO GoogleAuthentication(PersonIdentifier, Subject)
VALUES('100', 'subject1'),
	  ('200', 'subject2');
		
INSERT INTO Category (Name) VALUES ('Sea');
INSERT INTO Category (Name) VALUES ('Sports');
INSERT INTO Category (Name) VALUES ('Nature');
INSERT INTO Category (Name) VALUES ('City');
INSERT INTO Category (Name) VALUES ('Other');
--INSERT INTO Route (Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier)
--VALUES ('100', 'Lisbon', 'Route 1', 'Description 1', 0, 10, now(), '[]', 100),
--	   ('200', 'Lisbon', 'Route 2', 'Description 2', 2.5, 20, now(), '[]', 200),
--	   ('300', 'Lisbon', 'Route 3', 'Description 3', 3, 30, now(), '[]', 100),
--	   ('400', 'Lisbon', 'Route 4', 'Description 4', 4.5, 40, now(), '[]', 200);



--INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sea', 2);
--INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sports', 2);
--INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 1);

SELECT * FROM Category;
SELECT * FROM RouteCategory;
SELECT * FROM Person;
SELECT * FROM Route;
SELECT * FROM GoogleAuthentication;
SELECT * FROM Authentication;

SELECT Route.Identifier, Route.Location, Route.Name, Route.Description, Route.Rating, Route.Duration, Route.DateCreated, Route.Points, Route.PersonIdentifier, RouteCategory.CategoryName 
FROM Route 
JOIN RouteCategory ON Route.Identifier = RouteCategory.RouteIdentifier
WHERE Route.Identifier = 1


--UPDATE Person SET (Name, Email) = ('ze', 'm') WHERE identifier = 6;

SELECT CategoryName FROM RouteCategory WHERE RouteIdentifier = 1

