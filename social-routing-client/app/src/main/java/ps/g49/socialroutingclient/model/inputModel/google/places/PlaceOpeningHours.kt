package ps.g49.socialroutingclient.model.inputModel.google.places

import com.fasterxml.jackson.annotation.JsonProperty

data class PlaceOpeningHours (
    @JsonProperty("open_now")
    val openNow: Boolean
)
