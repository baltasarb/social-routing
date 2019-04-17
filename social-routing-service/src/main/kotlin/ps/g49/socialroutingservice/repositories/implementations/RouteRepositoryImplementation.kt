package ps.g49.socialroutingservice.repositories.implementations

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.repositories.RouteRepository

@Component
class RouteRepositoryImplementation(private val connectionManager: ConnectionManager, private val mapper: RouteMapper) : RouteRepository {

    override fun create(connectionHandle: Handle, route: Route) {
        val query = "INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier)" +
                "VALUES (:location, :name, :description, :duration, CURRENT_DATE, to_json(:points), :personIdentifier);"

        connectionHandle.createUpdate(query)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("personIdentifier", route.personIdentifier)
                .bind("points", route.points.toString())
                .execute()
    }

    override fun delete(identifier: Int) {
        val query = "DELETE FROM Route WHERE identifier = ?;"
        return connectionManager.deleteByIntId(query, identifier)
    }

    override fun update(connectionHandle: Handle, route: Route) {
        val query = "UPDATE Route SET (Location, Name, Description, Points) = (:location, :name, :description, to_json(:points)) WHERE identifier = :identifier;"

        connectionHandle.createUpdate(query)
                .bind("identifier", route.identifier)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("points", route.points.toString())
                .execute()
    }

    /**
     * @Param id is the name of the route
     */
    override fun findRouteById(id: Int): Route {
        val query = "SELECT Identifier, Location, Name, Description, Classification, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Identifier = ?;"
        return connectionManager.findOnlyByIntId(query, mapper, id)
    }

    override fun findAll(): List<Route> {
        val query = "SELECT Identifier, Location, Name, Description, Classification, Duration, DateCreated, Points, PersonIdentifier FROM Route;"
        return connectionManager.findMany(query, mapper)
    }

    override fun findAllByParameter(parameter: String): List<Route> {
        val query = "SELECT Identifier, Location, Name, Description, Classification, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Location = ?;"
        return connectionManager.findMany(query, mapper, parameter)
    }

    override fun findPersonCreatedRoutes(identifier: Int): List<Route> {
        val query = "SELECT Identifier, Location, Name, Description, Classification, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE PersonIdentifier = ?;"
        return connectionManager.findManyByIntId(query, mapper, identifier)
    }

}