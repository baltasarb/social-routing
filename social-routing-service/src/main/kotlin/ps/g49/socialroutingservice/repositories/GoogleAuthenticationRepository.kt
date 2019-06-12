package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle

interface GoogleAuthenticationRepository {

    fun findPersonIdBySub(connectionHandle: Handle, subject : String) : Int?

    fun create(connectionHandle: Handle, subject: String, personIdentifier : Int)
}