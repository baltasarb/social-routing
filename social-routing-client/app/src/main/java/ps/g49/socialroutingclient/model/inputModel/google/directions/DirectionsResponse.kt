package ps.g49.socialroutingclient.model.inputModel.google.directions

data class DirectionsResponse(
    val status: String,
    val geocoded_waypoints: List<GeocodedWaypoint>?,
    val routes: List<RouteDirections>?,
    val available_travel_modes: List<String>?
)