package ps.g49.socialroutingservice.utils.sqlQueries

class AuthenticationQueries {

    companion object{
        const val UPSERT_AUTHENTICATION_DATA = "INSERT INTO Authentication (CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier) " +
                "VALUES (:creationDate, :expirationDate, :accessToken, :refreshToken, :personIdentifier) " +
                "ON CONFLICT (PersonIdentifier) " +
                "DO UPDATE " +
                "SET CreationDate = :creationDate, ExpirationDate = :expirationDate, AccessToken = :accessToken, RefreshToken = :refreshToken, PersonIdentifier = :personIdentifier;"

        const val FIND_AUTHENTICATION_DATA_BY_PERSON_IDENTIFIER = "SELECT CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier " +
                "FROM AuthenticationData " +
                "WHERE personIdentifier = :personIdentifier;"

        const val FIND_AUTHENTICATION_DATA_BY_ACCESS_TOKEN = "SELECT CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier " +
                "FROM Authentication " +
                "WHERE AccessToken = ?;"
    }


}