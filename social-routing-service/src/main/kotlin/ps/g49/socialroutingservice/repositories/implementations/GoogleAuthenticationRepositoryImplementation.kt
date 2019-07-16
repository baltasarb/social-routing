package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.repositories.GoogleAuthenticationRepository
import ps.g49.socialroutingservice.utils.sqlQueries.GoogleAuthenticationQueries
import java.sql.SQLException

/**
 * repository used to establish a connection to the database regarding every google related transaction
 */
@Component
class GoogleAuthenticationRepositoryImplementation : GoogleAuthenticationRepository {

    /**
     * creates an entry in the database with the google subject
     */
    override fun create(connectionHandle: Handle, subject: String, personIdentifier: Int) {
        connectionHandle.createUpdate(GoogleAuthenticationQueries.INSERT)
                .bind("subject", subject)
                .bind("personIdentifier", personIdentifier)
                .execute()
    }

    /**
     * retrieves the person matching the received sub
     */
    override fun findPersonIdBySub(connectionHandle: Handle, subject: String): Int? {
        var personIdentifier: Int?

        try {
            personIdentifier = connectionHandle.select(GoogleAuthenticationQueries.FIND_BY_SUB)
                    .bind("subject", subject)
                    .mapTo(Int::class.java)
                    .findOnly()
        } catch (e: Exception) {
            //if no value is found
            personIdentifier = null
        }

        return personIdentifier
    }

}