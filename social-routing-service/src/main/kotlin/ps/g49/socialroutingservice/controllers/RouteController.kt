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
import ps.g49.socialroutingservice.models.requests.DeleteRouteRequest
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

    /**
     * endpoint used to retrieve a single route
     * @param identifier the identifier of the route to be retrieved
     * @return an object containing the information of a single route
     */
    @GetMapping("/{identifier}")
    fun findRouteById(@PathVariable identifier: Int): ResponseEntity<RouteOutput> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use { handle ->
            val route = routeService.findRouteById(handle, identifier)
            val output = routeOutputMapper.map(route)
            return ResponseEntity.ok(output)
        }
    }

    /**
     * endpoint used to search for a collection of routes that satisfy certain parameters
     * @param params the parameters of the query which might me categories, duration, page, location and coordinates
     * @return a collection of simplified routes
     */
    @GetMapping("/search")
    fun searchRoute(@RequestParam params: HashMap<String, String>): ResponseEntity<SimplifiedRouteCollectionOutput> {
        val searchRequest = SearchRequest.build(params)
        val routes = routeService.search(searchRequest)
        val output = simplifiedRouteCollectionOutputMapper.mapSearch(routes, params)
        return OutputUtils.ok(output)
    }

    /**
     * endpoint used to create a new route
     * @param route the information of the route to be created
     */
    @PostMapping
    fun createRoute(@RequestBody route: RouteInput, @RequestAttribute personIdentifier: Int): ResponseEntity<Void> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use {
            val routeRequest = RouteRequest.build(route, 100/*personIdentifier*/)
            val routeIdentifier = routeService.createRoute(it, routeRequest)
            val headers = HttpHeaders()
            headers.set("Location", OutputUtils.routeUrl(routeIdentifier))
            return OutputUtils.ok(headers)
        }
    }

    /**
     * endpoint used to update a single route
     * @param route the information of the route to be updated
     */
    @PutMapping("/{identifier}")
    fun updateRoute(@PathVariable identifier: Int, @RequestBody route: RouteInput, @RequestAttribute personIdentifier: Int): ResponseEntity<Void> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use {
            val routeRequest = RouteRequest.build(route, 100, identifier)

            routeService.updateRoute(it, routeRequest)

            return OutputUtils.ok()
        }
    }

    /**
     * endpoint used to delete an existing route, a user can only delete his own routes
     * @param identifier
     */
    @DeleteMapping("/{identifier}")
    fun deleteRoute(@PathVariable identifier: Int, @RequestAttribute personIdentifier: Int): ResponseEntity<Void> {
        val deleteRouteRequest = DeleteRouteRequest.build(identifier, personIdentifier)
        routeService.deleteRoute(deleteRouteRequest)
        return OutputUtils.ok()
    }

}