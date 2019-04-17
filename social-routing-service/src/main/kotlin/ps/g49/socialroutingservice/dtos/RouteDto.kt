package ps.g49.socialroutingservice.dtos

import ps.g49.socialroutingservice.inputModel.RouteInput
import ps.g49.socialroutingservice.model.Point

data class RouteDto(
        var location: String,
        var name: String,
        var description: String,
        var points: List<Point>,
        var personIdentifier: Int
)