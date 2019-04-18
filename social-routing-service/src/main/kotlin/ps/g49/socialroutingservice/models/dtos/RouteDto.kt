package ps.g49.socialroutingservice.models.dtos

import ps.g49.socialroutingservice.models.domainModel.Point

data class RouteDto(
        var location: String,
        var name: String,
        var description: String,
        var points: List<Point>,
        var personIdentifier: Int
)