package ps.g49.socialroutingservice.repositories

import ps.g49.socialroutingservice.model.Route

interface RouteRepository {

    fun findAll(): List<Route>

    fun findRouteById(id: String): Route

}