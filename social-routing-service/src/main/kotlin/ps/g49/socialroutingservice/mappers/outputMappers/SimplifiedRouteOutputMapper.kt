package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class SimplifiedRouteOutputMapper : OutputMapper<SimplifiedRoute, SimplifiedRouteOutput>, OutputCollectionMapper<SimplifiedRoute, List<SimplifiedRouteOutput>> {
    override fun mapCollection(list: List<SimplifiedRoute>): List<SimplifiedRouteOutput>{
        return list.map { map(it) }
    }

    override fun map(from: SimplifiedRoute): SimplifiedRouteOutput {
        return SimplifiedRouteOutput(from.identifier, from.name, from.rating, OutputUtils.routeUrl(from.identifier))
    }
}