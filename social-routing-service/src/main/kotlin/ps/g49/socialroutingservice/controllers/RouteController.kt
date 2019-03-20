package ps.g49.socialroutingservice.controllers

import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.model.Point
import ps.g49.socialroutingservice.model.Route
import ps.g49.socialroutingservice.repositories.RouteRepository

@RestController
@RequestMapping("/api.sr/routes")
class RouteController(private val routeRepository: RouteRepository) {

    @GetMapping("/{id}")
    fun getRouteById(@PathVariable id: String) = routeRepository.findById(id);


    /*@PostMapping*/
    @GetMapping("/create")
    fun createRoute(/*@RequestParam latitude: Double, @RequestParam longitude: Double, @RequestParam name: String*/) : String {
        val points = setOf(
                Point(1.0, 1.1),
                Point(2.0, 2.1)
        )
        val route = Route("testRoute", points)
        routeRepository.save(route)
        return route.toString()
    }
}