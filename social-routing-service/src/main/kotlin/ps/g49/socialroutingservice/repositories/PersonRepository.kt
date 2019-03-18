package ps.g49.socialroutingservice.repositories

import org.springframework.data.repository.CrudRepository
import ps.g49.socialroutingservice.model.Person

interface PersonRepository : CrudRepository<Person, String> {
    fun findByLastName(lastName: String): Iterable<Person>
}