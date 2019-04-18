package ps.g49.socialroutingservice.models.inputModel

import ps.g49.socialroutingservice.models.domainModel.Point

data class RouteInput (
        val location : String,
        val name : String,
        val description : String,
        val points : List<Point>, // json in the db
        val personIdentifier: Int
)