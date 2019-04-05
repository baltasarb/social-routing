package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.inputModel.RouteInput
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class RouteService(private val routeRepository: RouteRepository) {

    fun findAllRoutes() = routeRepository.findAll()

    fun findRouteById(id: Int) = routeRepository.findRouteById(id)

    fun createRoute(connectionHandle : Handle, routeInput: RouteInput) {
        val route = Route(routeInput)
        routeRepository.create(connectionHandle, route)
    }

    fun deleteRoute(identifier: Int) = routeRepository.delete(identifier)

    fun updateRoute(connectionHandle : Handle, routeInput: RouteInput) {
        val route = Route(routeInput)
        routeRepository.update(connectionHandle, route)
    }
}