package ps.g49.socialroutingservice.models.inputModel

import ps.g49.socialroutingservice.models.domainModel.Point
import java.util.*

data class RouteInput (
        val identifier : Int? = null,//might be used on an update but not on a create
        val location : String,
        val name : String,
        val description : String,
        val personIdentifier: Int,
        val points: List<Point>,
        var rating: Double? = null, //might be used on an update but not on a create
        var duration: Long? = null, //might be used on an update but not on a create
        var dateCreated: Date? = null //might be used on an update but not on a create
)