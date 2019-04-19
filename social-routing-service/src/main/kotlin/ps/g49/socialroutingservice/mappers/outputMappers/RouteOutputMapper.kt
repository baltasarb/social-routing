package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.outputModel.RouteOutput
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class RouteOutputMapper : OutputMapper<Route, RouteOutput> {

    override fun map(from: Route): RouteOutput = RouteOutput(
            identifier = from.identifier!!,
            location = from.location!!,
            name = from.name,
            description = from.description!!,
            rating = from.rating!!,
            duration = from.duration!!,
            dateCreated = from.dateCreated!!,
            points = from.points!!,
            ownerUrl = OutputUtils.personUrl(from.personIdentifier)
    )

    fun mapSimplifiedRouteCollection(collection: List<Route>): List<SimplifiedRouteOutput> {
        return collection.map { mapSimplifiedRoute(it) }
    }

    fun mapSimplifiedRoute(from: Route): SimplifiedRouteOutput {
        val id = from.identifier!!
        return SimplifiedRouteOutput(
                identifier = id,
                name = from.name,
                rating = from.rating!!,
                routeUrl = OutputUtils.routeUrl(id)
        )
    }


}