package ps.g49.socialroutingservice.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.utils.DtoBuilder
import ps.g49.socialroutingservice.models.inputModel.RouteInput
import ps.g49.socialroutingservice.mappers.outputMappers.RouteOutputMapper
import ps.g49.socialroutingservice.mappers.outputMappers.SimplifiedRouteOutputMapper
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.outputModel.RouteOutput
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutput
import ps.g49.socialroutingservice.services.RouteService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/api.sr/routes")
class RouteController(
        private val connectionManager: ConnectionManager,
        private val routeService: RouteService,
        private val routeOutputMapper: RouteOutputMapper,
        private val simplifiedRouteOutputMapper: SimplifiedRouteOutputMapper
) {

    @GetMapping
    fun findAllRoutes() = routeService.findAllRoutes()

    @GetMapping("/{identifier}")
    fun findRouteById(@PathVariable identifier: Int): ResponseEntity<RouteOutput> {
        val connectionHandle = connectionManager.generateHandle()
        val route = routeService.findRouteById(connectionHandle, identifier)
        connectionHandle.close()
        val output = routeOutputMapper.map(route)
        return ResponseEntity.ok(output)
    }

    @GetMapping("/search")
    fun searchRoute(@RequestParam params: HashMap<String, String>): ResponseEntity<List<SimplifiedRouteOutput>> {
        val searchDto = DtoBuilder.buildSearchDto(params)
        val routes = routeService.search(searchDto)
        val output = simplifiedRouteOutputMapper.mapSimplifiedRouteCollection(routes)
        return OutputUtils.ok(output)
    }

    @PostMapping
    fun createRoute(@RequestBody route: RouteInput) : ResponseEntity<Void>{
        val connectionHandle = connectionManager.generateHandle()

        val routeDto = DtoBuilder.buildRouteDto(route)
        val id = routeService.createRoute(connectionHandle, routeDto)

        connectionHandle.close()

        val headers = HttpHeaders()
        headers.set("Location", OutputUtils.routeUrl(id))

        return OutputUtils.ok(headers)
    }

    @PutMapping("/{identifier}")
    fun updateRoute(@PathVariable identifier : Int, @RequestBody route: RouteInput) : ResponseEntity<Void>{
        val connectionHandle = connectionManager.generateHandle()

        val routeDto = DtoBuilder.buildRouteDto(route, identifier)

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