CREATE TABLE Person(
	Identifier serial PRIMARY KEY,
	Name text NOT NULL,
	Email text UNIQUE NOT NULL
);

CREATE TABLE Route(
	Identifier serial PRIMARY KEY,
	Location text NOT NULL,
	Name text NOT NULL, 
	Description text,
	Classification real, -- different no classified with zero classification
	-- Duration in minutes (provided by google api)
	Duration bigint NOT NULL, 
	DateCreated date NOT NULL,
	Points json NOT NULL,
	PersonIdentifier serial REFERENCES Person(Identifier) -- change name to email
);

/*
CREATE TABLE Category(
	Name text PRIMARY KEY
);

CREATE TABLE RouteCategory(
	CategoryName text REFERENCES Category(Name),
	RouteIdentifier bigint REFERENCES Route(Identifier),
	PRIMARY KEY (CategoryName, RouteIdentifier)
);
*/