package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.PersonMapper
import ps.g49.socialroutingservice.models.domainModel.Person
import ps.g49.socialroutingservice.repositories.PersonRepository

@Component
class PersonRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val mapper: PersonMapper
) : PersonRepository {

    override fun create(connectionHandle: Handle, person: Person): Int {
        val query = "INSERT INTO Person (name, email) VALUES (:name, :email);"
        return connectionHandle.createUpdate(query)
                .bind("name", person.name)
                .bind("email", person.email)
                .executeAndReturnGeneratedKeys("identifier")
                .mapTo(Int::class.java)
                .findOnly()
    }

    override fun delete(identifier: Int) {
        val query = "DELETE FROM Person WHERE identifier = ?;"
        connectionManager.deleteByIntId(query, identifier)
    }

    override fun findPersonById(identifier: Int): Person {
        val query = "SELECT Identifier, Name, Email, Rating FROM Person WHERE Identifier = ?;"
        return connectionManager.findOnlyByIntId(query, mapper, identifier)
    }

    override fun update(connectionHandle: Handle, person: Person) {
        val query = "UPDATE Person SET (Name, Email, Rating) = (:name, :email, :rating) WHERE identifier = :identifier;"

        connectionHandle.createUpdate(query)
                .bind("name", person.name)
                .bind("email", person.email)
                .bind("rating", person.rating)
                .bind("identifier", person.identifier)
                .execute()
    }

}