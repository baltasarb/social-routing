package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.Person

interface PersonRepository {

    fun findPersonById(identifier: Int): Person

    fun create(connectionHandle: Handle, person: Person) : Int

    fun delete(identifier: Int)

    fun update(connectionHandle: Handle, person: Person)

}