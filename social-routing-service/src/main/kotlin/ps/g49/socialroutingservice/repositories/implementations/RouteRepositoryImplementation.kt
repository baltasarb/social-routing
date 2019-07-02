package ps.g49.socialroutingservice.repositories.implementations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.InsertException
import ps.g49.socialroutingservice.exceptions.ResourceNotFoundException
import ps.g49.socialroutingservice.mappers.sqlArrayTypeMappers.CategoryArrayType
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteWithCountMapper
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteCollection
import ps.g49.socialroutingservice.repositories.RouteRepository
import ps.g49.socialroutingservice.utils.sqlQueries.RouteQueries

@Component
class RouteRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val mapper: RouteMapper,
        private val simplifiedRouteMapper: SimplifiedRouteMapper,
        private val simplifiedRouteWithCountMapper: SimplifiedRouteWithCountMapper
) : RouteRepository {

    private val routesPerResult = 2

    override fun findById(connectionHandle: Handle, id: Int): Route {
        return connectionHandle.select(RouteQueries.SELECT_BY_ID_WITH_CATEGORIES)
                .bind("routeIdentifier", id)
                .map(mapper)
                .findOnly()
    }

    override fun findAll(): List<SimplifiedRoute> {
        return connectionManager.findMany(RouteQueries.SELECT_MANY, simplifiedRouteMapper)
    }

    override fun findByLocation(location: String, page: Int, categories: List<Category>?, duration: String?): SimplifiedRouteCollection {
        val params = hashMapOf<String, Any>("location" to location)

        val queryStringBuilder = StringBuilder()

        queryStringBuilder.append(RouteQueries.SELECT_MANY_BY_LOCATION_WITH_PAGINATION)

        if (categories != null) {
            queryStringBuilder.append("AND (")
            for(i in categories.indices){
                if(i != 0){
                    queryStringBuilder.append(" OR ")
                }
                queryStringBuilder.append("RouteCategory.CategoryName = :category$i")
                params["category$i"] = categories[i].name
            }
            queryStringBuilder.append(")")
        }

        if(duration != null){
            params["duration"] = duration
            val durationQuery = "AND Route.Duration = :duration "
            queryStringBuilder.append(durationQuery)
        }

        queryStringBuilder.append("" +
                "GROUP BY Route.identifier " +
               "ORDER BY Rating DESC " +
                "LIMIT :limit " +
                "OFFSET :offset;"
        )

        //if categories is null && duration is not null

        //if categories is null && duration is null

        //if categories is not null && duration is null

        //if categories is not null && duration is not null

        val result = connectionManager.findManyWithPagination(
                routesPerResult,
                RouteQueries.SELECT_MANY_BY_LOCATION_WITH_PAGINATION,
                simplifiedRouteWithCountMapper,
                page,
                params
        )

        if (result.isEmpty())
            throw ResourceNotFoundException()

        val totalCount = result.first().count

        return SimplifiedRouteCollection(
                result.map { SimplifiedRoute(it.identifier, it.name, it.rating, it.personIdentifier) },
                if (nextPageExists(totalCount, page)) page + 1 else null
        )
    }

    override fun findPersonCreatedRoutes(identifier: Int, page: Int): SimplifiedRouteCollection {
        val params = hashMapOf<String, Any>("personIdentifier" to identifier)

        val result = connectionManager.findManyWithPagination(
                routesPerResult,
                RouteQueries.SELECT_MANY_BY_OWNER_WITH_PAGINATION_AND_COUNT,
                simplifiedRouteWithCountMapper,
                page,
                params
        )

        if (result.isEmpty())
            throw ResourceNotFoundException()

        val totalCount = result.first().count

        return SimplifiedRouteCollection(
                result.map { SimplifiedRoute(it.identifier, it.name, it.rating, it.personIdentifier) },
                if (nextPageExists(totalCount, page)) page + 1 else null
        )
    }

    override fun create(connectionHandle: Handle, route: Route): Int {
        //convert list of geographicPoints to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.geographicPoints)
        //todo exception in type conversion

        //register the type converter
        connectionHandle.registerArrayType(CategoryArrayType())

        val insertedRouteId = connectionHandle.createUpdate(RouteQueries.INSERT_WITH_CATEGORIES)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("personIdentifier", route.personIdentifier)
                .bind("geographicPoints", points)
                .bind("categories", route.categories!!.toTypedArray())
                .executeAndReturnGeneratedKeys("route_id")
                .mapTo(Int::class.java)
                .findFirst()

        if (!insertedRouteId.isPresent)
            throw InsertException()

        return insertedRouteId.get()
    }

    override fun update(connectionHandle: Handle, route: Route) {
        //convert list of geographicPoints to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.geographicPoints)

        //register the type converter
        connectionHandle.registerArrayType(CategoryArrayType())

        connectionHandle.createUpdate(RouteQueries.UPDATE_WITH_CATEGORIES)
                .bind("location", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("rating", route.rating)
                .bind("duration", 0) //TODO (time needs to be calculated by the server)
                .bind("geographicPoints", points)
                .bind("routeIdentifier", route.identifier)
                .bind("categories", route.categories!!.toTypedArray())
                .execute()
    }

    override fun delete(identifier: Int) {
        return connectionManager.deleteByIntId(RouteQueries.DELETE, identifier)
    }

    override fun updateElevation(identifier: Int, elevation: Double) {
        val handle = connectionManager.generateHandle()

        handle.createUpdate(RouteQueries.UPDATE_ELEVATION)
                .bind("elevation", elevation)
                .bind("identifier", identifier)
                .execute()
    }

    private fun nextPageExists(totalCount: Int, currentPage: Int): Boolean {
        return totalCount != 0 && totalCount > routesPerResult * currentPage
    }
}