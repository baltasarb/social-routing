INSERT INTO Person (Name, Email) 
VALUES('Baltasar', 'baltasar@gmail.com'),
	  ('Bernardo', 'bernardo@gmail.com');

INSERT INTO Route (Location, Name, Description, Classification, Duration, DateCreated, Points, PersonIdentifier)
VALUES ('Lisbon', 'Route 1', 'Description 1', 0, 10, now(), '{}', 3),
	   ('Lisbon', 'Route 2', 'Description 2', 2.5, 20, now(), '{}', 4),
	   ('Lisbon', 'Route 3', 'Description 3', 3, 30, now(), '{}', 3),
	   ('Lisbon', 'Route 4', 'Description 4', 4.5, 40, now(), '{}', 4);

/*
INSERT INTO Category (Name) VALUES ('Sea');
INSERT INTO Category (Name) VALUES ('Sports');
INSERT INTO Category (Name) VALUES ('Nature');

INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sea', 1);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sports', 1);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 1);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 2);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 4);
*/

SELECT * FROM Person;
SELECT * FROM Route;
--SELECT * FROM Category;
--SELECT * FROM RouteCategory;




