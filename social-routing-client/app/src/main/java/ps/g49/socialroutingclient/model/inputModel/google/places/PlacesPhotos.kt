package ps.g49.socialroutingclient.model.inputModel.google.places

import com.fasterxml.jackson.annotation.JsonProperty

data class PlacesPhotos (
    val height: Int,
    @JsonProperty("html_attributions")
    val htmlAttributions: List<String>,
    @JsonProperty("photo_reference")
    val photoReference: String,
    val width: Int
)
