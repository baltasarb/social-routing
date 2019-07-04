package ps.g49.socialroutingservice.repositories

import org.jdbi.v3.core.Handle
import ps.g49.socialroutingservice.models.domainModel.*

interface RouteRepository {

    fun findAll(): List<SimplifiedRoute>

    fun findByLocation(location: String, page : Int, categories : List<Category>?, duration : String?): SimplifiedRouteCollection

    fun findByCoordinates(location: String, page : Int, categories : List<Category>?, duration : String?) : SimplifiedRouteCollection

    fun findById(connectionHandle: Handle, id: Int): Route

    fun findPersonCreatedRoutes(identifier: Int, page : Int): SimplifiedRouteCollection

    fun create(connectionHandle: Handle, route: Route) : Int

    fun delete(identifier: Int)

    fun update(connectionHandle: Handle, route: Route)

    fun updateElevation(identifier : Int, elevation : Double)
}