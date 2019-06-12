package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.repositories.GoogleAuthenticationRepository
import ps.g49.socialroutingservice.utils.sqlQueries.GoogleAuthenticationQueries

@Component
class GoogleAuthenticationRepositoryImplementation : GoogleAuthenticationRepository {

    override fun create(connectionHandle: Handle, subject: String, personIdentifier: Int) {
        connectionHandle.createUpdate(GoogleAuthenticationQueries.INSERT)
                .bind("subject", subject)
                .bind("authenticationPersonIdentifier", personIdentifier)
                .execute()
    }

    override fun findPersonIdBySub(connectionHandle: Handle, subject: String): Int? {
        var personIdentifier : Int?

        try{
            personIdentifier = connectionHandle.select(GoogleAuthenticationQueries.FIND_BY_SUB)
                    .bind("subject", subject)
                    .mapTo(Int::class.java)
                    .findOnly()
        }catch (e : Exception){
            //if no value is found
            personIdentifier = null
        }

        return personIdentifier
    }

}