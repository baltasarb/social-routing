package ps.g49.socialroutingclient.model.inputModel.google.geocoding

data class GeoCodingResponse(
    val results: List<Result>,
    val status: String
)