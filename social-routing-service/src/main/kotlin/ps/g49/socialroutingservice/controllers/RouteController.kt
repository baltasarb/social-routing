package ps.g49.socialroutingservice.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.models.inputModel.RouteInput
import ps.g49.socialroutingservice.mappers.dtoMappers.RouteDtoMapper
import ps.g49.socialroutingservice.mappers.dtoMappers.SearchDtoMapper
import ps.g49.socialroutingservice.mappers.outputMappers.RouteOutputMapper
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.outputModel.RouteOutput
import ps.g49.socialroutingservice.services.RouteService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/api.sr/routes")
class RouteController(
        private val connectionManager: ConnectionManager,
        private val routeService: RouteService,
        private val mapper: RouteDtoMapper,
        private val routeDtoMapper: RouteDtoMapper,
        private val searchDtoMapper: SearchDtoMapper,
        private val routeOutputMapper: RouteOutputMapper
) {

    @GetMapping
    fun findAllRoutes() = routeService.findAllRoutes()

    @GetMapping("/{identifier}")
    fun findRouteById(@PathVariable identifier: Int): ResponseEntity<RouteOutput> {
        val route = routeService.findRouteById(identifier)
        val output = routeOutputMapper.map(route)
        return ResponseEntity.ok(output)
    }

    @GetMapping("/search")
    fun searchRoute(@RequestParam params: HashMap<String, String>): ResponseEntity<List<Route>> {
        val searchDto = searchDtoMapper.map(params)
        val routes = routeService.search(searchDto)
        return OutputUtils.ok(routes)
    }

    @PostMapping
    fun createRoute(@RequestBody route: RouteInput) : ResponseEntity<Void>{
        val connectionHandle = connectionManager.generateHandle()
        val routeDto = routeDtoMapper.map(route)
        val id = routeService.createRoute(connectionHandle, routeDto)
        connectionHandle.close()

        val headers = HttpHeaders()
        headers.set("Location", OutputUtils.personUrl(id))

        return OutputUtils.ok(headers)
    }

    @PutMapping("/{identifier}")
    fun updateRoute(@RequestBody route: RouteInput) : ResponseEntity<Void>{
        val connectionHandle = connectionManager.generateHandle()
        val routeDto = routeDtoMapper.map(route)
        routeService.updateRoute(connectionHandle, routeDto)
        connectionHandle.close()
        return OutputUtils.ok()
    }

    @DeleteMapping("/{identifier}")
    fun deleteRoute(@PathVariable identifier: Int) : ResponseEntity<Void>{
        routeService.deleteRoute(identifier)
        return OutputUtils.ok()
    }

}