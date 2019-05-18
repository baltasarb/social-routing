package ps.g49.socialroutingservice.models.requests

import ps.g49.socialroutingservice.models.domainModel.Point
import java.util.*

data class RouteRequest(
        var identifier: Int? = null,
        var location: String,
        var name: String,
        var description: String,
        var points: List<Point>,
        var personIdentifier: Int,
        var rating: Double? = null,
        var dateCreated: Date? = null,
        var duration : Int? = null,
        var categories : List<CategoryRequest>
)