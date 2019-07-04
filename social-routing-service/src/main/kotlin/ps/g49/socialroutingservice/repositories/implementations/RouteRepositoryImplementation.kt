package ps.g49.socialroutingservice.repositories.implementations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.InsertException
import ps.g49.socialroutingservice.exceptions.ResourceNotFoundException
import ps.g49.socialroutingservice.mappers.modelMappers.RedundantRouteMapper
import ps.g49.socialroutingservice.mappers.sqlArrayTypeMappers.CategoryArrayType
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteMapper
import ps.g49.socialroutingservice.mappers.modelMappers.SimplifiedRouteWithCountMapper
import ps.g49.socialroutingservice.models.domainModel.*
import ps.g49.socialroutingservice.repositories.RouteRepository
import ps.g49.socialroutingservice.utils.sqlQueries.*
import java.lang.Exception
import java.util.*


@Component
class RouteRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val routeMapper: RouteMapper,
        private val redundantRouteMapper: RedundantRouteMapper,
        private val simplifiedRouteMapper: SimplifiedRouteMapper,
        private val simplifiedRouteWithCountMapper: SimplifiedRouteWithCountMapper
) : RouteRepository {

    private val routesPerResult = 2//TODO CHANGE TO 10

    override fun findById(connectionHandle: Handle, id: Int): Route {
        val redundantRouteList = connectionHandle.select(RouteQueries.SELECT_BY_ID)
                .bind("routeIdentifier", id)
                .map(redundantRouteMapper)
                .list()

        return routeMapper.buildRouteFromRedundantRouteList(redundantRouteList)
    }

    override fun findAll(): List<SimplifiedRoute> {
        return connectionManager.findMany(RouteQueries.SELECT_MANY, simplifiedRouteMapper)
    }

    override fun findByLocation(location: String, page: Int, categories: List<Category>?, duration: String?): SimplifiedRouteCollection {
        val params = hashMapOf<String, Any>("locationIdentifier" to location)

        if (categories != null) {
            for (i in categories.indices) {
                params["category$i"] = categories[i].name
            }
        }

        if (duration != null) {
            params["duration"] = duration
        }

        val result = connectionManager.findManyWithPagination(
                routesPerResult,
                RouteQueries.getSearchByLocationQuery(categories, duration),
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

    override fun findByCoordinates(location: String, page: Int, categories: List<Category>?, duration: String?): SimplifiedRouteCollection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun executeInsertPointOfInterestBatch(connectionHandle: Handle, pointsOfInterest : List<PointOfInterest>) : Int{
        //insert into points of interest
        val pointOfInterestInsertBatch = connectionHandle.prepareBatch(PointOfInterestQueries.INSERT)
        for (pointOfInterest in pointsOfInterest) {
            pointOfInterestInsertBatch
                    .bind("identifier", pointOfInterest.identifier)
                    .bind("latitude", pointOfInterest.latitude)
                    .bind("longitude", pointOfInterest.longitude)
                    .add()
        }
        return pointOfInterestInsertBatch.execute().size
    }

    private fun executeRouteCategoriesInsertBatch(connectionHandle: Handle, route: Route, insertedRouteIdentifier: Int) {
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

    private fun executeRoutePointsOfInterestInsertBatch(connectionHandle: Handle, route: Route, insertedRouteIdentifier: Int) {
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