package ps.g49.socialroutingclient.model.inputModel.google.directions

import ps.g49.socialroutingclient.model.domainModel.Point

data class Leg(
    val distance: PairValue,
    val duration: PairValue,
    val end_address: String,
    val end_location: Point,
    val start_address: String,
    val start_location: Point,
    val steps: List<Step>,
    val traffic_speed_entry: List<String>,
    val via_waypoint: List<String>
)

data class Step(
    val distance: PairValue,
    val duration: PairValue,
    val end_location: Point,
    val html_instructions: String,
    val polyline: Overview_Polyline,
    val start_location: Point,
    val travel_mode: String
)