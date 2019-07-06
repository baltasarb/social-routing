package ps.g49.socialroutingclient.model.inputModel.google.directions

data class GeocodedWaypoint(
    val geocoder_status: String,
    val partial_match: Boolean?,
    val place_id: String,
    val types: List<String>
)