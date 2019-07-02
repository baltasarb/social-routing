package ps.g49.socialroutingservice.models.inputModel

import ps.g49.socialroutingservice.models.domainModel.GeographicPoint
import java.util.*

data class RouteInput (
        val location : String,
        val name : String,
        val description : String,
        val personIdentifier: Int,
        val geographicPoints: List<GeographicPoint>,
        val categories : List<CategoryInput>,
        var rating: Double? = null, //might be used on an UPDATE but not on a create
        var duration: Int? = null, //might be used on an UPDATE but not on a create
        var dateCreated: Date? = null //might be used on an UPDATE but not on a create
)