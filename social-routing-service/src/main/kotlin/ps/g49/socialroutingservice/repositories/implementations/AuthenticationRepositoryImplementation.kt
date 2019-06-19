package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.InvalidAccessTokenException
import ps.g49.socialroutingservice.mappers.modelMappers.AuthenticationDataMapper
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import ps.g49.socialroutingservice.utils.sqlQueries.AuthenticationQueries
import ps.g49.socialroutingservice.utils.sqlQueries.GoogleAuthenticationQueries

@Component
class AuthenticationRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val authenticationDataMapper: AuthenticationDataMapper
) : AuthenticationRepository {

    override fun validateServerGeneratedTokenAndSubject(hashedToken: String, subject: String): Boolean {
        val handle = connectionManager.generateHandle()

        return try {
            val res = handle.select(GoogleAuthenticationQueries.SELECT_IF_SUB_EXISTS, subject)
                    .bind("hashedToken", hashedToken)
                    .bind("subject", subject)
                    .mapTo(Int::class.java)
                    .findOnly()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    override fun findAuthenticationDataByRefreshToken(connectionHandler: Handle, refreshToken: String): AuthenticationData? {
        var authenticationData: AuthenticationData?

        try {
            authenticationData = connectionHandler.select(AuthenticationQueries.FIND_AUTHENTICATION_DATA_BY_REFRESH_TOKEN)
                    .bind("refreshToken", refreshToken)
                    .map(authenticationDataMapper)
                    .findOnly()
        } catch (e: Exception) {
            //if no value is found
            authenticationData = null
        }

        return authenticationData
    }

    override fun createOrUpdateAuthenticationData(connectionHandler: Handle, authenticationData: AuthenticationData) {
        connectionHandler.createUpdate(AuthenticationQueries.INSERT_AUTHENTICATION_DATA)
                .bind("creationDate", authenticationData.creationDate)
                .bind("expirationDate", authenticationData.expirationDate)
                .bind("accessToken", authenticationData.accessToken)
                .bind("refreshToken", authenticationData.refreshToken)
                .bind("personIdentifier", authenticationData.personIdentifier)
                .execute()
    }

    override fun findAuthenticationDataByAccessToken(accessToken: String): AuthenticationData {
        try {
            return connectionManager.findOnly(AuthenticationQueries.FIND_AUTHENTICATION_DATA_BY_ACCESS_TOKEN, authenticationDataMapper, accessToken)
        } catch (e: java.lang.IllegalStateException) {
            //if no access token matching was foun then acces to the api should not be granted
            throw InvalidAccessTokenException()
        }
    }

}