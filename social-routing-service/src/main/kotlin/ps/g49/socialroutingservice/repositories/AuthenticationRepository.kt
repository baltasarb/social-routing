package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData

interface AuthenticationRepository {

    fun validateServerGeneratedTokenAndSubject(hashedToken: String, subject: String): Boolean

    fun findAuthenticationDataByRefreshToken(connectionHandler: Handle, refreshToken: String): AuthenticationData?

    fun createAuthenticationData(connectionHandler: Handle, authenticationData: AuthenticationData)

    fun findAuthenticationDataByAccessToken(accessToken: String): AuthenticationData

    fun validateUserRequest(hashedAccessToken: String, personIdentifier: Int): Boolean

}