package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.requests.RouteRequest
import ps.g49.socialroutingservice.models.requests.SearchRequest
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class RouteService(private val routeRepository: RouteRepository, private val routeMapper: RouteMapper) {

    fun findAllRoutes() = routeRepository.findAll()

    fun findRouteById(connectionHandle: Handle, id: Int) = routeRepository.findById(connectionHandle, id)

    fun createRoute(connectionHandle: Handle, routeRequest: RouteRequest) : Int {
        val route = routeMapper.map(routeRequest)
        return routeRepository.create(connectionHandle, route)
    }

    fun deleteRoute(identifier: Int) = routeRepository.delete(identifier)

    fun updateRoute(connectionHandle: Handle, routeRequest: RouteRequest) {
        val route = routeMapper.map(routeRequest)
        routeRepository.update(connectionHandle, route)
    }

    fun search(searchRequest: SearchRequest): List<SimplifiedRoute> {
        return routeRepository.findAllByParameter(searchRequest.location!!, searchRequest.page)
    }

}