package ps.g49.socialroutingservice.repositories.implementations

import org.apache.logging.log4j.core.util.UuidUtil.*
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.GooglePersonInfoMapper
import ps.g49.socialroutingservice.models.domainModel.GooglePersonInfo
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import ps.g49.socialroutingservice.utils.sqlQueries.GoogleAuthenticationQueries
import java.sql.SQLException

@Component
class AuthenticationRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val googlePersonInfoMapper: GooglePersonInfoMapper
) : AuthenticationRepository {

    override fun findGooglePersonInfoAndCreateIfNotExists(
            subject: String,
            hashToken: (String) -> String
    ): GooglePersonInfo {
        val connection = connectionManager.generateHandle()
        return connection.inTransaction<GooglePersonInfo, SQLException> { handle ->
            getPersonIdentifierAndTokenIfExists(handle, subject)
                    ?: createAndGetPersonAndAuth(handle, hashToken, subject)
        }
    }

    private fun getPersonIdentifierAndTokenIfExists(handle: org.jdbi.v3.core.Handle, subject: String): GooglePersonInfo? {
        try {
            return handle.createQuery(GoogleAuthenticationQueries.SELECT_BY_SUB)
                    .bind("subject", subject)
                    .map(googlePersonInfoMapper)
                    .findOnly()
        } catch (e: IllegalStateException) {
            return null
        }
    }

    private fun createAndGetPersonAndAuth(handle: org.jdbi.v3.core.Handle, hashToken: (String) -> String, subject: String): GooglePersonInfo {
        val token = getTimeBasedUuid().toString()
        val hashedToken = hashToken(token)
        val personIdentifier = handle.createUpdate(GoogleAuthenticationQueries.INSERT_PERSON_AND_AUTH)
                .bind("hashedToken", hashedToken)
                .bind("subject", subject)
                .executeAndReturnGeneratedKeys("person_identifier")
                .mapTo(Int::class.java)
                .findOnly()//todo exception

        return GooglePersonInfo(personIdentifier, token)
    }

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
}