CREATE TABLE Person(
	Identifier serial PRIMARY KEY,
	Rating real DEFAULT 0.0 NOT NULL
);

CREATE TABLE Authentication(
	CreationDate bigint NOT NULL,
	ExpirationDate bigint NOT NULL,
	AccessToken text NOT NULL,
	RefreshToken text NOT NULL PRIMARY KEY,
	PersonIdentifier integer REFERENCES Person(Identifier)
);

CREATE TABLE GoogleAuthentication(
	Subject text NOT NULL UNIQUE,
	PersonIdentifier integer REFERENCES Person(Identifier) PRIMARY KEY
);

CREATE TABLE Image(
	Reference text NOT NULL,
	PRIMARY KEY(Reference)
);

CREATE TABLE Route(
	Identifier serial PRIMARY KEY,
	LocationIdentifier text NOT NULL,
	Name text NOT NULL, 
	Description text,
	Rating double precision DEFAULT 0.0, -- different no classified with zero classification
	Duration text NOT NULL, -- short, medium, long
	DateCreated date NOT NULL,
	Points jsonb NOT NULL,
	Elevation double precision DEFAULT NULL,
	Circular boolean NOT NULL,
	Ordered boolean NOT NULL,
	ImageReference text REFERENCES Image(Reference) NOT NULL,
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

CREATE TABLE PointOfInterest(
	Identifier text UNIQUE,
	latitude real NOT NULL,
	longitude real NOT NULL,
	PRIMARY KEY(Identifier)
);

CREATE TABLE RoutePointOfInterest(
	RouteIdentifier integer REFERENCES Route(Identifier) ON DELETE CASCADE,
	PointOfInterestIdentifier text REFERENCES PointOfInterest(Identifier) ON DELETE CASCADE,
	PRIMARY KEY(RouteIdentifier, PointOfInterestIdentifier)
);

