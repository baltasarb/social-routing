package ps.g49.socialroutingservice.models.outputModel

data class SimplifiedRouteOutput(
        var identifier: Int,
        val name: String,
        var rating: Double,
        val routeUrl: String
)