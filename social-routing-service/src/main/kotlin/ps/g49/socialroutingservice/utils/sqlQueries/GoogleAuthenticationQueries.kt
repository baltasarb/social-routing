package ps.g49.socialroutingservice.utils.sqlQueries

class GoogleAuthenticationQueries {

    companion object{
        const val SELECT_BY_SUB = "SELECT PersonIdentifier, HashedToken " +
                "FROM GoogleAuthentication " +
                "WHERE Subject = :subject;"

        const val INSERT_PERSON_AND_AUTH = "WITH InsertedPerson AS( " +
                "INSERT INTO Person (Rating) VALUES (DEFAULT) " +
                "RETURNING Identifier as person_identifier " +
                ") " +
                "INSERT INTO GoogleAuthentication (HashedToken, Subject, PersonIdentifier) " +
                "VALUES (:hashedToken, :subject, (SELECT person_identifier FROM InsertedPerson)) " +
                "RETURNING (SELECT person_identifier FROM InsertedPerson);"

        const val SELECT_IF_SUB_EXISTS = "SELECT 1 as Result FROM GoogleAuthentication WHERE HashedToken = :hashedToken AND Subject = :subject;"
    }
}