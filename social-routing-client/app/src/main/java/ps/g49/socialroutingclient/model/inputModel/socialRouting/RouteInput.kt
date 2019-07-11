package ps.g49.socialroutingclient.model.inputModel.socialRouting

import com.fasterxml.jackson.annotation.JsonProperty

data class RouteInput (
    var identifier: Int,
    val name: String,
    var rating: Double,
    @JsonProperty("route_url")
    val routeUrl: String
)