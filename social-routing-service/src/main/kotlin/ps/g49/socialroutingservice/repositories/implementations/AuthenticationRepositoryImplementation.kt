package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.AuthenticationDataMapper
import ps.g49.socialroutingservice.models.AuthenticationData
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

    override fun findAuthenticationDataById(connectionHandler: Handle, personIdentifier: Int): AuthenticationData? {
        var authenticationData : AuthenticationData?

        try{
            authenticationData = connectionHandler.select(AuthenticationQueries.FIND_AUTHENTICATION_DATA_BY_PERSON_IDENTIFIER)
                    .bind("personIdentifier", personIdentifier)
                    .map(authenticationDataMapper)
                    .findOnly()
        }catch (e :Exception){
            //if no value is found
            authenticationData = null
        }

        return authenticationData
    }

    override fun createOrUpdateAuthenticationData(connectionHandler: Handle, authenticationData: AuthenticationData) {
        connectionHandler.createUpdate(AuthenticationQueries.UPSERT_AUTHENTICATION_DATA)
                .bind("creationDate", authenticationData.creationDate)
                .bind("expirationDate", authenticationData.expirationDate)
                .bind("accessToken", authenticationData.accessToken)
                .bind("refreshToken", authenticationData.refreshToken)
                .bind("personIdentifier", authenticationData.personIdentifier)
                .execute()
    }

}