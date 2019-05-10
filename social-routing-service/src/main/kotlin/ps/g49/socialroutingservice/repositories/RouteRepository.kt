package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute

interface RouteRepository {

    fun findAll(): List<SimplifiedRoute>

    fun findAllByParameter(parameter: String): List<SimplifiedRoute>

    fun findById(connectionHandle: Handle, id: Int): Route

    fun findPersonCreatedRoutes(identifier: Int): List<SimplifiedRoute>

    fun create(connectionHandle: Handle, route: Route) : Int

    fun delete(identifier: Int)

    fun update(connectionHandle: Handle, route: Route)

}