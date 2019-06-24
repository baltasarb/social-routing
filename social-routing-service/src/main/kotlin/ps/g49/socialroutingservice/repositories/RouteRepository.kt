package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteCollection
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteWithCount

interface RouteRepository {

    fun findAll(): List<SimplifiedRoute>

    fun findAllByParameter(parameter: String, page : Int): SimplifiedRouteCollection

    fun findById(connectionHandle: Handle, id: Int): Route

    fun findPersonCreatedRoutes(identifier: Int, page : Int): SimplifiedRouteCollection

    fun create(connectionHandle: Handle, route: Route) : Int

    fun delete(identifier: Int)

    fun update(connectionHandle: Handle, route: Route)

}