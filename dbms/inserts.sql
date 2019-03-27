INSERT INTO Person (Name) VALUES('Baltasar');
INSERT INTO Person (Name) VALUES('Bernardo');

INSERT INTO Route (Identifier, PersonIdentifier) VALUES(1, 'Baltasar');
INSERT INTO Route (Identifier, PersonIdentifier) VALUES(2, 'Baltasar');
INSERT INTO Route (Identifier, PersonIdentifier) VALUES(3, 'Baltasar');
INSERT INTO Route (Identifier, PersonIdentifier) VALUES(4, 'Bernardo');

INSERT INTO Category (Name) VALUES ('Sea');
INSERT INTO Category (Name) VALUES ('Sports');
INSERT INTO Category (Name) VALUES ('Nature');

INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sea', 1);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Sports', 1);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 1);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 2);
INSERT INTO RouteCategory(CategoryName, RouteIdentifier) VALUES ('Nature', 4);

SELECT * FROM Person;
SELECT * FROM Route;
SELECT * FROM Category;
SELECT * FROM RouteCategory;




