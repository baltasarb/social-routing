package ps.g49.socialroutingservice.mappers.modelMappers

import ps.g49.socialroutingservice.models.domainModel.Point
import java.util.*

data class DetailedRouteDB (
        var identifier: Int,
        val location: String,
        val name: String,
        val description: String,
        var rating: Double,
        var duration: Long,
        var dateCreated: Date,
        val points: List<Point>,
        val personIdentifier: Int
)

data class SimplifiedRoute (
        var identifier: Int,
        val name: String,
        var rating: Double,
        val personIdentifier: Int
)

data class RoutePost(
        val location: String,
        val name: String,
        val description: String,
        val points: List<Point>,
        val personIdentifier: Int
)

data class RouteUpdate(
        var identifier: Int,
        val location: String,
        val name: String,
        val description: String,
        var rating: Double,
        var duration: Long,
        var dateCreated: Date,
        val points: List<Point>,
        val personIdentifier: Int
)