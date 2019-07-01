package ps.g49.socialroutingclient.model.inputModel.socialRouting

import com.fasterxml.jackson.annotation.JsonProperty

data class PersonInput(
    val identifier: Int,
    val rating: Double,
    @JsonProperty("routes_url")
    val routesUrl: String
)