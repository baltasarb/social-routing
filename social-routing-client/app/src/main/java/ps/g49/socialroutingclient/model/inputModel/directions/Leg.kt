package ps.g49.socialroutingclient.model.inputModel.directions

import ps.g49.socialroutingclient.model.Point


data class Leg(
    val distance: PairValue,
    val duration: PairValue,
    val end_address: String,
    val end_location: Point,
    val start_address: String,
    val start_location: Point
)