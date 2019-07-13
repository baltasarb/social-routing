package ps.g49.socialroutingclient.model.outputModel

import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Point

data class RouteOutput (
    val location : String,
    val name : String,
    val description : String,
    val points: List<Point>,
    val categories : List<Category>,
    val isCircular : Boolean,
    val isOrdered : Boolean,
    val pointsOfInterest : List<PointOfInterestOutput>?,
    val imageReference : String,
    val duration: Int
)