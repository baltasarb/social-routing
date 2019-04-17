package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.dtos.RouteDto
import ps.g49.socialroutingservice.inputModel.RouteInput
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class RouteService(private val routeRepository: RouteRepository, private val routeMapper: RouteMapper) {

    fun findAllRoutes() = routeRepository.findAll()

    fun findRouteById(id: Int) = routeRepository.findRouteById(id)

    fun createRoute(connectionHandle: Handle, routeInput: RouteInput) {
        val route = Route(routeInput)
        routeRepository.create(connectionHandle, route)
    }

    fun deleteRoute(identifier: Int) = routeRepository.delete(identifier)

    fun updateRoute(connectionHandle: Handle, routeInput: RouteInput) {
        val route = Route(routeInput)
        routeRepository.update(connectionHandle, route)
    }

    fun search(routeDto: RouteDto) {
        val route = routeMapper.map(routeDto)
        routeRepository.findAllByParameter(route.location)
    }

}