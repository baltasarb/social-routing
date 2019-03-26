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
	Id bigint NOT NULL,
	Latitude double precision NOT NULL,
	Longitude double precision NOT NULL,
	RouteName text REFERENCES Route(Name)
);

-- Used to generate point ids
CREATE SEQUENCE PointSequence
OWNED BY Point.ID

SELECT FirstName, LastName, Email FROM Person;
SELECT Name FROM Route;
SELECT Latitude, Longitude, RouteName FROM Point;

DROP TABLE Point;
DROP TABLE Route;
DROP TABLE Person;