package ps.g49.socialroutingclient.model.inputModel.socialRouting

import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingclient.model.Point
import java.util.*

data class RouteDetailedInput(
    val identifier : Int,
    val location : String,
    val name : String,
    val description : String,
    val rating : Double,
    val duration : Int,
    val dateCreated : Date,
    val points : List<Point>,
    val categories : List<CategoryInput>,
    @JsonProperty("owner_url")
    val ownerUrl : String
)
