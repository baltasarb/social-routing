package ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse

import com.fasterxml.jackson.annotation.JsonProperty

data class PlusCode (
    @JsonProperty("compound_code")
    val compoundCode: String,
    @JsonProperty("global_code")
    val globalCode: String
)