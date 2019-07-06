package ps.g49.socialroutingclient.model.inputModel.google.places

import com.fasterxml.jackson.annotation.JsonProperty

data class PlacesResponse(
    @JsonProperty("html_attributions")
    val htmlAttributions: List<String>,
    @JsonProperty("next_page_token")
    val nextPageToken: String?,
    val results: List<PlacesResult>
)