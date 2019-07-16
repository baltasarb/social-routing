package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.ForbiddenRequestException
import ps.g49.socialroutingservice.exceptions.InternalServerErrorException
import ps.g49.socialroutingservice.exceptions.InvalidAccessTokenException
import ps.g49.socialroutingservice.mappers.modelMappers.AuthenticationDataMapper
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import ps.g49.socialroutingservice.utils.sqlQueries.AuthenticationQueries
import ps.g49.socialroutingservice.utils.sqlQueries.GoogleAuthenticationQueries

/**
 * repository used to communicate with the database, pertaining every action that is server authentication related
 */
@Component
class AuthenticationRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val authenticationDataMapper: AuthenticationDataMapper
) : AuthenticationRepository {

    /**
     * used to validate if the token received is valid, by checking if the subject and token belong to the same tuple
     * @param hashedToken the hashed token received by the user
     * @param subject the subject of the user attempting to authenticate
     * @return true if the user is in fact authenticated or false if not
     */
    override fun validateServerGeneratedTokenAndSubject(hashedToken: String, subject: String): Boolean {
        val handle = connectionManager.generateHandle()
        handle.use {
            return try {
                handle.select(GoogleAuthenticationQueries.SELECT_IF_SUB_EXISTS, subject)
                        .bind("hashedToken", hashedToken)
                        .bind("subject", subject)
                        .mapTo(Int::class.java)
                        .findOnly()
                true
            } catch (e: IllegalStateException) {
                false
            }
        }
    }

    /**
     * used to retrieve a user's authentication data by his refresh token
     * @param connectionHandler jdbi connection shared at controller level
     * @param refreshToken the token used to fetch the authentication data
     * @return the authentication data if found or null if not present
     */
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

    /**
     * used to store a user's credentials in the database
     * @param authenticationData the user's authentication data
     * @param connectionHandler jdbi connection shared at controller level
     */
    override fun createAuthenticationData(connectionHandler: Handle, authenticationData: AuthenticationData) {
        connectionHandler.createUpdate(AuthenticationQueries.INSERT_AUTHENTICATION_DATA)
                .bind("creationDate", authenticationData.creationDate)
                .bind("expirationDate", authenticationData.expirationDate)
                .bind("accessToken", authenticationData.accessToken)
                .bind("refreshToken", authenticationData.refreshToken)
                .bind("personIdentifier", authenticationData.personIdentifier)
                .execute()
    }

    /**
     * attempts to retrieve a user's authentication using an access token
     * @param accessToken the access token used to search the database
     */
    override fun findAuthenticationDataByAccessToken(accessToken: String): AuthenticationData {
        val params = hashMapOf<String, Any>(accessToken to "accessToken")
        return connectionManager.findOnlyByParams(
                AuthenticationQueries.FIND_AUTHENTICATION_DATA_BY_ACCESS_TOKEN,
                authenticationDataMapper as RowMapper<AuthenticationData>,
                params
        )
    }

    /**
     * used to validate a user's token
     * @param hashedAccessToken the token of the user trying to be authenticated
     * @param personIdentifier the person identifier received from the user, to be checked against the one returned from the
     * database
     * @throws ForbiddenRequestException if the received personIdentifier does not match the one retrieved from the database
     * @throws IllegalStateException if no entry is present with that access token
     * @throws InternalServerErrorException if an uknown error occurs
     */
    override fun validateUserRequest(hashedAccessToken: String, personIdentifier: Int): Boolean {
        val handle = connectionManager.generateHandle()
        handle.use {
            try {
                val retrievedPersonIdentifier = handle
                        .select(AuthenticationQueries.VALIDATE_USER)
                        .bind("accessToken", hashedAccessToken)
                        .mapTo(Int::class.java)
                        .findOnly()
                if (retrievedPersonIdentifier != personIdentifier)
                    throw ForbiddenRequestException()
            } catch (e: java.lang.IllegalStateException) {
                throw InvalidAccessTokenException()
            } catch (sqlException: Exception) {
                throw InternalServerErrorException()
            }
            return true
        }
    }
}