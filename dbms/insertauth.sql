--not concurrent
--if person exists
SELECT Subject FROM GoogleAuthentication WHERE Subject = 1;
--if person does not exist
WITH InsertedPerson(
	INSERT INTO Person (Rating) VALUES (DEFAULT) 
	RETURNING Identifier as person_identifer
)
INSERT INTO GoogleAuthentication VALUES (HashedToken, Subject, (SELECT PersonIdentifier FROM InsertedPerson))