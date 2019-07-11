package ps.g49.socialroutingclient.model.inputModel.socialRouting

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingclient.model.domainModel.BasicPointOfInterest
import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Point
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RouteDetailedInput(
    val identifier : Int,
    val location : String,
    val name : String,
    val description : String,
    val rating : Double,
    val duration : String,
    val dateCreated : Date,
    val points : List<Point>,
    val categories : List<Category>,
    val pointsOfInterest : List<BasicPointOfInterest>,
    val ordered : Boolean,
    val circular : Boolean,
    val imageReference : String,
    @JsonProperty("owner_url")
    val ownerUrl : String
)
