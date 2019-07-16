package ps.g49.socialroutingservice.models.outputModel

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingservice.models.domainModel.GeographicPoint
import ps.g49.socialroutingservice.models.domainModel.PointOfInterest
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RouteOutput(
        val identifier: Int,
        val location: String,
        val name: String,
        val description: String,
        val rating: Double,
        val duration: String,
        val dateCreated: Date,
        val points: List<GeographicPoint>,
        val categories: List<CategoryOutput>,
        val pointsOfInterest: List<PointOfInterest>,
        val ordered: Boolean,
        val circular: Boolean,
        val imageReference: String,
        @JsonProperty("owner_url")
        val ownerUrl: String
)