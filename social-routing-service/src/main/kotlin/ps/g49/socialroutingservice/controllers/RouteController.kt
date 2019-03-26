package ps.g49.socialroutingservice.controllers

import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.services.RouteService

@RestController
@RequestMapping("/api.sr/routes")
class RouteController (private val routeService: RouteService){

    @GetMapping
    fun findAllRoutes() = routeService.findAllRoutes()

    @GetMapping("/{id}")
    fun findRouteById(@PathVariable id : String) = routeService.findRouteById(id)

}