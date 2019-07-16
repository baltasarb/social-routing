package ps.g49.socialroutingservice.models.inputModel

import ps.g49.socialroutingservice.models.domainModel.GeographicPoint
import ps.g49.socialroutingservice.models.domainModel.PointOfInterest
import java.util.*

data class RouteInput(
        val location: String,
        val name: String,
        val description: String,
        val points: List<GeographicPoint>,
        val categories: List<CategoryInput>,
        val isCircular: Boolean,
        val isOrdered: Boolean,
        val pointsOfInterest: List<PointOfInterest>?,
        val imageReference: String,
        var rating: Double? = null, //might be used on an UPDATE but not on a create
        var duration: String, // short, medium , long
        var dateCreated: Date? = null //might be used on an UPDATE but not on a create
)