package ps.g49.socialroutingservice.models.outputModel

import ps.g49.socialroutingservice.models.domainModel.Point
import java.util.*

data class RouteOutput(
        val identifier : Int,
        val location : String,
        val name : String,
        val description : String,
        val rating : Double,
        val duration : Int,
        val dateCreated : Date,
        val points : List<Point>,
        val categories : List<CategoryOutput>,
        val ownerUrl : String

)