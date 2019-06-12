CREATE TABLE Person(
	Identifier serial PRIMARY KEY,
	Rating real DEFAULT 0.0 NOT NULL
);

CREATE TABLE Authentication(
	CreationDate bigint NOT NULL,
	ExpirationDate bigint NOT NULL,
	AccessToken text NOT NULL,
	RefreshToken text NOT NULL,
	PersonIdentifier integer REFERENCES Person(Identifier) PRIMARY KEY
);

CREATE TABLE GoogleAuthentication(
	Subject text NOT NULL UNIQUE,
	AuthenticationPersonIdentifier integer REFERENCES Authentication(PersonIdentifier) PRIMARY KEY
)

INSERT INTO Person (Identifier, Rating) values (100, 1.0)
ON CONFLICT (Identifier)
DO UPDATE
	SET Rating = 5.0

insert into GoogleAuthentication (DateCreated, ExpiresIn, AccessToken, RefreshToken, Subject, PersonIdentifier)
values(1, 1, 'at1', 'rt1','sub1', 100),
(2, 2, 'at2', 'rt2','sub2', 100)

select * from Person
select * from GoogleAuthentication

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
