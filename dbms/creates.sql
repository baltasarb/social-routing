CREATE TABLE Person(
	Identifier serial PRIMARY KEY,
	Name text NOT NULL,
	Email text UNIQUE NOT NULL,
	Rating real DEFAULT 0.0 NOT NULL
);

CREATE TABLE Route(
	Identifier serial PRIMARY KEY,
	Location text NOT NULL,
	Name text NOT NULL, 
	Description text,
	Rating double precision DEFAULT 0.0, -- different no classified with zero classification
	Duration bigint NOT NULL, -- Duration in minutes (provided by google api)
	DateCreated date NOT NULL,
	Points json NOT NULL,
	PersonIdentifier serial REFERENCES Person(Identifier) ON DELETE CASCADE
);

CREATE TABLE Category(
	Name text PRIMARY KEY
);

CREATE TABLE RouteCategory(
	CategoryName text REFERENCES Category(Name),
	RouteIdentifier integer REFERENCES Route(Identifier) ON DELETE CASCADE,
	PRIMARY KEY (CategoryName, RouteIdentifier)
);
