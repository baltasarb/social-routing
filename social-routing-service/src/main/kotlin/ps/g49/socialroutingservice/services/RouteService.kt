package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.dtos.RouteDto
import ps.g49.socialroutingservice.models.dtos.SearchDto
import ps.g49.socialroutingservice.mappers.modelMappers.RouteMapper
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class RouteService(private val routeRepository: RouteRepository, private val routeMapper: RouteMapper) {

    fun findAllRoutes() = routeRepository.findAll()

    fun findRouteById(id: Int) = routeRepository.findRouteById(id)

    fun createRoute(connectionHandle: Handle, routeDto: RouteDto) : Int {
        val route = routeMapper.map(routeDto)
        return routeRepository.create(connectionHandle, route)
    }

    fun deleteRoute(identifier: Int) = routeRepository.delete(identifier)

    fun updateRoute(connectionHandle: Handle, routeDto: RouteDto) {
        val route = routeMapper.map(routeDto)
        routeRepository.update(connectionHandle, route)
    }

    fun search(searchDto: SearchDto): List<Route> {
        return routeRepository.findAllByParameter(searchDto.location!!)
    }

}