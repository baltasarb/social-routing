package ps.g49.socialroutingservice.models.outputModel

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingservice.models.domainModel.GeographicPoint
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RouteOutput(
        val identifier : Int,
        val location : String,
        val name : String,
        val description : String,
        val rating : Double,
        val duration : Int,
        val dateCreated : Date,
        val geographicPoints : List<GeographicPoint>,
        val categories : List<CategoryOutput>,
        @JsonProperty("route_accumulated_elevation")
        val elevation : Double? = null,
        @JsonProperty("owner_url")
        val ownerUrl : String
)