package ps.g49.socialroutingclient.model.outputModel

import ps.g49.socialroutingclient.model.domainModel.Point

data class RouteOutput (
    val location : String,
    val name : String,
    val description : String,
    val points: List<Point>,
    val categories : List<CategoryOutput>,
    val isCircular : Boolean,
    val isOrdered : Boolean,
    val pointsOfInterest : List<PointOfInterestOutput>,
    val imageReference : String
)