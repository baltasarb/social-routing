package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.SqlConnection
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.model.modelMappers.RouteMapper
import ps.g49.socialroutingservice.repositories.RouteRepository
import java.sql.SQLException

@Component
class RouteRepositoryImplementation(private val sqlConnection: SqlConnection, private val mapper: RouteMapper) : RouteRepository {

    /**
     * @Param id is the name of the route
     */
    override fun findRouteById(id: String): Route {
        val query = "SELECT Identifier FROM Route WHERE Identifier = ?;"
        return sqlConnection.findOnly(query, mapper, id)
    }

    override fun findAll(): List<Route> {
        val query = "SELECT Identifier, PersonIdentifier FROM Route;"
        return sqlConnection.findMany(query, mapper)
    }
}