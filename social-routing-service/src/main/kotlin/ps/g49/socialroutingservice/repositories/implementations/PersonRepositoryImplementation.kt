package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.SqlConnection
import ps.g49.socialroutingservice.model.Person
import ps.g49.socialroutingservice.modelMappers.PersonMapper
import ps.g49.socialroutingservice.repositories.PersonRepository
import java.sql.SQLException

@Component
class PersonRepositoryImplementation(private val sqlConnection: SqlConnection, private val mapper: PersonMapper) : PersonRepository {
    override fun findPersonById(id: String): Person {
        return sqlConnection
                .jdbi
                .withHandle<Person, SQLException> { handle ->
                    handle.select("SELECT Name FROM Person WHERE Name = ?;", id)
                            .map(mapper)
                            .findOnly()
                }
    }
}