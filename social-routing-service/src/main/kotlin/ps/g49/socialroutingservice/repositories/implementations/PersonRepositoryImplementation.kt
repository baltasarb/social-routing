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
        val query = "SELECT Identifier, PersonIdentifier FROM Route WHERE PersonIdentifier = ?;"
        return sqlConnection.findMany(query, routeMapper, identifier)
    }

    override fun findUserPerformedRoutes(identifier: String): List<Route> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findPersonById(identifier: String): Person {
        val query = "SELECT Name FROM Person WHERE Name = ?;"
        return sqlConnection.findOnly(query, personMapper, identifier)
    }
}