package ps.g49.socialroutingservice.repositories.implementations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.InsertException
import ps.g49.socialroutingservice.mappers.sqlArrayTypeMappers.CategoryArrayType
import ps.g49.socialroutingservice.mappers.modelMappers.CategoryMapper
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteMapper
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.repositories.RouteRepository
import ps.g49.socialroutingservice.utils.sqlQueries.RouteQueries

@Component
class RouteRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val mapper: RouteMapper,
        private val categoryMapper: CategoryMapper,
        private val simplifiedRouteMapper: SimplifiedRouteMapper
) : RouteRepository {

    override fun findById(connectionHandle: Handle, id: Int): Route {
        //TODO split into transaction
        val route = connectionHandle.select(RouteQueries.SELECT, id).map(mapper).findOnly()
        val categories = connectionHandle.select(RouteQueries.SELECT_ROUTE_CATEGORIES, id).map(categoryMapper).toList()
        route.categories = categories.map { Category(it) }
        return route
    }

    override fun findAll(): List<SimplifiedRoute> {
        return connectionManager.findMany(RouteQueries.SELECT_MANY, simplifiedRouteMapper)
    }

    override fun findAllByParameter(parameter: String): List<SimplifiedRoute> {
        return connectionManager.findMany(RouteQueries.SELECT_MANY_BY_LOCATION, simplifiedRouteMapper, parameter)
    }

    override fun findPersonCreatedRoutes(identifier: Int): List<SimplifiedRoute> {
        return connectionManager.findManyByIntId(RouteQueries.SELECT_MANY_BY_OWNER, simplifiedRouteMapper, identifier)
    }

    override fun create(connectionHandle: Handle, route: Route): Int {
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)
        //todo exception in type conversion

        //register the type converter
        connectionHandle.registerArrayType(CategoryArrayType())

        val insertedRouteId = connectionHandle.createUpdate(RouteQueries.INSERT_WITH_CATEGORIES)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("personIdentifier", route.personIdentifier)
                .bind("points", points)
                .bind("categories", route.categories!!.toTypedArray())
                .executeAndReturnGeneratedKeys("route_id")
                .mapTo(Int::class.java)
                .findFirst()

        if (!insertedRouteId.isPresent)
            throw InsertException()

        return insertedRouteId.get()
    }

    override fun update(connectionHandle: Handle, route: Route) {
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)

        //register the type converter
        connectionHandle.registerArrayType(CategoryArrayType())

        connectionHandle.createUpdate(RouteQueries.UPDATE_WITH_CATEGORIES)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("rating", route.rating)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("points", points)
                .bind("routeIdentifier", route.identifier)
                .bind("categories", route.categories!!.toTypedArray())
                .execute()
    }

    override fun delete(identifier: Int) {
        return connectionManager.deleteByIntId(RouteQueries.DELETE, identifier)
    }

}