package ps.g49.socialroutingservice.models.domainModel

import java.util.*

data class Route(
        var identifier: Int? = null, // created by db
        val location: String? = null,
        val name: String,
        val description: String? = null,
        var rating: Double? = null, // created by db
        var duration: String,// short, medium, long
        var dateCreated: Date? = null, // created by db
        val points: List<GeographicPoint>? = null,
        val personIdentifier: Int,
        var categories : List<Category>? = null,
        val elevation : Double? = null,
        val isCircular: Boolean,
        val isOrdered: Boolean,
        val pointsOfInterest: List<PointOfInterest>,
        val imageReference: String
)