package ps.g49.socialroutingservice.models.outputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class SimplifiedRouteOutput(
        var identifier: Int,
        val name: String,
        var rating: Double,
        var imageReference: String,
        @JsonProperty("route_url")
        val routeUrl: String
)