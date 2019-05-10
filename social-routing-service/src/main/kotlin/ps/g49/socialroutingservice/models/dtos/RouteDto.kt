package ps.g49.socialroutingservice.models.dtos

import ps.g49.socialroutingservice.models.domainModel.Point
import java.util.*

data class RouteDto(
        var identifier: Int? = null,
        var location: String,
        var name: String,
        var description: String,
        var points: List<Point>,
        var personIdentifier: Int,
        var rating: Double? = null,
        var dateCreated: Date? = null,
        var duration : Int? = null,
        var categories : List<CategoryDto>
)