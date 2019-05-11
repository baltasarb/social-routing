package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutputCollection
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class SimplifiedRouteOutputMapper : OutputMapper<SimplifiedRoute, SimplifiedRouteOutput>, OutputCollectionMapper<SimplifiedRoute, SimplifiedRouteOutputCollection> {

    override fun mapCollection(list: List<SimplifiedRoute>): SimplifiedRouteOutputCollection {
        return SimplifiedRouteOutputCollection(list.map { map(it) })
    }

    override fun map(from: SimplifiedRoute): SimplifiedRouteOutput {
        val id = from.identifier
        return SimplifiedRouteOutput(
                identifier = id,
                name = from.name,
                rating = from.rating,
                routeUrl = OutputUtils.routeUrl(id)
        )
    }

}