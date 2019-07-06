package ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse

import com.fasterxml.jackson.annotation.JsonProperty

data class ReverseGeoCodingResponse (
    @JsonProperty("plus_code")
    val plusCode: PlusCode,
    val results: List<ReverseResult>,
    val status: String
)