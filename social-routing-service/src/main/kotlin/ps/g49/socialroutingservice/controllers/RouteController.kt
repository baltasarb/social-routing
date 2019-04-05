package ps.g49.socialroutingservice.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.inputModel.RouteInput
import ps.g49.socialroutingservice.services.RouteService

@RestController
@RequestMapping("/api.sr/routes")
class RouteController(private val connectionManager: ConnectionManager, private val routeService: RouteService) {

    @GetMapping
    fun findAllRoutes() = routeService.findAllRoutes()

    @GetMapping("/{identifier}")
    fun findRouteById(@PathVariable identifier: Int) = routeService.findRouteById(identifier)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRoute(@RequestBody route: RouteInput) {
        val connectionHandle = connectionManager.generateHandle()
        routeService.createRoute(connectionHandle, route)
        connectionHandle.close()
    }

    @PutMapping("/{identifier}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRoute(@RequestBody route: RouteInput) {
        val connectionHandle = connectionManager.generateHandle()
        routeService.updateRoute(connectionHandle, route)
        connectionHandle.close()
    }

    @DeleteMapping("/{identifier}")
    fun deleteRoute(@PathVariable identifier: Int) = routeService.deleteRoute(identifier)

}