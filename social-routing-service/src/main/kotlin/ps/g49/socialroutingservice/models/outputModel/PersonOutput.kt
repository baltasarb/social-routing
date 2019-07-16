package ps.g49.socialroutingservice.models.outputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class PersonOutput(
        val identifier: Int,
        val rating: Double,
        @JsonProperty("routes_url")
        val routesUrl: String
)