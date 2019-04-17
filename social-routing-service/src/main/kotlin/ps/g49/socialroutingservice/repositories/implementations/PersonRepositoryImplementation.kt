package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.PersonMapper
import ps.g49.socialroutingservice.model.Person
import ps.g49.socialroutingservice.repositories.PersonRepository

@Component
class PersonRepositoryImplementation(private val connectionManager: ConnectionManager, private val mapper: PersonMapper) : PersonRepository {

    override fun create(name: String, email: String) {
        val query = "INSERT INTO Person (name, email) VALUES (?,?);"
        connectionManager.insert(query, name, email)
    }

    override fun delete(identifier: Int) {
        val query = "DELETE FROM Person WHERE identifier = ?;"
        connectionManager.deleteByIntId(query, identifier)
    }

    override fun findPersonById(identifier: Int): Person {
        val query = "SELECT Identifier, Name, Email FROM Person WHERE Identifier = ?;"
        return connectionManager.findOnlyByIntId(query, mapper, identifier)
    }

}