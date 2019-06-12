package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.AuthenticationData

interface AuthenticationRepository {

    fun validateServerGeneratedTokenAndSubject(hashedToken : String, subject: String) : Boolean

    fun findAuthenticationDataById(connectionHandler: Handle,personIdentifier: Int): AuthenticationData?

    fun createOrUpdateAuthenticationData(connectionHandler: Handle, authenticationData: AuthenticationData)

}