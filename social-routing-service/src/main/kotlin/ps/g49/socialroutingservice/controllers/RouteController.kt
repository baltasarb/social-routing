package ps.g49.socialroutingservice.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.ForbiddenRequestException
import ps.g49.socialroutingservice.models.inputModel.RouteInput
import ps.g49.socialroutingservice.mappers.outputMappers.RouteOutputMapper
import ps.g49.socialroutingservice.mappers.outputMappers.SimplifiedRouteCollectionOutputMapper
import ps.g49.socialroutingservice.models.outputModel.RouteOutput
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteCollectionOutput
import ps.g49.socialroutingservice.models.requests.RouteRequest
import ps.g49.socialroutingservice.models.requests.SearchRequest
import ps.g49.socialroutingservice.services.RouteService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/routes")
class RouteController(
        private val connectionManager: ConnectionManager,
        private val routeService: RouteService,
        private val routeOutputMapper: RouteOutputMapper,
        private val simplifiedRouteCollectionOutputMapper: SimplifiedRouteCollectionOutputMapper
) {

    @GetMapping("/{identifier}")
    fun findRouteById(@PathVariable identifier: Int): ResponseEntity<RouteOutput> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use { handle ->
            val route = routeService.findRouteById(handle, identifier)
            val output = routeOutputMapper.map(route)
            return ResponseEntity.ok(output)
        }
    }

    @GetMapping("/search")
    fun searchRoute(@RequestParam params: HashMap<String, String>): ResponseEntity<SimplifiedRouteCollectionOutput> {
        val searchRequest = SearchRequest.build(params)
        val routes = routeService.search(searchRequest)
        val output = simplifiedRouteCollectionOutputMapper.mapSearch(routes, params)
        return OutputUtils.ok(output)
    }

    @PostMapping
    fun createRoute(@RequestBody route: RouteInput /*,@RequestAttribute personIdentifier: Int*/): ResponseEntity<Void> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use{
            val routeRequest = RouteRequest.build(route, 100/*personIdentifier*/)
            val routeIdentifier = routeService.createRoute(it, routeRequest)
            val headers = HttpHeaders()
            headers.set("Location", OutputUtils.routeUrl(routeIdentifier))
            return OutputUtils.ok(headers)
        }
    }

    @PutMapping("/{identifier}")
    fun updateRoute(@PathVariable identifier: Int, @RequestBody route: RouteInput/*@RequestAttribute personIdentifier: Int*/): ResponseEntity<Void> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use{
            val routeRequest = RouteRequest.build(route, 100, identifier)

            routeService.updateRoute(it, routeRequest)

            return OutputUtils.ok()
        }
    }

    @DeleteMapping("/{identifier}")
    fun deleteRoute(@PathVariable identifier: Int/*@RequestAttribute personIdentifier: Int*/): ResponseEntity<Void> {
        if (identifier != 1)
            throw ForbiddenRequestException()
        routeService.deleteRoute(identifier)
        return OutputUtils.ok()
    }

}