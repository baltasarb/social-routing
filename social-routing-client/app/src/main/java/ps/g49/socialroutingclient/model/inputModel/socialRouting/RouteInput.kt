package ps.g49.socialroutingclient.model.inputModel.socialRouting

import com.fasterxml.jackson.annotation.JsonProperty

data class RouteInput (
    val identifier: Int,
    val name: String,
    val rating: Double,
    @JsonProperty("route_url")
    val routeUrl: String
)