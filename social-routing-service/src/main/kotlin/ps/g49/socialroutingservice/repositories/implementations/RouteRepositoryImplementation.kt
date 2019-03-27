package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.SqlConnection
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.modelMappers.RouteMapper
import ps.g49.socialroutingservice.repositories.RouteRepository
import java.sql.SQLException

@Component
class RouteRepositoryImplementation(private val sqlConnection: SqlConnection, private val mapper: RouteMapper) : RouteRepository {

    /**
     * @Param id is the name of the route
     */
    override fun findRouteById(id: String): Route {
        return sqlConnection.jdbi.withHandle<Route, SQLException> { handle ->
            handle.select("SELECT Identifier FROM Route WHERE Identifier = ?;", id)
                    .map(mapper)//when a query returns a single column, we can map it to the desired Java type:
                    .findOnly()//for a single result
        }
    }

    override fun findAll(): List<Route> {
        return sqlConnection
                .jdbi
                .withHandle<List<Route>, SQLException> { handle ->
                    handle.select("SELECT Identifier, PersonIdentifier FROM Route;")
                            .map(mapper)
                            .list()
                }
    }
}