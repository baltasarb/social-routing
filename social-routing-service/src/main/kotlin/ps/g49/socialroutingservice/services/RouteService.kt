package ps.g49.socialroutingservice.services

import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class RouteService(private val routeRepository: RouteRepository) {

    fun findAllRoutes() = routeRepository.findAll()

    fun findRouteById(id: String) = routeRepository.findRouteById(id)
}