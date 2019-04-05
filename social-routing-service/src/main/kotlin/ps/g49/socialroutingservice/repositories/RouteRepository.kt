package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.model.Route

interface RouteRepository {

    fun findAll(): List<Route>

    fun findRouteById(id: Int): Route

    fun findPersonCreatedRoutes(identifier: Int): List<Route>

    fun create(connectionHandle : Handle, route: Route)

    fun delete(identifier: Int)

    fun update(connectionHandle : Handle, route: Route)

}