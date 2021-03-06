CREATE TABLE Person(
	-- Idtoken text NOT NULL PRIMARY KEY,
	-- Identifier text NOT NULL, hash do email
	Name text PRIMARY KEY
);

CREATE TABLE Route(
	Identifier bigint PRIMARY KEY,
	--Location text,
	--Name text, 
	--Description text,
	--Classification real,
	--Duration bigint, -- minutes
	--DateCreated date,
	--Points json,
	PersonIdentifier text REFERENCES Person(Name) -- change name to email
);

CREATE TABLE Category(
	Name text PRIMARY KEY
);

CREATE TABLE RouteCategory(
	CategoryName text REFERENCES Category(Name),
	RouteIdentifier bigint REFERENCES Route(Identifier),
	PRIMARY KEY (CategoryName, RouteIdentifier)
);

SELECT Name FROM Person;
SELECT Identifier, PersonIdentifier FROM Route;
SELECT Name FROM Category;
SELECT RouteIdentifier, CategoryName FROM RouteCategory;

DROP TABLE RouteCategory;
DROP TABLE Category;
DROP TABLE Route ;
DROP TABLE Person;