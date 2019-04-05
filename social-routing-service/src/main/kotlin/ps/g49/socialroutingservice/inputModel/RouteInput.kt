package ps.g49.socialroutingservice.inputModel

import ps.g49.socialroutingservice.model.Point

data class RouteInput (
        val location : String,
        val name : String,
        val description : String,
        val points : List<Point>, // json in the db
        val personIdentifier: Int
)