package ps.g49.socialroutingservice.repositories.implementations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.sqlArrayTypeMappers.CategoryArrayType
import ps.g49.socialroutingservice.mappers.modelMappers.CategoryMapper
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteMapper
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.repositories.RouteRepository

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

        val routeInsertQuery = "INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) VALUES (:location, :name, :description, :duration, CURRENT_DATE, to_json(:points), :personIdentifier);"

        val categoryInsertQuery = "INSERT INTO RouteCategory (RouteIdentifier, CategoryName) VALUES (:routeIdentifier, unnest(:categories));"

        //guarantee that either both transactions are made or none at all with inTransaction
        return connectionHandle.inTransaction<Int, Exception> { h ->
            //insert every route
            val routeIdentifier = h.createUpdate(routeInsertQuery)
                    .bind("location", route.location)
                    .bind("name", route.name)
                    .bind("description", route.description)
                    .bind("duration", 0) //TODO (time needs to be calculated by the server)
                    .bind("personIdentifier", route.personIdentifier)
                    .bind("points", points)
                    .executeAndReturnGeneratedKeys("identifier")
                    .mapTo(Int::class.java)
                    .findOnly()

            //register the type converter
            h.registerArrayType(CategoryArrayType())

            //insert the route categories
            h.createUpdate(categoryInsertQuery)
                    .bind("categories", route.categories!!.toTypedArray())
                    .bind("routeIdentifier", routeIdentifier!!)
                    .execute()

            routeIdentifier
        }
    }

    override fun delete(identifier: Int) {
        val query = "DELETE FROM Route WHERE identifier = ?;"
        return connectionManager.deleteByIntId(query, identifier)
    }

    override fun update(connectionHandle: Handle, route: Route) {
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)

        connectionHandle.inTransaction<Int, Exception> { h ->
            //update every route
            val routeUpdateQuery = "UPDATE Route SET (Location, Name, Description, Rating, Duration, Points) = (:location, :name, :description, :rating, :duration, to_json(:points));"
            h.createUpdate(routeUpdateQuery)
                    .bind("location", route.location)
                    .bind("name", route.name)
                    .bind("description", route.description)
                    .bind("rating", route.rating)
                    .bind("duration", 0) //TODO (time needs to be calculated by the server)
                    .bind("points", points)
                    .execute()

            //delete existing categories
            val categoryDeleteQuery = "DELETE FROM RouteCategory WHERE RouteIdentifier = :identifier;"
            h.createUpdate(categoryDeleteQuery)
                    .bind("identifier", route.identifier)
                    .execute()

            //register the type converter
            h.registerArrayType(CategoryArrayType())

            //insert the new route categories
            val categoryInsertQuery = "INSERT INTO RouteCategory (RouteIdentifier, CategoryName) VALUES (:routeIdentifier, unnest(:categories));"
            h.createUpdate(categoryInsertQuery)
                    .bind("categories", route.categories!!.toTypedArray())
                    .bind("routeIdentifier", route.identifier)
                    .execute()
        }
    }

    /**
     * @Param id is the name of the route
     */
    override fun findById(connectionHandle: Handle, id: Int): Route {
        val routeQuery = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Identifier = ?;"
        val route = connectionHandle.select(routeQuery, id).map(mapper).findOnly()
        //TODO sql function for both queries
        val categoriesQuery = "SELECT CategoryName FROM RouteCategory WHERE RouteIdentifier = ?;"
        val categories = connectionHandle.select(categoriesQuery, id).map(categoryMapper).toList()

        route.categories = categories.map { Category(it) }
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