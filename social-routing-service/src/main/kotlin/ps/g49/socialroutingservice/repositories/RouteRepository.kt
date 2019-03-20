package ps.g49.socialroutingservice.repositories

import org.springframework.data.repository.CrudRepository
import ps.g49.socialroutingservice.model.Route

interface RouteRepository : CrudRepository<Route, String> {
}