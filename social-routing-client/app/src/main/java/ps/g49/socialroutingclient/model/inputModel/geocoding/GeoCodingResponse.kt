package ps.g49.socialroutingclient.model.inputModel.geocoding

data class GeoCodingResponse(
    val results: List<Result>,
    val status: String
)