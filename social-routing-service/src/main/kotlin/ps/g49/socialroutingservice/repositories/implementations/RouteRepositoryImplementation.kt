package ps.g49.socialroutingservice.repositories.implementations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sun.xml.internal.fastinfoset.util.StringArray
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.generic.GenericType
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.CategoryMapper
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteMapper
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.repositories.RouteRepository
import java.sql.Types

@Component
class RouteRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val mapper: RouteMapper,
        private val categoryMapper: CategoryMapper,
        private val simplifiedRouteMapper: SimplifiedRouteMapper
) : RouteRepository {

    override fun create(connectionHandle: Handle, route: Route): Int {
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)

        val query = "{ :createdRouteId = call insertRouteAndRouteCategories(:location, :name, :description, :duration, to_json(:points), :personIdentifier, <categories>)}"
        val result = connectionHandle
                .createCall(query)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("personIdentifier", route.personIdentifier)
                .bind("points", points)
                .bindList("categories", route.categories)
                .registerOutParameter("createdRouteId", Types.INTEGER)
                .invoke()

        return result.getInt("createdRouteId")
    }

    override fun delete(identifier: Int) {
        val query = "DELETE FROM Route WHERE identifier = ?;"
        return connectionManager.deleteByIntId(query, identifier)
    }

    override fun update(connectionHandle: Handle, route: Route) {
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)

        val query = "{call updateRouteAndRouteCategories(:identifier, :location, :name, :description, :rating, :duration, to_json(:points), <categories>)}"
        connectionHandle
                .createCall(query)
                .bind("identifier", route.identifier)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("rating", route.rating)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("points", points)
                .bindList("categories", route.categories)
                .invoke()
    }

    /**
     * @Param id is the name of the route
     */
    override fun findRouteById(connectionHandle: Handle, id: Int): Route {
        val routeQuery = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Identifier = ?;"
        val route = connectionHandle.select(routeQuery, id).map(mapper).findOnly()
        //TODO sql function for both queries
        val categoriesQuery = "SELECT CategoryName FROM RouteCategory WHERE RouteIdentifier = ?;"
        val categories = connectionHandle.select(categoriesQuery, id).map(categoryMapper).toList()

        route.categories = categories
        return route
    }

    override fun findAll(): List<SimplifiedRoute> {
        val query = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route;"
        return connectionManager.findMany(query, simplifiedRouteMapper)
    }

    override fun findAllByParameter(parameter: String): List<SimplifiedRoute> {
        val query = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Location = ?;"
        return connectionManager.findMany(query, simplifiedRouteMapper, parameter)
    }

    override fun findPersonCreatedRoutes(identifier: Int): List<SimplifiedRoute> {
        val query = "SELECT Identifier, Name, Rating, PersonIdentifier FROM Route WHERE PersonIdentifier = ?;"
        return connectionManager.findManyByIntId(query, simplifiedRouteMapper, identifier)
    }

}