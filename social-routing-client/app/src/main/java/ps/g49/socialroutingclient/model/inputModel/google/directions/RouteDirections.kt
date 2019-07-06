package ps.g49.socialroutingclient.model.inputModel.google.directions

import ps.g49.socialroutingclient.model.inputModel.google.geocoding.Bound

data class RouteDirections(
    val summary: String,
    val overview_polyline: Overview_Polyline,
    val warnings: List<String>?,
    val waypoint_order: List<String>?,
    val bounds: Bound,
    val copyrights: String,
    val fare: Fare?,
    val legs: List<Leg>?
)