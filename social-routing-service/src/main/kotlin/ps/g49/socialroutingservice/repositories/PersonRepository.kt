package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.Person

interface PersonRepository {

    fun findById(identifier: Int): Person

    fun create(connectionHandle: Handle) : Int

    fun delete(identifier: Int)

    fun update(connectionHandle: Handle, person: Person)

    fun findBySub(sub : String) : Person?
}