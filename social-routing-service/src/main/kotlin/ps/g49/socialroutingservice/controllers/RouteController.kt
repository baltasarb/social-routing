package ps.g49.socialroutingservice.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.inputModel.RouteInput
import ps.g49.socialroutingservice.mappers.dtoMappers.RouteDtoMapper
import ps.g49.socialroutingservice.mappers.dtoMappers.SearchDtoMapper
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.services.RouteService

@RestController
@RequestMapping("/api.sr/routes")
class RouteController(private val connectionManager: ConnectionManager, private val routeService: RouteService, private val routeDtoMapper: RouteDtoMapper, private val searchDtoMapper: SearchDtoMapper) {

    @GetMapping
    fun findAllRoutes() = routeService.findAllRoutes()

    @GetMapping("/{identifier}")
    fun findRouteById(@PathVariable identifier: Int) = routeService.findRouteById(identifier)

    @GetMapping("/search")
    fun searchRoute(@RequestParam params: HashMap<String, String>): List<Route> {
        val searchDto = searchDtoMapper.map(params)
        return routeService.search(searchDto)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRoute(@RequestBody route: RouteInput) {
        val connectionHandle = connectionManager.generateHandle()
        val routeDto = routeDtoMapper.map(route)
        routeService.createRoute(connectionHandle, routeDto)
        connectionHandle.close()
    }

    @PutMapping("/{identifier}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRoute(@RequestBody route: RouteInput) {
        val connectionHandle = connectionManager.generateHandle()
        val routeDto = routeDtoMapper.map(route)
        routeService.updateRoute(connectionHandle, routeDto)
        connectionHandle.close()
    }

    @DeleteMapping("/{identifier}")
    fun deleteRoute(@PathVariable identifier: Int) = routeService.deleteRoute(identifier)

}