package ps.g49.socialroutingservice.models.domainModel

import java.util.*

data class Route(
        var identifier: Int? = null, // created by db
        val location: String? = null,
        val name: String,
        val description: String? = null,
        var rating: Double? = null, // created by db
        var duration: Int? = null,// TODO created by service
        var dateCreated: Date? = null, // created by db
        val points: List<Point>? = null,
        val personIdentifier: Int,
        var categories : List<Category>? = null
)