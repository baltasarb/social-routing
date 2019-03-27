package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.SqlConnection
import ps.g49.socialroutingservice.model.Person
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.model.modelMappers.PersonMapper
import ps.g49.socialroutingservice.model.modelMappers.RouteMapper
import ps.g49.socialroutingservice.repositories.PersonRepository
import java.sql.SQLException

@Component
class PersonRepositoryImplementation(private val sqlConnection: SqlConnection, private val personMapper: PersonMapper, private val routeMapper: RouteMapper) : PersonRepository {

    override fun findUserCreatedRoutes(identifier: String): List<Route> {
        return sqlConnection
                .jdbi
                .withHandle<List<Route>, SQLException> { handle ->
                    handle.select("SELECT Identifier, PersonIdentifier FROM Route WHERE PersonIdentifier = ?;", identifier)
                            .map(routeMapper)
                            .list()
                }
    }

    override fun findUserPerformedRoutes(identifier: String): List<Route> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findPersonById(identifier: String): Person {
        return sqlConnection
                .jdbi
                .withHandle<Person, SQLException> { handle ->
                    handle.select("SELECT Name FROM Person WHERE Name = ?;", identifier)
                            .map(personMapper)
                            .findOnly()
                }
    }
}