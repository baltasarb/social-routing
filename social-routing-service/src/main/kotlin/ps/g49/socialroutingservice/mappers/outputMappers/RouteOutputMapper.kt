package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.models.outputModel.CategoryOutput
import ps.g49.socialroutingservice.models.outputModel.RouteOutput
import ps.g49.socialroutingservice.models.outputModel.RouteOutputCollection
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class RouteOutputMapper : OutputMapper<Route, RouteOutput>, OutputCollectionMapper<Route, RouteOutputCollection> {

    override fun mapCollection(list: List<Route>): RouteOutputCollection {
        return RouteOutputCollection(list.map { map(it) })
    }

    override fun map(from: Route): RouteOutput = RouteOutput(
            identifier = from.identifier!!,
            location = from.location!!,
            name = from.name,
            description = from.description!!,
            rating = from.rating!!,
            duration = from.duration!!,
            dateCreated = from.dateCreated!!,
            points = from.points!!,
            ownerUrl = OutputUtils.personUrl(from.personIdentifier),
            categories = from.categories!!.map { CategoryOutput(it.name) }
    )

}