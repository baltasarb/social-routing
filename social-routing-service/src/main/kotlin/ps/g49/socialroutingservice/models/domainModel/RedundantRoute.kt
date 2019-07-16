package ps.g49.socialroutingservice.models.domainModel

import java.util.*

/*
* used to map the result from the database into a single route objects
* because of required table joins redundant information is returned and
* needs to be filtered
* */
data class RedundantRoute (
        var identifier: Int,
        val location: String,
        val name: String,
        val description: String,
        var rating: Double,
        var duration: String,
        var dateCreated: Date,
        val points: List<GeographicPoint>? = null,
        val personIdentifier: Int,
        var category : Category,
        val elevation : Double? = null,
        val isCircular: Boolean,
        val isOrdered: Boolean,
        val pointOfInterest: PointOfInterest,
        val imageReference: String
)