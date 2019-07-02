package ps.g49.socialroutingservice.models.requests

import ps.g49.socialroutingservice.models.domainModel.GeographicPoint
import ps.g49.socialroutingservice.models.inputModel.RouteInput
import java.util.*

data class RouteRequest(
        var identifier: Int? = null,
        var location: String,
        var name: String,
        var description: String,
        var geographicPoints: List<GeographicPoint>,
        var personIdentifier: Int,
        var rating: Double? = null,
        var dateCreated: Date? = null,
        var duration : Int? = null,
        var categories : List<CategoryRequest>
){
    companion object{
        fun build(routeInput: RouteInput, id: Int? = null): RouteRequest {
            return RouteRequest(
                    identifier = id,
                    location = routeInput.location,
                    name = routeInput.name,
                    description = routeInput.description,
                    rating = routeInput.rating,
                    geographicPoints = routeInput.geographicPoints,
                    personIdentifier = routeInput.personIdentifier,
                    dateCreated = routeInput.dateCreated,
                    duration = routeInput.duration,
                    categories = routeInput.categories.map { CategoryRequest(it.name) }
            )
        }
    }
}