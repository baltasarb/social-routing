package ps.g49.socialroutingservice.repositories.implementations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.ForbiddenRequestException
import ps.g49.socialroutingservice.exceptions.InsertException
import ps.g49.socialroutingservice.exceptions.ResourceNotFoundException
import ps.g49.socialroutingservice.exceptions.RouteCategoriesRequiredException
import ps.g49.socialroutingservice.mappers.modelMappers.RedundantRouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteWithCountMapper
import ps.g49.socialroutingservice.models.domainModel.*
import ps.g49.socialroutingservice.models.requests.DeleteRouteRequest
import ps.g49.socialroutingservice.models.requests.SearchRequest
import ps.g49.socialroutingservice.repositories.RouteRepository
import ps.g49.socialroutingservice.utils.sqlQueries.*
import java.lang.Exception

/**
 * repository used to establish a connection to the database regarding every route related transaction
 */
@Component
class RouteRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val routeMapper: RouteMapper,
        private val redundantRouteMapper: RedundantRouteMapper,
        private val simplifiedRouteWithCountMapper: SimplifiedRouteWithCountMapper
) : RouteRepository {

    companion object {
        private const val ROUTES_PER_RESULT = 10

        // the user surrounding area, goes from 0 to 1km by default
        private const val LOWER_BOUND_RADIUS = 0.toDouble()
        private const val UPPER_BOUND_RADIUS = 1000.toDouble()
    }

    /**
     * used to get the details of a route by its identifier
     * @param id the identifier of the route
     * @return A route object containing the details of a route
     */
    override fun findById(connectionHandle: Handle, id: Int): Route {
        val redundantRouteList = connectionHandle.select(RouteQueries.SELECT_BY_ID)
                .bind("routeIdentifier", id)
                .map(redundantRouteMapper)
                .list()

        if (redundantRouteList.isEmpty())
            throw ResourceNotFoundException()
        return routeMapper.buildRouteFromRedundantRouteList(redundantRouteList)
    }

    /**
     * retrieves a collection of routes that belong to a chosen location
     */
    override fun findByLocation(searchRequest: SearchRequest): SimplifiedRouteCollection {
        val params = hashMapOf<String, Any>("locationIdentifier" to searchRequest.location)
        val categories = searchRequest.categories
        val duration = searchRequest.duration
        val page = searchRequest.page

        for (i in categories.indices) {
            params["category$i"] = categories[i].name.toLowerCase()
        }

        params["duration"] = duration

        val result = connectionManager.findManyWithPagination(
                ROUTES_PER_RESULT,
                RouteQueries.getSearchByLocationQuery(categories),
                simplifiedRouteWithCountMapper,
                page,
                params
        )

        if (result.isEmpty())
            throw ResourceNotFoundException()

        val totalCount = result.first().count

        return SimplifiedRouteCollection(
                result.map { SimplifiedRoute(it.identifier, it.name, it.rating, it.imageReference, it.personIdentifier) },
                if (nextPageExists(totalCount, page)) page + 1 else null
        )
    }

    /**
     * retrieves a collection of routes that are close to the received user coordinates
     */
    override fun findByCoordinates(searchRequest: SearchRequest): SimplifiedRouteCollection {
        val params = hashMapOf<String, Any>(
                "userLatitude" to searchRequest.coordinates!!.latitude,
                "userLongitude" to searchRequest.coordinates.longitude,
                "upperBoundRadius" to UPPER_BOUND_RADIUS,
                "lowerBoundRadius" to LOWER_BOUND_RADIUS
        )
        val categories = searchRequest.categories
        val duration = searchRequest.duration
        val page = searchRequest.page

        for (i in categories.indices) {
            params["category$i"] = categories[i].name.toLowerCase()
        }

        params["duration"] = duration

        val query = RouteQueries.getSearchByCoordinatesQuery(categories)
        val result = connectionManager.findManyWithPagination(
                ROUTES_PER_RESULT,
                query,
                simplifiedRouteWithCountMapper,
                page,
                params
        )

        if (result.isEmpty())
            throw ResourceNotFoundException()

        val totalCount = result.first().count

        return SimplifiedRouteCollection(
                result.map { SimplifiedRoute(it.identifier, it.name, it.rating, it.imageReference, it.personIdentifier) },
                if (nextPageExists(totalCount, page)) page + 1 else null
        )
    }

    /**
     * retrieves a persons created routes
     * @param identifier the identifier of the owner of the routes
     */
    override fun findPersonCreatedRoutes(identifier: Int, page: Int): SimplifiedRouteCollection {
        val params = hashMapOf<String, Any>("personIdentifier" to identifier)

        val result = connectionManager.findManyWithPagination(
                ROUTES_PER_RESULT,
                RouteQueries.SELECT_MANY_BY_OWNER_WITH_PAGINATION_AND_COUNT,
                simplifiedRouteWithCountMapper,
                page,
                params
        )

        if (result.isEmpty())
            throw ResourceNotFoundException()

        val totalCount = result.first().count

        return SimplifiedRouteCollection(
                result.map { SimplifiedRoute(it.identifier, it.name, it.rating, it.imageReference, it.personIdentifier) },
                if (nextPageExists(totalCount, page)) page + 1 else null
        )
    }

    override fun create(connectionHandle: Handle, route: Route): Int {
        //insert into points of interest
        executeInsertPointOfInterestBatch(connectionHandle, route.pointsOfInterest)

        //insert into image
        connectionHandle.createUpdate(ImageQueries.INSERT)
                .bind("reference", route.imageReference)
                .execute()

        //insert route
        val insertedRouteIdentifier = insertRoute(route, connectionHandle)

        //insert route points of interest
        executeRoutePointsOfInterestInsertBatch(connectionHandle, route, insertedRouteIdentifier)

        //insert route categories
        executeRouteCategoriesInsertBatch(connectionHandle, route, insertedRouteIdentifier)

        return insertedRouteIdentifier
    }

    override fun update(connectionHandle: Handle, route: Route) {
        val routeIdentifier = route.identifier!!

        updateRoute(route, connectionHandle)

        //delete route categories
        connectionHandle.createUpdate(RouteCategoryQueries.DELETE)
                .bind("routeIdentifier", routeIdentifier)
                .execute()

        //update route categories
        executeRouteCategoriesInsertBatch(connectionHandle, route, routeIdentifier)

        //delete points of interest
        connectionHandle.createUpdate(RoutePointOfInterestQueries.DELETE)
                .bind("routeIdentifier", routeIdentifier)
                .execute()

        //update points of interest
        executeInsertPointOfInterestBatch(connectionHandle, route.pointsOfInterest)
    }

    private fun updateRoute(route: Route, connectionHandle: Handle) {
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)

        connectionHandle.createUpdate(RouteQueries.UPDATE)
                .bind("identifier", route.identifier)
                .bind("locationIdentifier", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("rating", route.rating)
                .bind("duration", route.duration)
                .bind("circular", route.isCircular)
                .bind("ordered", route.isOrdered)
                .bind("imageReference", route.imageReference)
                .bind("points", points)
                .execute()
    }

    override fun delete(deleteRouteRequest: DeleteRouteRequest) {
        val connectionHandle = connectionManager.generateHandle()
        var numberOfRowsAffected: Int = 0
        connectionHandle.use {
            numberOfRowsAffected = it.execute(RouteQueries.DELETE, deleteRouteRequest.identifier, deleteRouteRequest.personIdentifier)
        }
        if (numberOfRowsAffected == 0) {
            throw ForbiddenRequestException()
        }
    }

    private fun nextPageExists(totalCount: Int, currentPage: Int): Boolean {
        return totalCount != 0 && totalCount > ROUTES_PER_RESULT * currentPage
    }

    /**
     * inserts multiple points of interest in a batch into the database
     */
    private fun executeInsertPointOfInterestBatch(connectionHandle: Handle, pointsOfInterest: List<PointOfInterest>) {
        //insert into points of interest
        if (pointsOfInterest.isEmpty()) return
        val pointOfInterestInsertBatch = connectionHandle.prepareBatch(PointOfInterestQueries.INSERT)
        for (pointOfInterest in pointsOfInterest) {
            pointOfInterestInsertBatch
                    .bind("identifier", pointOfInterest.identifier)
                    .bind("latitude", pointOfInterest.latitude)
                    .bind("longitude", pointOfInterest.longitude)
                    .add()
        }
        pointOfInterestInsertBatch.execute()
    }

    /**
     * inserts multiple categories in a batch into the database
     */
    private fun executeRouteCategoriesInsertBatch(connectionHandle: Handle, route: Route, insertedRouteIdentifier: Int) {
        val categories = route.categories
        if (categories == null || categories.isEmpty())
            throw RouteCategoriesRequiredException()
        val routeCategoryBatch = connectionHandle.prepareBatch(RouteCategoryQueries.INSERT)
        for (category in route.categories!!) {
            routeCategoryBatch
                    .bind("routeIdentifier", insertedRouteIdentifier)
                    .bind("categoryName", category.name)
                    .add()
        }

        val routeCategoryBatchInsertCount = routeCategoryBatch.execute().size

        if (routeCategoryBatchInsertCount != route.categories!!.size) //TODO CHANGE
            throw Exception("Categories insert error on route creation")
    }

    /**
     * inserts multiple routeCategories in a batch into the database
     */
    private fun executeRoutePointsOfInterestInsertBatch(connectionHandle: Handle, route: Route, insertedRouteIdentifier: Int) {
        if (route.pointsOfInterest.isEmpty()) return
        val routePointOfInterestInsertBatch = connectionHandle.prepareBatch(RoutePointOfInterestQueries.INSERT)
        for (pointOfInterest in route.pointsOfInterest) {
            routePointOfInterestInsertBatch
                    .bind("routeIdentifier", insertedRouteIdentifier)
                    .bind("pointOfInterestIdentifier", pointOfInterest.identifier)
                    .add()
        }
        val routePointOfInterestBatchCount = routePointOfInterestInsertBatch.execute().size

        if (routePointOfInterestBatchCount != route.pointsOfInterest.size)//TODO CHANGE
            throw Exception("Route points of interest error inserting on route creation")
    }

    private fun insertRoute(route: Route, connectionHandle: Handle): Int {
        //Insert into route
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)
        //todo exception in type conversion

        val insertedRouteIdentifier = connectionHandle.createUpdate(RouteQueries.INSERT)
                .bind("locationIdentifier", route.location)
                .bind("name", route.name)
                .bind("description", route.description)
                .bind("duration", route.duration)
                .bind("personIdentifier", route.personIdentifier)
                .bind("points", points)
                .bind("imageReference", route.imageReference)
                .bind("circular", route.isCircular)
                .bind("ordered", route.isOrdered)
                .executeAndReturnGeneratedKeys("route_id")
                .mapTo(Int::class.java)
                .findFirst()

        if (!insertedRouteIdentifier.isPresent)
            throw InsertException()

        return insertedRouteIdentifier.get()
    }

}