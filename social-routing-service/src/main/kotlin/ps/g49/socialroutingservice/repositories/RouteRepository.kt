package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.*
import ps.g49.socialroutingservice.models.requests.DeleteRouteRequest
import ps.g49.socialroutingservice.models.requests.SearchRequest

interface RouteRepository {

    fun findByLocation(searchRequest: SearchRequest): SimplifiedRouteCollection

    fun findByCoordinates(searchRequest: SearchRequest) : SimplifiedRouteCollection

    fun findById(connectionHandle: Handle, id: Int): Route

    fun findPersonCreatedRoutes(identifier: Int, page : Int): SimplifiedRouteCollection

    fun create(connectionHandle: Handle, route: Route) : Int

    fun delete(deleteRouteRequest: DeleteRouteRequest)

    fun update(connectionHandle: Handle, route: Route)

}