package ps.g49.socialroutingservice.utils.sqlQueries

class AuthenticationQueries {

    companion object {
        const val INSERT_AUTHENTICATION_DATA = "INSERT INTO Authentication (CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier) " +
                "VALUES (:creationDate, :expirationDate, :accessToken, :refreshToken, :personIdentifier);"

        const val FIND_AUTHENTICATION_DATA_BY_REFRESH_TOKEN = "SELECT CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier " +
                "FROM AuthenticationData " +
                "WHERE RefreshToken = :refreshToken;"

        const val FIND_AUTHENTICATION_DATA_BY_ACCESS_TOKEN = "SELECT CreationDate, ExpirationDate, AccessToken, RefreshToken, PersonIdentifier " +
                "FROM Authentication " +
                "WHERE AccessToken = :accessToken;"
        const val VALIDATE_USER = "Select PersonIdentifier FROM Authentication WHERE AccessToken = :accessToken;"
    }


}