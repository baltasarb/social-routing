package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class SimplifiedRouteOutputMapper : OutputMapper<SimplifiedRoute, SimplifiedRouteOutput> {

    override fun map(from: SimplifiedRoute): SimplifiedRouteOutput {
        val id = from.identifier
        return SimplifiedRouteOutput(
                identifier = id,
                name = from.name,
                rating = from.rating,
                routeUrl = OutputUtils.routeUrl(id)
        )
    }

    fun mapSimplifiedRouteCollection(collection: List<SimplifiedRoute>): List<SimplifiedRouteOutput> {
        return collection.map { map(it) }
    }

}