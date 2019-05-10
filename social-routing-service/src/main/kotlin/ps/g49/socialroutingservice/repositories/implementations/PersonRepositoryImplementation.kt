package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.PersonMapper
import ps.g49.socialroutingservice.models.domainModel.Person
import ps.g49.socialroutingservice.repositories.PersonRepository
import ps.g49.socialroutingservice.utils.sqlQueries.PersonQueries

@Component
class PersonRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val mapper: PersonMapper
) : PersonRepository {

    override fun create(connectionHandle: Handle, person: Person): Int {
        return connectionHandle.createUpdate(PersonQueries.INSERT)
                .bind("name", person.name)
                .bind("email", person.email)
                .executeAndReturnGeneratedKeys("identifier")
                .mapTo(Int::class.java)
                .findOnly()
    }

    override fun delete(identifier: Int) {
        connectionManager.deleteByIntId(PersonQueries.DELETE, identifier)
    }

    override fun findById(identifier: Int): Person {
        return connectionManager.findOnlyByIntId(PersonQueries.SELECT, mapper, identifier)
    }

    override fun update(connectionHandle: Handle, person: Person) {
        connectionHandle.createUpdate(PersonQueries.UPDATE)
                .bind("name", person.name)
                .bind("email", person.email)
                .bind("rating", person.rating)
                .bind("identifier", person.identifier)
                .execute()
    }

}