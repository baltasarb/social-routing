package ps.g49.socialroutingclient.model.inputModel.google.places

import com.fasterxml.jackson.annotation.JsonProperty

data class PlaceDetailsResponse (
    @JsonProperty("html_attributions")
    val htmlAttributions: List<String>,
    val results: PlaceDetailsResult
)