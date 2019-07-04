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
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteCollection
import ps.g49.socialroutingservice.repositories.RouteRepository
import ps.g49.socialroutingservice.utils.sqlQueries.RouteQueries
import java.lang.Exception


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
        val insertPointOfInterestQuery = "INSERT INTO PointOfInterest(Identifier, Latitude, Longitude) VALUES(:name, :latitude, :longitude)" +
                "ON CONFLICT (Identifier) " +
                "DO NOTHING;"
        val pointOfInterestInsertBatch = connectionHandle.prepareBatch(insertPointOfInterestQuery)
        for (pointOfInterest in route.pointsOfInterest) {
            pointOfInterestInsertBatch
                    .bind("name", pointOfInterest.identifier)
                    .bind("latitude", pointOfInterest.latitude)
                    .bind("longitude", pointOfInterest.longitude)
                    .add()
        }
        val pointOfInterestBatchCount = pointOfInterestInsertBatch.execute().sum()//todo use value?

        //insert into image
        val insertImageQuery = "INSERT INTO Image (Reference) VALUES(:reference)" +
                "ON CONFLICT (Reference)" +
                "DO NOTHING;"
        connectionHandle.createUpdate(insertImageQuery)
                .bind("reference", route.imageReference)
                .execute()

        //Insert into route
        //convert list of points to json
        val jsonMapper = jacksonObjectMapper()
        val points = jsonMapper.writeValueAsString(route.points)
        //todo exception in type conversion

        val insertRouteQuery = "INSERT INTO Route (LocationIdentifier, Name, Description, Duration, DateCreated, Points, PersonIdentifier, ImageReference, Circular, Ordered) " +
                "VALUES (:locationIdentifier, :name, :description, :duration, CURRENT_DATE, to_json(:points), :personIdentifier, :imageReference, :circular, :ordered) " +
                "RETURNING Identifier AS route_id;"

        val insertedRouteIdentifier = connectionHandle.createUpdate(insertRouteQuery)
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

        //insert route points of interest
        val insertRoutePointOfInterestQuery = "INSERT INTO RoutePointOfInterest(RouteIdentifier, PointOfInterestIdentifier) VALUES(:routeIdentifier, :pointOfInterestIdentifier);"
        val routePointOfInterestInsertBatch = connectionHandle.prepareBatch(insertRoutePointOfInterestQuery)
        for (pointOfInterest in route.pointsOfInterest) {
            routePointOfInterestInsertBatch
                    .bind("routeIdentifier", insertedRouteIdentifier)
                    .bind("pointOfInterestIdentifier", pointOfInterest.identifier)
                    .add()
        }
        val routePointOfInterestBatchCount = routePointOfInterestInsertBatch.execute().sum()

        if (routePointOfInterestBatchCount != route.pointsOfInterest.size)//TODO CHANGE
            throw Exception("Route points of interest error inserting on route creation")

        //insert route categories
        val insertRouteCategoryQuery = "INSERT INTO RouteCategory(RouteIdentifier, CategoryName) VALUES(:routeIdentifier, :categoryName);"
        val routeCategoryBatch = connectionHandle.prepareBatch(insertRouteCategoryQuery)
        for (category in route.categories!!) {
            routeCategoryBatch
                    .bind("routeIdentifier", insertedRouteIdentifier)
                    .bind("categoryName", category.name)
                    .add()
        }

        val routeCategoryBatchInsertCount = routeCategoryBatch.execute().sum()

        if (routeCategoryBatchInsertCount != route.categories!!.size) //TODO CHANGE
            throw Exception("Categories insert error on route creation")

        return insertedRouteIdentifier.get()
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