package ps.g49.socialroutingclient.model.domainModel

data class Route (
    val existingRoute: Boolean,
    val routeUrl: String?,
    val placeId: String,
    val points: List<Point>,
    val pointsOfInterest: List<BasicPointOfInterest>,
    val isCircular: Boolean
)