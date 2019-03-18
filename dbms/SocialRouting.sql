CREATE TABLE Person(
	FirstName text,
	LastName text,
	Email text PRIMARY KEY
);

CREATE TABLE Route(
	Name text PRIMARY KEY, 
	CreatedByPerson text REFERENCES Person(Email)
);

CREATE TABLE Point(
	Latitude double precision NOT NULL,
	Longitude double precision NOT NULL,
	OwnerRoute text REFERENCES Route(Name)
);

SELECT FirstName, LastName, Email FROM Person;
SELECT Name, CreatedByPerson FROM Route;
SELECT Latitude, Longitude, OwnerRoute FROM Point;

DROP TABLE Point;
DROP TABLE Route;
DROP TABLE Person;