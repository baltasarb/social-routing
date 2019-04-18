package ps.g49.socialroutingservice.models.inputModel

import ps.g49.socialroutingservice.models.domainModel.Point
import java.util.*

data class RouteInput (
        val location : String,
        val name : String,
        val description : String,
        val personIdentifier: Int,
        val points: List<Point>,
        var rating: Double? = null,
        var duration: Long? = null,
        var dateCreated: Date? = null
)