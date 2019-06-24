package ps.g49.socialroutingservice.models.outputModel

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SimplifiedRouteCollectionOutput(
        val next : String ? = null,
        val routes : List<SimplifiedRouteOutput>
)