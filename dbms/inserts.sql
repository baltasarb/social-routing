-- tables being inserted with ids for testing

INSERT INTO Person (Identifier, Name, Email) 
VALUES('100', 'Baltasar', 'baltasar@gmail.com'),
	  ('200', 'Bernardo', 'bernardo@gmail.com');

INSERT INTO Route (Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier)
VALUES ('100', 'Lisbon', 'Route 1', 'Description 1', 0, 10, now(), '[]', 100),
	   ('200', 'Lisbon', 'Route 2', 'Description 2', 2.5, 20, now(), '[]', 200),
	   ('300', 'Lisbon', 'Route 3', 'Description 3', 3, 30, now(), '[]', 100),
	   ('400', 'Lisbon', 'Route 4', 'Description 4', 4.5, 40, now(), '[]', 200);

INSERT INTO Category (Name) VALUES ('Sea');
INSERT INTO Category (Name) VALUES ('Sports');
INSERT INTO Category (Name) VALUES ('Nature');

INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sea', 100);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sports', 100);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 100);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 200);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sports', 200);

SELECT * FROM Category;
SELECT * FROM RouteCategory;
SELECT * FROM Person;
SELECT * FROM Route;

UPDATE Person SET (Name, Email) = ('ze', 'm') WHERE identifier = 6;



