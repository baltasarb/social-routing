package ps.g49.socialroutingservice.utils.sqlQueries

class GoogleAuthenticationQueries {

    companion object{
        const val INSERT: String = "INSERT INTO GoogleAuthentication (Subject, PersonIdentifier) " +
                "VALUES (:subject, :personIdentifier);"

        const val FIND_BY_SUB = "SELECT PersonIdentifier " +
                "FROM GoogleAuthentication " +
                "WHERE Subject = :subject;"

        const val SELECT_IF_SUB_EXISTS = "SELECT 1 as Result FROM GoogleAuthenticationData WHERE HashedToken = :hashedToken AND Subject = :subject;"
    }
}